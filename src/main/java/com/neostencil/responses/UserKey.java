package com.neostencil.responses;

/**
 * To be used as key for a map in response object
 * @author shilpa
 *
 */
public final class UserKey {
  
   long id;
  
   String name;
  
   String email;

  public UserKey(long id, String name, String email) {
    super();
    this.id = id;
    this.name = name;
    this.email = email;
  }

  /**
   * @return the id
   */
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  @Override
  public String toString() {
    return "{" +
        "\"id\":" + id +
        ",\"name\":" + '\"'+name + '\"' +
        ",\"email\":" + '\"'+ email + '\"' +
        '}';
  }
}
