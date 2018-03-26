package ummisco.gama.participative;

import java.util.Arrays;

import org.apache.log4j.Logger;
//import ummisco.gama.participative.jface.IContentAssistProcessor;

import ummisco.gama.participative.styledtext.jface.ICompletionProposal;
import ummisco.gama.participative.styledtext.jface.IContentAssistProcessor;
import ummisco.gama.participative.ITextViewer;
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

	private ITextViewer viewer;


	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		throw new UnsupportedOperationException("Unhandled operation");
	}
	
	public  ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, XtextResource resource, int offset) {
		this.viewer = viewer;
		if (contentProposalProvider == null)
			return null;
		ICompletionProposal[] result = null;
		try {
			CompletionProposalComputer completionProposalComputer = createCompletionProposalComputer(getViewer(), offset);
			result = (ICompletionProposal[]) completionProposalComputer.exec(resource);
			Arrays.sort(result, completionProposalComparator);
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
	public ITextViewer getViewer() {
		return viewer;
	}
	
	public void setViewer(ITextViewer viewer) {
		this.viewer = viewer;
	}


	
}
