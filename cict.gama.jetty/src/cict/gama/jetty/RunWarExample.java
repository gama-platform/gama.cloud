package cict.gama.jetty;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
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

	File warFile;// = "/GamaWeb.war";
	String executionPath = "";

	public void retrieveWar(String contextpath) {
//		warFile = new File(executionPath + "/" + contextpath + ".war");
		String warpath = "C:\\git\\gama.cloud\\cict.gama.jetty\\target\\GamaWeb\\GamaWeb.war";

		warFile = new File(warpath);
//		warpath=file1.getAbsolutePath();
		if (!warFile.exists()) {
			InputStream link = (getClass().getResourceAsStream("/GamaWeb.war"));
			try {
				Files.copy(link, warFile.getAbsoluteFile().toPath());
				System.out.println(warFile.getAbsoluteFile());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * args[0] contextpath args[1] port args[2] controller's address
	 */
	public static void main(String[] args) {
//		final String ctx_path = args[0];
//		final String ctx_port = args[1];
//		final String ctrl_addr = args[2];
		final String ctx_path = "GamaWeb";
		final String ctx_port = "8080";
		final String ctrl_addr = "127.0.0.1:8080";
		final RunWarExample r = new RunWarExample();
		File currentJavaJarFile = new File(
				RunWarExample.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String currentJavaJarFilePath = currentJavaJarFile.getAbsolutePath();
		r.executionPath = currentJavaJarFilePath.replace(currentJavaJarFile.getName(), "");
		r.retrieveWar(ctx_path);
		final Server server = new Server(stringToInt(ctx_port));
//		String warpath = "C:\\git\\gama.cloud\\cict.gama.tomcat\\target\\GamaWeb\\GamaWeb.war";
//		server.setStopAtShutdown(true);
//		File currentJavaJarFile = new File(
//				RunWarExample.class.getProtectionDomain().getCodeSource().getLocation().getPath());
//		String currentJavaJarFilePath = currentJavaJarFile.getAbsolutePath();
//		String executionPath = currentJavaJarFilePath.replace(currentJavaJarFile.getName(), "");
//		File warpath = new File(executionPath + "/" + ctx_path + ".war");
		WebAppContext context = new WebAppContext();
//		context.setResourceBase(warpath);
		context.setConfigurations(new Configuration[] { new AnnotationConfiguration(), new WebInfConfiguration(),
				new WebXmlConfiguration(), new MetaInfConfiguration(), new FragmentConfiguration(),
				new EnvConfiguration(), new PlusConfiguration(), new JettyWebXmlConfiguration() });
		context.setContextPath("/" + ctx_path);

		context.setWar(r.warFile.getAbsolutePath());
		context.setParentLoaderPriority(false);
		context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "true");

		final File tmpPath = new File(r.executionPath + "/" + ctx_path + "_tmp_");
		if (tmpPath.exists()) {
//			System.exit(1);
		} else {
			tmpPath.mkdirs();
		}
		System.out.println("tmpPath " + tmpPath);
		context.setPersistTempDirectory(false);
		context.setTempDirectory(tmpPath);

//		  ((HandlerCollection) server.getHandler()).addHandler(context);

		context.setParentLoaderPriority(true);
		server.setHandler(context);
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				IO.delete(r.warFile);
				deleteFolder(tmpPath);
			}

		});
		new Thread() {

			@Override
			public void run() {
				while (true) {

					try {
						if (ctx_path.startsWith("user_GamaWeb")
//								&& getStatus("http://localhost:10080/controller_GamaWeb/texteditor")
//								&& getStatus("http://51.255.46.42:8080/controller_GamaWeb/texteditor")
								&& getStatus("http://" + ctrl_addr + "/controller_GamaWeb/").startsWith("-> Red <-")
								&& getStatus("http://localhost:8080/controller_GamaWeb/").startsWith("-> Red <-")) {
							System.out.println("http://" + ctrl_addr + "/controller_GamaWeb/");
							System.exit(0);
						}
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}.start();
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

	public static String getStatus(String url) throws IOException {

		String result = "";
		int code = 200;
		try {
			URL siteURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3000);
			connection.connect();

			code = connection.getResponseCode();
			if (code == 200) {
				result = "-> Green <-\t" + "Code: " + code;
				;
			} else {
				result = "-> Yellow <-\t" + "Code: " + code;
			}
		} catch (Exception e) {
			result = "-> Red <-\t" + "Wrong domain - Exception: " + e.getMessage();

		}
//		System.out.println(url + "\t\tStatus:" + result);
		return result;
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
