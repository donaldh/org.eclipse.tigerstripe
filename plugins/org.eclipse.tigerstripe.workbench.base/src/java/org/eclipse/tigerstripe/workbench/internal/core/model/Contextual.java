package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The annotation is used for marking methods result of which will be wrapped to contextual proxy.
 * It is used to avoid hardcoding signatures of methods explicitly in source code in {@link ContextProjectAwareProxy}.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Contextual {

}
