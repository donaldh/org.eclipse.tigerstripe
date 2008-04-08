package org.eclipse.tigerstripe.workbench.optional.UML2Export;

import java.io.PrintWriter;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;

public class Maker {

	private Map<String, Type> typeMap;
	private PrintWriter out;
	private MessageList messages;
	private Model typesModel;
	private Model unknownTypeModel;
	private Map<String, Type> unknownTypeMap;
	
	boolean mapUnknownTypes = true; // This is never changed!
	
	
	public Maker(Map<String, Type> typeMap, PrintWriter out,
			MessageList messages, Model typesModel, Model unknownTypeModel, Map<String, Type> unknownTypeMap) {
		super();
		this.typeMap = typeMap;
		this.out = out;
		this.messages = messages;
		this.typesModel = typesModel;
		this.unknownTypeModel = unknownTypeModel;
		this.unknownTypeMap = unknownTypeMap;
	}
	
	/**
	 * Find an association if it exists, or make one if it doesn't.
	 */
	protected Dependency makeDependency(IAbstractArtifact artifact, Model model) {
		IDependencyArtifact dependencyArtifact = (IDependencyArtifact) artifact;
		Type aEndType = getUMLType(dependencyArtifact.getAEndType(), model);
		Type zEndType = getUMLType(dependencyArtifact.getZEndType(),model);
		if (aEndType != null && zEndType != null) {
			Type type1 = typeMap.get(aEndType.getQualifiedName());
			Type type2 = typeMap.get(zEndType.getQualifiedName());
			Dependency dep = type1.createDependency(type2);
			dep.setName(artifact.getName());

			this.out.println("Made a new Dependency " + dep.getQualifiedName());
			return dep;
		} else {
			String msgText = artifact.getName()
					+ " One or the other end was not a valid type : "
					+ dependencyArtifact.getAEndType().getFullyQualifiedName()
					+ " "
					+ dependencyArtifact.getZEndType().getFullyQualifiedName();
			out.println("ERROR :" + msgText);
			TS2UML2.addMessage(msgText, 0);
			return null;
		}

	}
	
	protected Type getUMLType(IType iType, Model model) {
		// The type here might be a another classifier, or a primitive type
		// OR Any built-in Type?

		String iTypeName = Utilities.mapName(iType.getFullyQualifiedName(), model);

		// Look in project classes
		if (typeMap.containsKey(iTypeName)) {
			this.out.println("Mapped "
					+ typeMap.get(iTypeName).getQualifiedName());
			return typeMap.get(iTypeName);
		}

		iTypeName = iType.getName().replace(".", "::");

		// Now look at primitive types..
		if (typesModel.getOwnedType(iTypeName) != null)
			return typesModel.getOwnedType(iTypeName);

		/*
		 * This doesn't make any sense during export as you can never recover on
		 * import // Look for java versions of the names.. BuiltInTypeMapper
		 * mapper = new BuiltInTypeMapper(); if
		 * (mapper.getMappedName(iTypeName)!= null){ if
		 * (typesModel.getOwnedType(mapper.getMappedName(iTypeName)) != null){
		 * return typesModel.getOwnedType(mapper.getMappedName(iTypeName)); } }
		 */

		// Make a "special" model for the unknown types.....
		// Note the need to make fully qualified Name
		if (mapUnknownTypes) {
			if (unknownTypeModel.getOwnedType(iTypeName) != null)
				return unknownTypeModel.getOwnedType(iTypeName);
			else {
				// OK - so we got here because the type isn't known , so we'd
				// better create one
				String p = iType.getFullyQualifiedName().substring(0,
						iType.getFullyQualifiedName().lastIndexOf("."));

				Class madeClass = null;
				Package modelPackage = makeOrFindPackage(p, unknownTypeModel);
				EList classList = modelPackage.getOwnedMembers();
				for (Object cl : classList) {
					if (cl instanceof Class) {
						Class aClass = (Class) cl;
						if (aClass.getQualifiedName().equals(iTypeName)) {
							// this.out.println("Found it!");
							madeClass = aClass;
						}
					}
				}
				if (madeClass == null) {
					// Didn't find it so make it!
					// this.out.println("Make it!");
					madeClass = modelPackage.createOwnedClass(iType.getName(),
							true);
				}
				if (madeClass != null) {
					this.out.println(iTypeName + " Added to unknown type Map");
					unknownTypeMap.put(madeClass.getQualifiedName(), madeClass);
					return madeClass;
				}
			}
		}

		return null;
	}
	
	/**
	 * Find a package if it exists, or make one if it doesn't.
	 * 
	 * Iterate up the tree making any that are missing
	 * 
	 * @param packageName
	 * @return
	 */
	protected Package makeOrFindPackage(String packageName, Model modelToAddTo) {

		Package parent;
		// out.println ("MoF " + packageName);
		if (packageName.contains(".")) {
			parent = makeOrFindPackage(packageName.substring(0, packageName
					.lastIndexOf(".")), modelToAddTo);
		} else {
			parent = modelToAddTo;
		}
		String umlPackageName = Utilities.mapName(packageName, modelToAddTo);
		EList packageList = parent.getNestedPackages();
		for (Object pack : packageList) {
			Package aPackage = (Package) pack;
			// out.println("Found "+aPackage.getQualifiedName()+" compare
			// "+umlPackageName);
			if (aPackage.getQualifiedName().equals(umlPackageName))
				return aPackage;
		}

		// Didn't find it...so make one.
		Package newPackage = parent.createNestedPackage(packageName
				.substring(packageName.lastIndexOf(".") + 1));
		out.println("Made a new package " + newPackage.getQualifiedName());
		return newPackage;
	}
	
	/**
	 * Find a class if it exists, or make one if it doesn't.
	 */
	protected Class makeOrFindClass(IAbstractArtifact artifact, Model model) {
		try {
			String packageName = artifact.getPackage();
			String className = artifact.getFullyQualifiedName();
			String umlClassName = Utilities.mapName(className, model);

			Package modelPackage = makeOrFindPackage(packageName, model);
			EList classList = modelPackage.getOwnedMembers();
			for (Object cl : classList) {
				if (cl instanceof Class) {
					Class aClass = (Class) cl;
					if (aClass.getQualifiedName().equals(umlClassName))
						return aClass;
				}
			}

			Class clazz = modelPackage.createOwnedClass(artifact.getName(),
					artifact.isAbstract());
			this.out.println("Made a new class " + clazz.getQualifiedName());

			Comment comment = clazz.createOwnedComment();
			comment.setBody(artifact.getComment());

			clazz.setIsAbstract(artifact.isAbstract());
			typeMap.put(clazz.getQualifiedName(), clazz);
			return clazz;
		} catch (Exception e) {
			String msgText = artifact.getName() + e.getMessage();
			out.println("ERROR :" + msgText);
			TS2UML2.addMessage(msgText, 0);
			e.printStackTrace(this.out);
			return null;
		}
	}
	
	protected AssociationClass makeOrFindAssociationClass(
			IAbstractArtifact artifact, Model model) {
		try {
			String packageName = artifact.getPackage();
			String className = artifact.getFullyQualifiedName();
			String umlClassName = Utilities.mapName(className, model);

			Package modelPackage = makeOrFindPackage(packageName, model);
			EList classList = modelPackage.getOwnedMembers();
			for (Object as : classList) {
				if (as instanceof AssociationClass) {
					AssociationClass aAssoc = (AssociationClass) as;
					if (aAssoc.getQualifiedName().equals(umlClassName))
						return aAssoc;
				}
			}
			// TODO make one...

			IAssociationEnd end1 = ((IAssociationArtifact) artifact).getAEnd();
			Type e1Type = getUMLType(end1.getType(), model);
			Type type1 = null;
			if (e1Type != null)
				type1 = typeMap.get(e1Type.getQualifiedName());

			IAssociationEnd end2 = ((IAssociationArtifact) artifact).getZEnd();
			Type e2Type = getUMLType(end2.getType(), model);
			Type type2 = null;
			if (e2Type != null)
				type2 = typeMap.get(e2Type.getQualifiedName());
			if (type1 != null && type2 != null) {

				boolean end1IsNavigable = end1.isNavigable();
				AggregationKind end1Aggregation = AggregationKind.get(end1
						.getAggregation().getLabel());

				// Swap the types over for some reason....

				String end1Name = end1.getName();
				String end2Name = end2.getName();

				int end1LowerBound = Utilities.getLowerBound(end1.getMultiplicity());
				int end1UpperBound = Utilities.getUpperBound(end1.getMultiplicity());

				boolean end2IsNavigable = end2.isNavigable();
				AggregationKind end2Aggregation = AggregationKind.get(end2
						.getAggregation().getLabel());

				int end2LowerBound = Utilities.getLowerBound(end1.getMultiplicity());
				int end2UpperBound = Utilities.getUpperBound(end2.getMultiplicity());

				/*
				 * Association assoc = type2.createAssociation(end1IsNavigable,
				 * end1Aggregation, end1Name, end1LowerBound, end1UpperBound,
				 * type1, end2IsNavigable, end2Aggregation, end2Name,
				 * end2LowerBound, end2UpperBound);
				 */

				AssociationClass aClass = UMLFactory.eINSTANCE
						.createAssociationClass();
				aClass.setPackage(modelPackage);
				aClass.setName(artifact.getName());

				Property aEnd;
				if (end2.isNavigable()) {
					aEnd = aClass.createNavigableOwnedEnd(end1Name, type2);
				} else {
					aEnd = aClass.createOwnedEnd(end1Name, type2);
				}
				aEnd.setAggregation(end1Aggregation);

				aEnd.setLower(end1LowerBound);
				aEnd.setUpper(end1UpperBound);

				switch (end1.getVisibility()) {
				case PACKAGE:
					aEnd.setVisibility(VisibilityKind.PACKAGE_LITERAL);
					break;
				case PRIVATE:
					aEnd.setVisibility(VisibilityKind.PRIVATE_LITERAL);
					break;
				case PROTECTED:
					aEnd.setVisibility(VisibilityKind.PROTECTED_LITERAL);
					break;
				case PUBLIC:
					aEnd.setVisibility(VisibilityKind.PUBLIC_LITERAL);
					break;
				}

				Property zEnd;
				if (end1.isNavigable()) {
					zEnd = aClass.createNavigableOwnedEnd(end2Name, type1);
				} else {
					zEnd = aClass.createOwnedEnd(end2Name, type1);
				}
				zEnd.setAggregation(end2Aggregation);

				aEnd.setLower(end2LowerBound);
				aEnd.setUpper(end2UpperBound);

				switch (end2.getVisibility()) {
				case PACKAGE:
					zEnd.setVisibility(VisibilityKind.PACKAGE_LITERAL);
					break;
				case PRIVATE:
					zEnd.setVisibility(VisibilityKind.PRIVATE_LITERAL);
					break;
				case PROTECTED:
					zEnd.setVisibility(VisibilityKind.PROTECTED_LITERAL);
					break;
				case PUBLIC:
					zEnd.setVisibility(VisibilityKind.PUBLIC_LITERAL);
					break;
				}

				aClass.setIsAbstract(artifact.isAbstract());
				typeMap.put(aClass.getQualifiedName(), aClass);
				out.println("Made a new association Class "
						+ aClass.getQualifiedName());
				return aClass;

			} else {
				String msgText = artifact.getName()
						+ " One or the other end was not a valid type : "
						+ end1.getType().getFullyQualifiedName() + " "
						+ end2.getType().getFullyQualifiedName();
				out.println("ERROR :" + msgText);
				TS2UML2.addMessage(msgText, 0);
				return null;
			}

		} catch (Exception e) {
			String msgText = artifact.getName() + e.getMessage();
			out.println("ERROR :" + msgText);
			TS2UML2.addMessage(msgText, 0);
			e.printStackTrace(this.out);
			return null;
		}
	}
	
	/**
	 * Find an association if it exists, or make one if it doesn't.
	 */
	protected Association makeOrFindAssociation(IAbstractArtifact artifact, Model model) {
		// TigerstripeRuntime.logInfoMessage(" artiacf=" +
		// artifact.getFullyQualifiedName());
		try {
			String packageName = artifact.getPackage();
			String className = artifact.getFullyQualifiedName();
			String umlClassName = Utilities.mapName(className, model);

			Package modelPackage = makeOrFindPackage(packageName, model);
			EList classList = modelPackage.getOwnedMembers();
			for (Object as : classList) {
				if (as instanceof Association) {
					Association aAssoc = (Association) as;
					if (aAssoc.getQualifiedName().equals(umlClassName))
						return aAssoc;
				}
			}
			// TODO make one...
			IAssociationEnd end1 = ((IAssociationArtifact) artifact).getAEnd();
			Type e1Type = getUMLType(end1.getType(), model);
			Type type1 = null;
			if (e1Type != null)
				type1 = typeMap.get(e1Type.getQualifiedName());

			IAssociationEnd end2 = ((IAssociationArtifact) artifact).getZEnd();
			Type e2Type = getUMLType(end2.getType(), model);
			Type type2 = null;
			if (e2Type != null)
				type2 = typeMap.get(e2Type.getQualifiedName());
			if (type1 != null && type2 != null) {

				boolean end1IsNavigable = end1.isNavigable();
				AggregationKind end1Aggregation = AggregationKind.get(end1
						.getAggregation().getLabel());

				// Swap the types over for some reason....

				String end1Name = end1.getName();
				String end2Name = end2.getName();

				int end1LowerBound = Utilities.getLowerBound(end1.getMultiplicity());
				int end1UpperBound = Utilities.getUpperBound(end1.getMultiplicity());

				boolean end2IsNavigable = end2.isNavigable();
				AggregationKind end2Aggregation = AggregationKind.get(end2
						.getAggregation().getLabel());

				int end2LowerBound = Utilities.getLowerBound(end2.getMultiplicity());
				int end2UpperBound = Utilities.getUpperBound(end2.getMultiplicity());

				Association newAssoc = UMLFactory.eINSTANCE.createAssociation();

				Property aEnd = newAssoc.createOwnedEnd(end1Name, type1);
				Property zEnd = newAssoc.createOwnedEnd(end2Name, type2);

				aEnd.setAggregation(end1Aggregation);
				aEnd.setIsNavigable(end1.isNavigable());
				aEnd.setIsOrdered(end1.isOrdered());
				aEnd.setIsUnique(end1.isUnique());

				// Multiplicity
				aEnd.setLower(end1LowerBound);
				aEnd.setUpper(end1UpperBound);

				zEnd.setAggregation(end2Aggregation);
				zEnd.setIsNavigable(end2.isNavigable());
				zEnd.setIsOrdered(end2.isOrdered());
				zEnd.setIsUnique(end2.isUnique());

				// Multiplicity
				zEnd.setLower(end2LowerBound);
				zEnd.setUpper(end2UpperBound);

				newAssoc.setName(artifact.getName());
				newAssoc.setIsAbstract(artifact.isAbstract());

				Association assoc = type2.createAssociation(end1IsNavigable,
						end1Aggregation, end1Name, end1LowerBound,
						end1UpperBound, type1, end2IsNavigable,
						end2Aggregation, end2Name, end2LowerBound,
						end2UpperBound);

				assoc.setName(artifact.getName());
				assoc.setIsAbstract(artifact.isAbstract());
				out.println("Made a new association "
						+ assoc.getQualifiedName());
				return assoc;
			} else {
				String msgText = artifact.getName()
						+ " One or the other end was not a valid type : "
						+ end1.getType().getFullyQualifiedName() + " "
						+ end2.getType().getFullyQualifiedName();
				out.println("ERROR :" + msgText);
				TS2UML2.addMessage(msgText, 0);
				return null;
			}
		} catch (Exception e) {
			String msgText = artifact.getName() + e.getMessage();
			out.println("ERROR :" + msgText);
			TS2UML2.addMessage(msgText, 0);
			e.printStackTrace(this.out);
			return null;
		}
	}
	
	/**
	 * Find a class if it exists, or make one if it doesn't.
	 */
	protected Enumeration makeOrFindEnum(IAbstractArtifact artifact, Model model) {
		try {
			String packageName = artifact.getPackage();
			String className = artifact.getFullyQualifiedName();
			String umlClassName = Utilities.mapName(className, model);

			Package modelPackage = makeOrFindPackage(packageName, model);
			EList classList = modelPackage.getOwnedMembers();
			for (Object cl : classList) {
				if (cl instanceof Enumeration) {
					Enumeration aClass = (Enumeration) cl;
					if (aClass.getQualifiedName().equals(umlClassName))
						return aClass;
				}
			}

			Enumeration enumz = modelPackage.createOwnedEnumeration(artifact
					.getName());
			this.out.println("Made a new enumeration "
					+ enumz.getQualifiedName());
			typeMap.put(enumz.getQualifiedName(), enumz);

			// We can add EnumLiterals
			for (ILiteral literal : artifact.getLiterals()) {
				EnumerationLiteral lit = enumz.createOwnedLiteral(literal
						.getName());
				this.out.println("Made a new literal " + literal.getName());
				if (literal.getType().getName().equals("int")) {
					LiteralInteger literalInt = UMLFactory.eINSTANCE
							.createLiteralInteger();
					literalInt.setValue(Integer.parseInt(literal.getValue()));
					lit.setSpecification(literalInt);
				} else {
					LiteralString literalString = UMLFactory.eINSTANCE
							.createLiteralString();
					literalString.setValue(literal.getValue());
					lit.setSpecification(literalString);
				}
			}
			enumz.setIsAbstract(artifact.isAbstract());
			return enumz;
		} catch (Exception e) {
			String msgText = artifact.getName() + e.getMessage();
			out.println("ERROR :" + msgText);
			TS2UML2.addMessage(msgText, 0);
			e.printStackTrace(this.out);
			return null;
		}
	}
	
	/**
	 * Find a class if it exists, or make one if it doesn't.
	 */
	protected Interface makeOrFindInterface(IAbstractArtifact artifact,
			Model model) {
		try {
			String packageName = artifact.getPackage();
			String className = artifact.getFullyQualifiedName();

			String umlClassName = Utilities.mapName(className, model);
			Package modelPackage = makeOrFindPackage(packageName, model);
			EList classList = modelPackage.getOwnedMembers();
			for (Object cl : classList) {
				if (cl instanceof Interface) {
					Interface aClass = (Interface) cl;
					if (aClass.getQualifiedName().equals(umlClassName))
						return aClass;
				}
			}

			Interface interf = modelPackage.createOwnedInterface(artifact
					.getName());
			this.out.println("Made a new interface "
					+ interf.getQualifiedName());

			Comment comment = interf.createOwnedComment();
			comment.setBody(artifact.getComment());
			interf.setIsAbstract(artifact.isAbstract());
			typeMap.put(interf.getQualifiedName(), interf);
			return interf;
		} catch (Exception e) {
			String msgText = artifact.getName() + e.getMessage();
			out.println("ERROR :" + msgText);
			TS2UML2.addMessage(msgText, 0);
			e.printStackTrace(this.out);
			return null;
		}
	}
}
