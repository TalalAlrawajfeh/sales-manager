package entities;

import adapters.Convertable;
import beans.Product;
import beans.builders.ProductBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by u624 on 3/22/17.
 */
@Entity
@Table(name = "products")
public class ProductEntity implements Serializable, Convertable<Product> {
    @Id
    @Column(name = "PRODUCT_CODE")
    private String code;

    @Column(name = "PRODUCT_DESCRIPTION")
    private String description;

    @Column(name = "PRODUCT_PRICE")
    private BigDecimal price;

    @Column(name = "QUANTITY_REMAINING")
    private Long quantityRemaining;

    @Column(name = "QUANTITY_SOLD")
    private Long quantitySold;

    public ProductEntity() {
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
    public Product convert() {
        return new ProductBuilder()
                .setCode(code)
                .setDescription(description)
                .setPrice(price)
                .setQuantityRemaining(quantityRemaining)
                .setQuantitySold(quantitySold)
                .build();
    }
}
