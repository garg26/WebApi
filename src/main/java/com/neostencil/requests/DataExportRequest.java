package com.neostencil.requests;

import java.sql.Timestamp;
import java.util.List;

public class DataExportRequest {

	private String entityName;
	private List<DataFieldRequest> dataFields;
	private Timestamp startDate;
	private Timestamp endDate;

	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName
	 *            the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * @return the dataFields
	 */
	public List<DataFieldRequest> getDataFields() {
		return dataFields;
	}

	/**
	 * @param dataFields
	 *            the dataFields to set
	 */
	public void setDataFields(List<DataFieldRequest> dataFields) {
		this.dataFields = dataFields;
	}

	/**
	 * @return the startDate
	 */
	public Timestamp getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Timestamp getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

}
