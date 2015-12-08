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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.plugins;

import java.util.Collection;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.internal.core.util.MatchedConfigHousing;
import org.eclipse.tigerstripe.workbench.internal.core.util.OSGIRef;
import org.eclipse.tigerstripe.workbench.plugins.IPluggablePluginPropertyListener;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.osgi.framework.Version;

/**
 * Builds a section dynamically based on the metadata contained in the housing
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class OsgiPluggablePluginSection extends PluggablePluginSection implements IPluggablePluginPropertyListener {

	private Collection<PluggableHousing> housings;

	public Collection<PluggableHousing> getHousings() {
		return housings;
	}

	private IPluginConfig usedConfig;

	private Label availableVersionsLabel;
	protected Text minVersionText = null;
	protected Text maxVersionText = null;
	protected Combo minVersionCombo = null;
	protected Combo maxVersionCombo = null;

	public OsgiPluggablePluginSection(TigerstripeFormPage page, Composite parent, FormToolkit toolkit,
			Collection<PluggableHousing> housings) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);

		this.housings = housings;
		getHousing(true);
		createContent();
	}

	private String name = null;

	protected String getName() {
		if (name == null) {
			for (PluggableHousing candidateHousing : housings) {
				name = candidateHousing.getPluginName();
				// The name should be the same for all of them
				break;
			}
		}
		return name;
	}

	protected String getTitleString() {
		return getName();
	}

	/**
	 * This override is used to find the housing that best matches the
	 * "specification".
	 * 
	 * @param housing
	 */
	public PluggableHousing getHousing() {
		return getHousing(false);
	}

	public PluggableHousing getHousing(boolean refresh) {
		if (housing == null || refresh) {
			try {
				ITigerstripeModelProject handle = getTSProject();
				IPluginConfig[] plugins = handle.getPluginConfigs();
				MatchedConfigHousing m = PluginManager.getManager().resolve(getHousings(), plugins);
				setHousing(m.getHousing());
				usedConfig = m.getConfig();
			} catch (TigerstripeException t) {
				BasePlugin.logErrorMessage("An error occurred reading housing information", t);
			}
		}
		return housing;
	}

	@Override
	protected IPluginConfig getPluginConfig() {
		getHousing(true);
		return usedConfig;
	}

	protected void buildSpecifics(Composite parent, FormToolkit toolkit, DefaultPageListener listener) {
		Composite versionComposite = new Composite(parent, SWT.NULL);
		GridDataFactory.fillDefaults().span(3, SWT.DEFAULT).applyTo(versionComposite);

		GridLayout layout = new GridLayout(4, false);
		versionComposite.setLayout(layout);

		toolkit.createLabel(versionComposite, "Minimum Version");
		minVersionText = toolkit.createText(versionComposite, "");
		minVersionText.setEnabled(true);
		GridDataFactory.fillDefaults().hint(150, SWT.DEFAULT).applyTo(minVersionText);
		minVersionText.addModifyListener(listener);

		minVersionCombo = new Combo(versionComposite, SWT.READ_ONLY | SWT.BORDER);
		minVersionCombo.setEnabled(true);
		GridDataFactory.fillDefaults().applyTo(minVersionCombo);
		toolkit.adapt(minVersionCombo, true, true);
		minVersionCombo.setItems(OSGIRef.constraintNames);
		minVersionCombo.addSelectionListener(listener);

		availableVersionsLabel = toolkit.createLabel(versionComposite, "");

		toolkit.createLabel(versionComposite, "Maximum Version");

		maxVersionText = toolkit.createText(versionComposite, "");
		maxVersionText.setEnabled(true);
		GridDataFactory.fillDefaults().applyTo(maxVersionText);
		maxVersionText.addModifyListener(listener);

		maxVersionCombo = new Combo(versionComposite, SWT.READ_ONLY | SWT.BORDER);
		maxVersionCombo.setEnabled(true);
		GridDataFactory.fillDefaults().applyTo(maxVersionCombo);
		toolkit.adapt(maxVersionCombo, true, true);
		maxVersionCombo.setItems(OSGIRef.constraintNames);
		maxVersionCombo.addSelectionListener(listener);

		// Fill out the grid
		toolkit.createLabel(versionComposite, "");
		toolkit.paintBordersFor(versionComposite);

	}

	private String getAvailableVersions(boolean refresh) {
		String string = "Available Versions :";
		for (PluggableHousing h : housings) {

			string = "    " + string + "'" + h.getVersion();
			if (h.equals(getHousing(refresh))) {
				string = string + "(active)";
			}
			string = string + "'";
		}
		return string;
	}

	protected void initVersionInfo() throws TigerstripeException {
		setSilentUpdate(true);
		OSGIRef ref;
		try {
			if (getPluginConfig() != null) {
				ref = OSGIRef.parseRef(getPluginConfig().getVersion());
			} else {
				ref = OSGIRef.parseRef(getHousing().getVersion());
			}
			minVersionText.setText(ref.getMinVersion().toString());
			minVersionCombo.select(ref.getMinConstraint());
			if (ref.getMaxVersion() != null) {
				maxVersionText.setText(ref.getMaxVersion().toString());
			} else {
				maxVersionText.setText("");
			}
			maxVersionCombo.select(ref.getMaxConstraint());

			availableVersionsLabel.setText(getAvailableVersions(false));

		} catch (Exception e) {
			throw new TigerstripeException("An error occurred reading version info.", e);
		} finally {

			setSilentUpdate(false);
		}
	}

	public void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {

			OSGIRef ref;
			if (getPluginConfig() != null) {

				ref = OSGIRef.parseRef(getPluginConfig().getVersion());

				if (e.getSource() == minVersionText) {

					if (minVersionText.getText().trim().equals("")) {

						ref.setMinVersion(new Version("0.0.0"));

					} else {

						try {
							Version v = new Version(minVersionText.getText().trim());
							ref.setMinVersion(v);

						} catch (IllegalArgumentException iae) {

							// not a valid OSGi ref, but we want to allow
							// editing to continue.
							markPageModified();
							updateForm();
							return;
						}
					}
					if (!ref.toString().equals(((PluginConfig) getPluginConfig()).getVersion()))
						((PluginConfig) getPluginConfig()).setVersion(ref.toString());

				} else if (e.getSource() == maxVersionText) {

					if (maxVersionText.getText().trim().equals("")) {

						ref.setMaxVersion(null);
					} else {

						try {

							Version v = new Version(maxVersionText.getText().trim());
							ref.setMaxVersion(v);

						} catch (IllegalArgumentException iae) {

							// not a valid OSGi ref, but we want to allow
							// editing to continue.
							markPageModified();
							updateForm();
							return;
						}
					}
					if (!ref.toString().equals(((PluginConfig) getPluginConfig()).getVersion()))
						((PluginConfig) getPluginConfig()).setVersion(ref.toString());
				}
			}
			markPageModified();
		}
		updateForm();
	}

	@Override
	protected void handleWidgetSelected(SelectionEvent e) {
		if (!isSilentUpdate()) {
			OSGIRef ref;
			if (getPluginConfig() != null) {
				ref = OSGIRef.parseRef(getPluginConfig().getVersion());
			} else {
				ref = OSGIRef.parseRef(getHousing().getVersion());
			}
			if (e.getSource() == minVersionCombo) {
				ref.setMinConstraint(minVersionCombo.getSelectionIndex());
				markPageModified();
				if (!ref.toString().equals(((PluginConfig) getPluginConfig()).getVersion()))
					((PluginConfig) getPluginConfig()).setVersion(ref.toString());
			} else if (e.getSource() == maxVersionCombo) {
				ref.setMaxConstraint(maxVersionCombo.getSelectionIndex());
				markPageModified();
				if (!ref.toString().equals(((PluginConfig) getPluginConfig()).getVersion()))
					((PluginConfig) getPluginConfig()).setVersion(ref.toString());
			}
		}
		super.handleWidgetSelected(e);
	}

	@Override
	protected void createID(Composite parent, FormToolkit toolkit) throws TigerstripeException {
		super.createID(parent, toolkit);
		initVersionInfo();
	}

	@Override
	protected void updateForm() {
		boolean startState = isSilentUpdate();
		setSilentUpdate(true);

		boolean isEnabled = generate.getSelection();
		minVersionText.setEnabled(isEnabled);
		minVersionCombo.setEnabled(isEnabled);
		maxVersionText.setEnabled(isEnabled);
		maxVersionCombo.setEnabled(isEnabled);
		availableVersionsLabel.setText(getAvailableVersions(true));

		super.updateForm();
		setSilentUpdate(startState);
	}

}
