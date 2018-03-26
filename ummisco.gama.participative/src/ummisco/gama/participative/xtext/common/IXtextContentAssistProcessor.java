package ummisco.gama.participative.xtext.common;

import ummisco.gama.participative.EtherpadBasicText;
import ummisco.gama.participative.styledtext.jface.IContentAssistProcessor;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.contentassist.CompletionProposalComputer;

public interface IXtextContentAssistProcessor extends IContentAssistProcessor{

	CompletionProposalComputer createCompletionProposalComputer(EtherpadBasicText textWidget, XtextResource xtextResource, int offset);
}

