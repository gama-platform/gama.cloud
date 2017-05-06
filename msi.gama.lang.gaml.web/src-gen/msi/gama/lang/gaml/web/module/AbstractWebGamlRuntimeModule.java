/**
 * @Generated
 */
package msi.gama.lang.gaml.web.module;

import org.dslforge.xtext.common.shared.SharedModule;
import com.google.inject.Binder;

import msi.gama.lang.gaml.web.contentassist.GamlProposalProvider;
import msi.gama.lang.gaml.web.contentassist.antlr.GamlParser;
import msi.gama.lang.gaml.web.contentassist.antlr.internal.InternalGamlLexer;
import org.dslforge.styledtext.jface.IContentAssistProcessor;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.LexerUIBindings;
import org.eclipse.xtext.ui.editor.contentassist.XtextContentAssistProcessor;

public abstract class AbstractWebGamlRuntimeModule extends SharedModule {

	@Override
	public void configure(Binder binder) {
		super.configure(binder);
		binder.bind(org.eclipse.xtext.ui.editor.contentassist.IContentAssistParser.class).to(GamlParser.class);
		binder.bind(InternalGamlLexer.class)
				.toProvider(org.eclipse.xtext.parser.antlr.LexerProvider.create(InternalGamlLexer.class));
		binder.bind(org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer.class)
				.annotatedWith(com.google.inject.name.Names.named(LexerUIBindings.CONTENT_ASSIST))
				.to(InternalGamlLexer.class);
		binder.bind(org.eclipse.xtext.ui.editor.contentassist.IContentProposalProvider.class)
				.to(GamlProposalProvider.class);
		binder.bind(IContentAssistProcessor.class).to(XtextContentAssistProcessor.class);
		binder.bind(ContentAssistContext.Factory.class)
				.to(org.eclipse.xtext.ui.editor.contentassist.ParserBasedContentAssistContextFactory.class);
		binder.bind(org.eclipse.xtext.ui.editor.contentassist.PrefixMatcher.class)
				.to(org.eclipse.xtext.ui.editor.contentassist.PrefixMatcher.IgnoreCase.class);


	}
}
