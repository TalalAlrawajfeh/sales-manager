package interactors;

import adapters.UseCase;
import beans.Product;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ProductRepository;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Created by u624 on 3/25/17.
 */
public class ListAllProductsUseCase implements UseCase<List<Product>> {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void execute(List<Product> products) throws UseCaseException {
        StreamSupport.stream(productRepository.findAll().spliterator(), true)
                .forEach(p -> products.add(p.convert()));
    }
}
