package com.neostencil.dtos;

/**
 * DTO for Institute details
 * 
 * @author shilpa
 *
 */
public class InstituteDTO {

  private int id;
  private String name;
  private String url;
  private String instituteSlug;
  private String bannerImage;

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  private String imageUrl;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the instituteSlug
     */
    public String getInstituteSlug() {
        return instituteSlug;
    }

    /**
     * @param instituteSlug
     *            the instituteSlug to set
     */
    public void setInstituteSlug(String instituteSlug) {
        this.instituteSlug = instituteSlug;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }
}
