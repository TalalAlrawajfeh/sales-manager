package interactors;

import adapters.UseCase;
import beans.Receipt;
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
 * Created by u624 on 3/31/17.
 */
public class EditReceiptUseCase implements UseCase<Receipt> {
    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ProductRepository productRepository;

    private Map<Predicate<Receipt>, String> receiptsValidationsMessagesMap = new HashMap<>();

    public EditReceiptUseCase() {
        receiptsValidationsMessagesMap.put(r -> Objects.nonNull(r.getProduct().getCode()) && r.getProduct().getCode().matches("[A-Z0-9]+"),
                "The code field is not valid");
        receiptsValidationsMessagesMap.put(r -> Objects.nonNull(r.getPrice()),
                "The price field is not valid");
        receiptsValidationsMessagesMap.put(r -> Objects.nonNull(r.getQuantity()),
                "The quantity field is not valid");
        receiptsValidationsMessagesMap.put(r -> Objects.nonNull(productRepository.findByCode(r.getProduct().getCode())),
                "Product doesn't exist");
        receiptsValidationsMessagesMap.put(r -> productRepository.findByCode(r.getProduct().getCode()).getQuantityRemaining() + receiptRepository.findById(r.getId()).getQuantity() >= r.getQuantity(),
                "Product quantity remaining is not sufficient");
        receiptsValidationsMessagesMap.put(r -> Objects.isNull(r.getId()) || Objects.nonNull(receiptRepository.findById(r.getId())),
                "Invalid ID or receipt doesn't exist");
    }

    @Override
    public void execute(Receipt receipt) throws UseCaseException {
        validateReceipt(receipt);
        Receipt oldReceipt = receiptRepository.findById(receipt.getId()).convert();
        receiptRepository.save(receipt.convert());
        updateProduct(oldReceipt, receipt);
    }

    private void updateProduct(Receipt oldReceipt, Receipt newReceipt) {
        ProductEntity productEntity = productRepository.findByCode(oldReceipt.getProduct().getCode());
        Long oldQuantity = oldReceipt.getQuantity();
        Long newQuantity = newReceipt.getQuantity();
        productEntity.setQuantityRemaining(productEntity.getQuantityRemaining() + oldQuantity - newQuantity);
        productEntity.setQuantitySold(productEntity.getQuantitySold() - oldQuantity + newQuantity);
        productRepository.save(productEntity);
    }

    private void validateReceipt(Receipt receipt) throws UseCaseException {
        Optional<Map.Entry<Predicate<Receipt>, String>> entry = receiptsValidationsMessagesMap.entrySet()
                .stream()
                .filter(e -> !e.getKey().test(receipt))
                .findAny();
        if (entry.isPresent()) {
            throw new UseCaseException(entry.get().getValue());
        }
    }
}
