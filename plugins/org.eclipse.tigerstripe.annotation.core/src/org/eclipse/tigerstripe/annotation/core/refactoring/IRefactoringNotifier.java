/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Alena Repina)
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.core.refactoring;

import java.util.Map;

/**
 * Notifier that allows any refactoring tool to notify Annotation framework
 * about changes performed with objects (resources, java objects, Tigerstripe
 * model components and so on).
 * <p>
 * Clients may implement this interface.
 * 
 * @see ILazyObject
 * 
 * @author Alena Repina
 * 
 */
public interface IRefactoringNotifier {

	/**
	 * Notify framework about object has been deleted
	 * 
	 * @param object
	 */
	void fireDeleted(ILazyObject object);

	/**
	 * Notify framework about object has been changed. Kind of notification can
	 * take a value of {@link IRefactoringChangesListener.ABOUT_TO_CHANGE}
	 * before changes and {@link IRefactoringChangesListener.CHANGED} after
	 * that.
	 * 
	 * @param oldObject
	 *            represents object before changes
	 * @param newObject
	 *            represents object after changes
	 * @param kind
	 *            kind of notification
	 */
	void fireChanged(ILazyObject oldObject, ILazyObject newObject, int kind);

	/**
	 * Notify framework about objects have been moved. Kind of notification can
	 * take a value of {@link IRefactoringChangesListener.ABOUT_TO_CHANGE}
	 * before moving and {@link IRefactoringChangesListener.CHANGED} after that.
	 * 
	 * @param objects
	 *            represent objects before moving
	 * @param destination
	 *            destination of objects moved
	 * @param kind
	 *            kind of notification
	 */
	void fireMoved(ILazyObject[] objects, ILazyObject destination, int kind);

	/**
	 * Notify framework about objects have been copied. Kind of notification can
	 * take a value of {@link IRefactoringChangesListener.ABOUT_TO_CHANGE}
	 * before moving and {@link IRefactoringChangesListener.CHANGED} after that.
	 * 
	 * @param objects
	 *            represent objects before coping
	 * @param destination
	 *            destination of objects copied
	 * @param newNames
	 *            map from object to its new name
	 * @param kind
	 *            kind of notification
	 */
	void fireCopy(ILazyObject[] objects, ILazyObject destination,
			Map<ILazyObject, String> newNames, int kind);
}
