package controllers;

import adapters.UseCase;
import beans.Pair;
import beans.Product;
import beans.builders.ProductBuilder;
import exceptions.UseCaseException;
import interactors.AddProductUseCase;
import interactors.DeleteProductUseCase;
import interactors.EditProductUseCase;
import interactors.ListAllProductsUseCase;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by u624 on 3/25/17.
 */
@Controller
public class ProductsController {
    private static final String MANAGEMENT_PRODUCTS_VIEW_NAME = "management-products";
    private static final String SHOW_ERROR_ATTRIBUTE_NAME = "showError";
    private static final String PRODUCTS_ATTRIBUTE_NAME = "products";
    private static final String DELETE_PRODUCT_URL = "/delete-product";
    private static final String PRODUCTS_URL = "/products";
    private static final String EDIT_ACTION = "edit";
    private static final String ADD_ACTION = "add";

    private final Logger logger = Logger.getLogger(ProductsController.class);

    @Autowired
    private ListAllProductsUseCase listAllProductsUseCase;

    @Autowired
    private AddProductUseCase addProductUseCase;

    @Autowired
    private EditProductUseCase editProductUseCase;

    @Autowired
    private DeleteProductUseCase deleteProductUseCase;

    private Map<String, UseCase<Pair<String, Product>>> actionUseCaseMap = new HashMap<>();

    public ProductsController() {
        actionUseCaseMap.put(ADD_ACTION, p -> addProductUseCase.execute(p.getSecond()));
        actionUseCaseMap.put(EDIT_ACTION, p -> editProductUseCase.execute(p));
    }

    @RequestMapping(path = PRODUCTS_URL, method = RequestMethod.GET)
    public ModelAndView getProductsModelAndView() {
        List<Product> products = new ArrayList<>();
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put(PRODUCTS_ATTRIBUTE_NAME, products);
        return new UseCaseModelAndViewBuilder<List<Product>>()
                .setUseCase(listAllProductsUseCase)
                .setUseCaseParameter(products)
                .setSuccessViewName(MANAGEMENT_PRODUCTS_VIEW_NAME)
                .setErrorViewName(MANAGEMENT_PRODUCTS_VIEW_NAME)
                .setModelMap(modelMap)
                .executeUseCaseAndBuild();
    }

    @RequestMapping(path = PRODUCTS_URL, method = RequestMethod.POST)
    public ModelAndView addProduct(@RequestParam String action, @RequestParam String oldCode, @RequestParam String code, @RequestParam String description, @RequestParam BigDecimal price, @RequestParam Long quantity) {
        ModelAndView modelAndView = new UseCaseModelAndViewBuilder<Pair<String, Product>>()
                .setUseCase(actionUseCaseMap.get(action))
                .setUseCaseParameter(new Pair<>(oldCode, new ProductBuilder()
                        .setCode(code)
                        .setDescription(description)
                        .setPrice(price)
                        .setQuantityRemaining(quantity)
                        .build()))
                .setSuccessViewName(MANAGEMENT_PRODUCTS_VIEW_NAME)
                .setErrorViewName(MANAGEMENT_PRODUCTS_VIEW_NAME)
                .executeUseCaseAndBuild();
        addProductsToModelAndView(modelAndView);
        return modelAndView;
    }

    @RequestMapping(path = DELETE_PRODUCT_URL, method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> deleteProduct(@RequestParam String code) {
        try {
            deleteProductUseCase.execute(new ProductBuilder().setCode(code).build());
        } catch (UseCaseException e) {
            logger.debug(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addProductsToModelAndView(ModelAndView modelAndView) {
        Map<String, Object> model = getProductsModelAndView().getModel();
        if (!(boolean) model.get(SHOW_ERROR_ATTRIBUTE_NAME)) {
            modelAndView.getModel().put(PRODUCTS_ATTRIBUTE_NAME,
                    model.get(PRODUCTS_ATTRIBUTE_NAME));
        }
    }
}
