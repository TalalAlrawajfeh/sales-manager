package interactors;

import adapters.UseCase;
import beans.Receipt;
import constants.PaginationUseCasesParameters;
import entities.ReceiptEntity;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.ReceiptRepository;
import utilities.DateUtility;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by u624 on 3/30/17.
 */
public class ListReceiptsUseCase implements UseCase<Map<PaginationUseCasesParameters, Object>> {
    private static final long PAGE_SIZE = 50;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Override
    public void execute(Map<PaginationUseCasesParameters, Object> paginationUseCasesParameters) throws UseCaseException {
        Iterable<ReceiptEntity> receipts = receiptRepository.findAll();
        long receiptsCount = StreamSupport.stream(receipts.spliterator(), true).count();

        paginationUseCasesParameters.put(PaginationUseCasesParameters.TOTAL_NUMBER_OF_PAGES,
                (int) ((float) receiptsCount / (float) PAGE_SIZE));

        List<ReceiptEntity> receiptsList = getList(receipts);
        sortReceiptsList(receiptsList);

        paginationUseCasesParameters.put(PaginationUseCasesParameters.PAGE_ITEMS_LIST,
                getReceiptsPage(receiptsCount,
                        (Integer) paginationUseCasesParameters.get(PaginationUseCasesParameters.PAGE_NUMBER),
                        receiptsList));
    }

    private List<Receipt> getReceiptsPage(long receiptsCount, Integer pageNumber, List<ReceiptEntity> receiptsList) {
        List<Receipt> receiptsPage = new ArrayList<>();
        for (int i = (pageNumber - 1) * 50; i < pageNumber * 50 && i < receiptsCount; i++) {
            receiptsPage.add(receiptsList.get(i).convert());
        }
        return receiptsPage;
    }

    private void sortReceiptsList(List<ReceiptEntity> receiptsList) {
        Collections.sort(receiptsList, Comparator.comparing(r -> DateUtility.parseDate(r.getDate())));
    }

    private <T> List<T> getList(Iterable<T> receipts) {
        return StreamSupport.stream(receipts.spliterator(), true)
                .collect(Collectors.toList());
    }
}
