package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

public class Achievements {

  private String iconUrl;

  private String iconName;

  private String description;

  public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public String getIconName() {
    return iconName;
  }

  public void setIconName(String iconName) {
    this.iconName = iconName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
