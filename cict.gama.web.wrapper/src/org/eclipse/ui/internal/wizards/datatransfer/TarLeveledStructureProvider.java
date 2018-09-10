package org.eclipse.ui.internal.wizards.datatransfer;

import java.io.InputStream;
import java.util.List;

public class TarLeveledStructureProvider implements ILeveledImportStructureProvider{

	public TarLeveledStructureProvider(TarFile sourceTarFile) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public InputStream getContents(Object projectArchiveFile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel(Object parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<?> getChildren(Object entry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFolder(Object child) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setStrip(int level) {
		// TODO Auto-generated method stub
		
	}

}
