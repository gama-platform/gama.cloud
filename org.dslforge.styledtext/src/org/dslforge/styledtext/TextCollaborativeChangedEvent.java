/**
 * <copyright>

 *
 * Contributors:
 *     Youcef SKLAB
 *
 * </copyright>
 */
package org.dslforge.styledtext;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Event;

/**
 * This event is sent by the StyledTextContent implementor when a change to the
 * text occurs.
 */
public class TextCollaborativeChangedEvent extends TypedEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the TextCollaborativeChangedEvent to be used by the StyledTextContent
	 * implementor.
	 * <p>
	 *
	 * @param source
	 *            the object that will be sending the TextCollaborativeChangedEvent, cannot
	 *            be null
	 */
	public TextCollaborativeChangedEvent(Event source) {
		
		super(source);
	}
}
