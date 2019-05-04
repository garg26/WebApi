package com.neostencil.projections;

import org.springframework.data.rest.core.config.Projection;
import com.neostencil.model.entities.JWMacro;

@Projection(name = "jwMacroProjection", types = JWMacro.class)
public interface JWMacroProjection {

  String getName();

  String getDescription();

  boolean isHlshtml();

  boolean isAutoStart();

  boolean isPlaybackRateControls();

  String getAbouttext();

  String getAboutlink();

  String getLogoFile();

  String getLogoPosition();

  boolean isDrmEnabled();

  boolean isWowzaHashEnabled();

  boolean isWrenchEnabled();
  
  WowzaMacroProjection getWowzaMacro();
}
