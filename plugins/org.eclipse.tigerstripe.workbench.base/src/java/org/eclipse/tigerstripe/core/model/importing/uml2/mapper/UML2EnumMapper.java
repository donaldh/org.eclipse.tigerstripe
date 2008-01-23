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
package org.eclipse.tigerstripe.core.model.importing.uml2.mapper;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.core.model.importing.ModelImporterListener;
import org.eclipse.tigerstripe.core.model.importing.uml2.annotables.UML2AnnotableElement;
import org.eclipse.tigerstripe.core.model.importing.uml2.annotables.UML2AnnotableElementPackage;
import org.eclipse.tigerstripe.core.util.Util;
import org.eclipse.tigerstripe.core.util.messages.MessageList;
import org.eclipse.uml2.uml.NamedElement;

public class UML2EnumMapper extends UML2BaseMapper {

	public UML2EnumMapper(NamedElement namedElement, UML2MappingUtils utils) {
		super(namedElement, utils);
	}

	public UML2AnnotableElement mapToAnnotable(ModelImporterListener listener,
			MessageList messageList) throws TigerstripeException {

		listener.importBeginTask("Processing UML Classes : "
				+ namedElement.getName(), 1);

		// Some EObjects could be of Type "NestedClassifier" which we can't
		// handle in our model.
		// Not sure how to detect these..this is a bit "hacky"
		String packageName = namedElement.getNearestPackage()
				.getQualifiedName();
		String elementName = namedElement.getQualifiedName();
		elementName = elementName.substring(packageName.length() + 2);

		if (elementName.contains("::")) {
			// // some elements left in name - ie not child of the package
			// String msgText = "Nested Classifier is not supported in TS : "
			// + element.getName();
			// addMessage(msgText, 0);
			// this.out.println("Error :" + msgText);
			// return null;
		}

		String validPackage = ValidIdentifiersUtils.getValidPackageName(
				messageList, Util.packageOf(UML2MappingUtils
						.convertToFQN(namedElement.getQualifiedName())), utils
						.getTargetProject(), elementName);
		UML2AnnotableElementPackage pack = new UML2AnnotableElementPackage(
				validPackage);

		String validElementName = ValidIdentifiersUtils.getValidArtifactName(
				messageList, elementName);

		UML2AnnotableElement result = new UML2AnnotableElement(validElementName);
		result.setAnnotableElementPackage(pack);

		result.setDescription(utils.getComment(namedElement));

		utils.setConstants(result, namedElement);

		result.setAnnotationType(AnnotableElement.AS_ENUMERATION);

		return result;
	}
}
