package interactors;

import adapters.UseCase;
import beans.Receipt;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ReceiptRepository;

/**
 * Created by u624 on 3/25/17.
 */
public class DeleteReceiptUseCase implements UseCase<Receipt> {
    @Autowired
    private ReceiptRepository receiptRepository;

    @Override
    public void execute(Receipt receipt) throws UseCaseException {
        receiptRepository.delete(receipt.convert());
    }
}
