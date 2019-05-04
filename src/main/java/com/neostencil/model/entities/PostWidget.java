package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PostWidget {

  @Column(name = "widget_name")
  private String widgetName;

  @Column(name = "widget_url")
  private String widgetUrl;

  public String getWidgetName() {
    return widgetName;
  }

  public void setWidgetName(String widgetName) {
    this.widgetName = widgetName;
  }

  public String getWidgetUrl() {
    return widgetUrl;
  }

  public void setWidgetUrl(String widgetUrl) {
    this.widgetUrl = widgetUrl;
  }
}
