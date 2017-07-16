package com.rest.assignment.product;

import java.util.List;

/**
 * This interface declares the methods that provides CRUD operations for
 * Product objects.
 * @author Rohan Das
 */
interface ProductService {

    /**
     * Creates a new product.
     * @param product  The information of the created product.
     * @return      The information of the created product.
     */
    ProductDTO create(ProductDTO product);

    /**
     * Deletes a product.
     * @param id    The id of the deleted product.
     * @return      THe information of the deleted product.
     * @throws com.rest.assignment.product.ProductNotFoundException if no product is found.
     */
    ProductDTO delete(String id);

    /**
     * Finds all product.
     * @return      The information of all product.
     */
    List<ProductDTO> findAll();

    /**
     * Finds a single product.
     * @param id    The id of the requested product.
     * @return      The information of the requested product.
     * @throws com.rest.assignment.product.ProductNotFoundException if no product is found.
     */
    ProductDTO findById(String id);

    /**
     * Updates the information of a product.
     * @param product  The information of the updated product.
     * @return      The information of the updated product.
     * @throws com.rest.assignment.product.ProductNotFoundException if no product is found.
     */
    ProductDTO update(ProductDTO product);
    
    /**
     * Finds a product by type.
     * @param id    The id of the requested product.
     * @return      The information of the requested product.
     * @throws com.rest.assignment.product.ProductNotFoundException if no product is found.
     */
    ProductDTO findByType(String type);
}
