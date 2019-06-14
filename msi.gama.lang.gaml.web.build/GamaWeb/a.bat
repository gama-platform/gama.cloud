net stop Tomcat9
del "C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\*.*" /F /Q
del "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\GamaWeb.war" /F
copy "e:\git\gama.cloud\msi.gama.lang.gaml.web.build\GamaWeb\target\GamaWeb.war" "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps"
net start Tomcat9