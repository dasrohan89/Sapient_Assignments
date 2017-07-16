package com.rest.assignment.product;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * This data transfer object contains the information of a single product
 * and specifies validation rules that are used to ensure that only
 * valid information can be saved to the used database.
 * @author Rohan Das
 */
public final class ProductDTO {

    private String id;

    @Size(max = Product.MAX_LENGTH_DESCRIPTION)
    private String description;

    @NotEmpty
    @Size(max = Product.MAX_LENGTH_TITLE)
    private String title;
    
    @NotEmpty
    private String price;
    
    private String type;

    public ProductDTO() {

    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
    public String toString() {
        return String.format(
                "ProductDTO[id=%s, description=%s, title=%s, price=%s, type=%s]",
                this.id,
                this.description,
                this.title,
                this.price,
                this.type
        );
    }
}
