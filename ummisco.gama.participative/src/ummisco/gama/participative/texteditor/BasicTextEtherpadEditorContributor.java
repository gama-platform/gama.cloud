package ummisco.gama.participative.texteditor;

import org.dslforge.styledtext.ITextSelection;
import org.dslforge.styledtext.TextSelection;
import org.dslforge.texteditor.BasicTextEditorContributor;
import org.dslforge.texteditor.IBasicTextEditor;
import org.dslforge.texteditor.actions.CopyAction;
import org.dslforge.texteditor.actions.CutAction;
import org.dslforge.texteditor.actions.DeleteAction;
import org.dslforge.texteditor.actions.PasteAction;
import org.dslforge.texteditor.actions.RedoAction;
import org.dslforge.texteditor.actions.UndoAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;

public class BasicTextEtherpadEditorContributor extends BasicTextEditorContributor {// EditorActionBarContributor implements IPropertyListener {


	public BasicTextEtherpadEditorContributor() {
	}

	
	/**
	 * Returns the action registered with the given text editor.
	 *
	 * @param etherpadBasicTextEditor the editor, or <code>null</code>
	 * @param actionId the action id
	 * @return the action, or <code>null</code> if none
	 */
	
	protected final IAction getAction(EtherpadBasicTextEditor etherpadBasicTextEditor, String actionId) {
		return (etherpadBasicTextEditor == null || actionId == null ? null : getAction(actionId));
	}

	
	private IAction getAction(String actionId) {
		if (actionId.equals(ActionFactory.COPY.getId()))
			return copyAction;
		if (actionId.equals(ActionFactory.CUT.getId()))
			return cutAction;
		if (actionId.equals(ActionFactory.PASTE.getId()))
			return pasteAction;
		if (actionId.equals(ActionFactory.UNDO.getId()))
			return undoAction;
		if (actionId.equals(ActionFactory.REDO.getId()))
			return redoAction;
		return null;
	}


}
