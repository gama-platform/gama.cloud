/*********************************************************************************************
 *
 * 'ShowViewContributionItem.java, in plugin ummisco.gama.ui.shared, is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.actions.ContributionItemFactory;

public class ShowViewContributionItem extends CompoundContributionItem {

	private String label="Show View";
	public ShowViewContributionItem() {
	}

	public ShowViewContributionItem(final String id) {
		super(id);
	}

	@Override
	public void fill(Menu menu, int index) {
		if(!menu.getParentItem().getText().equals(label)) {
			return;
		}
		super.fill(menu, index);
	}

	@Override
	protected IContributionItem[] getContributionItems() {
		final List<IContributionItem> menuContributionList = new ArrayList<>();
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		final IContributionItem item = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
		menuContributionList.add(item); // add the list of views in the menu
		return menuContributionList.toArray(new IContributionItem[0]);
	}

}
