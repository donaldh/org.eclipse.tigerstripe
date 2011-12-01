package org.eclipse.tigerstripe.annotation.core.storage.internal;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;

public interface ChangeListener {
	
	void onChange(List<Notification> notifications);
	
}
