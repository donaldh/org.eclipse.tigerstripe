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
package org.eclipse.tigerstripe.api.external.plugins;

import java.util.List;

import org.eclipse.tigerstripe.api.plugins.pluggable.ITablePPluginProperty;

public interface IextTablePPluginProperty extends IextPluggablePluginProperty {

	public class TablePropertyRow {

		private ITablePPluginProperty parentProperty;

		private String[] values;

		public TablePropertyRow(ITablePPluginProperty parentProperty) {
			this.parentProperty = parentProperty;
			values = new String[parentProperty.getColumnDefs().size()];
			applyDefault();
		}

		public void setValue(String columnName, String value) {
			int index = indexOf(columnName);
			if (index != -1)
				values[index] = value;
		}

		public String getValue(String columnName) {
			int index = indexOf(columnName);
			if (index == -1)
				return null;
			return values[index];
		}

		public String[] getValues() {
			return values;
		}

		public String[] getColumnNames() {
			String[] result = new String[parentProperty.getColumnDefs().size()];
			for (int i = 0; i < parentProperty.getColumnDefs().size(); i++) {
				result[i] = parentProperty.getColumnDefs().get(i).columnName;
			}
			return result;
		}

		public int indexOf(String columnName) {
			for (int index = 0; index < parentProperty.getColumnDefs().size(); index++) {
				if (parentProperty.getColumnDefs().get(index) != null
						&& parentProperty.getColumnDefs().get(index).columnName
								.equals(columnName))
					return index;
			}
			return -1;
		}

		private void applyDefault() {
			for (int i = 0; i < values.length; i++) {
				values[i] = parentProperty.getColumnDefs().get(i).defaultColumnValue;
			}
		}
	}

	public class ColumnDef {
		public String columnName;

		public String defaultColumnValue = "default";

		@Override
		public boolean equals(Object other) {
			if (other instanceof ColumnDef) {
				ColumnDef ent = (ColumnDef) other;
				return columnName.equals(ent.columnName);
			}
			return false;
		}
	}

	public List<ColumnDef> getColumnDefs();

	public TablePropertyRow makeRow();

}
