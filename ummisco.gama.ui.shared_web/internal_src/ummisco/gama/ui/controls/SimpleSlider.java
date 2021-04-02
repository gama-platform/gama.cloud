/*********************************************************************************************
 *
 * 'SimpleSlider.java, in plugin ummisco.gama.ui.shared, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.controls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import ummisco.gama.dev.utils.DEBUG;
import ummisco.gama.ui.resources.GamaColors.GamaUIColor;
import ummisco.gama.ui.resources.IGamaColors;

public class SimpleSlider extends Composite implements IPopupProvider {

	static {
		DEBUG.OFF();
	}

	final Composite parent;

	final Panel rightRegion;
	final Thumb thumb;
	final Panel leftRegion;
	boolean mouseDown = false;
	private int sliderHeight;
	private Double step = null;

	public class Thumb extends Canvas implements PaintListener {

		final Image image;

		public Thumb(final Composite parent, final Image image) {
			super(parent, SWT.NO_BACKGROUND);
			this.image = image;
			addPaintListener(this);
			final GridData d = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
			d.minimumHeight = image.getBounds().height;
			d.minimumWidth = image.getBounds().width;
			d.widthHint = image.getBounds().width;
			setLayoutData(d);
		}

		@Override
		public boolean forceFocus() {
			return true;
		}

		@Override
		public Rectangle getBounds() {
			return image.getBounds();
		}

		@Override
		public Point computeSize(final int w, final int h) {
			return new Point(image.getBounds().width, image.getBounds().height);
		}

		/**
		 * Method paintControl()
		 * 
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		@Override
		public void paintControl(final PaintEvent e) {
			final GC gc = e.gc;
			final Rectangle r = getClientArea();
			gc.setBackground(getParent().getBackground());
			gc.fillRectangle(r);
			final int height = parent.getSize().y;
			final double y = (height - (double) image.getBounds().height) / 2;
			gc.drawImage(image, 0, (int) y);
		}

	}

	/**
	 * The class implementing this interface will be asked to give a user understandable <code>String</code> to the
	 * slider's current position
	 */
	private IToolTipProvider toolTipInterperter;
	/** A list of position changed listeners */
	private final List<IPositionChangeListener> positionChangedListeners = new ArrayList<>();
	/**
	 * stores the previous position that was sent out to the position changed listeners
	 */
	double previousPosition = 0;

	GamaUIColor popupColor = IGamaColors.GRAY_LABEL;
	Popup2 popup = null;
	int thumbWidth = 0;
	private boolean notify = true;
	private final IPositionChangeListener popupListener = (slider, position) -> popup.display();

	public SimpleSlider(final Composite parent, final Color color, final Image thumbImageNormal) {
		this(parent, color, color, thumbImageNormal);
	}

	public SimpleSlider(final Composite parent, final Color leftColor, final Color rightColor,
			final Image thumbImageNormal) {
		this(parent, leftColor, rightColor, thumbImageNormal, true);
	}

	public SimpleSlider(final Composite parent, final Color leftColor, final Color rightColor,
			final Image thumbImageNormal, final boolean withPopup) {
		super(parent, SWT.DOUBLE_BUFFERED);
		this.parent = parent;
		final GridLayout gl = new GridLayout(3, false);
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		setLayout(gl);

		leftRegion = new Panel(this, leftColor);
		leftRegion.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(final MouseEvent e) {
				mouseDown = true;
				moveThumbHorizontally(e.x - thumbWidth / 2);
			}

			@Override
			public void mouseUp(final MouseEvent e) {
				mouseDown = false;
			}
		});
//		leftRegion.addMouseMoveListener(e -> {
//			if (mouseDown) {
//				moveThumbHorizontally(e.x - thumbWidth / 2);
//			}
//		});
		thumb = new Thumb(this, thumbImageNormal);
		thumbWidth = thumb.computeSize(0, 0).x;
		thumb.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(final MouseEvent e) {
				mouseDown = true;
				moveThumbHorizontally(leftRegion.getBounds().width + e.x - thumbWidth / 2);
			}

			@Override
			public void mouseUp(final MouseEvent e) {
				mouseDown = false;
			}
		});
//		thumb.addMouseMoveListener(e -> {
//			if (mouseDown) {
//				moveThumbHorizontally(leftRegion.getBounds().width + e.x - thumbWidth / 2);
//			}
//		});

		rightRegion = new Panel(this, rightColor, true);
		rightRegion.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(final MouseEvent e) {
				mouseDown = true;
				moveThumbHorizontally(leftRegion.getBounds().width + thumb.getBounds().width / 2 + e.x);
			}

			@Override
			public void mouseUp(final MouseEvent e) {
				mouseDown = false;
			}
		});
//		rightRegion.addMouseMoveListener(e -> {
//			if (mouseDown) {
//				moveThumbHorizontally(leftRegion.getBounds().width + thumb.getBounds().width / 2 + e.x);
//
//			}
//		});

		addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(final ControlEvent e) {
				updateSlider(previousPosition, false);
			}
		});

		addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(final FocusEvent e) {
				thumb.setFocus();
			}
		});

		addTraverseListener(e -> e.doit = true);
		if (withPopup) {
			addPositionChangeListener(popupListener);
			popup = new Popup2(this, leftRegion, thumb, rightRegion);
		} else {
			popup = null;
		}
		if (DEBUG.IS_ON()) {
			addPositionChangeListener((slider, position) -> DEBUG.OUT("Position changed to : " + position));
		}
	}

	public void removePositionChangeListener(final IPositionChangeListener listener) {
		synchronized (positionChangedListeners) {
			positionChangedListeners.remove(listener);
		}
	}

	public void addPositionChangeListener(final IPositionChangeListener listener) {
		synchronized (positionChangedListeners) {
			if (!positionChangedListeners.contains(listener)) {
				positionChangedListeners.add(listener);
			}
		}
	}

	/**
	 *
	 * @return the position of the slider in the form of a percentage. Note the range is from 0 to 1
	 */
	public double getCurrentPosition() {
		return previousPosition;
	}

	private void updatePositionListeners(final double perc) {
		if (!notify) { return; }
		if (Math.abs(perc - previousPosition) > 0.000001) {
			// synchronized (positionChangedListeners) {
			final Iterator<IPositionChangeListener> iter = positionChangedListeners.iterator();
			while (iter.hasNext()) {
				iter.next().positionChanged(SimpleSlider.this, perc);
			}
		}
		// }
	}

	void moveThumbHorizontally(final int x) {
		final int width = getClientArea().width - thumbWidth;
		final int pos = x < 0 ? 0 : x > width ? width : x;
		thumb.setFocus();
		leftRegion.updatePosition(pos);
		layout();

		double percentage = pos / (double) width;

		if (step != null) {
			percentage = Math.round(percentage / step) * step;
		}

		updatePositionListeners(percentage);
		previousPosition = percentage;

	}

	private class Panel extends Canvas implements PaintListener {

		private final GridData gd;
		private final Color color;

		public Panel(final Composite parent, final Color color) {
			this(parent, color, false);
		}
		//
		// @Override
		// public void setBackground(final Color color) {
		// // super.setBackground(color);
		// this.color = color;
		// }

		public Panel(final Composite parent, final Color color, final boolean lastFillerRegion) {
			super(parent, SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND);
			gd = new GridData(lastFillerRegion ? SWT.FILL : SWT.BEGINNING, SWT.BEGINNING, lastFillerRegion, false);
			gd.minimumHeight = 4;
			this.color = color;
			setLayoutData(gd);
			addPaintListener(this);
		}

		void updatePosition(final int value) {
			gd.minimumWidth = value;
			gd.widthHint = value;
		}

		/**
		 * Method paintControl()
		 * 
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		@Override
		public void paintControl(final PaintEvent e) {
			final GC gc = e.gc;
			final Rectangle r = getClientArea();
			gc.setBackground(getParent().getBackground());
			gc.fillRectangle(r);
			gc.setBackground(color);
			r.y = (int) ((double) parent.getSize().y / 2 - 2d);
			r.height = 3;
			gc.fillRoundRectangle(r.x, r.y, r.width, r.height, 3, 3);
		}

	}

	/**
	 * Method to update current position of the slider
	 *
	 * @param percentage
	 *            between 0 and 1 (i.e 0% to 100%)
	 */
	public void updateSlider(final double p, final boolean n) {
		checkWidget();

		double percentage = p;
		if (step != null) {
			percentage = Math.round(percentage / step) * step;
		}
		this.notify = n;
		if (percentage < 0) {
			percentage = 0;
		} else if (percentage > 1) {
			percentage = 1;
		}

		final int usefulWidth = getClientArea().width - thumbWidth;
		final int width = (int) Math.round(usefulWidth * percentage);
		moveThumbHorizontally(width);
		previousPosition = percentage;
		this.notify = true;
	}

	/**
	 *
	 * @param toolTipInterperter
	 */
	public void setTooltipInterperter(final IToolTipProvider toolTipInterperter) {
		this.toolTipInterperter = toolTipInterperter;
	}

	@Override
	public void setBackground(final Color color) {
		thumb.setBackground(color);
		rightRegion.setBackground(color);
		leftRegion.setBackground(color);
		super.setBackground(color);
	}

	public void setLeftBackground(final Color color) {
		leftRegion.setBackground(color);
	}

	public void setRightBackground(final Color color) {
		rightRegion.setBackground(color);
	}

	public void setPopupBackground(final GamaUIColor color) {
		popupColor = color;
	}

	@Override
	public void setToolTipText(final String string) {
		super.setToolTipText(string);
		thumb.setToolTipText(string);
		rightRegion.setToolTipText(string);
		leftRegion.setToolTipText(string);
	}

	/**
	 * @see ummisco.gama.ui.controls.IPopupProvider#getPopupText()
	 */
	@Override
	public PopupText getPopupText() {
		final double value = getCurrentPosition();
		final String text =
				toolTipInterperter == null ? String.valueOf(value) : toolTipInterperter.getToolTipText(value);
		return PopupText.with(popupColor, text);
	}

	@Override
	public Point getAbsoluteOrigin() {
		return leftRegion.toDisplay(new Point(leftRegion.getLocation().x, -sliderHeight));
	}

	@Override
	public Shell getControllingShell() {
		return leftRegion.getShell();
	}

	public void specifyHeight(final int heightsize) {
		sliderHeight = heightsize;
	}

	public void setStep(final Double realStep) {
		if (realStep != null && realStep > 0d) {
			step = realStep;
		}
	}

}
