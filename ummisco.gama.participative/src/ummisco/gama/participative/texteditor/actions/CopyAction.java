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
package ummisco.gama.participative.texteditor.actions;

import ummisco.gama.participative.ITextSelection;
import ummisco.gama.participative.TextSelection;
import ummisco.gama.participative.TextSelectionListenerAction;
import ummisco.gama.participative.styledtext.jface.TextViewer;
import ummisco.gama.participative.texteditor.EtherpadBasicTextEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;

public class CopyAction extends TextSelectionListenerAction {

	private static final long serialVersionUID = 1L;
	private EtherpadBasicTextEditor activeEditor;

	public CopyAction(String text) {
		super(text);
	}
	
	@Override
	public String getId() {
		return ActionFactory.COPY.getId();
	}
	
	@Override
	public void run() {
		if (activeEditor != null) {
			TextViewer viewer = (TextViewer) activeEditor.getViewer();
			TextSelection selection = (TextSelection) viewer.getSelection();
			activeEditor.performCopy(selection);
		}
	}
	
	@Override
	public boolean updateSelection(ITextSelection structuredSelection) {
		return super.updateSelection(structuredSelection);
	}

	public void setActiveWorkbenchPart(IEditorPart activeEditor) {
		if (activeEditor instanceof EtherpadBasicTextEditor)
			this.activeEditor = (EtherpadBasicTextEditor) activeEditor;
	}

}
