package interactors;

import beans.builders.ProductBuilder;
import entities.ProductEntity;
import entities.builders.ProductEntityBuilder;
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

    @Before
    public void setup() {
        Mockito.doReturn(null)
                .when(productRepository)
                .findByCode(Matchers.anyString());
    }

    @Test(expected = UseCaseException.class)
    public void GivenProductWithInvalidCodeThenUseCaseExceptionShouldBeThrown() throws Exception {
        addProductUseCase.execute(new ProductBuilder()
                .setCode("1 23")
                .setDescription("ABC")
                .setPrice(BigDecimal.valueOf(1.5))
                .setQuantityRemaining(0L)
                .setQuantitySold(0L)
                .build());
    }

    @Test(expected = UseCaseException.class)
    public void GivenProductWithInvalidDescriptionThenUseCaseExceptionShouldBeThrown() throws Exception {
        addProductUseCase.execute(new ProductBuilder()
                .setCode("123")
                .setDescription("###")
                .setPrice(BigDecimal.valueOf(1.5))
                .setQuantityRemaining(0L)
                .setQuantitySold(0L)
                .build());
    }

    @Test(expected = UseCaseException.class)
    public void GivenProductWithNoPriceThenUseCaseExceptionShouldBeThrown() throws Exception {
        addProductUseCase.execute(new ProductBuilder()
                .setCode("123")
                .setDescription("ABC")
                .setQuantityRemaining(0L)
                .setQuantitySold(0L)
                .build());
    }

    @Test(expected = UseCaseException.class)
    public void GivenProductWithNoQuantityRemainingThenUseCaseExceptionShouldBeThrown() throws Exception {
        addProductUseCase.execute(new ProductBuilder()
                .setCode("123")
                .setDescription("ABC")
                .setPrice(BigDecimal.valueOf(1.5))
                .setQuantitySold(0L)
                .build());
    }

    @Test
    public void GivenProductWithNoQuantitySoldThenNoExceptionShouldBeThrown() throws Exception {
        addProductUseCase.execute(new ProductBuilder()
                .setCode("123")
                .setDescription("ABC")
                .setPrice(BigDecimal.valueOf(1.5))
                .setQuantityRemaining(0L)
                .build());
    }

    @Test(expected = UseCaseException.class)
    public void GivenProductWithCodeThatAlreadyExistsThenUseCaseExceptionShouldBeThrown() throws Exception {
        Mockito.doReturn(new ProductEntityBuilder()
                .setCode("123")
                .setDescription("ABC")
                .setPrice(BigDecimal.valueOf(1.5))
                .setQuantityRemaining(0L)
                .setQuantitySold(0L)
                .build())
                .when(productRepository)
                .findByCode("123");
        addProductUseCase.execute(new ProductBuilder()
                .setCode("123")
                .setDescription("ABC")
                .setPrice(BigDecimal.valueOf(1.5))
                .setQuantityRemaining(0L)
                .setQuantitySold(0L)
                .build());
    }

    @Test
    public void GivenAnyProductThenQuantitySoldMustBeSetToZeroBeforePersisted() throws Exception {
        List<ProductEntity> productEntities = new ArrayList<>();
        Mockito.doAnswer(invocationOnMock -> {
            productEntities.add((ProductEntity) invocationOnMock.getArguments()[0]);
            return null;
        }).when(productRepository).save(Matchers.<ProductEntity>any());
        addProductUseCase.execute(new ProductBuilder()
                .setCode("123")
                .setDescription("ABC")
                .setPrice(BigDecimal.valueOf(1.5))
                .setQuantityRemaining(0L)
                .setQuantitySold(5L)
                .build());
        assertTrue(new ProductEntityBuilder()
                .setCode("123")
                .setDescription("ABC")
                .setPrice(BigDecimal.valueOf(1.5))
                .setQuantityRemaining(0L)
                .setQuantitySold(0L)
                .build()
                .equals(productEntities.get(0)));
    }

    @Test
    public void GivenAValidProductThenExactlyOneProductEntityShouldBePersisted() throws Exception {
        List<ProductEntity> productEntities = new ArrayList<>();
        Mockito.doAnswer(invocationOnMock -> {
            productEntities.add((ProductEntity) invocationOnMock.getArguments()[0]);
            return null;
        }).when(productRepository).save(Matchers.<ProductEntity>any());
        addProductUseCase.execute(new ProductBuilder()
                .setCode("123")
                .setDescription("ABC")
                .setPrice(BigDecimal.valueOf(1.5))
                .setQuantityRemaining(0L)
                .build());
        assertEquals(1, productEntities.size());
    }
}
