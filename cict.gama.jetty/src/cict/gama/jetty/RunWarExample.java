package cict.gama.jetty;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
//import org.eclipse.jetty.annotations.AnnotationConfiguration;
//import org.eclipse.jetty.plus.webapp.EnvConfiguration;
//import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
//import org.eclipse.jetty.webapp.TagLibConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

public class RunWarExample {
	public static int port = 8080;

	File warpath;// = "/GamaWeb.war";

	public void retrieveWar(String contextpath) {
		File currentJavaJarFile = new File(
				RunWarExample.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String currentJavaJarFilePath = currentJavaJarFile.getAbsolutePath();
		String executionPath = currentJavaJarFilePath.replace(currentJavaJarFile.getName(), "");
		warpath = new File(executionPath + "/" + contextpath + ".war");
//		warpath=file1.getAbsolutePath();
		if (!warpath.exists()) {
			InputStream link = (getClass().getResourceAsStream("/GamaWeb.war"));
			try {
				Files.copy(link, warpath.getAbsoluteFile().toPath());
				System.out.println(warpath.getAbsoluteFile());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		RunWarExample r = new RunWarExample();
		r.retrieveWar(args[0]);
		Server server = new Server(stringToInt(args[1]));
//		String warpath = "C:\\git\\gama.cloud\\cict.gama.tomcat\\target\\GamaWeb\\GamaWeb.war";

		File currentJavaJarFile = new File(
				RunWarExample.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String currentJavaJarFilePath = currentJavaJarFile.getAbsolutePath();
		String executionPath = currentJavaJarFilePath.replace(currentJavaJarFile.getName(), "");
		File warpath = new File(executionPath + "/" + args[0] + ".war");
		WebAppContext context = new WebAppContext();
//		context.setResourceBase(warpath);
		context.setConfigurations(new Configuration[] { new AnnotationConfiguration(), new WebInfConfiguration(),
				new WebXmlConfiguration(), new MetaInfConfiguration(), new FragmentConfiguration(),
				new EnvConfiguration(), new PlusConfiguration(), new JettyWebXmlConfiguration() });
		context.setContextPath("/" + args[0]);

		context.setWar(warpath.getAbsolutePath());
		context.setParentLoaderPriority(false);
		context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "true");

		File tmpPath = new File(args[0] + "_tmp_");
		if (tmpPath.exists()) {
			System.exit(1);
		}
		tmpPath.mkdirs();
		context.setTempDirectory(tmpPath);

//		  ((HandlerCollection) server.getHandler()).addHandler(context);

		context.setParentLoaderPriority(true);
		server.setHandler(context);
		try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//	    Server server = new Server(9090);
//	    WebAppContext webapp = new WebAppContext();
//	    webapp.setContextPath("/");
//
//	    webapp.setWar("test.war");
//	    server.setHandler(webapp);
//
//	    try {
//	        server.start();
//	        System.out.println("Press any key to stop the server...");
//	        System.in.read(); System.in.read();
//	        server.stop();
//	    } catch (Exception ex) {
//	        System.out.println("error");
//	    }
//
//	    System.out.println("Server stopped");

//		String contextPath = "/GamaWeb";
//		String warFilePath = "GamaWeb.war";
//
////		String warFilePath = "C:\\git\\gama.cloud\\cict.gama.tomcat\\target\\GamaWeb\\GamaWeb.war";
////		if(args.length==1) port=stringToInt(args[0]);
//		if(args.length==3) {
//			contextPath=args[0];
//			warFilePath=args[1];
//			port=stringToInt(args[2]);
//		}
//		
//		Tomcat tomcat = new Tomcat();
//		tomcat.setPort(port);
//		tomcat.setBaseDir(".");
////		Catalina cat=new Catalina();
////		System.out.println(cat.getConfigFile());
////		cat.setConfigFile("C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\conf\\server.xml");
////		System.out.println(cat.getConfigFile());
////		cat.setUseNaming(true);
////		tomcat.getServer().setCatalina(cat);
////		org.apache.catalina.connector.Connector def=tomcat.getConnector();
////		def.setURIEncoding("UTF-8");
////		def.setAttribute("relaxedQueryChars", "[]|{}^&#x5c;&#x60;&quot;&lt;&gt;");
////		def.setAttribute("relaxedPathChars", "[]|");//org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH
////		def.setProperty("URIEncoding", "UTF-8");
////		def.setProtocol("org.apache.coyote.http11.Http11NioProtocol");
////		def.setUseBodyEncodingForURI(true);
//
//		tomcat.getHost().setAppBase(".");
//
//		tomcat.addWebapp(contextPath, warFilePath);
//
//		tomcat.start();
//		if(args.length==4 || args.length==0) {			
//			tomcat.getServer().await();
//		}
	}

	public static boolean stringToBool(String param) {
		return Boolean.valueOf(param);
	}

	public static int stringToInt(String param) throws NumberFormatException {
		try {
			return Integer.valueOf(param);
		} catch (NumberFormatException e) {
			// return -1;
			port = 8080;
			throw e;
		}
	}
}
