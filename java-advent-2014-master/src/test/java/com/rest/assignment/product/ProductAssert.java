package com.rest.assignment.product;

import org.assertj.core.api.AbstractAssert;

import com.rest.assignment.product.Product;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rohan Das
 */
class ProductAssert extends AbstractAssert<ProductAssert, Product> {

    private ProductAssert(Product actual) {
        super(actual, ProductAssert.class);
    }

    static ProductAssert assertThatProduct(Product actual) {
        return new ProductAssert(actual);
    }

    ProductAssert hasDescription(String expectedDescription) {
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

    ProductAssert hasId(String expectedId) {
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

    ProductAssert hasNoDescription() {
        isNotNull();

        String actualDescription = actual.getDescription();
        assertThat(actualDescription)
                .overridingErrorMessage("Expected description to be <null> but was <%s>", actualDescription)
                .isNull();

        return this;
    }

    ProductAssert hasNoId() {
        isNotNull();

        String actualId = actual.getId();
        assertThat(actualId)
                .overridingErrorMessage("Expected id to be <null> but was <%s>", actualId)
                .isNull();

        return this;
    }

    ProductAssert hasTitle(String expectedTitle) {
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
    
    ProductAssert hasNoPrice() {
        isNotNull();

        String price = actual.getPrice();
        assertThat(price)
                .overridingErrorMessage("Expected price to be <null> but was <%s>", price)
                .isNull();

        return this;
    }
    
    ProductAssert hasPrice(String expectedPrice) {
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
    
    ProductAssert hasType(String expectedType) {
        isNotNull();

        String actualType = actual.getType();
        assertThat(actualType)
                .overridingErrorMessage("Expected type to be <%s> but was <%s>",
                        expectedType,
                        actualType
                )
                .isEqualTo(expectedType);

        return this;
    }
}
