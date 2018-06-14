package msi.gama.lang.gaml.web.editor.participative;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dslforge.styledtext.Annotation;
import org.dslforge.styledtext.Position;
import org.dslforge.styledtext.jface.ITextViewer;
import org.dslforge.workspace.jpa.database.User;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.service.UISession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import msi.gama.core.web.editor.GamlEditorState;
import msi.gama.lang.gaml.validation.IGamlBuilderListener;
import msi.gama.lang.gaml.web.editor.AbstractGamlEtherpadEditor;
import msi.gama.lang.gaml.web.ui.views.toolbar.CollaboratingUserControlsEtherpad;
import msi.gama.lang.gaml.web.ui.views.toolbar.OpenExperimentSelectionListenerEtherpad;
import msi.gama.lang.gaml.web.ui.views.toolbar.OpenImportedErrorSelectionListenerEtherpad;
import msi.gama.lang.gaml.web.ui.views.toolbar.RevalidateModelSelectionListenerEtherpad;
import msi.gaml.descriptions.IDescription;
import msi.gaml.descriptions.ValidationContext;
import net.gjerull.etherpad.client.EPLiteClient;
import ummisco.gama.participative.EtherpadBasicText;
//import ummisco.gama.participative.EtherpadComposite;
import ummisco.gama.participative.texteditor.EtherpadBasicTextEditor;
import ummisco.gama.ui.controls.FlatButton;
import ummisco.gama.ui.interfaces.IModelRunner;
import ummisco.gama.ui.resources.GamaColors.GamaUIColor;
import ummisco.gama.ui.resources.GamaIcons;
import ummisco.gama.ui.resources.IGamaColors;
import ummisco.gama.ui.resources.IGamaIcons;
import ummisco.gama.ui.utils.ModelRunner;
import ummisco.gama.ui.utils.WorkbenchHelper;
import ummisco.gama.ui.views.toolbar.GamaToolbar2;
import ummisco.gama.ui.views.toolbar.GamaToolbarFactory;
import ummisco.gama.ui.views.toolbar.IToolbarDecoratedView;
import ummisco.gama.ui.views.toolbar.Selector;

/*
 * The class EtherpadEditor.
 *
 * @author drogoul
 *
 * @since 4 mars 2012 
 */
@SuppressWarnings ("all")
public class EtherpadEditor extends AbstractGamlEtherpadEditor  implements IGamlBuilderListener, IToolbarDecoratedView {

	static {
//		final IPreferenceStore store = EditorsUI.getPreferenceStore();
//		store.setDefault(AbstractDecoratedTextEditorPreferenceConstants.SHOW_RANGE_INDICATOR, false);
//		store.setDefault(SpellingService.PREFERENCE_SPELLING_ENABLED, false);
//		store.setValue(SpellingService.PREFERENCE_SPELLING_ENABLED, false);

	}
	GamlEditorState state = new GamlEditorState(null, Collections.EMPTY_LIST);
	
	
	
	IModelRunner runner =new ModelRunner();
	private TemplateStore templateStore;
	static final String EDITOR_ID = "msi.gama.lang.gaml.web.editor.EtherpadEditor";
	//private EtherpadComposite epEditor;
	private Composite epEditor;
	
	public EtherpadEditor() {
		super();
		
		
		  ArrayList<EtherpadEditor> listEtherpadEd = (ArrayList<EtherpadEditor>) RWT.getApplicationContext().getAttribute("etherpadEditors");;
			if (listEtherpadEd == null) {
				listEtherpadEd = new ArrayList<>();
			}
			listEtherpadEd.add(this);
		    RWT.getApplicationContext().setAttribute("etherpadEditors", listEtherpadEd);
	}

	
	public static HashMap<String,GamaToolbar2> thetoolbar=new HashMap<String,GamaToolbar2>();
	public static HashMap<String,Composite> thetoolbarParent=new HashMap<String,Composite>();
//	private EditorSearchControls findControl;
	public static HashMap<String,CollaboratingUserControlsEtherpad>  thecollaboratingControl=new HashMap<String, CollaboratingUserControlsEtherpad>();
//	boolean decorationEnabled = AutoStartup.EDITBOX_ENABLED.getValue();
//	boolean editToolbarEnabled = AutoStartup.EDITOR_SHOW_TOOLBAR.getValue();
//	@Override
//	protected ITextViewer createTextViewer(Composite parent, int styles) {
//		final BasicText textWidget = createTextWidget(parent, styles);
//		return (org.dslforge.styledtext.jface.TextViewer) new GamaTextViewer(textWidget, parent, styles,xtextResource);
//	}

	
	public Composite getComposite() {
	
		return epEditor;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		setSite(site);
		setPartName(input.getName());
		setInput(input);
		setDirty(false);
		
		String uid=RWT.getUISession().getAttribute("user").toString();
		
		
		ArrayList<User> onlines= (ArrayList<User>) RWT.getApplicationContext().getAttribute("onlines");
		for(User us:onlines) {
			if(us.getId().equals(uid)) {
				us.setOrganization(xtextResource.getURI().toFileString());
			}
		}
		RWT.getApplicationContext().setAttribute("onlines", onlines);

		
	}

	private void configureTabFolder(final Composite compo) {
		Composite c = compo;
		while (c != null) {
			if (c instanceof CTabFolder)
				break;
			c = c.getParent();
		}
		if (c != null) {
			final CTabFolder folder = (CTabFolder) c;
			folder.setMaximizeVisible(true);
			folder.setMinimizeVisible(true);
			folder.setMinimumCharacters(10);
			folder.setMRUVisible(true);
			// Makes sure the listener is added only once
			folder.removeMouseListener(FIX_FOR_ISSUE_2108);
			folder.addMouseListener(FIX_FOR_ISSUE_2108);
		}

	}

	// Fix for #2108 -- forces the selection of the "clicked" tab
	private static MouseAdapter FIX_FOR_ISSUE_2108 = new MouseAdapter() {

		@Override
		public void mouseUp(final MouseEvent e) {
			// System.out.println("MOUSE up IN TAB FOLDER");
			final CTabFolder folder = (CTabFolder) e.widget;
			final int x = e.x;
			final int y = e.y;
			for (final CTabItem item : folder.getItems()) {
				final Rectangle r = item.getBounds();
				if (r.contains(x, y) && !item.equals(folder.getSelection())) {
					System.out.println("Detected problem in editors tab selection (see #2108). Fixed.");
					folder.setSelection(item);
					folder.update();
					return;
				}
			}
		}

	};

	@Override
	public void createToolItems(GamaToolbar2 tb) {
		
		thetoolbar.put(RWT.getUISession().getAttribute("user").toString(), tb);
		buildRightToolbar();		
	}

	private void buildRightToolbar() {
		GamaToolbar2 toolbar=thetoolbar.get(""+RWT.getUISession().getAttribute("user"));
		toolbar.wipe(SWT.LEFT, true);
		final ToolItem t = toolbar.button(IGamaColors.NEUTRAL, "Waiting...", GamaIcons.create("status.clock").image(),
				null, SWT.LEFT);
		toolbar.sep(4, SWT.LEFT);

//		findControl = new EditorSearchControls(this).fill(toolbar.getToolbar(SWT.RIGHT));
		toolbar.refresh(true);
	}


	public XtextResource getXtextResource(){
		return xtextResource;
	}

	
	@Override
	public void createPartControl(final Composite compo) {
		
		configureTabFolder(compo);
		Composite toolbarParent 	= GamaToolbarFactory.createToolbars(this, compo);
		final GridLayout layout 	= new GridLayout(1, false);
		layout.horizontalSpacing 	= 0;
		layout.verticalSpacing 		= 0;
		layout.marginWidth 			= 0;
		layout.marginHeight 		= 0;
		layout.marginLeft 			= 0; //0
		layout.marginRight 			= -5;
		toolbarParent.setLayout(layout);
		toolbarParent.setBackground(IGamaColors.WHITE.color());

		// Asking the editor to fill the rest
		epEditor=new Composite(toolbarParent,SWT.BORDER);
		final GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		epEditor.setLayoutData(data);
		epEditor.setLayout(new FillLayout());
		super.createPartControl(epEditor);
		setResourceListener(this);
		validateResource();
		String uid=RWT.getUISession().getAttribute("user").toString();
		epEditor.layout();
		toolbarParent.layout();
		
	
		
		thetoolbarParent.put(uid, toolbarParent);
		CollaboratingUserControlsEtherpad collaboratingControl=new CollaboratingUserControlsEtherpad(this).fill(thetoolbar.get(uid).getToolbar(SWT.RIGHT));
		thecollaboratingControl.put(uid, collaboratingControl);
	
		IResourceValidator resourceValidator = xtextResource.getResourceServiceProvider()
							.getResourceValidator();
	
		List<Issue> issues = resourceValidator.validate(xtextResource, CheckMode.NORMAL_AND_FAST,
								CancelIndicator.NullImpl);
		
	//	basic.createAnnotations(issues);
		List<Annotation> annotations = new ArrayList<Annotation>();
		for (Issue issue : issues) {
					Integer offset = issue.getOffset();
					Integer line = issue.getLineNumber();
					int lineNumber = line.intValue();
					String message = issue.getMessage();
					Severity severity = issue.getSeverity();
					System.out.println("Le message :-> "+message);
					//annotations.add(new Annotation(convertSeverity(severity), lineNumber, offset, message));
					annotations.add(new Annotation(null, lineNumber, offset, message));
		
		}
	
		this.padId = getFilePath().toFile().getName().toString();
		openEtherpaEditor(getFilePath().toString());
		
		
	}
	
	
	



public void openEtherpaEditor(final String absolutePath) {
		
	
		
		
		try {
			String content = new String(Files.readAllBytes(Paths.get(absolutePath)));
	//	Map padList = (Map) ((EtherpadBasicText)epClient.listAllPads());
		//	Iterator entries = Map.entrySet().iterator();
	
			String text = null;
			try {
				text = epClient.getText(this.padId).get("text").toString(); 
			}catch(Exception e){
				
			}
			
			
			
			if(text !=null) {
				Map<String,Object> lastEdited = epClient.getLastEdited(this.padId);
				Date date = new Date((long) lastEdited.get("lastEdited"));
				
				Map<String,Object> authorsList = epClient.listAuthorsOfPad(this.padId);
				//Date date = new Date((long) authorsList.get("lastEdited")); listAuthorsOfPad
				epClient.createAuthor((String) RWT.getUISession().getAttribute("user"));
				System.out.println("----> Pad exists " +this.padId + " - it's last edited date is: "+date);
				System.out.println("----> Its athors list is: " +authorsList);
				
			
				
			}else{
				System.out.println(" ----> Pad dosn't exists " +this.padId);
				epClient.createPad(this.padId);
				epClient.setText(this.padId, content);
				String padContent = epClient.getText(this.padId).get("text").toString();
				//System.out.println(" The pad content is :"+ padContent);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected synchronized void handleTextChanged(JsonObject object) {
		// TODO Auto-generated method stub
		
		super.handleTextChanged(object);
		String uid=RWT.getUISession().getAttribute("user").toString();
		int offset=getViewer().getTextWidget().getOffsetAtCursorPosition();
//		System.out.println(uid+" at "+offset+" : ");
		String value = object.get("value") != null ? object.get("value").asString() : null;	
		
		//System.out.println("uID "+uid+" --> handleTextChanged on padId: "+this.padId);
		//((EtherpadBasicText)getViewer().getTextWidget()).setText(uid, value, this.padId);
		((EtherpadBasicText)getViewer().getTextWidget()).setCollaborativeText(uid, value, this.padId);
	
		ArrayList<User> onlines= (ArrayList<User>) RWT.getApplicationContext().getAttribute("onlines");
		//getViewer().getTextWidget().setCursorPosition(new Position(0, 0));
		//((EtherpadBasicText)getViewer().getTextWidget()).setCursorPosition(2, 2);
		for(User u:onlines) {
			if(u.getId().equals(uid)) {
				u.setOrganization(xtextResource.getURI().toFileString());
				u.setLastName(""+offset);
			}
		}

		RWT.getApplicationContext().setAttribute("onlines", onlines);
		
		
	}

	private synchronized void enableButton(final int index, final String text, final SelectionListener listener) {
		if (text == null) { return; }
		final boolean isBatch = state.types.get(index);
		final Image image = isBatch ? GamaIcons.create(IGamaIcons.BUTTON_BATCH).image()
				: GamaIcons.create(IGamaIcons.BUTTON_GUI).image();
		GamaToolbar2 toolbar=thetoolbar.get(""+RWT.getUISession().getAttribute("user"));
		final ToolItem t = toolbar.button(IGamaColors.OK,
				text/* + "  " + GamaKeyBindings.format(GamlEditorBindings.MODIFIERS, String.valueOf(index).charAt(0)) */,
				image, SWT.LEFT);
		final String type = isBatch ? "batch" : "regular";
		t.getControl().setToolTipText("Executes the " + type + " experiment " + text);
		((FlatButton) t.getControl()).addSelectionListener(listener);
		t.setData("index", index);
		((FlatButton) t.getControl()).setData("exp", text);
		toolbar.sep(4, SWT.LEFT);
	}

	private synchronized void updateToolbar(final GamlEditorState newState, final boolean forceState) {
//		if (forceState || !state.equals(newState)) {
		String uid = RWT.getUISession().getAttribute("user").toString();
		WorkbenchHelper.getDisplay(uid).asyncExec(() -> {
			GamaToolbar2 toolbar = thetoolbar.get(uid);
			if (toolbar == null || toolbar.isDisposed()) {
				return;
			}
			toolbar.wipe(SWT.LEFT, true);

			thecollaboratingControl.get(uid).fill(toolbar.getToolbar(SWT.RIGHT));
			final GamaUIColor c = state.getColor();
			String msg = state.getStatus();

			Selector listener = null;
			String imageName = null;

			if (msg == GamlEditorState.NO_EXP_DEFINED) {
//				listener = new CreateExperimentSelectionListenerEtherpad(EtherpadEditor.this, toolbar.getToolbar(SWT.LEFT));
//				imageName = "small.dropdown";
				msg = null;
			} else if (newState.hasImportedErrors) {
				listener = new OpenImportedErrorSelectionListenerEtherpad(EtherpadEditor.this, newState,
						toolbar.getToolbar(SWT.LEFT));
				imageName = "small.dropdown";
			} else if (msg != null) {
				listener = new RevalidateModelSelectionListenerEtherpad(EtherpadEditor.this);
				imageName = "marker.error2";
			} else {
				listener = new OpenExperimentSelectionListenerEtherpad(EtherpadEditor.this, newState, runner);
			}

			if (msg != null) {
				final ToolItem t = toolbar.button(c, msg, GamaIcons.create(imageName).image(), listener, SWT.LEFT);

				// without the following line, the display of the
				// text "msg" is not updated
				// correctly (at least for Windows OS)
				toolbar.sep(4, SWT.LEFT);
			} else {
				int i = 0;
				for (final String e : state.abbreviations) {
					enableButton(i++, e, listener);
				}

			}
			toolbar.refresh(true);

		});
//		}
	
	}

	@Override
	public void validationEnded(Iterable<? extends IDescription> newExperiments,
			final ValidationContext status) {
		if (newExperiments == null && state != null)
			updateToolbar(state, true);
		else {
			final GamlEditorState newState = new GamlEditorState(status, newExperiments);
			updateToolbar(newState, false);
			state = newState;
		}
		
		
		
		
		
	}
	


	


//	public GamlTemplateStore getTemplateStore() {
//		return (GamlTemplateStore) templateStore;
//	}
}



