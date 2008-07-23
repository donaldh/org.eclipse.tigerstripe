/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode.IMapModeHolder;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.ui.internal.viewers.TigerstripeDecoratorManager;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.DiagramPropertiesHelper;

/**
 * @generated NOT
 */
public abstract class StereotypeNamePackageEditPart extends
		AbstractLabelEditPart implements ITextAwareEditPart,
		NamePackageInterface {

	protected WrappingLabel figure;

	private static final String _ellipse = "..."; //$NON-NLS-1$

	private static final Dimension EMPTY_DIMENSION = new Dimension(0, 0);

	private static final Map mapModeConstantsMap = new WeakHashMap();

	private MapModeConstants mapModeConstants;

	public StereotypeNamePackageEditPart(View view) {
		super(view);
	}

	public int getAvgCharWidth() {
		if (figure == null)
			throw new IllegalArgumentException("Figure is not initialized yet");
		Font f = figure.getFont();
		FontMetrics metrics = FigureUtilities.getFontMetrics(f);
		IMapMode mm = getMapMode();
		int fontHeight = mm.DPtoLP(metrics.getHeight());
		return mm.DPtoLP(metrics.getAverageCharWidth());
	}

	protected int getRenderedLength(String s, Font f) {
		if (s.length() == 0)
			return 0;
		else {
			Dimension d = FigureUtilities.getTextExtents(s, f);
			IMapMode mapMode = getMapMode();
			d.width = mapMode.DPtoLP(d.width);
			return d.width;
		}
	}

	protected Dimension getTextExtents(String s, Font f, int fontHeight) {
		if (s.length() == 0)
			return getMapModeConstants().dimension_nDPtoLP_0;
		else {
			// height should be set using the font height and the number of
			// lines in the string
			Dimension d = FigureUtilities.getTextExtents(s, f);
			IMapMode mapMode = getMapMode();
			d.width = mapMode.DPtoLP(d.width);
			d.height = fontHeight * new StringTokenizer(s, "\n").countTokens();//$NON-NLS-1$
			return d;
		}
	}

	private MapModeConstants getMapModeConstants() {
		if (mapModeConstants == null) {
			IMapMode mapMode = MapModeUtil.getMapMode(figure);
			while (mapMode instanceof IMapModeHolder) {
				mapMode = ((IMapModeHolder) mapMode).getMapMode();
			}
			mapModeConstants = (MapModeConstants) mapModeConstantsMap
					.get(mapMode);
			if (mapModeConstants == null) {
				mapModeConstants = new MapModeConstants(mapMode);
				mapModeConstantsMap.put(mapMode, mapModeConstants);
			}
		}
		return mapModeConstants;
	}

	private static class MapModeConstants {

		private static final int MAX_IMAGE_INFO = 12;

		public final WeakReference mapModeRef;

		public final int nDPtoLP_3;

		public final int nDPtoLP_2;

		public final int nDPtoLP_0;

		public final Dimension dimension_nDPtoLP_0;

		public final WeakHashMap fontToEllipseTextSize = new WeakHashMap();

		public final SingleIconInfo[] singleIconInfos = new SingleIconInfo[MAX_IMAGE_INFO];

		public MapModeConstants(IMapMode mapMode) {
			this.mapModeRef = new WeakReference(mapMode);
			nDPtoLP_2 = mapMode.DPtoLP(2);
			nDPtoLP_3 = mapMode.DPtoLP(3);
			nDPtoLP_0 = mapMode.DPtoLP(0);
			dimension_nDPtoLP_0 = new Dimension(nDPtoLP_0, nDPtoLP_0);
		}

		public Dimension getEllipseTextSize(Font f) {
			Dimension d = (Dimension) fontToEllipseTextSize.get(f);
			if (d == null) {
				IMapMode mapMode = (IMapMode) mapModeRef.get();
				d = FigureUtilities.getTextExtents(_ellipse, f);
				d.height = FigureUtilities.getFontMetrics(f).getHeight();
				d = new Dimension(mapMode.DPtoLP(d.width), mapMode
						.DPtoLP(d.height));
				fontToEllipseTextSize.put(f, d);
			}
			return d;
		}

		public SingleIconInfo getSingleIconInfo(Image image) {
			if (image == null)
				return SingleIconInfo.NULL_INFO;
			SingleIconInfo info;
			for (int i = 0; i < MAX_IMAGE_INFO; ++i) {
				info = singleIconInfos[i];
				if (info == null) {
					info = new SingleIconInfo(image);
					singleIconInfos[i] = info;
					return info;
				}
				if (info.icon == image)
					return info;
			}
			int index = SingleIconInfo.count % MAX_IMAGE_INFO;
			info = new SingleIconInfo(image);
			singleIconInfos[index] = info;
			return info;
		}
	}

	private static abstract class IconInfo {
		/**
		 * Gets the icon at the index location.
		 * 
		 * @param i
		 *            the index to retrieve the icon of
		 * @return <code>Image</code> that corresponds to the given index.
		 */
		public abstract Image getIcon(int i);

		/**
		 * Gets the icon size of the icon at the given index.
		 * 
		 * @param i
		 * @return the <code>Dimension</code> that is the size of the icon at
		 *         the given index.
		 */
		public abstract Dimension getIconSize(IMapMode mapMode, int i);

		/**
		 * @return the number of icons
		 */
		public abstract int getNumberofIcons();

		/**
		 * @return the <code>Dimension</code> that is the total size of all the
		 *         icons.
		 */
		public abstract Dimension getTotalIconSize(IMapMode mapMode);

		public abstract void invalidate();

		/**
		 * Sets the icon at the index location.
		 * 
		 * @param icon
		 * @param i
		 */
		public abstract void setIcon(Image icon, int i);

		/**
		 * 
		 */
		public abstract int getMaxIcons();

	}

	private static class SingleIconInfo extends IconInfo {

		static int count;

		public static final SingleIconInfo NULL_INFO = new SingleIconInfo() {
			@Override
			public int getNumberofIcons() {
				return 0;
			}
		};

		final Image icon;

		/** total icon size */
		private Dimension totalIconSize;

		private SingleIconInfo() {
			icon = null;// don't increment count, used only for NULL_INFO
		}

		public SingleIconInfo(Image icon) {
			this.icon = icon;
			++count;
		}

		@Override
		public final int getMaxIcons() {
			return 1;
		}

		@Override
		public Image getIcon(int i) {
			if (i == 0)
				return icon;
			else if (i > 0)
				return null;
			throw new IndexOutOfBoundsException();
		}

		@Override
		public void setIcon(Image img, int i) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Dimension getIconSize(IMapMode mapMode, int i) {
			if (i == 0)
				return getTotalIconSize(mapMode);

			throw new IndexOutOfBoundsException();
		}

		@Override
		public int getNumberofIcons() {
			return 1;
		}

		@Override
		public Dimension getTotalIconSize(IMapMode mapMode) {
			if (totalIconSize != null)
				return totalIconSize;

			if (icon != null && !icon.isDisposed()) {
				org.eclipse.swt.graphics.Rectangle imgBounds = icon.getBounds();
				totalIconSize = new Dimension(mapMode.DPtoLP(imgBounds.width),
						mapMode.DPtoLP(imgBounds.height));
			} else {
				totalIconSize = EMPTY_DIMENSION;
			}

			return totalIconSize;
		}

		@Override
		public void invalidate() {
			totalIconSize = null;
		}

	}

	protected boolean hideArtifactPackages(
			org.eclipse.tigerstripe.workbench.ui.visualeditor.Map map) {
		if (map != null) {
			DiagramPropertiesHelper helper = new DiagramPropertiesHelper(map);
			return Boolean
					.parseBoolean(helper
							.getPropertyValue(DiagramPropertiesHelper.HIDEARTIFACTPACKAGES));
		}
		return false;
	}

	protected String decorateText(String text) {
		return TigerstripeDecoratorManager.getDefault().decorateText(
				text,
				(IModelComponent) this.getParent().getAdapter(
						IModelComponent.class));
	}

}
