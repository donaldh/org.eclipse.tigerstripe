package org.eclipse.tigerstripe.workbench.sdk.internal.contents;

import java.util.Collection;

import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationTypeContribution.Target;

public class GeneratedPackageContribution implements IContribution {

	public GeneratedPackageContribution(IPluginModelBase contributor,
			String uri, String _class, String genModel,
			boolean readOnly,
			IPluginElement pluginElement) {
		super();
		this.contributor = contributor;
		this.uri = uri;
		this._class = _class;
		this.genModel = genModel;
		this.readOnly = readOnly;
		this.pluginElement = pluginElement;
	}
	private IPluginModelBase contributor;
	private String uri;
	private String _class;
	private String genModel;
	private IPluginElement pluginElement;
	private boolean readOnly;
	
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public IPluginModelBase getContributor() {
		return contributor;
	}
	public void setContributor(IPluginModelBase contributor) {
		this.contributor = contributor;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String get_class() {
		return _class;
	}
	public void set_class(String _class) {
		this._class = _class;
	}
	public String getGenModel() {
		return genModel;
	}
	public void setGenModel(String genModel) {
		this.genModel = genModel;
	}
	public IPluginElement getPluginElement() {
		return pluginElement;
	}
	public void setPluginElement(IPluginElement pluginElement) {
		this.pluginElement = pluginElement;
	}
	
	
	
	
}
