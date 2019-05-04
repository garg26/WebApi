package com.neostencil.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.neostencil.common.enums.PublishStatus;

@Entity
@Table(name = "ns_custom_posts")
public class CustomPost extends DomainObject {

	@Id
	@Column(name = "custom_post_id", nullable = false)
	int customPostId;

	@Column(name = "custom_post_slug", nullable = false, length = 20000)
	String slug;

	@OneToOne
	Post previousParentPost;

	@OneToOne
	Post nextParentPost;

	@Column(name = "post_name", length = 10000)
	String postName;

	/**
	 * For displaying date time on screen
	 */
	@Column(name = "post_date")
	private int postDate;

	@Column(name = "post_month")
	private String postMonth;

	@Column(name = "post_year")
	private int postYear;

	@Enumerated(EnumType.STRING)
	@Column(name = "publish_status")
	private PublishStatus status;

	/**
	 * @return the slug
	 */
	public String getSlug() {
		return slug;
	}

	/**
	 * @param slug
	 *            the slug to set
	 */
	public void setSlug(String slug) {
		this.slug = slug;
	}

	/**
	 * @return the previousParentPost
	 */
	public Post getPreviousParentPost() {
		return previousParentPost;
	}

	/**
	 * @param previousParentPost
	 *            the previousParentPost to set
	 */
	public void setPreviousParentPost(Post previousParentPost) {
		this.previousParentPost = previousParentPost;
	}

	/**
	 * @return the nextParentPost
	 */
	public Post getNextParentPost() {
		return nextParentPost;
	}

	/**
	 * @param nextParentPost
	 *            the nextParentPost to set
	 */
	public void setNextParentPost(Post nextParentPost) {
		this.nextParentPost = nextParentPost;
	}

	/**
	 * @return the postName
	 */
	public String getPostName() {
		return postName;
	}

	/**
	 * @param postName
	 *            the postName to set
	 */
	public void setPostName(String postName) {
		this.postName = postName;
	}

	/**
	 * @return the postDate
	 */
	public int getPostDate() {
		return postDate;
	}

	/**
	 * @param postDate
	 *            the postDate to set
	 */
	public void setPostDate(int postDate) {
		this.postDate = postDate;
	}

	/**
	 * @return the postMonth
	 */
	public String getPostMonth() {
		return postMonth;
	}

	/**
	 * @param postMonth
	 *            the postMonth to set
	 */
	public void setPostMonth(String postMonth) {
		this.postMonth = postMonth;
	}

	/**
	 * @return the postYear
	 */
	public int getPostYear() {
		return postYear;
	}

	/**
	 * @param postYear
	 *            the postYear to set
	 */
	public void setPostYear(int postYear) {
		this.postYear = postYear;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((slug == null) ? 0 : slug.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomPost other = (CustomPost) obj;
		if (slug == null) {
			if (other.slug != null)
				return false;
		} else if (!slug.equals(other.slug))
			return false;
		return true;
	}

	/**
	 * @return the status
	 */
	public PublishStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(PublishStatus status) {
		this.status = status;
	}

	/**
	 * @return the customPostId
	 */
	public int getCustomPostId() {
		return customPostId;
	}

	/**
	 * @param customPostId
	 *            the customPostId to set
	 */
	public void setCustomPostId(int customPostId) {
		this.customPostId = customPostId;
	}

}

