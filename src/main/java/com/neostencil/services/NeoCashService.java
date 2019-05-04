package com.neostencil.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.neostencil.common.enums.NeoCashAction;
import com.neostencil.model.entities.Cart;
import com.neostencil.model.entities.CartOrderItem;
import com.neostencil.model.entities.User;
import com.neostencil.model.entities.UserNeoCashHistory;
import com.neostencil.model.repositories.CartRepository;
import com.neostencil.model.repositories.UserNeoCashHistoryRepository;
import com.neostencil.model.repositories.UserRepository;
import com.neostencil.requests.CreditNeoCashRequest;
import com.neostencil.requests.DebitNeoCashRequest;
import com.neostencil.requests.ValidateNeoCashRedemptionRequest;
import com.neostencil.responses.NeoCashDetailResponse;
import com.neostencil.responses.NeoCashHistoryResponse;
import com.neostencil.responses.UserNeoCashHistoryDTO;
import com.neostencil.responses.ValidateNeoCashRedemptionResponse;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.utils.CommonAssembler;

/**
 * 
 * @author shilpa
 *
 */
@Service
public class NeoCashService {

  @Autowired
  JwtTokenUtil jwtUtil;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserNeoCashHistoryRepository historyRepository;

  @Autowired
  CartRepository cartRepository;

  final double MAX_USEABLE_PERCENT = 0.1;
  
  @Autowired
  EmailService emailService;


  /**
   * for crediting neo cash to the loggedin user. This will be done automatically
   * 
   * @param amount
   */
  public void creditNeoCashToLoggedInUser(CreditNeoCashRequest request) {
    long userId = jwtUtil.getLoggedInUserID();
    User user = userRepository.findByUserId(userId);
    double currentBalance = user.getNeoCashBalance();
    /*if(currentBalance==null)
    {
      currentBalance=0;
    }*/
    user.setNeoCashBalance(currentBalance + request.getAmount());
    userRepository.saveAndFlush(user);

    UserNeoCashHistory history = new UserNeoCashHistory();
    history.setAction(NeoCashAction.CREDIT);
    history.setReason(request.getReason());
    history.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusDays(2)));
    history.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    history.setUpdatedAt(history.getCreatedAt());
    history.setNeoCashValue(request.getAmount());
    history.setUserId(userId);
    history.setCreditedBy("AUTO");
    
    historyRepository.saveAndFlush(history);
    
    StringBuilder msg=new StringBuilder();
    msg.append(request.getAmount());
    msg.append(" NeoCash credited to ");
    msg.append(user.getEmailId());
    msg.append(" for ");
    msg.append(request.getReason());
    
    
    emailService.sendSimpleMessage("dev@neostencil.com", "NeoCash Credited", msg.toString());
    
    //TODO: To send NeoCash credited email to logged in user.
    

  }

  /**
   * For crediting the NeoCash manually from the admin dashboard
   * 
   * @param request
   */
  public void manuallyCreditNeoCash(CreditNeoCashRequest request) {
    String loggedInUserEmail = jwtUtil.getLoggedInUserEmail();

    User user = userRepository.findByUserId(request.getCreditTo());
    if (user != null) {
      double currentBalance = user.getNeoCashBalance();
      user.setNeoCashBalance(currentBalance + request.getAmount());
      userRepository.saveAndFlush(user);
    }

    UserNeoCashHistory history = new UserNeoCashHistory();
    history.setAction(NeoCashAction.CREDIT);
    history.setReason(request.getReason());
    history.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusDays(2)));
    history.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    history.setUpdatedAt(history.getCreatedAt());
    history.setNeoCashValue(request.getAmount());
    history.setUserId(user.getUserId());
    history.setCreditedBy(loggedInUserEmail);
    
    
    historyRepository.saveAndFlush(history);
    StringBuilder msg=new StringBuilder();
    msg.append(request.getAmount());
    msg.append(" NeoCash credited to ");
    msg.append(user.getEmailId());
    msg.append(" for ");
    msg.append(request.getReason());
    msg.append("by ");
    msg.append(loggedInUserEmail);
    
    emailService.sendSimpleMessage("dev@neostencil.com", "NeoCash Credited", msg.toString());
    //TODO: To send NeoCash credited email to the receiver.


  }

  /**
   * For debiting the neocash
   * 
   * @param request
   */
  public void debitNeoCash(DebitNeoCashRequest request) {

    long userId = jwtUtil.getLoggedInUserID();

    User user = userRepository.findByUserId(userId);

    double currentBalance = user.getNeoCashBalance();
    user.setNeoCashBalance(currentBalance - request.getAmount());
    userRepository.saveAndFlush(user);

    UserNeoCashHistory history = new UserNeoCashHistory();
    history.setAction(NeoCashAction.DEBIT);
    history.setReason(request.getReason());
    history.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusDays(2000)));
    history.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    history.setUpdatedAt(history.getCreatedAt());
    history.setNeoCashValue(request.getAmount());
    history.setUserId(user.getUserId());
    history.setAdditionalInfo("Order Id : " + request.getOrderId());

    historyRepository.saveAndFlush(history);

  }

  /**
   * For validating the application of the neoCash
   * 
   * @param request
   * @return
   */
  public ValidateNeoCashRedemptionResponse validateNeoCashRedemption(
      ValidateNeoCashRedemptionRequest request) {
    ValidateNeoCashRedemptionResponse response = new ValidateNeoCashRedemptionResponse();

    long userId = jwtUtil.getLoggedInUserID();

    User user = userRepository.findByUserId(userId);
    // Checking for expired NeoCash
    UserNeoCashHistory latestHistory = historyRepository.findTopByUserIdOrderByCreatedAt(userId);
    if (latestHistory != null) {
      if (latestHistory.getCreatedAt().after(Timestamp.valueOf(LocalDateTime.now()))) {
        user.setNeoCashBalance(0);
        userRepository.saveAndFlush(user);
        response.setValid(false);
        response.setMessage("NeoCash Expired");
        return response;
      }
    }


    Cart cart = cartRepository.findByCustomer(user);

    double payableAmount = 0.0;
    double totalAmount=0.0;
    if (cart != null && cart.getItems() != null && cart.getItems().size() > 0) {
      for (CartOrderItem ci : cart.getItems()) {
        if (ci != null) {
          payableAmount += ci.getPayableAmount();
          totalAmount+=ci.getTotalAmount();
        }
      }
    }
    

    /**
     * Assuming max 10% is useable
     */

    double maxAllowedDiscount=MAX_USEABLE_PERCENT*totalAmount;
    
    double appliedDiscount=totalAmount-payableAmount;
    
    double maxUseableCash=maxAllowedDiscount-appliedDiscount;

    if (request.getNeoCashAmount() <= maxUseableCash
        && request.getNeoCashAmount() <= user.getNeoCashBalance()) {
      response.setValid(true);
      response.setNeoCashRedeemed(request.getNeoCashAmount());
      response.setPayableAmount(payableAmount-request.getNeoCashAmount());
    } else {
      response.setValid(false);
    }
    
  
    
    
    
    return response;
  }

  /**
   * Fetching all the neocash details about the customer
   * @return
   */
  public NeoCashDetailResponse fetchNeoCashDetailsForAUser() {
    NeoCashDetailResponse response = new NeoCashDetailResponse();

    long userId = jwtUtil.getLoggedInUserID();

    User user = userRepository.findByUserId(userId);

    Cart cart = cartRepository.findByCustomer(user);

    double payableAmount = 0.0;
    double totalAmount = 0.0;
    if (cart != null && cart.getItems() != null && cart.getItems().size() > 0) {
      for (CartOrderItem ci : cart.getItems()) {
        if (ci != null) {
          payableAmount += ci.getPayableAmount();
          totalAmount += ci.getTotalAmount();
        }
      }
    }
    
    //Checking for expired NeoCash
    UserNeoCashHistory latestHistory=historyRepository.findTopByUserIdOrderByCreatedAt(userId);
    
    
    if (latestHistory != null) {
      if (latestHistory.getCreatedAt().after(Timestamp.valueOf(LocalDateTime.now()))) {
        user.setNeoCashBalance(0);
        userRepository.saveAndFlush(user);
        response.setMessage("All Cash expired!!!");
        response.setUseableNeoCash(0);
        response.setTotalNeoCash(0);
        return response;
      }
    }
    
    response.setTotalNeoCash(user.getNeoCashBalance());

    double maxAllowedDiscount = MAX_USEABLE_PERCENT * totalAmount;

    double appliedDiscount = totalAmount - payableAmount;

    double useableAmount = maxAllowedDiscount - appliedDiscount;
    if (useableAmount > user.getNeoCashBalance()) {
      useableAmount = user.getNeoCashBalance();
    }
    response.setUseableNeoCash(useableAmount);

    return response;
  }
  
  public NeoCashHistoryResponse fetchNeoCashHistoryForLoggedInUser()
  {
    NeoCashHistoryResponse response=new NeoCashHistoryResponse();
    
    List<UserNeoCashHistoryDTO> historyDTOList=new LinkedList<>();
    
    long userId=jwtUtil.getLoggedInUserID();
    
    User user = userRepository.findByUserId(userId);
    response.setCurrentNeoCashBalance(user.getNeoCashBalance());
    
    List<UserNeoCashHistory> historyList=historyRepository.findAllByUserIdOrderByCreatedAt(userId);
    
    if(historyList!=null && !historyList.isEmpty())
    {
      for(UserNeoCashHistory h:historyList)
      {
        if(h!=null)
        {
          historyDTOList.add(CommonAssembler.toUserNeoCashHistoryDTO(h));
        }
      }
    }
    response.setHistoryList(historyDTOList);
    
    return response;
  }
  
}
