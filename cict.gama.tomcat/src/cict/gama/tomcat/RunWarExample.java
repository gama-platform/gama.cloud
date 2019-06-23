package cict.gama.tomcat;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class RunWarExample {
	public static int port=8080;
	public static void main(String[] args) throws ServletException, LifecycleException {
		String contextPath = "/GamaWeb";
		String warFilePath = "GamaWeb.war";
//		if(args.length==1) port=stringToInt(args[0]);
		if(args.length==3) {
			contextPath=args[0];
			warFilePath=args[1];
			port=stringToInt(args[2]);
		}
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(port);
		tomcat.setBaseDir(".");

		tomcat.getHost().setAppBase(".");

		tomcat.addWebapp(contextPath, warFilePath);

		tomcat.start();
		if(args.length!=1) {			
			tomcat.getServer().await();
		}
	}

	public static boolean stringToBool(String param)  {		
			return Boolean.valueOf(param);
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
