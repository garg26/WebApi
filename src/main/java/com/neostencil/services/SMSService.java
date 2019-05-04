package com.neostencil.services;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SMSService {

  private AmazonSNS amazonSNS;

  @Autowired
  public void setAmazonSNS(AmazonSNS snsClient) {
    this.amazonSNS = snsClient;
  }

  public void sendSMSMessage(String message,
      String phoneNumber) {
    Map<String, MessageAttributeValue> smsAttributes =
        new HashMap<String, MessageAttributeValue>();
    smsAttributes.put("AWS.SNS.SMS.SenderID",
        new MessageAttributeValue().withStringValue("NEOSTN").withDataType("String"));
    smsAttributes.put("AWS.SNS.SMS.SMSType",
        new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));

    PublishResult result = amazonSNS.publish(new PublishRequest()
        .withMessage(message)
        .withPhoneNumber(phoneNumber)
        .withMessageAttributes(smsAttributes));
    //System.out.println("Hello SMS");
     System.out.println(result); // Prints the message ID.
  }

}
