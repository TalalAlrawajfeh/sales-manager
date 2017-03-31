package controllers;

import adapters.UseCase;
import beans.Pair;
import beans.Product;
import beans.Receipt;
import beans.builders.ProductBuilder;
import beans.builders.ReceiptBuilder;
import exceptions.UseCaseException;
import interactors.AddReceiptUseCase;
import interactors.DeleteReceiptUseCase;
import interactors.ListAllProductsUseCase;
import interactors.ListAllReceiptsUseCase;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import utilities.DateUtility;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by u624 on 3/25/17.
 */
@Controller
public class ReceiptsController {
    private static final String MANAGEMENT_RECEIPTS_VIEW_NAME = "management-receipts";
    private static final String SHOW_ERROR_ATTRIBUTE_NAME = "showError";
    private static final String RECEIPTS_ATTRIBUTE_NAME = "receipts";
    private static final String PRODUCTS_ATTRIBUTE_NAME = "products";
    private static final String DELETE_RECEIPT_URL = "/delete-receipt";
    private static final String RECEIPTS_URL = "/receipts";
    private static final String EDIT_ACTION = "edit";
    private static final String ADD_ACTION = "add";

    private final Logger logger = Logger.getLogger(ReceiptsController.class);

    @Autowired
    private ListAllReceiptsUseCase listAllReceiptsUseCase;

    @Autowired
    private ListAllProductsUseCase listAllProductsUseCase;

    @Autowired
    private AddReceiptUseCase addReceiptUseCase;

    @Autowired
    private DeleteReceiptUseCase deleteReceiptUseCase;

    private UseCase<Pair<List<Receipt>, List<Product>>> listAllReceiptsAndProductsUseCase = p -> {
        listAllReceiptsUseCase.execute(p.getFirst());
        listAllProductsUseCase.execute(p.getSecond());
    };

    private Map<String, UseCase<Pair<Long, Receipt>>> actionUseCaseMap = new HashMap<>();

    public ReceiptsController() {
        actionUseCaseMap.put(ADD_ACTION, p -> {
            p.getSecond().setDate(DateUtility.formatDate(new Date()));
            addReceiptUseCase.execute(p.getSecond());
        });
        actionUseCaseMap.put(EDIT_ACTION, p -> {
            Receipt receipt = p.getSecond();
            receipt.setId(p.getFirst());
            addReceiptUseCase.execute(receipt);
        });
    }

    @RequestMapping(path = RECEIPTS_URL, method = RequestMethod.GET)
    public ModelAndView getReceiptsModelAndView() {
        List<Receipt> receipts = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put(RECEIPTS_ATTRIBUTE_NAME, receipts);
        modelMap.put(PRODUCTS_ATTRIBUTE_NAME, products);
        return new UseCaseModelAndViewBuilder<Pair<List<Receipt>, List<Product>>>()
                .setUseCase(listAllReceiptsAndProductsUseCase)
                .setUseCaseParameter(new Pair<>(receipts, products))
                .setSuccessViewName(MANAGEMENT_RECEIPTS_VIEW_NAME)
                .setErrorViewName(MANAGEMENT_RECEIPTS_VIEW_NAME)
                .setModelMap(modelMap)
                .executeUseCaseAndBuild();
    }

    @RequestMapping(path = RECEIPTS_URL, method = RequestMethod.POST)
    public ModelAndView addReceipt(@RequestParam String action, @RequestParam Long receiptId, @RequestParam String code, @RequestParam BigDecimal price, @RequestParam Long quantity, @RequestParam BigDecimal total) {
        ModelAndView modelAndView = new UseCaseModelAndViewBuilder<Pair<Long, Receipt>>()
                .setUseCase(actionUseCaseMap.get(action))
                .setUseCaseParameter(new Pair<>(receiptId, new ReceiptBuilder()
                        .setProduct(new ProductBuilder().setCode(code).build())
                        .setPrice(price)
                        .setQuantity(quantity)
                        .setTotal(total)
                        .build()))
                .setSuccessViewName(MANAGEMENT_RECEIPTS_VIEW_NAME)
                .setErrorViewName(MANAGEMENT_RECEIPTS_VIEW_NAME)
                .executeUseCaseAndBuild();
        addReceiptsAndProductsToModelAndView(modelAndView);
        return modelAndView;
    }

    @RequestMapping(path = DELETE_RECEIPT_URL, method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> deleteReceipt(@RequestParam Long receiptId) {
        try {
            deleteReceiptUseCase.execute(new ReceiptBuilder().setId(receiptId).build());
        } catch (UseCaseException e) {
            logger.debug(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addReceiptsAndProductsToModelAndView(ModelAndView modelAndView) {
        Map<String, Object> receiptsModel = getReceiptsModelAndView().getModel();
        if (!(boolean) receiptsModel.get(SHOW_ERROR_ATTRIBUTE_NAME)) {
            Map<String, Object> model = modelAndView.getModel();
            model.put(RECEIPTS_ATTRIBUTE_NAME,
                    receiptsModel.get(RECEIPTS_ATTRIBUTE_NAME));
            model.put(PRODUCTS_ATTRIBUTE_NAME,
                    receiptsModel.get(PRODUCTS_ATTRIBUTE_NAME));
        }
    }
}
