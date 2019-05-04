package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.CollectionTable;
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
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Indexed
@Table(name = "ns_post_tag")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class PostTag implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_tag_seq")
  @SequenceGenerator(name = "post_tag_seq", sequenceName = "post_tag_seq", allocationSize = 1)
  private long tagId;

  @JsonIgnoreProperties({"tags"})
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  Post post;

  @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
  @Column(name = "tag_name")
  private String name;


  public long getTagId() {
    return tagId;
  }

  public void setTagId(long tagId) {
    this.tagId = tagId;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
