/**
 * <copyright>
 *
 * Copyright (c) 2015 PlugBee. All rights reserved.
 * 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which 
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Amine Lajmi - Initial API and implementation
 *
 * </copyright>
 */
package msi.gama.lang.gaml.web.workspace.ui;

import org.dslforge.workspace.ui.BasicWokspaceNavigator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.navigator.CommonViewer;

/**
 * A basic implementation of the CNF based on java.io.File
 *
 */
public class GamlWokspaceNavigator extends BasicWokspaceNavigator {
//	private List<PropertySheetPage> propertySheetPages = new ArrayList<PropertySheetPage>();
//	private String uid="";

	@Override
	public void workspaceChanged(Object e) {
		final CommonViewer commonViewer = getCommonViewer();
		Control control = commonViewer.getControl();
		if (!control.isDisposed()) {
			Display display = control.getDisplay();
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					if (!commonViewer.isBusy() && !commonViewer.getTree().isDisposed())
						
						commonViewer.refresh();
				}
			});
		}
	}

//	@Override
//	public IPropertySheetPage getPropertySheetPage() {
//		PropertySheetPage propertySheetPage = new PropertySheetPage();
//		propertySheetPage.setPropertySourceProvider(new FilePropertySourceProvider());
//		
//		propertySheetPages.add(propertySheetPage);
//		return propertySheetPage;
//		return null;
//	}

	@Override
	protected void handleDoubleClick(DoubleClickEvent anEvent) {
		// TODO Auto-generated method stub
		super.handleDoubleClick(anEvent);
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		// TODO Auto-generated method stub
		super.init(site);
//		TitleAreaDialog a=new TitleAreaDialog(getSite().getShell());
//		a.open();
//		MessageDialog.openInformation(getSite().getShell(), "Open", "Open Message Dialog!");
	}


}