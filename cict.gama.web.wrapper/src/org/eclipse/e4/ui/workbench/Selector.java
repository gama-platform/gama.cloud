package org.eclipse.e4.ui.workbench;

import org.eclipse.e4.ui.model.application.MApplication;

public interface Selector {
	/**
	 * Call for each element to find matching elements
	 *
	 * @param element
	 *            the element
	 * @return <code>true</code> if matches else <code>false</code>
	 */
	public boolean select(MApplication element);
}