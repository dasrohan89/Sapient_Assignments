package com.rest.assignment.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

/**
 * This repository provides CRUD operations for product
 * objects.
 * @author Rohan Das
 */
interface ProductRepository extends Repository<Product, String> {

    /**
     * Deletes a product from the database.
     * @param deleted   The deleted product.
     */
    void delete(Product deleted);

    /**
     * Finds all products from the database.
     * @return  The information of all products that are found from the database.
     */
    List<Product> findAll();

    /**
     * Finds the information of a single product.
     * @param id    The id of the requested product.
     * @return      The information of the found product. If no product
     *              is found, this method returns an empty object.
     */
    Optional<Product> findOne(String id);

    /**
     * Saves a new product to the database.
     * @param saved The information of the saved product.
     * @return      The information of the saved product.
     */
    Product save(Product saved);
    
    /**
     * Finds the information of a type of product.
     * @param id    The type of the requested product.
     * @return      The information of the found product. If no product
     *              is found, this method returns an empty object.
     */
    Optional<Product> findByType(String type);
}
