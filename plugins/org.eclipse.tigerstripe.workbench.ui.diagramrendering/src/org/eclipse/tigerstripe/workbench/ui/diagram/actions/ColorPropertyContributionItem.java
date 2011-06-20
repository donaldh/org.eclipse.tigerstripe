/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    xored software, Inc. - move to java 1.5
 ****************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.diagram.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.ui.util.WindowUtil;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.tigerstripe.workbench.ui.Images;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author melaasar
 * 
 */
public class ColorPropertyContributionItem extends
		PropertyChangeContributionItem
		implements Listener {

	private static class ColorMenuImageDescriptor extends
			CompositeImageDescriptor {
		private final ImageData basicImgData;
		private final RGB rgb;

		public ColorMenuImageDescriptor(ImageData basicImgData, RGB rgb) {
			this.basicImgData = basicImgData;
			this.rgb = rgb;
		}

		@Override
		protected void drawCompositeImage(int width, int height) {
			drawImage(basicImgData, 0, 0);

			// draw the thin color bar underneath
			if (rgb != null) {
				ImageData colorBar = new ImageData(14, 4, 1, new PaletteData(
						new RGB[] { rgb }));
				drawImage(colorBar, 1, height - 4);
			}
		}

		@Override
		protected Point getSize() {
			return ICON_SIZE;
		}
	}

	private static class ColorBoxImageDescriptor extends ImageDescriptor {
		private final RGB rgb;

		public ColorBoxImageDescriptor(RGB rgb) {
			this.rgb = rgb;
		}

		@Override
		public ImageData getImageData() {
			ImageData data = new ImageData(ICON_SIZE.x, ICON_SIZE.y, 1,
					new PaletteData(new RGB[] { rgb, OUTLINE_COLOR }));

			for (int i = 0; i < ICON_SIZE.y; i++)
				data.setPixel(0, i, 1);
			for (int i = 0; i < ICON_SIZE.y; i++)
				data.setPixel(ICON_SIZE.x - 1, i, 1);
			for (int i = 0; i < ICON_SIZE.x; i++)
				data.setPixel(i, 0, 1);
			for (int i = 0; i < ICON_SIZE.x; i++)
				data.setPixel(i, ICON_SIZE.y - 1, 1);
			return data;
		}
	}

	private static class InventoryColorDescriptor {
		public RGB colorValue;
		public String colorName;

		public InventoryColorDescriptor(RGB colorValue, String colorName) {
			this.colorValue = colorValue;
			this.colorName = colorName;
		}
	}

	private static final InventoryColorDescriptor TIGERSTRIPE_YELLOW = new InventoryColorDescriptor(
			new RGB(250, 245, 210), "Tigerstripe Yellow");
	private static final InventoryColorDescriptor RED = new InventoryColorDescriptor(
			new RGB(255, 223, 223), "Red");
	private static final InventoryColorDescriptor GREEN = new InventoryColorDescriptor(
			new RGB(223, 255, 223), "Green");
	private static final InventoryColorDescriptor BLUE = new InventoryColorDescriptor(
			new RGB(223, 239, 255), "Blue");
	private static final InventoryColorDescriptor YELLOW = new InventoryColorDescriptor(
			new RGB(255, 255, 223), "Yellow");
	private static final InventoryColorDescriptor PINK = new InventoryColorDescriptor(
			new RGB(255, 223, 255), "Pink");
	private static final InventoryColorDescriptor CYAN = new InventoryColorDescriptor(
			new RGB(223, 255, 255), "Cyan");
	private static final InventoryColorDescriptor PURPLE = new InventoryColorDescriptor(
			new RGB(239, 223, 255), "Purple");
	private static final InventoryColorDescriptor BROWN = new InventoryColorDescriptor(
			new RGB(239, 223, 223), "Brown");
	private static final InventoryColorDescriptor ORANGE = new InventoryColorDescriptor(
			new RGB(255, 239, 223), "Orange");
			
	
	private static final Point ICON_SIZE = new Point(16, 16);
	private static final int CUSTOM_SIZE = 3;
	private static final RGB DEFAULT_PREF_COLOR = new RGB(0, 0, 0);
	private static final RGB OUTLINE_COLOR = new RGB(192, 192, 192);
	private static final String DEFAULT = "Default"; //$NON-NLS-1$
	private static final String CHOOSE = "Choose"; //$NON-NLS-1$
	private static final String CLEAR = "Clear"; //$NON-NLS-1$

	private final ImageData basicImageData;
	private final ImageData disabledBasicImageData;
	private Image disabledBasicImage;
	private Image overlyedImage;
	private Integer lastColor;
	private final List<RGB> customColors = new ArrayList<RGB>();
	private final List<RGB> inventoryColors = new ArrayList<RGB>();
	private Map<RGB, Image> imageColorMap = new HashMap<RGB, Image>();
	private Menu menu;

	public ColorPropertyContributionItem(IWorkbenchPage workbenchPage,
			String id, String propertyId, String propertyName,
			String toolTipText, ImageData basicImageData,
			ImageData disabledBasicImageData) {
		super(workbenchPage, id, propertyId, propertyName);
		assert null != toolTipText;
		assert null != basicImageData;
		this.basicImageData = basicImageData;
		this.disabledBasicImageData = disabledBasicImageData;
		setLabel(toolTipText);
	}

	@Override
	protected void init() {
		super.init();
		this.overlyedImage = new ColorMenuImageDescriptor(getBasicImageData(),
				null).createImage();
		this.disabledBasicImage = new ColorMenuImageDescriptor(
				this.disabledBasicImageData, null).createImage();
	}

	/**
	 * @see org.eclipse.jface.action.IContributionItem#dispose()
	 */
	@Override
	public void dispose() {
		if (overlyedImage != null && !overlyedImage.isDisposed()) {
			overlyedImage.dispose();
			overlyedImage = null;
		}
		if (menu != null && !menu.isDisposed()) {
			menu.dispose();
			menu = null;
		}
		for (Image image : imageColorMap.values()) {
			if (!image.isDisposed()) {
				image.dispose();
			}
		}
		if (disabledBasicImage != null && !disabledBasicImage.isDisposed()) {
			disabledBasicImage.dispose();
			disabledBasicImage = null;
		}
		imageColorMap = new HashMap<RGB, Image>();

		super.dispose();
	}

	/**
	 * Render the UI as a
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#createToolItem(org.eclipse.swt.widgets.ToolBar,
	 *      int)
	 */
	@Override
	protected ToolItem createToolItem(ToolBar parent, int index) {
		ToolItem ti = new ToolItem(parent, SWT.DROP_DOWN, index);
		ti.addListener(SWT.Selection, getItemListener());
		ti.setImage(overlyedImage);
		ti.setDisabledImage(this.disabledBasicImage);
		return ti;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#createMenuItem(org.eclipse.swt.widgets.Menu,
	 *      int)
	 */
	@Override
	protected MenuItem createMenuItem(Menu parent, int index) {
		MenuItem mi = index >= 0 ? new MenuItem(parent, SWT.CASCADE, index)
				: new MenuItem(parent, SWT.CASCADE);
		createMenu(mi);
		mi.setImage(overlyedImage);
		return mi;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#handleWidgetEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	protected void handleWidgetEvent(Event e) {
		switch (e.type) {
		case SWT.Selection:
			handleWidgetSelection(e);
			break;
		default:
			super.handleWidgetEvent(e);
		}
	}

	/**
	 * Handles a widget selection event.
	 */
	private void handleWidgetSelection(Event e) {
		if (e.detail == 4) { // on drop-down button
			createMenu(getItem());
		} else { // on icon button
			if (lastColor != null)
				runWithEvent(e);
		}
	}

	/**
	 * Creates the color menu
	 */
	private void createMenu(Item item) {
		if (menu != null && !menu.isDisposed())
			menu.dispose();

		if (item instanceof ToolItem) {
			ToolItem toolItem = (ToolItem) item;
			menu = new Menu(toolItem.getParent());
			Rectangle b = toolItem.getBounds();
			Point p = toolItem.getParent().toDisplay(
					new Point(b.x, b.y + b.height));
			menu.setLocation(p.x, p.y); // waiting for SWT 0.42
			menu.setVisible(true);
		} else if (item instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) item;
			menu = new Menu(menuItem.getParent());
			menuItem.setMenu(menu);
		}

		assert null != menu : "falid to create menu"; //$NON-NLS-1$
		buildMenu(menu);
	}

	@Override
	protected Object getNewPropertyValue() {
		return lastColor;
	}

	private void buildMenu(Menu theMenu) {
		createInventoryColorMenuItem(theMenu, TIGERSTRIPE_YELLOW);
		createInventoryColorMenuItem(theMenu, RED);
		createInventoryColorMenuItem(theMenu, GREEN);
		createInventoryColorMenuItem(theMenu, BLUE);
		createInventoryColorMenuItem(theMenu, YELLOW);
		createInventoryColorMenuItem(theMenu, PINK);
		createInventoryColorMenuItem(theMenu, CYAN);
		createInventoryColorMenuItem(theMenu, PURPLE);
		createInventoryColorMenuItem(theMenu, BROWN);
		createInventoryColorMenuItem(theMenu, ORANGE);

		// history colors
		if (!customColors.isEmpty()) {
			createMenuSeparator(theMenu);
			Iterator<RGB> iter = customColors.iterator();
			while (iter.hasNext()) {
				RGB rgb = iter.next();
				createColorMenuItem(theMenu, rgb);
			}
			createClearCustomColorMenuItem(theMenu);
		}

		// default color and color picker
		createMenuSeparator(theMenu);
		createDefaultColorMenuItem(theMenu);
		createChooseColorMenuItem(theMenu);
	}

	private void createMenuSeparator(Menu theMenu) {
		new MenuItem(theMenu, SWT.SEPARATOR);
	}

	private void createInventoryColorMenuItem(Menu theMenu,
			InventoryColorDescriptor color) {

		RGB rgb = color.colorValue;
		Image image = (Image) imageColorMap.get(rgb);
		if (image == null) {
			image = new ColorBoxImageDescriptor(color.colorValue).createImage();
			imageColorMap.put(rgb, image);
		}
		MenuItem mi = createMenuItem(theMenu, color.colorName, image);
		mi.setData(rgb);
		inventoryColors.add(rgb);
	}

	private void createColorMenuItem(Menu theMenu, RGB rgb) {
		Image image = (Image) imageColorMap.get(rgb);
		if (image == null) {
			image = new ColorBoxImageDescriptor(rgb).createImage();
			imageColorMap.put(rgb, image);
		}
		MenuItem mi = createMenuItem(theMenu, rgb.toString(), image);
		mi.setData(rgb);
	}

	private void createDefaultColorMenuItem(Menu theMenu) {
		MenuItem mi = createMenuItem(theMenu, "Default Color", null);
		mi.setData(DEFAULT);
	}

	private void createChooseColorMenuItem(Menu theMenu) {
		MenuItem mi = createMenuItem(theMenu, "More Colors ...", null);
		mi.setData(CHOOSE);
	}

	private void createClearCustomColorMenuItem(Menu theMenu) {
		MenuItem mi = createMenuItem(theMenu, "Clear Custom Colors", null);
		mi.setData(CLEAR);
	}

	private MenuItem createMenuItem(Menu theMenu, String text, Image image) {
		MenuItem mi = new MenuItem(theMenu, SWT.PUSH);
		if (text != null)
			mi.setText(text);
		if (image != null)
			mi.setImage(image);
		mi.addListener(SWT.Selection, this);
		return mi;
	}

	public void handleEvent(Event event) {
		MenuItem menuItem = (MenuItem) event.widget;
		Object data = menuItem.getData();

		RGB rgb = null;

		if (data instanceof RGB) {
			rgb = (RGB) data;
		} else if (data.equals(CHOOSE)) {
			rgb = getBrowseColor();
		} else if (data.equals(DEFAULT)) {
			rgb = getDefaultColor();
		} else if (data.equals(CLEAR)) {
			customColors.clear();
		}

		if (rgb != null) {
			if (getToolItem() != null) {
				// if a new custom color add it to history
				if (!customColors.contains(rgb)
						&& !inventoryColors.contains(rgb)) {
					if (customColors.size() == CUSTOM_SIZE)
						customColors.remove(0);
					customColors.add(rgb);
				}

				// create a new overlayed icon with the new color
				if (overlyedImage != null)
					overlyedImage.dispose();
				overlyedImage = new ColorMenuImageDescriptor(
						getBasicImageData(), rgb).createImage();
				getItem().setImage(overlyedImage);
			}

			// set the new color as the last color
			lastColor = FigureUtilities.RGBToInteger(rgb);

			// run the action
			runWithEvent(event);
		}
	}

	protected RGB getBrowseColor() {
		ColorDialog dialog = new ColorDialog(Display.getCurrent()
				.getActiveShell());
		WindowUtil.centerDialog(dialog.getParent(), Display.getCurrent()
				.getActiveShell());
		if (lastColor != null) {
			dialog.setRGB(FigureUtilities.integerToRGB(lastColor));
		}
		dialog.open();
		return dialog.getRGB();
	}

	/**
	 * Returns the color to use in the default mode. A limitation is that if
	 * there are multiple edit parts with different default colors only the
	 * default color of the first is returned.
	 * 
	 * @return The color to use in default mode
	 */
	protected RGB getDefaultColor() {
		for (Iterator<?> iter = getOperationSet().iterator(); iter.hasNext();) {
			EditPart editpart = (EditPart) iter.next();
			if (editpart instanceof IGraphicalEditPart) {
				final EStructuralFeature feature = (EStructuralFeature) PackageUtil
						.getElement(getPropertyId());

				Object preferredValue = ((IGraphicalEditPart) editpart)
						.getPreferredValue(feature);
				if (preferredValue instanceof Integer) {
					return FigureUtilities
							.integerToRGB((Integer) preferredValue);
				}

			}

		}

		return DEFAULT_PREF_COLOR;
	}

	protected ImageData getBasicImageData() {
		return this.basicImageData;
	}

	public static ColorPropertyContributionItem createFillColorContributionItem(
			IWorkbenchPage workbenchPage) {
		return new ColorPropertyContributionItem(workbenchPage,
				ActionIds.CUSTOM_FILL_COLOR,
				PackageUtil.getID(NotationPackage.eINSTANCE
						.getFillStyle_FillColor()), "Fill Color",
				"Fill &Color", Images.getImage(Images.FILL_COLOR_ENABLED)
						.getImageData(), Images.getImage(
						Images.FILL_COLOR_DISABLED).getImageData());
	}

	@Override
	protected boolean digIntoGroups() {
		return true;
	}
}
