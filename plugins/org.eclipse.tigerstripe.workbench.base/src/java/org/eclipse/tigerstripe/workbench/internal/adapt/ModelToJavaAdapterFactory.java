package org.eclipse.tigerstripe.workbench.internal.adapt;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.Signature;
import org.eclipse.tigerstripe.workbench.internal.core.model.Field;
import org.eclipse.tigerstripe.workbench.internal.core.model.Literal;
import org.eclipse.tigerstripe.workbench.internal.core.model.Method;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMember;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;

public class ModelToJavaAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {

		if (adapterType == IJavaElement.class && adaptableObject instanceof IMember) {
			IMember member = (IMember) adaptableObject;
			IJavaElement element = getJavaElement(member.getContainingArtifact());
			if (element instanceof ITypeRoot) {
				ITypeRoot root = (ITypeRoot) element;
				org.eclipse.jdt.core.IType owner = root.findPrimaryType();
				if (owner != null) {
					return getJavaMember(owner, member);
				}
			}

		}

		return null;
	}


	protected IJavaElement getJavaElement(IAbstractArtifact artifact) {
		if (!Proxy.isProxyClass(artifact.getClass())) {
			return (IJavaElement) artifact.getAdapter(IJavaElement.class);
		}
		return null;
	}

	protected org.eclipse.jdt.core.IMember getJavaMember(org.eclipse.jdt.core.IType owner, IMember model) {
		if (model instanceof IField || model instanceof ILiteral) {
			return owner.getField(model.getName());
		} else if (model instanceof IMethod) {
			IMethod method = (IMethod) model;
			Collection<IArgument> args = method.getArguments();
			List<String> params = new ArrayList<String>(args.size());
			for (IArgument arg : args) {
				String name = arg.getType().getFullyQualifiedName();
				if (arg.getType().getTypeMultiplicity().isArray()) {
					name += " []";
				}
				params.add(Signature.createTypeSignature(name, false));
			}

			return owner.getMethod(method.getName(), params.toArray(new String[params.size()]));
		}
		return null;
	}

	public @SuppressWarnings("rawtypes")
	Class[] getAdapterList() {
		return new Class[] { Literal.class, Field.class, Method.class };
	}

}
