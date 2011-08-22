package org.eclipse.tigerstripe.workbench.internal.core.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.tigerstripe.workbench.internal.BasePlugin;

import com.thoughtworks.qdox.model.Type;

public class QDoxUtils {

	public static final Field TYPE_NAME_FIELD;
	public static final Set<String> ELIGABLE_SIMPLE_NAMES = new HashSet<String>();

	static {
		Field typeNameField;
		try {
			typeNameField = com.thoughtworks.qdox.model.Type.class
					.getDeclaredField("name");
		} catch (Exception e) {
			BasePlugin.log(e);
			typeNameField = null;
		}
		typeNameField.setAccessible(true);

		TYPE_NAME_FIELD = typeNameField;

		ELIGABLE_SIMPLE_NAMES.add(Boolean.TYPE.getName());
		ELIGABLE_SIMPLE_NAMES.add(Byte.TYPE.getName());
		ELIGABLE_SIMPLE_NAMES.add(Character.TYPE.getName());
		ELIGABLE_SIMPLE_NAMES.add(Double.TYPE.getName());
		ELIGABLE_SIMPLE_NAMES.add(Float.TYPE.getName());
		ELIGABLE_SIMPLE_NAMES.add(Integer.TYPE.getName());
		ELIGABLE_SIMPLE_NAMES.add(Long.TYPE.getName());
		ELIGABLE_SIMPLE_NAMES.add(Short.TYPE.getName());
		ELIGABLE_SIMPLE_NAMES.add(Void.TYPE.getName());
		
		ELIGABLE_SIMPLE_NAMES.add(String.class.getSimpleName());
		ELIGABLE_SIMPLE_NAMES.add(Boolean.class.getSimpleName());
		ELIGABLE_SIMPLE_NAMES.add(Byte.class.getSimpleName());
		ELIGABLE_SIMPLE_NAMES.add(Character.class.getSimpleName());
		ELIGABLE_SIMPLE_NAMES.add(Double.class.getSimpleName());
		ELIGABLE_SIMPLE_NAMES.add(Float.class.getSimpleName());
		ELIGABLE_SIMPLE_NAMES.add(Integer.class.getSimpleName());
		ELIGABLE_SIMPLE_NAMES.add(Long.class.getSimpleName());
		ELIGABLE_SIMPLE_NAMES.add(Short.class.getSimpleName());
		ELIGABLE_SIMPLE_NAMES.add(Void.class.getSimpleName());
		ELIGABLE_SIMPLE_NAMES.add(java.lang.Exception.class.getSimpleName());
	}

	public static String getTypeName(Type type) {
		 
		if (TYPE_NAME_FIELD == null) {
			return type.getValue();
		}
		try {
			String value = (String) TYPE_NAME_FIELD.get(type);
			if (needResolve(value)) {
				return type.getValue();
			} else {
				return value;
			}
		} catch (Exception e) {
			e.printStackTrace();
			BasePlugin.log(e);
			return type.getValue();
		}
	}

	private static boolean needResolve(String value) {
		return value == null
				|| (!ELIGABLE_SIMPLE_NAMES.contains(value) && value.indexOf('.') == -1);
	}
}
