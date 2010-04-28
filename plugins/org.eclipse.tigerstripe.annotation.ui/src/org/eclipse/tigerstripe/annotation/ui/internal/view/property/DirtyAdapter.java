package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;

public abstract class DirtyAdapter extends ChangeRecorder {

	private boolean dirty;

	public DirtyAdapter(EObject element) {
		super(element);
	}

	@Override
	public void notifyChanged(Notification msg) {
		if (msg.getEventType() == Notification.RESOLVE
				|| msg.getEventType() == Notification.REMOVING_ADAPTER)
			return;
		if (!dirty) {
			dirty = true;
			update();
		}
	}

	public void clear() {
		dirty = false;
		update();
	}

	protected abstract void update();

	public boolean isDirty() {
		return dirty;
	}

}