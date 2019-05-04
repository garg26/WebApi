package com.neostencil.services;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.util.TextUtils;
import org.joda.time.Days;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.neostencil.base.BaseService;
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.common.enums.OrderStatusType;
import com.neostencil.common.enums.OrderType;
import com.neostencil.common.enums.PaymentModeType;
import com.neostencil.common.enums.ProductType;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.common.enums.StudentStatusType;
import com.neostencil.common.enums.UserCourseLinkageStatus;
import com.neostencil.common.enums.WatchStatus;
import com.neostencil.exceptions.InvalidInputException;
import com.neostencil.exceptions.NoPermissionException;
import com.neostencil.exceptions.PaymentException;
import com.neostencil.exceptions.ProductNotFoundException;
import com.neostencil.model.entities.Address;
import com.neostencil.model.entities.Authority;
import com.neostencil.model.entities.AuthorityName;
import com.neostencil.model.entities.Cart;
import com.neostencil.model.entities.CartOrderItem;
import com.neostencil.model.entities.Coupon;
import com.neostencil.model.entities.Course;
import com.neostencil.model.entities.CourseBatch;
import com.neostencil.model.entities.CustomStudent;
import com.neostencil.model.entities.Message;
import com.neostencil.model.entities.Order;
import com.neostencil.model.entities.OrderItem;
import com.neostencil.model.entities.OrderNotes;
import com.neostencil.model.entities.Product;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.model.entities.TeacherStatistics;
import com.neostencil.model.entities.Unit;
import com.neostencil.model.entities.User;
import com.neostencil.model.entities.UserCourseBatchLinkage;
import com.neostencil.model.entities.UserUnitLinkage;
import com.neostencil.model.repositories.AddressRepository;
import com.neostencil.model.repositories.AuthorityRepository;
import com.neostencil.model.repositories.CartOrderItemRepository;
import com.neostencil.model.repositories.CartRepository;
import com.neostencil.model.repositories.CouponRepository;
import com.neostencil.model.repositories.CourseBatchRepository;
import com.neostencil.model.repositories.CourseRepository;
import com.neostencil.model.repositories.CustomStudentRepository;
import com.neostencil.model.repositories.OrderItemRepository;
import com.neostencil.model.repositories.OrderRepository;
import com.neostencil.model.repositories.ProductRepository;
import com.neostencil.model.repositories.TeacherStatisticsRepository;
import com.neostencil.model.repositories.UnitRepository;
import com.neostencil.model.repositories.UserCourseBatchLinkageRepository;
import com.neostencil.model.repositories.UserRepository;
import com.neostencil.model.repositories.UserUnitLinkageRepository;
import com.neostencil.model.specifications.CouponSpecificationBuilder;
import com.neostencil.model.specifications.OrderSpecificationBuilder;
import com.neostencil.model.specifications.ProductSpecificationBuilder;
import com.neostencil.projections.AddressProjection;
import com.neostencil.projections.CouponProjection;
import com.neostencil.projections.OrderProjection;
import com.neostencil.projections.ProductProjection;
import com.neostencil.projections.UnitProjection;
import com.neostencil.requests.CreateZestmoneyOrderRequest;
import com.neostencil.requests.DataExportRequest;
import com.neostencil.requests.DataFieldRequest;
import com.neostencil.requests.DebitNeoCashRequest;
import com.neostencil.requests.ExtensionRequest;
import com.neostencil.requests.OrderSubmitRequest;
import com.neostencil.requests.RefundRequest;
import com.neostencil.requests.SalesReportRequest;
import com.neostencil.requests.UserBatchLinkagesRequest;
import com.neostencil.requests.ValidateNeoCashRedemptionRequest;
import com.neostencil.requests.ZestmoneyOrderItemRequest;
import com.neostencil.requests.ZestmoneyOrderRequest;
import com.neostencil.responses.CouponDTO;
import com.neostencil.responses.CouponReport;
import com.neostencil.responses.CourseProductResponse;
import com.neostencil.responses.FreeOrdersReport;
import com.neostencil.responses.GetZestmoneyOrderStatusResponse;
import com.neostencil.responses.OrderDTO;
import com.neostencil.responses.OrderResponse;
import com.neostencil.responses.OrderStatusSummaryResponse;
import com.neostencil.responses.OrdersResponse;
import com.neostencil.responses.PaidOrdersReport;
import com.neostencil.responses.RefundReport;
import com.neostencil.responses.SalesReportResponse;
import com.neostencil.responses.UnitResponse;
import com.neostencil.responses.ValidateCouponResponse;
import com.neostencil.responses.ValidateNeoCashRedemptionResponse;
import com.neostencil.responses.ZestmoneyOrderResponse;
import com.neostencil.responses.ZestmoneyToken;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.utils.CSVUtility;
import com.neostencil.utils.ECommerceAssembler;
import com.neostencil.utils.Utils;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;

@Service
public class EcommerceService {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  @Autowired
  UserRepository userRepository;
  @Autowired
  CouponRepository couponRepository;
  @Autowired
  CartRepository cartRepository;
  @Autowired
  CartOrderItemRepository cartOrderItemRepository;
  @Autowired
  OrderRepository orderRepository;
  @Autowired
  OrderItemRepository orderItemRepository;
  @Autowired
  AddressRepository addressRepository;
  @Autowired
  ProductRepository productRepository;
  @Autowired
  CourseRepository courseRepository;
  @Autowired
  CourseBatchRepository courseBatchRepository;
  @Autowired
  TeacherStatisticsRepository teacherStatisticsRepository;
  @Autowired
  private AuthorityRepository authRepository;
  @Autowired
  UserCourseBatchLinkageRepository userCourseBatchLinkageRepository;
  @Autowired
  UserUnitLinkageRepository userUnitLinkageRepository;
  @Autowired
  UnitRepository unitRepository;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired
  BaseService baseService;
  private RazorpayClient razorpayClient;

  @Autowired
  EmailService emailService;
  @Autowired
  SMSService smsService;
  @Autowired
  ECommerceAssembler assembler;
  @Autowired
  UserService userService;
  
  @Autowired
  NeoCashService neoCashService;

  @Autowired
  CustomStudentRepository customStudentRepository;

  @Value("${zestmoney.clientId}")
  private String zestmoneyClientId;

  @Value("${zestmoney.clientSecret}")
  private String zestmoneyclientSecret;

  @Value("${zestmoney.scope}")
  private String zestmoneyScope;

  @Value("${zestmoney.grantType}")
  private String zestmoneyGrantType;

  @Value("${zestmoney.authURL}")
  private String zestmoneyAuthUrl;

  @Value("${zestmoney.baseURL}")
  private String zestmoneyBaseUrl;

  @Value("${neostencil.website.url}")
  private String websiteHomePageURL;


  private HashMap<String,String> zestmoneyTokenMap = new HashMap<String,String>(){
    {
      put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6ImZmOVNNeXRCU3hyUVNlS1U1d2tBYmpYZi1IVSIsImtpZCI6ImZmOVNNeXRCU3hyUVNlS1U1d2tBYmpYZi1IVSJ9.eyJpc3MiOiJodHRwOi8vc3RhZ2luZy56ZXN0bW9uZXkuaW4iLCJhdWQiOiJodHRwOi8vc3RhZ2luZy56ZXN0bW9uZXkuaW4vcmVzb3VyY2VzIiwiZXhwIjoxNTUzNTEzMzcxLCJuYmYiOjE1NTM1MDk3NzEsImNsaWVudF9pZCI6Ijg5NjViNTZiLWE3YjYtNDkyNC05MTU2LThiYzljOGU0M2RkYyIsImNsaWVudF9NZXJjaGFudElkIjoiODk2NWI1NmItYTdiNi00OTI0LTkxNTYtOGJjOWM4ZTQzZGRjIiwic2NvcGUiOiJtZXJjaGFudF9hcGlfc2Vuc2l0aXZlIn0.WlmIYRTocXgZWx4NO2pJWXfXuGMah3-G9K0hFCyjXCgkd2drha4qDc1LztlEM7xNmUuJTQNH90BtrhOoabEdVszNE8TRzW4PAXaJ5YrT7QFNi43aDhxRGtAoCsUe56sn1utfIAWkFNVVhJeFHbTqffbQGhUNr2W_PQi4Z-itST97iiSWXEhpfxLZmyUQiRWbehMJwmeHL5zkkQAWUWcTnpb1Y9xAP5Z2MAggdmUF2Ix8WCp7CqbxX-qtkNx47Uc1yo3LvhgGgcNCch70jjmFzXq3YPHa0s2sOSdL970oK8NJ6gHyNhsxDVdH8vWjpgAH1kGy_mQe-q1Na5Hgm9Mxww");
    }
  };



  @Autowired
  public EcommerceService(@Value("${razorpay.keyid}") String razorpayKeyId,
      @Value("${razorpay.secret}") String razorPaySecret) {
    super();
    try {
      razorpayClient = new RazorpayClient(razorpayKeyId, razorPaySecret);
    } catch (RazorpayException e) {
      e.printStackTrace();
    }
  }

  public Coupon addCoupon(Coupon coupon) {
    Coupon response = null;
    if (coupon != null) {
      if (coupon.getCode() == null || coupon.getCode().isEmpty()) {
        String randomCouponCode = Utils.randomAlphaNumeric(6);
        coupon.setCode(randomCouponCode);
      }

      try {
        response = couponRepository.save(coupon);
      } catch (Exception e) {
        LOGGER.error(Coupon.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Coupon.class.getName(), null, null);
    }
    return response;
  }

  public List<CouponProjection> fetchAllCoupons() {
    List<CouponProjection> response = couponRepository.findAllProjectedBy();
    return response;
  }

  public Coupon updateCoupon(int id, Coupon coupon) {

    try {

      if (coupon != null) {
        coupon.setId(id);
        coupon = couponRepository.save(coupon);
      }
    } catch (Exception e) {
      LOGGER.error(Coupon.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return coupon;
  }

  public List<ProductProjection> fetchAllProducts() {
    List<ProductProjection> response = null;
    try {
      response = productRepository.findAllProjectedBy();
    } catch (Exception e) {
      LOGGER.error(Product.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public List<UnitResponse> fetchAllUnitProducts() {
    List<UnitResponse> response = new LinkedList<UnitResponse>();
    try {
      List<ProductProjection> products = productRepository.findByType(ProductType.UNIT);
      List<UnitProjection> units = unitRepository.findByProduct(true);
      if (units != null && units.size() > 0 && products != null && products.size() > 0) {
        for (UnitProjection u : units) {
          for (ProductProjection p : products) {
            if (p.getCommodityId() == u.getUnitId()) {
              UnitResponse ur = new UnitResponse();
              ur.setBatchName(u.getBatchName());
              ur.setProduct(p);
              ur.setUnit(u);
              response.add(ur);
            }
          }
        }
      }
    } catch (Exception e) {
      LOGGER.error(Product.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  /**
   * For fetching all the course batch products
   */
  public List<CourseProductResponse> fetchAllCourseProducts() {
    List<CourseProductResponse> response = new LinkedList<CourseProductResponse>();
    try {
      List<ProductProjection> products = productRepository.findByType(ProductType.COURSE);
      List<Integer> batchIdList = new LinkedList<Integer>();

      if (products != null && products.size() > 0) {
        for (ProductProjection p : products) {
          if (p != null && p.getCommodityId() != 0) {
            batchIdList.add(p.getCommodityId());
          }
        }
      }

      List<CourseBatch> batches = courseBatchRepository.findByIdIn(batchIdList);

      if (batches != null && batches.size() > 0 && products != null && products.size() > 0) {
        for (ProductProjection p : products) {
          for (CourseBatch cb : batches) {
            if (cb.getId() == p.getCommodityId()) {
              CourseProductResponse cpr = new CourseProductResponse();
              if (cb.getCourse() != null) {
                cpr.setCourseName(cb.getCourse().getCourseName());
                cpr.setBatchName(cb.getBatchName());
              }
              cpr.setProduct(p);
              response.add(cpr);
            }

          }
        }
      }
    } catch (Exception e) {
      LOGGER.error(CourseProductResponse.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }

  public Coupon deleteCoupon(int id) {
    try {
      Coupon check = couponRepository.findById(id);
      if (check != null) {
        couponRepository.delete(check);
        return check;
      }
    } catch (Exception e) {
      LOGGER.error(Coupon.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  public Product createProduct(Product product) {
    Product response = null;
    if (product != null) {

      try {
        response = productRepository.save(product);
      } catch (Exception e) {
        LOGGER.error(Product.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Product.class.getName(), null, null);
    }
    return response;
  }

  public Product updateProduct(Product product) {
    Product response = null;
    if (product != null) {

      try {
        Product check = productRepository.findById(product.getId());
        if (check != null) {
          response = productRepository.save(product);
        }
      } catch (Exception e) {
        LOGGER.error(Product.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Product.class.getName(), null, null);
    }
    return response;
  }

  public Product deleteProduct(Product product) {
    Product response = null;
    if (product != null) {

      try {
        Product check = productRepository.findById(product.getId());
        if (check != null) {
          productRepository.delete(product);
        }
      } catch (Exception e) {
        LOGGER.error(Product.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Product.class.getName(), null, null);
    }
    return response;
  }

  public Address addAddress(long userID, Address address) {
    Address response = null;
    if (address != null) {
      try {
        User u = userRepository.findByUserId(userID);
        address.setUser(u);
        response = addressRepository.save(address);
      } catch (Exception e) {
        LOGGER.error(User.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(Address.class.getName(), null, null);
    }
    return response;
  }

  public List<AddressProjection> getAddresses(long userID) {
    List<AddressProjection> response = null;
    try {
      User u = userRepository.findByUserId(userID);
      response = addressRepository.findAllProjectedByUser(u);
    } catch (Exception e) {
      LOGGER.error(Address.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;

  }

  private List<Address> getAddressesForUser(long userID) {
    List<Address> response = null;
    try {
      User u = userRepository.findByUserId(userID);
      response = addressRepository.findByUser(u);
    } catch (Exception e) {
      // throw new UsernameNotFoundException(userID + " not found");
      LOGGER.error(userID + " not found" + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;

  }

  private Cart updateCartHelper(long userID, Product product, int quantity) {
    Cart response = null;
    try {
      User u = userRepository.findByUserId(userID);
      Cart c = cartRepository.findByCustomer(u);

      if (c == null) {
        LOGGER.info("Cart for user ID " + userID + " doesn't exist, we will create one");
        c = new Cart();
        c.setCustomer(u);
        cartRepository.saveAndFlush(c);
      }

      CartOrderItem cartItem = cartOrderItemRepository.findByCartAndProduct(c, product);
      if (cartItem == null) {
        Set<CartOrderItem> cartOrderItems = null;
        if (c.getItems() == null) {
          cartOrderItems = new HashSet<>();
        } else if (c.getItems().size() > 0) {
          cartOrderItems = c.getItems();
        } else {
          cartOrderItems = new HashSet<>();
        }

        cartItem = new CartOrderItem();
        cartItem.addMetadata();
        cartItem.setProduct(product);
        cartItem.setTotalAmount(product.getPrice());
        cartItem.setRegularAmount(product.getRegularPrice());
        cartItem.setPayableAmount(cartItem.getTotalAmount());
        cartItem.setCart(c);
        cartItem.setQuantity(1);

        /**
         * Add By Kartikeya Garg
         */
        cartOrderItems.add(cartItem);
        c.setItems(cartOrderItems);

        cartOrderItemRepository.saveAndFlush(cartItem);

        StringBuilder message = new StringBuilder();
        message.append("Details");
        message.append("\n");
        message.append("Email:" + u.getEmailId());
        message.append("\n");
        if (u.getMobileNumber() != null && !u.getMobileNumber().isEmpty()) {
          message.append("Phone Number:" + u.getMobileNumber());
          message.append("\n");
        }
        message.append("Item Slug: " + product.getProductSlug());
        message.append("\n");
        message.append("Item Title: " + product.getProductTitle());
        message.append("\n");
        message.append("Item Id: " + product.getCommodityId());
        message.append("\n");
        message.append("Item Value: " + product.getRegularPrice());
        message.append("\n");

        emailService.sendSimpleMessage("shivam@neostencil.com",
            "New Item Added by " + u.getFullName(), message.toString());
        emailService.sendSimpleMessage("heena@neostencil.com",
            "New Item Added by " + u.getFullName(), message.toString());
        emailService.sendSimpleMessage("ashishsingla@neostencil.com",
            "New Item Added by " + u.getFullName(), message.toString());
        emailService.sendSimpleMessage("ashima@neostencil.com",
            "New Item Added by " + u.getFullName(), message.toString());
        emailService.sendSimpleMessage("shriram@neostencil.com",
            "New Item Added by " + u.getFullName(), message.toString());
        emailService.sendSimpleMessage("anaadi@neostencil.com",
            "New Item Added by " + u.getFullName(), message.toString());

      } else if (quantity == 0) {
        cartOrderItemRepository.delete(cartItem);
        cartOrderItemRepository.flush();
        c.getItems().remove(cartItem);
        cartRepository.saveAndFlush(c);
      } else {
        cartItem.setQuantity(quantity);
        cartOrderItemRepository.saveAndFlush(cartItem);
      }

      response = cartRepository.findByCustomer(u);
    } catch (Exception e) {
      LOGGER.error(Product.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public Cart updateCart(long userID, int productId, int quantity) {
    Cart response = null;
    try {
      Product p = productRepository.findById(productId);
      if (p == null) {
        throw new ProductNotFoundException("Product Not found", new RuntimeException());
      } else {
        response = updateCartHelper(userID, p, quantity);
      }
    } catch (Exception e) {
      LOGGER.error(Product.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public Cart updateCart(long userID, ProductType type, @Valid int commodityId, int quantity) {
    Cart response = null;
    Product p = productRepository.findByCommodityIdAndType(commodityId, type);
    if (p == null) {
      String errorDetails = "Product Not found for type" + type + " id:" + commodityId;
      throw new ProductNotFoundException(errorDetails, new RuntimeException());
      } else {
        response = updateCartHelper(userID, p, quantity);
      }
    return response;
  }

  public Cart getCart(long userID) {
    Cart response = null;
    try {
      User u = userRepository.findByUserId(userID);
      response = cartRepository.findByCustomer(u);
    } catch (Exception e) {
      LOGGER.error(Product.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public double calculateDiscount(Cart cart, Coupon coupon) {
    ValidateCouponResponse response = null;
    try {
      if (cart != null && cart.getCustomer() != null && coupon != null) {
        response = validateCoupon(cart.getCustomer().getUserId(), coupon.getCode());
      }
      if (response != null) {
        return response.getDiscount();
      }
    } catch (Exception e) {
      LOGGER.error(Product.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return 0.0;
  }

  public Order submitOrder(long userID, PaymentModeType paymentModeType, String couponCode,
      int addressId,double neoCashRedeemed) {
    Order rv = null;
    try {
      User u = userRepository.findByUserId(userID);
      Cart cart = cartRepository.findByCustomer(u);

      Coupon coupon = couponRepository.findByCodeIgnoreCase(couponCode);
      Address address = addressRepository.findByAddressId(addressId);

      boolean validNeoCash = false;
      boolean validOrder =false;
      ValidateNeoCashRedemptionResponse validateResponse =null;
      // Confirming the NeoCash redemption
      if (neoCashRedeemed > 0) {
        ValidateNeoCashRedemptionRequest request = new ValidateNeoCashRedemptionRequest();
        request.setNeoCashAmount(neoCashRedeemed);
         validateResponse =
            neoCashService.validateNeoCashRedemption(request);
        validNeoCash = validateResponse.isValid();
        validOrder=validateResponse.isValid();
        
      } else
      {
        validOrder=true;
      }
      Order o = null;
      if (validOrder) {
        o = Order.createOrderFromCart(cart, paymentModeType, coupon,
            calculateDiscount(cart, coupon), address);
      }
      if (validNeoCash) {
        // Setting neoCash per order item
        for (OrderItem oi : o.getOrderItems()) {
          double percent = oi.getPayableAmount() / o.getPayableAmount();
          double neoCashPerOrderItem =Math.round( neoCashRedeemed * percent);
          oi.setNeoCash(neoCashPerOrderItem);
          double payableAmountPerOrderItem=oi.getPayableAmount()-neoCashPerOrderItem;
          oi.setPayableAmount(payableAmountPerOrderItem);
        }
        DebitNeoCashRequest debitNCRequest = new DebitNeoCashRequest();
        debitNCRequest.setAmount(neoCashRedeemed);
        debitNCRequest.setReason("Order Placement");
        debitNCRequest.setOrderId(o.getOrderId());
        neoCashService.debitNeoCash(debitNCRequest);
        o.setNeoCashRedeemed(neoCashRedeemed);
        o.setPayableAmount(validateResponse.getPayableAmount());
      }
        if (coupon != null && paymentModeType.equals(PaymentModeType.neft)
            && calculateDiscount(cart, coupon) > 0) {
          int newNoOfUsages = coupon.getNoOfUsages() + 1;
          coupon.setNoOfUsages(newNoOfUsages);
          couponRepository.save(coupon);
        }
        o.setOrderType(OrderType.REGULAR);

        rv = orderRepository.saveAndFlush(o);
        if (rv != null && paymentModeType != null
            && (paymentModeType.equals(PaymentModeType.neft)
                || paymentModeType.equals(PaymentModeType.free))
            || paymentModeType.equals(PaymentModeType.zestmoney)) {
          this.emptyCart(userID);
          if (paymentModeType.equals(PaymentModeType.neft)) {
            emailService.sendOrderPlacementEmailToCustomerNEFT(u, o);
          }
        }
      

    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return rv;
  }

  public Order submitOrderFromAdminDashboard(OrderSubmitRequest request) {
    Order rv = null;
    if (request != null) {
      try {
        User u = userRepository.findByUserId(request.getUserId());
        Cart cart = cartRepository.findByCustomer(u);

        Coupon coupon = couponRepository.findByCodeIgnoreCase(request.getCouponCode());
        Address address = addressRepository.findByAddressId(request.getAddressId());
        Order o = Order.createOrderFromCart(cart, request.getPaymentModeType(), coupon,
            calculateDiscount(cart, coupon), address);
        if (coupon != null && calculateDiscount(cart, coupon) > 0) {
          int newNoOfUsages = coupon.getNoOfUsages() + 1;
          coupon.setNoOfUsages(newNoOfUsages);
          couponRepository.save(coupon);
        }
        o.setOrderType(OrderType.MANUAL);
        o.setEmi(request.isEmi());
        o.setInstallmentNumber(request.getInstallmentNumber());
        o.setReason(request.getReason());
        if (request.getActualAmount() > 0
            || (request.getActualAmount() == 0 && request.getPaymentModeType() != null
            && request.getPaymentModeType().equals(PaymentModeType.free))) {
          o.setTotalAmount(request.getActualAmount());
          o.setPayableAmount(request.getActualAmount());

          // Assuming the order with EMI or other amount will have a single item only
          for (OrderItem oi : o.getOrderItems()) {
            oi.setPayableAmount(request.getActualAmount());
            //orderItemRepository.saveAndFlush(oi);
          }
        }

        rv = orderRepository.saveAndFlush(o);
        /*
         * if (rv != null && request.getPaymentModeType() != null ||
         * request.getPaymentModeType().equals(PaymentModeType.free)) {
         * this.emptyCart(request.getUserId()); if
         * (request.getPaymentModeType().equals(PaymentModeType.neft)) {
         * emailService.sendOrderPlacementEmailToCustomerNEFT(u, o); } }
         */

      } catch (Exception e) {
        LOGGER.error(Order.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    }
    return rv;
  }

  /**
   * For conditionally submitting free courses
   */
  public OrderResponse submitFreeOrder(OrderSubmitRequest orderRequest) {
    OrderResponse response = null;
    try {
      long userID = jwtTokenUtil.getLoggedInUserID();

      // Figuring out whether the cart contains more items
      User u = userRepository.findByUserId(userID);
      Cart cart = cartRepository.findByCustomer(u);

      double totalAmount = 0;
      for (CartOrderItem cartOrderItem : cart.getItems()) {
        totalAmount += cartOrderItem.getProduct().getPrice();
      }
      Order submittedOrder = null;
      if (totalAmount == 0) {
        submittedOrder = submitOrder(userID, orderRequest.getPaymentModeType(),
            orderRequest.getCouponCode(),
            orderRequest.getAddressId(),orderRequest.getNeoCashRedeemed());
        approveOrder(submittedOrder.getOrderId(), false);
        Order o = orderRepository.findByOrderId(submittedOrder.getOrderId());
        response = new OrderResponse(o);
        response.setFreeOnlyOrder(true);
        for (OrderItem oi : o.getOrderItems()) {
          if (oi != null && oi.getProduct() != null && oi.getProduct().getType() != null
              && oi.getProduct().getType().equals(ProductType.COURSE)
              && oi.getProduct().getCommodityId() != 0) {
            CourseBatch batch = courseBatchRepository.findById(oi.getProduct().getCommodityId());
            if (batch != null) {
              emailService.sendCourseSubscriptionMailToTeacher(u, batch, o);
              String msgString = null;
              if (o.getCustomer() != null && o.getAddress() != null && batch.getCourse() != null) {
                msgString = o.getCustomer().getEmailId() + "," + o.getAddress().getMobileNumber()
                    + " subscribed for course: " + batch.getCourse().getCourseName()
                    + " by NeoStencil";
              }

              if (msgString != null && !msgString.equals("null")) {
                if (batch.getCourse().getInstructors() != null
                    && batch.getCourse().getInstructors().size() > 0) {
                  for (TeacherDetails td : batch.getCourse().getInstructors()) {
                    if (td.getContactNo() != null && !td.getContactNo().isEmpty()
                        && td.getContactNo().startsWith("+91")) {
                      smsService.sendSMSMessage(msgString, td.getContactNo());

                      // For updating teacher statistics
                      // for free orders
                      if (response != null && response.isFreeOnlyOrder()
                          && submittedOrder != null) {
                        updateTeacherStatistics(submittedOrder, true);
                      }
                    }
                  }
                }

              }
            }

          }
        }

      }
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;

  }

  public List<Order> getOrders(long userID) {
    List<Order> customerOrders = null;
    try {
      User customer = userRepository.findByUserId(userID);
      customerOrders = orderRepository.findByCustomer(customer);
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return customerOrders;
  }

  // TODO exception handling is mot right, we need to make it right
  public Order verifyPaymentRazorPay(long userID, int orderId, String paymentId) {
    Payment payment = null;
    Order order = null;
    try {
      // TODO null check at all the places
      User customer = userRepository.findByUserId(userID);
      order = orderRepository.findByOrderId(orderId);
      payment = razorpayClient.Payments.fetch(paymentId);
      LOGGER.debug("Payment details are " + payment);
      // The the Entity.get("attribute_key") method has flexible return
      // types depending on the
      // attribute

      int amount = payment.get("amount");
      int expectedAmount = (int) (order.getPayableAmount() * 100);

      if (amount != expectedAmount) {
        throw new PaymentException("RazorPay amount not matched with expected amount",
            new RuntimeException());
      }

      String id = payment.get("id");
      // Date createdAt = payment.get("created_at");
      JSONObject options = new JSONObject();
      options.put("amount", expectedAmount);
      Payment payment1 = razorpayClient.Payments.capture(paymentId, options);

      // if no exception then mark payment to complete
      order.setOrderStatus(OrderStatusType.PAYMENT_COMPLETE);
      order.setTransactionId(paymentId);
      order = orderRepository.saveAndFlush(order);

      // Now that payment has been verified, get rid of cart item
      Cart cart = cartRepository.findByCustomer(customer);
      if (cart != null && cart.getItems() != null && cart.getItems().size() > 0) {
        for (CartOrderItem item : cart.getItems()) {
          cartOrderItemRepository.delete(item);
        }
        cartRepository.delete(cart);
      }

      if (customer.isKYCVerified())
      {
        approveOrder(orderId, true);
        sendAutoApprovalEmail(order);
      }

    } catch (RazorpayException e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return order;
  }

  public void emptyCart(long userId) {
    try {
      User customer = userRepository.findByUserId(userId);
      Cart cart = cartRepository.findByCustomer(customer);
      if (cart != null && cart.getItems() != null && cart.getItems().size() > 0) {
        for (CartOrderItem item : cart.getItems()) {
          cartOrderItemRepository.delete(item);
          /*
           * cartOrderItemRepository.flush();
           * cart.getItems().remove(item);
           * cartRepository.saveAndFlush(cart);
           */
        }
      }
      cartRepository.delete(cart);
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
  }
  /*
   * public void addUserToBatch(long userId, int batchId) { User user =
   * userRepository.findByUserId(userId); CourseBatch batch =
   * courseBatchRepository.findById(batchId); addUserToBatch(user, batch); }
   */

  public void addUserToBatch(User user, CourseBatch courseBatch, boolean addAllUnits) {
    UserCourseBatchLinkage savedLinkage = null;
    try {
      List<UserCourseBatchLinkage> savedLinkages = userCourseBatchLinkageRepository
          .findByUserAndBatch(user.getUserId(), courseBatch.getId());
      if (savedLinkages != null && savedLinkages.size() > 1) {
        for (UserCourseBatchLinkage link : savedLinkages) {
          if (link.getStatus() != null && link.getStatus().equals(UserCourseLinkageStatus.ACTIVE)) {
            savedLinkage = link;
            break;
          }
        }
      } else if (savedLinkages != null && savedLinkages.size() > 0) {

        savedLinkage = savedLinkages.get(0);
      }
      if (savedLinkage == null || (savedLinkage.getStatus() != null
          && savedLinkage.getStatus().equals(UserCourseLinkageStatus.EXPIRED))) {
        UserCourseBatchLinkage linkage = new UserCourseBatchLinkage();
        linkage.setStatus(UserCourseLinkageStatus.ACTIVE);
        linkage.setCourseBatch(courseBatch);
        // linkage.setStartDate();
        linkage.setUser(user);
        // User's validity is defaulted to 0 here. After approval it
        // will be set to the validity of
        // the
        // batch and can be later on modified to any desired/ required
        // value.
        linkage.setEnrolledOn(Timestamp.valueOf(LocalDateTime.now()));
        Timestamp expiryDate = Timestamp
            .valueOf(LocalDateTime.now().plusDays(courseBatch.getValidity()));
        linkage.setExpiryDate(expiryDate);

        // linkage.setValidity(courseBatch.getValidity());
        userCourseBatchLinkageRepository.saveAndFlush(linkage);
        if (addAllUnits) {
          linkUnitsToUser(user, courseBatch.getUnits());
        }
      }
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
  }

  public Order handlePaymentVerifiedOrder(long userID, Order order) {
    User user = order.getCustomer();
    // For marking default address
    if (user != null && order != null && order.getAddress() != null
        && order.getAddress().getAddressId() != 0) {
      changeDefaultAddress(user.getUserId(), order.getAddress().getAddressId());
    }
    if (user == null) {
      throw new UsernameNotFoundException("Why is there no user for order?",
          new RuntimeException());
    }
    for (OrderItem orderItem : order.getOrderItems()) {
      if (orderItem.getProduct().getType() == ProductType.COURSE) {
        CourseBatch batch = courseBatchRepository.findById(orderItem.getProduct().getCommodityId());
        // addUserToBatch(user, batch);
        // updateTeacherStatistics(user, batch, orderItem);
        // TODO: Send Course purchase email to customer from here with
        // the new template.

        emailService.sendOrderPlacementEmailToCustomerRazorPay(user, order);

      }
    }

    order.setOrderStatus(OrderStatusType.PROCESSED);
    order = orderRepository.saveAndFlush(order);
    if (order.getCoupon() != null) {
      Coupon coupon = couponRepository.findByCodeIgnoreCase(order.getCoupon().getCode());
      if (coupon != null && order.getPaymentMode().equals(PaymentModeType.razorpay)) {
        int newNoOfUsages = coupon.getNoOfUsages() + 1;
        coupon.setNoOfUsages(newNoOfUsages);
        couponRepository.save(coupon);
      }

    }

    return order;
  }

  public Order handleZeroPaymentRazorpayOrder(int orderId) {
    long loggedInUserId = jwtTokenUtil.getLoggedInUserID();
    User user = userRepository.findByUserId(loggedInUserId);

    Order order = orderRepository.findByOrderId(orderId);

    if (order != null && order.getCustomer() != null
        && order.getCustomer().getUserId() == loggedInUserId) {
      if (order.getPayableAmount() == 0 && order.getOrderStatus()
          .equals(OrderStatusType.PENDING_PAYMENT)) {
        order.setOrderStatus(OrderStatusType.PROCESSED);

      }
      if (user != null && order != null && order.getAddress() != null
          && order.getAddress().getAddressId() != 0) {
        changeDefaultAddress(user.getUserId(), order.getAddress().getAddressId());
      }
      for (OrderItem orderItem : order.getOrderItems()) {
        if (orderItem.getProduct().getType() == ProductType.COURSE) {
          CourseBatch batch = courseBatchRepository
              .findById(orderItem.getProduct().getCommodityId());
          // addUserToBatch(user, batch);
          // updateTeacherStatistics(user, batch, orderItem);
          // TODO: Send Course purchase email to customer from here
          // with
          // the new template.

          emailService.sendOrderPlacementEmailToCustomerRazorPay(user, order);

        }
      }
      order = orderRepository.saveAndFlush(order);
      this.emptyCart(loggedInUserId);
      if (order.getCoupon() != null) {
        Coupon coupon = couponRepository.findByCodeIgnoreCase(order.getCoupon().getCode());
        if (coupon != null && order.getPaymentMode().equals(PaymentModeType.razorpay)) {
          int newNoOfUsages = coupon.getNoOfUsages() + 1;
          coupon.setNoOfUsages(newNoOfUsages);
          couponRepository.save(coupon);
        }
      }
    }
    return order;
  }

  /**
   * For updating teachers' statistical data with every enrollment
   */
  public void updateStatsForAUserAnOrderItem(User user, OrderItem oi, boolean visibleToTeacher) {
    try {
      int courseBatchId = oi.getProduct().getCommodityId();
      CourseBatch cb = courseBatchRepository.findById(courseBatchId);
      if (cb != null) {
        StringBuilder courseBatchInfo = new StringBuilder();
        courseBatchInfo
            .append(courseBatchId + "#" + cb.getCourse().getCourseName() + "|" + cb.getBatchName());
        // Finding teacher details and accordingly populating the
        // statistics table
        if (cb.getCourse() != null && cb.getCourse().getInstructors() != null
            && cb.getCourse().getInstructors().size() > 0) {
          for (TeacherDetails td : cb.getCourse().getInstructors()) {
            String teacherEmail = td.getEmailId();
            StringBuilder monthYear1 = new StringBuilder();
            monthYear1.append(
                LocalDateTime.now().getMonth().name() + "#" + LocalDateTime.now().getYear());
            TeacherStatistics ts =
                teacherStatisticsRepository.findByTeacherAndCourseBatchAndMonthYear(td,
                    courseBatchInfo.toString(), monthYear1.toString());
            CustomStudent cs = null;
            // For new entry
            if (ts != null) {
              double contri = oi.getPayableAmount() * (td.getCommissionPercentage() / 100);
              ts.setCommission(ts.getCommission() + contri);
              if (visibleToTeacher) {
                ts.setTeacherCommission(ts.getTeacherCommission() + contri);
                ts.setVisibleToTeacher(visibleToTeacher);
              }
              cs = new CustomStudent();
              cs.setStudent(user);
              cs.setEnrolledOn(Timestamp.valueOf(LocalDateTime.now()));
              cs.setContribution(contri);
              cs.setVisibleToTeacher(visibleToTeacher);
              cs.setStudentStatus(StudentStatusType.ENROLLED);

              Address address = oi.getOrder().getAddress();
              StringBuilder addressText = new StringBuilder();

              addressText.append(address.getAddressText());
              addressText.append(",");
              addressText.append(address.getCity());
              addressText.append(",");
              addressText.append(address.getState());
              addressText.append(",");
              addressText.append(address.getPincode());
              cs.setAddress(addressText.toString());

              cs.setTeacherStatistics(ts);
            } else {
              ts = new TeacherStatistics();
              ts.setTeacher(td);
              ts.setCourseBatch(courseBatchInfo.toString());
              ts.setVisibleToTeacher(visibleToTeacher);
              StringBuilder monthYear = new StringBuilder();
              monthYear.append(
                  LocalDateTime.now().getMonth().name() + "#" + LocalDateTime.now().getYear());

              ts.setMonthYear(monthYear.toString());
              double contri = oi.getPayableAmount() * (td.getCommissionPercentage() / 100);
              ts.setCommission(contri);
              if (visibleToTeacher) {
                ts.setTeacherCommission(ts.getTeacherCommission() + contri);
              }
              cs = new CustomStudent();
              cs.setStudent(user);
              cs.setEnrolledOn(Timestamp.valueOf(LocalDateTime.now()));
              cs.setContribution(contri);
              cs.setVisibleToTeacher(visibleToTeacher);
              cs.setStudentStatus(StudentStatusType.ENROLLED);

              Address address = oi.getOrder().getAddress();
              StringBuilder addressText = new StringBuilder();

              addressText.append(address.getAddressText());
              addressText.append(",");
              addressText.append(address.getCity());
              addressText.append(",");
              addressText.append(address.getState());
              addressText.append(",");
              addressText.append(address.getPincode());
              cs.setAddress(addressText.toString());

              TeacherStatistics teacherStatistics = teacherStatisticsRepository.saveAndFlush(ts);
              cs.setTeacherStatistics(teacherStatistics);
            }

            CustomStudent customStudent = customStudentRepository.saveAndFlush(cs);
            System.out.println(customStudent);
          }
        }
      }
    } catch (Exception e) {
      LOGGER.error(TeacherStatistics.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
  }

  /**
   * For fetching all the orders in the decreasing order of there date of placemen00000000000000t.
   */
  public OrdersResponse fetchAllOrders(Integer page, Integer size) {
    OrdersResponse response = new OrdersResponse();
    try {
      // Sort sort = new Sort(Direction.DESC, new String[]
      // {"paymentDate"});
      // long startTime = System.currentTimeMillis();
      List<OrderProjection> ordersPage = orderRepository.findAllProjectedByOrderByPaymentDateDesc();
      // long endTime = System.currentTimeMillis();

      // System.out.println("Total time spent : " + (endTime -
      // startTime));
      response.setOrders(ordersPage);
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public OrdersResponse fetchAllOrdersWithInRange(Timestamp startDate, Timestamp EndDate) {
    OrdersResponse response = new OrdersResponse();
    try {
      List<OrderProjection> OrdersWithInRange = orderRepository
          .findAllProjectedByPaymentDateBetween(startDate, EndDate);
      response.setOrders(OrdersWithInRange);
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  public Order getOrder(long userID, int orderId) {
    Order order = null;
    try {
      order = orderRepository.findByOrderId(orderId);
      if (order.getCustomer().getUserId() == userID) {
        return order;
      } else {
        throw new NoPermissionException("It appears user is trying to access someone else's order",
            new RuntimeException());
      }
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return order;
  }

  public void confirmPayment(int orderId) {
    Order response = null;
    try {
      if (orderId != 0) {
        Order order = orderRepository.findByOrderId(orderId);

        if (order != null) {
          order.setOrderStatus(OrderStatusType.PROCESSED);
          response = orderRepository.saveAndFlush(order);
          if (response != null) {
            emailService.sendPaymentConfirmationEmail(order.getCustomer(), order);
          }

        }
      }
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

  }

  private void sendAutoApprovalEmail(Order order)
  {
    String message = "Order Autoapproved for Verified customer " + order.getCustomer().getEmailId() + "\n orderID = " + order.getOrderId();
  //  emailService.sendNotificationToDevelopers(message);
    emailService
        .sendSimpleMessage("info@neostencil.com", "Order AutoApproved",
            message);
  }

  public void approveOrder(int orderId, boolean sendNotification) {

    try {
      if (orderId != 0) {
        Order order = orderRepository.findByOrderId(orderId);
        if (order != null) {
          StringBuilder orderItemStr = new StringBuilder();
          // addUserToBatch(order.getCustomer(),);
          if (order.getOrderItems() != null) {
            for (OrderItem oi : order.getOrderItems()) {
              if (oi.getProduct() != null && oi.getProduct().getCommodityId() != 0) {
                if (oi.getProduct().getType() != null
                    && oi.getProduct().getType().equals(ProductType.COURSE)) {
                  orderItemStr.append(oi.getProduct().getCommodityId());
                  orderItemStr.append(",");
                  CourseBatch batch = courseBatchRepository
                      .findById(oi.getProduct().getCommodityId());

                  if (order.isPaidExtension()) {

                    UserBatchLinkagesRequest request = new UserBatchLinkagesRequest();
                    request.setBatchId(oi.getProduct().getCommodityId());
                    List<String> emails = new LinkedList<String>();
                    emails.add(order.getCustomer().getEmailId());
                    request.setEmails(emails);
                    request.setNewValidity(order.getValidity());
                    updateUserCourseBatchLinkages(request);
                    order.setOrderStatus(OrderStatusType.APPROVED);
                    orderRepository.save(order);
                    return;
                  }
                  addUserToBatch(order.getCustomer(), batch, true);
                  // linkUnitsToUser(order.getCustomer(),
                  // batch.getUnits());
                  order.setOrderStatus(OrderStatusType.APPROVED);

                  if (batch != null && batch.getCourse() != null) {
                    Course c = batch.getCourse();
                    int enrolledStudents = c.getStudentsEnrolled();
                    enrolledStudents += 1;
                    c.setStudentsEnrolled(enrolledStudents);
                    courseRepository.saveAndFlush(c);
                  }
                  if (OrderType.REGULAR.equals(order.getOrderType())) {
                    emailService.sendCourseSubscriptionMailToStudent(order.getCustomer(),
                        batch.getCourse().getCourseTitle(), order);
                  } else {
                    String message = "Manual Order created for " + "Items: "
                        + orderItemStr.toString() + " ,for Customer: "
                        + order.getCustomer().getEmailId() + "\nCreated By:"
                        + jwtTokenUtil.getLoggedInUserEmail() + "\nReason :"
                        + order.getReason();
                    emailService.sendNotificationToDevelopers(message);
                    emailService
                        .sendSimpleMessage("gurpreet@neostencil.com", "New Manual order created",
                            message);
                  }
                  if (sendNotification) {
                    emailService.sendCourseSubscriptionMailToTeacher(order.getCustomer(), batch,
                        order);

                  }

                  String msgString = null;
                  if (order.getCustomer() != null && order.getAddress() != null
                      && batch.getCourse() != null) {
                    msgString = order.getCustomer().getEmailId() + ","
                        + order.getAddress().getMobileNumber() + " subscribed for course: "
                        + batch.getCourse().getCourseName() + " by NeoStencil";
                  }

                  if (sendNotification && msgString != null && !msgString.equals("null")) {
                    if (batch.getCourse().getInstructors() != null
                        && batch.getCourse().getInstructors().size() > 0) {
                      for (TeacherDetails td : batch.getCourse().getInstructors()) {
                        Message newMessage = new Message();
                        newMessage.setBody(msgString);
                        newMessage.setMessageUrl(
                            batch.getCourse().getCourseOldSlug() + "--" + batch.getBatchName());
                        if (td.getUserAccount() != null) {
                          newMessage.setMessageTo(td.getUserAccount().getEmailId());
                        }
                        userService.createNotification(newMessage);
                        if (td.getContactNo() != null && !td.getContactNo().isEmpty()
                            && td.getContactNo().startsWith("+91")) {
                          smsService.sendSMSMessage(msgString, td.getContactNo());
                        }
                      }
                    }

                  }

                } else if (ProductType.UNIT.equals(oi.getProduct().getType())) {
                  Unit unit = unitRepository.findByUnitId(oi.getProduct().getCommodityId());
                  if (unit != null) {
                    // TODO: To remove this iteration when
                    // the linkage between unit and
                    // coursebatch
                    // will be one to many instead of many
                    // to many.
                    // Iterator<CourseBatch> it =
                    // unit.getBatches().iterator();
                    CourseBatch cb = unit.getBatch();
                    /*
                     * if (it.hasNext()) { cb = it.next(); }
                     */
                    if (cb != null) {
                      UserCourseBatchLinkage ucbl = userCourseBatchLinkageRepository
                          .findByUserAndBatchAndStatus(order.getCustomer().getUserId(),
                              cb.getId(), UserCourseLinkageStatus.ACTIVE);

                      if (ucbl == null) {
                        addUserToBatch(order.getCustomer(), cb, false);
                      }
                      UserCourseBatchLinkage ucbl1 = userCourseBatchLinkageRepository
                          .findByUserAndBatchAndStatus(order.getCustomer().getUserId(),
                              cb.getId(), UserCourseLinkageStatus.ACTIVE);

                      ucbl1.setRestrictedAccess(true);
                      userCourseBatchLinkageRepository.saveAndFlush(ucbl1);

                      UserUnitLinkage linkage = linkSingleUnitToUser(order.getCustomer(), unit);
                      order.setOrderStatus(OrderStatusType.APPROVED);

                      if (order.getOrderType() != null
                          && order.getOrderType().equals(OrderType.REGULAR)) {
                        emailService.sendCourseSubscriptionMailToStudent(order.getCustomer(),
                            unit.getTitle(), order);
                      }
                      if (sendNotification) {
                        emailService.sendCourseSubscriptionMailToTeacher(order.getCustomer(),
                            cb, order);
                      }

                      String msgString = null;
                      if (order.getCustomer() != null && order.getAddress() != null
                          && linkage.getUnit() != null) {
                        msgString = order.getCustomer().getEmailId() + ","
                            + order.getAddress().getMobileNumber()
                            + " subscribed for course: " + linkage.getUnit().getTitle()
                            + " by NeoStencil";
                      }

                      if (sendNotification) {

                        if (cb.getCourse().getInstructors() != null
                            && cb.getCourse().getInstructors().size() > 0) {
                          for (TeacherDetails td : cb.getCourse().getInstructors()) {
                            if (td.getContactNo() != null && !td.getContactNo().isEmpty()
                                && td.getContactNo().startsWith("+91")) {
                              smsService.sendSMSMessage(msgString, td.getContactNo());
                            }
                          }
                        }
                      }

                    }
                  }
                }
              }
            }
            orderRepository.saveAndFlush(order);

          }

          addStudentRoleToUser(order.getCustomer());
          // Updating the validity of the user for a particular course
          // batch
          updateUserValidity(order);
          // To Populate statistical data

          if (sendNotification) {
            updateTeacherStatistics(order, true);
          } else {
            updateTeacherStatistics(order, false);

          }

          if (order.getPayableAmount() >= 500)
          {
            if (!order.getCustomer().isKYCVerified())
            {
              User u = order.getCustomer();
              u.setKYCVerified(true);
              userRepository.saveAndFlush(u);
            }

          }

        }
      }
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

  }

  /**
   * For refunding the order.
   */
  public void refundOrder(RefundRequest request) {

    int orderId = request.getOrderId();
    if (orderId != 0) {
      Order order = orderRepository.findByOrderId(orderId);
      order.setRefundDate(Timestamp.valueOf(LocalDateTime.now()));
      order.setRefundReason(request.getRefundReason());
      /* if (order != null&& order.getTransactionId()!=null) { */
      /*
       * JSONObject refundRequest = new JSONObject();
       * refundRequest.put("payment_id", order.getTransactionId());
       */
      try {
        // Refund refund =
        // razorpayClient.Payments.refund(refundRequest);
        // if (refund != null) {

        if (order != null && order.getOrderItems() != null && order.getOrderItems().size() > 0) {
          for (OrderItem oi : order.getOrderItems()) {
            UserBatchLinkagesRequest linkageRequest = new UserBatchLinkagesRequest();
            List<String> emails = new ArrayList<String>();
            emails.add(order.getCustomer().getEmailId());
            linkageRequest.setEmails(emails);
            linkageRequest.setNewValidity(0);
            linkageRequest.setBatchId(oi.getProduct().getCommodityId());
            this.updateUserCourseBatchLinkages(linkageRequest);

          }
          order.setOrderStatus(OrderStatusType.REFUNDED);
          order.setRefundAmount(request.getAmount());
          orderRepository.saveAndFlush(order);
          updateTeacherStatsForRefunds(order);
          emailService.sendRefundMailToStudent(order.getCustomer(), order);
        }

      } catch (Exception e) {
        LOGGER.error(Order.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
      /*
       * } else { order.setOrderStatus(OrderStatusType.REFUNDED);
       * orderRepository.saveAndFlush(order); }
       */
    }

  }

  public void updateTeacherStatsForRefunds(Order order) {

    if (order != null && order.getOrderItems() != null && order.getOrderItems().size() > 0) {
      String month = order.getPaymentDate().toLocalDateTime().getMonth().name();
      int year = order.getPaymentDate().toLocalDateTime().getYear();
      for (OrderItem oi : order.getOrderItems()) {
        int courseBatchId = oi.getProduct().getCommodityId();
        CourseBatch batch = courseBatchRepository.findById(courseBatchId);

        Set<TeacherDetails> teacherDetails = batch.getCourse().getInstructors();

        for (TeacherDetails td : teacherDetails) {
          List<TeacherStatistics> teacherStats =
              teacherStatisticsRepository.findByTeacherAndVisibleToTeacherIsTrue(td);
          if (teacherStats != null && teacherStats.size() > 0) {
            for (TeacherStatistics ts : teacherStats) {
              String courseName = ts.getCourseBatch();
              String monthYear = ts.getMonthYear();
              if (courseName.contains(Integer.toString(courseBatchId)) && monthYear.contains(month)
                  && monthYear.contains(Integer.toString(year))) {
                double currentEarning = ts.getCommission();
                double currentTeacherEarnings = ts.getTeacherCommission();
                if (ts.getStudents() != null && ts.getStudents().size() > 0) {
                  for (CustomStudent cs : ts.getStudents()) {
                    if (cs.getStudent().getUserId() == order.getCustomer().getUserId()) {
                      cs.setStudentStatus(StudentStatusType.REFUND);
                      double contri = cs.getContribution();
                      double newEarning = currentEarning - contri;
                      double newTeacherEarnings = currentTeacherEarnings - contri;
                      ts.setCommission(newEarning);
                      ts.setTeacherCommission(newTeacherEarnings);
                      TeacherStatistics teacherStatistics =
                          teacherStatisticsRepository.saveAndFlush(ts);
                      cs.setTeacherStatistics(teacherStatistics);
                      customStudentRepository.saveAndFlush(cs);
                      String msgString = "Student with email " + order.getCustomer().getEmailId()
                          + " got refund for course " + batch.getCourse().getCourseTitle()
                          + " batch " + batch.getBatchName();
                      Message newMessage = new Message();
                      newMessage.setBody(msgString);
                      newMessage.setMessageUrl(
                          batch.getCourse().getCourseOldSlug() + "--" + batch.getBatchName());
                      if (td.getUserAccount() != null) {
                        newMessage.setMessageTo(td.getUserAccount().getEmailId());
                      }
                      userService.createNotification(newMessage);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  public void removeUserFromBatch(User user, int batchId) {
    try {
      if (user != null && batchId != 0) {
        UserCourseBatchLinkage linkage = userCourseBatchLinkageRepository
            .findByUserAndBatchAndStatus(user.getUserId(), batchId, UserCourseLinkageStatus.ACTIVE);
        if (linkage != null) {
          userCourseBatchLinkageRepository.delete(linkage);
        }
      }
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
  }

  public void partialRefundOrder(RefundRequest partialrefundRequest) {
    int orderId = partialrefundRequest.getOrderId();

    Order order = orderRepository.findByOrderId(orderId);
    order.setRefundDate(Timestamp.valueOf(LocalDateTime.now()));
    order.setRefundReason(partialrefundRequest.getRefundReason());
    order.setRefundAmount(partialrefundRequest.getAmount());
    orderRepository.saveAndFlush(order);
    emailService.sendRefundMailToStudent(order.getCustomer(), order);


  }

  public UserUnitLinkage linkSingleUnitToUser(User user, Unit unit) {
    UserUnitLinkage savedLinkage = userUnitLinkageRepository
        .findByUserAndUnit(user.getUserId(), unit.getUnitId());
    if (savedLinkage == null) {
      UserUnitLinkage linkage = new UserUnitLinkage();
      // To check if this needs to change and also provide
      // functionality to
      linkage.setActive(true);
      linkage.addMetadata();
      linkage.setNoOfClicks(0);
      linkage.setResumeFrom(0);
      linkage.setUnit(unit);
      linkage.setUser(user);
      linkage.setWatchStatus(WatchStatus.NOT_STARTED);
      savedLinkage = userUnitLinkageRepository.saveAndFlush(linkage);
    }
    return savedLinkage;
  }

  public void linkUnitsToUser(User user, Set<Unit> units) {
    try {
      if (user != null && units != null && units.size() > 0) {
        for (Unit u : units) {
          linkSingleUnitToUser(user, u);
        }
      }
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
  }

  /**
   * For getting
   */
  public void addStudentRoleToUser(User user) {
    try {
      Authority authority = authRepository.findByName(AuthorityName.ROLE_STUDENT);
      Set<Authority> authorities = user.getAuthorities();
      if (authorities != null && !authorities.isEmpty() && !authorities
          .contains(AuthorityName.ROLE_STUDENT)) {
        authorities.add(authority);
      }
      userRepository.saveAndFlush(user);
    } catch (Exception e) {
      LOGGER.error(User.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
  }

  public void updateUserValidity(Order order) {
    try {
      if (order != null) {
        if (order.getOrderItems() != null && order.getOrderItems().size() > 0) {
          for (OrderItem oi : order.getOrderItems()) {
            if (oi != null) {
              if (oi.getProduct() != null) {
                int courseBatchId = oi.getProduct().getCommodityId();
                CourseBatch cb = courseBatchRepository.findById(courseBatchId);
                UserCourseBatchLinkage linkage = userCourseBatchLinkageRepository
                    .findByUserAndBatchAndStatus(order.getCustomer().getUserId(), courseBatchId,
                        UserCourseLinkageStatus.ACTIVE);
                if (linkage != null) {
                  Timestamp enrolledOn = linkage.getEnrolledOn();
                  if (enrolledOn != null) {
                    LocalDateTime enrolledDate = enrolledOn.toLocalDateTime();
                    Timestamp newExpiryDate = Timestamp
                        .valueOf(enrolledDate.plusDays(cb.getValidity()));
                    linkage.setExpiryDate(newExpiryDate);
                  }
                  UserCourseBatchLinkage savedLinkage = userCourseBatchLinkageRepository
                      .saveAndFlush(linkage);
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
  }

  public void updateCourseBatchValidity(CourseBatch batch, User user) {
    UserCourseBatchLinkage linkage = userCourseBatchLinkageRepository
        .findByUserAndBatchAndStatus(user.getUserId(),
            batch.getId(), UserCourseLinkageStatus.ACTIVE);
    if (linkage != null) {
      Timestamp enrolledOn = linkage.getEnrolledOn();
      if (enrolledOn != null) {
        LocalDateTime enrolledDate = enrolledOn.toLocalDateTime();
        Timestamp newExpiryDate = Timestamp.valueOf(enrolledDate.plusDays(batch.getValidity()));
        linkage.setExpiryDate(newExpiryDate);
      }
      UserCourseBatchLinkage savedLinkage = userCourseBatchLinkageRepository.saveAndFlush(linkage);
    }
  }

  /**
   * This method will just update the statistical data of teachers (also other entities if
   * necessary) at the time of order approval.Putting this here because it is for internal use and
   * can afford to be a little slow because of the various db hits. * @param order
   */
  public void updateTeacherStatistics(Order order, boolean visibleToTeacher) {
    try {
      for (OrderItem oi : order.getOrderItems()) {

        updateStatsForAUserAnOrderItem(order.getCustomer(), oi, visibleToTeacher);

      }
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
  }

  /**
   * For updating the validity of users (or any other attribute as and when required).
   */
  public void updateUserCourseBatchLinkages(UserBatchLinkagesRequest request) {

    if (request != null && request.getBatchId() != 0 && request.getEmails() != null
        && request.getEmails().size() > 0) {
      for (String email : request.getEmails()) {

        try {
          // TODO Assume User link to a batch one time
          UserCourseBatchLinkage ul = userCourseBatchLinkageRepository
              .findByUserEmailIdAndBatch(email,
                  request.getBatchId());
          if (ul != null) {
            // For changing the expiry date of the user as well
            Timestamp expiryDate = ul.getExpiryDate();
            if (expiryDate != null) {
              LocalDateTime expiryDateTime = expiryDate.toLocalDateTime();

              if (LocalDateTime.now().isAfter(expiryDateTime)) {
                expiryDateTime = LocalDateTime.now();
              }
              Timestamp newExpiryDate = null;

              if (request.getNewValidity() == 0) {
                newExpiryDate = Timestamp.valueOf(LocalDateTime.now());
                ul.setStatus(UserCourseLinkageStatus.EXPIRED);
                ul.setExpiryDate(newExpiryDate);
                userCourseBatchLinkageRepository.saveAndFlush(ul);

              } else {
                newExpiryDate = Timestamp
                    .valueOf(expiryDateTime.plusDays(request.getNewValidity()));

                ul.setExpiryDate(newExpiryDate);
                // Updating the status as well
                if (newExpiryDate.after(Timestamp.valueOf(LocalDateTime.now()))
                    && UserCourseLinkageStatus.EXPIRED.equals(ul.getStatus())) {
                  ul.setStatus(UserCourseLinkageStatus.ACTIVE);
                } else if (newExpiryDate.before(Timestamp.valueOf(LocalDateTime.now()))
                    && !UserCourseLinkageStatus.EXPIRED.equals(ul.getStatus())) {
                  ul.setStatus(UserCourseLinkageStatus.EXPIRED);
                }
                userCourseBatchLinkageRepository.saveAndFlush(ul);
              }
            }
          }

        } catch (Exception e) {
          LOGGER.error(Order.class.getName() + " Exception Occured");
          emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
        }

      }
    }

    /*
     * if (linkages != null && linkages.size() > 0) {
     * userCourseBatchLinkageRepository.saveAll(linkages); }
     */
    // return true;
  }

  public ValidateCouponResponse validateCoupon(long userID, String code) {
    ValidateCouponResponse validateCouponResponse = null;
    Coupon coupon = couponRepository.findByCodeIgnoreCase(code);
    User user = userRepository.findByUserId(userID);
    Cart cart = cartRepository.findByCustomer(user);

    if (coupon == null || cart == null
        || (coupon.getNoOfUsages() >= coupon.getUsageLimitPerCoupon())) {

      validateCouponResponse =
          new ValidateCouponResponse(cart, 0, false, "Your coupon code is not valid");
      return validateCouponResponse;
    } else if (!coupon.getExpiryDate().after(new Date())) {
      validateCouponResponse = new ValidateCouponResponse(cart, 0, false, "Coupon has expired");
      return validateCouponResponse;
    } else if (coupon.getAllowedUsers() != null && coupon.getAllowedUsers().size() > 0) {
      if (!coupon.getAllowedUsers().contains(user)) {
        validateCouponResponse =
            new ValidateCouponResponse(cart, 0, false, "Coupon is not applicable");
        return validateCouponResponse;
      }
    }

    // all invalid coupon cases are handled now lets actually apply coupon

    boolean doesCouponExcludeSomeProducts =
        (coupon.getExcludedProducts() != null && coupon.getExcludedProducts().size() > 0);
    boolean doesCouponOnlyAllowSomeProducts =
        (coupon.getIncludedProducts() != null && coupon.getIncludedProducts().size() > 0);

    if (doesCouponExcludeSomeProducts == true && doesCouponOnlyAllowSomeProducts == true) {
      throw new InvalidInputException(Coupon.class.toString(),
          "I am supposed to do magic by allowing  exclude and include both which I cant, I am dying",
          null);
    }

    List<CartOrderItem> discountedItems = new ArrayList<>();
    List<CartOrderItem> notDiscountedItems = new ArrayList<>();
    Set<CartOrderItem> items = cart.getItems();
    double amountOnWhichDiscountToApply = 0.0;

    for (CartOrderItem cartOrderItem : items) {

      if (doesCouponOnlyAllowSomeProducts) {
        if (coupon.getIncludedProducts().contains(cartOrderItem.getProduct())) {

          discountedItems.add(cartOrderItem);
        } else {
          notDiscountedItems.add(cartOrderItem);
        }
      } else if (doesCouponExcludeSomeProducts) {
        if (coupon.getExcludedProducts().contains(cartOrderItem.getProduct())) {

          notDiscountedItems.add(cartOrderItem);
        } else {
          discountedItems.add(cartOrderItem);
        }
      } else {
        discountedItems.add(cartOrderItem);
      }
    }

    for (CartOrderItem item : discountedItems) {
      amountOnWhichDiscountToApply += item.getProduct().getPrice();
    }

    if (amountOnWhichDiscountToApply == 0) {
      validateCouponResponse =
          new ValidateCouponResponse(cart, 0, false, "Coupon is not required ");
      validateCouponResponse.setDiscount(0);
      return validateCouponResponse;
    }
    if (coupon.getMinimumSpend() != 0 && coupon.getMinimumSpend() > amountOnWhichDiscountToApply) {
      validateCouponResponse =
          new ValidateCouponResponse(cart, 0, false, "Coupon is not applicable");
      return validateCouponResponse;
    }

    switch (coupon.getDiscountType()) {
      case FIXED_AMOUNT_DISCOUNT:

        validateCouponResponse =
            new ValidateCouponResponse(cart, coupon.getAmount(), true, "Coupon is applied");
        validateCouponResponse.setDiscount(coupon.getAmount());

        // Updating cartOrderItems with the discounted value

        for (CartOrderItem item : discountedItems) {
          if (item != null) {
            item.setTotalAmount(item.getProduct().getPrice());
            item.setRegularAmount(item.getProduct().getRegularPrice());
            double percent = item.getTotalAmount() / amountOnWhichDiscountToApply;
            double discountPerItem = coupon.getAmount() * percent;
            discountPerItem = Math.round(discountPerItem * 100D) / 100D;
            item.setPayableAmount(item.getTotalAmount() - discountPerItem);
            cartOrderItemRepository.saveAndFlush(item);
          }
        }
        break;

      case PERCENTAGE_DISCOUNT:

        double discount = (coupon.getDiscountPercentage() * (amountOnWhichDiscountToApply)) / 100;

        validateCouponResponse =
            new ValidateCouponResponse(cart, discount, true, "Coupon is applied");
        validateCouponResponse.setDiscount(discount);

        for (CartOrderItem item : discountedItems) {

          double itemDiscount = (coupon.getDiscountPercentage() * (item.getTotalAmount())) / 100;
          itemDiscount = Math.round(itemDiscount * 100D) / 100D;
          item.setPayableAmount(item.getTotalAmount() - itemDiscount);
          cartOrderItemRepository.saveAndFlush(item);
        }

        break;
      case AMOUNT_PERCENTAGE_DISCOUNT:

        double computedDiscount =
            (coupon.getDiscountPercentage() * (amountOnWhichDiscountToApply)) / 100;
        if (computedDiscount > coupon.getAmount()) {

          validateCouponResponse =
              new ValidateCouponResponse(cart, coupon.getAmount(), true, "Coupon is applied");
          validateCouponResponse.setDiscount(coupon.getAmount());
          for (CartOrderItem item : discountedItems) {
            if (item != null) {
              item.setTotalAmount(item.getProduct().getPrice());
              item.setRegularAmount(item.getProduct().getRegularPrice());
              double percent = item.getTotalAmount() / amountOnWhichDiscountToApply;
              double discountPerItem = coupon.getAmount() * percent;
              discountPerItem = Math.round(discountPerItem * 100D) / 100D;
              item.setPayableAmount(item.getTotalAmount() - discountPerItem);
              cartOrderItemRepository.saveAndFlush(item);
            }
          }
        } else {

          validateCouponResponse =
              new ValidateCouponResponse(cart, computedDiscount, true, "Coupon is applied");
          validateCouponResponse.setDiscount(computedDiscount);

          for (CartOrderItem item : discountedItems) {

            double itemDiscount = (coupon.getDiscountPercentage() * (item.getTotalAmount())) / 100;
            itemDiscount = Math.round(itemDiscount * 100D) / 100D;
            item.setPayableAmount(item.getTotalAmount() - itemDiscount);
            cartOrderItemRepository.saveAndFlush(item);
          }

          break;
        }

      default:
        validateCouponResponse =
            new ValidateCouponResponse(cart, 0, false, "Coupon is not applicable");
    }

    // Handling and updating all the non discounted items(if any)

    if (notDiscountedItems != null) {
      for (CartOrderItem item : notDiscountedItems) {
        item.setPayableAmount(item.getTotalAmount());
        cartOrderItemRepository.saveAndFlush(item);
      }
    }
    return validateCouponResponse;


  }


  public CouponProjection fetchCouponByCode(String code) {
    CouponProjection coupon = null;
    if (!TextUtils.isEmpty(code)) {

      try {

        coupon = couponRepository.findAllProjectedByCode(code);

      } catch (Exception e) {

        LOGGER.error(Coupon.class.getName() + " Exception Occured");
        emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new InvalidInputException(TeacherDetails.class.getName(), null, null);
    }

    return coupon;
  }

  public Coupon fetchCouponByCouponCode(int id) {
    Coupon coupon = null;
    try {

      coupon = couponRepository.findById(id);

    } catch (Exception e) {

      LOGGER.error(Coupon.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return coupon;
  }

  /**
   * For changing the default address of the user
   */
  public boolean changeDefaultAddress(long userId, int addressId) {
    boolean success = false;
    try {
      if (userId != 0 && addressId != 0) {
        List<Address> addresses = getAddressesForUser(userId);
        List<Address> result = new LinkedList<Address>();
        if (addresses != null && addresses.size() > 0) {
          for (Address ad : addresses) {
            if (ad != null) {
              if (ad.isDefaultAddress() && ad.getAddressId() != addressId) {
                ad.setDefaultAddress(false);
                result.add(ad);
              } else if (ad.getAddressId() == addressId) {
                ad.setDefaultAddress(true);
                result.add(ad);

              } else {
                result.add(ad);
              }
            }
          }
          addressRepository.saveAll(result);
          return true;
        }
      }
    } catch (Exception e) {
      LOGGER.error(Address.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return success;
  }

  public boolean deleteAddress(int addressId) {
    try {
      if (addressId != 0) {
        addressRepository.deleteById(addressId);
        return true;
      }
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return false;
  }

  public boolean exportData(DataExportRequest request, Writer writer) {

    boolean isExport = false;

    if (request != null && request.getEntityName() != null) {
      switch (request.getEntityName()) {
        case "Coupon":

          Specification<Coupon> couponSpec = null;
          CouponSpecificationBuilder couponBuilder = new CouponSpecificationBuilder();
          List<Coupon> couponList = null;

          if (request.getDataFields() != null && request.getDataFields().size() > 0) {
            for (DataFieldRequest req : request.getDataFields()) {
              couponBuilder.with(req.getFieldName(), req.getFieldValue());
            }
            couponSpec = couponBuilder.build();
            couponList = couponRepository.findAllByCreatedAtBetween(request.getStartDate(),
                request.getEndDate());

          } else {
            couponList = couponRepository.findAllByCreatedAtBetween(request.getStartDate(),
                request.getEndDate());
          }
          List<CouponDTO> couponDTOs = new ArrayList<CouponDTO>();
          if (couponList != null && couponList.size() > 0) {
            for (Coupon coupon : couponList) {
              couponDTOs.add(assembler.toCouponDTO(coupon));
            }
          }
          CSVUtility.writeCouponsToCsv(writer, couponDTOs, request.getDataFields());
          isExport = true;
          break;
        case "Product":

          Specification<Product> prodSpec = null;
          ProductSpecificationBuilder prodBuilder = new ProductSpecificationBuilder();

          List<Product> productList = null;
          if (request.getDataFields() != null && request.getDataFields().size() > 0) {
            for (DataFieldRequest req : request.getDataFields()) {
              prodBuilder.with(req.getFieldName(), req.getFieldValue());
            }

            prodSpec = prodBuilder.build();
            productList = productRepository.findAll(prodSpec);
          } else {
            productList = productRepository.findAllByCreatedAtBetween(request.getStartDate(),
                request.getEndDate());
          }

          if (productList != null && productList.size() > 0) {
            CSVUtility.writeProductsToCsv(writer, productList, request.getDataFields());
            isExport = true;
          }
          break;
        case "Order":
          Specification<Order> orderSpec = null;
          OrderSpecificationBuilder orderBuilder = new OrderSpecificationBuilder();

          List<Order> orderList = null;
          if (request.getDataFields() != null && request.getDataFields().size() > 0) {
            for (DataFieldRequest req : request.getDataFields()) {
              orderBuilder.with(req.getFieldName(), req.getFieldValue());
            }

            orderSpec = orderBuilder.build();
            orderList = orderRepository.findAll(orderSpec);
          } else {
            Timestamp endDate =
                Timestamp.valueOf(request.getEndDate().toLocalDateTime().plusDays(1));
            orderList = orderRepository.findByPaymentDateBetween(request.getStartDate(), endDate);
          }

          List<OrderDTO> orders = new ArrayList<OrderDTO>();
          if (orderList != null && orderList.size() > 0) {
            for (Order order : orderList) {
              if (order != null) {
                orders.addAll(assembler.toOrderDTO(order));
              }
            }
          }
          CSVUtility.writeOrdersToCsv(writer, orders, request.getDataFields());
          isExport = true;
          break;

      }

    }
    return isExport;
  }

  public OrderProjection fetchOrderById(int id) {
    OrderProjection response = null;
    try {
      response = orderRepository.findAllProjectedByOrderId(id);
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    /*
     * if (response != null && response.getCustomer() != null &&
     * response.getCustomer().getUserId() != 0.0) { List<Address> addresses
     * = getAddresses(response.getCustomer().getUserId()); if (addresses !=
     * null && addresses.size() > 0) { for (Address ad : addresses) { if
     * (ad.isDefaultAddress()) { response.setAddress(ad); break; } } } }
     */
    return response;
  }

  public String uploadCSVToCloud(String fileName) {
    File convFile = new File(fileName);
    String downloadFileUrl = null;
    try {
      downloadFileUrl = baseService.uploadFile("OrderExport", fileName, "text/csv", convFile,
          "ns-web-storage.appspot.com", "OrderExport");
    } catch (IOException e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    } catch (GeneralSecurityException e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
    return downloadFileUrl;
  }

  /*public SalesReportResponse generateSalesReport(SalesReportRequest request) {
    SalesReportResponse response = new SalesReportResponse();
    double grossSales = 0.0;
    double netSales = 0.0;
    double grossAvgDailySales = 0.0;
    double netAvgDailySales = 0.0;
    int noOfOrders = 0;
    int noOfItems = 0;
    int noOfRefundedOrders = 0;
    double refundValue = 0.0;
    double totalShippingCharged = 0.0;
    double totalCouponDiscount = 0.0;
    double grossAvgMonthlySales = 0.0;
    double netAvgMonthlySales = 0.0;
    boolean calculateMonthly = false;
    int noOfFreeItems = 0;
    int noOfPaidItems = 0;
    int noOfManualItems = 0;
    int noOfFreeOrders = 0;
    int noOfPaidOrders = 0;
    int noOfManualOrders = 0;
    double netAvgMonthlySalesRegular = 0.0;
    double netAvgMonthlySalesManual = 0.0;
    double grossAvgMonthlySalesRegular = 0.0;
    double grossAvgMonthlySalesManual = 0.0;
    double grossAvgDailySalesRegular = 0.0;
    double netAvgDailySalesRegular = 0.0;
    double grossAvgDailySalesManual = 0.0;
    double netAvgDailySalesManual = 0.0;
    double grossSalesManual = 0.0;
    double grossSalesRegular = 0.0;
    double netSalesRegular = 0.0;
    double netSalesManual = 0.0;
    double totalCouponDiscountManual = 0.0;
    double totalCouponDiscountRegular = 0.0;
    int noOfPartialRefunds = 0;
    double partialRefundValue = 0.0;

    try {
      if (request != null) {
        long miliSeconds = request.getEndDate().getTime() - request.getStartDate().getTime();
        long seconds = miliSeconds / 1000;
        long hours = seconds / 3600;
        int noOfDays = (int) hours / 24;

        request.setStartDate(
            Timestamp.valueOf(request.getStartDate().toLocalDateTime().with(LocalTime.MIN)));

        request.setEndDate(
            Timestamp.valueOf(request.getEndDate().toLocalDateTime().with(LocalTime.MAX)));

        // request.setEndDate(Timestamp.valueOf(request.getEndDate().toLocalDateTime().plusDays(1)));

        if (noOfDays >= 365) {
          calculateMonthly = true;
        }

        List<Order> orders = orderRepository.findByPaymentDateBetween(request.getStartDate(),
            request.getEndDate());
        if (orders != null && orders.size() > 0) {
          for (Order o : orders) {
            if (o != null) {
              if (o.getOrderStatus() != null && o.getOrderStatus()
                  .equals(OrderStatusType.REFUNDED)) {
                noOfRefundedOrders += 1;
                refundValue += o.getRefundAmount();
                noOfOrders += 1;
                noOfItems += o.getOrderItems().size();

                if (o.getOrderType() != null && o.getOrderType().equals(OrderType.MANUAL)) {
                  noOfManualOrders += 1;
                  noOfManualItems += o.getOrderItems().size();
                } else if (o.getTotalAmount() > 0) {
                  noOfPaidOrders += 1;
                  noOfPaidItems += o.getOrderItems().size();
                }

                if (o.getDiscount() != 0) {
                  totalCouponDiscount += o.getDiscount();
                }

                if (o.getOrderType() != null && o.getOrderType().equals(OrderType.MANUAL)) {
                  totalCouponDiscountManual += o.getDiscount();
                } else {
                  totalCouponDiscountRegular += o.getDiscount();
                }
              } else if (o.getOrderStatus() != null
                  && (o.getOrderStatus().equals(OrderStatusType.APPROVED)
                  || o.getOrderStatus().equals(OrderStatusType.PROCESSED))) {

                if (o.getOrderType() != null && o.getOrderType().equals(OrderType.MANUAL)) {
                  noOfManualOrders += 1;
                  noOfManualItems += o.getOrderItems().size();

                } else {
                  if (o.getTotalAmount() > 0) {
                    noOfPaidOrders += 1;
                    noOfPaidItems += o.getOrderItems().size();
                  } else {
                    noOfFreeOrders += 1;
                    noOfFreeItems += o.getOrderItems().size();
                  }
                }
                grossSales += o.getTotalAmount();
                netSales += o.getPayableAmount();

                if (o.getOrderType() != null && o.getOrderType().equals(OrderType.MANUAL)) {
                  grossSalesManual += o.getTotalAmount();
                  netSalesManual += o.getPayableAmount();
                } else {
                  grossSalesRegular += o.getTotalAmount();
                  netSalesRegular += o.getPayableAmount();
                }

                noOfOrders += 1;
                noOfItems += o.getOrderItems().size();
                if (o.getDiscount() != 0) {
                  totalCouponDiscount += o.getDiscount();
                }

                if (o.getOrderType() != null && o.getOrderType().equals(OrderType.MANUAL)) {
                  totalCouponDiscountManual += o.getDiscount();
                } else {
                  totalCouponDiscountRegular += o.getDiscount();
                }

              }
              if (o.getOrderStatus() != null
                  && o.getOrderStatus().equals(OrderStatusType.APPROVED)
                  && o.getRefundAmount() > 0) {
                noOfPartialRefunds += 1;
                partialRefundValue += o.getRefundAmount();
              }

            }
          }
          if (noOfDays > 0) {
            grossAvgDailySales = grossSales / noOfDays;
            grossAvgDailySalesManual = grossSalesManual / noOfDays;
            grossAvgDailySalesRegular = grossSalesRegular / noOfDays;

          } else {
            grossAvgDailySales = grossSales;
            grossAvgDailySalesManual = grossSalesManual;
            grossAvgDailySalesRegular = grossSalesRegular;
          }

          if (noOfDays > 0) {
            netAvgDailySales = netSales / noOfDays;
            netAvgDailySalesManual = netSalesManual / noOfDays;
            netAvgDailySalesRegular = netSalesRegular / noOfDays;
          } else {
            netAvgDailySales = netSales;
            netAvgDailySalesManual = netSalesManual;
            netAvgDailySalesRegular = netSalesRegular;
          }
          if (calculateMonthly) {
            grossAvgMonthlySales = grossSales / 30;
            netAvgMonthlySales = netSales / 30;

            grossAvgMonthlySalesManual = grossSalesManual / 30;
            netAvgMonthlySalesManual = netSalesManual / 30;

            grossAvgMonthlySalesRegular = grossSalesRegular / 30;
            netAvgMonthlySalesRegular = netSalesRegular / 30;
          }

          response.setGrossAvgDailySales(grossAvgDailySales);
          response.setGrossSales(grossSales);
          response.setNetAvgDailySales(netAvgDailySales);
          response.setNetSales(netSales);
          response.setNoOfItems(noOfPaidItems + noOfFreeItems);
          response.setNoOfOrders(noOfPaidOrders + noOfFreeOrders);
          response.setNoOfRefundedOrders(noOfRefundedOrders);
          response.setRefundValue(refundValue);
          response.setTotalCouponDiscount(totalCouponDiscount);
          response.setTotalShippingCharged(totalShippingCharged);
          response.setGrossAvgMonthlySales(grossAvgMonthlySales);
          response.setNetAvgMonthlySales(netAvgMonthlySales);
          response.setNoOfFreeItems(noOfFreeItems);
          response.setNoOfFreeOrders(noOfFreeOrders);
          response.setNoOfManualItems(noOfManualItems);
          response.setNoOfManualOrders(noOfManualOrders);
          response.setNoOfPaidItems(noOfPaidItems);
          response.setNoOfPaidOrders(noOfPaidOrders);

          
           * double netAvgMonthlySalesRegular = 0.0; double
           * netAvgMonthlySalesManual = 0.0; double
           * grossAvgMonthlySalesRegular = 0.0; double
           * grossAvgMonthlySalesManual = 0.0; double
           * grossAvgDailySalesRegular = 0.0; double
           * netAvgDailySalesRegular = 0.0; double
           * grossAvgDailySalesManual = 0.0; double
           * netAvgDailySalesManual = 0.0; double grossSalesManual =
           * 0.0; double grossSalesRegular = 0.0; double
           * netSalesRegular = 0.0; double netSalesManual = 0.0;
           * double totalCouponDiscountManual = 0.0; double
           * totalCouponDiscountRegular = 0.0;
           

          response.setNetAvgMonthlySalesManual(netAvgMonthlySalesManual);
          response.setNetAvgMonthlySalesRegular(netAvgMonthlySalesRegular);
          response.setGrossAvgMonthlySalesRegular(grossAvgMonthlySalesRegular);
          response.setGrossAvgMonthlySalesManual(grossAvgMonthlySalesManual);
          response.setGrossAvgDailySalesRegular(grossAvgDailySalesRegular);
          response.setNetAvgDailySalesRegular(netAvgDailySalesRegular);
          response.setGrossAvgDailySalesManual(grossAvgDailySalesManual);
          response.setNetAvgDailySalesManual(netAvgDailySalesManual);
          response.setGrossSalesManual(grossSalesManual);
          response.setGrossSalesRegular(grossSalesRegular);
          response.setNetSalesRegular(netSalesRegular);
          response.setNetSalesManual(netSalesManual);
          response.setTotalCouponDiscountManual(totalCouponDiscountManual);
          response.setTotalCouponDiscountRegular(totalCouponDiscountRegular);
          response.setPartialRefundValue(partialRefundValue);
          response.setNoOfPartialRefunds(noOfPartialRefunds);


        }
      }
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;
  }*/

  public OrderStatusSummaryResponse fetchOrderStatusSummary(Timestamp startDate, Timestamp endDate) {
    List<OrderProjection> orders = orderRepository
        .findAllProjectedByPaymentDateBetween(startDate, endDate);
    int noOfRefunds = 0;
    int noOfProcessed = 0;
    int noOfPendingPayment = 0;
    int noOfPaymentComplete = 0;
    int noOfPaymentFailed = 0;
    int noOfCancelled = 0;
    int noOfApproved = 0;
    int noOfOnHold = 0;
    OrderStatusSummaryResponse response = new OrderStatusSummaryResponse();
    try {
      if (orders != null && orders.size() > 0) {
        for (OrderProjection o : orders) {
          if (o != null) {
            switch (o.getOrderStatus()) {
              case REFUNDED:
                noOfRefunds += 1;
                break;
              case PROCESSED:
                noOfProcessed += 1;
                break;
              case PENDING_PAYMENT:
                noOfPendingPayment += 1;
                break;
              case PAYMENT_COMPLETE:
                noOfPaymentComplete += 1;
                break;
              case PAYMENT_FAILED:
                noOfPaymentFailed += 1;
                break;
              case CANCELLED:
                noOfCancelled += 1;
                break;
              case APPROVED:
                noOfApproved += 1;
                break;
              case ON_HOLD:
                noOfOnHold += 1;
                break;
              case PENDING_INTERNAL_PROCESSING:
                break;
              case PENDING_PAYMENT_VERIFICATON:
                break;
              default:
                break;
            }
          }
        }

        response.setNoOfApproved(noOfApproved);
        response.setNoOfCancelled(noOfCancelled);
        response.setNoOfPaymentComplete(noOfPaymentComplete);
        response.setNoOfPaymentFailed(noOfPaymentFailed);
        response.setNoOfPendingPayment(noOfPendingPayment);
        response.setNoOfProcessed(noOfProcessed);
        response.setNoOfRefunds(noOfRefunds);
        response.setNoOfOnHold(noOfOnHold);
      }
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

    return response;

  }

  public void addNoteToOrder(int orderId, OrderNotes newNote) {

    try {
      String userName = jwtTokenUtil.getLoggedInUserName();
      Timestamp notedAt = Timestamp.valueOf(LocalDateTime.now());
      newNote.setAddedBy(userName);
      newNote.setNoteDate(notedAt);
      Order o = orderRepository.findByOrderId(orderId);

      if (o != null) {
        if (o.getOrderNotes() != null && o.getOrderNotes().size() > 0) {

          o.getOrderNotes().add(newNote);

        } else {
          List<OrderNotes> newList = new LinkedList<OrderNotes>();
          newList.add(newNote);
          o.setOrderNotes(newList);
        }

        orderRepository.save(o);
      }
    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }

  }

  /**
   * Utility service to create one user with all the batches and units
   */

  public void linkUserToAllBatches(long userId) {
    try {
      User user = userRepository.findByUserId(userId);

      addStudentRoleToUser(user);

      List<CourseBatch> batches = courseBatchRepository.findByStatus(PublishStatus.publish);

      if (batches != null && batches.size() > 0) {
        for (CourseBatch cb : batches) {
          addUserToBatch(user, cb, true);

        }
      }

      if (batches != null && batches.size() > 0) {
        for (CourseBatch cb : batches) {
          if (cb.getUnits() != null && cb.getUnits().size() > 0) {
            linkUnitsToUser(user, cb.getUnits());
          }
        }
      }

    } catch (Exception e) {
      LOGGER.error(Order.class.getName() + " Exception Occured");
      emailService.sendErrorLogsToDevelopers(ExceptionUtils.getStackTrace(e));
    }
  }

  /**
   * For giving direct access to the user
   */
  public String giveCourseAccessToUser(String userEmailId, int batchId) {
    if (userEmailId != null && batchId != 0) {
      User user = userRepository.findByEmailId(userEmailId);
      CourseBatch batch = courseBatchRepository.findById(batchId);

      addUserToBatch(user, batch, true);
      addStudentRoleToUser(user);
      updateCourseBatchValidity(batch, user);
      String message =
          "Access given to user with email Id " + userEmailId + " of batch with Id " + batchId;
      emailService.sendNotificationToDevelopers(message);
      return "success";
    }
    return "error";

  }

  /**
   * For giving per lecture access to the students
   */
  public boolean givePaidExtension(ExtensionRequest request) {
    if (request != null) {
      OrderItem item = new OrderItem();
      Product product = productRepository
          .findByCommodityIdAndType(request.getBatchId(), ProductType.COURSE);
      item.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
      item.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
      item.setPrice(product.getPrice());
      item.setProduct(product);
      item.setQuantity(1);
      item.setSubtotal(product.getPrice());

      User user = userRepository.findByEmailId(request.getEmail());
      Address address = null;

      if (user != null) {
        List<Address> addresses = addressRepository.findByUser(user);
        if (addresses != null && addresses.size() > 0) {
          for (Address ad : addresses) {
            if (ad.isDefaultAddress()) {
              address = ad;
              break;
            }
          }
          if (address == null) {
            address = addresses.get(0);
          }
        }
      }

      Set<OrderItem> items = new LinkedHashSet<OrderItem>();
      OrderItem it = orderItemRepository.saveAndFlush(item);
      items.add(it);

      Order order = new Order(request.getAmount(), request.getAmount(), OrderStatusType.PROCESSED,
          request.getPaymentMode(), Timestamp.valueOf(LocalDateTime.now()), null,
          Timestamp.valueOf(LocalDateTime.now()), null, items, 0.0, user, address);
      order.setPaidExtension(true);
      order.setValidity(request.getValidity());
      order.setOrderType(OrderType.MANUAL);
      order.setReason("Per Lecture Access");
      order.setOrderItems(items);
      Order o = orderRepository.saveAndFlush(order);
      it.setOrder(o);
      orderItemRepository.saveAndFlush(it);
      return true;
    }
    return false;
  }

  public void createZestmoneyToken() {

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.add("client_id", zestmoneyClientId);
    map.add("client_secret", zestmoneyclientSecret);
    map.add("grant_type", zestmoneyGrantType);
    map.add("scope", zestmoneyScope);

    HttpEntity<MultiValueMap<String, String>> request =
        new HttpEntity<MultiValueMap<String, String>>(map, headers);
    String url = zestmoneyAuthUrl;

    ResponseEntity<ZestmoneyToken> response =
        restTemplate.postForEntity(url, request, ZestmoneyToken.class);

    LOGGER.info(response.toString());

    ZestmoneyToken zestmoneyToken = response.getBody();
    String token = zestmoneyToken.getToken_type() + " " + zestmoneyToken.getAccess_token();

    zestmoneyTokenMap.put("Authorization",token);

  }

  public ZestmoneyOrderResponse createZestmoneyOrder(CreateZestmoneyOrderRequest request,
      String baseURL) {

    Order order = orderRepository.findByOrderId(request.getOrderId());
    ZestmoneyOrderResponse zestmoneyOrderResponse = new ZestmoneyOrderResponse();
    if (order != null) {
      ZestmoneyOrderRequest zestmoneyOrderRequest = new ZestmoneyOrderRequest();

      zestmoneyOrderRequest.setBasketAmount(String.valueOf(order.getPayableAmount()));
      zestmoneyOrderRequest.setOrderId(String.valueOf(order.getOrderId()));
      zestmoneyOrderRequest.setReturnUrl(websiteHomePageURL+"/thank-you?orderId=" + order.getOrderId());
      zestmoneyOrderRequest.setApprovedUrl(websiteHomePageURL+"/thank-you?orderId=" + order.getOrderId());
      zestmoneyOrderRequest.setMerchantCustomerId(String.valueOf(order.getCustomer().getUserId()));
      zestmoneyOrderRequest.setEmailAddress(order.getCustomer().getEmailId());
      zestmoneyOrderRequest.setFullName(order.getCustomer().getFullName());

      StringBuilder address = new StringBuilder();
      address.append(order.getAddress().getAddressText().replaceAll(",", " | "));
      address.append(" | ");
      address.append(order.getAddress().getCity());
      address.append(" | ");
      address.append(order.getAddress().getState());
      address.append(" - ");
      address.append(order.getAddress().getPincode());

      zestmoneyOrderRequest.setAddressLine1(address.toString().replaceAll("\n", " "));
      zestmoneyOrderRequest.setMobileNumber(order.getAddress().getMobileNumber());
      zestmoneyOrderRequest.setDeliveryPostCode(order.getAddress().getPincode());

      List<ZestmoneyOrderItemRequest> itemRequestList = new ArrayList<>();

      for (OrderItem orderItem : order.getOrderItems()) {

        ZestmoneyOrderItemRequest zestmoneyOrderItemRequest = new ZestmoneyOrderItemRequest();

        zestmoneyOrderItemRequest.setId(String.valueOf(orderItem.getItemId()));
        zestmoneyOrderItemRequest.setQuantity(String.valueOf(orderItem.getQuantity()));
        zestmoneyOrderItemRequest.setTotalPrice(String.valueOf(orderItem.getPayableAmount()));
        zestmoneyOrderItemRequest.setCategory(orderItem.getProduct().getType().toString());
        zestmoneyOrderItemRequest.setDescription(orderItem.getProduct().getProductTitle());

        itemRequestList.add(zestmoneyOrderItemRequest);

      }

      zestmoneyOrderRequest.setBasket(itemRequestList);

      RestTemplate restTemplate = new RestTemplate();

      HttpHeaders headers = new HttpHeaders();

      headers.set("Content-Type", MediaType.APPLICATION_JSON.toString());
      headers.set("Authorization", zestmoneyTokenMap.get("Authorization"));

      HttpEntity<ZestmoneyOrderRequest> requestBody =
          new HttpEntity<>(zestmoneyOrderRequest, headers);
      String url = zestmoneyBaseUrl + "ApplicationFlow/LoanApplications?mid=" + zestmoneyClientId;

      ResponseEntity<String> response = null;

      try {
         response =
            restTemplate.exchange(url, HttpMethod.POST, requestBody, String.class);
      } catch (Exception e) {

            /*zestmoneyOrderResponse.setCode(
                String.valueOf(((HttpClientErrorException) e).getStatusCode().value()));*/

            createZestmoneyToken();

            headers.set("Authorization", zestmoneyTokenMap.get("Authorization"));
            requestBody =
                new HttpEntity<>(zestmoneyOrderRequest, headers);
            response =
                restTemplate.exchange(url, HttpMethod.POST, requestBody, String.class);




        /*return zestmoneyOrderResponse;*/
      }

      if(response.getBody()!=null){
        zestmoneyOrderResponse.setCode(String.valueOf(response.getStatusCode().value()));

        zestmoneyOrderResponse.setUrl(new JSONObject(response.getBody()).getString("LogonUrl"));
      }


      LOGGER.info(response.toString());
    }

    return zestmoneyOrderResponse;

  }


  public GetZestmoneyOrderStatusResponse getZestmoneyOrderStatus(int orderId){

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();

    headers.set("Authorization", zestmoneyTokenMap.get("Authorization"));

    HttpEntity<String> requestBody =
        new HttpEntity<>(headers);

    String url = zestmoneyBaseUrl + "ApplicationFlow/LoanApplications/orders"+orderId;

    ResponseEntity<GetZestmoneyOrderStatusResponse> response = null;

    try{
      response = restTemplate
          .exchange(url, HttpMethod.GET, requestBody, GetZestmoneyOrderStatusResponse.class);
    }catch (Exception e){

      if(e.getClass()==HttpClientErrorException.class){
        if (((HttpClientErrorException) e).getStatusCode().value() == 401) {

          createZestmoneyToken();

          headers.set("Authorization", zestmoneyTokenMap.get("Authorization"));
          requestBody =
              new HttpEntity<>(headers);
          response = restTemplate
              .exchange(url, HttpMethod.GET, requestBody, GetZestmoneyOrderStatusResponse.class);

        }
      }

    }
    return response.getBody();

  }
  
  /**
   * For generating the sales report
   * @param request
   * @return
   */
  public SalesReportResponse generateSalesReport(SalesReportRequest request) {

    SalesReportResponse response = new SalesReportResponse();

    Timestamp startDate = request.getStartDate();
    Timestamp endDate = request.getEndDate();
    endDate=Timestamp.valueOf(endDate.toLocalDateTime().plusDays(1));

    long miliSeconds = endDate.getTime() - startDate.getTime();
    long seconds = miliSeconds / 1000;
    long hours = seconds / 3600;
    int noOfDays = (int) hours / 24;

    List<ExamSegmentTypes> examSegments = new ArrayList<ExamSegmentTypes>();

    if (request.getExamSegments() != null && request.getExamSegments().length > 0) {

      for (String exam : request.getExamSegments()) {
        examSegments.add(ExamSegmentTypes.valueOf(exam));
      }
      if (examSegments.contains(ExamSegmentTypes.IES_GATE)) {
        examSegments.add(ExamSegmentTypes.IES);
        examSegments.add(ExamSegmentTypes.GATE);
      }
    }

    List<OrderType> orderTypes = new ArrayList<OrderType>();

    if (request.getOrderTypes() != null) {
      for (String type : request.getOrderTypes()) {
        orderTypes.add(OrderType.valueOf(type));
      }
      /*
       * StringTokenizer token = new StringTokenizer(request.getOrderTypes(), ","); while
       * (token.hasMoreTokens()) { orderTypes.add(OrderType.valueOf(token.nextToken())); }
       */
    }

    List<Order> paidOrders =
        orderRepository.findAllPaidOrderDetails(examSegments, startDate, endDate, orderTypes);

    PaidOrdersReport paidOrdersReport =
        this.calculatePaidOrderReport(paidOrders, noOfDays, startDate);

    CouponReport couponReport = this.getCouponReport(paidOrders);

    response.setCouponReport(couponReport);

    response.setPaidOrdersReport(paidOrdersReport);



    List<Order> freeOrders =
        orderRepository.findAllFreeOrderDetails(examSegments, startDate, endDate, orderTypes);

    FreeOrdersReport freeOrdersReport =
        this.calculateFreeOrdersReport(freeOrders, noOfDays, startDate);
    response.setFreeOrdersReport(freeOrdersReport);

    List<Order> refundedOrders =
        orderRepository.findAllRefundOrderDetails(examSegments, startDate, endDate, orderTypes);

    List<Order> partiallyRefundedOrders =
        orderRepository.findAllPartialOrderDetails(examSegments, startDate, endDate, orderTypes);

    RefundReport refundReport = this.getRefundReport(refundedOrders, partiallyRefundedOrders);

    response.setRefundReport(refundReport);

    return response;


  }

  public RefundReport getRefundReport(List<Order> refundedOrders,
      List<Order> partiallyRefundedOrders) {
    RefundReport report = new RefundReport();

    int noOfFullRefunds = 0;
    double fullRefundValue = 0;
    int noOfPartialRefunds = 0;
    double partialRefundValue = 0;

    for (Order o : refundedOrders) {
      fullRefundValue += o.getRefundAmount();
    }

    for (Order o : partiallyRefundedOrders) {
      partialRefundValue += o.getRefundAmount();
    }

    noOfFullRefunds = refundedOrders.size();
    noOfPartialRefunds = partiallyRefundedOrders.size();

    report.setFullRefundValue(fullRefundValue);
    report.setNoOfFullRefunds(noOfFullRefunds);
    report.setNoOfPartialRefunds(noOfPartialRefunds);
    report.setPartialRefundValue(partialRefundValue);

    return report;


  }

  public CouponReport getCouponReport(List<Order> orders) {
    CouponReport report = new CouponReport();

    int totalNoOfCouponsUsed = 0;
    double totalDiscountValue = 0;
    StringBuilder mostPopularCouponsStb = new StringBuilder();

    Map<String, Integer> couponUsageMap = new HashMap<String, Integer>();

    for (Order o : orders) {
      if (o != null && o.getCoupon() != null) {
        totalNoOfCouponsUsed += 1;
        totalDiscountValue += o.getDiscount();

        if (couponUsageMap.containsKey(o.getCoupon().getCode())) {
          int count = couponUsageMap.get(o.getCoupon().getCode());
          couponUsageMap.put(o.getCoupon().getCode(), count + 1);
        } else {
          couponUsageMap.put(o.getCoupon().getCode(), 1);

        }
      }
    }

    Map<String, Integer> sortedMap = couponUsageMap.entrySet().stream()
        .sorted((Map.Entry.<String, Integer>comparingByValue().reversed())).collect(Collectors
            .toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    if (sortedMap != null && sortedMap.size() > 0) {
      int n = 0;
      for (String code : sortedMap.keySet()) {
        if (n < 5) {
          mostPopularCouponsStb.append(code);
          n++;
          if(n<5)
          {
            mostPopularCouponsStb.append(",");
          }
        }
      }
    }

    report.setMostPopularCoupons(mostPopularCouponsStb.toString());
    report.setTotalDiscountValue(totalDiscountValue);
    report.setTotalNoOfCouponsUsed(totalNoOfCouponsUsed);

    return report;

  }


  public FreeOrdersReport calculateFreeOrdersReport(List<Order> freeOrders, int noOfDays,
      Timestamp startDate) {
    FreeOrdersReport report = new FreeOrdersReport();

    long totalNoOfOrders = 0;
    long totalNoOfCustomers = 0;
    long totalNoOfOrderItems = 0;
    long totalNoOfLeadsGenerated = 0;

    Set<Long> customerSet = new HashSet<Long>();

    Map<Long, Integer> freeCustomerMap = new HashMap<Long, Integer>();

    List<Order> previousOrders = orderRepository.findByPaymentDateBefore(startDate);

    for (Order o : previousOrders) {
      freeCustomerMap.put(o.getCustomer().getUserId(), o.getOrderId());

    }


    for (Order o : freeOrders) {
      if (o != null) {
        customerSet.add(o.getCustomer().getUserId());
        totalNoOfOrderItems += o.getOrderItems().size();

        if (!freeCustomerMap.containsKey(o.getCustomer().getUserId())) {
          totalNoOfLeadsGenerated += 1;
        }

      }
    }

    totalNoOfCustomers = customerSet.size();
    totalNoOfOrders = freeOrders.size();

    report.setTotalNoOfCustomers(totalNoOfCustomers);
    report.setTotalNoOfLeadsGenerated(totalNoOfLeadsGenerated);
    report.setTotalNoOfOrderItems(totalNoOfOrderItems);
    report.setTotalNoOfOrders(totalNoOfOrders);

    return report;

  }

  public PaidOrdersReport calculatePaidOrderReport(List<Order> paidOrders, int noOfDays,
      Timestamp startDate) {
    PaidOrdersReport report = new PaidOrdersReport();

    /*
     * Calculating the paid order values
     */

    int totalNoOfOrders = 0;
    int totalNoOfCustomers = 0;
    int totalNoOfOrderItems = 0;
    double totalNeoCashRedeemed=0.0;
    Set<Long> paidCustomerSet = new HashSet<Long>();
    Map<Long, Integer> paidCustomerOrderMap = new HashMap<Long, Integer>();

    Map<Long, Integer> newCustomerOrderCountMap = new HashMap<Long, Integer>();

    List<Order> previousOrders = orderRepository.findByPaymentDateBefore(startDate);

    for (Order o : previousOrders) {
      paidCustomerOrderMap.put(o.getCustomer().getUserId(), o.getOrderId());

    }

    double grossSales = 0;
    double netSales = 0.0;
    double avgGrossDailySales = 0.0;
    double avgNetDailySales = 0.0;
    long noOfNewCustomers = 0;
    long noOfOrdersByNewCustomers = 0;


    if (paidOrders != null && !paidOrders.isEmpty()) {
      totalNoOfOrders = paidOrders.size();

      for (Order paidOrder : paidOrders) {
        if (paidOrder != null) {
          totalNoOfOrderItems += paidOrder.getOrderItems().size();

          paidCustomerSet.add(paidOrder.getCustomer().getUserId());
          // For getting new Customers Information
          if (!paidCustomerOrderMap.containsKey(paidOrder.getCustomer().getUserId())) {
            if (newCustomerOrderCountMap.containsKey(paidOrder.getCustomer().getUserId())) {
              int count = newCustomerOrderCountMap.get(paidOrder.getCustomer().getUserId());
              newCustomerOrderCountMap.put(paidOrder.getCustomer().getUserId(), count + 1);
            } else {
              newCustomerOrderCountMap.put(paidOrder.getCustomer().getUserId(), 1);

            }
          }

          for (OrderItem oi : paidOrder.getOrderItems()) {
            grossSales += oi.getRegularAmount();
            netSales += oi.getPayableAmount();
            totalNoOfOrderItems += 1;
            totalNeoCashRedeemed+=oi.getNeoCash();
          }


        }
      }
      totalNoOfCustomers = paidCustomerSet.size();
      
      avgGrossDailySales = Math.round(grossSales / noOfDays);
      avgNetDailySales = Math.round(netSales / noOfDays);
      for (Integer v : newCustomerOrderCountMap.values()) {
        noOfOrdersByNewCustomers += v;
      }

      noOfNewCustomers = newCustomerOrderCountMap.keySet().size();



    }

    report.setTotalNoOfOrders(totalNoOfOrders);
    report.setTotalNoOfCustomers(totalNoOfCustomers);
    report.setGrossSales(Math.round(grossSales));
    report.setNetSales(Math.round(netSales));
    report.setAvgGrossDailySales(avgGrossDailySales);
    report.setAvgNetDailySales(avgNetDailySales);
    report.setNoOfNewCustomers(noOfNewCustomers);
    report.setNoOfOrdersByNewCustomers(noOfOrdersByNewCustomers);
    report.setTotalNoOfOrderItems(totalNoOfOrderItems);
    report.setTotalNeoCashRedeemed(totalNeoCashRedeemed);

    return report;

  }

}
