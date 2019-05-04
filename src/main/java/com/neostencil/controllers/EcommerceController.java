package com.neostencil.controllers;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.neostencil.common.enums.OrderStatusType;
import com.neostencil.common.enums.ProductType;
import com.neostencil.model.entities.Address;
import com.neostencil.model.entities.Cart;
import com.neostencil.model.entities.Coupon;
import com.neostencil.model.entities.Order;
import com.neostencil.model.entities.Product;
import com.neostencil.projections.AddressProjection;
import com.neostencil.projections.CouponProjection;
import com.neostencil.projections.OrderProjection;
import com.neostencil.projections.ProductProjection;
import com.neostencil.requests.CartItemUpdateRequest;
import com.neostencil.requests.CourseAccessRequest;
import com.neostencil.requests.CreateZestmoneyOrderRequest;
import com.neostencil.requests.DataExportRequest;
import com.neostencil.requests.ExtensionRequest;
import com.neostencil.requests.OrderSubmitRequest;
import com.neostencil.requests.RazorPayPaymentVerifyRequest;
import com.neostencil.requests.RefundRequest;
import com.neostencil.requests.SalesReportRequest;
import com.neostencil.requests.UpdateCartRequest;
import com.neostencil.requests.UserBatchLinkagesRequest;
import com.neostencil.responses.GetZestmoneyOrderStatusResponse;
import com.neostencil.responses.OrderResponse;
import com.neostencil.responses.OrderSummaryResponse;
import com.neostencil.responses.SalesReportResponse;
import com.neostencil.responses.UpdateResponse;
import com.neostencil.responses.ValidateCouponResponse;
import com.neostencil.responses.ZestmoneyOrderResponse;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.services.EcommerceService;

@RestController
@RequestMapping({ "/api/v1" })

public class EcommerceController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Autowired
	EcommerceService ecommerceService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@RequestMapping(value = "/admin_sales/coupons", method = RequestMethod.POST)
	@ResponseBody
	Coupon addCoupon(@Valid @RequestBody Coupon coupon) {
		coupon.addMetadata();
		Coupon response = ecommerceService.addCoupon(coupon);
		return response;
	}

	@RequestMapping(value = "/admin_sales/coupons", method = RequestMethod.GET)
	@ResponseBody
	List<CouponProjection> fetchAllCoupons() {
		List<CouponProjection> response = ecommerceService.fetchAllCoupons();
		return response;
	}

	@RequestMapping(value = "/admin_sales/coupons/{couponId}", method = RequestMethod.PUT)
	@ResponseBody
	Coupon updateCoupon(@PathVariable("couponId") int couponId, @Valid @RequestBody Coupon coupon) {
		Coupon response = ecommerceService.updateCoupon(couponId, coupon);
		return response;
	}

	@RequestMapping(value = "/admin_sales/coupons/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	Coupon deleteCoupon(@PathVariable("id") int id) {
		Coupon response = ecommerceService.deleteCoupon(id);
		return response;
	}

	@RequestMapping(value = "/coupons/{code}", method = RequestMethod.GET)
	@ResponseBody
	CouponProjection fetchCouponByCode(@PathVariable("coupon") String code) {
		CouponProjection response = ecommerceService.fetchCouponByCode(code);
		return response;
	}

	@RequestMapping(value = "/user/validatecoupon", method = RequestMethod.GET)
	@ResponseBody
	ValidateCouponResponse validateCoupon(@RequestParam(value = "coupon", required = false) String code) {
		long userID = jwtTokenUtil.getLoggedInUserID();
		ValidateCouponResponse response = ecommerceService.validateCoupon(userID, code);
		return response;

	}

	@Value("${razorpay.keyid}")
	private String razorPayKeyID;

	@RequestMapping(value = "/user/getrazorpaykeyid", method = RequestMethod.GET)
	@ResponseBody
	public String getRazorPayKeyID()
	{
		return razorPayKeyID;
	}


	// TODO all products methods to move to admin role

	@RequestMapping(value = { "/admin/products" }, method = RequestMethod.POST)
	@ResponseBody
	public Product createProduct(@Valid @RequestBody Product product) {
		Product response = ecommerceService.createProduct(product);
		return response;
	}

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	@ResponseBody
	public List<ProductProjection> fetchAllProducts() {
		List<ProductProjection> response = ecommerceService.fetchAllProducts();
		return response;
	}

	@RequestMapping(value = "/admin/products", method = RequestMethod.PUT)
	@ResponseBody
	public Product updateProduct(@Valid @RequestBody Product product) {
		Product response = ecommerceService.updateProduct(product);
		return response;
	}

	@RequestMapping(value = "/admin/products", method = RequestMethod.DELETE)
	@ResponseBody
	public Product deleteProduct(@Valid @RequestBody Product product) {
		Product response = ecommerceService.deleteProduct(product);
		return response;
	}

	@RequestMapping(value = "/address", method = RequestMethod.POST)
	@ResponseBody
	public Address addAddress(@Valid @RequestBody Address address) {
		long userID = jwtTokenUtil.getLoggedInUserID();
		Address response = ecommerceService.addAddress(userID, address);
		return response;
	}

	@RequestMapping(value = "/address", method = RequestMethod.GET)
	@ResponseBody
	public List<AddressProjection> getAddresses() {
		long userID = jwtTokenUtil.getLoggedInUserID();
		List<AddressProjection> response = ecommerceService.getAddresses(userID);
		return response;
	}

	@RequestMapping(value = "/user/update-cart", method = RequestMethod.POST)
	@ResponseBody
	public OrderResponse updateCart(@Valid @RequestBody CartItemUpdateRequest cartItemUpdateRequest) {

		long userID = jwtTokenUtil.getLoggedInUserID();
		ProductType type = cartItemUpdateRequest.getProductType();
		int commodityID = cartItemUpdateRequest.getCommodityId();
		int productID = cartItemUpdateRequest.getProductId();
		int quantity = cartItemUpdateRequest.getQuantity();
		Cart response = null;

		if (productID != 0) {
			response = ecommerceService.updateCart(userID, productID, quantity);
		} else if (commodityID != 0) {
			response = ecommerceService.updateCart(userID, type, commodityID, quantity);
		} else {
			LOGGER.error("Invalid data" + cartItemUpdateRequest);
		}

		OrderResponse rv = new OrderResponse(response);
		return rv;
	}

	@RequestMapping(value = "/user/get-cart", method = RequestMethod.GET)
	@ResponseBody
	public OrderResponse getCart() {
		long userID = jwtTokenUtil.getLoggedInUserID();
		Cart cart = ecommerceService.getCart(userID);
		OrderResponse rv = new OrderResponse(cart);
		return rv;
	}

	@RequestMapping(value = "/user/apply-coupon/{coupon_code}", method = RequestMethod.POST)
	@ResponseBody
	public OrderResponse updateCart(@PathVariable String coupon_code) {
		// TODO actually apply coupon code, for now we are just returning cart
		// as it is
		long userID = jwtTokenUtil.getLoggedInUserID();
		Cart userCart = ecommerceService.getCart(userID);
		OrderResponse rv = new OrderResponse(userCart);
		return rv;
	}

	@RequestMapping(value = "/user/submit_order", method = RequestMethod.POST)
	@ResponseBody
  public OrderResponse submitOrder(@Valid @RequestBody OrderSubmitRequest orderRequest) {
    // TODO verify prices and the whole order again here
    OrderResponse rv = null;
    long userID = jwtTokenUtil.getLoggedInUserID();
    Order submittedOrder = ecommerceService.submitOrder(userID, orderRequest.getPaymentModeType(),
        orderRequest.getCouponCode(), orderRequest.getAddressId(),
        orderRequest.getNeoCashRedeemed());
    rv = new OrderResponse(submittedOrder);
    return rv;
  }

	@RequestMapping(value = "/user/submit_free_order", method = RequestMethod.POST)
	@ResponseBody
	public OrderResponse submitFreeOrder(@Valid @RequestBody OrderSubmitRequest orderRequest) {
		// TODO verify prices and the whole order again here
		OrderResponse rv = null;
		rv = ecommerceService.submitFreeOrder(orderRequest);
		return rv;
	}

	@RequestMapping(value = "/user/verify_payment_razorpay", method = RequestMethod.POST)
	@ResponseBody
	public OrderSummaryResponse verifyPayment(@Valid @RequestBody RazorPayPaymentVerifyRequest paymentVerifyRequest) {
		OrderSummaryResponse rv = null;
		long userID = jwtTokenUtil.getLoggedInUserID();
		Order order = ecommerceService.verifyPaymentRazorPay(userID, paymentVerifyRequest.getOrderId(),
				paymentVerifyRequest.getPaymentId());

		// TODO check for null
		if (order.getOrderStatus() == OrderStatusType.PAYMENT_COMPLETE) {
			ecommerceService.handlePaymentVerifiedOrder(userID, order);
		}
		rv = new OrderSummaryResponse(order);
		return rv;
	}

	@RequestMapping(value = "/user/handle-zero-payment-razorpay-order/{orderId}", method = RequestMethod.PATCH)
	@ResponseBody
	public Order handleZeroPaymentRazorpayOrder(@PathVariable("orderId") int orderId) {
		Order order = ecommerceService.handleZeroPaymentRazorpayOrder(orderId);
		return order;
	}

	@RequestMapping(value = "/admin_sales/ecommerce/updatecart", method = RequestMethod.POST)
	OrderResponse addItemToOrder(@Valid @RequestBody UpdateCartRequest request) {
		Cart cart = ecommerceService.updateCart(request.getUserId(), request.getProductId(), request.getQuantity());
		OrderResponse response = new OrderResponse(cart);
		return response;
	}

	@RequestMapping(value = "/get_orders", method = RequestMethod.GET)
	@ResponseBody
	public List<OrderSummaryResponse> getAllOrders() {
		List<OrderSummaryResponse> rv = new ArrayList<>();
		long userID = jwtTokenUtil.getLoggedInUserID();
		List<Order> allUserOrders = ecommerceService.getOrders(userID);

		for (Order order : allUserOrders) {
			rv.add(new OrderSummaryResponse(order));
		}
		// rv = new OrderResponse(submittedOrder);
		return rv;
	}

	@RequestMapping(value = "/order/{order_id}", method = RequestMethod.GET)
	@ResponseBody
	OrderSummaryResponse getOrderSummary(@PathVariable("order_id") int orderId) {

		// TODO check everywehre for loging
		long userID = jwtTokenUtil.getLoggedInUserID();
		Order order = ecommerceService.getOrder(userID, orderId);
		OrderSummaryResponse response = new OrderSummaryResponse(order);
		return response;
	}

	@RequestMapping(value = "/admin_sales/orders/{id}/approve", method = RequestMethod.PATCH)
	void approveOrder(@PathVariable("id") int id, @RequestParam("sendNotification") boolean sendNotification) {
		ecommerceService.approveOrder(id, sendNotification);

	}

	@RequestMapping(value = "/admin_sales/userbatchlinkages", method = RequestMethod.PUT)
	@ResponseBody
	public UpdateResponse updateUserBatchLinkages(@Valid @RequestBody UserBatchLinkagesRequest request) {
		ecommerceService.updateUserCourseBatchLinkages(request);
		UpdateResponse response = new UpdateResponse();
		response.setSuccess(true);
		return response;

	}

	@RequestMapping(value = "/admin_sales/orders/refund", method = RequestMethod.PATCH)
	void refundOrder(@Valid @RequestBody RefundRequest request) {
		ecommerceService.refundOrder(request);

	}

	@RequestMapping(value = "/admin_sales/orders/partialrefund", method = RequestMethod.PATCH)
	void partialRefundOrder(@Valid @RequestBody RefundRequest request) {
		ecommerceService.partialRefundOrder(request);

	}

	@RequestMapping(value = "/admin_sales/export", method = RequestMethod.POST)
	public String exportData(@Valid @RequestBody DataExportRequest request) throws IOException {

		// TODO /tmp should be replaced and read by application.properties
		String fileNamePath = "/tmp/" + request.getEntityName() + LocalDate.now().toString() + ".csv";
		Writer writer = Files.newBufferedWriter(Paths.get(fileNamePath));

		boolean isExportData = ecommerceService.exportData(request, writer);

		if (isExportData) {
			String downloadFileUrl = ecommerceService.uploadCSVToCloud(fileNamePath);

			File file = new File(fileNamePath);
			if (file.exists()) {
				file.delete();
			}
			return downloadFileUrl;
		}
		return null;
	}

	/**
	 * For fetching the order by its Id for showing summary of order from admin
	 * dashboard
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/admin_sales/orders/{id}", method = RequestMethod.GET)
	@ResponseBody
	OrderProjection fetchOrderById(@PathVariable("id") int id) {

		OrderProjection response = ecommerceService.fetchOrderById(id);
		return response;
	}

	@RequestMapping(value = "/admin_sales/report", method = RequestMethod.POST)
	@ResponseBody
	SalesReportResponse generateSalesReport(@RequestBody SalesReportRequest request) {
		SalesReportResponse response = ecommerceService.generateSalesReport(request);
		return response;
	}

	@RequestMapping(value = "/admin_sales/payment-confirm/{orderId}", method = RequestMethod.PATCH)
	void confirmPayment(@PathVariable("orderId") int orderId) {
		ecommerceService.confirmPayment(orderId);
	}

	@RequestMapping(value = "/admin/give-course-access", method = RequestMethod.POST)
	String giveCourseAccessToUser(@Valid @RequestBody CourseAccessRequest request) {
		String response = ecommerceService.giveCourseAccessToUser(request.getUserEmailId(), request.getBatchId());
		return response;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/admin_sales/give-paid-extension", method = RequestMethod.POST)
	boolean givePaidExtension(@Valid @RequestBody ExtensionRequest request) {
		boolean success = ecommerceService.givePaidExtension(request);
		return success;
	}


	@RequestMapping(value = "/user/create-zestmoney-token",method = RequestMethod.GET)
	void createZestmoneyToken(){

		ecommerceService.createZestmoneyToken();
	}

	@RequestMapping(value = "/user/create-zestmoney-order",method = RequestMethod.POST)
	ZestmoneyOrderResponse createZestmoneyOrder(HttpServletRequest httpServletRequest,@RequestBody CreateZestmoneyOrderRequest request){

		String requestURL = httpServletRequest.getRequestURL().toString();
		String baseURL = requestURL.split("api")[0];

		return ecommerceService.createZestmoneyOrder(request,baseURL);
	}

	GetZestmoneyOrderStatusResponse getZestmoneyOrderStatus(int orderId){
		return ecommerceService.getZestmoneyOrderStatus(orderId);
	}
}
