# ray
Ray is a light-weight dependency container implementation. It was created for research purposes 
and does not support most of the features, that are present in Spring or Piko. However, several applications were
created using Ray.

Ray creates annotation-based context from a list of packages using Reflections library 
(http://mvnrepository.com/artifact/org.reflections/reflections) to found managed components.

Supported features:
- Context of managed components
- Singleton and Prototype scopes
- As-Is component injection
- PostConstruct annotation for methods
