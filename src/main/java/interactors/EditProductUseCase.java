package interactors;

import adapters.UseCase;
import beans.Pair;
import beans.Product;
import entities.ProductEntity;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ProductRepository;
import persistence.ReceiptRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by u624 on 3/26/17.
 */
public class EditProductUseCase implements UseCase<Pair<String, Product>> {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    private ProductEntity productEntity;
    private Map<Predicate<Product>, String> productsValidationsMessagesMap = new HashMap<>();
    private Map<Predicate<Pair<String, Product>>, String> repositoryValidationsMessagesMap = new HashMap<>();

    public EditProductUseCase() {
        initializeProductsValidationsMessagesMap();
        initializeRepositoryValidationsMessagesMap();
    }

    @Override
    public void execute(Pair<String, Product> oldCodeProductPair) throws UseCaseException {
        Product product = oldCodeProductPair.getSecond();
        prepareAndValidateProduct(oldCodeProductPair, product);
        deleteOldProductEntity();
        productRepository.save(product.convert());
    }

    private void deleteOldProductEntity() throws UseCaseException {
        try {
            productRepository.delete(productEntity);
        } catch (Exception e) {
            throw new UseCaseException(e);
        }
    }

    private void prepareAndValidateProduct(Pair<String, Product> oldCodeProductPair, Product product)
            throws UseCaseException {
        product.setCode(product.getCode().toUpperCase());
        validateProduct(product);
        productEntity = productRepository.findByCode(oldCodeProductPair.getFirst());
        doRepositoryValidations(oldCodeProductPair);
        product.setQuantitySold(productEntity.getQuantitySold());
    }

    private void doRepositoryValidations(Pair<String, Product> oldCodeProductPair) throws UseCaseException {
        Optional<Map.Entry<Predicate<Pair<String, Product>>, String>> entry =
                repositoryValidationsMessagesMap.entrySet()
                        .stream()
                        .filter(e -> !e.getKey().test(oldCodeProductPair))
                        .findAny();
        if (entry.isPresent()) {
            throw new UseCaseException(entry.get().getValue());
        }
    }

    private void validateProduct(Product product) throws UseCaseException {
        Optional<Map.Entry<Predicate<Product>, String>> entry = productsValidationsMessagesMap.entrySet()
                .stream()
                .filter(e -> !e.getKey().test(product))
                .findAny();
        if (entry.isPresent()) {
            throw new UseCaseException(entry.get().getValue());
        }
    }


    private void initializeRepositoryValidationsMessagesMap() {
        repositoryValidationsMessagesMap.put(p -> Objects.nonNull(productEntity),
                "Product doesn't exist");
        repositoryValidationsMessagesMap.put(p -> receiptRepository.findByProductEntity(productEntity).isEmpty(),
                "There are receipts that depend on this product");
        repositoryValidationsMessagesMap.put(p -> p.getFirst().equals(p.getSecond().getCode())
                        || Objects.isNull(productRepository.findByCode(p.getSecond().getCode())),
                "Another product with the same code already exists");
    }

    private void initializeProductsValidationsMessagesMap() {
        productsValidationsMessagesMap.put(p -> Objects.nonNull(p.getCode())
                        && p.getCode().matches("[A-Z0-9]+"),
                "The code field is not valid");
        productsValidationsMessagesMap.put(p -> Objects.nonNull(p.getDescription())
                        && p.getCode().matches(".*[\\w]+.*"),
                "The code field is not valid");
        productsValidationsMessagesMap.put(p -> Objects.nonNull(p.getPrice()),
                "The price field is not valid");
        productsValidationsMessagesMap.put(p -> Objects.nonNull(p.getQuantityRemaining()),
                "The quantity field is not valid");
    }
}
