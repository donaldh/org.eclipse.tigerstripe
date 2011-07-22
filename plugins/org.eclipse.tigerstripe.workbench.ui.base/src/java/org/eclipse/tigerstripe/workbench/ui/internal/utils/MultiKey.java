package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import java.util.Arrays;

public class MultiKey {

	private final Object[] values;

	private final int hashCode;

	public MultiKey(Object... values) {
		this.values = values;
		hashCode = 31 * Arrays.hashCode(this.values);
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MultiKey other = (MultiKey) obj;

		return Arrays.equals(values, other.values);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("MultiKey[");

		if (values.length != 0) {
			int i = 0;
			builder.append(values[i]);
			while (++i < values.length) {
				builder.append(", ").append(values[i]);
			}
		}
		builder.append("]");
		return builder.toString();
	}
}
