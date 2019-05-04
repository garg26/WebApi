package com.neostencil.model.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.neostencil.common.enums.OrderStatusType;
import com.neostencil.common.enums.OrderType;
import com.neostencil.common.enums.PaymentModeType;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "ns_orders")
public class Order extends DomainObject {

	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ns_order_gen")
	@TableGenerator(name = "ns_order_gen", table = "ns_order_id_gen", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "OrderId_Gen", initialValue = 50000, allocationSize = 1)
	@Id
	@Column(name = "order_id")
	private int orderId;

	@Column(name = "total_amount", columnDefinition = "int default 0")
	private double totalAmount;

	@Column(name = "pay_amount", columnDefinition = "int default 0")
	private double payableAmount;

	@Column(name = "order_status")
	@Enumerated(EnumType.STRING)
	private OrderStatusType orderStatus;

	@Column(name = "payment_mode")
	@Enumerated(EnumType.STRING)
	private PaymentModeType paymentMode;

	@Column(name = "payment_date")
	private Timestamp paymentDate;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "order_completion_date")
	private Timestamp orderCompletionDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_id")
	private Coupon coupon;

	@JsonManagedReference(value = "orderReference")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
	private Set<OrderItem> orderItems;

	@Column(name = "discount", columnDefinition = "int default 0")
	private double discount;

	@JsonIgnoreProperties({ "userCourseBatchLinkages", "userUnitLinkages" })
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private User customer;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id")
	private Address address;

	@Column(name = "approval_date")
	private Timestamp approvalDate;

	@Column(name = "order_reason", columnDefinition = "TEXT")
	private String reason;

	@Column(name = "paid_extension")
	private boolean paidExtension;

	@Column(name = "validity")
	private int validity;
	
	@Column(name="neocash_redeemed")
	private double neoCashRedeemed;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	@Column(name = "refund_date")
	private Timestamp refundDate;

	@Column(name = "refund_reason")
	private String refundReason;

	@Column(name = "refund_amount")
	private double refundAmount;

	@Fetch(value = FetchMode.SUBSELECT)
	@ElementCollection(fetch = FetchType.LAZY)
	@JoinTable(name = "ns_order_notes", joinColumns = @JoinColumn(name = "order_id"))
	@OrderColumn(name = "sequence", updatable = true)
	private List<OrderNotes> orderNotes = new LinkedList<OrderNotes>();

	@Enumerated(EnumType.STRING)
	@Column(name = "order_type")
	OrderType orderType;

	@Column(name = "is_emi")
	boolean emi;

	@Column(name = "installment_number")
	int installmentNumber;

	public boolean isEmi() {
		return emi;
	}

	public void setEmi(boolean emi) {
		this.emi = emi;
	}

	public int getInstallmentNumber() {
		return installmentNumber;
	}

	public void setInstallmentNumber(int installmentNumber) {
		this.installmentNumber = installmentNumber;
	}

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Order(double totalAmount, double payableAmount, OrderStatusType orderStatus, PaymentModeType paymentMode,
			Timestamp paymentDate, String transactionId, Timestamp orderCompletionDate, Coupon coupon,
			Set<OrderItem> orderItems, double discount, User customer, Address address) {
		this.totalAmount = totalAmount;
		this.payableAmount = payableAmount;
		this.orderStatus = orderStatus;
		this.paymentMode = paymentMode;
		this.paymentDate = paymentDate;
		this.transactionId = transactionId;
		this.orderCompletionDate = orderCompletionDate;
		this.coupon = coupon;
		this.orderItems = orderItems;
		this.discount = discount;
		this.customer = customer;
		this.address = address;
	}

	public static Order createOrderFromCart(Cart c, PaymentModeType paymentMode, Coupon coupon, double discount,
			Address address) {
		Order order = null;
		LocalDateTime currentTime = LocalDateTime.now();
		Timestamp currentTimeStamp = Timestamp.valueOf(currentTime);
		if (!PaymentModeType.neft.equals(paymentMode)) {
			order = new Order(0, 0, OrderStatusType.PENDING_PAYMENT, paymentMode, currentTimeStamp, null, null, coupon,
					null, discount, c.getCustomer(), address);
		} else {
			order = new Order(0, 0, OrderStatusType.ON_HOLD, paymentMode, currentTimeStamp, null, null, coupon, null,
					discount, c.getCustomer(), address);
		}
		double totalAmount_ = 0;
		Set<OrderItem> orderItems_ = new HashSet<OrderItem>();
		for (CartOrderItem item : c.getItems()) {
			OrderItem oi = new OrderItem(item, order);
			orderItems_.add(oi);
			totalAmount_ = totalAmount_ + oi.getSubtotal();
		}
		double payableAmount_ = totalAmount_ - discount;
		order.setTotalAmount(totalAmount_);
		order.setPayableAmount(payableAmount_);
		order.setOrderItems(orderItems_);
		return order;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public OrderStatusType getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatusType orderStatus) {
		this.orderStatus = orderStatus;
	}

	public PaymentModeType getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentModeType paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Timestamp getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Timestamp paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Timestamp getOrderCompletionDate() {
		return orderCompletionDate;
	}

	public void setOrderCompletionDate(Timestamp orderCompletionDate) {
		this.orderCompletionDate = orderCompletionDate;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	public double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the discount
	 */
	public double getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}

	/**
	 * @return the approvalDate
	 */
	public Timestamp getApprovalDate() {
		return approvalDate;
	}

	/**
	 * @param approvalDate
	 *            the approvalDate to set
	 */
	public void setApprovalDate(Timestamp approvalDate) {
		this.approvalDate = approvalDate;
	}

	/**
	 * @return the refundDate
	 */
	public Timestamp getRefundDate() {
		return refundDate;
	}

	/**
	 * @param refundDate
	 *            the refundDate to set
	 */
	public void setRefundDate(Timestamp refundDate) {
		this.refundDate = refundDate;
	}

	/**
	 * @return the refundReason
	 */
	public String getRefundReason() {
		return refundReason;
	}

	/**
	 * @param refundReason
	 *            the refundReason to set
	 */
	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	/**
	 * @return the orderNotes
	 */
	public List<OrderNotes> getOrderNotes() {
		return orderNotes;
	}

	/**
	 * @param orderNotes
	 *            the orderNotes to set
	 */
	public void setOrderNotes(List<OrderNotes> orderNotes) {
		this.orderNotes = orderNotes;
	}

	/**
	 * @return the refundAmount
	 */
	public double getRefundAmount() {
		return refundAmount;
	}

	/**
	 * @param refundAmount
	 *            the refundAmount to set
	 */
	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	/**
	 * @return the paidExtension
	 */
	public boolean isPaidExtension() {
		return paidExtension;
	}

	/**
	 * @param paidExtension
	 *            the paidExtension to set
	 */
	public void setPaidExtension(boolean paidExtension) {
		this.paidExtension = paidExtension;
	}

	/**
	 * @return the validity
	 */
	public int getValidity() {
		return validity;
	}

	/**
	 * @param validity
	 *            the validity to set
	 */
	public void setValidity(int validity) {
		this.validity = validity;
	}

  /**
   * @return the neoCashRedeemed
   */
  public double getNeoCashRedeemed() {
    return neoCashRedeemed;
  }

  /**
   * @param neoCashRedeemed the neoCashRedeemed to set
   */
  public void setNeoCashRedeemed(double neoCashRedeemed) {
    this.neoCashRedeemed = neoCashRedeemed;
  }
}
