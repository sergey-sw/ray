package com.intelli.ray.core;

import com.intelli.ray.util.ReflectionHelper;
import org.reflections.Reflections;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Author: Sergey42
 * Date: 14.11.13 21:11
 */
public class AnnotationBasedContext implements Context {

    protected String[] scanLocations;

    protected Map<String, BeanDefinition> definitionByName = new HashMap<>();
    protected Map<Class, BeanDefinition> definitionByClass = new HashMap<>();
    protected Map<String, Class> clazzByName = new HashMap<>();

    protected boolean muteRegistrationLog = true;
    protected boolean started = false;

    /**
     * Builds the context by annotation scanning in specified locations.
     * Location order provides overriding.
     *
     * @param scanLocations  - package paths
     * @throws ContextInitializationException is thrown in cases of invalid paths.
     */
    public AnnotationBasedContext(String... scanLocations) throws ContextInitializationException {
        this(true, scanLocations);
    }

    public AnnotationBasedContext(boolean muteRegistrationLog, String... scanLocations) throws ContextInitializationException {
        this.scanLocations = scanLocations;
        this.muteRegistrationLog = muteRegistrationLog;
        init();
        injectSingletonDependencies();
        invokePostConstruct();
        started = true;
    }

    public boolean isStarted() {
        return started;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<T> beanClass) {
        BeanDefinition beanDefinition = definitionByClass.get(beanClass);

        if (beanDefinition == null) {
            throw new BeanNotFoundException(beanClass);
        }

        if (Scope.SINGLETON == beanDefinition.scope) {
            return (T) beanDefinition.singletonInstance;
        } else {
            throw new BeanInstantiationException(
                    String.format("Failed to instantiate bean of class %s. Expected singleton scope.", beanClass.getName()));
        }
    }

    @SuppressWarnings({"unchecked", "unused"})
    @Override
    public <T> T getBean(String name) {
        BeanDefinition beanDefinition = definitionByName.get(name);

        if (beanDefinition == null) {
            throw new BeanNotFoundException(name);
        }

        if (Scope.SINGLETON == beanDefinition.scope) {
            return (T) beanDefinition.singletonInstance;
        } else {
            throw new BeanInstantiationException(
                    String.format("Failed to instantiate bean with name %s. Expected singleton scope.", name));
        }
    }

    @SuppressWarnings({"unchecked", "unused"})
    @Override
    public <T> T getPrototype(String name) {
        BeanDefinition beanDefinition = definitionByName.get(name);

        if (beanDefinition == null) {
            throw new BeanNotFoundException(name);
        }

        if (beanDefinition.scope != Scope.PROTOTYPE) {
            throw new BeanInstantiationException(
                    String.format("Error in %s prototype instantiation. Expected Prototype scope", name));
        }

        try {
            T prototypeInstance = (T) beanDefinition.beanClass.newInstance();
            doPrototypeInject(prototypeInstance, beanDefinition);
            invokePostConstructMethods(prototypeInstance);
            return prototypeInstance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new BeanInstantiationException(e);
        }
    }

    @SuppressWarnings({"unchecked", "unused"})
    @Override
    public <T> T getManagedConstructorBean(Class<T> beanClass, Object... params) {
        BeanDefinition beanDefinition = definitionByClass.get(beanClass);

        if (beanDefinition == null) {
            throw new BeanNotFoundException(beanClass);
        }

        if (beanDefinition.scope != Scope.PROTOTYPE) {
            throw new BeanInstantiationException(
                    String.format("Error in %s bean instantiation. Expected Prototype scope", beanClass.getName()));
        }

        try {
            T prototypeInstance = (T) beanDefinition.managedConstructor.newInstance(params);
            doPrototypeInject(prototypeInstance, beanDefinition);
            invokePostConstructMethods(prototypeInstance);
            return prototypeInstance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Collection<T> getBeansByType(Class<T> beanClass) {
        List<T> beansOfType = new ArrayList<>();
        for (BeanDefinition definition : definitionByClass.values()) {
            if (definition.scope == Scope.SINGLETON && beanClass.isAssignableFrom(definition.beanClass)) {
                beansOfType.add((T) definition.singletonInstance);
            }
        }
        return beansOfType;
    }

    @SuppressWarnings("unused")
    @Override
    public void printConfiguredBeans(OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            outputStream = System.out;
        }

        outputStream.write("Registered beans :\n".getBytes());
        for (Map.Entry<String, Class> entry : clazzByName.entrySet()) {
            String string = entry.getKey() + " : " + entry.getValue().getName() + "\n";
            outputStream.write(string.getBytes());
        }
    }

    protected void init() throws ContextInitializationException {
        List<Class<?>> classes = new ArrayList<>();
        Reflections reflections = new Reflections(scanLocations);
        classes.addAll(reflections.getTypesAnnotatedWith(ManagedComponent.class));
        for (Class clazz : classes) {
            if (clazz.isAnnotationPresent(ManagedComponent.class)) {
                try {
                    ManagedComponent ann = (ManagedComponent) clazz.getAnnotation(ManagedComponent.class);
                    Scope scope = ann.scope();
                    Constructor managedConstructor = getManagedConstructor(clazz);
                    if (scope == Scope.SINGLETON && managedConstructor != null) {
                        throw new ContextInitializationException("Managed constructor is not supported in singletons");
                    }

                    BeanDefinition definition;
                    if (managedConstructor != null) {
                        definition = new BeanDefinition(scope, clazz, null, managedConstructor);
                    } else {
                        definition = new BeanDefinition(scope, clazz, clazz.newInstance(), null);
                    }

                    definitionByName.put(ann.name(), definition);
                    clazzByName.put(ann.name(), clazz);
                    definitionByClass.put(clazz, definition);
                    logRegistration(clazz, scope);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new ContextInitializationException(e);
                }
            }
        }
    }

    protected Constructor getManagedConstructor(Class clazz) {
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.getAnnotation(ManagedConstructor.class) != null) {
                return constructor;
            }
        }
        return null;
    }

    protected void injectSingletonDependencies() {
        for (Class clazz : definitionByClass.keySet()) {
            BeanDefinition definition = definitionByClass.get(clazz);
            if (definition.scope == Scope.PROTOTYPE) {
                continue;
            }

            Object beanInstance = definition.singletonInstance;
            Field[] fields = ReflectionHelper.getAllAnnotatedFields(beanInstance.getClass(), Inject.class);

            for (Field field : fields) {
                doInject(field, beanInstance, definition);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void doInject(Field field, Object beanInstance, BeanDefinition definition) {
        Class fieldClazz = field.getType();
        BeanDefinition fieldBeanDefinition = definitionByClass.get(fieldClazz);
        if (fieldBeanDefinition == null) {
            for (Class clazz : definitionByClass.keySet()) {
                if (fieldClazz.isAssignableFrom(clazz)) {
                    fieldBeanDefinition = definitionByClass.get(clazz);
                    break;
                }
            }
            if (fieldBeanDefinition == null) {
                throw new ContextInitializationException("Can not inject property '" + field.getName() + "' in bean " +
                        definition.beanClass.getName() + ", because property bean class " + fieldClazz.getName()
                        + " is not present in context.");
            }
        }
        try {
            boolean override = false;
            if (!field.isAccessible()) {
                field.setAccessible(true);
                override = true;
            }
            field.set(beanInstance, getBean(fieldBeanDefinition.beanClass));
            field.setAccessible(!override);
        } catch (IllegalAccessException e) {
            throw new BeanInstantiationException(e);
        }
    }

    protected void invokePostConstruct() {
        for (BeanDefinition beanDefinition : definitionByClass.values()) {
            if (beanDefinition.scope == Scope.SINGLETON) {
                try {
                    invokePostConstructMethods(beanDefinition.singletonInstance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new BeanInstantiationException("Failed to execute post construct methods of bean " +
                            beanDefinition.beanClass.getName(), e);
                }
            }
        }
    }

    protected void invokePostConstructMethods(Object component) throws InvocationTargetException, IllegalAccessException {
        List<Method> postConstructMethods = new ArrayList<>(4);
        List<String> methodNames = new ArrayList<>(4);
        Class clazz = component.getClass();
        while (clazz != Object.class) {
            Method[] classMethods = clazz.getDeclaredMethods();
            for (Method method : classMethods) {
                if (method.isAnnotationPresent(PostConstruct.class) && !methodNames.contains(method.getName())) {
                    postConstructMethods.add(method);
                    methodNames.add(method.getName());
                }
            }
            clazz = clazz.getSuperclass();
        }

        ListIterator<Method> iterator = postConstructMethods.listIterator(postConstructMethods.size());
        while (iterator.hasPrevious()) {
            Method method = iterator.previous();
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            method.invoke(component);
        }
    }

    protected void doPrototypeInject(Object prototype, BeanDefinition definition) {
        Class prototypeClass = definition.beanClass;
        Field[] fields = ReflectionHelper.getAllAnnotatedFields(prototypeClass, Inject.class);
        for (Field field : fields) {
            doInject(field, prototype, definition);
        }
    }

    protected void logRegistration(Class clazz, Scope scope) {
        if (!muteRegistrationLog) {
            System.out.println(String.format("Registered bean of class %s with scope %s", clazz.getName(), scope.getId()));
        }
    }

    protected static class BeanDefinition {
        protected Scope scope;
        protected Class beanClass;
        protected Object singletonInstance;
        protected Constructor managedConstructor;

        protected BeanDefinition(Scope scope, Class beanClass, Object singletonInstance, Constructor constructor) {
            this.scope = scope;
            this.beanClass = beanClass;
            this.singletonInstance = singletonInstance;
            this.managedConstructor = constructor;
        }
    }
}
