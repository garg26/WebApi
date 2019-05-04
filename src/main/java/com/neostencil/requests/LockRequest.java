package com.neostencil.requests;

import com.neostencil.common.enums.EntityType;

/**
 * 
 * @author shilpa
 *
 */
public class LockRequest {

	private String entityId;

	private EntityType entityType;

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

}
