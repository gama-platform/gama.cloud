package org.eclipse.ui.internal.wizards.datatransfer;

import java.io.InputStream;
import java.util.List;

public interface ILeveledImportStructureProvider {

	public InputStream getContents(Object projectArchiveFile) ;
	public String getLabel(Object parent);
	public Object getRoot();
	public List<?> getChildren(Object entry);
	public boolean isFolder(Object child);
	public void setStrip(int level);
}
