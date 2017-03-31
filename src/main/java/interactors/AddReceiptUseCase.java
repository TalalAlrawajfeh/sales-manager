package interactors;

import adapters.UseCase;
import beans.Receipt;
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
 * Created by u624 on 3/25/17.
 */
public class AddReceiptUseCase implements UseCase<Receipt> {
    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ProductRepository productRepository;

    private Map<Predicate<Receipt>, String> receiptsValidationsMessagesMap = new HashMap<>();

    public AddReceiptUseCase() {
        receiptsValidationsMessagesMap.put(r -> Objects.nonNull(r.getProduct().getCode()) && r.getProduct().getCode().matches("[A-Z0-9]+"),
                "The code field is not valid");
        receiptsValidationsMessagesMap.put(r -> Objects.nonNull(r.getPrice()),
                "The price field is not valid");
        receiptsValidationsMessagesMap.put(r -> Objects.nonNull(r.getQuantity()),
                "The quantity field is not valid");
        receiptsValidationsMessagesMap.put(r -> Objects.nonNull(productRepository.findByCode(r.getProduct().getCode())),
                "Product doesn't exist");
        receiptsValidationsMessagesMap.put(r -> Objects.isNull(r.getId()) || Objects.nonNull(receiptRepository.findById(r.getId())),
                "Invalid ID or receipt doesn't exist");
    }

    @Override
    public void execute(Receipt receipt) throws UseCaseException {
        validateReceipt(receipt);
        receiptRepository.save(receipt.convert());
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
