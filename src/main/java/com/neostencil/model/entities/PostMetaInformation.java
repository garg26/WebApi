package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "ns_post_meta_information")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PostMetaInformation {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_meta_seq")
  @SequenceGenerator(name = "post_meta_seq", sequenceName = "post_meta_seq", allocationSize = 1)
  private long id;

  @JsonBackReference(value = "meta-reference")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  Post post;

  @Embedded
  private MetaInformation metaInformation;

  /**
   * @return the id
   */
  public long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * @return the post
   */
  public Post getPost() {
    return post;
  }

  /**
   * @param post the post to set
   */
  public void setPost(Post post) {
    this.post = post;
  }

  /**
   * @return the metaInformation
   */
  public MetaInformation getMetaInformation() {
    return metaInformation;
  }

  /**
   * @param metaInformation the metaInformation to set
   */
  public void setMetaInformation(MetaInformation metaInformation) {
    this.metaInformation = metaInformation;
  }

}
