package beans;

import adapters.Convertable;
import entities.ProductEntity;
import entities.builders.ProductEntityBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by u624 on 3/22/17.
 */
public class Product implements Serializable, Convertable<ProductEntity> {
    private String code;
    private String description;
    private BigDecimal price;
    private Long quantityRemaining;
    private Long quantitySold;

    public Product() {
        /* default constructor */
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantityRemaining() {
        return quantityRemaining;
    }

    public void setQuantityRemaining(Long quantityRemaining) {
        this.quantityRemaining = quantityRemaining;
    }

    public Long getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Long quantitySold) {
        this.quantitySold = quantitySold;
    }

    @Override
    public ProductEntity convert() {
        return new ProductEntityBuilder()
                .setCode(code)
                .setDescription(description)
                .setPrice(price)
                .setQuantityRemaining(quantityRemaining)
                .setQuantitySold(quantitySold)
                .build();
    }
}
