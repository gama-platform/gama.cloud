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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

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
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonViewer;
// import ummisco.gama.participative.EtherPadEditor;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Export;


import net.gjerull.etherpad.client.EPLiteClient;

/**
 * A basic implementation of the CNF based on java.io.File
 *
 */
public class GamlWorkspaceNavigator extends BasicWokspaceNavigator {
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
		getCommonViewer().setContentProvider(new GamaFileSystemContentProvider());
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
	  

	  /**
	   * Update an existing file's metadata and content.
	   *
	   * @param service Drive API service instance.
	   * @param fileId ID of the file to update.
	   * @param newTitle New title for the file.
	   * @param newDescription New description for the file.
	   * @param newMimeType New MIME type for the file.
	   * @param newFilename Filename of the new content to upload.
	   * @param newRevision Whether or not to create a new revision for this
	   *        file.
	   * @return Updated file metadata if successful, {@code null} otherwise.
	   */
	  private com.google.api.services.drive.model.File updateFile(Drive service, String fileId, String newTitle,
	      String newDescription, String newMimeType, String newFilename, boolean newRevision) {
	    try {
	      // First retrieve the file from the API.
	      com.google.api.services.drive.model.File file = service.files().get(fileId).execute();

	      // File's new metadata.
	      file.setTitle(newTitle);
	      file.setDescription(newDescription);
	      file.setMimeType(newMimeType);

	      // File's new content.
	      java.io.File fileContent = new java.io.File(newFilename);
	      FileContent mediaContent = new FileContent(newMimeType, fileContent);

	      // Send the request to the API.
	      com.google.api.services.drive.model.File updatedFile = service.files().update(fileId, file, mediaContent).execute();

	      return updatedFile;
	    } catch (IOException e) {
	      System.out.println("An error occurred: " + e);
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
			if (element instanceof File && !(element instanceof GDriveFile)) {
				final File file = (File) element;
				if (file.exists() && !file.isDirectory()) {
					final Display display = PlatformUI.getWorkbench().getDisplay();
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							// Double click a file
							System.out.println(" Trying to connect to etherpad. From "+getClass().toString());
							//EPLiteClient epClient = new EPLiteClient("http://localhost:9001", "ea45b73fbcba78e79125835624433e291c7ca2418b93d7cc14964bc7abc4e6f4");
							EPLiteClient epClient = new EPLiteClient("http://localhost:9001", "13b06a4979933f63641228e4a22d4b9e816b0e7237a8ae3d36b2ccb254ddb5e6");
							
							
							// Create pad and set text
							//client.createPad("my_pad");
							//client.setText("my_pad", "foo!!");

							// Get pad text
							
							// ---------------------
			//				IEditorRegistry editorRegistry = PlatformUI.getWorkbench().getEditorRegistry();
			//				IEditorDescriptor ed = editorRegistry.findEditor("ummisco.gama.participative.EtherPadEditor");
						//	EtherPadEditor ethEditor = new EtherPadEditor();
						//	ethEditor.createPartControl(compo);
						
						
						//	editorRegistry.setDefaultEditor(new Path(absolutePath), "ummisco.gama.participative.EtherPadEditor");
			//				System.out.println(" ---> Il exite bien: " +ed.getId());
						//	editorRegistry.getDefaultEditor(new Path(absolutePath), "ummisco.gama.participative.EtherPadEditor");
							// --------------------
							
							
						//	String uid=RWT.getUISession().getAttribute("user").toString();
							String absolutePath = file.getAbsolutePath();
							IWorkbench workbench = PlatformUI.getWorkbench();
							
							if (EditorUtil.openEditor(workbench, new Path(absolutePath)) != null) {
//								logger.info("Double click on file " + absolutePath);
								
							
								
								try {
									String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
									//System.out.println(" -> Double click on file: " +content);
									Map padList = epClient.listAllPads();
								
									String value = (String) padList.get(file.getName());
									if (value != null) {
										epClient.deletePad(file.getName());
									} else {
										epClient.createPad(file.getName());
										epClient.setText(file.getName(), content);
									}
									
									
									
								//	xcv
									String text = epClient.getText(file.getName()).get("text").toString();
									System.out.println(" The pad content is :"+ text);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							
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
							// Add here the code to insert the file in the dataBase. 
							EPLiteClient client = new EPLiteClient("http://localhost:9001", "K8OF91QMQYUvrNu3e9rJ7FnnVgaB3m9q");
//							
							// Create pad and set text
							//client.createPad("my_pad");
							//client.setText("my_pad", "foo!!");

							// Get pad text
							String text = client.getText("SKLAB").get("text").toString();

							System.out.println(" The pad content is :"+ text);
							// Get list of all pad ids
						//	Map result = client.listAllPads();
						//	List padIds = (List) result.get("padIDs");
							
							Export s;
							try {
//								s = GamaFileSystemContentProvider.drive.files().export(file.id, "text/plain");
								InputStream in=downloadFile(GamaFileSystemContentProvider.drive,file);//s.executeMediaAsInputStream();

								
							    File targetFile = new File(file.getPath());
							    OutputStream outStream = new FileOutputStream(targetFile);
							    
							    

								int read = 0;
								byte[] bytes = new byte[1024];

								while ((read = in.read(bytes)) != -1) {
									outStream.write(bytes, 0, read);
								}
							    outStream.close();
							    in.close();
							    /*
								InputStreamReader isr=new InputStreamReader(in);
								BufferedReader br = new BufferedReader(isr);
								String line = null;
								
								StringBuilder responseData = new StringBuilder();
								while((line = br.readLine()) != null) {
									responseData.append(line);
								}
								System.out.println(responseData);*/
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							updateFile(GamaFileSystemContentProvider.drive, file.getId(), file.getTitle(), file.getDescription(), file.getMimeType(), file.getPath(), true);
							String absolutePath = file.getPath();//file.getAbsolutePath();
							IWorkbench workbench = PlatformUI.getWorkbench();
							if (GamlEditorUtil.openEditor(workbench, new Path(absolutePath)) != null) {
//								logger.info("Double click on file " + absolutePath);
							}
//							workspaceChanged(null);
						}
					});

				} else {//if (file.exists() && file.isDirectory()) {
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