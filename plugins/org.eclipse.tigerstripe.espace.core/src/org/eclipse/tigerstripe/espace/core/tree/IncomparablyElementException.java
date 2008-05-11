package org.eclipse.tigerstripe.espace.core.tree;

/**
 * @author Yuri Strot
 *
 */
public class IncomparablyElementException extends RuntimeException {

    private static final long serialVersionUID = -5559661166558130849L;
    
    public IncomparablyElementException() {
    	super();
    }
    
    public IncomparablyElementException(String message) {
    	super(message);
    }
    
    public IncomparablyElementException(Throwable t) {
    	super(t);
    }
    
    public IncomparablyElementException(String message, Throwable t) {
    	super(message, t);
    }
    
}
