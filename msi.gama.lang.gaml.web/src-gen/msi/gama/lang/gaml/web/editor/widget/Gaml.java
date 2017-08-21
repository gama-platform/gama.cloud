/**
 * @Generated
 */
package msi.gama.lang.gaml.web.editor.widget;

import java.util.ArrayList;
import java.util.List;

import org.dslforge.styledtext.BasicText;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Composite;

public class Gaml extends BasicText {
	
	private static final long serialVersionUID = 1L;
	
	private static final String REMOTE_TYPE = "msi.gama.lang.gaml.web.editor.widget.Gaml";
	
	public Gaml(Composite parent, int style) {
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
		languageResources.add(new Path("src-js/msi/gama/lang/gaml/web/ace/three.min.js"));
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
