package com.neostencil.model.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ns_locks")
public class Lock {

	@EmbeddedId
	private LockId lockId;

	@Column(name = "acquired_at")
	private Timestamp acquiredAt;

	@Column(name = "acquired_by")
	private String acquiredBy;

	public LockId getLockId() {
		return lockId;
	}

	public void setLockId(LockId lockId) {
		this.lockId = lockId;
	}

	public Timestamp getAcquiredAt() {
		return acquiredAt;
	}

	public void setAcquiredAt(Timestamp acquiredAt) {
		this.acquiredAt = acquiredAt;
	}

	public String getAcquiredBy() {
		return acquiredBy;
	}

	public void setAcquiredBy(String acquiredBy) {
		this.acquiredBy = acquiredBy;
	}

	public Lock(LockId lockId, Timestamp acquiredAt, String acquiredBy) {
		super();
		this.lockId = lockId;
		this.acquiredAt = acquiredAt;
		this.acquiredBy = acquiredBy;
	}

	public Lock() {

	}

}
