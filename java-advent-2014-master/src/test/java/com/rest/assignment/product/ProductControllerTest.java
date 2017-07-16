package com.rest.assignment.product;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.rest.assignment.error.RestErrorHandler;
import com.rest.assignment.product.ProductController;
import com.rest.assignment.product.ProductDTO;
import com.rest.assignment.product.ProductNotFoundException;
import com.rest.assignment.product.ProductService;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;

import static com.rest.assignment.product.ProductDTOAssert.assertThatProductDTO;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rohan Das
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    private static final String DESCRIPTION = "description";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String PRICE = "price";
    private static final String TYPE = "type";

    private static final int MAX_LENGTH_DESCRIPTION = 500;
    private static final int MAX_LENGTH_TITLE = 100;

    @Mock
    private ProductService service;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductController(service))
                .setHandlerExceptionResolvers(withExceptionControllerAdvice())
                .build();
    }

    
    private ExceptionHandlerExceptionResolver withExceptionControllerAdvice() {
        final ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(final HandlerMethod handlerMethod,
                                                                              final Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(RestErrorHandler.class).resolveMethod(exception);
                if (method != null) {
                    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
                    messageSource.setBasename("messages");
                    return new ServletInvocableHandlerMethod(new RestErrorHandler(messageSource), method);
                }
                return super.getExceptionHandlerMethod(handlerMethod, exception);
            }
        };
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }

    @Test
    public void create_ProductWithOnlyTitle_ShouldCreateNewProductWithoutDescription() throws Exception {
        ProductDTO newProduct = new ProductDTOBuilder()
                .title(TITLE)
                .price(PRICE)
                .build();

        mockMvc.perform(post("/api/product")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newProduct))
        );

        ArgumentCaptor<ProductDTO> createdArgument = ArgumentCaptor.forClass(ProductDTO.class);
        verify(service, times(1)).create(createdArgument.capture());
        verifyNoMoreInteractions(service);

        ProductDTO created = createdArgument.getValue();
        assertThatProductDTO(created)
                .hasNoId()
                .hasTitle(TITLE)
                .hasNoDescription();
    }

    @Test
    public void create_ProductWithOnlyTitle_ShouldReturnResponseStatusCreated() throws Exception {
        ProductDTO newProduct = new ProductDTOBuilder()
                .title(TITLE)
                .price(PRICE)
                .build();

        when(service.create(isA(ProductDTO.class))).then(invocationOnMock -> {
            ProductDTO saved = (ProductDTO) invocationOnMock.getArguments()[0];
            saved.setId(ID);
            return saved;
        });

        mockMvc.perform(post("/api/product")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newProduct))
        )
                .andExpect(status().isCreated());
    }

    @Test
    public void create_ProductWithOnlyTitle_ShouldReturnTheInformationOfCreatedProductAsJSon() throws Exception {
        ProductDTO newProduct = new ProductDTOBuilder()
                .title(TITLE)
                .price(PRICE)
                .build();

        when(service.create(isA(ProductDTO.class))).then(invocationOnMock -> {
            ProductDTO saved = (ProductDTO) invocationOnMock.getArguments()[0];
            saved.setId(ID);
            return saved;
        });

        mockMvc.perform(post("/api/product")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newProduct))
        )
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.title", is(TITLE)))
                .andExpect(jsonPath("$.description", isEmptyOrNullString()))
                .andExpect(jsonPath("$.type", isEmptyOrNullString()));
    }

    @Test
    public void create_ProductWithMaxLengthTitleAndDescription_ShouldCreateNewProductWithCorrectInformation() throws Exception {
        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);

        ProductDTO newProduct = new ProductDTOBuilder()
                .title(maxLengthTitle)
                .description(maxLengthDescription)
                .price(PRICE)
                .build();

        mockMvc.perform(post("/api/product")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newProduct))
        );

        ArgumentCaptor<ProductDTO> createdArgument = ArgumentCaptor.forClass(ProductDTO.class);
        verify(service, times(1)).create(createdArgument.capture());
        verifyNoMoreInteractions(service);

        ProductDTO created = createdArgument.getValue();
        assertThatProductDTO(created)
                .hasNoId()
                .hasTitle(maxLengthTitle)
                .hasDescription(maxLengthDescription);
    }

    @Test
    public void create_ProductWithMaxLengthTitleAndDescription_ShouldReturnResponseStatusCreated() throws Exception {
        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);

        ProductDTO newProduct = new ProductDTOBuilder()
                .title(maxLengthTitle)
                .description(maxLengthDescription)
                .price("100")
                .build();

        when(service.create(isA(ProductDTO.class))).then(invocationOnMock -> {
            ProductDTO saved = (ProductDTO) invocationOnMock.getArguments()[0];
            saved.setId(ID);
            return saved;
        });

        mockMvc.perform(post("/api/product")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newProduct))
        )
                .andExpect(status().isCreated());
    }

    @Test
    public void create_ProductWithMaxLengthTitleAndDescription_ShouldReturnTheInformationOfCreatedProductAsJson() throws Exception {
        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
        String price = "Price";
        String type = "Type1";

        ProductDTO newProduct = new ProductDTOBuilder()
                .title(maxLengthTitle)
                .description(maxLengthDescription)
                .price(price)
                .type(type)
                .build();

        when(service.create(isA(ProductDTO.class))).then(invocationOnMock -> {
            ProductDTO saved = (ProductDTO) invocationOnMock.getArguments()[0];
            saved.setId(ID);
            return saved;
        });

        mockMvc.perform(post("/api/product")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(newProduct))
        )
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.title", is(maxLengthTitle)))
                .andExpect(jsonPath("$.description", is(maxLengthDescription)))
                .andExpect(jsonPath("$.price", is(price)))
                .andExpect(jsonPath("$.type", is(type)));
    }

    @Test
    public void delete_ProductNotFound_ShouldReturnResponseStatusNotFound() throws Exception {
        when(service.delete(ID)).thenThrow(new ProductNotFoundException(ID));

        mockMvc.perform(delete("/api/product/deleteproduct/{id}", ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void delete_ProductFound_ShouldReturnResponseStatusOk() throws Exception {
        ProductDTO deleted = new ProductDTOBuilder()
                .id(ID)
                .build();

        when(service.delete(ID)).thenReturn(deleted);

        mockMvc.perform(delete("/api/product/deleteproduct/{id}", ID))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_ProductFound_ShouldTheInformationOfDeletedProductAsJson() throws Exception {
        ProductDTO deleted = new ProductDTOBuilder()
                .id(ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .type(TYPE)
                .build();

        when(service.delete(ID)).thenReturn(deleted);

        mockMvc.perform(delete("/api/product/deleteproduct/{id}", ID))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.title", is(TITLE)))
                .andExpect(jsonPath("$.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.price", is(PRICE)))
                .andExpect(jsonPath("$.type", is(TYPE)));
    }

    @Test
    public void findAll_ShouldReturnResponseStatusOk() throws Exception {
        mockMvc.perform(get("/api/product"))
                .andExpect(status().isOk());
    }

    @Test
    public void findAll_OneProductFound_ShouldReturnListThatContainsOneProductAsJson() throws Exception {
        ProductDTO found = new ProductDTOBuilder()
                .id(ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .type(TYPE)
                .build();

        when(service.findAll()).thenReturn(Arrays.asList(found));

        mockMvc.perform(get("/api/product"))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(ID)))
                .andExpect(jsonPath("$[0].title", is(TITLE)))
                .andExpect(jsonPath("$[0].description", is(DESCRIPTION)))
                .andExpect(jsonPath("$[0].price", is(PRICE)))
                .andExpect(jsonPath("$[0].type", is(TYPE)));
    }

    @Test
    public void findById_ProductFound_ShouldReturnResponseStatusOk() throws Exception {
        ProductDTO found = new ProductDTOBuilder().build();

        when(service.findById(ID)).thenReturn(found);

        mockMvc.perform(get("/api/product/{id}", ID))
                .andExpect(status().isOk());
    }

    @Test
    public void findById_ProductFound_ShouldTheInformationOfFoundProductAsJson() throws Exception {
        ProductDTO found = new ProductDTOBuilder()
                .id(ID)
                .title(TITLE)
                .description(DESCRIPTION)
                .price(PRICE)
                .type(TYPE)
                .build();

        when(service.findById(ID)).thenReturn(found);

        mockMvc.perform(get("/api/price/{id}", ID))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.title", is(TITLE)))
                .andExpect(jsonPath("$.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.price", is(PRICE)))
                .andExpect(jsonPath("$.type", is(TYPE)));
    }

    @Test
    public void findById_ProductNotFound_ShouldReturnResponseStatusNotFound() throws Exception {
        when(service.findById(ID)).thenThrow(new ProductNotFoundException(ID));

        mockMvc.perform(get("/api/product/{id}", ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void update_ProductWithOnlyTitle_ShouldUpdateTheInformationOfProduct() throws Exception {
        ProductDTO updatedProduct = new ProductDTOBuilder()
                .id(ID)
                .title(TITLE)
                .price(PRICE)
                .type(TYPE)
                .build();

        mockMvc.perform(put("/api/product/{id}", ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedProduct))
        );

        ArgumentCaptor<ProductDTO> updatedArgument = ArgumentCaptor.forClass(ProductDTO.class);
        verify(service, times(1)).update(updatedArgument.capture());
        verifyNoMoreInteractions(service);

        ProductDTO updated = updatedArgument.getValue();
        assertThatProductDTO(updated)
                .hasId(ID)
                .hasTitle(TITLE)
                .hasNoDescription();
    }

    @Test
    public void update_ProductWithOnlyTitle_ShouldReturnResponseStatusOk() throws Exception {
        ProductDTO updatedProduct = new ProductDTOBuilder()
                .id(ID)
                .title(TITLE)
                .price(PRICE)
                .build();

        when(service.update(isA(ProductDTO.class))).then(invocationOnMock -> (ProductDTO) invocationOnMock.getArguments()[0]);

        mockMvc.perform(put("/api/product/{id}", ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedProduct))
        )
                .andExpect(status().isOk());
    }

    @Test
    public void update_ProductWithOnlyTitle_ShouldReturnTheInformationOfUpdatedProductAsJSon() throws Exception {
        ProductDTO updatedProduct = new ProductDTOBuilder()
                .id(ID)
                .title(TITLE)
                .price(PRICE)
                .build();

        when(service.update(isA(ProductDTO.class))).then(invocationOnMock ->  (ProductDTO) invocationOnMock.getArguments()[0]);

        mockMvc.perform(put("/api/product/{id}", ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedProduct))
        )
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.title", is(TITLE)))
                .andExpect(jsonPath("$.description", isEmptyOrNullString()))
                .andExpect(jsonPath("$.price", is(PRICE)))
                .andExpect(jsonPath("$.type", isEmptyOrNullString()));
    }

    @Test
    public void update_ProductWithMaxLengthTitleAndDescription_ShouldUpdateTheInformationOfProduct() throws Exception {
        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
        String price = "price";
        String type = "type1";

        ProductDTO updatedProduct = new ProductDTOBuilder()
                .id(ID)
                .title(maxLengthTitle)
                .description(maxLengthDescription)
                .price(price)
                .type(type)
                .build();

        mockMvc.perform(put("/api/product/{id}", ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedProduct))
        );

        ArgumentCaptor<ProductDTO> updatedArgument = ArgumentCaptor.forClass(ProductDTO.class);
        verify(service, times(1)).update(updatedArgument.capture());
        verifyNoMoreInteractions(service);

        ProductDTO updated = updatedArgument.getValue();
        assertThatProductDTO(updated)
                .hasId(ID)
                .hasTitle(maxLengthTitle)
                .hasDescription(maxLengthDescription)
                .hasPrice(price)
                .hasType(type);
    }

    @Test
    public void update_ProductWithMaxLengthTitleAndDescription_ShouldReturnResponseStatusOk() throws Exception {
        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
        String price = "price";
        String type = "type1";

        ProductDTO updatedProduct = new ProductDTOBuilder()
                .id(ID)
                .title(maxLengthTitle)
                .description(maxLengthDescription)
                .price(price)
                .type(type)
                .build();

        when(service.create(isA(ProductDTO.class))).then(invocationOnMock -> (ProductDTO) invocationOnMock.getArguments()[0]);

        mockMvc.perform(put("/api/product/{id}", ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedProduct))
        )
                .andExpect(status().isOk());
    }

    @Test
    public void update_ProductWithMaxLengthTitleAndDescription_ShouldReturnTheInformationOfCreatedUpdatedProductAsJson() throws Exception {
        String maxLengthTitle = StringTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
        String maxLengthDescription = StringTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
        String price = "price";
        String type = "type1";

        ProductDTO updatedProduct = new ProductDTOBuilder()
                .id(ID)
                .title(maxLengthTitle)
                .description(maxLengthDescription)
                .price(price)
                .type(type)
                .build();

        when(service.update(isA(ProductDTO.class))).then(invocationOnMock -> (ProductDTO) invocationOnMock.getArguments()[0]);

        mockMvc.perform(put("/api/product/{id}", ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(updatedProduct))
        )
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(ID)))
                .andExpect(jsonPath("$.title", is(maxLengthTitle)))
                .andExpect(jsonPath("$.description", is(maxLengthDescription)))
                .andExpect(jsonPath("$.price", is(price)))
                .andExpect(jsonPath("$.type", is(type)));
    }
}
