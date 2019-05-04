package com.neostencil.model.entities;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neostencil.common.enums.DiscountType;
import com.neostencil.common.enums.PublishStatus;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ns_coupons")
public class Coupon extends DomainObject {

  @GeneratedValue(strategy = GenerationType.TABLE, generator = "ns_coupon_gen")
  @TableGenerator(name = "ns_coupon_gen", table = "ns_coupon_id_gen", pkColumnName = "GEN_NAME",
      valueColumnName = "GEN_VAL", pkColumnValue = "coupon_Gen", initialValue = 30000,
      allocationSize = 100)
  @Id
  @Column(name = "coupon_id")
  int id;

  @ManyToMany
  @JoinTable(name = "ns_coupon_included_products", joinColumns = {@JoinColumn(name = "coupon_id")},
      inverseJoinColumns = {@JoinColumn(name = "product_id")})
  public Set<Product> includedProducts;

  @ManyToMany
  @JoinTable(name = "ns_coupon_excluded_products", joinColumns = {@JoinColumn(name = "coupon_id")},
      inverseJoinColumns = {@JoinColumn(name = "product_id")})
  public Set<Product> excludedProducts;

  @ManyToMany
  @JoinTable(name = "ns_coupon_allowed_users", joinColumns = {@JoinColumn(name = "coupon_id")},
      inverseJoinColumns = {@JoinColumn(name = "ID")})
  public Set<User> allowedUsers;


  @Column(name = "coupon_code",unique=true)
  String code;
  @Column(name = "description")
  String description;
  @Column(name = "amount")
  float amount;
  @Column(name = "discount_type")
  @Enumerated(EnumType.STRING)
  DiscountType discountType;
  @Column(name = "expiry_date")
  Date expiryDate;
  @Column(name = "minimum_spend")
  double minimumSpend;
  @Column(name = "discount_percentage")
  double discountPercentage;
  @Column(name = "coupon_status")
  @Enumerated(EnumType.STRING)
  PublishStatus status;
  @Column(name = "usage_limit_per_coupon")
  int usageLimitPerCoupon;
  @Column(name = "usage_limit_per_user")
  int usageLimitPerUser;
  @Column(name = "no_of_usages")
  int noOfUsages;

  public Coupon() {
    super();
    // TODO Auto-generated constructor stub
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  public DiscountType getDiscountType() {
    return discountType;
  }

  public void setDiscountType(DiscountType discountType) {
    this.discountType = discountType;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  public double getMinimumSpend() {
    return minimumSpend;
  }

  public void setMinimumSpend(double minimumSpend) {
    this.minimumSpend = minimumSpend;
  }

  public Set<Product> getIncludedProducts() {
    return includedProducts;
  }

  public void setIncludedProducts(Set<Product> includedProducts) {
    this.includedProducts = includedProducts;
  }

  public Set<Product> getExcludedProducts() {
    return excludedProducts;
  }

  public void setExcludedProducts(Set<Product> excludedProducts) {
    this.excludedProducts = excludedProducts;
  }

  public Set<User> getAllowedUsers() {
    return allowedUsers;
  }

  public void setAllowedUsers(Set<User> allowedUsers) {
    this.allowedUsers = allowedUsers;
  }

  public PublishStatus getStatus() {
    return status;
  }

  public void setStatus(PublishStatus status) {
    this.status = status;
  }

  public double getDiscountPercentage() {
    return discountPercentage;
  }

  public void setDiscountPercentage(double discountPercentage) {
    this.discountPercentage = discountPercentage;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   * 
   * @Override public String toString() { return "Coupon [id=" + id + ", includedProducts=" +
   * includedProducts + ", excludedProducts=" + excludedProducts + ", allowedUsers=" + allowedUsers
   * + ", code=" + code + ", description=" + description + ", amount=" + amount + ", discountType="
   * + discountType + ", expiryDate=" + expiryDate + ", minimumSpend=" + minimumSpend +
   * ", discountPercentage=" + discountPercentage + ", status=" + status + "]"; }
   */

  @Override
  public String toString() {
    return code;
  }

  /**
   * @return the usageLimitPerCoupon
   */
  public int getUsageLimitPerCoupon() {
    return usageLimitPerCoupon;
  }

  /**
   * @param usageLimitPerCoupon the usageLimitPerCoupon to set
   */
  public void setUsageLimitPerCoupon(int usageLimitPerCoupon) {
    this.usageLimitPerCoupon = usageLimitPerCoupon;
  }

  /**
   * @return the usageLimitPerUser
   */
  public int getUsageLimitPerUser() {
    return usageLimitPerUser;
  }

  /**
   * @param usageLimitPerUser the usageLimitPerUser to set
   */
  public void setUsageLimitPerUser(int usageLimitPerUser) {
    this.usageLimitPerUser = usageLimitPerUser;
  }

  /**
   * @return the noOfUsages
   */
  public int getNoOfUsages() {
    return noOfUsages;
  }

  /**
   * @param noOfUsages the noOfUsages to set
   */
  public void setNoOfUsages(int noOfUsages) {
    this.noOfUsages = noOfUsages;
  }
}
