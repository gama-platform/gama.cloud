package msi.gama.lang.gaml.web.workspace.ui;

import java.net.URI;

public class GDriveFile extends java.io.File {
	public boolean isDir=false;
	public String id;
	public String downloadUrl;
	public GDriveFile(String the_id,String pathname) {
		super(pathname);
		id=the_id;
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

	public String getDownloadUrl() {
		// TODO Auto-generated method stub
		return downloadUrl;
	}
}
