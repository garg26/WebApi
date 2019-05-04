package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ns_wowzamacros")
public class WowzaMacro extends DomainObject {

  @Column(name = "token")
  String token;
  @Id
  @Column(name = "wowza_macro_name")
  private String name;
  @Column(name = "wowza_macro_description")
  private String description;
  @Column(name = "server_ip")
  private String serverIp;
  @Column(name = "wowza_start")
  private int wowzaStart;

  @Column(name = "validity")
  private int validity;

  @Column(name = "wowza_end")
  private int wowzaEnd;

  @Column(name = "secret")
  private String secret;

  @JsonManagedReference(value = "jw-wowza")
  @OneToMany(mappedBy = "wowzaMacro")
  private Set<JWMacro> jwMacros;

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
   * @return the serverIp
   */
  public String getServerIp() {
    return serverIp;
  }

  /**
   * @param serverIp the serverIp to set
   */
  public void setServerIp(String serverIp) {
    this.serverIp = serverIp;
  }

  /**
   * @return the token
   */
  public String getToken() {
    return token;
  }

  /**
   * @param token the token to set
   */
  public void setToken(String token) {
    this.token = token;
  }

  /**
   * @return the wowzaStart
   */
  public int getWowzaStart() {
    return wowzaStart;
  }

  /**
   * @param wowzaStart the wowzaStart to set
   */
  public void setWowzaStart(int wowzaStart) {
    this.wowzaStart = wowzaStart;
  }

  /**
   * @return the validity
   */
  public int getValidity() {
    return validity;
  }

  /**
   * @param validity the validity to set
   */
  public void setValidity(int validity) {
    this.validity = validity;
  }

  /**
   * @return the wowzaEnd
   */
  public int getWowzaEnd() {
    return wowzaEnd;
  }

  /**
   * @param wowzaEnd the wowzaEnd to set
   */
  public void setWowzaEnd(int wowzaEnd) {
    this.wowzaEnd = wowzaEnd;
  }

  /**
   * @return the secret
   */
  public String getSecret() {
    return secret;
  }

  /**
   * @param secret the secret to set
   */
  public void setSecret(String secret) {
    this.secret = secret;
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
    WowzaMacro other = (WowzaMacro) obj;
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
   * @return the jwMacros
   */
  public Set<JWMacro> getJwMacros() {
    return jwMacros;
  }

  /**
   * @param jwMacros the jwMacros to set
   */
  public void setJwMacros(Set<JWMacro> jwMacros) {
    this.jwMacros = jwMacros;
  }

  
}
