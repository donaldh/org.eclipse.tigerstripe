package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;

public class DefaultModelMapper implements IModelMapper {

	public Map<Classifier, String> getMapping(Model model) {
		Map<Classifier, String> classMap = new HashMap<Classifier, String>();
		
		/* 
		 * First build a map of the eObject and the "default" artifact
		 * type that we will map to based on the Class in the UML
		 */
		TreeIterator t = model.eAllContents();
		t = model.eAllContents();

		while (t.hasNext()) {
				
			EObject eObject = (EObject) t.next();
			if (eObject instanceof AssociationClass) {
				classMap.put((Classifier) eObject, IAssociationClassArtifact.class.getName());
			} else if (eObject instanceof Association) {
				classMap.put((Classifier) eObject, IAssociationArtifact.class.getName());
			} else if (eObject instanceof Enumeration) {
				classMap.put((Classifier) eObject, IEnumArtifact.class.getName());
			} else if (eObject instanceof Interface) {
				classMap.put((Classifier) eObject,  ISessionArtifact.class.getName());
			} else if (eObject instanceof Class) {
				// We cannot determine "Class" types - could be Entity, Datatype, Exception etc
				classMap.put((Classifier) eObject, "");
			}
		}
		return classMap;
	}

}
