package com.rest.assignment.product;

import static com.rest.assignment.product.ProductAssert.assertThatProduct;
import static com.rest.assignment.product.ProductDTOAssert.assertThatProductDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.rest.assignment.product.ProductDTO;
import com.rest.assignment.product.ProductNotFoundException;

/**
 * @author Rohan Das
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    private static final String DESCRIPTION = "description";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String PRICE = "price";
    private static final String TYPE = "type";

    @Mock
    private ProductRepository repository;

    private ProductServiceImpl service;

    @Before
    public void setUp() {
        this.service = new ProductServiceImpl(repository);
    }

    @Test
    public void create_ShouldSaveNewProduct() {
        ProductDTO newProduct = new ProductDTOBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .type(TYPE)
                .build();

        when(repository.save(isA(Product.class))).thenAnswer(invocation -> (Product) invocation.getArguments()[0]);

        service.create(newProduct);

        ArgumentCaptor<Product> savedProductArgument = ArgumentCaptor.forClass(Product.class);

        verify(repository, times(1)).save(savedProductArgument.capture());
        verifyNoMoreInteractions(repository);

        Product savedProduct = savedProductArgument.getValue();
        assertThatProduct(savedProduct)
                .hasTitle(TITLE)
                .hasDescription(DESCRIPTION)
                .hasPrice(PRICE)
                .hasType(TYPE);
    }

    @Test
    public void create_ShouldReturnTheInformationOfCreatedProduct() {
        ProductDTO newProduct = new ProductDTOBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .type(TYPE)
                .build();

        when(repository.save(isA(Product.class))).thenAnswer(invocation -> {
            Product persisted = (Product) invocation.getArguments()[0];
            ReflectionTestUtils.setField(persisted, "id", ID);
            return persisted;
        });

        ProductDTO returned = service.create(newProduct);

        assertThatProductDTO(returned)
                .hasId(ID)
                .hasTitle(TITLE)
                .hasDescription(DESCRIPTION)
                .hasPrice(PRICE);
    }

    @Test(expected = ProductNotFoundException.class)
    public void delete_ProductNotFound_ShouldThrowException() {
        when(repository.findOne(ID)).thenReturn(Optional.empty());

        service.findById(ID);
    }

    @Test
    public void delete_ProductFound_ShouldDeleteTheFoundProduct() {
        Product deleted = new ProductBuilder()
                .id(ID)
                .price(PRICE)
                .build();

        when(repository.findOne(ID)).thenReturn(Optional.of(deleted));

        service.delete(ID);

        verify(repository, times(1)).delete(deleted);
    }

    @Test
    public void delete_ProductFound_ShouldReturnTheDeletedProduct() {
        Product deleted = new ProductBuilder()
                .id(ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .type(TYPE)
                .build();

        when(repository.findOne(ID)).thenReturn(Optional.of(deleted));

        ProductDTO returned = service.delete(ID);

        assertThatProductDTO(returned)
                .hasId(ID)
                .hasTitle(TITLE)
                .hasDescription(DESCRIPTION)
                .hasPrice(PRICE);
    }

    @Test
    public void findAll_OneProductFound_ShouldReturnTheInformationOfFoundProduct() {
        Product expected = new ProductBuilder()
                .id(ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .type(TYPE)
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(expected));

        List<ProductDTO> products = service.findAll();
        assertThat(products).hasSize(1);

        ProductDTO actual = products.iterator().next();
        assertThatProductDTO(actual)
                .hasId(ID)
                .hasTitle(TITLE)
                .hasDescription(DESCRIPTION)
                .hasPrice(PRICE);
    }

    @Test(expected = ProductNotFoundException.class)
    public void findById_ProductNotFound_ShouldThrowException() {
        when(repository.findOne(ID)).thenReturn(Optional.empty());

        service.findById(ID);
    }

    @Test
    public void findById_ProductFound_ShouldReturnTheInformationOfFoundProduct() {
        Product found = new ProductBuilder()
                .id(ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .type(TYPE)
                .build();

        when(repository.findOne(ID)).thenReturn(Optional.of(found));

        ProductDTO returned = service.findById(ID);

        assertThatProductDTO(returned)
                .hasId(ID)
                .hasTitle(TITLE)
                .hasDescription(DESCRIPTION)
                .hasPrice(PRICE);
    }

    @Test(expected = ProductNotFoundException.class)
    public void update_UpdatedProductNotFound_ShouldThrowException() {
        when(repository.findOne(ID)).thenReturn(Optional.empty());

        ProductDTO updated = new ProductDTOBuilder()
                .id(ID)
                .build();

        service.update(updated);
    }

    @Test
    public void update_UpdatedProductFound_ShouldSaveUpdatedProduct() {
        Product existing = new ProductBuilder()
                .id(ID)
                .price(PRICE)
                .build();

        when(repository.findOne(ID)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        ProductDTO updated = new ProductDTOBuilder()
                .id(ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .type(TYPE)
                .build();

        service.update(updated);

        verify(repository, times(1)).save(existing);
        assertThatProduct(existing)
                .hasId(ID)
                .hasTitle(TITLE)
                .hasDescription(DESCRIPTION)
                .hasPrice(PRICE)
                .hasType(TYPE);
    }

    @Test
    public void update_UpdatedProductFound_ShouldReturnTheInformationOfUpdatedProduct() {
        Product existing = new ProductBuilder()
                .id(ID)
                .price(PRICE)
                .build();

        when(repository.findOne(ID)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        ProductDTO updated = new ProductDTOBuilder()
                .id(ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .type(TYPE)
                .build();

        ProductDTO returned = service.update(updated);
        assertThatProductDTO(returned)
                .hasId(ID)
                .hasTitle(TITLE)
                .hasDescription(DESCRIPTION)
                .hasPrice(PRICE);
    }
}
