package com.neostencil.model.entities;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neostencil.common.enums.LoginType;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Indexed
@Table(name = "ns_users")
public class User extends DomainObject {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
  @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
  @Field(name = "userValueId", index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  private Long userId;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "USERNAME", length = 50, unique = true)
  private String userName;

  @Column(name = "PASSWORD", length = 100)
  @Size(min = 4, max = 100)
  private String password;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "FIRSTNAME", length = 50)
  private String firstname;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "LASTNAME", length = 50)
  private String lastname;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "fullname", length = 50)
  @NotNull
  @Size(min = 4, max = 100)
  private String fullName;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "EMAIL", unique = true)
  @NotNull
  @Size(min = 4)
  private String emailId;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "mobile", length = 15)
  private String mobileNumber;

  @Column(name = "is_mobile_verified")
  private boolean isMobileNumberVerified;

  @Column(name = "ENABLED")
  private boolean enabled;

  @JsonIgnore
  @Column(name = "LASTPASSWORDRESETDATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastPasswordResetDate;

  @Column(name = "avatar")
  private String avatar;

  @JsonIgnoreProperties("users")
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "NS_USER_AUTHORITY",
      joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
      inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
  private Set<Authority> authorities;

  @Column(name = "is_kyc_verified")
  private boolean isKYCVerified;

  @JsonIgnore
  @OrderBy("linkageId desc")
  // @JsonManagedReference(value = "user-linkage")
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private Set<UserCourseBatchLinkage> userCourseBatchLinkages;

  @JsonIgnore
  @JsonIgnoreProperties(value = "user")
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private Set<UserUnitLinkage> userUnitLinkages;

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "ns_user_devices",
      joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
      inverseJoinColumns = {@JoinColumn(name = "device_id", referencedColumnName = "ID")})
  private Set<UserDevice> devices;

  @Enumerated(EnumType.STRING)
  @Column(name = "login_type")
  private LoginType loginType;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "customer")
  private Cart cart;

  @JsonIgnore
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "queriedBy")
  Set<Query> queriesSent;

  @JsonIgnore
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "queriedTo")
  Set<Query> queriesReceived;

  @Column(name = "exam_segment")
  String examSegment;

  @Column(name = "city")
  String city;
  
  @Column(name="neo_cash_balance")
  double neoCashBalance;

  @JsonIgnore
  @JsonIgnoreProperties(value = "user")
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private Set<Discussion> discussions;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }

  public Date getLastPasswordResetDate() {
    return lastPasswordResetDate;
  }

  public void setLastPasswordResetDate(Date lastPasswordResetDate) {
    this.lastPasswordResetDate = lastPasswordResetDate;
  }

  /**
   * @return the userCourseBatchLinkages
   */
  public Set<UserCourseBatchLinkage> getUserCourseBatchLinkages() {
    return userCourseBatchLinkages;
  }

  /**
   * @param userCourseBatchLinkages the userCourseBatchLinkages to set
   */
  public void setUserCourseBatchLinkages(Set<UserCourseBatchLinkage> userCourseBatchLinkages) {
    this.userCourseBatchLinkages = userCourseBatchLinkages;
  }

  /**
   * @return the userUnitLinkages
   */
  public Set<UserUnitLinkage> getUserUnitLinkages() {
    return userUnitLinkages;
  }

  /**
   * @param userUnitLinkages the userUnitLinkages to set
   */
  public void setUserUnitLinkages(Set<UserUnitLinkage> userUnitLinkages) {
    this.userUnitLinkages = userUnitLinkages;
  }

  /**
   * @param enabled the enabled to set
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public Set<UserDevice> getDevices() {
    return devices;
  }

  public void setDevices(Set<UserDevice> devices) {
    this.devices = devices;
  }

  public LoginType getLoginType() {
    return loginType;
  }

  public void setLoginType(LoginType loginType) {
    this.loginType = loginType;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  /**
   * @return the cart
   */
  public Cart getCart() {
    return cart;
  }

  /**
   * @param cart the cart to set
   */
  public void setCart(Cart cart) {
    this.cart = cart;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public boolean isMobileNumberVerified() {
    return isMobileNumberVerified;
  }

  public void setMobileNumberVerified(boolean mobileNumberVerified) {
    isMobileNumberVerified = mobileNumberVerified;
  }

  /**
   * @return the queriesSent
   */
  public Set<Query> getQueriesSent() {
    return queriesSent;
  }

  /**
   * @param queriesSent the queriesSent to set
   */
  public void setQueriesSent(Set<Query> queriesSent) {
    this.queriesSent = queriesSent;
  }

  /**
   * @return the queriesReceived
   */
  public Set<Query> getQueriesReceived() {
    return queriesReceived;
  }

  /**
   * @param queriesReceived the queriesReceived to set
   */
  public void setQueriesReceived(Set<Query> queriesReceived) {
    this.queriesReceived = queriesReceived;
  }

  /**
   * @return the examSegment
   */
  public String getExamSegment() {
    return examSegment;
  }

  /**
   * @param examSegment the examSegment to set
   */
  public void setExamSegment(String examSegment) {
    this.examSegment = examSegment;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public boolean isKYCVerified() {
    return isKYCVerified;
  }

  public void setKYCVerified(boolean KYCVerified) {
    isKYCVerified = KYCVerified;
  }

  /**
   * @return the discussions
   */
  public Set<Discussion> getDiscussions() {
    return discussions;
  }

  /**
   * @param discussions the discussions to set
   */
  public void setDiscussions(Set<Discussion> discussions) {
    this.discussions = discussions;
  }

  /**
   * @return the neoCashBalance
   */
  public double getNeoCashBalance() {
    return neoCashBalance;
  }

  /**
   * @param neoCashBalance the neoCashBalance to set
   */
  public void setNeoCashBalance(double neoCashBalance) {
    this.neoCashBalance = neoCashBalance;
  }

}
