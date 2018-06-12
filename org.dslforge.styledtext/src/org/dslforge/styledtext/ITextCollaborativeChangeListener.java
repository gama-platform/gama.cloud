/**
 * <copyright>
 *
 * 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which 
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Youcef SKLAB
 *
 * </copyright>
 */
package org.dslforge.styledtext;

import org.eclipse.swt.internal.SWTEventListener;

@SuppressWarnings("restriction")
public interface ITextCollaborativeChangeListener extends SWTEventListener {

	//
	/**
	 * Handle a text changed event
	 * 
	 * @param textCollaborativeChangedEvent
	 *            the text changed event
	 */
	void handleTextCollaborativeChanged(TextCollaborativeChangedEvent textCollaborativeChangedEvent);
}
