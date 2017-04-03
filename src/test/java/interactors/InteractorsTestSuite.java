package interactors;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by u624 on 4/1/17.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AddProductsUseCaseTests.class,
        AddReceiptsUseCaseTests.class,
        DeleteProductUseCaseTests.class,
        DeleteReceiptsUseCaseTests.class,
        EditProductUseCaseTests.class,
        ListReceiptsUseCaseTests.class
})
public class InteractorsTestSuite {
}
