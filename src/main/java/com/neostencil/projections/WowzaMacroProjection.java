package com.neostencil.projections;

import org.springframework.data.rest.core.config.Projection;
import com.neostencil.model.entities.WowzaMacro;

@Projection(name = "wowzaMacroProjection", types = WowzaMacro.class)
public interface WowzaMacroProjection {

  String getToken();

  String getName();

  String getDescription();

  String getServerIp();

  int getWowzaStart();

  int getValidity();

  int getWowzaEnd();

  String getSecret();
}
