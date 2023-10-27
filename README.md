![](https://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=gru-library-identityquality-deploy)
[![Alerte](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Alibrary-identityquality&metric=alert_status)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Alibrary-identityquality)
[![Line of code](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Alibrary-identityquality&metric=ncloc)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Alibrary-identityquality)
[![Coverage](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Alibrary-identityquality&metric=coverage)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Alibrary-identityquality)

# Library Identityquality

## Introduction

This library provides services to communicate with Identity Store quality REST API.

Further information can be found on the [official wiki](https://lutece.paris.fr/support/wiki/gru-library-identityquality.html).

## Services

The main service is `fr.paris.lutece.plugins.identityquality.v3.web.service.IdentityQualityService` which provides methods to call Identity Store quality.

These methods are implemented in the rest transport `fr.paris.lutece.plugins.identityquality.v3.web.rs.service.IdentityQualityTransportRest` .

It requires an implementation of `fr.paris.lutece.plugins.identityquality.v3.web.service.IHttpTransportProvider` to define the HTTP transport. Two implementations of this interface are provided in the library :
 
*  `fr.paris.lutece.plugins.identityquality.v3.web.rs.service.HttpAccessTransport` , which uses simple requests
*  `fr.paris.lutece.plugins.identityquality.v3.web.rs.service.HttpApiManagerAccessTransport` , which calls endpoints through an ApiManager in order to secure requests to the API (by using tokens)


Both implementations require URL definition of the Identity Store service end point.
 
*  `apiEndPointUrl` , the URL to the Identity store endpoint.


When using `HttpApiManagerAccessTransport` , two extra parameters are required:
 
*  `accessManagerEndPointUrl` , the URL to the API manager token endpoint.
*  `accessManagerCredentials` , the credentials to access the API manager token endpoint.


## Configuration using Spring context

First, define the bean for the HTTP transport you want to use:
 
* set the property for the URL of the Identity Store quality service end point
* set other properties if using the HTTP transport `HttpApiManagerAccessTransport` 


Then, define the bean for the rest transport `IdentityQualityTransportRest` :
 
* as a constructor argument, refer to the bean for HTTP transport


Then, define the bean for the service `IdentityQualityService` :
 
* as a constructor argument, refer to the bean for rest transport


Here is an example of Spring configuration with the HTTP transport `HttpAccessTransport` :
```

                            <!-- library identitystore -->
                            <!-- IHttpTransportProvider declarations -->
                            <bean id="identitystore.httpAccessTransport" class="fr.paris.lutece.plugins.identityquality.v3.web.rs.service.HttpAccessTransport" >
                            <property name="apiEndPointUrl">
                            <value>${myplugin.identitystore.apiEndPointUrl}</value>
                            </property>
                            </bean>

                            <bean id="identity.restTransport.httpAccess" class="fr.paris.lutece.plugins.identityquality.v3.web.rs.service.IdentityQualityTransportRest">
                            <constructor-arg ref="identitystore.httpAccessTransport"/>
                            </bean>

                            <!-- IdentityService impl -->
                            <bean id="identity.identityService" class="fr.paris.lutece.plugins.identityquality.v3.web.service.IdentityQualityService">
                            <constructor-arg ref="identity.restTransport.httpAccess"/>
                            </bean>
                        
```


Here is an example of Spring configuration with the HTTP transport `HttpApiManagerAccessTransport` :
```

                            <!-- library identitystore -->
                            <!-- IHttpTransportProvider declarations -->
                            <bean id="identitystore.httpAccessTransport" class="fr.paris.lutece.plugins.identityquality.v3.web.rs.service.HttpApiManagerAccessTransport">
                            <property name="apiEndPointUrl">
                            <value>${myplugin.identitystore.ApiEndPointUrl}</value>
                            </property>
                            <property name="accessManagerEndPointUrl">
                            <value>${myplugin.identitystore.accessManagerEndPointUrl}</value>
                            </property>
                            <property name="accessManagerCredentials">
                            <value>${myplugin.identitystore.accessManagerCredentials}</value>
                            </property>
                            </bean>

                            <bean id="identity.restTransport.httpAccess" class="fr.paris.lutece.plugins.identityquality.v3.web.rs.service.IdentityQualityTransportRest">
                            <constructor-arg ref="identitystore.httpAccessTransport"/>
                            </bean>

                            <!-- IdentityService impl -->
                            <bean id="identity.identityService" class="fr.paris.lutece.plugins.identityquality.v3.web.service.IdentityQualityService">
                            <constructor-arg ref="identity.restTransport.httpAccess"/>
                            </bean>
                        
```


You can finally access your bean in the java code as follows:
```

                            import fr.paris.lutece.plugins.identityquality.v3.web.service.IdentityQualityService;
                            ...
                            private final IdentityQualityService _serviceQuality = SpringContextService.getBean( "identity.identityService" );
                        
```


## Configuration in Java code

The service can directly be created in the Java code. Here is an example with the HTTP transport `HttpApiManagerAccessTransport` (the same mechanism can be applied for the HTTP transport `HttpAccessTransport` ).

First, define the following keys in a properties file:
```

                            myplugin.identitystore.ApiEndPointUrl=http://mydomain.com/url/to/apimanager/api/identitystore
                            myplugin.identitystore.accessManagerEndPointUrl=http://mydomain.com/url/to/apimanager/token
                            myplugin.identitystore.accessManagerCredentials=your_private_key
                        
```


Then, add the following code in the Java code:
```

                            ...
                            private static final String PROPERTY_GRU_ENDPOINT_IDENTITYQUALITY = "myplugin.identitystore.ApiEndPointUrl";
                            private static final String PROPERTY_GRU_ENDPOINT_TOKEN = "myplugin.identitystore.accessManagerEndPointUrl";
                            private static final String PROPERTY_GRU_APIMANAGER_CREDENTIALS = "myplugin.identitystore.accessManagerCredentials";
                            ...
                            final IdentityTransportApiManagerRest apiManagerTransport = new IdentityTransportApiManagerRest(  );
                            apiManagerTransport.setIdentityStoreEndPoint( AppPropertiesService.getProperty( PROPERTY_GRU_ENDPOINT_IDENTITYQUALITY ) );
                            apiManagerTransport.setApiManagerEndPoint( AppPropertiesService.getProperty( PROPERTY_GRU_ENDPOINT_TOKEN ) );
                            apiManagerTransport.setApiManagerCredentials( AppPropertiesService.getProperty( PROPERTY_GRU_APIMANAGER_CREDENTIALS ) );

                            final IdentityQualityService identityService = new IdentityQualityService( apiManagerTransport );
                            ...
                        
```



[Maven documentation and reports](https://dev.lutece.paris.fr/plugins/library-identityquality/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*