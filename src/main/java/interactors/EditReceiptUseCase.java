package interactors;

import adapters.UseCase;
import beans.Pair;
import beans.Receipt;
import entities.ProductEntity;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ProductRepository;
import persistence.ReceiptRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by u624 on 3/31/17.
 */
public class EditReceiptUseCase implements UseCase<Receipt> {
    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ProductRepository productRepository;

    private List<Pair<Predicate<Receipt>, String>> validations = new ArrayList<>();

    public EditReceiptUseCase() {
        validations.add(new Pair<>(r -> Objects.nonNull(r.getProduct().getCode()) && r.getProduct().getCode().matches("[A-Z0-9]+"),
                "The code field is not valid"));
        validations.add(new Pair<>(r -> Objects.nonNull(r.getPrice()),
                "The price field is not valid"));
        validations.add(new Pair<>(r -> Objects.nonNull(r.getQuantity()),
                "The quantity field is not valid"));
        validations.add(new Pair<>(r -> Objects.nonNull(productRepository.findByCode(r.getProduct().getCode())),
                "Product doesn't exist"));
        validations.add(new Pair<>(r -> productRepository.findByCode(r.getProduct().getCode()).getQuantityRemaining() >= r.getQuantity(),
                "Product quantity remaining is not sufficient"));
        validations.add(new Pair<>(r -> Objects.isNull(r.getId()) || Objects.nonNull(receiptRepository.findById(r.getId())),
                "Invalid ID or receipt doesn't exist"));
    }

    @Override
    public void execute(Receipt receipt) throws UseCaseException {
        validateReceipt(receipt);
        Receipt oldReceipt = receiptRepository.findById(receipt.getId()).convert();
        receipt.setDate(oldReceipt.getDate());
        receiptRepository.save(receipt.convert());
        updateProducts(oldReceipt, receipt);
    }

    private void updateProducts(Receipt oldReceipt, Receipt newReceipt) {
        updateOldProduct(oldReceipt);
        updateNewProduct(newReceipt);
    }

    private void updateNewProduct(Receipt newReceipt) {
        ProductEntity productEntity;
        productEntity = productRepository.findByCode(newReceipt.getProduct().getCode());
        Long newQuantity = newReceipt.getQuantity();
        productEntity.setQuantityRemaining(productEntity.getQuantityRemaining() - newQuantity);
        productEntity.setQuantitySold(productEntity.getQuantitySold() + newQuantity);
        productRepository.save(productEntity);
    }

    private void updateOldProduct(Receipt oldReceipt) {
        ProductEntity productEntity = productRepository.findByCode(oldReceipt.getProduct().getCode());
        Long oldQuantity = oldReceipt.getQuantity();
        productEntity.setQuantityRemaining(productEntity.getQuantityRemaining() + oldQuantity);
        productEntity.setQuantitySold(productEntity.getQuantitySold() - oldQuantity);
        productRepository.save(productEntity);
    }

    private void validateReceipt(Receipt receipt) throws UseCaseException {
        Optional<Pair<Predicate<Receipt>, String>> entry = validations
                .stream()
                .sequential()
                .filter(p -> !p.getFirst().test(receipt))
                .findAny();
        if (entry.isPresent()) {
            throw new UseCaseException(entry.get().getSecond());
        }
    }
}
