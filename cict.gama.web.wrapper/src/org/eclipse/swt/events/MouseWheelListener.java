package org.eclipse.swt.events;

import org.eclipse.swt.internal.SWTEventListener;

public interface MouseWheelListener extends SWTEventListener {

/**
 * Sent when the mouse wheel is scrolled.
 *
 * @param e an event containing information about the mouse wheel action
 */
public void mouseScrolled (MouseEvent e);
}

