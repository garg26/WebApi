package com.neostencil.responses;

import java.sql.Timestamp;

/**
 * 
 * @author shilpa
 *
 */
public class LockResponse {

	boolean success;

	String acquiredBy;

	Timestamp acquiredAt;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getAcquiredBy() {
		return acquiredBy;
	}

	public void setAcquiredBy(String acquiredBy) {
		this.acquiredBy = acquiredBy;
	}

	public Timestamp getAcquiredAt() {
		return acquiredAt;
	}

	public void setAcquiredAt(Timestamp acquiredAt) {
		this.acquiredAt = acquiredAt;
	}

}
