package com.neostencil.common.enums;

/**
 * This enum will be specifying whether the product was purchased (or given access to) from the
 * admin dashboard or the public facing website. One of these values should be set in frontend based
 * upon the source of order.
 * 
 * @author shilpa
 *
 */
public enum OrderType {

  MANUAL, REGULAR;
}
