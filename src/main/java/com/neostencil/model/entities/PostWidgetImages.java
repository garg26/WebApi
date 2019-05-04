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
@Table(name = "ns_post_widget_images")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PostWidgetImages {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_widget_seq")
	@SequenceGenerator(name = "post_widget_seq", sequenceName = "post_widget_seq", allocationSize = 1)
	private long id;

	@JsonBackReference(value = "widget-reference")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	Post post;

	@Embedded
	private PostWidget postWidget;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param post
	 *            the post to set
	 */
	public void setPost(Post post) {
		this.post = post;
	}

	/**
	 * @return the postWidget
	 */
	public PostWidget getPostWidget() {
		return postWidget;
	}

	/**
	 * @param postWidget
	 *            the postWidget to set
	 */
	public void setPostWidget(PostWidget postWidget) {
		this.postWidget = postWidget;
	}

}
