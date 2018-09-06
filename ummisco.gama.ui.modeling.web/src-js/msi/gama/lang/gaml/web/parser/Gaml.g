/**
 * @Generated
 */
grammar Gaml;

options {
  language=JavaScript;
  output=AST;
  ASTLabelType=CommonTree;
}

@lexer::header {
package msi.gama.lang.gaml.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

@parser::header {
package msi.gama.lang.gaml.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import msi.gama.lang.gaml.services.GamlGrammarAccess;

}

@parser::members {

 	private GamlGrammarAccess grammarAccess;
 	
    public InternalGamlParser(TokenStream input, GamlGrammarAccess grammarAccess) {
        this(input);
        this.grammarAccess = grammarAccess;
        registerRules(grammarAccess.getGrammar());
    }
    
    @Override
    protected String getFirstRuleName() {
    	return "Entry";	
   	}
   	
   	@Override
   	protected GamlGrammarAccess getGrammarAccess() {
   		return grammarAccess;
   	}
}

@rulecatch { 
    catch (RecognitionException re) { 
        recover(input,re); 
        appendSkippedTokens();
    } 
}




// Entry rule entryRuleEntry
entryRuleEntry returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getEntryRule()); }
	 iv_ruleEntry=ruleEntry 
	 { $current=$iv_ruleEntry.current; } 
	 EOF 
;

// Rule Entry
ruleEntry returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getEntryAccess().getModelParserRuleCall_0()); 
    }
    this_Model_0=ruleModel
    { 
        $current = $this_Model_0.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getEntryAccess().getStringEvaluatorParserRuleCall_1()); 
    }
    this_StringEvaluator_1=ruleStringEvaluator
    { 
        $current = $this_StringEvaluator_1.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getEntryAccess().getStandaloneBlockParserRuleCall_2()); 
    }
    this_StandaloneBlock_2=ruleStandaloneBlock
    { 
        $current = $this_StandaloneBlock_2.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getEntryAccess().getExperimentFileStructureParserRuleCall_3()); 
    }
    this_ExperimentFileStructure_3=ruleExperimentFileStructure
    { 
        $current = $this_ExperimentFileStructure_3.current; 
        afterParserOrEnumRuleCall();
    }
)
;





// Entry rule entryRuleStandaloneBlock
entryRuleStandaloneBlock returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getStandaloneBlockRule()); }
	 iv_ruleStandaloneBlock=ruleStandaloneBlock 
	 { $current=$iv_ruleStandaloneBlock.current; } 
	 EOF 
;

// Rule StandaloneBlock
ruleStandaloneBlock returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='__synthetic__' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getStandaloneBlockAccess().get__synthetic__Keyword_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getStandaloneBlockAccess().getBlockBlockParserRuleCall_1_0()); 
	    }
		lv_block_1_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStandaloneBlockRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_1_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleStringEvaluator
entryRuleStringEvaluator returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getStringEvaluatorRule()); }
	 iv_ruleStringEvaluator=ruleStringEvaluator 
	 { $current=$iv_ruleStringEvaluator.current; } 
	 EOF 
;

// Rule StringEvaluator
ruleStringEvaluator returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		lv_toto_0_0=RULE_ID
		{
			newLeafNode(lv_toto_0_0, grammarAccess.getStringEvaluatorAccess().getTotoIDTerminalRuleCall_0_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getStringEvaluatorRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"toto",
        		lv_toto_0_0, 
        		"msi.gama.lang.gaml.Gaml.ID");
	    }

)
)	otherlv_1='<-' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getStringEvaluatorAccess().getLessThanSignHyphenMinusKeyword_1());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getStringEvaluatorAccess().getExprExpressionParserRuleCall_2_0()); 
	    }
		lv_expr_2_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStringEvaluatorRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_2_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleModel
entryRuleModel returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getModelRule()); }
	 iv_ruleModel=ruleModel 
	 { $current=$iv_ruleModel.current; } 
	 EOF 
;

// Rule Model
ruleModel returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getModelAccess().getPragmasPragmaParserRuleCall_0_0()); 
	    }
		lv_pragmas_0_0=rulePragma		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getModelRule());
	        }
       		add(
       			$current, 
       			"pragmas",
        		lv_pragmas_0_0, 
        		"msi.gama.lang.gaml.Gaml.Pragma");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_1='model' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getModelAccess().getModelKeyword_1());
    }
(
(
		lv_name_2_0=RULE_ID
		{
			newLeafNode(lv_name_2_0, grammarAccess.getModelAccess().getNameIDTerminalRuleCall_2_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getModelRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_2_0, 
        		"msi.gama.lang.gaml.Gaml.ID");
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getModelAccess().getImportsImportParserRuleCall_3_0()); 
	    }
		lv_imports_3_0=ruleImport		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getModelRule());
	        }
       		add(
       			$current, 
       			"imports",
        		lv_imports_3_0, 
        		"msi.gama.lang.gaml.Gaml.Import");
	        afterParserOrEnumRuleCall();
	    }

)
)*(
(
		{ 
	        newCompositeNode(grammarAccess.getModelAccess().getBlockModelBlockParserRuleCall_4_0()); 
	    }
		lv_block_4_0=ruleModelBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getModelRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_4_0, 
        		"msi.gama.lang.gaml.Gaml.ModelBlock");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleModelBlock
entryRuleModelBlock returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getModelBlockRule()); }
	 iv_ruleModelBlock=ruleModelBlock 
	 { $current=$iv_ruleModelBlock.current; } 
	 EOF 
;

// Rule ModelBlock
ruleModelBlock returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getModelBlockAccess().getBlockAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getModelBlockAccess().getStatementsS_SectionParserRuleCall_1_0()); 
	    }
		lv_statements_1_0=ruleS_Section		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getModelBlockRule());
	        }
       		add(
       			$current, 
       			"statements",
        		lv_statements_1_0, 
        		"msi.gama.lang.gaml.Gaml.S_Section");
	        afterParserOrEnumRuleCall();
	    }

)
)*)
;





// Entry rule entryRuleImport
entryRuleImport returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getImportRule()); }
	 iv_ruleImport=ruleImport 
	 { $current=$iv_ruleImport.current; } 
	 EOF 
;

// Rule Import
ruleImport returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='import' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getImportAccess().getImportKeyword_0());
    }
(
(
		lv_importURI_1_0=RULE_STRING
		{
			newLeafNode(lv_importURI_1_0, grammarAccess.getImportAccess().getImportURISTRINGTerminalRuleCall_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getImportRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"importURI",
        		lv_importURI_1_0, 
        		"msi.gama.lang.gaml.Gaml.STRING");
	    }

)
)(	otherlv_2='as' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getImportAccess().getAsKeyword_2_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getImportAccess().getNameValid_IDParserRuleCall_2_1_0()); 
	    }
		lv_name_3_0=ruleValid_ID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getImportRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_3_0, 
        		"msi.gama.lang.gaml.Gaml.Valid_ID");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRulePragma
entryRulePragma returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getPragmaRule()); }
	 iv_rulePragma=rulePragma 
	 { $current=$iv_rulePragma.current; } 
	 EOF 
;

// Rule Pragma
rulePragma returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='@' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getPragmaAccess().getCommercialAtKeyword_0());
    }
(
(
		lv_name_1_0=RULE_ID
		{
			newLeafNode(lv_name_1_0, grammarAccess.getPragmaAccess().getNameIDTerminalRuleCall_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getPragmaRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_1_0, 
        		"msi.gama.lang.gaml.Gaml.ID");
	    }

)
))
;





// Entry rule entryRuleExperimentFileStructure
entryRuleExperimentFileStructure returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getExperimentFileStructureRule()); }
	 iv_ruleExperimentFileStructure=ruleExperimentFileStructure 
	 { $current=$iv_ruleExperimentFileStructure.current; } 
	 EOF 
;

// Rule ExperimentFileStructure
ruleExperimentFileStructure returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
(
		{ 
	        newCompositeNode(grammarAccess.getExperimentFileStructureAccess().getExpHeadlessExperimentParserRuleCall_0()); 
	    }
		lv_exp_0_0=ruleHeadlessExperiment		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getExperimentFileStructureRule());
	        }
       		set(
       			$current, 
       			"exp",
        		lv_exp_0_0, 
        		"msi.gama.lang.gaml.Gaml.HeadlessExperiment");
	        afterParserOrEnumRuleCall();
	    }

)
)
;





// Entry rule entryRuleHeadlessExperiment
entryRuleHeadlessExperiment returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getHeadlessExperimentRule()); }
	 iv_ruleHeadlessExperiment=ruleHeadlessExperiment 
	 { $current=$iv_ruleHeadlessExperiment.current; } 
	 EOF 
;

// Rule HeadlessExperiment
ruleHeadlessExperiment returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getHeadlessExperimentAccess().getKey_ExperimentKeyParserRuleCall_0_0()); 
	    }
		lv_key_0_0=rule_ExperimentKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getHeadlessExperimentRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml._ExperimentKey");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		lv_firstFacet_1_0=	'name:' 
    {
        newLeafNode(lv_firstFacet_1_0, grammarAccess.getHeadlessExperimentAccess().getFirstFacetNameKeyword_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getHeadlessExperimentRule());
	        }
       		setWithLastConsumed($current, "firstFacet", lv_firstFacet_1_0, "name:");
	    }

)
)?(
(
(
		{ 
	        newCompositeNode(grammarAccess.getHeadlessExperimentAccess().getNameValid_IDParserRuleCall_2_0_0()); 
	    }
		lv_name_2_1=ruleValid_ID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getHeadlessExperimentRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_2_1, 
        		"msi.gama.lang.gaml.Gaml.Valid_ID");
	        afterParserOrEnumRuleCall();
	    }

    |		lv_name_2_2=RULE_STRING
		{
			newLeafNode(lv_name_2_2, grammarAccess.getHeadlessExperimentAccess().getNameSTRINGTerminalRuleCall_2_0_1()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getHeadlessExperimentRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_2_2, 
        		"msi.gama.lang.gaml.Gaml.STRING");
	    }

)

)
)	otherlv_3='model:' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getHeadlessExperimentAccess().getModelKeyword_3());
    }
(
(
		lv_importURI_4_0=RULE_STRING
		{
			newLeafNode(lv_importURI_4_0, grammarAccess.getHeadlessExperimentAccess().getImportURISTRINGTerminalRuleCall_4_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getHeadlessExperimentRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"importURI",
        		lv_importURI_4_0, 
        		"msi.gama.lang.gaml.Gaml.STRING");
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getHeadlessExperimentAccess().getFacetsFacetParserRuleCall_5_0()); 
	    }
		lv_facets_5_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getHeadlessExperimentRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_5_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*((
(
		{ 
	        newCompositeNode(grammarAccess.getHeadlessExperimentAccess().getBlockBlockParserRuleCall_6_0_0()); 
	    }
		lv_block_6_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getHeadlessExperimentRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_6_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |	otherlv_7=';' 
    {
    	newLeafNode(otherlv_7, grammarAccess.getHeadlessExperimentAccess().getSemicolonKeyword_6_1());
    }
))
;





// Entry rule entryRuleS_Section
entryRuleS_Section returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_SectionRule()); }
	 iv_ruleS_Section=ruleS_Section 
	 { $current=$iv_ruleS_Section.current; } 
	 EOF 
;

// Rule S_Section
ruleS_Section returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getS_SectionAccess().getS_GlobalParserRuleCall_0()); 
    }
    this_S_Global_0=ruleS_Global
    { 
        $current = $this_S_Global_0.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getS_SectionAccess().getS_SpeciesParserRuleCall_1()); 
    }
    this_S_Species_1=ruleS_Species
    { 
        $current = $this_S_Species_1.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getS_SectionAccess().getS_ExperimentParserRuleCall_2()); 
    }
    this_S_Experiment_2=ruleS_Experiment
    { 
        $current = $this_S_Experiment_2.current; 
        afterParserOrEnumRuleCall();
    }
)
;





// Entry rule entryRuleS_Global
entryRuleS_Global returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_GlobalRule()); }
	 iv_ruleS_Global=ruleS_Global 
	 { $current=$iv_ruleS_Global.current; } 
	 EOF 
;

// Rule S_Global
ruleS_Global returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		lv_key_0_0=	'global' 
    {
        newLeafNode(lv_key_0_0, grammarAccess.getS_GlobalAccess().getKeyGlobalKeyword_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_GlobalRule());
	        }
       		setWithLastConsumed($current, "key", lv_key_0_0, "global");
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_GlobalAccess().getFacetsFacetParserRuleCall_1_0()); 
	    }
		lv_facets_1_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_GlobalRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_1_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*((
(
		{ 
	        newCompositeNode(grammarAccess.getS_GlobalAccess().getBlockBlockParserRuleCall_2_0_0()); 
	    }
		lv_block_2_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_GlobalRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_2_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |	otherlv_3=';' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getS_GlobalAccess().getSemicolonKeyword_2_1());
    }
))
;





// Entry rule entryRuleS_Species
entryRuleS_Species returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_SpeciesRule()); }
	 iv_ruleS_Species=ruleS_Species 
	 { $current=$iv_ruleS_Species.current; } 
	 EOF 
;

// Rule S_Species
ruleS_Species returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getS_SpeciesAccess().getKey_SpeciesKeyParserRuleCall_0_0()); 
	    }
		lv_key_0_0=rule_SpeciesKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_SpeciesRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml._SpeciesKey");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		lv_firstFacet_1_0=	'name:' 
    {
        newLeafNode(lv_firstFacet_1_0, grammarAccess.getS_SpeciesAccess().getFirstFacetNameKeyword_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_SpeciesRule());
	        }
       		setWithLastConsumed($current, "firstFacet", lv_firstFacet_1_0, "name:");
	    }

)
)?(
(
		lv_name_2_0=RULE_ID
		{
			newLeafNode(lv_name_2_0, grammarAccess.getS_SpeciesAccess().getNameIDTerminalRuleCall_2_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_SpeciesRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_2_0, 
        		"msi.gama.lang.gaml.Gaml.ID");
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_SpeciesAccess().getFacetsFacetParserRuleCall_3_0()); 
	    }
		lv_facets_3_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_SpeciesRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_3_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*((
(
		{ 
	        newCompositeNode(grammarAccess.getS_SpeciesAccess().getBlockBlockParserRuleCall_4_0_0()); 
	    }
		lv_block_4_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_SpeciesRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_4_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |	otherlv_5=';' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getS_SpeciesAccess().getSemicolonKeyword_4_1());
    }
))
;





// Entry rule entryRuleS_Experiment
entryRuleS_Experiment returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_ExperimentRule()); }
	 iv_ruleS_Experiment=ruleS_Experiment 
	 { $current=$iv_ruleS_Experiment.current; } 
	 EOF 
;

// Rule S_Experiment
ruleS_Experiment returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getS_ExperimentAccess().getKey_ExperimentKeyParserRuleCall_0_0()); 
	    }
		lv_key_0_0=rule_ExperimentKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_ExperimentRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml._ExperimentKey");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		lv_firstFacet_1_0=	'name:' 
    {
        newLeafNode(lv_firstFacet_1_0, grammarAccess.getS_ExperimentAccess().getFirstFacetNameKeyword_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_ExperimentRule());
	        }
       		setWithLastConsumed($current, "firstFacet", lv_firstFacet_1_0, "name:");
	    }

)
)?(
(
(
		{ 
	        newCompositeNode(grammarAccess.getS_ExperimentAccess().getNameValid_IDParserRuleCall_2_0_0()); 
	    }
		lv_name_2_1=ruleValid_ID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_ExperimentRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_2_1, 
        		"msi.gama.lang.gaml.Gaml.Valid_ID");
	        afterParserOrEnumRuleCall();
	    }

    |		lv_name_2_2=RULE_STRING
		{
			newLeafNode(lv_name_2_2, grammarAccess.getS_ExperimentAccess().getNameSTRINGTerminalRuleCall_2_0_1()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_ExperimentRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_2_2, 
        		"msi.gama.lang.gaml.Gaml.STRING");
	    }

)

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_ExperimentAccess().getFacetsFacetParserRuleCall_3_0()); 
	    }
		lv_facets_3_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_ExperimentRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_3_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*((
(
		{ 
	        newCompositeNode(grammarAccess.getS_ExperimentAccess().getBlockBlockParserRuleCall_4_0_0()); 
	    }
		lv_block_4_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_ExperimentRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_4_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |	otherlv_5=';' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getS_ExperimentAccess().getSemicolonKeyword_4_1());
    }
))
;





// Entry rule entryRuleStatement
entryRuleStatement returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getStatementRule()); }
	 iv_ruleStatement=ruleStatement 
	 { $current=$iv_ruleStatement.current; } 
	 EOF 
;

// Rule Statement
ruleStatement returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((((	ruleS_Declaration)=>
    { 
        newCompositeNode(grammarAccess.getStatementAccess().getS_DeclarationParserRuleCall_0_0()); 
    }
    this_S_Declaration_0=ruleS_Declaration
    { 
        $current = $this_S_Declaration_0.current; 
        afterParserOrEnumRuleCall();
    }
)
    |(((	ruleS_Assignment)=>
    { 
        newCompositeNode(grammarAccess.getStatementAccess().getS_AssignmentParserRuleCall_0_1_0()); 
    }
    this_S_Assignment_1=ruleS_Assignment
    { 
        $current = $this_S_Assignment_1.current; 
        afterParserOrEnumRuleCall();
    }
)
    |
    { 
        newCompositeNode(grammarAccess.getStatementAccess().getS_1Expr_Facets_BlockOrEndParserRuleCall_0_1_1()); 
    }
    this_S_1Expr_Facets_BlockOrEnd_2=ruleS_1Expr_Facets_BlockOrEnd
    { 
        $current = $this_S_1Expr_Facets_BlockOrEnd_2.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getStatementAccess().getS_OtherParserRuleCall_0_1_2()); 
    }
    this_S_Other_3=ruleS_Other
    { 
        $current = $this_S_Other_3.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getStatementAccess().getS_DoParserRuleCall_0_1_3()); 
    }
    this_S_Do_4=ruleS_Do
    { 
        $current = $this_S_Do_4.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getStatementAccess().getS_ReturnParserRuleCall_0_1_4()); 
    }
    this_S_Return_5=ruleS_Return
    { 
        $current = $this_S_Return_5.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getStatementAccess().getS_SolveParserRuleCall_0_1_5()); 
    }
    this_S_Solve_6=ruleS_Solve
    { 
        $current = $this_S_Solve_6.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getStatementAccess().getS_IfParserRuleCall_0_1_6()); 
    }
    this_S_If_7=ruleS_If
    { 
        $current = $this_S_If_7.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getStatementAccess().getS_EquationsParserRuleCall_0_1_7()); 
    }
    this_S_Equations_8=ruleS_Equations
    { 
        $current = $this_S_Equations_8.current; 
        afterParserOrEnumRuleCall();
    }
))
    |
    { 
        newCompositeNode(grammarAccess.getStatementAccess().getS_DisplayParserRuleCall_1()); 
    }
    this_S_Display_9=ruleS_Display
    { 
        $current = $this_S_Display_9.current; 
        afterParserOrEnumRuleCall();
    }
)
;





// Entry rule entryRuleS_1Expr_Facets_BlockOrEnd
entryRuleS_1Expr_Facets_BlockOrEnd returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_1Expr_Facets_BlockOrEndRule()); }
	 iv_ruleS_1Expr_Facets_BlockOrEnd=ruleS_1Expr_Facets_BlockOrEnd 
	 { $current=$iv_ruleS_1Expr_Facets_BlockOrEnd.current; } 
	 EOF 
;

// Rule S_1Expr_Facets_BlockOrEnd
ruleS_1Expr_Facets_BlockOrEnd returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getS_1Expr_Facets_BlockOrEndAccess().getKey_1Expr_Facets_BlockOrEnd_KeyParserRuleCall_0_0()); 
	    }
		lv_key_0_0=rule_1Expr_Facets_BlockOrEnd_Key		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_1Expr_Facets_BlockOrEndRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml._1Expr_Facets_BlockOrEnd_Key");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_1Expr_Facets_BlockOrEndAccess().getFirstFacetFirstFacetKeyParserRuleCall_1_0()); 
	    }
		lv_firstFacet_1_0=ruleFirstFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_1Expr_Facets_BlockOrEndRule());
	        }
       		set(
       			$current, 
       			"firstFacet",
        		lv_firstFacet_1_0, 
        		"msi.gama.lang.gaml.Gaml.FirstFacetKey");
	        afterParserOrEnumRuleCall();
	    }

)
)?(((
(
ruleExpression
)
)=>
(
		{ 
	        newCompositeNode(grammarAccess.getS_1Expr_Facets_BlockOrEndAccess().getExprExpressionParserRuleCall_2_0_0()); 
	    }
		lv_expr_2_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_1Expr_Facets_BlockOrEndRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_2_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |(
(
		{ 
	        newCompositeNode(grammarAccess.getS_1Expr_Facets_BlockOrEndAccess().getExprTypeRefParserRuleCall_2_1_0()); 
	    }
		lv_expr_3_0=ruleTypeRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_1Expr_Facets_BlockOrEndRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_3_0, 
        		"msi.gama.lang.gaml.Gaml.TypeRef");
	        afterParserOrEnumRuleCall();
	    }

)
))(
(
		{ 
	        newCompositeNode(grammarAccess.getS_1Expr_Facets_BlockOrEndAccess().getFacetsFacetParserRuleCall_3_0()); 
	    }
		lv_facets_4_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_1Expr_Facets_BlockOrEndRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_4_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*((
(
		{ 
	        newCompositeNode(grammarAccess.getS_1Expr_Facets_BlockOrEndAccess().getBlockBlockParserRuleCall_4_0_0()); 
	    }
		lv_block_5_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_1Expr_Facets_BlockOrEndRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_5_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |	otherlv_6=';' 
    {
    	newLeafNode(otherlv_6, grammarAccess.getS_1Expr_Facets_BlockOrEndAccess().getSemicolonKeyword_4_1());
    }
))
;





// Entry rule entryRuleS_Do
entryRuleS_Do returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_DoRule()); }
	 iv_ruleS_Do=ruleS_Do 
	 { $current=$iv_ruleS_Do.current; } 
	 EOF 
;

// Rule S_Do
ruleS_Do returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getS_DoAccess().getKey_DoKeyParserRuleCall_0_0()); 
	    }
		lv_key_0_0=rule_DoKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DoRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml._DoKey");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		lv_firstFacet_1_0=	'action:' 
    {
        newLeafNode(lv_firstFacet_1_0, grammarAccess.getS_DoAccess().getFirstFacetActionKeyword_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_DoRule());
	        }
       		setWithLastConsumed($current, "firstFacet", lv_firstFacet_1_0, "action:");
	    }

)
)?(
(
		{ 
	        newCompositeNode(grammarAccess.getS_DoAccess().getExprAbstractRefParserRuleCall_2_0()); 
	    }
		lv_expr_2_0=ruleAbstractRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DoRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_2_0, 
        		"msi.gama.lang.gaml.Gaml.AbstractRef");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_DoAccess().getFacetsFacetParserRuleCall_3_0()); 
	    }
		lv_facets_3_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DoRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_3_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*((
(
		{ 
	        newCompositeNode(grammarAccess.getS_DoAccess().getBlockBlockParserRuleCall_4_0_0()); 
	    }
		lv_block_4_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DoRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_4_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |	otherlv_5=';' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getS_DoAccess().getSemicolonKeyword_4_1());
    }
))
;





// Entry rule entryRuleS_Loop
entryRuleS_Loop returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_LoopRule()); }
	 iv_ruleS_Loop=ruleS_Loop 
	 { $current=$iv_ruleS_Loop.current; } 
	 EOF 
;

// Rule S_Loop
ruleS_Loop returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		lv_key_0_0=	'loop' 
    {
        newLeafNode(lv_key_0_0, grammarAccess.getS_LoopAccess().getKeyLoopKeyword_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_LoopRule());
	        }
       		setWithLastConsumed($current, "key", lv_key_0_0, "loop");
	    }

)
)(
(
		lv_name_1_0=RULE_ID
		{
			newLeafNode(lv_name_1_0, grammarAccess.getS_LoopAccess().getNameIDTerminalRuleCall_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_LoopRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_1_0, 
        		"msi.gama.lang.gaml.Gaml.ID");
	    }

)
)?(
(
		{ 
	        newCompositeNode(grammarAccess.getS_LoopAccess().getFacetsFacetParserRuleCall_2_0()); 
	    }
		lv_facets_2_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_LoopRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_2_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*(
(
		{ 
	        newCompositeNode(grammarAccess.getS_LoopAccess().getBlockBlockParserRuleCall_3_0()); 
	    }
		lv_block_3_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_LoopRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_3_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleS_If
entryRuleS_If returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_IfRule()); }
	 iv_ruleS_If=ruleS_If 
	 { $current=$iv_ruleS_If.current; } 
	 EOF 
;

// Rule S_If
ruleS_If returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		lv_key_0_0=	'if' 
    {
        newLeafNode(lv_key_0_0, grammarAccess.getS_IfAccess().getKeyIfKeyword_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_IfRule());
	        }
       		setWithLastConsumed($current, "key", lv_key_0_0, "if");
	    }

)
)(
(
		lv_firstFacet_1_0=	'condition:' 
    {
        newLeafNode(lv_firstFacet_1_0, grammarAccess.getS_IfAccess().getFirstFacetConditionKeyword_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_IfRule());
	        }
       		setWithLastConsumed($current, "firstFacet", lv_firstFacet_1_0, "condition:");
	    }

)
)?(
(
		{ 
	        newCompositeNode(grammarAccess.getS_IfAccess().getExprExpressionParserRuleCall_2_0()); 
	    }
		lv_expr_2_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_IfRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_2_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_IfAccess().getBlockBlockParserRuleCall_3_0()); 
	    }
		lv_block_3_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_IfRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_3_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
)(((	'else' 
)=>	otherlv_4='else' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getS_IfAccess().getElseKeyword_4_0());
    }
)(
(
(
		{ 
	        newCompositeNode(grammarAccess.getS_IfAccess().getElseS_IfParserRuleCall_4_1_0_0()); 
	    }
		lv_else_5_1=ruleS_If		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_IfRule());
	        }
       		set(
       			$current, 
       			"else",
        		lv_else_5_1, 
        		"msi.gama.lang.gaml.Gaml.S_If");
	        afterParserOrEnumRuleCall();
	    }

    |		{ 
	        newCompositeNode(grammarAccess.getS_IfAccess().getElseBlockParserRuleCall_4_1_0_1()); 
	    }
		lv_else_5_2=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_IfRule());
	        }
       		set(
       			$current, 
       			"else",
        		lv_else_5_2, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)

)
))?)
;





// Entry rule entryRuleS_Other
entryRuleS_Other returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_OtherRule()); }
	 iv_ruleS_Other=ruleS_Other 
	 { $current=$iv_ruleS_Other.current; } 
	 EOF 
;

// Rule S_Other
ruleS_Other returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		lv_key_0_0=RULE_ID
		{
			newLeafNode(lv_key_0_0, grammarAccess.getS_OtherAccess().getKeyIDTerminalRuleCall_0_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_OtherRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml.ID");
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_OtherAccess().getFacetsFacetParserRuleCall_1_0()); 
	    }
		lv_facets_1_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_OtherRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_1_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*((
(
		{ 
	        newCompositeNode(grammarAccess.getS_OtherAccess().getBlockBlockParserRuleCall_2_0_0()); 
	    }
		lv_block_2_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_OtherRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_2_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |	otherlv_3=';' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getS_OtherAccess().getSemicolonKeyword_2_1());
    }
))
;





// Entry rule entryRuleS_Return
entryRuleS_Return returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_ReturnRule()); }
	 iv_ruleS_Return=ruleS_Return 
	 { $current=$iv_ruleS_Return.current; } 
	 EOF 
;

// Rule S_Return
ruleS_Return returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		lv_key_0_0=	'return' 
    {
        newLeafNode(lv_key_0_0, grammarAccess.getS_ReturnAccess().getKeyReturnKeyword_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_ReturnRule());
	        }
       		setWithLastConsumed($current, "key", lv_key_0_0, "return");
	    }

)
)(
(
		lv_firstFacet_1_0=	'value:' 
    {
        newLeafNode(lv_firstFacet_1_0, grammarAccess.getS_ReturnAccess().getFirstFacetValueKeyword_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_ReturnRule());
	        }
       		setWithLastConsumed($current, "firstFacet", lv_firstFacet_1_0, "value:");
	    }

)
)?(
(
		{ 
	        newCompositeNode(grammarAccess.getS_ReturnAccess().getExprExpressionParserRuleCall_2_0()); 
	    }
		lv_expr_2_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_ReturnRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_2_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)?	otherlv_3=';' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getS_ReturnAccess().getSemicolonKeyword_3());
    }
)
;





// Entry rule entryRuleS_Declaration
entryRuleS_Declaration returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_DeclarationRule()); }
	 iv_ruleS_Declaration=ruleS_Declaration 
	 { $current=$iv_ruleS_Declaration.current; } 
	 EOF 
;

// Rule S_Declaration
ruleS_Declaration returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(((	ruleS_Definition)=>
    { 
        newCompositeNode(grammarAccess.getS_DeclarationAccess().getS_DefinitionParserRuleCall_0()); 
    }
    this_S_Definition_0=ruleS_Definition
    { 
        $current = $this_S_Definition_0.current; 
        afterParserOrEnumRuleCall();
    }
)
    |
    { 
        newCompositeNode(grammarAccess.getS_DeclarationAccess().getS_SpeciesParserRuleCall_1()); 
    }
    this_S_Species_1=ruleS_Species
    { 
        $current = $this_S_Species_1.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getS_DeclarationAccess().getS_ReflexParserRuleCall_2()); 
    }
    this_S_Reflex_2=ruleS_Reflex
    { 
        $current = $this_S_Reflex_2.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getS_DeclarationAccess().getS_ActionParserRuleCall_3()); 
    }
    this_S_Action_3=ruleS_Action
    { 
        $current = $this_S_Action_3.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getS_DeclarationAccess().getS_VarParserRuleCall_4()); 
    }
    this_S_Var_4=ruleS_Var
    { 
        $current = $this_S_Var_4.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getS_DeclarationAccess().getS_LoopParserRuleCall_5()); 
    }
    this_S_Loop_5=ruleS_Loop
    { 
        $current = $this_S_Loop_5.current; 
        afterParserOrEnumRuleCall();
    }
)
;





// Entry rule entryRuleS_Reflex
entryRuleS_Reflex returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_ReflexRule()); }
	 iv_ruleS_Reflex=ruleS_Reflex 
	 { $current=$iv_ruleS_Reflex.current; } 
	 EOF 
;

// Rule S_Reflex
ruleS_Reflex returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getS_ReflexAccess().getKey_ReflexKeyParserRuleCall_0_0()); 
	    }
		lv_key_0_0=rule_ReflexKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_ReflexRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml._ReflexKey");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		lv_firstFacet_1_0=	'name:' 
    {
        newLeafNode(lv_firstFacet_1_0, grammarAccess.getS_ReflexAccess().getFirstFacetNameKeyword_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_ReflexRule());
	        }
       		setWithLastConsumed($current, "firstFacet", lv_firstFacet_1_0, "name:");
	    }

)
)?(
(
		{ 
	        newCompositeNode(grammarAccess.getS_ReflexAccess().getNameValid_IDParserRuleCall_2_0()); 
	    }
		lv_name_2_0=ruleValid_ID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_ReflexRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_2_0, 
        		"msi.gama.lang.gaml.Gaml.Valid_ID");
	        afterParserOrEnumRuleCall();
	    }

)
)?(	otherlv_3='when' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getS_ReflexAccess().getWhenKeyword_3_0());
    }
	otherlv_4=':' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getS_ReflexAccess().getColonKeyword_3_1());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getS_ReflexAccess().getExprExpressionParserRuleCall_3_2_0()); 
	    }
		lv_expr_5_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_ReflexRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_5_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
))?(
(
		{ 
	        newCompositeNode(grammarAccess.getS_ReflexAccess().getBlockBlockParserRuleCall_4_0()); 
	    }
		lv_block_6_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_ReflexRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_6_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleS_Definition
entryRuleS_Definition returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_DefinitionRule()); }
	 iv_ruleS_Definition=ruleS_Definition 
	 { $current=$iv_ruleS_Definition.current; } 
	 EOF 
;

// Rule S_Definition
ruleS_Definition returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getS_DefinitionAccess().getTkeyTypeRefParserRuleCall_0_0()); 
	    }
		lv_tkey_0_0=ruleTypeRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DefinitionRule());
	        }
       		set(
       			$current, 
       			"tkey",
        		lv_tkey_0_0, 
        		"msi.gama.lang.gaml.Gaml.TypeRef");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		lv_firstFacet_1_0=	'name:' 
    {
        newLeafNode(lv_firstFacet_1_0, grammarAccess.getS_DefinitionAccess().getFirstFacetNameKeyword_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_DefinitionRule());
	        }
       		setWithLastConsumed($current, "firstFacet", lv_firstFacet_1_0, "name:");
	    }

)
)?(
(
(
		{ 
	        newCompositeNode(grammarAccess.getS_DefinitionAccess().getNameValid_IDParserRuleCall_2_0_0()); 
	    }
		lv_name_2_1=ruleValid_ID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DefinitionRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_2_1, 
        		"msi.gama.lang.gaml.Gaml.Valid_ID");
	        afterParserOrEnumRuleCall();
	    }

    |		lv_name_2_2=RULE_STRING
		{
			newLeafNode(lv_name_2_2, grammarAccess.getS_DefinitionAccess().getNameSTRINGTerminalRuleCall_2_0_1()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_DefinitionRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_2_2, 
        		"msi.gama.lang.gaml.Gaml.STRING");
	    }

)

)
)(	otherlv_3='(' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getS_DefinitionAccess().getLeftParenthesisKeyword_3_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getS_DefinitionAccess().getArgsActionArgumentsParserRuleCall_3_1_0()); 
	    }
		lv_args_4_0=ruleActionArguments		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DefinitionRule());
	        }
       		set(
       			$current, 
       			"args",
        		lv_args_4_0, 
        		"msi.gama.lang.gaml.Gaml.ActionArguments");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_5=')' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getS_DefinitionAccess().getRightParenthesisKeyword_3_2());
    }
)?(
(
		{ 
	        newCompositeNode(grammarAccess.getS_DefinitionAccess().getFacetsFacetParserRuleCall_4_0()); 
	    }
		lv_facets_6_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DefinitionRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_6_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*((
(
		{ 
	        newCompositeNode(grammarAccess.getS_DefinitionAccess().getBlockBlockParserRuleCall_5_0_0()); 
	    }
		lv_block_7_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DefinitionRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_7_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |	otherlv_8=';' 
    {
    	newLeafNode(otherlv_8, grammarAccess.getS_DefinitionAccess().getSemicolonKeyword_5_1());
    }
))
;





// Entry rule entryRuleS_Action
entryRuleS_Action returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_ActionRule()); }
	 iv_ruleS_Action=ruleS_Action 
	 { $current=$iv_ruleS_Action.current; } 
	 EOF 
;

// Rule S_Action
ruleS_Action returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getS_ActionAccess().getS_ActionAction_0(),
            $current);
    }
)(
(
		lv_key_1_0=	'action' 
    {
        newLeafNode(lv_key_1_0, grammarAccess.getS_ActionAccess().getKeyActionKeyword_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_ActionRule());
	        }
       		setWithLastConsumed($current, "key", lv_key_1_0, "action");
	    }

)
)(
(
		lv_firstFacet_2_0=	'name:' 
    {
        newLeafNode(lv_firstFacet_2_0, grammarAccess.getS_ActionAccess().getFirstFacetNameKeyword_2_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_ActionRule());
	        }
       		setWithLastConsumed($current, "firstFacet", lv_firstFacet_2_0, "name:");
	    }

)
)?(
(
		{ 
	        newCompositeNode(grammarAccess.getS_ActionAccess().getNameValid_IDParserRuleCall_3_0()); 
	    }
		lv_name_3_0=ruleValid_ID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_ActionRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_3_0, 
        		"msi.gama.lang.gaml.Gaml.Valid_ID");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_4='(' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getS_ActionAccess().getLeftParenthesisKeyword_4_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getS_ActionAccess().getArgsActionArgumentsParserRuleCall_4_1_0()); 
	    }
		lv_args_5_0=ruleActionArguments		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_ActionRule());
	        }
       		set(
       			$current, 
       			"args",
        		lv_args_5_0, 
        		"msi.gama.lang.gaml.Gaml.ActionArguments");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_6=')' 
    {
    	newLeafNode(otherlv_6, grammarAccess.getS_ActionAccess().getRightParenthesisKeyword_4_2());
    }
)?(
(
		{ 
	        newCompositeNode(grammarAccess.getS_ActionAccess().getFacetsFacetParserRuleCall_5_0()); 
	    }
		lv_facets_7_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_ActionRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_7_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*((
(
		{ 
	        newCompositeNode(grammarAccess.getS_ActionAccess().getBlockBlockParserRuleCall_6_0_0()); 
	    }
		lv_block_8_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_ActionRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_8_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |	otherlv_9=';' 
    {
    	newLeafNode(otherlv_9, grammarAccess.getS_ActionAccess().getSemicolonKeyword_6_1());
    }
))
;





// Entry rule entryRuleS_Var
entryRuleS_Var returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_VarRule()); }
	 iv_ruleS_Var=ruleS_Var 
	 { $current=$iv_ruleS_Var.current; } 
	 EOF 
;

// Rule S_Var
ruleS_Var returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getS_VarAccess().getS_VarAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_VarAccess().getKey_VarOrConstKeyParserRuleCall_1_0()); 
	    }
		lv_key_1_0=rule_VarOrConstKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_VarRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_1_0, 
        		"msi.gama.lang.gaml.Gaml._VarOrConstKey");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		lv_firstFacet_2_0=	'name:' 
    {
        newLeafNode(lv_firstFacet_2_0, grammarAccess.getS_VarAccess().getFirstFacetNameKeyword_2_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_VarRule());
	        }
       		setWithLastConsumed($current, "firstFacet", lv_firstFacet_2_0, "name:");
	    }

)
)?(
(
		{ 
	        newCompositeNode(grammarAccess.getS_VarAccess().getNameValid_IDParserRuleCall_3_0()); 
	    }
		lv_name_3_0=ruleValid_ID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_VarRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_3_0, 
        		"msi.gama.lang.gaml.Gaml.Valid_ID");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_VarAccess().getFacetsFacetParserRuleCall_4_0()); 
	    }
		lv_facets_4_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_VarRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_4_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_5=';' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getS_VarAccess().getSemicolonKeyword_5());
    }
)
;





// Entry rule entryRuleS_Assignment
entryRuleS_Assignment returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_AssignmentRule()); }
	 iv_ruleS_Assignment=ruleS_Assignment 
	 { $current=$iv_ruleS_Assignment.current; } 
	 EOF 
;

// Rule S_Assignment
ruleS_Assignment returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getS_AssignmentAccess().getS_DirectAssignmentParserRuleCall_0()); 
    }
    this_S_DirectAssignment_0=ruleS_DirectAssignment
    { 
        $current = $this_S_DirectAssignment_0.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getS_AssignmentAccess().getS_SetParserRuleCall_1()); 
    }
    this_S_Set_1=ruleS_Set
    { 
        $current = $this_S_Set_1.current; 
        afterParserOrEnumRuleCall();
    }
)
;





// Entry rule entryRuleS_DirectAssignment
entryRuleS_DirectAssignment returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_DirectAssignmentRule()); }
	 iv_ruleS_DirectAssignment=ruleS_DirectAssignment 
	 { $current=$iv_ruleS_DirectAssignment.current; } 
	 EOF 
;

// Rule S_DirectAssignment
ruleS_DirectAssignment returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(((
(
		{ 
	        newCompositeNode(grammarAccess.getS_DirectAssignmentAccess().getExprExpressionParserRuleCall_0_0_0()); 
	    }
		lv_expr_0_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DirectAssignmentRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_0_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_DirectAssignmentAccess().getKey_AssignmentKeyParserRuleCall_0_1_0()); 
	    }
		lv_key_1_0=rule_AssignmentKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DirectAssignmentRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_1_0, 
        		"msi.gama.lang.gaml.Gaml._AssignmentKey");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_DirectAssignmentAccess().getValueExpressionParserRuleCall_0_2_0()); 
	    }
		lv_value_2_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DirectAssignmentRule());
	        }
       		set(
       			$current, 
       			"value",
        		lv_value_2_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_DirectAssignmentAccess().getFacetsFacetParserRuleCall_0_3_0()); 
	    }
		lv_facets_3_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DirectAssignmentRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_3_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*)	otherlv_4=';' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getS_DirectAssignmentAccess().getSemicolonKeyword_1());
    }
)
;





// Entry rule entryRuleS_Set
entryRuleS_Set returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_SetRule()); }
	 iv_ruleS_Set=ruleS_Set 
	 { $current=$iv_ruleS_Set.current; } 
	 EOF 
;

// Rule S_Set
ruleS_Set returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		lv_key_0_0=	'set' 
    {
        newLeafNode(lv_key_0_0, grammarAccess.getS_SetAccess().getKeySetKeyword_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_SetRule());
	        }
       		setWithLastConsumed($current, "key", lv_key_0_0, "set");
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_SetAccess().getExprExpressionParserRuleCall_1_0()); 
	    }
		lv_expr_1_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_SetRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_1_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_2='value:' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getS_SetAccess().getValueKeyword_2_0());
    }

    |	otherlv_3='<-' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getS_SetAccess().getLessThanSignHyphenMinusKeyword_2_1());
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_SetAccess().getValueExpressionParserRuleCall_3_0()); 
	    }
		lv_value_4_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_SetRule());
	        }
       		set(
       			$current, 
       			"value",
        		lv_value_4_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_5=';' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getS_SetAccess().getSemicolonKeyword_4());
    }
)
;





// Entry rule entryRuleS_Equations
entryRuleS_Equations returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_EquationsRule()); }
	 iv_ruleS_Equations=ruleS_Equations 
	 { $current=$iv_ruleS_Equations.current; } 
	 EOF 
;

// Rule S_Equations
ruleS_Equations returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getS_EquationsAccess().getKey_EquationsKeyParserRuleCall_0_0()); 
	    }
		lv_key_0_0=rule_EquationsKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_EquationsRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml._EquationsKey");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_EquationsAccess().getNameValid_IDParserRuleCall_1_0()); 
	    }
		lv_name_1_0=ruleValid_ID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_EquationsRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_1_0, 
        		"msi.gama.lang.gaml.Gaml.Valid_ID");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_EquationsAccess().getFacetsFacetParserRuleCall_2_0()); 
	    }
		lv_facets_2_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_EquationsRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_2_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*((	otherlv_3='{' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getS_EquationsAccess().getLeftCurlyBracketKeyword_3_0_0());
    }
((
(
		{ 
	        newCompositeNode(grammarAccess.getS_EquationsAccess().getEquationsS_EquationParserRuleCall_3_0_1_0_0()); 
	    }
		lv_equations_4_0=ruleS_Equation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_EquationsRule());
	        }
       		add(
       			$current, 
       			"equations",
        		lv_equations_4_0, 
        		"msi.gama.lang.gaml.Gaml.S_Equation");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_5=';' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getS_EquationsAccess().getSemicolonKeyword_3_0_1_1());
    }
)*	otherlv_6='}' 
    {
    	newLeafNode(otherlv_6, grammarAccess.getS_EquationsAccess().getRightCurlyBracketKeyword_3_0_2());
    }
)
    |	otherlv_7=';' 
    {
    	newLeafNode(otherlv_7, grammarAccess.getS_EquationsAccess().getSemicolonKeyword_3_1());
    }
))
;





// Entry rule entryRuleS_Equation
entryRuleS_Equation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_EquationRule()); }
	 iv_ruleS_Equation=ruleS_Equation 
	 { $current=$iv_ruleS_Equation.current; } 
	 EOF 
;

// Rule S_Equation
ruleS_Equation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
(
		{ 
	        newCompositeNode(grammarAccess.getS_EquationAccess().getExprFunctionParserRuleCall_0_0_0()); 
	    }
		lv_expr_0_1=ruleFunction		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_EquationRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_0_1, 
        		"msi.gama.lang.gaml.Gaml.Function");
	        afterParserOrEnumRuleCall();
	    }

    |		{ 
	        newCompositeNode(grammarAccess.getS_EquationAccess().getExprVariableRefParserRuleCall_0_0_1()); 
	    }
		lv_expr_0_2=ruleVariableRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_EquationRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_0_2, 
        		"msi.gama.lang.gaml.Gaml.VariableRef");
	        afterParserOrEnumRuleCall();
	    }

)

)
)(
(
		lv_key_1_0=	'=' 
    {
        newLeafNode(lv_key_1_0, grammarAccess.getS_EquationAccess().getKeyEqualsSignKeyword_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_EquationRule());
	        }
       		setWithLastConsumed($current, "key", lv_key_1_0, "=");
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_EquationAccess().getValueExpressionParserRuleCall_2_0()); 
	    }
		lv_value_2_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_EquationRule());
	        }
       		set(
       			$current, 
       			"value",
        		lv_value_2_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleS_Solve
entryRuleS_Solve returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_SolveRule()); }
	 iv_ruleS_Solve=ruleS_Solve 
	 { $current=$iv_ruleS_Solve.current; } 
	 EOF 
;

// Rule S_Solve
ruleS_Solve returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getS_SolveAccess().getKey_SolveKeyParserRuleCall_0_0()); 
	    }
		lv_key_0_0=rule_SolveKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_SolveRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml._SolveKey");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		lv_firstFacet_1_0=	'equation:' 
    {
        newLeafNode(lv_firstFacet_1_0, grammarAccess.getS_SolveAccess().getFirstFacetEquationKeyword_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_SolveRule());
	        }
       		setWithLastConsumed($current, "firstFacet", lv_firstFacet_1_0, "equation:");
	    }

)
)?(
(
		{ 
	        newCompositeNode(grammarAccess.getS_SolveAccess().getExprEquationRefParserRuleCall_2_0()); 
	    }
		lv_expr_2_0=ruleEquationRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_SolveRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_2_0, 
        		"msi.gama.lang.gaml.Gaml.EquationRef");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_SolveAccess().getFacetsFacetParserRuleCall_3_0()); 
	    }
		lv_facets_3_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_SolveRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_3_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*((
(
		{ 
	        newCompositeNode(grammarAccess.getS_SolveAccess().getBlockBlockParserRuleCall_4_0_0()); 
	    }
		lv_block_4_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_SolveRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_4_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |	otherlv_5=';' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getS_SolveAccess().getSemicolonKeyword_4_1());
    }
))
;





// Entry rule entryRuleS_Display
entryRuleS_Display returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getS_DisplayRule()); }
	 iv_ruleS_Display=ruleS_Display 
	 { $current=$iv_ruleS_Display.current; } 
	 EOF 
;

// Rule S_Display
ruleS_Display returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		lv_key_0_0=	'display' 
    {
        newLeafNode(lv_key_0_0, grammarAccess.getS_DisplayAccess().getKeyDisplayKeyword_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_DisplayRule());
	        }
       		setWithLastConsumed($current, "key", lv_key_0_0, "display");
	    }

)
)(
(
		lv_firstFacet_1_0=	'name:' 
    {
        newLeafNode(lv_firstFacet_1_0, grammarAccess.getS_DisplayAccess().getFirstFacetNameKeyword_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_DisplayRule());
	        }
       		setWithLastConsumed($current, "firstFacet", lv_firstFacet_1_0, "name:");
	    }

)
)?(
(
(
		{ 
	        newCompositeNode(grammarAccess.getS_DisplayAccess().getNameValid_IDParserRuleCall_2_0_0()); 
	    }
		lv_name_2_1=ruleValid_ID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DisplayRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_2_1, 
        		"msi.gama.lang.gaml.Gaml.Valid_ID");
	        afterParserOrEnumRuleCall();
	    }

    |		lv_name_2_2=RULE_STRING
		{
			newLeafNode(lv_name_2_2, grammarAccess.getS_DisplayAccess().getNameSTRINGTerminalRuleCall_2_0_1()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getS_DisplayRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_2_2, 
        		"msi.gama.lang.gaml.Gaml.STRING");
	    }

)

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getS_DisplayAccess().getFacetsFacetParserRuleCall_3_0()); 
	    }
		lv_facets_3_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DisplayRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_3_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*(
(
		{ 
	        newCompositeNode(grammarAccess.getS_DisplayAccess().getBlockDisplayBlockParserRuleCall_4_0()); 
	    }
		lv_block_4_0=ruledisplayBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getS_DisplayRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_4_0, 
        		"msi.gama.lang.gaml.Gaml.displayBlock");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuledisplayBlock
entryRuledisplayBlock returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getDisplayBlockRule()); }
	 iv_ruledisplayBlock=ruledisplayBlock 
	 { $current=$iv_ruledisplayBlock.current; } 
	 EOF 
;

// Rule displayBlock
ruledisplayBlock returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getDisplayBlockAccess().getBlockAction_0(),
            $current);
    }
)	otherlv_1='{' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getDisplayBlockAccess().getLeftCurlyBracketKeyword_1());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getDisplayBlockAccess().getStatementsDisplayStatementParserRuleCall_2_0()); 
	    }
		lv_statements_2_0=ruledisplayStatement		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getDisplayBlockRule());
	        }
       		add(
       			$current, 
       			"statements",
        		lv_statements_2_0, 
        		"msi.gama.lang.gaml.Gaml.displayStatement");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_3='}' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getDisplayBlockAccess().getRightCurlyBracketKeyword_3());
    }
)
;





// Entry rule entryRuledisplayStatement
entryRuledisplayStatement returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getDisplayStatementRule()); }
	 iv_ruledisplayStatement=ruledisplayStatement 
	 { $current=$iv_ruledisplayStatement.current; } 
	 EOF 
;

// Rule displayStatement
ruledisplayStatement returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getDisplayStatementAccess().getSpeciesOrGridDisplayStatementParserRuleCall_0()); 
    }
    this_speciesOrGridDisplayStatement_0=rulespeciesOrGridDisplayStatement
    { 
        $current = $this_speciesOrGridDisplayStatement_0.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getDisplayStatementAccess().getS_1Expr_Facets_BlockOrEndParserRuleCall_1()); 
    }
    this_S_1Expr_Facets_BlockOrEnd_1=ruleS_1Expr_Facets_BlockOrEnd
    { 
        $current = $this_S_1Expr_Facets_BlockOrEnd_1.current; 
        afterParserOrEnumRuleCall();
    }
)
;





// Entry rule entryRulespeciesOrGridDisplayStatement
entryRulespeciesOrGridDisplayStatement returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getSpeciesOrGridDisplayStatementRule()); }
	 iv_rulespeciesOrGridDisplayStatement=rulespeciesOrGridDisplayStatement 
	 { $current=$iv_rulespeciesOrGridDisplayStatement.current; } 
	 EOF 
;

// Rule speciesOrGridDisplayStatement
rulespeciesOrGridDisplayStatement returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getSpeciesOrGridDisplayStatementAccess().getKey_SpeciesKeyParserRuleCall_0_0()); 
	    }
		lv_key_0_0=rule_SpeciesKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getSpeciesOrGridDisplayStatementRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml._SpeciesKey");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getSpeciesOrGridDisplayStatementAccess().getExprExpressionParserRuleCall_1_0()); 
	    }
		lv_expr_1_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getSpeciesOrGridDisplayStatementRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_1_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getSpeciesOrGridDisplayStatementAccess().getFacetsFacetParserRuleCall_2_0()); 
	    }
		lv_facets_2_0=ruleFacet		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getSpeciesOrGridDisplayStatementRule());
	        }
       		add(
       			$current, 
       			"facets",
        		lv_facets_2_0, 
        		"msi.gama.lang.gaml.Gaml.Facet");
	        afterParserOrEnumRuleCall();
	    }

)
)*((
(
		{ 
	        newCompositeNode(grammarAccess.getSpeciesOrGridDisplayStatementAccess().getBlockDisplayBlockParserRuleCall_3_0_0()); 
	    }
		lv_block_3_0=ruledisplayBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getSpeciesOrGridDisplayStatementRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_3_0, 
        		"msi.gama.lang.gaml.Gaml.displayBlock");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |	otherlv_4=';' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getSpeciesOrGridDisplayStatementAccess().getSemicolonKeyword_3_1());
    }
))
;





// Entry rule entryRule_EquationsKey
entryRule_EquationsKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.get_EquationsKeyRule()); } 
	 iv_rule_EquationsKey=rule_EquationsKey 
	 { $current=$iv_rule_EquationsKey.current.getText(); }  
	 EOF 
;

// Rule _EquationsKey
rule_EquationsKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:

	kw='equation' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_EquationsKeyAccess().getEquationKeyword()); 
    }

    ;





// Entry rule entryRule_SolveKey
entryRule_SolveKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.get_SolveKeyRule()); } 
	 iv_rule_SolveKey=rule_SolveKey 
	 { $current=$iv_rule_SolveKey.current.getText(); }  
	 EOF 
;

// Rule _SolveKey
rule_SolveKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:

	kw='solve' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_SolveKeyAccess().getSolveKeyword()); 
    }

    ;





// Entry rule entryRule_SpeciesKey
entryRule_SpeciesKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.get_SpeciesKeyRule()); } 
	 iv_rule_SpeciesKey=rule_SpeciesKey 
	 { $current=$iv_rule_SpeciesKey.current.getText(); }  
	 EOF 
;

// Rule _SpeciesKey
rule_SpeciesKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
	kw='species' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_SpeciesKeyAccess().getSpeciesKeyword_0()); 
    }

    |
	kw='grid' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_SpeciesKeyAccess().getGridKeyword_1()); 
    }
)
    ;





// Entry rule entryRule_ExperimentKey
entryRule_ExperimentKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.get_ExperimentKeyRule()); } 
	 iv_rule_ExperimentKey=rule_ExperimentKey 
	 { $current=$iv_rule_ExperimentKey.current.getText(); }  
	 EOF 
;

// Rule _ExperimentKey
rule_ExperimentKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:

	kw='experiment' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_ExperimentKeyAccess().getExperimentKeyword()); 
    }

    ;





// Entry rule entryRule_1Expr_Facets_BlockOrEnd_Key
entryRule_1Expr_Facets_BlockOrEnd_Key returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyRule()); } 
	 iv_rule_1Expr_Facets_BlockOrEnd_Key=rule_1Expr_Facets_BlockOrEnd_Key 
	 { $current=$iv_rule_1Expr_Facets_BlockOrEnd_Key.current.getText(); }  
	 EOF 
;

// Rule _1Expr_Facets_BlockOrEnd_Key
rule_1Expr_Facets_BlockOrEnd_Key returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().get_LayerKeyParserRuleCall_0()); 
    }
    this__LayerKey_0=rule_LayerKey    {
		$current.merge(this__LayerKey_0);
    }

    { 
        afterParserOrEnumRuleCall();
    }

    |
	kw='ask' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getAskKeyword_1()); 
    }

    |
	kw='release' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getReleaseKeyword_2()); 
    }

    |
	kw='capture' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getCaptureKeyword_3()); 
    }

    |
	kw='create' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getCreateKeyword_4()); 
    }

    |
	kw='write' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getWriteKeyword_5()); 
    }

    |
	kw='error' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getErrorKeyword_6()); 
    }

    |
	kw='warn' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getWarnKeyword_7()); 
    }

    |
	kw='exception' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getExceptionKeyword_8()); 
    }

    |
	kw='save' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getSaveKeyword_9()); 
    }

    |
	kw='assert' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getAssertKeyword_10()); 
    }

    |
	kw='inspect' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getInspectKeyword_11()); 
    }

    |
	kw='browse' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getBrowseKeyword_12()); 
    }

    |
	kw='draw' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getDrawKeyword_13()); 
    }

    |
	kw='using' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getUsingKeyword_14()); 
    }

    |
	kw='switch' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getSwitchKeyword_15()); 
    }

    |
	kw='put' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getPutKeyword_16()); 
    }

    |
	kw='add' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getAddKeyword_17()); 
    }

    |
	kw='remove' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getRemoveKeyword_18()); 
    }

    |
	kw='match' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getMatchKeyword_19()); 
    }

    |
	kw='match_between' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getMatch_betweenKeyword_20()); 
    }

    |
	kw='match_one' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getMatch_oneKeyword_21()); 
    }

    |
	kw='parameter' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getParameterKeyword_22()); 
    }

    |
	kw='status' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getStatusKeyword_23()); 
    }

    |
	kw='highlight' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getHighlightKeyword_24()); 
    }

    |
	kw='focus_on' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_1Expr_Facets_BlockOrEnd_KeyAccess().getFocus_onKeyword_25()); 
    }
)
    ;





// Entry rule entryRule_LayerKey
entryRule_LayerKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.get_LayerKeyRule()); } 
	 iv_rule_LayerKey=rule_LayerKey 
	 { $current=$iv_rule_LayerKey.current.getText(); }  
	 EOF 
;

// Rule _LayerKey
rule_LayerKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
	kw='light' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getLightKeyword_0()); 
    }

    |
	kw='camera' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getCameraKeyword_1()); 
    }

    |
	kw='text' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getTextKeyword_2()); 
    }

    |
	kw='image' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getImageKeyword_3()); 
    }

    |
	kw='data' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getDataKeyword_4()); 
    }

    |
	kw='chart' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getChartKeyword_5()); 
    }

    |
	kw='agents' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getAgentsKeyword_6()); 
    }

    |
	kw='graphics' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getGraphicsKeyword_7()); 
    }

    |
	kw='display_population' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getDisplay_populationKeyword_8()); 
    }

    |
	kw='display_grid' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getDisplay_gridKeyword_9()); 
    }

    |
	kw='quadtree' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getQuadtreeKeyword_10()); 
    }

    |
	kw='event' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getEventKeyword_11()); 
    }

    |
	kw='overlay' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getOverlayKeyword_12()); 
    }

    |
	kw='datalist' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_LayerKeyAccess().getDatalistKeyword_13()); 
    }
)
    ;





// Entry rule entryRule_DoKey
entryRule_DoKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.get_DoKeyRule()); } 
	 iv_rule_DoKey=rule_DoKey 
	 { $current=$iv_rule_DoKey.current.getText(); }  
	 EOF 
;

// Rule _DoKey
rule_DoKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:

	kw='do' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_DoKeyAccess().getDoKeyword()); 
    }

    ;





// Entry rule entryRule_VarOrConstKey
entryRule_VarOrConstKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.get_VarOrConstKeyRule()); } 
	 iv_rule_VarOrConstKey=rule_VarOrConstKey 
	 { $current=$iv_rule_VarOrConstKey.current.getText(); }  
	 EOF 
;

// Rule _VarOrConstKey
rule_VarOrConstKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
	kw='var' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_VarOrConstKeyAccess().getVarKeyword_0()); 
    }

    |
	kw='const' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_VarOrConstKeyAccess().getConstKeyword_1()); 
    }

    |
	kw='let' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_VarOrConstKeyAccess().getLetKeyword_2()); 
    }

    |
	kw='arg' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_VarOrConstKeyAccess().getArgKeyword_3()); 
    }
)
    ;





// Entry rule entryRule_ReflexKey
entryRule_ReflexKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.get_ReflexKeyRule()); } 
	 iv_rule_ReflexKey=rule_ReflexKey 
	 { $current=$iv_rule_ReflexKey.current.getText(); }  
	 EOF 
;

// Rule _ReflexKey
rule_ReflexKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
	kw='init' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_ReflexKeyAccess().getInitKeyword_0()); 
    }

    |
	kw='reflex' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_ReflexKeyAccess().getReflexKeyword_1()); 
    }

    |
	kw='aspect' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_ReflexKeyAccess().getAspectKeyword_2()); 
    }
)
    ;





// Entry rule entryRule_AssignmentKey
entryRule_AssignmentKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.get_AssignmentKeyRule()); } 
	 iv_rule_AssignmentKey=rule_AssignmentKey 
	 { $current=$iv_rule_AssignmentKey.current.getText(); }  
	 EOF 
;

// Rule _AssignmentKey
rule_AssignmentKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
	kw='<-' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_AssignmentKeyAccess().getLessThanSignHyphenMinusKeyword_0()); 
    }

    |
	kw='<<' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_AssignmentKeyAccess().getLessThanSignLessThanSignKeyword_1()); 
    }

    |(
	kw='>' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_AssignmentKeyAccess().getGreaterThanSignKeyword_2_0()); 
    }

	kw='>' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_AssignmentKeyAccess().getGreaterThanSignKeyword_2_1()); 
    }
)
    |
	kw='<<+' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_AssignmentKeyAccess().getLessThanSignLessThanSignPlusSignKeyword_3()); 
    }

    |(
	kw='>' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_AssignmentKeyAccess().getGreaterThanSignKeyword_4_0()); 
    }

	kw='>-' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_AssignmentKeyAccess().getGreaterThanSignHyphenMinusKeyword_4_1()); 
    }
)
    |
	kw='+<-' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_AssignmentKeyAccess().getPlusSignLessThanSignHyphenMinusKeyword_5()); 
    }

    |
	kw='<+' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_AssignmentKeyAccess().getLessThanSignPlusSignKeyword_6()); 
    }

    |
	kw='>-' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.get_AssignmentKeyAccess().getGreaterThanSignHyphenMinusKeyword_7()); 
    }
)
    ;





// Entry rule entryRuleParameters
entryRuleParameters returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getParametersRule()); }
	 iv_ruleParameters=ruleParameters 
	 { $current=$iv_ruleParameters.current; } 
	 EOF 
;

// Rule Parameters
ruleParameters returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getParametersAccess().getParametersAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getParametersAccess().getParamsParameterListParserRuleCall_1_0()); 
	    }
		lv_params_1_0=ruleParameterList		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getParametersRule());
	        }
       		set(
       			$current, 
       			"params",
        		lv_params_1_0, 
        		"msi.gama.lang.gaml.Gaml.ParameterList");
	        afterParserOrEnumRuleCall();
	    }

)
)?)
;





// Entry rule entryRuleActionArguments
entryRuleActionArguments returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getActionArgumentsRule()); }
	 iv_ruleActionArguments=ruleActionArguments 
	 { $current=$iv_ruleActionArguments.current; } 
	 EOF 
;

// Rule ActionArguments
ruleActionArguments returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getActionArgumentsAccess().getArgsArgumentDefinitionParserRuleCall_0_0()); 
	    }
		lv_args_0_0=ruleArgumentDefinition		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getActionArgumentsRule());
	        }
       		add(
       			$current, 
       			"args",
        		lv_args_0_0, 
        		"msi.gama.lang.gaml.Gaml.ArgumentDefinition");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_1=',' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getActionArgumentsAccess().getCommaKeyword_1_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getActionArgumentsAccess().getArgsArgumentDefinitionParserRuleCall_1_1_0()); 
	    }
		lv_args_2_0=ruleArgumentDefinition		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getActionArgumentsRule());
	        }
       		add(
       			$current, 
       			"args",
        		lv_args_2_0, 
        		"msi.gama.lang.gaml.Gaml.ArgumentDefinition");
	        afterParserOrEnumRuleCall();
	    }

)
))*)
;





// Entry rule entryRuleArgumentDefinition
entryRuleArgumentDefinition returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getArgumentDefinitionRule()); }
	 iv_ruleArgumentDefinition=ruleArgumentDefinition 
	 { $current=$iv_ruleArgumentDefinition.current; } 
	 EOF 
;

// Rule ArgumentDefinition
ruleArgumentDefinition returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getArgumentDefinitionAccess().getTypeTypeRefParserRuleCall_0_0()); 
	    }
		lv_type_0_0=ruleTypeRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getArgumentDefinitionRule());
	        }
       		set(
       			$current, 
       			"type",
        		lv_type_0_0, 
        		"msi.gama.lang.gaml.Gaml.TypeRef");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getArgumentDefinitionAccess().getNameValid_IDParserRuleCall_1_0()); 
	    }
		lv_name_1_0=ruleValid_ID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getArgumentDefinitionRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_1_0, 
        		"msi.gama.lang.gaml.Gaml.Valid_ID");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_2='<-' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getArgumentDefinitionAccess().getLessThanSignHyphenMinusKeyword_2_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getArgumentDefinitionAccess().getDefaultExpressionParserRuleCall_2_1_0()); 
	    }
		lv_default_3_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getArgumentDefinitionRule());
	        }
       		set(
       			$current, 
       			"default",
        		lv_default_3_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRuleFacet
entryRuleFacet returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getFacetRule()); }
	 iv_ruleFacet=ruleFacet 
	 { $current=$iv_ruleFacet.current; } 
	 EOF 
;

// Rule Facet
ruleFacet returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(((
(
ruleDefinitionFacetKey
)
)=>
    { 
        newCompositeNode(grammarAccess.getFacetAccess().getDefinitionFacetParserRuleCall_0()); 
    }
    this_DefinitionFacet_0=ruleDefinitionFacet
    { 
        $current = $this_DefinitionFacet_0.current; 
        afterParserOrEnumRuleCall();
    }
)
    |
    { 
        newCompositeNode(grammarAccess.getFacetAccess().getFunctionFacetParserRuleCall_1()); 
    }
    this_FunctionFacet_1=ruleFunctionFacet
    { 
        $current = $this_FunctionFacet_1.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getFacetAccess().getClassicFacetParserRuleCall_2()); 
    }
    this_ClassicFacet_2=ruleClassicFacet
    { 
        $current = $this_ClassicFacet_2.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getFacetAccess().getTypeFacetParserRuleCall_3()); 
    }
    this_TypeFacet_3=ruleTypeFacet
    { 
        $current = $this_TypeFacet_3.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getFacetAccess().getVarFacetParserRuleCall_4()); 
    }
    this_VarFacet_4=ruleVarFacet
    { 
        $current = $this_VarFacet_4.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getFacetAccess().getActionFacetParserRuleCall_5()); 
    }
    this_ActionFacet_5=ruleActionFacet
    { 
        $current = $this_ActionFacet_5.current; 
        afterParserOrEnumRuleCall();
    }
)
;





// Entry rule entryRuleFirstFacetKey
entryRuleFirstFacetKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.getFirstFacetKeyRule()); } 
	 iv_ruleFirstFacetKey=ruleFirstFacetKey 
	 { $current=$iv_ruleFirstFacetKey.current.getText(); }  
	 EOF 
;

// Rule FirstFacetKey
ruleFirstFacetKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getFirstFacetKeyAccess().getDefinitionFacetKeyParserRuleCall_0()); 
    }
    this_DefinitionFacetKey_0=ruleDefinitionFacetKey    {
		$current.merge(this_DefinitionFacetKey_0);
    }

    { 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getFirstFacetKeyAccess().getTypeFacetKeyParserRuleCall_1()); 
    }
    this_TypeFacetKey_1=ruleTypeFacetKey    {
		$current.merge(this_TypeFacetKey_1);
    }

    { 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getFirstFacetKeyAccess().getSpecialFacetKeyParserRuleCall_2()); 
    }
    this_SpecialFacetKey_2=ruleSpecialFacetKey    {
		$current.merge(this_SpecialFacetKey_2);
    }

    { 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getFirstFacetKeyAccess().getVarFacetKeyParserRuleCall_3()); 
    }
    this_VarFacetKey_3=ruleVarFacetKey    {
		$current.merge(this_VarFacetKey_3);
    }

    { 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getFirstFacetKeyAccess().getActionFacetKeyParserRuleCall_4()); 
    }
    this_ActionFacetKey_4=ruleActionFacetKey    {
		$current.merge(this_ActionFacetKey_4);
    }

    { 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getFirstFacetKeyAccess().getClassicFacetKeyParserRuleCall_5()); 
    }
    this_ClassicFacetKey_5=ruleClassicFacetKey    {
		$current.merge(this_ClassicFacetKey_5);
    }

    { 
        afterParserOrEnumRuleCall();
    }
)
    ;





// Entry rule entryRuleClassicFacetKey
entryRuleClassicFacetKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.getClassicFacetKeyRule()); } 
	 iv_ruleClassicFacetKey=ruleClassicFacetKey 
	 { $current=$iv_ruleClassicFacetKey.current.getText(); }  
	 EOF 
;

// Rule ClassicFacetKey
ruleClassicFacetKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(    this_ID_0=RULE_ID    {
		$current.merge(this_ID_0);
    }

    { 
    newLeafNode(this_ID_0, grammarAccess.getClassicFacetKeyAccess().getIDTerminalRuleCall_0()); 
    }

	kw=':' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getClassicFacetKeyAccess().getColonKeyword_1()); 
    }
)
    ;





// Entry rule entryRuleDefinitionFacetKey
entryRuleDefinitionFacetKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.getDefinitionFacetKeyRule()); } 
	 iv_ruleDefinitionFacetKey=ruleDefinitionFacetKey 
	 { $current=$iv_ruleDefinitionFacetKey.current.getText(); }  
	 EOF 
;

// Rule DefinitionFacetKey
ruleDefinitionFacetKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
	kw='name:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getDefinitionFacetKeyAccess().getNameKeyword_0()); 
    }

    |
	kw='returns:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getDefinitionFacetKeyAccess().getReturnsKeyword_1()); 
    }
)
    ;





// Entry rule entryRuleTypeFacetKey
entryRuleTypeFacetKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.getTypeFacetKeyRule()); } 
	 iv_ruleTypeFacetKey=ruleTypeFacetKey 
	 { $current=$iv_ruleTypeFacetKey.current.getText(); }  
	 EOF 
;

// Rule TypeFacetKey
ruleTypeFacetKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
	kw='as:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getTypeFacetKeyAccess().getAsKeyword_0()); 
    }

    |
	kw='of:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getTypeFacetKeyAccess().getOfKeyword_1()); 
    }

    |
	kw='parent:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getTypeFacetKeyAccess().getParentKeyword_2()); 
    }

    |
	kw='species:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getTypeFacetKeyAccess().getSpeciesKeyword_3()); 
    }

    |
	kw='type:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getTypeFacetKeyAccess().getTypeKeyword_4()); 
    }
)
    ;





// Entry rule entryRuleSpecialFacetKey
entryRuleSpecialFacetKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.getSpecialFacetKeyRule()); } 
	 iv_ruleSpecialFacetKey=ruleSpecialFacetKey 
	 { $current=$iv_ruleSpecialFacetKey.current.getText(); }  
	 EOF 
;

// Rule SpecialFacetKey
ruleSpecialFacetKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
	kw='data:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getDataKeyword_0()); 
    }

    |(
	kw='when' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getWhenKeyword_1_0()); 
    }

	kw=':' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getColonKeyword_1_1()); 
    }
)
    |
	kw='const:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getConstKeyword_2()); 
    }

    |
	kw='value:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getValueKeyword_3()); 
    }

    |
	kw='topology:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getTopologyKeyword_4()); 
    }

    |
	kw='item:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getItemKeyword_5()); 
    }

    |
	kw='init:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getInitKeyword_6()); 
    }

    |
	kw='message:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getMessageKeyword_7()); 
    }

    |
	kw='control:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getControlKeyword_8()); 
    }

    |
	kw='environment:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getEnvironmentKeyword_9()); 
    }

    |
	kw='text:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getTextKeyword_10()); 
    }

    |
	kw='image:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getImageKeyword_11()); 
    }

    |
	kw='using:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getUsingKeyword_12()); 
    }

    |
	kw='parameter:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getParameterKeyword_13()); 
    }

    |
	kw='aspect:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getAspectKeyword_14()); 
    }

    |
	kw='light:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getSpecialFacetKeyAccess().getLightKeyword_15()); 
    }
)
    ;





// Entry rule entryRuleActionFacetKey
entryRuleActionFacetKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.getActionFacetKeyRule()); } 
	 iv_ruleActionFacetKey=ruleActionFacetKey 
	 { $current=$iv_ruleActionFacetKey.current.getText(); }  
	 EOF 
;

// Rule ActionFacetKey
ruleActionFacetKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
	kw='action:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getActionFacetKeyAccess().getActionKeyword_0()); 
    }

    |
	kw='on_change:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getActionFacetKeyAccess().getOn_changeKeyword_1()); 
    }
)
    ;





// Entry rule entryRuleVarFacetKey
entryRuleVarFacetKey returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.getVarFacetKeyRule()); } 
	 iv_ruleVarFacetKey=ruleVarFacetKey 
	 { $current=$iv_ruleVarFacetKey.current.getText(); }  
	 EOF 
;

// Rule VarFacetKey
ruleVarFacetKey returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:

	kw='var:' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getVarFacetKeyAccess().getVarKeyword()); 
    }

    ;





// Entry rule entryRuleClassicFacet
entryRuleClassicFacet returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getClassicFacetRule()); }
	 iv_ruleClassicFacet=ruleClassicFacet 
	 { $current=$iv_ruleClassicFacet.current; } 
	 EOF 
;

// Rule ClassicFacet
ruleClassicFacet returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(((
(
		{ 
	        newCompositeNode(grammarAccess.getClassicFacetAccess().getKeyClassicFacetKeyParserRuleCall_0_0_0()); 
	    }
		lv_key_0_0=ruleClassicFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getClassicFacetRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml.ClassicFacetKey");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |(
(
		lv_key_1_0=	'<-' 
    {
        newLeafNode(lv_key_1_0, grammarAccess.getClassicFacetAccess().getKeyLessThanSignHyphenMinusKeyword_0_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getClassicFacetRule());
	        }
       		setWithLastConsumed($current, "key", lv_key_1_0, "<-");
	    }

)
)
    |(
(
		{ 
	        newCompositeNode(grammarAccess.getClassicFacetAccess().getKeySpecialFacetKeyParserRuleCall_0_2_0()); 
	    }
		lv_key_2_0=ruleSpecialFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getClassicFacetRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_2_0, 
        		"msi.gama.lang.gaml.Gaml.SpecialFacetKey");
	        afterParserOrEnumRuleCall();
	    }

)
))(
(
		{ 
	        newCompositeNode(grammarAccess.getClassicFacetAccess().getExprExpressionParserRuleCall_1_0()); 
	    }
		lv_expr_3_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getClassicFacetRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_3_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleDefinitionFacet
entryRuleDefinitionFacet returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getDefinitionFacetRule()); }
	 iv_ruleDefinitionFacet=ruleDefinitionFacet 
	 { $current=$iv_ruleDefinitionFacet.current; } 
	 EOF 
;

// Rule DefinitionFacet
ruleDefinitionFacet returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(((
(
ruleDefinitionFacetKey
)
)=>
(
		{ 
	        newCompositeNode(grammarAccess.getDefinitionFacetAccess().getKeyDefinitionFacetKeyParserRuleCall_0_0()); 
	    }
		lv_key_0_0=ruleDefinitionFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getDefinitionFacetRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml.DefinitionFacetKey");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
(
		{ 
	        newCompositeNode(grammarAccess.getDefinitionFacetAccess().getNameValid_IDParserRuleCall_1_0_0()); 
	    }
		lv_name_1_1=ruleValid_ID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getDefinitionFacetRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_1_1, 
        		"msi.gama.lang.gaml.Gaml.Valid_ID");
	        afterParserOrEnumRuleCall();
	    }

    |		lv_name_1_2=RULE_STRING
		{
			newLeafNode(lv_name_1_2, grammarAccess.getDefinitionFacetAccess().getNameSTRINGTerminalRuleCall_1_0_1()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getDefinitionFacetRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_1_2, 
        		"msi.gama.lang.gaml.Gaml.STRING");
	    }

)

)
))
;





// Entry rule entryRuleFunctionFacet
entryRuleFunctionFacet returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getFunctionFacetRule()); }
	 iv_ruleFunctionFacet=ruleFunctionFacet 
	 { $current=$iv_ruleFunctionFacet.current; } 
	 EOF 
;

// Rule FunctionFacet
ruleFunctionFacet returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(((
(
		lv_key_0_0=	'function:' 
    {
        newLeafNode(lv_key_0_0, grammarAccess.getFunctionFacetAccess().getKeyFunctionKeyword_0_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getFunctionFacetRule());
	        }
       		setWithLastConsumed($current, "key", lv_key_0_0, "function:");
	    }

)
)
    |(
(
		lv_key_1_0=	'->' 
    {
        newLeafNode(lv_key_1_0, grammarAccess.getFunctionFacetAccess().getKeyHyphenMinusGreaterThanSignKeyword_0_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getFunctionFacetRule());
	        }
       		setWithLastConsumed($current, "key", lv_key_1_0, "->");
	    }

)
))	otherlv_2='{' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getFunctionFacetAccess().getLeftCurlyBracketKeyword_1());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getFunctionFacetAccess().getExprExpressionParserRuleCall_2_0()); 
	    }
		lv_expr_3_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getFunctionFacetRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_3_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_4='}' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getFunctionFacetAccess().getRightCurlyBracketKeyword_3());
    }
)
;





// Entry rule entryRuleTypeFacet
entryRuleTypeFacet returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getTypeFacetRule()); }
	 iv_ruleTypeFacet=ruleTypeFacet 
	 { $current=$iv_ruleTypeFacet.current; } 
	 EOF 
;

// Rule TypeFacet
ruleTypeFacet returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getTypeFacetAccess().getKeyTypeFacetKeyParserRuleCall_0_0()); 
	    }
		lv_key_0_0=ruleTypeFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypeFacetRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml.TypeFacetKey");
	        afterParserOrEnumRuleCall();
	    }

)
)((((
(
ruleTypeRef
)
))=>(
(
		{ 
	        newCompositeNode(grammarAccess.getTypeFacetAccess().getExprTypeRefParserRuleCall_1_0_0_0()); 
	    }
		lv_expr_1_0=ruleTypeRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypeFacetRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_1_0, 
        		"msi.gama.lang.gaml.Gaml.TypeRef");
	        afterParserOrEnumRuleCall();
	    }

)
))
    |(
(
		{ 
	        newCompositeNode(grammarAccess.getTypeFacetAccess().getExprExpressionParserRuleCall_1_1_0()); 
	    }
		lv_expr_2_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypeFacetRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_2_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)))
;





// Entry rule entryRuleActionFacet
entryRuleActionFacet returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getActionFacetRule()); }
	 iv_ruleActionFacet=ruleActionFacet 
	 { $current=$iv_ruleActionFacet.current; } 
	 EOF 
;

// Rule ActionFacet
ruleActionFacet returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getActionFacetAccess().getKeyActionFacetKeyParserRuleCall_0_0()); 
	    }
		lv_key_0_0=ruleActionFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getActionFacetRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml.ActionFacetKey");
	        afterParserOrEnumRuleCall();
	    }

)
)((
(
		{ 
	        newCompositeNode(grammarAccess.getActionFacetAccess().getExprActionRefParserRuleCall_1_0_0()); 
	    }
		lv_expr_1_0=ruleActionRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getActionFacetRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_1_0, 
        		"msi.gama.lang.gaml.Gaml.ActionRef");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |(
(
		{ 
	        newCompositeNode(grammarAccess.getActionFacetAccess().getBlockBlockParserRuleCall_1_1_0()); 
	    }
		lv_block_2_0=ruleBlock		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getActionFacetRule());
	        }
       		set(
       			$current, 
       			"block",
        		lv_block_2_0, 
        		"msi.gama.lang.gaml.Gaml.Block");
	        afterParserOrEnumRuleCall();
	    }

)
)))
;





// Entry rule entryRuleVarFacet
entryRuleVarFacet returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getVarFacetRule()); }
	 iv_ruleVarFacet=ruleVarFacet 
	 { $current=$iv_ruleVarFacet.current; } 
	 EOF 
;

// Rule VarFacet
ruleVarFacet returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getVarFacetAccess().getKeyVarFacetKeyParserRuleCall_0_0()); 
	    }
		lv_key_0_0=ruleVarFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getVarFacetRule());
	        }
       		set(
       			$current, 
       			"key",
        		lv_key_0_0, 
        		"msi.gama.lang.gaml.Gaml.VarFacetKey");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getVarFacetAccess().getExprVariableRefParserRuleCall_1_0()); 
	    }
		lv_expr_1_0=ruleVariableRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getVarFacetRule());
	        }
       		set(
       			$current, 
       			"expr",
        		lv_expr_1_0, 
        		"msi.gama.lang.gaml.Gaml.VariableRef");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleBlock
entryRuleBlock returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getBlockRule()); }
	 iv_ruleBlock=ruleBlock 
	 { $current=$iv_ruleBlock.current; } 
	 EOF 
;

// Rule Block
ruleBlock returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getBlockAccess().getBlockAction_0(),
            $current);
    }
)	otherlv_1='{' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getBlockAccess().getLeftCurlyBracketKeyword_1());
    }
(((((
(
ruleExpression
)
)	'}' 
))=>((
(
		{ 
	        newCompositeNode(grammarAccess.getBlockAccess().getFunctionExpressionParserRuleCall_2_0_0_0_0()); 
	    }
		lv_function_2_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getBlockRule());
	        }
       		set(
       			$current, 
       			"function",
        		lv_function_2_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_3='}' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getBlockAccess().getRightCurlyBracketKeyword_2_0_0_1());
    }
))
    |((
(
		{ 
	        newCompositeNode(grammarAccess.getBlockAccess().getStatementsStatementParserRuleCall_2_1_0_0()); 
	    }
		lv_statements_4_0=ruleStatement		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getBlockRule());
	        }
       		add(
       			$current, 
       			"statements",
        		lv_statements_4_0, 
        		"msi.gama.lang.gaml.Gaml.Statement");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_5='}' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getBlockAccess().getRightCurlyBracketKeyword_2_1_1());
    }
)))
;





// Entry rule entryRuleExpression
entryRuleExpression returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getExpressionRule()); }
	 iv_ruleExpression=ruleExpression 
	 { $current=$iv_ruleExpression.current; } 
	 EOF 
;

// Rule Expression
ruleExpression returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((((((
(
ruleValid_ID
)
)	'::' 
)
    |((
(
(
ruleDefinitionFacetKey
    |ruleTypeFacetKey
    |ruleSpecialFacetKey
    |ruleActionFacetKey
    |ruleVarFacetKey
)

)
)	':' 
)))=>
    { 
        newCompositeNode(grammarAccess.getExpressionAccess().getArgumentPairParserRuleCall_0()); 
    }
    this_ArgumentPair_0=ruleArgumentPair
    { 
        $current = $this_ArgumentPair_0.current; 
        afterParserOrEnumRuleCall();
    }
)
    |
    { 
        newCompositeNode(grammarAccess.getExpressionAccess().getPairParserRuleCall_1()); 
    }
    this_Pair_1=rulePair
    { 
        $current = $this_Pair_1.current; 
        afterParserOrEnumRuleCall();
    }
)
;





// Entry rule entryRuleArgumentPair
entryRuleArgumentPair returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getArgumentPairRule()); }
	 iv_ruleArgumentPair=ruleArgumentPair 
	 { $current=$iv_ruleArgumentPair.current; } 
	 EOF 
;

// Rule ArgumentPair
ruleArgumentPair returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((((((
(
ruleValid_ID
)
)	'::' 
)
    |((
(
(
ruleDefinitionFacetKey
    |ruleTypeFacetKey
    |ruleSpecialFacetKey
    |ruleActionFacetKey
    |ruleVarFacetKey
)

)
)	':' 
)))=>(((
(
		{ 
	        newCompositeNode(grammarAccess.getArgumentPairAccess().getOpValid_IDParserRuleCall_0_0_0_0_0()); 
	    }
		lv_op_0_0=ruleValid_ID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getArgumentPairRule());
	        }
       		set(
       			$current, 
       			"op",
        		lv_op_0_0, 
        		"msi.gama.lang.gaml.Gaml.Valid_ID");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_1='::' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getArgumentPairAccess().getColonColonKeyword_0_0_0_1());
    }
)
    |((
(
(
		{ 
	        newCompositeNode(grammarAccess.getArgumentPairAccess().getOpDefinitionFacetKeyParserRuleCall_0_0_1_0_0_0()); 
	    }
		lv_op_2_1=ruleDefinitionFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getArgumentPairRule());
	        }
       		set(
       			$current, 
       			"op",
        		lv_op_2_1, 
        		"msi.gama.lang.gaml.Gaml.DefinitionFacetKey");
	        afterParserOrEnumRuleCall();
	    }

    |		{ 
	        newCompositeNode(grammarAccess.getArgumentPairAccess().getOpTypeFacetKeyParserRuleCall_0_0_1_0_0_1()); 
	    }
		lv_op_2_2=ruleTypeFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getArgumentPairRule());
	        }
       		set(
       			$current, 
       			"op",
        		lv_op_2_2, 
        		"msi.gama.lang.gaml.Gaml.TypeFacetKey");
	        afterParserOrEnumRuleCall();
	    }

    |		{ 
	        newCompositeNode(grammarAccess.getArgumentPairAccess().getOpSpecialFacetKeyParserRuleCall_0_0_1_0_0_2()); 
	    }
		lv_op_2_3=ruleSpecialFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getArgumentPairRule());
	        }
       		set(
       			$current, 
       			"op",
        		lv_op_2_3, 
        		"msi.gama.lang.gaml.Gaml.SpecialFacetKey");
	        afterParserOrEnumRuleCall();
	    }

    |		{ 
	        newCompositeNode(grammarAccess.getArgumentPairAccess().getOpActionFacetKeyParserRuleCall_0_0_1_0_0_3()); 
	    }
		lv_op_2_4=ruleActionFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getArgumentPairRule());
	        }
       		set(
       			$current, 
       			"op",
        		lv_op_2_4, 
        		"msi.gama.lang.gaml.Gaml.ActionFacetKey");
	        afterParserOrEnumRuleCall();
	    }

    |		{ 
	        newCompositeNode(grammarAccess.getArgumentPairAccess().getOpVarFacetKeyParserRuleCall_0_0_1_0_0_4()); 
	    }
		lv_op_2_5=ruleVarFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getArgumentPairRule());
	        }
       		set(
       			$current, 
       			"op",
        		lv_op_2_5, 
        		"msi.gama.lang.gaml.Gaml.VarFacetKey");
	        afterParserOrEnumRuleCall();
	    }

)

)
)	otherlv_3=':' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getArgumentPairAccess().getColonKeyword_0_0_1_1());
    }
)))?(
(
		{ 
	        newCompositeNode(grammarAccess.getArgumentPairAccess().getRightIfParserRuleCall_1_0()); 
	    }
		lv_right_4_0=ruleIf		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getArgumentPairRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_4_0, 
        		"msi.gama.lang.gaml.Gaml.If");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRulePair
entryRulePair returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getPairRule()); }
	 iv_rulePair=rulePair 
	 { $current=$iv_rulePair.current; } 
	 EOF 
;

// Rule Pair
rulePair returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getPairAccess().getIfParserRuleCall_0()); 
    }
    this_If_0=ruleIf
    { 
        $current = $this_If_0.current; 
        afterParserOrEnumRuleCall();
    }
(((
    {
        $current = forceCreateModelElementAndSet(
            grammarAccess.getPairAccess().getPairLeftAction_1_0_0(),
            $current);
    }
)(
(
		lv_op_2_0=	'::' 
    {
        newLeafNode(lv_op_2_0, grammarAccess.getPairAccess().getOpColonColonKeyword_1_0_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getPairRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_0, "::");
	    }

)
))(
(
		{ 
	        newCompositeNode(grammarAccess.getPairAccess().getRightIfParserRuleCall_1_1_0()); 
	    }
		lv_right_3_0=ruleIf		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getPairRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"msi.gama.lang.gaml.Gaml.If");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRuleIf
entryRuleIf returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getIfRule()); }
	 iv_ruleIf=ruleIf 
	 { $current=$iv_ruleIf.current; } 
	 EOF 
;

// Rule If
ruleIf returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getIfAccess().getOrParserRuleCall_0()); 
    }
    this_Or_0=ruleOr
    { 
        $current = $this_Or_0.current; 
        afterParserOrEnumRuleCall();
    }
((
    {
        $current = forceCreateModelElementAndSet(
            grammarAccess.getIfAccess().getIfLeftAction_1_0(),
            $current);
    }
)(
(
		lv_op_2_0=	'?' 
    {
        newLeafNode(lv_op_2_0, grammarAccess.getIfAccess().getOpQuestionMarkKeyword_1_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getIfRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_0, "?");
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getIfAccess().getRightOrParserRuleCall_1_2_0()); 
	    }
		lv_right_3_0=ruleOr		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getIfRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"msi.gama.lang.gaml.Gaml.Or");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_4=':' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getIfAccess().getColonKeyword_1_3_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getIfAccess().getIfFalseOrParserRuleCall_1_3_1_0()); 
	    }
		lv_ifFalse_5_0=ruleOr		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getIfRule());
	        }
       		set(
       			$current, 
       			"ifFalse",
        		lv_ifFalse_5_0, 
        		"msi.gama.lang.gaml.Gaml.Or");
	        afterParserOrEnumRuleCall();
	    }

)
)))?)
;





// Entry rule entryRuleOr
entryRuleOr returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getOrRule()); }
	 iv_ruleOr=ruleOr 
	 { $current=$iv_ruleOr.current; } 
	 EOF 
;

// Rule Or
ruleOr returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getOrAccess().getAndParserRuleCall_0()); 
    }
    this_And_0=ruleAnd
    { 
        $current = $this_And_0.current; 
        afterParserOrEnumRuleCall();
    }
((
    {
        $current = forceCreateModelElementAndSet(
            grammarAccess.getOrAccess().getExpressionLeftAction_1_0(),
            $current);
    }
)(
(
		lv_op_2_0=	'or' 
    {
        newLeafNode(lv_op_2_0, grammarAccess.getOrAccess().getOpOrKeyword_1_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getOrRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_0, "or");
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0()); 
	    }
		lv_right_3_0=ruleAnd		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getOrRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"msi.gama.lang.gaml.Gaml.And");
	        afterParserOrEnumRuleCall();
	    }

)
))*)
;





// Entry rule entryRuleAnd
entryRuleAnd returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getAndRule()); }
	 iv_ruleAnd=ruleAnd 
	 { $current=$iv_ruleAnd.current; } 
	 EOF 
;

// Rule And
ruleAnd returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getAndAccess().getCastParserRuleCall_0()); 
    }
    this_Cast_0=ruleCast
    { 
        $current = $this_Cast_0.current; 
        afterParserOrEnumRuleCall();
    }
((
    {
        $current = forceCreateModelElementAndSet(
            grammarAccess.getAndAccess().getExpressionLeftAction_1_0(),
            $current);
    }
)(
(
		lv_op_2_0=	'and' 
    {
        newLeafNode(lv_op_2_0, grammarAccess.getAndAccess().getOpAndKeyword_1_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getAndRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_0, "and");
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getAndAccess().getRightCastParserRuleCall_1_2_0()); 
	    }
		lv_right_3_0=ruleCast		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getAndRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"msi.gama.lang.gaml.Gaml.Cast");
	        afterParserOrEnumRuleCall();
	    }

)
))*)
;





// Entry rule entryRuleCast
entryRuleCast returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getCastRule()); }
	 iv_ruleCast=ruleCast 
	 { $current=$iv_ruleCast.current; } 
	 EOF 
;

// Rule Cast
ruleCast returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getCastAccess().getComparisonParserRuleCall_0()); 
    }
    this_Comparison_0=ruleComparison
    { 
        $current = $this_Comparison_0.current; 
        afterParserOrEnumRuleCall();
    }
(((
    {
        $current = forceCreateModelElementAndSet(
            grammarAccess.getCastAccess().getCastLeftAction_1_0_0(),
            $current);
    }
)(
(
		lv_op_2_0=	'as' 
    {
        newLeafNode(lv_op_2_0, grammarAccess.getCastAccess().getOpAsKeyword_1_0_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getCastRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_0, "as");
	    }

)
))((
(
		{ 
	        newCompositeNode(grammarAccess.getCastAccess().getRightTypeRefParserRuleCall_1_1_0_0()); 
	    }
		lv_right_3_0=ruleTypeRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getCastRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"msi.gama.lang.gaml.Gaml.TypeRef");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |(	otherlv_4='(' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getCastAccess().getLeftParenthesisKeyword_1_1_1_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getCastAccess().getRightTypeRefParserRuleCall_1_1_1_1_0()); 
	    }
		lv_right_5_0=ruleTypeRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getCastRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_5_0, 
        		"msi.gama.lang.gaml.Gaml.TypeRef");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_6=')' 
    {
    	newLeafNode(otherlv_6, grammarAccess.getCastAccess().getRightParenthesisKeyword_1_1_1_2());
    }
)))?)
;





// Entry rule entryRuleComparison
entryRuleComparison returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getComparisonRule()); }
	 iv_ruleComparison=ruleComparison 
	 { $current=$iv_ruleComparison.current; } 
	 EOF 
;

// Rule Comparison
ruleComparison returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getComparisonAccess().getAdditionParserRuleCall_0()); 
    }
    this_Addition_0=ruleAddition
    { 
        $current = $this_Addition_0.current; 
        afterParserOrEnumRuleCall();
    }
(((
    {
        $current = forceCreateModelElementAndSet(
            grammarAccess.getComparisonAccess().getExpressionLeftAction_1_0_0(),
            $current);
    }
)(
(
(
		lv_op_2_1=	'!=' 
    {
        newLeafNode(lv_op_2_1, grammarAccess.getComparisonAccess().getOpExclamationMarkEqualsSignKeyword_1_0_1_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getComparisonRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_1, null);
	    }

    |		lv_op_2_2=	'=' 
    {
        newLeafNode(lv_op_2_2, grammarAccess.getComparisonAccess().getOpEqualsSignKeyword_1_0_1_0_1());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getComparisonRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_2, null);
	    }

    |		lv_op_2_3=	'>=' 
    {
        newLeafNode(lv_op_2_3, grammarAccess.getComparisonAccess().getOpGreaterThanSignEqualsSignKeyword_1_0_1_0_2());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getComparisonRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_3, null);
	    }

    |		lv_op_2_4=	'<=' 
    {
        newLeafNode(lv_op_2_4, grammarAccess.getComparisonAccess().getOpLessThanSignEqualsSignKeyword_1_0_1_0_3());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getComparisonRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_4, null);
	    }

    |		lv_op_2_5=	'<' 
    {
        newLeafNode(lv_op_2_5, grammarAccess.getComparisonAccess().getOpLessThanSignKeyword_1_0_1_0_4());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getComparisonRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_5, null);
	    }

    |		lv_op_2_6=	'>' 
    {
        newLeafNode(lv_op_2_6, grammarAccess.getComparisonAccess().getOpGreaterThanSignKeyword_1_0_1_0_5());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getComparisonRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_6, null);
	    }

)

)
))(
(
		{ 
	        newCompositeNode(grammarAccess.getComparisonAccess().getRightAdditionParserRuleCall_1_1_0()); 
	    }
		lv_right_3_0=ruleAddition		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getComparisonRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"msi.gama.lang.gaml.Gaml.Addition");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRuleAddition
entryRuleAddition returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getAdditionRule()); }
	 iv_ruleAddition=ruleAddition 
	 { $current=$iv_ruleAddition.current; } 
	 EOF 
;

// Rule Addition
ruleAddition returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getAdditionAccess().getMultiplicationParserRuleCall_0()); 
    }
    this_Multiplication_0=ruleMultiplication
    { 
        $current = $this_Multiplication_0.current; 
        afterParserOrEnumRuleCall();
    }
(((
    {
        $current = forceCreateModelElementAndSet(
            grammarAccess.getAdditionAccess().getExpressionLeftAction_1_0_0(),
            $current);
    }
)(
(
(
		lv_op_2_1=	'+' 
    {
        newLeafNode(lv_op_2_1, grammarAccess.getAdditionAccess().getOpPlusSignKeyword_1_0_1_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getAdditionRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_1, null);
	    }

    |		lv_op_2_2=	'-' 
    {
        newLeafNode(lv_op_2_2, grammarAccess.getAdditionAccess().getOpHyphenMinusKeyword_1_0_1_0_1());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getAdditionRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_2, null);
	    }

)

)
))(
(
		{ 
	        newCompositeNode(grammarAccess.getAdditionAccess().getRightMultiplicationParserRuleCall_1_1_0()); 
	    }
		lv_right_3_0=ruleMultiplication		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getAdditionRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"msi.gama.lang.gaml.Gaml.Multiplication");
	        afterParserOrEnumRuleCall();
	    }

)
))*)
;





// Entry rule entryRuleMultiplication
entryRuleMultiplication returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getMultiplicationRule()); }
	 iv_ruleMultiplication=ruleMultiplication 
	 { $current=$iv_ruleMultiplication.current; } 
	 EOF 
;

// Rule Multiplication
ruleMultiplication returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getMultiplicationAccess().getExponentiationParserRuleCall_0()); 
    }
    this_Exponentiation_0=ruleExponentiation
    { 
        $current = $this_Exponentiation_0.current; 
        afterParserOrEnumRuleCall();
    }
(((
    {
        $current = forceCreateModelElementAndSet(
            grammarAccess.getMultiplicationAccess().getExpressionLeftAction_1_0_0(),
            $current);
    }
)(
(
(
		lv_op_2_1=	'*' 
    {
        newLeafNode(lv_op_2_1, grammarAccess.getMultiplicationAccess().getOpAsteriskKeyword_1_0_1_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getMultiplicationRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_1, null);
	    }

    |		lv_op_2_2=	'/' 
    {
        newLeafNode(lv_op_2_2, grammarAccess.getMultiplicationAccess().getOpSolidusKeyword_1_0_1_0_1());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getMultiplicationRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_2, null);
	    }

)

)
))(
(
		{ 
	        newCompositeNode(grammarAccess.getMultiplicationAccess().getRightExponentiationParserRuleCall_1_1_0()); 
	    }
		lv_right_3_0=ruleExponentiation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getMultiplicationRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"msi.gama.lang.gaml.Gaml.Exponentiation");
	        afterParserOrEnumRuleCall();
	    }

)
))*)
;





// Entry rule entryRuleExponentiation
entryRuleExponentiation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getExponentiationRule()); }
	 iv_ruleExponentiation=ruleExponentiation 
	 { $current=$iv_ruleExponentiation.current; } 
	 EOF 
;

// Rule Exponentiation
ruleExponentiation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getExponentiationAccess().getBinaryParserRuleCall_0()); 
    }
    this_Binary_0=ruleBinary
    { 
        $current = $this_Binary_0.current; 
        afterParserOrEnumRuleCall();
    }
(((
    {
        $current = forceCreateModelElementAndSet(
            grammarAccess.getExponentiationAccess().getExpressionLeftAction_1_0_0(),
            $current);
    }
)(
(
		lv_op_2_0=	'^' 
    {
        newLeafNode(lv_op_2_0, grammarAccess.getExponentiationAccess().getOpCircumflexAccentKeyword_1_0_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getExponentiationRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_0, "^");
	    }

)
))(
(
		{ 
	        newCompositeNode(grammarAccess.getExponentiationAccess().getRightBinaryParserRuleCall_1_1_0()); 
	    }
		lv_right_3_0=ruleBinary		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getExponentiationRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"msi.gama.lang.gaml.Gaml.Binary");
	        afterParserOrEnumRuleCall();
	    }

)
))*)
;





// Entry rule entryRuleBinary
entryRuleBinary returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getBinaryRule()); }
	 iv_ruleBinary=ruleBinary 
	 { $current=$iv_ruleBinary.current; } 
	 EOF 
;

// Rule Binary
ruleBinary returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getBinaryAccess().getUnitParserRuleCall_0()); 
    }
    this_Unit_0=ruleUnit
    { 
        $current = $this_Unit_0.current; 
        afterParserOrEnumRuleCall();
    }
(((
    {
        $current = forceCreateModelElementAndSet(
            grammarAccess.getBinaryAccess().getBinaryLeftAction_1_0_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getBinaryAccess().getOpValid_IDParserRuleCall_1_0_1_0()); 
	    }
		lv_op_2_0=ruleValid_ID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getBinaryRule());
	        }
       		set(
       			$current, 
       			"op",
        		lv_op_2_0, 
        		"msi.gama.lang.gaml.Gaml.Valid_ID");
	        afterParserOrEnumRuleCall();
	    }

)
))(
(
		{ 
	        newCompositeNode(grammarAccess.getBinaryAccess().getRightUnitParserRuleCall_1_1_0()); 
	    }
		lv_right_3_0=ruleUnit		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getBinaryRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"msi.gama.lang.gaml.Gaml.Unit");
	        afterParserOrEnumRuleCall();
	    }

)
))*)
;





// Entry rule entryRuleUnit
entryRuleUnit returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getUnitRule()); }
	 iv_ruleUnit=ruleUnit 
	 { $current=$iv_ruleUnit.current; } 
	 EOF 
;

// Rule Unit
ruleUnit returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getUnitAccess().getUnaryParserRuleCall_0()); 
    }
    this_Unary_0=ruleUnary
    { 
        $current = $this_Unary_0.current; 
        afterParserOrEnumRuleCall();
    }
(((
    {
        $current = forceCreateModelElementAndSet(
            grammarAccess.getUnitAccess().getUnitLeftAction_1_0_0(),
            $current);
    }
)(
(
(
		lv_op_2_1=	'\u00B0' 
    {
        newLeafNode(lv_op_2_1, grammarAccess.getUnitAccess().getOpDegreeSignKeyword_1_0_1_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getUnitRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_1, null);
	    }

    |		lv_op_2_2=	'#' 
    {
        newLeafNode(lv_op_2_2, grammarAccess.getUnitAccess().getOpNumberSignKeyword_1_0_1_0_1());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getUnitRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_2, null);
	    }

)

)
))(
(
		{ 
	        newCompositeNode(grammarAccess.getUnitAccess().getRightUnitRefParserRuleCall_1_1_0()); 
	    }
		lv_right_3_0=ruleUnitRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getUnitRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"msi.gama.lang.gaml.Gaml.UnitRef");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRuleUnary
entryRuleUnary returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getUnaryRule()); }
	 iv_ruleUnary=ruleUnary 
	 { $current=$iv_ruleUnary.current; } 
	 EOF 
;

// Rule Unary
ruleUnary returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getUnaryAccess().getAccessParserRuleCall_0()); 
    }
    this_Access_0=ruleAccess
    { 
        $current = $this_Access_0.current; 
        afterParserOrEnumRuleCall();
    }

    |((
    {
        $current = forceCreateModelElement(
            grammarAccess.getUnaryAccess().getUnaryAction_1_0(),
            $current);
    }
)(((
(
(
		lv_op_2_1=	'\u00B0' 
    {
        newLeafNode(lv_op_2_1, grammarAccess.getUnaryAccess().getOpDegreeSignKeyword_1_1_0_0_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getUnaryRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_1, null);
	    }

    |		lv_op_2_2=	'#' 
    {
        newLeafNode(lv_op_2_2, grammarAccess.getUnaryAccess().getOpNumberSignKeyword_1_1_0_0_0_1());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getUnaryRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_2, null);
	    }

)

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getUnaryAccess().getRightUnitRefParserRuleCall_1_1_0_1_0()); 
	    }
		lv_right_3_0=ruleUnitRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getUnaryRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_3_0, 
        		"msi.gama.lang.gaml.Gaml.UnitRef");
	        afterParserOrEnumRuleCall();
	    }

)
))
    |((
(
(
		lv_op_4_1=	'-' 
    {
        newLeafNode(lv_op_4_1, grammarAccess.getUnaryAccess().getOpHyphenMinusKeyword_1_1_1_0_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getUnaryRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_4_1, null);
	    }

    |		lv_op_4_2=	'!' 
    {
        newLeafNode(lv_op_4_2, grammarAccess.getUnaryAccess().getOpExclamationMarkKeyword_1_1_1_0_0_1());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getUnaryRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_4_2, null);
	    }

    |		lv_op_4_3=	'my' 
    {
        newLeafNode(lv_op_4_3, grammarAccess.getUnaryAccess().getOpMyKeyword_1_1_1_0_0_2());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getUnaryRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_4_3, null);
	    }

    |		lv_op_4_4=	'the' 
    {
        newLeafNode(lv_op_4_4, grammarAccess.getUnaryAccess().getOpTheKeyword_1_1_1_0_0_3());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getUnaryRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_4_4, null);
	    }

    |		lv_op_4_5=	'not' 
    {
        newLeafNode(lv_op_4_5, grammarAccess.getUnaryAccess().getOpNotKeyword_1_1_1_0_0_4());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getUnaryRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_4_5, null);
	    }

)

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getUnaryAccess().getRightUnaryParserRuleCall_1_1_1_1_0()); 
	    }
		lv_right_5_0=ruleUnary		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getUnaryRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_5_0, 
        		"msi.gama.lang.gaml.Gaml.Unary");
	        afterParserOrEnumRuleCall();
	    }

)
)))))
;





// Entry rule entryRuleAccess
entryRuleAccess returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getAccessRule()); }
	 iv_ruleAccess=ruleAccess 
	 { $current=$iv_ruleAccess.current; } 
	 EOF 
;

// Rule Access
ruleAccess returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getAccessAccess().getPrimaryParserRuleCall_0()); 
    }
    this_Primary_0=rulePrimary
    { 
        $current = $this_Primary_0.current; 
        afterParserOrEnumRuleCall();
    }
((
    {
        $current = forceCreateModelElementAndSet(
            grammarAccess.getAccessAccess().getAccessLeftAction_1_0(),
            $current);
    }
)(((
(
		lv_op_2_0=	'[' 
    {
        newLeafNode(lv_op_2_0, grammarAccess.getAccessAccess().getOpLeftSquareBracketKeyword_1_1_0_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getAccessRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_2_0, "[");
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getAccessAccess().getArgsExpressionListParserRuleCall_1_1_0_1_0()); 
	    }
		lv_args_3_0=ruleExpressionList		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getAccessRule());
	        }
       		set(
       			$current, 
       			"args",
        		lv_args_3_0, 
        		"msi.gama.lang.gaml.Gaml.ExpressionList");
	        afterParserOrEnumRuleCall();
	    }

)
)?	otherlv_4=']' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getAccessAccess().getRightSquareBracketKeyword_1_1_0_2());
    }
)
    |((
(
		lv_op_5_0=	'.' 
    {
        newLeafNode(lv_op_5_0, grammarAccess.getAccessAccess().getOpFullStopKeyword_1_1_1_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getAccessRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_5_0, ".");
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getAccessAccess().getRightAbstractRefParserRuleCall_1_1_1_1_0()); 
	    }
		lv_right_6_0=ruleAbstractRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getAccessRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_6_0, 
        		"msi.gama.lang.gaml.Gaml.AbstractRef");
	        afterParserOrEnumRuleCall();
	    }

)
))
    |((
(
		lv_op_7_0=	'.' 
    {
        newLeafNode(lv_op_7_0, grammarAccess.getAccessAccess().getOpFullStopKeyword_1_1_2_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getAccessRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_7_0, ".");
	    }

)
)(
(
		lv_named_exp_8_0=RULE_STRING
		{
			newLeafNode(lv_named_exp_8_0, grammarAccess.getAccessAccess().getNamed_expSTRINGTerminalRuleCall_1_1_2_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getAccessRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"named_exp",
        		lv_named_exp_8_0, 
        		"msi.gama.lang.gaml.Gaml.STRING");
	    }

)
))))*)
;





// Entry rule entryRulePrimary
entryRulePrimary returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getPrimaryRule()); }
	 iv_rulePrimary=rulePrimary 
	 { $current=$iv_rulePrimary.current; } 
	 EOF 
;

// Rule Primary
rulePrimary returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getPrimaryAccess().getTerminalExpressionParserRuleCall_0()); 
    }
    this_TerminalExpression_0=ruleTerminalExpression
    { 
        $current = $this_TerminalExpression_0.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getPrimaryAccess().getAbstractRefParserRuleCall_1()); 
    }
    this_AbstractRef_1=ruleAbstractRef
    { 
        $current = $this_AbstractRef_1.current; 
        afterParserOrEnumRuleCall();
    }

    |(	otherlv_2='(' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_2_0());
    }

    { 
        newCompositeNode(grammarAccess.getPrimaryAccess().getExpressionListParserRuleCall_2_1()); 
    }
    this_ExpressionList_3=ruleExpressionList
    { 
        $current = $this_ExpressionList_3.current; 
        afterParserOrEnumRuleCall();
    }
	otherlv_4=')' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_2_2());
    }
)
    |(	otherlv_5='(' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_3_0());
    }
(
    {
        $current = forceCreateModelElement(
            grammarAccess.getPrimaryAccess().getParametersAction_3_1(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getPrimaryAccess().getParamsParameterListParserRuleCall_3_2_0()); 
	    }
		lv_params_7_0=ruleParameterList		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getPrimaryRule());
	        }
       		set(
       			$current, 
       			"params",
        		lv_params_7_0, 
        		"msi.gama.lang.gaml.Gaml.ParameterList");
	        afterParserOrEnumRuleCall();
	    }

)
)?	otherlv_8=')' 
    {
    	newLeafNode(otherlv_8, grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_3_3());
    }
)
    |(	otherlv_9='[' 
    {
    	newLeafNode(otherlv_9, grammarAccess.getPrimaryAccess().getLeftSquareBracketKeyword_4_0());
    }
(
    {
        $current = forceCreateModelElement(
            grammarAccess.getPrimaryAccess().getArrayAction_4_1(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getPrimaryAccess().getExprsExpressionListParserRuleCall_4_2_0()); 
	    }
		lv_exprs_11_0=ruleExpressionList		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getPrimaryRule());
	        }
       		set(
       			$current, 
       			"exprs",
        		lv_exprs_11_0, 
        		"msi.gama.lang.gaml.Gaml.ExpressionList");
	        afterParserOrEnumRuleCall();
	    }

)
)?	otherlv_12=']' 
    {
    	newLeafNode(otherlv_12, grammarAccess.getPrimaryAccess().getRightSquareBracketKeyword_4_3());
    }
)
    |(	otherlv_13='{' 
    {
    	newLeafNode(otherlv_13, grammarAccess.getPrimaryAccess().getLeftCurlyBracketKeyword_5_0());
    }
(
    {
        $current = forceCreateModelElement(
            grammarAccess.getPrimaryAccess().getPointAction_5_1(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getPrimaryAccess().getLeftExpressionParserRuleCall_5_2_0()); 
	    }
		lv_left_15_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getPrimaryRule());
	        }
       		set(
       			$current, 
       			"left",
        		lv_left_15_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		lv_op_16_0=	',' 
    {
        newLeafNode(lv_op_16_0, grammarAccess.getPrimaryAccess().getOpCommaKeyword_5_3_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getPrimaryRule());
	        }
       		setWithLastConsumed($current, "op", lv_op_16_0, ",");
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getPrimaryAccess().getRightExpressionParserRuleCall_5_4_0()); 
	    }
		lv_right_17_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getPrimaryRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_17_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_18=',' 
    {
    	newLeafNode(otherlv_18, grammarAccess.getPrimaryAccess().getCommaKeyword_5_5_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getPrimaryAccess().getZExpressionParserRuleCall_5_5_1_0()); 
	    }
		lv_z_19_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getPrimaryRule());
	        }
       		set(
       			$current, 
       			"z",
        		lv_z_19_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
))?	otherlv_20='}' 
    {
    	newLeafNode(otherlv_20, grammarAccess.getPrimaryAccess().getRightCurlyBracketKeyword_5_6());
    }
))
;





// Entry rule entryRuleAbstractRef
entryRuleAbstractRef returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getAbstractRefRule()); }
	 iv_ruleAbstractRef=ruleAbstractRef 
	 { $current=$iv_ruleAbstractRef.current; } 
	 EOF 
;

// Rule AbstractRef
ruleAbstractRef returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getAbstractRefAccess().getFunctionParserRuleCall_0()); 
    }
    this_Function_0=ruleFunction
    { 
        $current = $this_Function_0.current; 
        afterParserOrEnumRuleCall();
    }

    |(((	ruleCastingFunction)=>
    { 
        newCompositeNode(grammarAccess.getAbstractRefAccess().getCastingFunctionParserRuleCall_1_0()); 
    }
    this_CastingFunction_1=ruleCastingFunction
    { 
        $current = $this_CastingFunction_1.current; 
        afterParserOrEnumRuleCall();
    }
)
    |
    { 
        newCompositeNode(grammarAccess.getAbstractRefAccess().getVariableRefParserRuleCall_1_1()); 
    }
    this_VariableRef_2=ruleVariableRef
    { 
        $current = $this_VariableRef_2.current; 
        afterParserOrEnumRuleCall();
    }
))
;





// Entry rule entryRuleFunction
entryRuleFunction returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getFunctionRule()); }
	 iv_ruleFunction=ruleFunction 
	 { $current=$iv_ruleFunction.current; } 
	 EOF 
;

// Rule Function
ruleFunction returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getFunctionAccess().getFunctionAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getFunctionAccess().getActionActionRefParserRuleCall_1_0()); 
	    }
		lv_action_1_0=ruleActionRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getFunctionRule());
	        }
       		set(
       			$current, 
       			"action",
        		lv_action_1_0, 
        		"msi.gama.lang.gaml.Gaml.ActionRef");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_2='(' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getFunctionAccess().getLeftParenthesisKeyword_2());
    }
((
(
		{ 
	        newCompositeNode(grammarAccess.getFunctionAccess().getParametersParametersParserRuleCall_3_0_0()); 
	    }
		lv_parameters_3_0=ruleParameters		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getFunctionRule());
	        }
       		set(
       			$current, 
       			"parameters",
        		lv_parameters_3_0, 
        		"msi.gama.lang.gaml.Gaml.Parameters");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |(
(
		{ 
	        newCompositeNode(grammarAccess.getFunctionAccess().getArgsExpressionListParserRuleCall_3_1_0()); 
	    }
		lv_args_4_0=ruleExpressionList		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getFunctionRule());
	        }
       		set(
       			$current, 
       			"args",
        		lv_args_4_0, 
        		"msi.gama.lang.gaml.Gaml.ExpressionList");
	        afterParserOrEnumRuleCall();
	    }

)
))	otherlv_5=')' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getFunctionAccess().getRightParenthesisKeyword_4());
    }
)
;





// Entry rule entryRuleCastingFunction
entryRuleCastingFunction returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getCastingFunctionRule()); }
	 iv_ruleCastingFunction=ruleCastingFunction 
	 { $current=$iv_ruleCastingFunction.current; } 
	 EOF 
;

// Rule CastingFunction
ruleCastingFunction returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getCastingFunctionAccess().getActionActionRefParserRuleCall_0_0()); 
	    }
		lv_action_0_0=ruleActionRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getCastingFunctionRule());
	        }
       		set(
       			$current, 
       			"action",
        		lv_action_0_0, 
        		"msi.gama.lang.gaml.Gaml.ActionRef");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getCastingFunctionAccess().getTypeTypeInfoParserRuleCall_1_0()); 
	    }
		lv_type_1_0=ruleTypeInfo		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getCastingFunctionRule());
	        }
       		set(
       			$current, 
       			"type",
        		lv_type_1_0, 
        		"msi.gama.lang.gaml.Gaml.TypeInfo");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_2='(' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getCastingFunctionAccess().getLeftParenthesisKeyword_2());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getCastingFunctionAccess().getArgsExpressionListParserRuleCall_3_0()); 
	    }
		lv_args_3_0=ruleExpressionList		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getCastingFunctionRule());
	        }
       		set(
       			$current, 
       			"args",
        		lv_args_3_0, 
        		"msi.gama.lang.gaml.Gaml.ExpressionList");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_4=')' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getCastingFunctionAccess().getRightParenthesisKeyword_4());
    }
)
;





// Entry rule entryRuleParameter
entryRuleParameter returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getParameterRule()); }
	 iv_ruleParameter=ruleParameter 
	 { $current=$iv_ruleParameter.current; } 
	 EOF 
;

// Rule Parameter
ruleParameter returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getParameterAccess().getParameterAction_0(),
            $current);
    }
)((
(
(
		{ 
	        newCompositeNode(grammarAccess.getParameterAccess().getBuiltInFacetKeyDefinitionFacetKeyParserRuleCall_1_0_0_0()); 
	    }
		lv_builtInFacetKey_1_1=ruleDefinitionFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getParameterRule());
	        }
       		set(
       			$current, 
       			"builtInFacetKey",
        		lv_builtInFacetKey_1_1, 
        		"msi.gama.lang.gaml.Gaml.DefinitionFacetKey");
	        afterParserOrEnumRuleCall();
	    }

    |		{ 
	        newCompositeNode(grammarAccess.getParameterAccess().getBuiltInFacetKeyTypeFacetKeyParserRuleCall_1_0_0_1()); 
	    }
		lv_builtInFacetKey_1_2=ruleTypeFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getParameterRule());
	        }
       		set(
       			$current, 
       			"builtInFacetKey",
        		lv_builtInFacetKey_1_2, 
        		"msi.gama.lang.gaml.Gaml.TypeFacetKey");
	        afterParserOrEnumRuleCall();
	    }

    |		{ 
	        newCompositeNode(grammarAccess.getParameterAccess().getBuiltInFacetKeySpecialFacetKeyParserRuleCall_1_0_0_2()); 
	    }
		lv_builtInFacetKey_1_3=ruleSpecialFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getParameterRule());
	        }
       		set(
       			$current, 
       			"builtInFacetKey",
        		lv_builtInFacetKey_1_3, 
        		"msi.gama.lang.gaml.Gaml.SpecialFacetKey");
	        afterParserOrEnumRuleCall();
	    }

    |		{ 
	        newCompositeNode(grammarAccess.getParameterAccess().getBuiltInFacetKeyActionFacetKeyParserRuleCall_1_0_0_3()); 
	    }
		lv_builtInFacetKey_1_4=ruleActionFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getParameterRule());
	        }
       		set(
       			$current, 
       			"builtInFacetKey",
        		lv_builtInFacetKey_1_4, 
        		"msi.gama.lang.gaml.Gaml.ActionFacetKey");
	        afterParserOrEnumRuleCall();
	    }

    |		{ 
	        newCompositeNode(grammarAccess.getParameterAccess().getBuiltInFacetKeyVarFacetKeyParserRuleCall_1_0_0_4()); 
	    }
		lv_builtInFacetKey_1_5=ruleVarFacetKey		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getParameterRule());
	        }
       		set(
       			$current, 
       			"builtInFacetKey",
        		lv_builtInFacetKey_1_5, 
        		"msi.gama.lang.gaml.Gaml.VarFacetKey");
	        afterParserOrEnumRuleCall();
	    }

)

)
)
    |((
(
		{ 
	        newCompositeNode(grammarAccess.getParameterAccess().getLeftVariableRefParserRuleCall_1_1_0_0()); 
	    }
		lv_left_2_0=ruleVariableRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getParameterRule());
	        }
       		set(
       			$current, 
       			"left",
        		lv_left_2_0, 
        		"msi.gama.lang.gaml.Gaml.VariableRef");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_3=':' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getParameterAccess().getColonKeyword_1_1_1());
    }
))(
(
		{ 
	        newCompositeNode(grammarAccess.getParameterAccess().getRightExpressionParserRuleCall_2_0()); 
	    }
		lv_right_4_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getParameterRule());
	        }
       		set(
       			$current, 
       			"right",
        		lv_right_4_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleExpressionList
entryRuleExpressionList returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getExpressionListRule()); }
	 iv_ruleExpressionList=ruleExpressionList 
	 { $current=$iv_ruleExpressionList.current; } 
	 EOF 
;

// Rule ExpressionList
ruleExpressionList returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getExpressionListAccess().getExprsExpressionParserRuleCall_0_0()); 
	    }
		lv_exprs_0_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getExpressionListRule());
	        }
       		add(
       			$current, 
       			"exprs",
        		lv_exprs_0_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_1=',' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getExpressionListAccess().getCommaKeyword_1_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getExpressionListAccess().getExprsExpressionParserRuleCall_1_1_0()); 
	    }
		lv_exprs_2_0=ruleExpression		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getExpressionListRule());
	        }
       		add(
       			$current, 
       			"exprs",
        		lv_exprs_2_0, 
        		"msi.gama.lang.gaml.Gaml.Expression");
	        afterParserOrEnumRuleCall();
	    }

)
))*)
;





// Entry rule entryRuleParameterList
entryRuleParameterList returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getParameterListRule()); }
	 iv_ruleParameterList=ruleParameterList 
	 { $current=$iv_ruleParameterList.current; } 
	 EOF 
;

// Rule ParameterList
ruleParameterList returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getParameterListAccess().getExprsParameterParserRuleCall_0_0()); 
	    }
		lv_exprs_0_0=ruleParameter		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getParameterListRule());
	        }
       		add(
       			$current, 
       			"exprs",
        		lv_exprs_0_0, 
        		"msi.gama.lang.gaml.Gaml.Parameter");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_1=',' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getParameterListAccess().getCommaKeyword_1_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getParameterListAccess().getExprsParameterParserRuleCall_1_1_0()); 
	    }
		lv_exprs_2_0=ruleParameter		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getParameterListRule());
	        }
       		add(
       			$current, 
       			"exprs",
        		lv_exprs_2_0, 
        		"msi.gama.lang.gaml.Gaml.Parameter");
	        afterParserOrEnumRuleCall();
	    }

)
))*)
;





// Entry rule entryRuleUnitRef
entryRuleUnitRef returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getUnitRefRule()); }
	 iv_ruleUnitRef=ruleUnitRef 
	 { $current=$iv_ruleUnitRef.current; } 
	 EOF 
;

// Rule UnitRef
ruleUnitRef returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getUnitRefAccess().getUnitNameAction_0(),
            $current);
    }
)(
(
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getUnitRefRule());
	        }
        }
	otherlv_1=RULE_ID
	{
		newLeafNode(otherlv_1, grammarAccess.getUnitRefAccess().getRefUnitFakeDefinitionCrossReference_1_0()); 
	}

)
))
;





// Entry rule entryRuleVariableRef
entryRuleVariableRef returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getVariableRefRule()); }
	 iv_ruleVariableRef=ruleVariableRef 
	 { $current=$iv_ruleVariableRef.current; } 
	 EOF 
;

// Rule VariableRef
ruleVariableRef returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getVariableRefAccess().getVariableRefAction_0(),
            $current);
    }
)(
(
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getVariableRefRule());
	        }
        }
		{ 
	        newCompositeNode(grammarAccess.getVariableRefAccess().getRefVarDefinitionCrossReference_1_0()); 
	    }
		ruleValid_ID		{ 
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleTypeRef
entryRuleTypeRef returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getTypeRefRule()); }
	 iv_ruleTypeRef=ruleTypeRef 
	 { $current=$iv_ruleTypeRef.current; } 
	 EOF 
;

// Rule TypeRef
ruleTypeRef returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(((
    {
        $current = forceCreateModelElement(
            grammarAccess.getTypeRefAccess().getTypeRefAction_0_0(),
            $current);
    }
)((
(
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getTypeRefRule());
	        }
        }
	otherlv_1=RULE_ID
	{
		newLeafNode(otherlv_1, grammarAccess.getTypeRefAccess().getRefTypeDefinitionCrossReference_0_1_0_0()); 
	}

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getTypeRefAccess().getParameterTypeInfoParserRuleCall_0_1_1_0()); 
	    }
		lv_parameter_2_0=ruleTypeInfo		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypeRefRule());
	        }
       		set(
       			$current, 
       			"parameter",
        		lv_parameter_2_0, 
        		"msi.gama.lang.gaml.Gaml.TypeInfo");
	        afterParserOrEnumRuleCall();
	    }

)
)?))
    |((
    {
        $current = forceCreateModelElement(
            grammarAccess.getTypeRefAccess().getTypeRefAction_1_0(),
            $current);
    }
)(	otherlv_4='species' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getTypeRefAccess().getSpeciesKeyword_1_1_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTypeRefAccess().getParameterTypeInfoParserRuleCall_1_1_1_0()); 
	    }
		lv_parameter_5_0=ruleTypeInfo		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypeRefRule());
	        }
       		set(
       			$current, 
       			"parameter",
        		lv_parameter_5_0, 
        		"msi.gama.lang.gaml.Gaml.TypeInfo");
	        afterParserOrEnumRuleCall();
	    }

)
))))
;





// Entry rule entryRuleTypeInfo
entryRuleTypeInfo returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getTypeInfoRule()); }
	 iv_ruleTypeInfo=ruleTypeInfo 
	 { $current=$iv_ruleTypeInfo.current; } 
	 EOF 
;

// Rule TypeInfo
ruleTypeInfo returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='<' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getTypeInfoAccess().getLessThanSignKeyword_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTypeInfoAccess().getFirstTypeRefParserRuleCall_1_0()); 
	    }
		lv_first_1_0=ruleTypeRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypeInfoRule());
	        }
       		set(
       			$current, 
       			"first",
        		lv_first_1_0, 
        		"msi.gama.lang.gaml.Gaml.TypeRef");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_2=',' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getTypeInfoAccess().getCommaKeyword_2_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTypeInfoAccess().getSecondTypeRefParserRuleCall_2_1_0()); 
	    }
		lv_second_3_0=ruleTypeRef		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypeInfoRule());
	        }
       		set(
       			$current, 
       			"second",
        		lv_second_3_0, 
        		"msi.gama.lang.gaml.Gaml.TypeRef");
	        afterParserOrEnumRuleCall();
	    }

)
))?((	'>' 
)=>	otherlv_4='>' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getTypeInfoAccess().getGreaterThanSignKeyword_3());
    }
))
;





// Entry rule entryRuleActionRef
entryRuleActionRef returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getActionRefRule()); }
	 iv_ruleActionRef=ruleActionRef 
	 { $current=$iv_ruleActionRef.current; } 
	 EOF 
;

// Rule ActionRef
ruleActionRef returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getActionRefAccess().getActionRefAction_0(),
            $current);
    }
)(
(
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getActionRefRule());
	        }
        }
		{ 
	        newCompositeNode(grammarAccess.getActionRefAccess().getRefActionDefinitionCrossReference_1_0()); 
	    }
		ruleValid_ID		{ 
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleEquationRef
entryRuleEquationRef returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getEquationRefRule()); }
	 iv_ruleEquationRef=ruleEquationRef 
	 { $current=$iv_ruleEquationRef.current; } 
	 EOF 
;

// Rule EquationRef
ruleEquationRef returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getEquationRefAccess().getEquationRefAction_0(),
            $current);
    }
)(
(
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getEquationRefRule());
	        }
        }
		{ 
	        newCompositeNode(grammarAccess.getEquationRefAccess().getRefEquationDefinitionCrossReference_1_0()); 
	    }
		ruleValid_ID		{ 
	        afterParserOrEnumRuleCall();
	    }

)
))
;





// Entry rule entryRuleValid_ID
entryRuleValid_ID returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.getValid_IDRule()); } 
	 iv_ruleValid_ID=ruleValid_ID 
	 { $current=$iv_ruleValid_ID.current.getText(); }  
	 EOF 
;

// Rule Valid_ID
ruleValid_ID returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getValid_IDAccess().get_SpeciesKeyParserRuleCall_0()); 
    }
    this__SpeciesKey_0=rule_SpeciesKey    {
		$current.merge(this__SpeciesKey_0);
    }

    { 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getValid_IDAccess().get_DoKeyParserRuleCall_1()); 
    }
    this__DoKey_1=rule_DoKey    {
		$current.merge(this__DoKey_1);
    }

    { 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getValid_IDAccess().get_ReflexKeyParserRuleCall_2()); 
    }
    this__ReflexKey_2=rule_ReflexKey    {
		$current.merge(this__ReflexKey_2);
    }

    { 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getValid_IDAccess().get_VarOrConstKeyParserRuleCall_3()); 
    }
    this__VarOrConstKey_3=rule_VarOrConstKey    {
		$current.merge(this__VarOrConstKey_3);
    }

    { 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getValid_IDAccess().get_1Expr_Facets_BlockOrEnd_KeyParserRuleCall_4()); 
    }
    this__1Expr_Facets_BlockOrEnd_Key_4=rule_1Expr_Facets_BlockOrEnd_Key    {
		$current.merge(this__1Expr_Facets_BlockOrEnd_Key_4);
    }

    { 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getValid_IDAccess().get_EquationsKeyParserRuleCall_5()); 
    }
    this__EquationsKey_5=rule_EquationsKey    {
		$current.merge(this__EquationsKey_5);
    }

    { 
        afterParserOrEnumRuleCall();
    }

    |    this_ID_6=RULE_ID    {
		$current.merge(this_ID_6);
    }

    { 
    newLeafNode(this_ID_6, grammarAccess.getValid_IDAccess().getIDTerminalRuleCall_6()); 
    }

    |
    { 
        newCompositeNode(grammarAccess.getValid_IDAccess().get_ExperimentKeyParserRuleCall_7()); 
    }
    this__ExperimentKey_7=rule_ExperimentKey    {
		$current.merge(this__ExperimentKey_7);
    }

    { 
        afterParserOrEnumRuleCall();
    }
)
    ;





// Entry rule entryRuleTerminalExpression
entryRuleTerminalExpression returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getTerminalExpressionRule()); }
	 iv_ruleTerminalExpression=ruleTerminalExpression 
	 { $current=$iv_ruleTerminalExpression.current; } 
	 EOF 
;

// Rule TerminalExpression
ruleTerminalExpression returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(((
    {
        $current = forceCreateModelElement(
            grammarAccess.getTerminalExpressionAccess().getIntLiteralAction_0_0(),
            $current);
    }
)(
(
		lv_op_1_0=RULE_INTEGER
		{
			newLeafNode(lv_op_1_0, grammarAccess.getTerminalExpressionAccess().getOpINTEGERTerminalRuleCall_0_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getTerminalExpressionRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"op",
        		lv_op_1_0, 
        		"msi.gama.lang.gaml.Gaml.INTEGER");
	    }

)
))
    |((
    {
        $current = forceCreateModelElement(
            grammarAccess.getTerminalExpressionAccess().getDoubleLiteralAction_1_0(),
            $current);
    }
)(
(
		lv_op_3_0=RULE_DOUBLE
		{
			newLeafNode(lv_op_3_0, grammarAccess.getTerminalExpressionAccess().getOpDOUBLETerminalRuleCall_1_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getTerminalExpressionRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"op",
        		lv_op_3_0, 
        		"msi.gama.lang.gaml.Gaml.DOUBLE");
	    }

)
))
    |((
    {
        $current = forceCreateModelElement(
            grammarAccess.getTerminalExpressionAccess().getColorLiteralAction_2_0(),
            $current);
    }
)(
(
		lv_op_5_0=RULE_COLOR
		{
			newLeafNode(lv_op_5_0, grammarAccess.getTerminalExpressionAccess().getOpCOLORTerminalRuleCall_2_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getTerminalExpressionRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"op",
        		lv_op_5_0, 
        		"msi.gama.lang.gaml.Gaml.COLOR");
	    }

)
))
    |((
    {
        $current = forceCreateModelElement(
            grammarAccess.getTerminalExpressionAccess().getStringLiteralAction_3_0(),
            $current);
    }
)(
(
		lv_op_7_0=RULE_STRING
		{
			newLeafNode(lv_op_7_0, grammarAccess.getTerminalExpressionAccess().getOpSTRINGTerminalRuleCall_3_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getTerminalExpressionRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"op",
        		lv_op_7_0, 
        		"msi.gama.lang.gaml.Gaml.STRING");
	    }

)
))
    |((
    {
        $current = forceCreateModelElement(
            grammarAccess.getTerminalExpressionAccess().getBooleanLiteralAction_4_0(),
            $current);
    }
)(
(
		lv_op_9_0=RULE_BOOLEAN
		{
			newLeafNode(lv_op_9_0, grammarAccess.getTerminalExpressionAccess().getOpBOOLEANTerminalRuleCall_4_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getTerminalExpressionRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"op",
        		lv_op_9_0, 
        		"msi.gama.lang.gaml.Gaml.BOOLEAN");
	    }

)
))
    |((
    {
        $current = forceCreateModelElement(
            grammarAccess.getTerminalExpressionAccess().getReservedLiteralAction_5_0(),
            $current);
    }
)(
(
		lv_op_11_0=RULE_KEYWORD
		{
			newLeafNode(lv_op_11_0, grammarAccess.getTerminalExpressionAccess().getOpKEYWORDTerminalRuleCall_5_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getTerminalExpressionRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"op",
        		lv_op_11_0, 
        		"msi.gama.lang.gaml.Gaml.KEYWORD");
	    }

)
)))
;





RULE_KEYWORD : ('each'|'self'|'myself'|'nil');

RULE_INTEGER : ('0'|'1'..'9' ('0'..'9')*);

RULE_BOOLEAN : ('true'|'false');

RULE_ID : ('a'..'z'|'A'..'Z'|'_'|'$') ('a'..'z'|'A'..'Z'|'_'|'$'|'0'..'9')*;

RULE_COLOR : '#' ('0'..'9'|'A'..'F')+;

RULE_DOUBLE : ('1'..'9' ('0'..'9')* ('.' ('0'..'9')+)? (('E'|'e') ('+'|'-')? ('0'..'9')+)?|'0' ('.' ('0'..'9')+)? (('E'|'e') ('+'|'-')? ('0'..'9')+)?);

RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'\''|'\\')|~(('\\'|'\'')))* '\'');

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ANY_OTHER : .;


