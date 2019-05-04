package com.neostencil.responses;

import com.neostencil.model.entities.Unit;
import java.util.Map;

public class PlayLectureOnMobile {

  String manifestFile;
  String playlistFile;
  DRMResponseForMobile widevine;
  DRMResponseForMobile playreadyDRM;
  String icon;
  Unit unit;



  public String getManifestFile() {
    return manifestFile;
  }

  public void setManifestFile(String manifestFile) {
    this.manifestFile = manifestFile;
  }

  public String getPlaylistFile() {
    return playlistFile;
  }

  public void setPlaylistFile(String playlistFile) {
    this.playlistFile = playlistFile;
  }

  public DRMResponseForMobile getWidevine() {
    return widevine;
  }

  public void setWidevine(DRMResponseForMobile widevine) {
    this.widevine = widevine;
  }

  public DRMResponseForMobile getPlayreadyDRM() {
    return playreadyDRM;
  }

  public void setPlayreadyDRM(DRMResponseForMobile playreadyDRM) {
    this.playreadyDRM = playreadyDRM;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }
}
