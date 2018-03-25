package ummisco.gama.ui.interfaces;

import java.util.List;

import org.eclipse.rap.ui.resources.IResource;

public interface IRefreshHandler {

	public void completeRefresh(List<? extends IResource> resources);

	public void refreshNavigator();

}
