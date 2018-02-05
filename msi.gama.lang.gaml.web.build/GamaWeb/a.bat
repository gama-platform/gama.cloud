net stop Tomcat8
del "C:\Program Files\Apache Software Foundation\Tomcat 8.5\logs\*.*" /F /Q
del "C:\Program Files\Apache Software Foundation\Tomcat 8.5\webapps\GamaWeb.war" /F
copy "C:\GitHub\gama.cloud\msi.gama.lang.gaml.web.build\GamaWeb\target\GamaWeb.war" "C:\Program Files\Apache Software Foundation\Tomcat 8.5\webapps"
net start Tomcat8