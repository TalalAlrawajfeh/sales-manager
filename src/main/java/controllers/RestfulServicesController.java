package controllers;

import beans.Product;
import beans.builders.ProductBuilder;
import beans.builders.ReceiptBuilder;
import constants.GetProductsUseCaseParameters;
import exceptions.UseCaseException;
import interactors.DeleteProductUseCase;
import interactors.DeleteReceiptUseCase;
import interactors.GetProductUseCase;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by u624 on 3/31/17.
 */
@Controller
public class RestfulServicesController {
    private static final String DELETE_RECEIPT_URL = "/delete-receipt";
    private static final String DELETE_PRODUCT_URL = "/delete-product";
    private static final String GET_PRODUCT_URL = "/get-product";

    private final Logger logger = Logger.getLogger(RestfulServicesController.class);

    @Autowired
    private DeleteReceiptUseCase deleteReceiptUseCase;

    @Autowired
    private DeleteProductUseCase deleteProductUseCase;

    @Autowired
    private GetProductUseCase getProductUseCase;

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

    @RequestMapping(path = GET_PRODUCT_URL, method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getProductPrice() {
        Map<GetProductsUseCaseParameters, Object> parametersMap = new EnumMap<>(GetProductsUseCaseParameters.class);
        try {
            getProductUseCase.execute(parametersMap);
        } catch (UseCaseException e) {
            logger.debug(e);
            return new ResponseEntity<>(new JSONObject(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(prepareResponse((Product) parametersMap.get(GetProductsUseCaseParameters.PRODUCT)),
                HttpStatus.OK);
    }

    private JSONObject prepareResponse(Product product) {
        JSONObject response = new JSONObject();
        response.put("code", product.getCode());
        response.put("description", product.getDescription());
        response.put("price", product.getPrice());
        response.put("quantityRemaining", product.getQuantityRemaining());
        response.put("quantitySold", product.getQuantitySold());
        return response;
    }
}