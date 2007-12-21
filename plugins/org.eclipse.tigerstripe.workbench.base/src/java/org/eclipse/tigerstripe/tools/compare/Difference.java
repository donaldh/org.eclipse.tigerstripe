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
package org.eclipse.tigerstripe.tools.compare;

public class Difference {

	private String type;

	private String local;

	private String remote;

	private String scope;

	private String object;

	private String localVal;

	private String remoteVal;

	public Difference(String type, String local, String remote, String scope,
			String object, String localVal, String remoteVal) {
		this.setType(type);
		this.setLocal(local);
		this.setRemote(remote);
		this.setScope(scope);
		this.setObject(object);
		this.setLocalVal(localVal);
		this.setRemoteVal(remoteVal);

	}

	public String getScope() {
		return this.scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getLocal() {
		return this.local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getObject() {
		return this.object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getRemote() {
		return this.remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setLocalVal(String localVal) {
		this.localVal = localVal;
	}

	public void setRemoteVal(String remoteVal) {
		this.remoteVal = remoteVal;
	}

	public String getLocalVal() {
		return this.localVal;
	}

	public String getRemoteVal() {
		return this.remoteVal;
	}

	@Override
	public String toString() {
		return " Type = " + this.getType() + " Scope = " + this.getScope()
				+ " Local art = " + this.getLocal() + " Remote art = "
				+ this.getRemote() + " Object = " + this.getObject()
				+ " LocalVal = " + this.getLocalVal() + " RemoteVal = "
				+ this.getRemoteVal();
	}

	public String getShortString() {
		if (!this.getLocal().equals(""))
			return this.getScope() + " " + this.getLocal() + " "
					+ this.getObject() + " {" + this.getLocalVal() + ","
					+ this.getRemoteVal() + "}";
		else
			return this.getScope() + " " + this.getRemote() + " "
					+ this.getObject() + " {" + this.getLocalVal() + ","
					+ this.getRemoteVal() + "}";
	}
}
