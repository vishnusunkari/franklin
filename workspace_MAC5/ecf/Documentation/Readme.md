Changes to be Made For Deploying into QA/Dev/Prod
=================================================

1.  Rename the appropriate application.properties.xx to application.properties
2.  Uncomment/comment inside **WEB-INF/spring-security.xml**,   *src/spring-ldap.xml* to 
    configure to dev/prod ldap settings
3.  Run build.xml and get the ecf.war file from folder named: 'dist'
 