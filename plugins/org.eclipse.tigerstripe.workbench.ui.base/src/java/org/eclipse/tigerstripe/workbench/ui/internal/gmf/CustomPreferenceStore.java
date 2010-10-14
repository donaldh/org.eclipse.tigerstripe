/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.gmf;

import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

public class CustomPreferenceStore implements IPreferenceStore {

	private final Map<String, String> values;
	private final IPreferenceStore defaultDelegat;

	public CustomPreferenceStore(Map<String, String> values,
			IPreferenceStore defaultDelegat) {
		this.values = values;
		this.defaultDelegat = defaultDelegat;
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public boolean contains(String name) {
		return values.containsKey(name);
	}

	public void firePropertyChangeEvent(String name, Object oldValue,
			Object newValue) {
		throw new UnsupportedOperationException();
	}

	public boolean getBoolean(String name) {
		String value = values.get(name);
		if (value != null) {
			return Boolean.valueOf(value);
		} else {
			return getDefaultBoolean(name);
		}
	}

	public boolean getDefaultBoolean(String name) {
		return defaultDelegat.getBoolean(name);
	}

	public double getDefaultDouble(String name) {
		return defaultDelegat.getDouble(name);
	}

	public float getDefaultFloat(String name) {
		return defaultDelegat.getFloat(name);
	}

	public int getDefaultInt(String name) {
		return defaultDelegat.getInt(name);
	}

	public long getDefaultLong(String name) {
		return defaultDelegat.getLong(name);
	}

	public String getDefaultString(String name) {
		return defaultDelegat.getString(name);
	}

	public double getDouble(String name) {
		String value = values.get(name);
		if (value != null) {
			return Double.valueOf(value);
		} else {
			return getDefaultDouble(name);
		}
	}

	public float getFloat(String name) {
		String value = values.get(name);
		if (value != null) {
			return Float.valueOf(value);
		} else {
			return getDefaultFloat(name);
		}
	}

	public int getInt(String name) {
		String value = values.get(name);
		if (value != null) {
			return Integer.valueOf(value);
		} else {
			return getDefaultInt(name);
		}
	}

	public long getLong(String name) {
		String value = values.get(name);
		if (value != null) {
			return Long.valueOf(value);
		} else {
			return getDefaultLong(name);
		}
	}

	public String getString(String name) {
		String value = values.get(name);
		if (value != null) {
			return value;
		} else {
			return getDefaultString(name);
		}
	}

	public boolean isDefault(String name) {
		return getDefaultString(name).equals(values.equals(name));
	}

	public boolean needsSaving() {
		return false;
	}

	public void putValue(String name, String value) {
		values.put(name, value);
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void setDefault(String name, double value) {
		throw new UnsupportedOperationException();
	}

	public void setDefault(String name, float value) {
		throw new UnsupportedOperationException();
	}

	public void setDefault(String name, int value) {
		throw new UnsupportedOperationException();
	}

	public void setDefault(String name, long value) {
		throw new UnsupportedOperationException();
	}

	public void setDefault(String name, String defaultObject) {
		throw new UnsupportedOperationException();
	}

	public void setDefault(String name, boolean value) {
		throw new UnsupportedOperationException();
	}

	public void setToDefault(String name) {
		values.put(name, defaultDelegat.getDefaultString(name));
	}

	public void setValue(String name, double value) {
		values.put(name, Double.toString(value));
	}

	public void setValue(String name, float value) {
		values.put(name, Float.toString(value));
	}

	public void setValue(String name, int value) {
		values.put(name, Integer.toString(value));
	}

	public void setValue(String name, long value) {
		values.put(name, Long.toString(value));
	}

	public void setValue(String name, String value) {
		values.put(name, value);
	}

	public void setValue(String name, boolean value) {
		values.put(name, Boolean.toString(value));
	}

	public Map<String, String> getValues() {
		return values;
	}
}
