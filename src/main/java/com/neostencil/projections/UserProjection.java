package com.neostencil.projections;

import org.springframework.data.rest.core.config.Projection;
import com.google.cloud.storage.Acl.User;

@Projection(name = "userDTO", types = User.class)
public interface UserProjection {

  String getEmailId();

  String getFullName();

  String getAvatar();

  String getUserId();

  String getMobileNumber();
  
  double getNeoCashBalance();

}
