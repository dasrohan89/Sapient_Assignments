package com.rest.assignment.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * This service class saves objects
 * to MongoDB database.
 * @author Rohan Das
 */
@Service
final class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository repository;

    @Autowired
    ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductDTO create(ProductDTO product) {
        LOGGER.info("Creating a new product with information: {}", product);

        Product persisted = Product.getBuilder()
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .type(product.getType())
                .build();

        persisted = repository.save(persisted);
        LOGGER.info("Created a new product with information: {}", persisted);

        return convertToDTO(persisted);
    }

    @Override
    public ProductDTO delete(String id) {
        LOGGER.info("Deleting a product with id: {}", id);

        Product deleted = findProductById(id);
        repository.delete(deleted);

        LOGGER.info("Deleted product with informtation: {}", deleted);

        return convertToDTO(deleted);
    }

    @Override
    public List<ProductDTO> findAll() {
        LOGGER.info("Finding all products.");

        List<Product> products = repository.findAll();

        LOGGER.info("Found {} products", products.size());

        return convertToDTOs(products);
    }

    private List<ProductDTO> convertToDTOs(List<Product> models) {
        return models.stream()
                .map(this::convertToDTO)
                .collect(toList());
    }

    @Override
    public ProductDTO findById(String id) {
        LOGGER.info("Finding product with id: {}", id);

        Product found = findProductById(id);

        LOGGER.info("Found product: {}", found);

        return convertToDTO(found);
    }

    @Override
    public ProductDTO update(ProductDTO product) {
        LOGGER.info("Updating product with information: {}", product);

        Product updated = findProductById(product.getId());
        updated.update(product.getTitle(), product.getDescription(), product.getPrice(), product.getType());
        updated = repository.save(updated);

        LOGGER.info("Updated product with information: {}", updated);

        return convertToDTO(updated);
    }
    
    @Override
    public ProductDTO findByType(String type) {
        LOGGER.info("Finding product by type: {}", type);

        Product found = findProductByType(type);

        LOGGER.info("Found product: {}", found);

        return convertToDTO(found);
    }

    private Product findProductById(String id) {
    	Optional<Product> result = repository.findOne(id);
        return result.orElseThrow(() -> new ProductNotFoundException(id));
        
        

    }
    
    private Product findProductByType(String type) {
        Optional<Product> result = repository.findByType(type);
        return result.orElseThrow(() -> new ProductNotFoundException(type));

    }

    private ProductDTO convertToDTO(Product model) {
        ProductDTO dto = new ProductDTO();

        dto.setId(model.getId());
        dto.setTitle(model.getTitle());
        dto.setDescription(model.getDescription());
        dto.setPrice(model.getPrice());
        dto.setType(model.getType());

        return dto;
    }
}
