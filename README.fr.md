![](https://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=gru-library-identityquality-deploy)
[![Alerte](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Alibrary-identityquality&metric=alert_status)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Alibrary-identityquality)
[![Line of code](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Alibrary-identityquality&metric=ncloc)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Alibrary-identityquality)
[![Coverage](https://dev.lutece.paris.fr/sonar/api/project_badges/measure?project=fr.paris.lutece.plugins%3Alibrary-identityquality&metric=coverage)](https://dev.lutece.paris.fr/sonar/dashboard?id=fr.paris.lutece.plugins%3Alibrary-identityquality)

# Library Identityquality

## Introduction

Cette librairie fournit des services pour communiquer avec l'API REST d'Identity Store Quality.

Pour plus d'information, la documentation de la librairie est disponible sur le [wiki officiel](https://lutece.paris.fr/support/wiki/gru-library-identityquality.html).

## Services

Le service principal est `fr.paris.lutece.plugins.identityquality.v3.web.service.IdentityQualityService` , qui fournit les m&eacute;thodes permettant d'appeler Identity Store Quality.

Ces m&eacute;thodes sont implémentées par le service de transport REST `fr.paris.lutece.plugins.identityquality.v3.web.rs.service.IdentityQualityTransportRest` .

Il requiert une impl&eacute;mentation de `fr.paris.lutece.plugins.identityquality.v3.web.service.IHttpTransportProvider` pour d&eacute;finir le transport HTTP. Deux impl&eacute;mentations de cette interface sont pr&eacute;sentes dans cette librairie :
 
*  `fr.paris.lutece.plugins.identityquality.v3.web.rs.service.HttpAccessTransport` , qui utilise des requ&ecirc;tes simples
*  `fr.paris.lutece.plugins.identityquality.v3.web.rs.service.HttpApiManagerAccessTransport` , qui utilise l'ApiManager pour s&eacute;curiser les requ&ecirc;tes&agrave; l'API (en utilisant des tokens)


Ces deux impl&eacute;mentations ont besoin de la d&eacute;finition de l'URL vers le service d'Identity Store.
 
*  `apiEndPointUrl` , l'URL de l'endpoint de l'Identity store.


Pour utiliser `HttpApiManagerAccessTransport` , deux paramètres supplémentaires sont requis:
 
*  `accessManagerEndPointUrl` , l'URL de l'endpoint de l'API manager pour récupérer le token.
*  `accessManagerCredentials` , les credentials permettant d'accéder à l'API manager pour récupérer le token.


## Configuration en utilisant le contexte Spring

Premi&egrave;rement, d&eacute;finir le bean pour le transport HTTP&agrave; utiliser :
 
* renseigner la propri&eacute;t&eacute; pour l'URL pointant vers le service d'Identity Store
* renseigner les autres propri&eacute;t&eacute;s si le transport HTTP `HttpApiManagerAccessTransport` est utilis&eacute;


Ensuite, d&eacute;finir le bean pour le transport REST `IdentityTransportRest` :
 
* comme argument de constructeur, faire r&eacute;f&eacute;rence au bean pour le transport HTTP


Ensuite, d&eacute;finir le bean pour le service `IdentityService` :
 
* comme argument de constructeur, faire r&eacute;f&eacute;rence au bean pour le transport REST


Voici un exemple de configuration Spring avec le transport HTTP `HttpAccessTransport` :
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


Voici un exemple de configuration Spring avec le transport HTTP `HttpApiManagerAccessTransport` :
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


Vous pouvez maintenant accéder au bean comme suit:
```

import fr.paris.lutece.plugins.identityquality.v3.web.service.IdentityQualityService;
...
private final IdentityQualityService _serviceQuality = SpringContextService.getBean( "identity.identityService" );
                        
```


## Configuration in Java code

Le service peut&ecirc;tre directement cr&eacute;&eacute; dans le code Java. Voici un exemple avec le transport HTTP `HttpApiManagerAccessTransport` (le m&ecirc;me m&eacute;canisme peut&ecirc;tre appliqu&eacute; pour le transport HTTP `HttpAccessTransport` ).

Premi&egrave;rement, d&eacute;finir les cl&eacute;s suivantes dans un fichier de propri&eacute;t&eacute;s :
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