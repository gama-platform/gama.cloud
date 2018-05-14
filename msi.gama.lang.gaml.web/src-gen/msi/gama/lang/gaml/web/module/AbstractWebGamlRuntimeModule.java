/**
 * @Generated
 */
package msi.gama.lang.gaml.web.module;

import org.dslforge.styledtext.jface.IContentAssistProcessor;
import org.dslforge.xtext.common.shared.SharedModule;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.LexerUIBindings;

import com.google.inject.Binder;

import msi.gama.lang.gaml.parser.antlr.GamlParser;
import msi.gama.lang.gaml.web.contentassist.GamlContentAssistProcessor;
import msi.gama.lang.gaml.web.contentassist.GamlProposalProvider;
import msi.gama.lang.gaml.web.contentassist.antlr.GamlParserWeb;
import msi.gama.lang.gaml.web.contentassist.antlr.internal.InternalGamlLexerWeb;

public abstract class AbstractWebGamlRuntimeModule extends SharedModule {

	@Override
	public void configure(Binder binder) {
		super.configure(binder);
		binder.bind(org.eclipse.xtext.ui.editor.contentassist.IContentAssistParser.class).to(GamlParserWeb.class);
		binder.bind(InternalGamlLexerWeb.class)
				.toProvider(org.eclipse.xtext.parser.antlr.LexerProvider.create(InternalGamlLexerWeb.class));
		binder.bind(org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer.class)
				.annotatedWith(com.google.inject.name.Names.named(LexerUIBindings.CONTENT_ASSIST))
				.to(InternalGamlLexerWeb.class);
		binder.bind(org.eclipse.xtext.ui.editor.contentassist.IContentProposalProvider.class)
				.to(GamlProposalProvider.class);
		binder.bind(IContentAssistProcessor.class).to(GamlContentAssistProcessor.class);
		binder.bind(ContentAssistContext.Factory.class)
				.to(org.eclipse.xtext.ui.editor.contentassist.ParserBasedContentAssistContextFactory.class);
		binder.bind(org.eclipse.xtext.ui.editor.contentassist.PrefixMatcher.class)
				.to(org.eclipse.xtext.ui.editor.contentassist.PrefixMatcher.IgnoreCase.class);


	}
}
