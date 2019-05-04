package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.neostencil.common.enums.StreamType;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ns_jwmacros")
public class JWMacro extends DomainObject {

  @Id
  @Column(name = "jw_macro_name")
  private String name;

  @Column(name = "jw_macro_description")
  private String description;

  @Column(name = "hlshtml")
  private boolean hlshtml;

  @Column(name = "auto_start")
  private boolean autoStart;

  @Column(name = "playback_rate_controls")
  private boolean playbackRateControls;

  @Column(name = "playback_rates")
  private float playbackRates;

  @Column(name = "about_text")
  private String abouttext;

  @Column(name = "about_link")
  private String aboutlink;

  @Column(name = "logo_file")
  private String logoFile;

  @Column(name = "logo_position")
  private String logoPosition;


  @Column(name = "is_drm_enabled")
  private boolean drmEnabled;

  @Column(name = "is_wowza_hash_enabled")
  private boolean wowzaHashEnabled;

  @Column(name = "is_wrench_enabled")
  private boolean wrenchEnabled;

  @Enumerated(EnumType.STRING)
  @Column(name = "stream_type")
  private StreamType streamType;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "jwMacro")
  @JsonManagedReference(value = "lecture-jw")
  private Set<Lecture> units;
  
  @JsonBackReference(value = "jw-wowza")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wowza_macro_id")
  private WowzaMacro wowzaMacro;

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the hlshtml
   */
  public boolean isHlshtml() {
    return hlshtml;
  }

  /**
   * @param hlshtml the hlshtml to set
   */
  public void setHlshtml(boolean hlshtml) {
    this.hlshtml = hlshtml;
  }

  /**
   * @return the autoStart
   */
  public boolean isAutoStart() {
    return autoStart;
  }

  /**
   * @param autoStart the autoStart to set
   */
  public void setAutoStart(boolean autoStart) {
    this.autoStart = autoStart;
  }

  /**
   * @return the playbackRateControls
   */
  public boolean isPlaybackRateControls() {
    return playbackRateControls;
  }

  /**
   * @param playbackRateControls the playbackRateControls to set
   */
  public void setPlaybackRateControls(boolean playbackRateControls) {
    this.playbackRateControls = playbackRateControls;
  }

  /**
   * @return the playbackRates
   */
  public float getPlaybackRates() {
    return playbackRates;
  }

  /**
   * @param playbackRates the playbackRates to set
   */
  public void setPlaybackRates(float playbackRates) {
    this.playbackRates = playbackRates;
  }

  /**
   * @return the abouttext
   */
  public String getAbouttext() {
    return abouttext;
  }

  /**
   * @param abouttext the abouttext to set
   */
  public void setAbouttext(String abouttext) {
    this.abouttext = abouttext;
  }

  /**
   * @return the aboutlink
   */
  public String getAboutlink() {
    return aboutlink;
  }

  /**
   * @param aboutlink the aboutlink to set
   */
  public void setAboutlink(String aboutlink) {
    this.aboutlink = aboutlink;
  }

  /**
   * @return the logoFile
   */
  public String getLogoFile() {
    return logoFile;
  }

  /**
   * @param logoFile the logoFile to set
   */
  public void setLogoFile(String logoFile) {
    this.logoFile = logoFile;
  }

  /**
   * @return the logoPosition
   */
  public String getLogoPosition() {
    return logoPosition;
  }

  /**
   * @param logoPosition the logoPosition to set
   */
  public void setLogoPosition(String logoPosition) {
    this.logoPosition = logoPosition;
  }

  /**
   * @return the units
   */
  public Set<Lecture> getUnits() {
    return units;
  }

  /**
   * @param units the units to set
   */
  public void setUnits(Set<Lecture> units) {
    this.units = units;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    JWMacro other = (JWMacro) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

  /**
   * @return the drmEnabled
   */
  public boolean isDrmEnabled() {
    return drmEnabled;
  }

  /**
   * @param drmEnabled the drmEnabled to set
   */
  public void setDrmEnabled(boolean drmEnabled) {
    this.drmEnabled = drmEnabled;
  }

  /**
   * @return the wowzaHashEnabled
   */
  public boolean isWowzaHashEnabled() {
    return wowzaHashEnabled;
  }

  /**
   * @param wowzaHashEnabled the wowzaHashEnabled to set
   */
  public void setWowzaHashEnabled(boolean wowzaHashEnabled) {
    this.wowzaHashEnabled = wowzaHashEnabled;
  }

  /**
   * @return the wrenchEnabled
   */
  public boolean isWrenchEnabled() {
    return wrenchEnabled;
  }

  /**
   * @param wrenchEnabled the wrenchEnabled to set
   */
  public void setWrenchEnabled(boolean wrenchEnabled) {
    this.wrenchEnabled = wrenchEnabled;
  }

  /**
   * @return the streamType
   */
  public StreamType getStreamType() {
    return streamType;
  }

  /**
   * @param streamType the streamType to set
   */
  public void setStreamType(StreamType streamType) {
    this.streamType = streamType;
  }

  /**
   * @return the wowzaMacro
   */
  public WowzaMacro getWowzaMacro() {
    return wowzaMacro;
  }

  /**
   * @param wowzaMacro the wowzaMacro to set
   */
  public void setWowzaMacro(WowzaMacro wowzaMacro) {
    this.wowzaMacro = wowzaMacro;
  }


}
