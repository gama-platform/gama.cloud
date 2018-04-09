package ummisco.gama.participative;

/**
 * <copyright>
 *
 * Copyright (c) 2015 PlugBee. All rights reserved.
 * 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which 
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Amine Lajmi - Initial API and implementation
 *
 * </copyright>
 */


import static org.eclipse.rap.rwt.RWT.getClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import org.apache.log4j.Logger;
import org.dslforge.styledtext.Annotation;
import org.dslforge.styledtext.BasicText;
import org.dslforge.styledtext.IContentAssistListener;
import org.dslforge.styledtext.ITextChangeListener;
import org.dslforge.styledtext.ITextModifyListener;
import org.dslforge.styledtext.ITextOperationConstants;
import org.dslforge.styledtext.ITextSaveListener;
import org.dslforge.styledtext.ITextSelection;
import org.dslforge.styledtext.TextChangedEvent;
import org.dslforge.styledtext.TextRange;
import org.dslforge.styledtext.TextSavedEvent;
import org.dslforge.styledtext.TextSelection;
import org.dslforge.styledtext.jface.ICompletionProposal;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.ClientFileLoader;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.service.ResourceManager;
import org.eclipse.rap.rwt.widgets.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;

import msi.gama.lang.gaml.web.editor.widget.Gaml;



/**
 * A basic implementation of a styled text widget.
 */
public class EtherpadBasicText extends BasicText {

	private static final long serialVersionUID = 131001464693386296L;

	private static final String REMOTE_TYPE = "ummisco.gama.participative.EtherpadBasicText";
	
	public EtherpadBasicText(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	protected RemoteObject createRemoteObject(Connection connection) {
		return connection.createRemoteObject(REMOTE_TYPE);
	}
	
	@Override 
	protected void setupClient() {
		super.setupClient();
		List<IPath> languageResources = new ArrayList<IPath>();
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/ace/theme-gaml.js"));
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/ace/mode-gaml.js"));
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/ace/worker-gaml.js"));
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/ace/snippets/gaml.js"));
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/parser/antlr-all-min.js"));
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/parser/GamlParser.js"));
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/parser/GamlLexer.js"));
		registerJsResources(languageResources, getClassLoader());
		loadJsResources(languageResources);
	}

	@Override
	protected ClassLoader getClassLoader() {
		ClassLoader classLoader = Gaml.class.getClassLoader();
		return classLoader;
	}
}
