package msi.gama.core.web.editor;
import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
 
public class RunWarExample {
	 public static void main(String[] args) throws ServletException, LifecycleException {
	        Tomcat tomcat = new Tomcat();
	        tomcat.setBaseDir("D:\\www");
	        tomcat.setPort(8080);
	         
	        String contextPath = "/GamaWeb";     
	        String warFilePath = "D:\\www\\GamaWeb.war";

	        tomcat.getHost().setAppBase(".");
	         
	        tomcat.addWebapp(contextPath, warFilePath);
	         
	        tomcat.start();
	        tomcat.getServer().await();
	    }
}
