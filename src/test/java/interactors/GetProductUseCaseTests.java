package interactors;

import constants.GetProductsUseCaseParameters;
import entities.ProductEntity;
import entities.builders.ProductEntityBuilder;
import exceptions.UseCaseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import persistence.ProductRepository;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Created by u624 on 4/3/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetProductUseCaseTests {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetProductUseCase getProductUseCase = new GetProductUseCase();

    private Map<GetProductsUseCaseParameters, Object> parametersMap;

    @Before
    public void setup() {
        parametersMap = new HashMap<>();
    }

    @Test(expected = UseCaseException.class)
    public void GivenParametersMapWithNoProductCodeThenUseCaseExceptionShouldBeThrown() throws Exception {
        parametersMap.put(GetProductsUseCaseParameters.PRODUCT_CODE, null);
        getProductUseCase.execute(parametersMap);
    }

    @Test(expected = UseCaseException.class)
    public void GivenParametersMapWithProductCodeThatDoesNotExistThenUseCaseExceptionShouldBeThrown() throws Exception {
        parametersMap.put(GetProductsUseCaseParameters.PRODUCT_CODE, "123");
        Mockito.doReturn(null).when(productRepository).findByCode("123");
        getProductUseCase.execute(parametersMap);
    }

    @Test
    public void GivenProductCodeThatExistsThenProductShouldBeSetInParametersMap() throws Exception {
        parametersMap.put(GetProductsUseCaseParameters.PRODUCT_CODE, "123");
        ProductEntity productEntity = new ProductEntityBuilder().setCode("123").build();
        Mockito.doReturn(productEntity).when(productRepository).findByCode("123");
        getProductUseCase.execute(parametersMap);
        assertEquals(productEntity.convert(), parametersMap.get(GetProductsUseCaseParameters.PRODUCT));
    }
}
