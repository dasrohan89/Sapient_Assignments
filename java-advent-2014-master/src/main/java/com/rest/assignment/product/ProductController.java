package com.rest.assignment.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * This controller provides the public API that is used to manage the information
 * of products.
 * @author Rohan Das
 */
@RestController
@RequestMapping("/api/product")
final class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final ProductService service;

    @Autowired
    ProductController(ProductService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    ProductDTO create(@RequestBody @Valid ProductDTO product) {
        LOGGER.info("Creating a new product with information: {}", product);

        ProductDTO created = service.create(product);
        LOGGER.info("Created a new product with information: {}", created);

        return created;
    }

    @RequestMapping(value = "/deleteproduct/{id}", method = RequestMethod.DELETE)
    ProductDTO delete(@PathVariable("id") String id) {
        LOGGER.info("Deleting a product with id: {}", id);

        ProductDTO deleted = service.delete(id);
        LOGGER.info("Deleted product with information: {}", deleted);

        return deleted;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<ProductDTO> findAll() {
        LOGGER.info("Finding all products");

        List<ProductDTO> products = service.findAll();
        LOGGER.info("Found {} products", products.size());

        return products;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    ProductDTO findById(@PathVariable("id") String id) {
        LOGGER.info("Finding product with id: {}", id);

        ProductDTO product = service.findById(id);
        LOGGER.info("Found product with information: {}", product);

        return product;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    ProductDTO update(@RequestBody @Valid ProductDTO product) {
        LOGGER.info("Updating product with information: {}", product);

        ProductDTO updated = service.update(product);
        LOGGER.info("Updated product with information: {}", updated);

        return updated;
    }
    
    @RequestMapping(value = "{type}", method = RequestMethod.GET)
    ProductDTO findByType(@PathVariable("type") String type) {
        LOGGER.info("Finding product with type: {}", type);

        ProductDTO product = service.findByType(type);
        LOGGER.info("Found product with information: {}", product);

        return product;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleProductNotFound(ProductNotFoundException ex) {
        LOGGER.error("Handling error with message: {}", ex.getMessage());
    }
}
