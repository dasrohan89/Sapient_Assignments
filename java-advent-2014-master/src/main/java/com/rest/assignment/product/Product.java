package com.rest.assignment.product;

import static com.rest.assignment.util.PreCondition.isTrue;
import static com.rest.assignment.util.PreCondition.notEmpty;
import static com.rest.assignment.util.PreCondition.notNull;

import org.springframework.data.annotation.Id;

/**
 * @author Rohan Das
 */
final class Product {

	static final int MAX_LENGTH_DESCRIPTION = 500;
	static final int MAX_LENGTH_TITLE = 100;

	@Id
	private String id;

	private String description;

	private String title;
	
	private String price;
	
	private String type;

	public Product() {
	}

	private Product(Builder builder) {
		this.description = builder.description;
		this.title = builder.title;
		this.price = builder.price;
		this.type = builder.type;
	}

	static Builder getBuilder() {
		return new Builder();
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

	public void update(String title, String description, String price, String type) {
		checkTitleAndDescription(title, description, price);

		this.title = title;
		this.description = description;
		this.price = price;
		this.type = type;
	}

	@Override
	public String toString() {
		return String.format("Product[id=%s, description=%s, title=%s, price=%s, type=%s]", 
				this.id, this.description, this.title, this.price, this.type);
	}

	
	static class Builder {

		private String description;

		private String title;
		
		private String price;
		
		private String type;

		private Builder() {
		}

		Builder description(String description) {
			this.description = description;
			return this;
		}

		Builder title(String title) {
			this.title = title;
			return this;
		}
		
		Builder price(String price) {
			this.price = price;
			return this;
		}
		
		Builder type(String type) {
			this.type = type;
			return this;
		}

		Product build() {
			Product build = new Product(this);

			build.checkTitleAndDescription(build.getTitle(), build.getDescription(), build.getPrice());

			return build;
		}
	}

	private void checkTitleAndDescription(String title, String description, String price) {
		notNull(title, "Title cannot be null");
		notNull(price, "Price cannot be null");
		notEmpty(title, "Title cannot be empty");
		isTrue(title.length() <= MAX_LENGTH_TITLE, "Title cannot be longer than %d characters", MAX_LENGTH_TITLE);

		if (description != null) {
			isTrue(description.length() <= MAX_LENGTH_DESCRIPTION, "Description cannot be longer than %d characters",
					MAX_LENGTH_DESCRIPTION);
		}
	}

	
}
