package controllers;

import beans.Receipt;
import constants.PaginationUseCasesParameters;
import interactors.ListReceiptsUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by u624 on 3/31/17.
 */
@Controller
public class SalesController {
    private static final int SECTION_LENGTH = 5;
    private static final String IS_LAST_SECTION_OF_PAGES_ATTRIBUTE = "isLastSectionOfPages";
    private static final String MANAGEMENT_SALES_VIEW_NAME = "management-sales";
    private static final String MIN_PAGE_NUMBER_ATTRIBUTE = "minPageNumber";
    private static final String MAX_PAGE_NUMBER_ATTRIBUTE = "maxPageNumber";
    private static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";
    private static final String SHOW_ERROR_ATTRIBUTE = "showError";
    private static final String RECEIPTS_ATTRIBUTE = "receipts";
    private static final String SALES_URL = "/sales";

    @Autowired
    private ListReceiptsUseCase listReceiptsUseCase;

    @RequestMapping(path = SALES_URL, method = RequestMethod.GET)
    public ModelAndView getSalesModelAndView(Integer pageNumber) {
        int page = getPageNumber(pageNumber);
        Map<PaginationUseCasesParameters, Object> paginationUseCasesParametersMap = new EnumMap<>(PaginationUseCasesParameters.class);
        paginationUseCasesParametersMap.put(PaginationUseCasesParameters.PAGE_NUMBER, page);
        ModelAndView modelAndView = new UseCaseModelAndViewBuilder<Map<PaginationUseCasesParameters, Object>>()
                .setUseCase(listReceiptsUseCase)
                .setUseCaseParameter(paginationUseCasesParametersMap)
                .setSuccessViewName(MANAGEMENT_SALES_VIEW_NAME)
                .setErrorViewName(MANAGEMENT_SALES_VIEW_NAME)
                .executeUseCaseAndBuild();
        addModelPaginationAttributes(page, paginationUseCasesParametersMap, modelAndView);
        return modelAndView;
    }

    private int getPageNumber(Integer pageNumber) {
        int page = 1;
        if (Objects.nonNull(pageNumber)) {
            page = pageNumber;
        }
        return page;
    }

    private void addModelPaginationAttributes(int pageNumber, Map<PaginationUseCasesParameters, Object> paginationUseCasesParametersMap, ModelAndView modelAndView) {
        int totalNumberOfPages = getTotalNumberOfPages(paginationUseCasesParametersMap);
        if (isShowErrorAttributeFalse(modelAndView) && 0 < totalNumberOfPages && pageNumber <= totalNumberOfPages) {
            Map<String, Object> model = modelAndView.getModel();
            int minPageNumber = getMinPageNumber(pageNumber);
            model.put(IS_LAST_SECTION_OF_PAGES_ATTRIBUTE, pageNumber >= getMinPageNumber(totalNumberOfPages));
            model.put(PAGE_NUMBER_ATTRIBUTE, pageNumber);
            model.put(MIN_PAGE_NUMBER_ATTRIBUTE, minPageNumber);
            model.put(MAX_PAGE_NUMBER_ATTRIBUTE, getMaxPageNumber(totalNumberOfPages, minPageNumber));
            model.put(RECEIPTS_ATTRIBUTE, getPageItemsList(paginationUseCasesParametersMap));
        }
    }

    private List<Receipt> getPageItemsList(Map<PaginationUseCasesParameters, Object> paginationUseCasesParametersMap) {
        return (List<Receipt>) paginationUseCasesParametersMap.get(PaginationUseCasesParameters.PAGE_ITEMS_LIST);
    }

    private int getTotalNumberOfPages(Map<PaginationUseCasesParameters, Object> paginationUseCasesParametersMap) {
        return (int) paginationUseCasesParametersMap.get(PaginationUseCasesParameters.TOTAL_NUMBER_OF_PAGES);
    }

    private int getMaxPageNumber(int totalNumberOfPages, int minPageNumber) {
        int maxPageNumber = minPageNumber + SECTION_LENGTH - 1;
        if (maxPageNumber > totalNumberOfPages) {
            maxPageNumber = totalNumberOfPages;
        }
        return maxPageNumber;
    }

    private int getMinPageNumber(int pageNumber) {
        return getPagesSection(pageNumber) * SECTION_LENGTH + 1;
    }

    private int getPagesSection(int pageNumber) {
        return (int) Math.floor((float) (pageNumber - 1) / (float) SECTION_LENGTH);
    }

    private boolean isShowErrorAttributeFalse(ModelAndView modelAndView) {
        return !(boolean) modelAndView.getModel().get(SHOW_ERROR_ATTRIBUTE);
    }
}
