package org.eclipse.tigerstripe.workbench.ui.components.md;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.eclipse.swt.widgets.Control;

public class MasterDetailsBuilder {

	private final Map<Object, IDetails> details = new HashMap<Object, IDetails>();
	private final Collection<IKindResolver> keyResolvers = new LinkedHashSet<IKindResolver>();
	private IDetails defaultDetails;

	private MasterDetailsBuilder() {
	}

	public static MasterDetailsBuilder create() {
		return new MasterDetailsBuilder();
	}

	public MasterDetailsBuilder addDetail(Object kind, Control detailControl) {
		return addDetail(kind, new StaticDetail(detailControl));
	}

	public MasterDetailsBuilder addDetail(Object kind, IDetails detail) {
		details.put(kind, detail);
		return this;
	}

	public MasterDetailsBuilder addKeyResolver(IKindResolver keyResolver) {
		keyResolvers.add(keyResolver);
		return this;
	}

	public MasterDetailsBuilder useAsDefault(IDetails detail) {
		defaultDetails = detail;
		return this;
	}

	public MasterDetailsBuilder useAsDefault(Control detailControl) {
		return useAsDefault(new StaticDetail(detailControl));
	}

	public MasterDetails build() {
		if (defaultDetails == null) {
			defaultDetails = IDetails.NULL;
		}
		return new MasterDetails(defaultDetails, details, keyResolvers);
	}
}
