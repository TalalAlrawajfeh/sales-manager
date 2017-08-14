package interactors;

import beans.Product;
import beans.builders.ProductBuilder;
import entities.ProductEntity;
import exceptions.UseCaseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import persistence.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by u624 on 4/1/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class AddProductsUseCaseTests {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private AddProductUseCase addProductUseCase = new AddProductUseCase();

    private Product product;

    @Before
    public void setup() {
        Mockito.doReturn(null)
                .when(productRepository)
                .findByCode(Matchers.anyString());
        product = getValidProduct();
    }

    @Test(expected = UseCaseException.class)
    public void GivenProductWithInvalidCodeThenUseCaseExceptionShouldBeThrown() throws Exception {
        product.setCode("1 23");
        addProductUseCase.execute(product);
    }

    @Test(expected = UseCaseException.class)
    public void GivenProductWithInvalidDescriptionThenUseCaseExceptionShouldBeThrown() throws Exception {
        product.setDescription("###");
        addProductUseCase.execute(product);
    }

    @Test(expected = UseCaseException.class)
    public void GivenProductWithNoPriceThenUseCaseExceptionShouldBeThrown() throws Exception {
        product.setPrice(null);
        addProductUseCase.execute(product);
    }

    @Test(expected = UseCaseException.class)
    public void GivenProductWithNoQuantityRemainingThenUseCaseExceptionShouldBeThrown() throws Exception {
        product.setQuantityRemaining(null);
        addProductUseCase.execute(product);
    }

    @Test
    public void GivenProductWithNoQuantitySoldThenNoExceptionShouldBeThrown() throws Exception {
        addProductUseCase.execute(product);
    }

    @Test(expected = UseCaseException.class)
    public void GivenProductWithCodeThatAlreadyExistsThenUseCaseExceptionShouldBeThrown() throws Exception {
        ProductEntity productEntity = product.convert();
        productEntity.setQuantitySold(0L);
        Mockito.doReturn(productEntity)
                .when(productRepository)
                .findByCode("123");
        addProductUseCase.execute(product);
    }

    @Test
    public void GivenAnyProductThenQuantitySoldMustBeSetToZeroBeforePersisted() throws Exception {
        List<ProductEntity> productEntities = new ArrayList<>();
        Mockito.doAnswer(invocationOnMock -> {
            productEntities.add((ProductEntity) invocationOnMock.getArguments()[0]);
            return null;
        }).when(productRepository).save(Matchers.<ProductEntity>any());
        product.setQuantitySold(5L);
        addProductUseCase.execute(product);
        ProductEntity productEntity = product.convert();
        productEntity.setQuantitySold(0L);
        assertTrue(productEntity.equals(productEntities.get(0)));
    }

    @Test
    public void GivenValidProductThenExactlyOneProductEntityShouldBePersisted() throws Exception {
        List<ProductEntity> productEntities = new ArrayList<>();
        Mockito.doAnswer(invocationOnMock -> {
            productEntities.add((ProductEntity) invocationOnMock.getArguments()[0]);
            return null;
        }).when(productRepository).save(Matchers.<ProductEntity>any());
        addProductUseCase.execute(product);
        assertEquals(1, productEntities.size());
    }

    public Product getValidProduct() {
        return new ProductBuilder()
                .setCode("123")
                .setDescription("ABC")
                .setPrice(BigDecimal.valueOf(1.5))
                .setQuantityRemaining(0L)
                .build();
    }
}
