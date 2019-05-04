package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neostencil.common.enums.ProductType;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ns_products")
public class Product extends DomainObject {

  @GeneratedValue(strategy = GenerationType.TABLE, generator = "ns_product_gen")
  @TableGenerator(name = "ns_product_gen", table = "ns_product_id_gen", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "ProductId_Gen", initialValue = 600, allocationSize = 1)
  @Id
  @Column(name = "product_id")
  int id;

  @Column(name = "title")
  String productTitle;

  @Column(name = "product_slug")
  String productSlug;

  @Column(name = "commodity_id")
  int commodityId;

  @Enumerated(EnumType.STRING)
  @Column(name = "product_type")
  ProductType type;

  @Column(name = "price")
  double price;
  
  @Column(name="regular_price")
  double regularPrice;

  @Column(name = "image_url")
  private String imageUrl;

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the type
   */
  public ProductType getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(ProductType type) {
    this.type = type;
  }

  /**
   * @return the commodityId
   */
  public int getCommodityId() {
    return commodityId;
  }

  /**
   * @param commodityId the commodityId to set
   */
  public void setCommodityId(int commodityId) {
    this.commodityId = commodityId;
  }

  public String getProductSlug() {
    return productSlug;
  }

  public void setProductSlug(String productSlug) {
    this.productSlug = productSlug;
  }

  public String getProductTitle() {
    return productTitle;
  }

  public void setProductTitle(String productTitle) {
    this.productTitle = productTitle;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Product product = (Product) o;
    return id == product.id;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  @Override
  public int hashCode() {

    return Objects.hash(id);
  }

  /**
   * @return imageUrl
   */
  public String getImageUrl() {
    return imageUrl;
  }

  /**
   *
   * @param imageUrl
   */
  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Product [productTitle=" + productTitle + "| commodityId=" + commodityId + "| type="
        + type + "| price="
        + price + "]";
  }

  /**
   * @return the regularPrice
   */
  public double getRegularPrice() {
    return regularPrice;
  }

  /**
   * @param regularPrice the regularPrice to set
   */
  public void setRegularPrice(double regularPrice) {
    this.regularPrice = regularPrice;
  }

}
