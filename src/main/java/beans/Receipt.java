package beans;

import adapters.Convertable;
import entities.ReceiptEntity;
import entities.builders.ReceiptEntityBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by u624 on 3/22/17.
 */
public class Receipt implements Serializable, Convertable<ReceiptEntity> {
    private Long id;
    private Product product;
    private BigDecimal price;
    private Long quantity;
    private BigDecimal total;
    private String date;

    public Receipt() {
        /* default constructor */
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public ReceiptEntity convert() {
        return new ReceiptEntityBuilder()
                .setId(id)
                .setProductEntity(Objects.isNull(product) ? null : product.convert())
                .setPrice(price)
                .setQuantity(quantity)
                .setTotal(total)
                .setDate(date)
                .build();
    }
}
