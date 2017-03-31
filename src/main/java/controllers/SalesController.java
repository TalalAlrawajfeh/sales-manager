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
    public static final int SECTION_LENGTH = 5;
    @Autowired
    private ListReceiptsUseCase listReceiptsUseCase;

    @RequestMapping(path = "/sales", method = RequestMethod.GET)
    public ModelAndView getSalesModelAndView(Integer pageNumber) {
        int page = 1;
        if (Objects.nonNull(pageNumber)) {
            page = pageNumber;
        }

        Map<PaginationUseCasesParameters, Object> paginationUseCasesParametersMap =
                new EnumMap<>(PaginationUseCasesParameters.class);
        paginationUseCasesParametersMap.put(PaginationUseCasesParameters.PAGE_NUMBER, page);

        ModelAndView modelAndView = new UseCaseModelAndViewBuilder<Map<PaginationUseCasesParameters, Object>>()
                .setUseCase(listReceiptsUseCase)
                .setUseCaseParameter(paginationUseCasesParametersMap)
                .setSuccessViewName("management-sales")
                .setErrorViewName("management-sales")
                .executeUseCaseAndBuild();

        if (addModelAttributes(page, paginationUseCasesParametersMap, modelAndView)) {
            return modelAndView;
        }

        return modelAndView;
    }

    private boolean addModelAttributes(int pageNumber, Map<PaginationUseCasesParameters, Object> paginationUseCasesParametersMap, ModelAndView modelAndView) {
        int totalNumberOfPages = getTotalNumberOfPages(paginationUseCasesParametersMap);
        if (0 == totalNumberOfPages) {
            return true;
        }
        modelAndView.getModel().put("pageNumber", pageNumber);
        int minPageNumber = getMinPageNumber(pageNumber);
        modelAndView.getModel().put("minPageNumber", minPageNumber);
        int maxPageNumber = minPageNumber + SECTION_LENGTH - 1;
        if (maxPageNumber > totalNumberOfPages) {
            maxPageNumber = totalNumberOfPages;
        }
        modelAndView.getModel().put("isLastSectionOfPages", pageNumber >= getMinPageNumber(totalNumberOfPages));
        modelAndView.getModel().put("maxPageNumber", maxPageNumber);
        modelAndView.getModel().put("receipts", getPageItemsList(paginationUseCasesParametersMap));
        return false;
    }

    private int getMinPageNumber(int pageNumber) {
        return getPagesSection(pageNumber) * SECTION_LENGTH + 1;
    }

    private List<Receipt> getPageItemsList(Map<PaginationUseCasesParameters, Object> paginationUseCasesParametersMap) {
        return (List<Receipt>) paginationUseCasesParametersMap.get(PaginationUseCasesParameters.PAGE_ITEMS_LIST);
    }

    private int getTotalNumberOfPages(Map<PaginationUseCasesParameters, Object> paginationUseCasesParametersMap) {
        return (int) paginationUseCasesParametersMap.get(PaginationUseCasesParameters.TOTAL_NUMBER_OF_PAGES);
    }

    private int getPagesSection(int pageNumber) {
        return (int) Math.floor((float) (pageNumber - 1) / (float) SECTION_LENGTH);
    }
}
