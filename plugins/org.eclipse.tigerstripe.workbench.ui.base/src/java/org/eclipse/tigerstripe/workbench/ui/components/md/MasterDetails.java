package org.eclipse.tigerstripe.workbench.ui.components.md;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MasterDetails {

	public final Map<Object, IDetails> details;
	private final Collection<IKindResolver> keyResolvers;
	private final IDetails defaultDetails;

	/**
	 * For lazy initialize
	 */
	private final Set<IDetails> initialized = new HashSet<IDetails>();

	private IDetails currentDetails;

	public MasterDetails(final IDetails defaultDetails,
			Map<Object, IDetails> details,
			Collection<IKindResolver> keyResolvers) {

		this.details = defenseNull(details, "details");
		this.defaultDetails = defenseNull(defaultDetails, "defenseNull");
		this.keyResolvers = new ArrayList<IKindResolver>(defenseNull(
				keyResolvers, "keyResolvers"));
		((List<IKindResolver>) this.keyResolvers)
				.add(new DefaultKindResolver());
		currentDetails = defaultDetails;
	}

	private <T> T defenseNull(T value, String name) {
		if (value == null) {
			throw new IllegalArgumentException(String.format(
					"'%s' parameter was null.", name));
		}
		return value;
	}

	/**
	 * Switches the current object, which entails switching forms for this kind
	 * if necessary and fill in the form attributes of the new object or if
	 * target is null switch to default empty details
	 * 
	 * @param target
	 *            object to switch
	 */
	public void switchTo(Object target) {
		if (target == null) {
			switchToDefault();
			return;
		}
		Object kind = resolveKind(target);
		IDetails detail = details.get(kind);
		if (detail == null) {
			throw new IllegalArgumentException(format(
					"Detials with key '%s' is not register", kind));
		}
		switchTo(detail);
		detail.switchTarget(target);
	}

	private Object resolveKind(Object target) {
		for (IKindResolver resolver : keyResolvers) {
			Object kind = resolver.toKind(target);
			if (kind != null) {
				return kind;
			}
		}
		throw new IllegalStateException(
				"Default key resolver must be resolve allways");
	}

	/**
	 * Switches to default empty details
	 */
	public void switchToDefault() {
		switchTo(defaultDetails);
	}

	private void switchTo(IDetails detail) {
		if (detail.equals(currentDetails)) {
			return;
		}
		if (initialized.add(detail)) {
			detail.init();
		}
		currentDetails.hide();
		currentDetails = detail;
		currentDetails.show();
	}
}
