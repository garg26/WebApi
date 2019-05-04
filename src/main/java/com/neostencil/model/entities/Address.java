package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "ns_addresses")
public class Address extends DomainObject {

  @GeneratedValue(strategy = GenerationType.TABLE, generator = "address_gen")
  @TableGenerator(name = "address_gen", table = "ns_address_id_gen", pkColumnName = "GEN_NAME",
      valueColumnName = "GEN_VAL", pkColumnValue = "addressId_Gen", initialValue = 30000,
      allocationSize = 100)
  @Id
  @Column(name = "address_id")
  int addressId;

  @Column(name = "first_name")
  String firstName;

  @Column(name = "last_name")
  String lastname;

  @Column(name = "mobile_number")
  String mobileNumber;

  @Column(name = "alternate_mobile_number")
  String alternateMobileNumber;

  @Column(name = "address_text")
  String addressText;

  @Column(name = "city")
  String city;

  @Column(name = "state")
  String state;

  @Column(name = "pincode")
  String pincode;

  @Column(name = "is_default_address")
  boolean defaultAddress;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  User user;

  public Address() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @return the addressText
   */
  public String getAddressText() {
    return addressText;
  }

  /**
   * @param addressText the addressText to set
   */
  public void setAddressText(String addressText) {
    this.addressText = addressText;
  }

  /**
   * @return the city
   */
  public String getCity() {
    return city;
  }

  /**
   * @param city the city to set
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * @return the state
   */
  public String getState() {
    return state;
  }

  /**
   * @param state the state to set
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * @return the pincode
   */
  public String getPincode() {
    return pincode;
  }

  /**
   * @param pincode the pincode to set
   */
  public void setPincode(String pincode) {
    this.pincode = pincode;
  }

  /**
   * @return the addressId
   */
  public int getAddressId() {
    return addressId;
  }

  /**
   * @param addressId the addressId to set
   */
  public void setAddressId(int addressId) {
    this.addressId = addressId;
  }

  /**
   * @return the isDefault
   */
  public boolean isDefaultAddress() {
    return defaultAddress;
  }

  /**
   * @param isDefault the isDefault to set
   */
  public void setDefaultAddress(boolean isDefault) {
    this.defaultAddress = isDefault;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getAlternateMobileNumber() {
    return alternateMobileNumber;
  }

  public void setAlternateMobileNumber(String alternateMobileNumber) {
    this.alternateMobileNumber = alternateMobileNumber;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {

    StringBuilder address = new StringBuilder();
    address.append(firstName + " ");
    address.append(lastname + " ");
    String tempAdd = addressText.replaceAll(",", "|");
    address.append(tempAdd + " ");
    address.append(city + " ");
    address.append(state + " ");
    address.append(pincode + " ");
    address.append(mobileNumber);

    return address.toString();
  }
}
