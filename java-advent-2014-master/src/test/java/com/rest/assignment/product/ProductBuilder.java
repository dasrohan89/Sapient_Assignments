package com.rest.assignment.product;

import org.springframework.test.util.ReflectionTestUtils;

import com.rest.assignment.product.Product;

/**
 * @author Rohan Das
 */
class ProductBuilder {

    private String description;
    private String id;
    private String title = "NOT_IMPORTANT";
    private String price;
    private String type;

    ProductBuilder() {

    }

    ProductBuilder description(String description) {
        this.description = description;
        return this;
    }

    ProductBuilder id(String id) {
        this.id = id;
        return this;
    }

    ProductBuilder title(String title) {
        this.title = title;
        return this;
    }
    
    ProductBuilder price(String price) {
        this.price = price;
        return this;
    }
    
    ProductBuilder type(String type) {
        this.type = type;
        return this;
    }

    Product build() {
        Product product = Product.getBuilder()
                .title(title)
                .description(description)
                .price(price)
                .type(type)
                .build();

        ReflectionTestUtils.setField(product, "id", id);

        return product;
    }
}
