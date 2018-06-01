/*********************************************************************************************
 *
 * 'EditorSearchControls.java, in plugin ummisco.gama.ui.modeling, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.web.ui.views.toolbar;

import java.util.ArrayList;
import java.util.HashMap;

import org.dslforge.styledtext.TextRange;
import org.dslforge.workspace.jpa.database.User;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.IFindReplaceTargetExtension;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ToolItem;

import msi.gama.lang.gaml.web.editor.participative.EtherpadEditor;
import msi.gaml.operators.Cast;
import ummisco.gama.participative.EtherpadBasicText;
import ummisco.gama.ui.views.toolbar.GamaToolbarSimple;

/**
 * The class EditToolbarFindControls.
 *
 * @author drogoul
 * @since 5 déc. 2014
 *
 */
public class CollaboratingUserControlsEtherpad {

	private static final String EMPTY = RWT.getUISession().getAttribute("user").toString(); //$NON-NLS-1$
	private HashMap<User,ToolItem> collaborating=new HashMap<User,ToolItem>();
	
	
	
	
	private int incrementalOffset = -1;
	final EtherpadEditor editor;

	public CollaboratingUserControlsEtherpad(final EtherpadEditor editor) {
		this.editor = editor;
		
	}
	
	

	public synchronized CollaboratingUserControlsEtherpad fill(final GamaToolbarSimple toolbar) {
		ArrayList<User> onlines= (ArrayList<User>) RWT.getApplicationContext().getAttribute("onlines");
		
		//HashMap<User,ArrayList<EtherpadBasicText>> btList = new HashMap<User,ArrayList<EtherpadBasicText>>();
		
		// HashMap<User,ArrayList<EtherpadBasicText>> btList = (HashMap<User,ArrayList<EtherpadBasicText>>) RWT.getApplicationContext().getAttribute("editorsList"); 
		// ArrayList<EtherpadBasicText> listBt = (ArrayList<EtherpadBasicText>) RWT.getApplicationContext().getAttribute("editors");
		//	if (btList == null) {
		//		btList = new HashMap<User,ArrayList<EtherpadBasicText>>();
		//	}
		
		for(User u:collaborating.keySet()) {
			if(collaborating.get(u)!=null && !collaborating.get(u).isDisposed() && !onlines.contains(u)) {
				collaborating.get(u).getControl().dispose();
				collaborating.get(u).dispose();
				collaborating.put(u, null);
			}
		}
		for(User u : onlines) {
			if (collaborating.get(u) == null && !u.getId().equals(RWT.getUISession().getAttribute("user").toString()) && (editor!=null
					&& editor.getXtextResource().getURI().toFileString().equals(u.getOrganization()))) {
				//
				Button btn = new Button(toolbar, SWT.PUSH | SWT.DOUBLE_BUFFERED);
				btn.setText(u.getId());

				// final IFocusService focusService =
				// editor.getSite().getService(IFocusService.class);
				// focusService.addFocusTracker(find, "search");

				// find.setBackground(IGamaColors.WHITE.color());
				// btn.setForeground(IGamaColors.BLACK.color());
				btn.addSelectionListener(new SelectionListener() {
					
					@Override
						public void widgetSelected(SelectionEvent arg0) {
							// TODO Auto-generated method stub
							int offset = Cast.asInt(null, u.getLastName());
							int line=editor.getViewer().getTextWidget().getLineAtOffset(offset);
							TextRange t=new TextRange(line, line, line, line);
							editor.getViewer().getTextWidget().clearMarkers();
							editor.getViewer().getTextWidget().addMarker(t);
	//						TextRange t=new TextRange(line, line+1, line, line);
	//						ArrayList a=new ArrayList<>(); a.add(t);
	//						editor.getViewer().getTextWidget().clearMarkers();
	//						editor.getViewer().getTextWidget().setMarkers(a);
						}
						
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				ToolItem t = toolbar.control(btn, u.getId().length() * 10);
				// this.adjustEnablement(false, null);
				collaborating.put(u, t);
			
			}
		}

		return this;
	}

//	public Button getFindControl() {
//		return find;
//	}

//	private final ModifyListener modifyListener = new ModifyListener() {
//
//		private String lastText = EMPTY;
//
//		@Override
//		public void modifyText(final ModifyEvent e) {
//
//			boolean wrap = true;
//			final String text = find.getText();
//			if (lastText.startsWith(text)) {
//				wrap = false;
//			}
//			lastText = text;
//			if (EMPTY.equals(text) || "".equals(text)) {
//				adjustEnablement(false, null);
//				final ISelectionProvider selectionProvider = editor.getSelectionProvider();
//				if (selectionProvider != null) {
//					final ISelection selection = selectionProvider.getSelection();
//					if (selection instanceof TextSelection) {
//						final ITextSelection textSelection = (ITextSelection) selection;
//						selectionProvider.setSelection(new TextSelection(textSelection.getOffset(), 0));
//					}
//				}
//			} else {
//				find(true, true, wrap);
//			}
//		}
//	};

	private void adjustEnablement(final boolean found, final Color color) {
//		final String text = find.getText();
//		if (color == null) {
//			find.setForeground(IGamaColors.BLACK.color());
//			// composite.setBackground(IGamaColors.VERY_LIGHT_GRAY.color());
//		} else {
//			find.setForeground(color);
//			// composite.setBackground(color);
//		}
	}

	private void findPrevious() {
		find(false);
	}

	public void findNext() {
		find(true);
	}

	private void find(final boolean forward) {
		find(forward, false);
	}

	private void find(final boolean forward, final boolean incremental) {
		find(forward, incremental, true, false);
	}

	private void find(final boolean forward, final boolean incremental, final boolean wrap) {
		find(forward, incremental, wrap, false);
	}

	private void find(final boolean forward, final boolean incremental, final boolean wrap, final boolean wrapping) {

		final IFindReplaceTarget findReplaceTarget = (IFindReplaceTarget) editor.getAdapter(IFindReplaceTarget.class);
		if (findReplaceTarget != null) {
			try {
//				final String findText = find.getText();
//				if (findReplaceTarget instanceof IFindReplaceTargetExtension) {
//					final IFindReplaceTargetExtension findReplaceTargetExtension =
//							(IFindReplaceTargetExtension) findReplaceTarget;
//					findReplaceTargetExtension.beginSession();
//				}
////				final ISourceViewer sourceViewer = getSourceViewer();
////				final StyledText textWidget = sourceViewer.getTextWidget();
//				BasicText textWidget=editor.getViewer().getTextWidget();
//						
//				int offset = textWidget.getOffsetAtCursorPosition();//.getCaretOffset();
//				org.dslforge.styledtext.ITextSelection selection = textWidget.getSelection();
//				if (wrapping) {
//					if (forward) {
//						offset = 0;
//					} else {
//						offset = textWidget.getText().getBytes().length-1;//sourceViewer.getDocument().getLength() - 1;
//					}
//				} else {
//					if (forward) {
//						if (incremental) {
//							if (incrementalOffset == -1) {
//								incrementalOffset = offset;
//							} else {
//								offset = incrementalOffset;
//							}
//						} else {
//							incrementalOffset = selection.getOffset();//.x;
//						}
//					} else {
//						incrementalOffset = selection.getOffset();
//						if (selection.getOffset() != offset) {
//							offset = selection.getOffset();
//						}
//					}
//				}
//				int newOffset = -1;
//				if (findReplaceTarget instanceof IFindReplaceTargetExtension3) {
//					newOffset = ((IFindReplaceTargetExtension3) findReplaceTarget).findAndSelect(offset, findText,
//							forward, false, false, false);
//
//				} else {
//					newOffset = findReplaceTarget.findAndSelect(offset, findText, forward, false, false);
//				}
//
//				if (newOffset != -1) {
//					adjustEnablement(true, IGamaColors.OK.inactive());
//					selection = textWidget.getSelection();
//					if (!forward) {
//						incrementalOffset = selection.getOffset();
//					}
//				} else {
//					if (wrap) {
//						if (!wrapping) {
//							find(forward, incremental, wrap, true);
//							return;
//						}
//					}
//					if (!EMPTY.equals(findText) && !"".equals(findText)) {
//						adjustEnablement(false, IGamaColors.ERROR.inactive());
//					}
//				}
			} finally {

				if (findReplaceTarget instanceof IFindReplaceTargetExtension) {
					final IFindReplaceTargetExtension findReplaceTargetExtension =
							(IFindReplaceTargetExtension) findReplaceTarget;
					findReplaceTargetExtension.endSession();
				}
			}
		}
	}

	/**
	 * @return the sourceView of the active textEditor
	 */
	private ISourceViewer getSourceViewer() {
		return (ISourceViewer) editor.getAdapter(ITextOperationTarget.class);
	}

}
