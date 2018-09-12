start java -jar .\tomcat_launcher.jar
@echo "Wait 10s to startup"
timeout 10 /nobreak
start "" http://localhost:8080/GamaWeb/texteditor