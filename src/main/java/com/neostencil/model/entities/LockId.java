package com.neostencil.model.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import com.neostencil.common.enums.EntityType;

@Embeddable
public class LockId implements Serializable{

	@NotNull
	private String entityId;

	@NotNull
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

	public LockId() {

	}

	public LockId(@NotNull String entityId, @NotNull EntityType entityType) {
		super();
		this.entityId = entityId;
		this.entityType = entityType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
		result = prime * result + ((entityType == null) ? 0 : entityType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LockId other = (LockId) obj;
		if (entityId == null) {
			if (other.entityId != null)
				return false;
		} else if (!entityId.equals(other.entityId))
			return false;
		if (entityType != other.entityType)
			return false;
		return true;
	}

}
