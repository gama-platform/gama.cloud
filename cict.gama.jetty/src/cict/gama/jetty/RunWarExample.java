package cict.gama.jetty;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.EventListener;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Handler;
//import org.eclipse.jetty.annotations.AnnotationConfiguration;
//import org.eclipse.jetty.plus.webapp.EnvConfiguration;
//import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionIdManager;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.HouseKeeper;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.component.Container;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.component.LifeCycle.Listener;
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
		final Server server = new Server(stringToInt(args[1]));
//		String warpath = "C:\\git\\gama.cloud\\cict.gama.tomcat\\target\\GamaWeb\\GamaWeb.war";
//		server.setStopAtShutdown(true);
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

		final File tmpPath = new File(args[0] + "_tmp_");
		if (tmpPath.exists()) {
//			System.exit(1);
		} else {
			tmpPath.mkdirs();
		}
		context.setPersistTempDirectory(false);
		context.setTempDirectory(tmpPath);

//		  ((HandlerCollection) server.getHandler()).addHandler(context);

		context.setParentLoaderPriority(true);
		server.setHandler(context);
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {  
					deleteFolder(tmpPath); 
			}

		});

		try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void deleteFolder(File file) {
		for (File subFile : file.listFiles()) {
			if (subFile.isDirectory()) {
				deleteFolder(subFile);
			} else {
				IO.delete(subFile);
//				System.out.println(subFile);
			}
		}
		IO.delete(file);
//		System.out.println(file);
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
