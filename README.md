# Ray
Ray is a light-weight dependency injection container implementation. It was created for research purposes 
and does not support most of the features, that are present in Spring, Tapestry or PicoContainer. However, several applications were created using Ray.

Ray project is separated into modules to make the result jar size as light as possible.

- ray-core (core API)
- ray-annotation (annotation context implementation)
- ray-json (json context implementation)
- ray-xml (xml context implementation)
- ray-txt-common (common tools for txt based contexts)

The minimun set of libraries you need is ray-core + any context implementation module.

Supported features:

- Context of managed components (annotations, xml, json)
- Singleton and Prototype scopes
- As-Is component injection
- PostConstruct and PreDestroy lifecycle hooks
- Profiles for different deployment environments


Examples:
-

**Annotation based context**

MyBean.java
    
    package com.myproject.core;

    import org.skywind.ray.meta.ManagedComponent;
    
    @ManagedComponent("greeter");
    public class Greeter {

       @Inject
       private OtherBean otherBean;

       public void sayHello() {
           System.out.println("Hello, " + otherBean.getName());
       }
    }

Main.java


    import org.skywind.ray.core.AnnotationContext;
    import org.skywind.ray.core.BeanContainer;
    import org.skywind.ray.core.Context;
        
    public class Main {

       public static void main(String[] args) {
           Context context = new AnnotationContext("com.myproject.core");
           context.refresh();

           BeanContainer beanContainer = context.getBeanContainer();
           Greeter greeter = beanContainer.getBean("greeter");
           greeter.sayHello();
       }
    }


**JSON based context**

context.json

    {
        "beans" : [
            {
                "id" : "greeter",
                "class" : "com.myproject.core.Greeter",
                "scope" : "singleton",
                "autowired" : ["otherBean"]
            },
            {
                "id" : "otherBean",
                "class" : "com.myproject.core.OtherBean",
                "scope" : "singleton"                
            }            
        ]
    }

Main.java

    import org.skywind.ray.core.JsonContext;
    import org.skywind.ray.core.BeanContainer;
    import org.skywind.ray.core.Context;

    public class Main {

       public static void main(String[] args) {
           Context context = new JsonContext("classpath:context.json");
           context.refresh();

           BeanContainer beanContainer = context.getBeanContainer();
           Greeter greeter = beanContainer.getBean(Greeter.class);
           greeter.sayHello();
       }
    }
    
**XML based context**

context.xml

    <context xmlns="ray-context">
       <bean id="greeter" 
             class="com.myproject.core.Greeter" 
             autowired="otherBean"/>

       <bean id="otherBean" 
             class="com.myproject.core.OtherBean"/>       
    </context>


Main.java

    import org.skywind.ray.core.XmlContext;
    import org.skywind.ray.core.BeanContainer;
    import org.skywind.ray.core.Context;

    public class Main {

       public static void main(String[] args) {
           Context context = new XmlContext("classpath:context.xml");
           context.refresh();

           BeanContainer beanContainer = context.getBeanContainer();
           Greeter greeter = beanContainer.getBean(Greeter.class);
           greeter.sayHello();
       }
    }
