/**
 * @Generated
 */
package ummisco.gama.participative;

import org.eclipse.rap.ui.resources.IResource;

//import msi.gama.lang.gaml.resource.GamlResource;

public final class EtherGamlResource  implements IResource {

  public String getCharset() {
    return "UTF-8";
  }

  public ClassLoader getLoader() {
    return this.getClass().getClassLoader();
  }

  @Override
  public String getLocation() { 	
    return "src-js/ummisco/gama/participative/EtherGaml.js"; //$NON-NLS-1$
  }
  
  public boolean isExternal() {
    return false;
  }

  public boolean isJSLibrary() {
    return true;
  }

  public boolean isAccessible() {
    return false;
  }
}
