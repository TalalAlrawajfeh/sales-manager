package entities;

import adapters.Convertable;
import beans.Receipt;
import beans.builders.ReceiptBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by u624 on 3/22/17.
 */
@Entity
@Table(name = "receipts")
public class ReceiptEntity implements Serializable, Convertable<Receipt> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RECEIPT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRODUCT")
    private ProductEntity productEntity;

    @Column(name = "PRODUCT_PRICE")
    private BigDecimal price;

    @Column(name = "PRODUCT_QUANTITY")
    private Long quantity;

    @Column(name = "RECEIPT_TOTAL")
    private BigDecimal total;

    @Column(name = "RECEIPT_DATE")
    private String date;

    public ReceiptEntity() {
        /* default constructor */
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
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
    public Receipt convert() {
        return new ReceiptBuilder()
                .setId(id)
                .setProduct(Objects.isNull(productEntity) ? null : productEntity.convert())
                .setPrice(price)
                .setQuantity(quantity)
                .setTotal(total)
                .setDate(date)
                .build();
    }
}
