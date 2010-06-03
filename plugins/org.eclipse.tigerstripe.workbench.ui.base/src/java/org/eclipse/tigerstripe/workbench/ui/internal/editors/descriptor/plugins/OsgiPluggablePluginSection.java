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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IGlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.GeneratorProjectDescriptor;
import org.eclipse.tigerstripe.workbench.internal.core.util.MatchedConfigHousing;
import org.eclipse.tigerstripe.workbench.internal.core.util.OSGIRef;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.plugins.IPluggablePluginPropertyListener;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.FacetSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.DescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.TigerstripeDescriptorSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.plugins.PluggablePluginSection.DefaultPageListener;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.plugins.renderer.BasePropertyRenderer;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.plugins.renderer.PropertyRendererFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLog;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.osgi.framework.Version;

/**
 * Builds a section dynamically based on the metadata contained in the housing
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class OsgiPluggablePluginSection extends PluggablePluginSection
		implements IPluggablePluginPropertyListener {


	private Collection<PluggableHousing> housings;
	public Collection<PluggableHousing> getHousings() {
		return housings;
	}


	private IPluginConfig usedConfig;
	
	private OSGIRef osgiRef;

	private Label availableVersionsLabel;
	protected Text minVersionText = null;
	protected Text maxVersionText = null;
	protected CCombo minVersionCombo = null; 
	protected CCombo maxVersionCombo = null;

	public OsgiPluggablePluginSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit, Collection<PluggableHousing> housings) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR
				| ExpandableComposite.TWISTIE);

		this.housings = housings;
		getHousing(true);
		createContent();
	}

	
	private String name = null;
	protected String getName(){
		if (name == null){
			for (PluggableHousing candidateHousing : housings){
				name = candidateHousing.getPluginName();
				// The name should be the same for all of them
				break;
			}
		}
		return name;
	}
	
	protected String getTitleString(){
		return getName();
	}
	
	/**
	 * This override is used to find the housing that best matches the "specification".
	 * @param housing
	 */
	public PluggableHousing getHousing() {
		return getHousing(false);
	}
	public PluggableHousing getHousing(boolean refresh) {
		if (housing == null || refresh ){
			try {
				ITigerstripeModelProject handle = getTSProject();
				IPluginConfig[] plugins = handle.getPluginConfigs();
				
				MatchedConfigHousing m = PluginManager.getManager().resolve(getHousings(),plugins);
				setHousing(m.getHousing());
				usedConfig = m.getConfig();
				
			} catch (TigerstripeException t){

			}
		}
		return housing;
	}			

	

	@Override
	protected IPluginConfig getPluginConfig() {
		getHousing(true);
		return usedConfig;
	}

	protected void buildSpecifics(Composite parent, FormToolkit toolkit,DefaultPageListener listener){
		Composite versionComposite = new Composite(parent,SWT.NULL);
		GridData grid = new GridData();
		grid.horizontalSpan = 3;
		versionComposite.setLayoutData(grid);
		
		GridLayout layout = new GridLayout(6, false);
		versionComposite.setLayout(layout);
		
		toolkit.createLabel(versionComposite, "Minimum Version");
		minVersionText = toolkit.createText(versionComposite, "");
		minVersionText.setEnabled(true);
		GridData gd = new GridData(SWT.LEFT);
		minVersionText.setLayoutData(gd);
		minVersionText.addModifyListener(listener);

		minVersionCombo =  new CCombo(versionComposite, SWT.READ_ONLY | SWT.BORDER);
		minVersionCombo.setEnabled(true);
		minVersionCombo.setLayoutData(gd);
		toolkit.adapt(minVersionCombo, true, true);
		minVersionCombo.setItems(OSGIRef.constraintNames);
		minVersionCombo.addSelectionListener(listener);

		// Fill out the grid
		toolkit.createLabel(versionComposite, "");
		availableVersionsLabel = toolkit.createLabel(versionComposite, "");
		toolkit.createLabel(versionComposite, "");
		
		toolkit.createLabel(versionComposite, "Maximum Version");

		maxVersionText = toolkit.createText(versionComposite, "");
		maxVersionText.setEnabled(true);
		gd = new GridData(SWT.LEFT);
		maxVersionText.setLayoutData(gd);
		maxVersionText.addModifyListener(listener);

		maxVersionCombo =  new CCombo(versionComposite, SWT.READ_ONLY | SWT.BORDER);
		maxVersionCombo.setEnabled(true);
		maxVersionCombo.setLayoutData(gd);
		toolkit.adapt(maxVersionCombo, true, true);
		maxVersionCombo.setItems(OSGIRef.constraintNames);
		maxVersionCombo.addSelectionListener(listener);

		// Fill out the grid
		toolkit.createLabel(versionComposite, "");
		toolkit.createLabel(versionComposite, "");
		toolkit.createLabel(versionComposite, "");
		toolkit.paintBordersFor(versionComposite);
	
	}
	
	private String getAvailableVersions(boolean refresh){
		String string = "Available Versions :";
		for (PluggableHousing h : housings) {
			
			string = "    "+string+"'"+h.getVersion();
			if (h.equals(getHousing(refresh))){
				string = string + "(active)";
			}
			string = string+ "'";
		}
		return string;
	}
	
	protected void initVersionInfo(){
		setSilentUpdate(true);
		OSGIRef ref;
		if (getPluginConfig() != null){
			ref = OSGIRef.parseRef(getPluginConfig().getVersion());
		} else {
			ref = OSGIRef.parseRef(getHousing().getVersion());
		}
		minVersionText.setText(ref.getMinVersion().toString());
		minVersionCombo.select(ref.getMinConstraint());
		if (ref.getMaxVersion() != null){
			maxVersionText.setText(ref.getMaxVersion().toString());
		} else {
			maxVersionText.setText("");
		}
		maxVersionCombo.select(ref.getMaxConstraint());
		
		
		availableVersionsLabel.setText(getAvailableVersions(false));
		setSilentUpdate(false);
	}

	public void handleModifyText(ModifyEvent e) {
		if (!isSilentUpdate()) {
			OSGIRef ref;
			if (getPluginConfig() != null){
				ref = OSGIRef.parseRef(getPluginConfig().getVersion());


				if (e.getSource() == minVersionText){
					try {
						Version v = new Version(minVersionText.getText().trim());
						ref.setMinVersion(v);
						if (!ref.toString().equals(((PluginConfig) getPluginConfig()).getVersion()))
							((PluginConfig) getPluginConfig()).setVersion(ref.toString());
					} catch (IllegalArgumentException iae){
						// TODO Don't set this for now?
					}
				} else if (e.getSource() == maxVersionText){
					try {
						Version v = new Version(maxVersionText.getText().trim());
						ref.setMaxVersion(v);
						if (!ref.toString().equals(((PluginConfig) getPluginConfig()).getVersion()))
							((PluginConfig) getPluginConfig()).setVersion(ref.toString());
					} catch (IllegalArgumentException iae){
						// TODO Don't set this for now?
					}
				}
			} 
			markPageModified();
		}
		updateForm();
	}

	@Override
	protected void handleWidgetSelected(SelectionEvent e) {
		if (! isSilentUpdate()){
			OSGIRef ref;
			if (getPluginConfig() != null){
				ref = OSGIRef.parseRef(getPluginConfig().getVersion());
			} else {
				ref = OSGIRef.parseRef(getHousing().getVersion());
			}
			if (e.getSource() == minVersionCombo){
				ref.setMinConstraint(minVersionCombo.getSelectionIndex());
				markPageModified();
				if (!ref.toString().equals(((PluginConfig) getPluginConfig()).getVersion()))
					((PluginConfig) getPluginConfig()).setVersion(ref.toString());
			} else if (e.getSource() == maxVersionCombo){
				ref.setMaxConstraint(maxVersionCombo.getSelectionIndex());
				markPageModified();
				if (!ref.toString().equals(((PluginConfig) getPluginConfig()).getVersion()))
					((PluginConfig) getPluginConfig()).setVersion(ref.toString());
			}
		}
		super.handleWidgetSelected(e);
	}


	
	
	@Override
	protected void createID(Composite parent, FormToolkit toolkit)
			throws TigerstripeException {
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
