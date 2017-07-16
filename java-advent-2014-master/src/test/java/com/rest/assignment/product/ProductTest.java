package com.rest.assignment.product;

import static com.rest.assignment.product.ProductAssert.assertThatProduct;

import org.junit.Test;

import com.rest.assignment.product.Product;

/**
 * @author Rohan Das
 */
public class ProductTest {

    private static final String DESCRIPTION = "description";
    private static final String TITLE = "title";
    private static final String PRICE = "price";

    private static final int MAX_LENGTH_DESCRIPTION = 500;
    private static final int MAX_LENGTH_TITLE = 100;

    private static final String UPDATED_DESCRIPTION = "updatedDescription";
    private static final String UPDATED_TITLE = "updatedTitle";
    private static final String UPDATED_PRICE = "updatedPrice";
    private static final String UPDATED_TYPE = "updatedType";

    @Test(expected = NullPointerException.class)
    public void build_TitleIsNull_ShouldThrowException() {
        Product.getBuilder()
                .title(null)
                .description(DESCRIPTION)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_TitleIsEmpty_ShouldThrowException() {
        Product.getBuilder()
                .title("")
                .description(DESCRIPTION)
                .price(PRICE)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_TitleIsTooLong_ShouldThrowException() {
        String tooLongTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_TITLE + 1);
        Product.getBuilder()
                .title(tooLongTitle)
                .description(DESCRIPTION)
                .price(PRICE)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_DescriptionIsTooLong_ShouldThrowException() {
        String tooLongDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1);
        Product.getBuilder()
                .title(TITLE)
                .description(tooLongDescription)
                .price("100")
                .build();
    }

    @Test
    public void build_WithoutDescription_ShouldCreateNewProductWithCorrectTitle() {
        Product build = Product.getBuilder()
                .title(TITLE)
                .price(PRICE)
                .build();

        assertThatProduct(build)
                .hasNoId()
                .hasTitle(TITLE)
                .hasPrice(PRICE)
                .hasNoDescription();
    }

    @Test
    public void build_WithTitleAndDescription_ShouldCreateNewProductWithCorrectTitleAndDescription() {
        Product build = Product.getBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .build();

        assertThatProduct(build)
                .hasNoId()
                .hasTitle(TITLE)
                .hasDescription(DESCRIPTION);
    }

    @Test
    public void build_WithMaxLengthTitleAndDescription_ShouldCreateNewProductWithCorrectTitleAndDescription() {
        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);

        Product build = Product.getBuilder()
                .title(maxLengthTitle)
                .description(maxLengthDescription)
                .price("500")
                .build();

        assertThatProduct(build)
                .hasNoId()
                .hasTitle(maxLengthTitle)
                .hasDescription(maxLengthDescription);
    }

    @Test(expected = NullPointerException.class)
    public void update_TitleIsNull_ShouldThrowException() {
        Product updated = Product.getBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .build();

        updated.update(null, UPDATED_DESCRIPTION, UPDATED_PRICE, UPDATED_TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_TitleIsEmpty_ShouldThrowException() {
        Product updated = Product.getBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .build();

        updated.update("", UPDATED_DESCRIPTION,UPDATED_PRICE, UPDATED_TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_TitleIsTooLong_ShouldThrowException() {
        Product updated = Product.getBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .build();

        String tooLongTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_TITLE + 1);
        updated.update(tooLongTitle, UPDATED_DESCRIPTION,UPDATED_PRICE, UPDATED_TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_DescriptionIsTooLong_ShouldThrowException() {
        Product updated = Product.getBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .build();

        String tooLongDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1);
        updated.update(UPDATED_TITLE, tooLongDescription,UPDATED_PRICE, UPDATED_TYPE);
    }

    @Test
    public void update_DescriptionIsNull_ShouldUpdateTitleAndDescription() {
        Product updated = Product.getBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .build();

        updated.update(UPDATED_TITLE, null,UPDATED_PRICE, UPDATED_TYPE);

        assertThatProduct(updated)
                .hasTitle(UPDATED_TITLE)
                .hasNoDescription();
    }

    @Test
    public void update_MaxLengthTitleAndDescription_ShouldUpdateTitleAndDescription() {
        Product updated = Product.getBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .build();

        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);

        updated.update(maxLengthTitle, maxLengthDescription,UPDATED_PRICE, UPDATED_TYPE);

        assertThatProduct(updated)
                .hasTitle(maxLengthTitle)
                .hasDescription(maxLengthDescription);
    }
    
    @Test
    public void update_PriceIsNull_ShouldUpdateTitleAndDescription() {
        Product updated = Product.getBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .build();

        updated.update(UPDATED_TITLE, UPDATED_DESCRIPTION,UPDATED_PRICE, UPDATED_TYPE);

        assertThatProduct(updated)
                .hasTitle(UPDATED_TITLE)
                .hasPrice(PRICE);
    }
}
