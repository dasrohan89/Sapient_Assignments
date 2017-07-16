package com.rest.assignment.product;

import org.assertj.core.api.AbstractAssert;

import com.rest.assignment.product.ProductDTO;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rohan Das
 */
class ProductDTOAssert extends AbstractAssert<ProductDTOAssert, ProductDTO> {

    private ProductDTOAssert(ProductDTO actual) {
        super(actual, ProductDTOAssert.class);
    }

    static ProductDTOAssert assertThatProductDTO(ProductDTO actual) {
        return new ProductDTOAssert(actual);
    }

    public ProductDTOAssert hasDescription(String expectedDescription) {
        isNotNull();

        String actualDescription = actual.getDescription();
        assertThat(actualDescription)
                .overridingErrorMessage("Expected description to be <%s> but was <%s>",
                        expectedDescription,
                        actualDescription
                )
                .isEqualTo(expectedDescription);

        return this;
    }

    public ProductDTOAssert hasId(String expectedId) {
        isNotNull();

        String actualId = actual.getId();
        assertThat(actualId)
                .overridingErrorMessage("Expected id to be <%s> but was <%s>",
                        expectedId,
                        actualId
                )
                .isEqualTo(expectedId);

        return this;
    }

    public ProductDTOAssert hasNoDescription() {
        isNotNull();

        String actualDescription = actual.getDescription();
        assertThat(actualDescription)
                .overridingErrorMessage("expected description to be <null> but was <%s>", actualDescription)
                .isNull();

        return this;
    }

    public ProductDTOAssert hasNoId() {
        isNotNull();

        String actualId = actual.getId();
        assertThat(actualId)
                .overridingErrorMessage("Expected id to be <null> but was <%s>", actualId)
                .isNull();

        return this;
    }

    public ProductDTOAssert hasTitle(String expectedTitle) {
        isNotNull();

        String actualTitle = actual.getTitle();
        assertThat(actualTitle)
                .overridingErrorMessage("Expected title to be <%s> but was <%s>",
                        expectedTitle,
                        actualTitle
                )
                .isEqualTo(expectedTitle);

        return this;
    }
    
    public ProductDTOAssert hasPrice(String expectedPrice) {
        isNotNull();

        String actualPrice = actual.getPrice();
        assertThat(actualPrice)
                .overridingErrorMessage("Expected price to be <%s> but was <%s>",
                		expectedPrice,
                		actualPrice
                )
                .isEqualTo(expectedPrice);

        return this;
    }
    
    public ProductDTOAssert hasType(String expectedType) {
        isNotNull();

        String actualType = actual.getTitle();
        assertThat(actualType)
                .overridingErrorMessage("Expected type to be <%s> but was <%s>",
                		expectedType,
                        actualType
                )
                .isEqualTo(expectedType);

        return this;
    }
}
