package msi.gama.lang.gaml.web.editor;

import java.util.HashMap;

import msi.gama.common.interfaces.IGui;
import msi.gama.runtime.GAMA;

public class GAMAHelper extends GAMA{
	public static HashMap<String, IGui> theGUI=new HashMap<String,IGui>();

	public static IGui getRegularGui(final String uid) {
		return theGUI.get(uid);
	}

	public static void setRegularGui(final IGui g,final String uid) {
		theGUI.put(uid, g);
		setRegularGui(g);
	}
	
	public static void changeRegularGui(final String uid) {
		setRegularGui(getRegularGui(uid));
	}
}
