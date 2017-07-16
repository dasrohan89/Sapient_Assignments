package com.rest.assignment.product;

import com.rest.assignment.product.ProductDTO;

/**
 * @author Rohan Das
 */
class ProductDTOBuilder {

    private String description;
    private String id;
    private String title;
    private String price;
    private String type;

    ProductDTOBuilder() {

    }

    ProductDTOBuilder description(String description) {
        this.description = description;
        return this;
    }

    ProductDTOBuilder id(String id) {
        this.id = id;
        return this;
    }

    ProductDTOBuilder title(String title) {
        this.title = title;
        return this;
    }
    
    ProductDTOBuilder price(String price) {
        this.price = price;
        return this;
    }
    
    ProductDTOBuilder type(String type) {
        this.type = type;
        return this;
    }

    ProductDTO build() {
        ProductDTO dto = new ProductDTO();

        dto.setDescription(description);
        dto.setId(id);
        dto.setTitle(title);
        dto.setPrice(price);
        dto.setType(type);

        return dto;
    }
}
