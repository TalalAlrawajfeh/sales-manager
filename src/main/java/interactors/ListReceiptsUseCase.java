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
    private static final int PAGE_SIZE = 3; //TODO:RESET TO 50

    @Autowired
    private ReceiptRepository receiptRepository;

    @Override
    public void execute(Map<PaginationUseCasesParameters, Object> paginationUseCasesParameters) throws UseCaseException {
        Iterable<ReceiptEntity> receipts = receiptRepository.findAll();
        if (Objects.nonNull(receipts)) {
            long receiptsCount = StreamSupport.stream(receipts.spliterator(), true).count();
            setTotalNumberOfPages(paginationUseCasesParameters, receiptsCount);
            prepareAndSetPageItems(paginationUseCasesParameters, receipts, receiptsCount);
        } else {
            paginationUseCasesParameters.put(PaginationUseCasesParameters.TOTAL_NUMBER_OF_PAGES, 0);
        }
    }

    private void setTotalNumberOfPages(Map<PaginationUseCasesParameters, Object> paginationUseCasesParameters, long receiptsCount) {
        paginationUseCasesParameters.put(PaginationUseCasesParameters.TOTAL_NUMBER_OF_PAGES,
                getTotalNumberOfPages(receiptsCount));
    }

    private void prepareAndSetPageItems(Map<PaginationUseCasesParameters, Object> paginationUseCasesParameters, Iterable<ReceiptEntity> receipts, long receiptsCount) {
        List<ReceiptEntity> receiptsList = getList(receipts);
        receiptsList.sort(Comparator.comparingLong(r -> -DateUtility.parseDate(r.getDate()).getTime()));
        paginationUseCasesParameters.put(PaginationUseCasesParameters.PAGE_ITEMS_LIST,
                getReceiptsPage(receiptsCount, getPageNumber(paginationUseCasesParameters), receiptsList));
    }

    private Integer getPageNumber(Map<PaginationUseCasesParameters, Object> paginationUseCasesParameters) {
        return (Integer) paginationUseCasesParameters.get(PaginationUseCasesParameters.PAGE_NUMBER);
    }

    private int getTotalNumberOfPages(float receiptsCount) {
        return (int) Math.ceil(receiptsCount / (float) PAGE_SIZE);
    }

    private List<Receipt> getReceiptsPage(long receiptsCount, Integer pageNumber, List<ReceiptEntity> receiptsList) {
        List<Receipt> receiptsPage = new ArrayList<>();
        for (int i = (pageNumber - 1) * PAGE_SIZE; i < pageNumber * PAGE_SIZE && i < receiptsCount; i++) {
            receiptsPage.add(receiptsList.get(i).convert());
        }
        return receiptsPage;
    }

    private <T> List<T> getList(Iterable<T> receipts) {
        return StreamSupport.stream(receipts.spliterator(), true)
                .collect(Collectors.toList());
    }
}
