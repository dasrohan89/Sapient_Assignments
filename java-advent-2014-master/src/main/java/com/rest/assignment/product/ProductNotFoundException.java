package com.rest.assignment.product;

/**
 * This exception is thrown when the requested product is not found.
 * @author Rohan Das
 */
public class ProductNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(String id) {
        super(String.format("No product found with id: <%s>", id));
    }
}
