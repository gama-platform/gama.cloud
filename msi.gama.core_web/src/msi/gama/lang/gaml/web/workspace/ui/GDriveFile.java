package msi.gama.lang.gaml.web.workspace.ui;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class GDriveFile extends java.io.File {
	public boolean isDir=false;
	/**
	 * @return the isDir
	 */
	public boolean isDir() {
		return isDir;
	}

	/**
	 * @param isDir the isDir to set
	 */
	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

	public String id;
	private ArrayList<GDriveFile> children=new ArrayList<>();
	/**
	 * @return the children
	 */
	public ArrayList<GDriveFile> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(ArrayList<GDriveFile> children) {
		this.children = children;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String downloadUrl;
	
    public String title;
    public String description;
    public String mimeType;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public GDriveFile(String the_id,String pathname, String t, String desc, String mime, String url) {
		super(pathname);
		id=the_id;
		title=t;
		description =desc;
		mimeType=mime;
		downloadUrl=url;
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

	/**
	 * @param downloadUrl the downloadUrl to set
	 */
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getDownloadUrl() {
		// TODO Auto-generated method stub
		return downloadUrl;
	}
	
	
	@Override
    public String[] list() {
		return null;
    	
    }
}
