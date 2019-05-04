package com.neostencil.projections;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.springframework.data.rest.core.config.Projection;
import com.neostencil.common.enums.OrderStatusType;
import com.neostencil.common.enums.OrderType;
import com.neostencil.common.enums.PaymentModeType;
import com.neostencil.model.entities.Order;
import com.neostencil.model.entities.OrderNotes;

@Projection(name = "orderProjection", types = Order.class)
public interface OrderProjection {

	int getOrderId();

	double getTotalAmount();

	double getPayableAmount();
	
	OrderStatusType getOrderStatus();

	PaymentModeType getPaymentMode();

	Timestamp getPaymentDate();

	String getTransactionId();

	CouponCodeProjection getCoupon();

	Set<OrderItemProjection> getOrderItems();

	double getDiscount();

	UserProjection getCustomer();

	AddressProjection getAddress();

	double getRefundAmount();

	List<OrderNotes> getOrderNotes();

	String getRefundReason();

	boolean isEmi();

	int getInstallmentNumber();

	String getReason();

	OrderType getOrderType();
	
	double getNeoCashRedeemed();
}
