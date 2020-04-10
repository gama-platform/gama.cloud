package cict.gama.tomcat;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class RunWarExample {
	public static int port=8080;
	public static void main(String[] args) throws ServletException, LifecycleException {
		String contextPath = "/GamaWeb";
		String warFilePath = "GamaWeb.war";

//		String warFilePath = "C:\\git\\gama.cloud\\cict.gama.tomcat\\target\\GamaWeb\\GamaWeb.war";
//		if(args.length==1) port=stringToInt(args[0]);
		if(args.length==3) {
			contextPath=args[0];
			warFilePath=args[1];
			port=stringToInt(args[2]);
		}

		Tomcat tomcat = new Tomcat();
		tomcat.setPort(port);
		tomcat.setBaseDir(".");
//		Catalina cat=new Catalina();
//		System.out.println(cat.getConfigFile());
//		cat.setConfigFile("C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\conf\\server.xml");
//		System.out.println(cat.getConfigFile());
//		cat.setUseNaming(true);
//		tomcat.getServer().setCatalina(cat);
//		org.apache.catalina.connector.Connector def=tomcat.getConnector();
//		def.setURIEncoding("UTF-8");
//		def.setAttribute("relaxedQueryChars", "[]|{}^&#x5c;&#x60;&quot;&lt;&gt;");
//		def.setAttribute("relaxedPathChars", "[]|");//org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH
//		def.setProperty("URIEncoding", "UTF-8");
//		def.setProtocol("org.apache.coyote.http11.Http11NioProtocol");
//		def.setUseBodyEncodingForURI(true);

		tomcat.getHost().setAppBase(".");

		tomcat.addWebapp(contextPath, warFilePath);

		tomcat.start();
		if(args.length==4 || args.length==0) {			
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
