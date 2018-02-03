package msi.gama.lang.gaml.web.workspace.ui;

import java.net.URI;

public class GDriveFile extends java.io.File {
	public boolean isDir=false;
	public GDriveFile(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getParent() {
		return "";
	}

	@Override
	public boolean isDirectory() {
		return isDir;
	}
}
