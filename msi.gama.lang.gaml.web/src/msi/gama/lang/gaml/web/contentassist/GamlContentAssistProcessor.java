package msi.gama.lang.gaml.web.contentassist;

import org.dslforge.styledtext.jface.ICompletionProposal;
import org.dslforge.styledtext.jface.ITextViewer;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.contentassist.XtextContentAssistProcessor;

public class GamlContentAssistProcessor extends XtextContentAssistProcessor {

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, XtextResource resource, int offset) {
		// TODO Auto-generated method stub
		return super.computeCompletionProposals(viewer, resource, offset);
	}

}
