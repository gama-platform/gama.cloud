package ummisco.gama.participative;

import java.util.Arrays;

import org.apache.log4j.Logger;
//import ummisco.gama.participative.jface.IContentAssistProcessor;


//import ummisco.gama.participative.ITextViewer;
import org.dslforge.styledtext.jface.ITextViewer;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.contentassist.CompletionProposalComputer;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalComparator;
import org.eclipse.xtext.ui.editor.contentassist.IContentProposalProvider;
import org.eclipse.xtext.ui.editor.contentassist.XtextContentAssistProcessor;

import com.google.inject.Inject;

public class XtextContentAssistProcessorEtherpad extends XtextContentAssistProcessor{

	public  XtextContentAssistProcessorEtherpad() {
		super();
	}
	
	
	static final Logger logger = Logger.getLogger(XtextContentAssistProcessorEtherpad.class);
	
	@Inject
	private ContentAssistContext.Factory contextFactory;

	@Inject
	private IContentProposalProvider contentProposalProvider;

	@Inject
	private ICompletionProposalComparator completionProposalComparator;

	private org.dslforge.styledtext.jface.ITextViewer viewer;


	public org.dslforge.styledtext.jface.ICompletionProposal[] computeCompletionProposals(org.dslforge.styledtext.jface.ITextViewer viewer, int offset) {
		throw new UnsupportedOperationException("Unhandled operation");
	}
	
	public  org.dslforge.styledtext.jface.ICompletionProposal[] computeCompletionProposals(org.dslforge.styledtext.jface.ITextViewer viewer, XtextResource resource, int offset) {
		this.viewer = viewer;
		if (contentProposalProvider == null)
			return null;
		org.dslforge.styledtext.jface.ICompletionProposal[] result = null;
		try {
			CompletionProposalComputer completionProposalComputer = createCompletionProposalComputer(getViewer(), offset);
			result = (org.dslforge.styledtext.jface.ICompletionProposal[]) completionProposalComputer.exec(resource);
			Arrays.sort(result);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return result;
	} 

	public CompletionProposalComputer createCompletionProposalComputer(ITextViewer viewer, int offset) {
		this.viewer = viewer;
		return new CompletionProposalComputer(this, offset);
	}

	public void setContextFactory(ContentAssistContext.Factory contextFactory) {
		this.contextFactory = contextFactory;
	}

	public ContentAssistContext.Factory getContextFactory() {
		return contextFactory;
	}

	public void setContentProposalProvider(IContentProposalProvider contentProposalProvider) {
		this.contentProposalProvider = contentProposalProvider;
	}

	public IContentProposalProvider getContentProposalProvider() {
		return contentProposalProvider;
	}

	public ICompletionProposalAcceptor decorateAcceptor(ICompletionProposalAcceptor acceptor) {
		return acceptor;
	}
	
	@Override
	public org.dslforge.styledtext.jface.ITextViewer getViewer() {
		return viewer;
	}
	
	public void setViewer(ITextViewer viewer) {
		this.viewer = viewer;
	}


	
}
