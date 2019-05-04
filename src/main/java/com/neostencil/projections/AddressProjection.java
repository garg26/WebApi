package com.neostencil.projections;

import org.springframework.data.rest.core.config.Projection;
import com.amazonaws.services.ec2.model.Address;

@Projection(name = "addressProjection", types = Address.class)
public interface AddressProjection {

  int getAddressId();

  String getFirstName();

  String getLastname();

  String getMobileNumber();

  String getAlternateMobileNumber();

  String getAddressText();

  String getCity();

  String getState();

  String getPincode();

  boolean isDefaultAddress();

  UserProjection getUser();
}
