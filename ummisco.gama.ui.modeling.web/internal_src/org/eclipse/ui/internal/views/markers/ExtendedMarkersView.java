/*******************************************************************************
 * Copyright (c) 2007, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Andrew Gvozdev -  Bug 364039 - Add "Delete All Markers"
 *     Lars Vogel <Lars.Vogel@gmail.com> - Bug 440810
 *     Cornel Izbasa <cizbasa@info.uvt.ro> - Bug 442440
 *     Andrey Loskutov <loskutov@gmx.de> - Bug 446864, 466927
 *     Mickael Istria (Red Hat Inc.) - Bug 486901
 *     Patrik Suzzi <psuzzi@gmail.com> - Bug 489250
 *******************************************************************************/
package org.eclipse.ui.internal.views.markers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeAdapter;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.OpenAndLinkWithEditorHelper;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.operations.RedoActionHandler;
import org.eclipse.ui.operations.UndoActionHandler;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.statushandlers.StatusManager;


/**
 * The ExtendedMarkersView is the internal implementation of the view that shows
 * markers using the markerGenerators extension point.
 *
 * The ExtendedMarkersView fully supports the markerSupport extension point and
 * is meant to be used as a view to complement them.
 *
 * The markerContentGenerators to be used by the view can be specified by
 * appending a comma separated list of them after a colon in the class
 * specification of the view. If this list is left out the problems
 * markerContentProvider will be used.
 *
 * @since 3.4
 *
 */
public class ExtendedMarkersView extends ViewPart {

	/**
	 * The Markers View Update Job Family
	 * @since 3.6
	 */
	public final Object MARKERSVIEW_UPDATE_JOB_FAMILY = new Object();

	static final String MARKER_FIELD = "MARKER_FIELD"; //$NON-NLS-1$

	private static int instanceCount = 1;
	private static final String TAG_GENERATOR = "markerContentGenerator"; //$NON-NLS-1$

	private static final String TAG_EXPANDED = "expanded"; //$NON-NLS-1$

	private static final String TAG_CATEGORY = "category"; //$NON-NLS-1$

	private static final String TAG_PART_NAME = "partName"; //$NON-NLS-1$

	private static final String TAG_COLUMN_WIDTHS = "columnWidths"; //$NON-NLS-1$

	private Collection<String> categoriesToExpand;

	private UIJob uiUpdateJob;

	private TreeViewer viewer;

	private Action filterAction;


	/**
	 * Tells whether the tree has been painted.
	 * @since 3.7
	 */
	private boolean treePainted= false;

	private ISelectionListener pageSelectionListener;
	private IPartListener2 partListener;
	private Clipboard clipboard;
	private IMemento memento;
	private String[] defaultGeneratorIds = new String[0];

	private UndoActionHandler undoAction;

	private RedoActionHandler redoAction;

	private boolean isViewVisible= true;


	/**
	 * Return a new instance of the receiver.
	 *
	 * @param contentGeneratorId
	 *            the id of the generator to load.
	 */
	public ExtendedMarkersView(String contentGeneratorId) {
		super();
		defaultGeneratorIds = new String[] { contentGeneratorId };
	}

   
	/**
	 * Create the columns for the receiver.
	 *
	 * @param parent
	 */
	private void createViewer(Composite parent) {
		parent.setLayout(new FillLayout());

		viewer = new TreeViewer(new Tree(parent, SWT.H_SCROLL
				/*| SWT.VIRTUAL */| SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION));
		viewer.getTree().setLinesVisible(true);
		viewer.setUseHashlookup(true);
		createColumns(new TreeColumn[0], new int[0]);

	}

	/**
	 * Create the columns for the receiver.
	 *
	 * @param currentColumns
	 *            the columns to refresh
	 * @param widths
	 */
	private void createColumns(TreeColumn[] currentColumns, int[] widths) {

		Tree tree = viewer.getTree();
		TableLayout layout = new TableLayout();


		viewer.getTree().setLayout(layout);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.layout(true);

	}

	@Override
	public void createPartControl(Composite parent) {

		createViewer(parent);

		addDoubleClickListener();

		addPageAndPartSelectionListener();

		addLinkWithEditorSupport();

		addSelectionListener();
 

		getSite().setSelectionProvider(viewer);

		IUndoContext undoContext= getUndoContext();
		undoAction= new UndoActionHandler(getSite(), undoContext);
		undoAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_UNDO);
		redoAction= new RedoActionHandler(getSite(), undoContext);
		redoAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_REDO);
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);


	}

	/**
	 * Attaches an {@link IDoubleClickListener} to expand items that are not openable
	 * @since 3.8
	 */
	private void addDoubleClickListener() {
		viewer.addDoubleClickListener(event -> {
			ISelection selection = event.getSelection();
			if(selection instanceof ITreeSelection) {
				ITreeSelection ss = (ITreeSelection) selection;
				if(ss.size() == 1) {
					Object obj = ss.getFirstElement();
					if(viewer.isExpandable(obj)) {
						viewer.setExpandedState(obj, !viewer.getExpandedState(obj));
					}
				}
			}
		});
	}

	/**
	 *
	 */
	private void addPageAndPartSelectionListener() {
		// Initialize any selection based filtering
		pageSelectionListener = new ViewerPageSelectionListener(this);
		getSite().getPage().addPostSelectionListener(pageSelectionListener);

		partListener = getPartListener();
		getSite().getPage().addPartListener(partListener);

		pageSelectionListener.selectionChanged(getSite().getPage().getActivePart(), getSite().getPage().getSelection());
	}

	/**
	 *
	 */
	private void addSelectionListener() {
		viewer.addSelectionChangedListener(event -> {
			ISelection selection = event.getSelection();
		});
	}

	/**
	 *
	 */
	private void addLinkWithEditorSupport() {
		new OpenAndLinkWithEditorHelper(viewer) {
			@Override
			protected void activate(ISelection selection) {
				final int currentMode = OpenStrategy.getOpenMethod();
				try {
					OpenStrategy.setOpenMethod(OpenStrategy.DOUBLE_CLICK);
				} finally {
					OpenStrategy.setOpenMethod(currentMode);
				}
			}

			@Override
			protected void linkToEditor(ISelection selection) {
				// Not supported by this part
			}

			@Override
			protected void open(ISelection selection, boolean activate) {
			}
		};
	}

	/**
	 * Return the clipboard for the receiver.
	 *
	 * @return Clipboard
	 */
	Clipboard getClipboard() {
		if (clipboard == null) {
			clipboard = new Clipboard(viewer.getControl().getDisplay());
		}
		return clipboard;
	}

	/**
	 * Return the ids of the generators specified for the receiver.
	 *
	 * @return String[]
	 */
	String[] getGeneratorIds() {
		return defaultGeneratorIds;
	}


	/**
	 * Return a part listener for the receiver.
	 *
	 * @return IPartListener2
	 */
	private IPartListener2 getPartListener() {
		return new IPartListener2() {

			@Override
			public void partActivated(IWorkbenchPartReference partRef) {
				// Do nothing by default
			}

			@Override
			public void partBroughtToTop(IWorkbenchPartReference partRef) {
				// Do nothing by default
			}

			@Override
			public void partClosed(IWorkbenchPartReference partRef) {
				// Do nothing by default
			}

			@Override
			public void partDeactivated(IWorkbenchPartReference partRef) {
				// Do nothing by default
			}

			@Override
			public void partHidden(IWorkbenchPartReference partRef) {
				if (partRef.getId().equals(getSite().getId())) {
					isViewVisible= false;
				}
			}

			@Override
			public void partInputChanged(IWorkbenchPartReference partRef) {
				// Do nothing by default
			}

			@Override
			public void partOpened(IWorkbenchPartReference partRef) {
				// Do nothing by default
			}

			@Override
			public void partVisible(IWorkbenchPartReference partRef) {
				if (partRef.getId().equals(getSite().getId())) {
					isViewVisible = true;
					boolean needUpdate = hasPendingChanges();
					if (needUpdate) {
						// trigger UI update, the data is changed meanwhile
					} else {
						// data is same as before, only clear tooltip
						setTitleToolTip(null);
					}
				}
			}

			/**
			 * @return true if the builder noticed that marker updates were made
			 *         but UI is not updated yet
			 */
			private boolean hasPendingChanges() {
				return false;
			}
		};
	}

	/**
	 * Return all of the markers in the current selection
	 *
	 * @return Array of {@link IMarker}
	 */
	public IMarker[] getSelectedMarkers() {
		IStructuredSelection structured = viewer.getStructuredSelection();
		final List<IMarker> result = new ArrayList<>(structured.size());
		return result.toArray(new IMarker[result.size()]);
	}

	/**
	 * Return the sort direction.
	 *
	 * @return boolean
	 */
	public boolean getSortAscending() {
		return viewer.getTree().getSortDirection() == SWT.TOP;
	}

	@Override
	public void init(IViewSite site, IMemento m) throws PartInitException {
		super.init(site, m);

		// Add in the entries common to all markers views
		IMenuService menuService = site.getService(IMenuService.class);

		IWorkbenchSiteProgressService service = Adapters.adapt(site, IWorkbenchSiteProgressService.class);
		
		this.memento = m;

		if (m == null || m.getString(TAG_PART_NAME) == null) {
			return;
		}
		setPartName(m.getString(TAG_PART_NAME));
	}

	/**
	 * @return viewId
	 *
	 */
	String getViewsEffectiveId() {
		IViewSite site = (IViewSite) getSite();
		String viewId = site.getId();
		if (site.getSecondaryId() != null) {
			viewId = viewId + site.getSecondaryId();
		}
		return viewId;
	}

	/**
	 * @return viewsPrimaryId
	 *
	 */
	String getViewsPrimaryId() {
		IViewSite site = (IViewSite) getSite();
		return site.getId();
	}

	/**
	 * @return viewsSecondaryId
	 *
	 */
	String getViewsSecondaryId() {
		IViewSite site = (IViewSite) getSite();
		return site.getSecondaryId();
	}

	/**
	 * Initialize the title based on the name
	 *
	 * @param name
	 */
	void initializeTitle(String name) {
		setPartName(name);
	}


	@Override
	public void saveState(IMemento m) {
		super.saveState(m);
		m.putString(TAG_PART_NAME, getPartName());
	}

	/**
	 * Select all of the elements in the receiver.
	 */
	void selectAll() {
		viewer.getTree().selectAll();
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	/**
	 * @return the viewer
	 */
	TreeViewer getViewer() {
		return viewer;
	}


	/**
	 * The method should not be called directly, see
	 * {@link MarkerUpdateScheduler}
	 *
	 * @param delay
	 * @return UIUpdateJob
	 */
	UIJob scheduleUpdate(long delay) {
			if (uiUpdateJob != null) {
				// ensure cancellation before calling the method
				// uiUpdateJob.cancel();
			} else {
				uiUpdateJob = new UIJob(this.toString()) {
					
					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {
						return null;
					}
				};
				// uiUpdateJob.setPriority(Job.SHORT);
				uiUpdateJob.setSystem(true);
			}
				uiUpdateJob.schedule(delay);
			return uiUpdateJob;
	}
 

	/**
	 * Return the next secondary id that has not been opened for a primary id of
	 * a part.
	 *
	 * @return part
	 */
	static String newSecondaryID(IViewPart part) {
		while (part.getSite().getPage().findViewReference(
				part.getSite().getId(), String.valueOf(instanceCount)) != null) {
			instanceCount++;
		}

		return String.valueOf(instanceCount);
	} 
	/**
	 * Return The selection listener for the page selection change.
	 *
	 */
	private class ViewerPageSelectionListener implements ISelectionListener {
		private final ExtendedMarkersView view;

		ViewerPageSelectionListener(ExtendedMarkersView view) {
			this.view = view;
		}

		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {

			// Do not respond to our own selections
			if (part == ExtendedMarkersView.this) {
				return;
			}

			// get Objects to adapt
			List<Object> objectsToAdapt = new ArrayList<>();
			if (part instanceof IEditorPart) {
				IEditorPart editor = (IEditorPart) part;
				objectsToAdapt.add(editor.getEditorInput());
			} else {
				if (selection instanceof IStructuredSelection) {
					for (Iterator<?> iterator = ((IStructuredSelection) selection).iterator(); iterator.hasNext();) {
						Object object = iterator.next();
						objectsToAdapt.add(object);
					}
				}
			} 
		}
	}

	/**
	 * Return the undo context associated with operations performed in this view. By default, return
	 * the workspace undo context. Subclasses should override if a more specific undo context should
	 * be used.
	 *
	 * @since 3.7
	 */
	protected IUndoContext getUndoContext() {
		return Adapters.adapt(ResourcesPlugin.getWorkspace(), IUndoContext.class);
	} 

	/**
	 * Tells whether this view is visible.
	 * <p>
	 * See bug 401632 why we can't use {@link IWorkbenchPage#isPartVisible(IWorkbenchPart)}.
	 * </p>
	 *
	 * @return <code>true</code> if this view is visible, <code>false</code> otherwise
	 */
	boolean isVisible() {
		return isViewVisible;
	}
}