package com.jogamp.common.util;

import java.net.URL;

import com.jogamp.common.util.JarUtil.Resolver;

public class JarUtil {

	 public interface Resolver {
		 URL resolve(URL url);
	}

	public static void setResolver(Resolver resolver) {
		// TODO Auto-generated method stub
		
	}

}
