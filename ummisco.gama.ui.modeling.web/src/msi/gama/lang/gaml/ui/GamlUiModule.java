/*********************************************************************************************
 *
 * 'GamlUiModule.java, in plugin ummisco.gama.ui.modeling, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.lang.gaml.ui;

/**
 * Use this class to register components to be used within the IDE.
 */
public class GamlUiModule {//extends msi.gama.lang.gaml.ui.AbstractGamlUiModule {
//
//	public GamlUiModule(final AbstractUIPlugin plugin) {
//		super(plugin);
//	}
//
//	@SuppressWarnings ("unchecked")
//	@Override
//	public void configure(final Binder binder) {
//
//		super.configure(binder);
//		binder.bind(String.class).annotatedWith(
//				com.google.inject.name.Names.named(XtextContentAssistProcessor.COMPLETION_AUTO_ACTIVATION_CHARS))
//				.toInstance(".");
//		binder.bind(IContentAssistParser.class).to((Class<? extends IContentAssistParser>) GamlParser.class);
//		binder.bind(Lexer.class).annotatedWith(Names.named(LexerIdeBindings.CONTENT_ASSIST))
//				.to(InternalGamlLexer.class);
//		binder.bind(IResourceLoader.class).toProvider(ResourceLoaderProviders.getParallelLoader());
//		binder.bind(IResourceClusteringPolicy.class).to(DynamicResourceClusteringPolicy.class);
//		binder.bind(IModelRunner.class).to(ModelRunner.class);
//		// binder.bind(XtextDocumentProvider.class).to(XtextDocumentProvider.class);
//		binder.bind(IMarkerUpdater.class).to(GamlMarkerUpdater.class);
//		binder.bind(IGamlLabelProvider.class).to(GamlLabelProvider.class);
//	}
//
//	@Override
//	public void configureUiEncodingProvider(final Binder binder) {
//		binder.bind(IEncodingProvider.class).annotatedWith(DispatchingProvider.Ui.class).to(GamlEncodingProvider.class);
//	}
//
//	public Class<? extends org.eclipse.xtext.ui.editor.contentassist.antlr.ParserBasedContentAssistContextFactory.StatefulFactory>
//			bindParserBasedContentAssistContextFactory$StatefulFactory() {
//		return msi.gama.lang.gaml.ui.contentassist.ContentAssistContextFactory.class;
//	}
//
//	public Class<? extends XtextSourceViewer.Factory> bindSourceViewerFactory() {
//		return GamaSourceViewerFactory.class;
//	}
//
//	@Override
//	@SingletonBinding (
//			eager = true)
//	public Class<? extends org.eclipse.jface.viewers.ILabelProvider> bindILabelProvider() {
//		return msi.gama.lang.gaml.ui.labeling.GamlLabelProvider.class;
//	}
//
//	@Override
//	public Class<? extends ITemplateProposalProvider> bindITemplateProposalProvider() {
//		return GamlTemplateProposalProvider.class;
//	}
//
//	public Class<? extends IFoldingRegionProvider> bindFoldingRegionProvider() {
//		return GamaFoldingRegionProvider.class;
//	}
//
//	@Override
//	public Class<? extends org.eclipse.jface.text.ITextHover> bindITextHover() {
//		return GamlDispatchingEObjectTextHover.class;
//	}
//
//	// For performance issues on opening files : see
//	// http://alexruiz.developerblogs.com/?p=2359
//	@Override
//	public Class<? extends IResourceSetProvider> bindIResourceSetProvider() {
//		return SimpleResourceSetProvider.class;
//	}
//
//	@Override
//	public void configureXtextEditorErrorTickUpdater(final com.google.inject.Binder binder) {
//		binder.bind(IXtextEditorCallback.class).annotatedWith(Names.named("IXtextEditorCallBack")).to( //$NON-NLS-1$
//				GamlEditorTickUpdater.class);
//	}
//
//	/**
//	 * @author Pierrick
//	 * @return GAMLSemanticHighlightingCalculator
//	 */
//	public Class<? extends ISemanticHighlightingCalculator> bindSemanticHighlightingCalculator() {
//		return GamlSemanticHighlightingCalculator.class;
//	}
//
//	public Class<? extends IHighlightingConfiguration> bindIHighlightingConfiguration() {
//		return GamlHighlightingConfiguration.class;
//	}
//
//	@Override
//	public Class<? extends org.eclipse.xtext.ui.editor.IXtextEditorCallback> bindIXtextEditorCallback() {
//		// TODO Verify this as it is only needed, normally, for languages that
//		// do not use the builder infrastructure
//		// (see http://www.eclipse.org/forums/index.php/mv/msg/167666/532239/)
//		// not correct for 2.7: return GamlEditorCallback.class;
//		return IXtextEditorCallback.NullImpl.class;
//	}
//
//	public Class<? extends ISyntaxErrorMessageProvider> bindISyntaxErrorMessageProvider() {
//		return GamlSyntaxErrorMessageProvider.class;
//	}
//
//	public Class<? extends IEObjectHoverProvider> bindIEObjectHoverProvider() {
//		return GamlHoverProvider.class;
//	}
//
//	public Class<? extends IEObjectDocumentationProvider> bindIEObjectDocumentationProviderr() {
//		return GamlDocumentationProvider.class;
//	}
//
//	@Override
//	public Provider<IAllContainersState> provideIAllContainersState() {
//		return org.eclipse.xtext.ui.shared.Access.getWorkspaceProjectsState();
//	}
//
//	public Class<? extends XtextEditor> bindXtextEditor() {
//		return GamlEditor.class;
//	}
//
//	public Class<? extends XtextSourceViewerConfiguration> bindXtextSourceViewerConfiguration() {
//		return GamaSourceViewerConfiguration.class;
//	}
//
//	@Override
//	public Class<? extends IHyperlinkDetector> bindIHyperlinkDetector() {
//		return GamlHyperlinkDetector.class;
//	}
//
//	@Override
//	public void configureBracketMatchingAction(final Binder binder) {
//		// actually we want to override the first binding only...
//		binder.bind(IActionContributor.class).annotatedWith(Names.named("foldingActionGroup")).to( //$NON-NLS-1$
//				GamaFoldingActionContributor.class);
//		binder.bind(IActionContributor.class).annotatedWith(Names.named("bracketMatcherAction")).to( //$NON-NLS-1$
//				org.eclipse.xtext.ui.editor.bracketmatching.GoToMatchingBracketAction.class);
//		binder.bind(IPreferenceStoreInitializer.class).annotatedWith(Names.named("bracketMatcherPrefernceInitializer")) //$NON-NLS-1$
//				.to(org.eclipse.xtext.ui.editor.bracketmatching.BracketMatchingPreferencesInitializer.class);
//		binder.bind(IActionContributor.class).annotatedWith(Names.named("selectionActionGroup")).to( //$NON-NLS-1$
//				org.eclipse.xtext.ui.editor.selection.AstSelectionActionContributor.class);
//	}
//
//	@Override
//	public void configureMarkOccurrencesAction(final Binder binder) {
//		binder.bind(IActionContributor.class).annotatedWith(Names.named("markOccurrences"))
//				.to(GamlMarkOccurrenceActionContributor.class);
//		binder.bind(IPreferenceStoreInitializer.class).annotatedWith(Names.named("GamlMarkOccurrenceActionContributor")) //$NON-NLS-1$
//				.to(GamlMarkOccurrenceActionContributor.class);
//	}
//
//	@Override
//	public Class<? extends IResourceForEditorInputFactory> bindIResourceForEditorInputFactory() {
//		return ResourceForIEditorInputFactory.class;
//	}
//
//	@Override
//	public Class<? extends IContentOutlinePage> bindIContentOutlinePage() {
//		return GamlOutlinePage.class;
//	}
//
//	@Override
//	public Class<? extends IImageHelper> bindIImageHelper() {
//		return GamlImageHelper.class;
//	}
//
//	@Override
//	public Class<? extends IImageDescriptorHelper> bindIImageDescriptorHelper() {
//		return GamlImageHelper.class;
//	}
//
//	@Override
//	public void configureIOutlineContribution$Composite(final Binder binder) {
//		binder.bind(IPreferenceStoreInitializer.class).annotatedWith(IOutlineContribution.All.class)
//				.to(IOutlineContribution.Composite.class);
//	}
//
//	@Override
//	public Class<? extends AbstractEditStrategyProvider> bindAbstractEditStrategyProvider() {
//		return GamaAutoEditStrategyProvider.class;
//	}
//
//	@Override
//	public void configureToggleSortingOutlineContribution(final Binder binder) {
//		binder.bind(IOutlineContribution.class).annotatedWith(IOutlineContribution.Sort.class)
//				.to(GamlSortOutlineContribution.class);
//	}
//
//	@Override
//	public void configureToggleLinkWithEditorOutlineContribution(final Binder binder) {
//		binder.bind(IOutlineContribution.class).annotatedWith(IOutlineContribution.LinkWithEditor.class)
//				.to(GamlLinkWithEditorOutlineContribution.class);
//	}
//
//	@Override
//	@SingletonBinding
//	public Class<? extends TemplateStore> bindTemplateStore() {
//		return GamlTemplateStore.class;
//	}
//
//	@Override
//	public Class<? extends IReconciler> bindIReconciler() {
//		return GamlReconciler.class;
//	}

}
