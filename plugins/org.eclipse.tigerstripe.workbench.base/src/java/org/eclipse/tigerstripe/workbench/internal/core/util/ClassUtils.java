package org.eclipse.tigerstripe.workbench.internal.core.util;

import java.util.Arrays;
import java.util.List;

public class ClassUtils {

	/**
	 * Check that the clazz implements the interface intf.
	 * 
	 * This is needed because class.getInterfaces only works on direct implementations.
	 * 
	 * @param clazz
	 * @param intf
	 * @return
	 */
	public static boolean doesImplement(Class clazz, Class intf) throws Exception {
		if (intf.isInterface()){
			Class[] implementsArray = clazz.getInterfaces();
			List<Class> implementsList = Arrays.asList(implementsArray);
			if (implementsList.contains(intf)){
				return true;
			}
			for (Class impl : implementsList){
				Class[] extendedInterfaces = impl.getInterfaces();
				List<Class> extendedInterfacesList = Arrays.asList(extendedInterfaces);
				if (extendedInterfacesList.contains(intf)){
					return true;
				}
				// Do we need to go up each of the extendedItems to look for our thing?
				// TODO
			}
			Class parent = clazz.getSuperclass();
			if (parent != null){
				return ClassUtils.doesImplement(parent, intf);
			}
			
		} else {
			throw new Exception("Arg 2 must be an Interface");
		}
		return false;
	}
	
}
