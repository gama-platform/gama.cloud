package ummisco.gama.participative;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.dslforge.styledtext.jface.ICompletionProposal;
import org.dslforge.styledtext.jface.IContentAssistProcessor;
import org.dslforge.styledtext.jface.IDocument;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescription.Manager;
import org.eclipse.xtext.ui.editor.contentassist.XtextContentAssistProcessor;
import org.eclipse.xtext.util.ReplaceRegion;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import ummisco.gama.participative.xtext.common.BasicXtextEtherpadEditor;

/**
 * An Xtext web editor which makes use of the migrated content assist feature.
 */
public class XtextContentAssistEnabledEtherpadEditor extends BasicXtextEtherpadEditor {

	@Inject
	IContentAssistProcessor contentAssistProcessor;

	public XtextContentAssistEnabledEtherpadEditor() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		validateResource();
	}

	@Override
	protected EtherpadBasicText createTextWidget(Composite parent, int styles) {
		EtherpadBasicText textWidget = new EtherpadBasicText(parent, SWT.FILL);
		//textWidget.setEpClient(epClient);
		GridData textLayoutData = new GridData();
		textLayoutData.horizontalAlignment = SWT.FILL;
		textLayoutData.verticalAlignment = SWT.FILL;
		textLayoutData.grabExcessHorizontalSpace = true;
		textLayoutData.grabExcessVerticalSpace = true;
		textWidget.setLayoutData(textLayoutData);
		textWidget.setEditable(true);
		return textWidget;
	}

	@Override
	protected void handleTextChanged(JsonObject object) {
		int offset = object.get("offset") != null ? object.get("offset").asInt() : 0;
		String value = object.get("value") != null ? object.get("value").asString() : null;	
		if (value != null) {
			try {
				ReplaceRegion replaceRegionToBeProcessed = new ReplaceRegion(offset, value.length(), value);
				xtextResource.reparse(replaceRegionToBeProcessed.getText());
			} catch (IOException ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
		validateResource();
		updateIndex();
	}
	
	@Override
	public void updateIndex() {
		SafeRunnable.run(new SafeRunnable() {
			private static final long serialVersionUID = 1L;

			public void run() {
				iObjectDescriptions = Collections.emptyList();
				EcoreUtil2.resolveAll(xtextResource);
				IResourceDescription resourceDescription = descriptionManager.getResourceDescription(xtextResource);
				Manager manager = xtextResource.getResourceServiceProvider().getResourceDescriptionManager();
				resourceDescription = manager.getResourceDescription(xtextResource);
				iObjectDescriptions = Iterables.concat(iObjectDescriptions, resourceDescription.getExportedObjects());
				ArrayList<String> referrables = Lists.newArrayList(
						Iterables.transform(iObjectDescriptions, new Function<IEObjectDescription, String>() {
							@Override
							public String apply(IEObjectDescription input) {
								return input.getName().toString() + ":" + "reference";
							}
						}));
				Collections.sort(referrables);
				setScope(referrables);
			}
		});
	}

	@Override
	protected void createCompletionProposals() {
		IDocument document = getViewer().getDocument();
		EtherpadBasicText textWidget = (EtherpadBasicText) getViewer().getTextWidget();
		createCompletionProposals(textWidget.getOffsetAtCursorPosition());	
	}
	
	@Override
	public void createCompletionProposals(final int offset) {
		SafeRunnable.run(new SafeRunnable() {
			private static final long serialVersionUID = 1L;
			public void run() {
				XtextContentAssistProcessor xtextContentAssistProcessor = (XtextContentAssistProcessor)contentAssistProcessor;
//				ICompletionProposal[] computedCompletionProposals = XtextContentAssistProcessorEtherpad.computeCompletionProposals(getViewer(), xtextResource, offset);
//				if (computedCompletionProposals!=null) {
//					setProposals(Arrays.asList(computedCompletionProposals));	
//				}
			}
		});
	}
}
