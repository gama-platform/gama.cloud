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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.dslforge.workspace.ui.BasicViewerComparator;
import org.dslforge.workspace.ui.BasicWokspaceNavigator;
import org.dslforge.workspace.ui.BasicWorkspaceFilter;
import org.dslforge.workspace.ui.BasicWorkspaceSorter;
import org.dslforge.workspace.ui.FileSystemContentProvider;
import org.dslforge.workspace.ui.FileSystemLabelProvider;
import org.dslforge.workspace.ui.util.EditorUtil;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonViewer;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Export;

/**
 * A basic implementation of the CNF based on java.io.File
 *
 */
public class GamlWokspaceNavigator extends BasicWokspaceNavigator {
//	private List<PropertySheetPage> propertySheetPages = new ArrayList<PropertySheetPage>();
//	private String uid="";


	@Override
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
		getCommonViewer().setSorter(new BasicWorkspaceSorter());
		getCommonViewer().setComparator(new BasicViewerComparator());
		String workspaceRoot = getWorkspaceRoot();
		IWorkbench workbench = PlatformUI.getWorkbench();
		IPartService partService = workbench.getActiveWorkbenchWindow().getPartService();
		partService.addPartListener(this);
//		getCommonViewer().addSelectionChangedListener(selectionListener);
		getCommonViewer().addFilter(new BasicWorkspaceFilter());
		getCommonViewer().setContentProvider(new FileSystemContentProvider());
		getCommonViewer().setLabelProvider(new FileSystemLabelProvider());
		getCommonViewer().setInput(new File(workspaceRoot));
	}

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

	  /**
	   * Download a file's content.
	   *
	   * @param service Drive API service instance.
	   * @param file Drive File instance.
	   * @return InputStream containing the file's content if successful,
	   *         {@code null} otherwise.
	   */
	  private static InputStream downloadFile(Drive service, GDriveFile file) {
	    if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
	      try {
	        HttpResponse resp =
	            service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl()))
	                .execute();
	        return resp.getContent();
	      } catch (IOException e) {
	        // An error occurred.
	        e.printStackTrace();
	        return null;
	      }
	    } else {
	      // The file doesn't have any content stored on Drive.
	      return null;
	    }
	  }

	@Override
	protected void handleDoubleClick(DoubleClickEvent anEvent) {
		// TODO Auto-generated method stub
//		super.handleDoubleClick(anEvent);

		IAction openHandler = getViewSite().getActionBars()
				.getGlobalActionHandler("org.eclipse.ui.actionSet.openFiles");
		if (openHandler == null) {
			IStructuredSelection selection = (IStructuredSelection) anEvent.getSelection();
			Object element = selection.getFirstElement();
			if (element instanceof File) {
				final File file = (File) element;
				if (file.exists() && !file.isDirectory()) {
					final Display display = PlatformUI.getWorkbench().getDisplay();
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							// Double click a file
							String absolutePath = file.getAbsolutePath();
							IWorkbench workbench = PlatformUI.getWorkbench();
							if (EditorUtil.openEditor(workbench, new Path(absolutePath)) != null) {
//								logger.info("Double click on file " + absolutePath);
							}
							workspaceChanged(null);
						}
					});

				} else if (file.exists() && file.isDirectory()) {
					// Double click a folder
					Object eventSource = anEvent.getSource();
					if (eventSource instanceof TreeViewer) {
						TreeViewer treeViewer = (TreeViewer) eventSource;
						Widget widget = treeViewer.testFindItem(file);
						if (widget != null && widget instanceof TreeItem) {
							TreeItem item = (TreeItem) widget;
							boolean toExpand = !item.getExpanded();
							item.setExpanded(toExpand);
							if (toExpand) {
								treeViewer.expandToLevel(item, 0);
							} else {
								treeViewer.collapseToLevel(item, 0);
							}
							treeViewer.refresh(item.getData());
						}
					}
				}
			}else
			if (element instanceof GDriveFile) {
				final GDriveFile file = (GDriveFile) element;
				if ( !file.isDirectory()) {//file.exists() &&
					final Display display = PlatformUI.getWorkbench().getDisplay();
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							// Double click a file
//							Export s;
							try {
//								s = GamaFileSystemContentProvider.drive.files().export(file.id, "text/plain");
								InputStream in=downloadFile(GamaFileSystemContentProvider.drive,file);//s.executeMediaAsInputStream();
								InputStreamReader isr=new InputStreamReader(in);
								BufferedReader br = new BufferedReader(isr);
								String line = null;
								
								StringBuilder responseData = new StringBuilder();
								while((line = br.readLine()) != null) {
									responseData.append(line);
								}
								System.out.println(responseData);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//							String absolutePath = file.getAbsolutePath();
//							IWorkbench workbench = PlatformUI.getWorkbench();
//							if (EditorUtil.openEditor(workbench, new Path(absolutePath)) != null) {
////								logger.info("Double click on file " + absolutePath);
//							}
//							workspaceChanged(null);
						}
					});

				} else if (file.exists() && file.isDirectory()) {
					// Double click a folder
					Object eventSource = anEvent.getSource();
					if (eventSource instanceof TreeViewer) {
						TreeViewer treeViewer = (TreeViewer) eventSource;
						Widget widget = treeViewer.testFindItem(file);
						if (widget != null && widget instanceof TreeItem) {
							TreeItem item = (TreeItem) widget;
							boolean toExpand = !item.getExpanded();
							item.setExpanded(toExpand);
							if (toExpand) {
								treeViewer.expandToLevel(item, 0);
							} else {
								treeViewer.collapseToLevel(item, 0);
							}
							treeViewer.refresh(item.getData());
						}
					}
				}
			}
		}
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