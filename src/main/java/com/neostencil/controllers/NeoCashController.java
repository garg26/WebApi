package com.neostencil.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.neostencil.requests.CreditNeoCashRequest;
import com.neostencil.requests.ValidateNeoCashRedemptionRequest;
import com.neostencil.responses.NeoCashDetailResponse;
import com.neostencil.responses.ValidateNeoCashRedemptionResponse;
import com.neostencil.services.NeoCashService;

/**
 * 
 * @author shilpa
 *
 */
@RestController
@RequestMapping({"/api/v1"})
public class NeoCashController {
  
  @Autowired
  NeoCashService service;
 
  @RequestMapping(value="/user/neocash-details",method=RequestMethod.GET)
  @ResponseBody
  NeoCashDetailResponse fetchNeoCashDetailsForALoggedInUser()
  {
    NeoCashDetailResponse response=service.fetchNeoCashDetailsForAUser();
    return response;
  }
  
  
  @RequestMapping(value="/user/validate-neocash-redemption",method=RequestMethod.PUT)
  @ResponseBody
  ValidateNeoCashRedemptionResponse validateNeoCashRedemption(@Valid @RequestBody ValidateNeoCashRedemptionRequest request)
  {
    ValidateNeoCashRedemptionResponse response=service.validateNeoCashRedemption(request);
    return response;
  }
  
  @RequestMapping(value="/user/credit-neo-cash",method=RequestMethod.PUT)
  void creditNeoCashToLoggedInUser(@Valid @RequestBody CreditNeoCashRequest request)
  {
    service.creditNeoCashToLoggedInUser(request);
  }
  
  @RequestMapping(value="/admin/credit-neo-cash",method=RequestMethod.PUT)
  void manuallyCreditNeoCash(@Valid @RequestBody CreditNeoCashRequest request)
  {
    service.manuallyCreditNeoCash(request);
  }
  
}
