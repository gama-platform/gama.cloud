package ummisco.gama.participative;




import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import ummisco.gama.participative.styledtext.jface.IDocument;

public class EtherpadTextViewer extends Viewer implements ITextViewer{

	private static final long serialVersionUID = -5997421207828153511L;
	
	private EtherpadBasicText fTextWidget;
	private IDocument fDocument;
	private Control fDisposedControl;
	
	/**
	 * Create a new text viewer with the given style bits.
	 */
	public EtherpadTextViewer(Composite parent, int styles) {
		createControl(parent, styles);
	}

	/**
	 * Create a new text viewer with the given style bits.
	 */
	public EtherpadTextViewer(EtherpadBasicText textWidget, Composite parent, int styles) {
		fTextWidget = textWidget;
		configure();
	}
	
	/**
	 * Creates the viewer's control. The viewer's text widget either is
	 * the control or is a child of the control.
	 */
	protected void createControl(Composite parent, int styles) {
		fTextWidget = createTextWidget(parent, styles);
		configure();
	}

	private void configure() {
		GridData textLayoutData = new GridData();
		textLayoutData.horizontalAlignment = SWT.FILL;
		textLayoutData.verticalAlignment = SWT.FILL;
		textLayoutData.grabExcessHorizontalSpace = true;
		textLayoutData.grabExcessVerticalSpace = true;
		fTextWidget.setLayoutData(textLayoutData);
		
		fTextWidget.addTraverseListener(new TraverseListener() {
			private static final long serialVersionUID = 1L;

			public void keyTraversed(TraverseEvent e) {
				if ((SWT.SHIFT == e.stateMask) && ('\t' == e.character))
					e.doit= !fTextWidget.getEditable();
			}
		});
		fTextWidget.addDisposeListener(new DisposeListener() {
			private static final long serialVersionUID = 1L;

			public void widgetDisposed(DisposeEvent e) {
				fDisposedControl = getControl();
				handleDispose();
			}
		});
	}

	/**
	 * Factory method to create the text widget to be used as the viewer's text widget.
	 *
	 * @param parent the parent of the styled text
	 * @param styles the styles for the styled text
	 * @return the text widget to be used
	 */
	protected EtherpadBasicText createTextWidget(Composite parent, int styles) {
		EtherpadBasicText styledText= new EtherpadBasicText(parent, styles);
		return styledText;
	}

	@Override
	public Control getControl() {
		return fTextWidget != null ? fTextWidget : fDisposedControl;
	}

	@Override
	public Object getInput() {
		return getDocument();
	}
	
	@Override
	public ISelection getSelection() {
		if (fTextWidget != null ) {
			ITextSelection selection = fTextWidget.getSelection();
			return new TextSelection(getDocument(), selection.getOffset(), selection.getLength());
		}
		return TextSelection.emptySelection();
	}

	@Override
	public void refresh() {
		setDocument(getDocument());
	}

	@Override
	public void setInput(Object input) {
		IDocument document= null;
		if (input instanceof IDocument)
			document= (IDocument) input;
		setDocument(document);
	}

	@Override
	public void setSelection(ISelection selection) {
		if (selection instanceof ITextSelection) {
			ITextSelection textSelection= (ITextSelection) selection;
			fTextWidget.setSelection(textSelection.getOffset(), textSelection.getLength());
		}
	}

	@Override
	public EtherpadBasicText getTextWidget() {
		return fTextWidget;
	}

	@Override
	public void setDocument(IDocument document) {
		fDocument= document;
	}

	@Override
	public void setText(String text) {
		fDocument.set(text);
	}

	@Override
	public IDocument getDocument() {
		return fDocument;
	}

	@Override
	public boolean isEditable() {
		if (fTextWidget == null)
			return false;
		return fTextWidget.getEditable();
	}

	@Override
	public void setEditable(boolean editable) {
		if (fTextWidget != null)
			fTextWidget.setEditable(editable);
	}

	@Override
	public ISelectionProvider getSelectionProvider() {
		return this;
	}

	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		if (reveal)
			setSelection(selection);
	}
	
	/**
	 * Frees all resources allocated by this viewer.
	 */
	protected void handleDispose() {
		fTextWidget= null;
	}
}

