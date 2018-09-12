package cict.gama.tomcat;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class RunWarExample {
	public static int port=8080;
	public static void main(String[] args) throws ServletException, LifecycleException {

		String contextPath = "/GamaWeb";
		String warFilePath = "GamaWeb.war";
		Tomcat tomcat = new Tomcat();
		if(args.length>0) port=stringToInt(args[0]);
		tomcat.setPort(port);
		tomcat.setBaseDir(".");

		tomcat.getHost().setAppBase(".");

		tomcat.addWebapp(contextPath, warFilePath);

		tomcat.start();
		tomcat.getServer().await();
	}

	public static int stringToInt(String param) throws NumberFormatException {
		try {
			return Integer.valueOf(param);
		} catch (NumberFormatException e) {
			// return -1;
			port=8080;
			throw e;
		}
	}
}
