package com.neostencil.utils;

import com.neostencil.common.enums.ProductType;
import com.neostencil.model.entities.Coupon;
import com.neostencil.model.entities.CourseBatch;
import com.neostencil.model.entities.Order;
import com.neostencil.model.entities.OrderItem;
import com.neostencil.model.entities.OrderNotes;
import com.neostencil.model.entities.Product;
import com.neostencil.model.entities.TeacherDetails;
import com.neostencil.model.entities.Unit;
import com.neostencil.model.entities.User;
import com.neostencil.model.repositories.CourseBatchRepository;
import com.neostencil.model.repositories.UnitRepository;
import com.neostencil.responses.CouponDTO;
import com.neostencil.responses.OrderDTO;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ECommerceAssembler {

	@Autowired
	CourseBatchRepository courseBatchRepository;

	@Autowired
	UnitRepository unitRepository;

	public CouponDTO toCouponDTO(Coupon coupon) {
		CouponDTO response = new CouponDTO();

		if (coupon != null) {
			response.setAmount(coupon.getAmount());
			response.setCouponCode(coupon.getCode());
			response.setDescription(coupon.getDescription());
			response.setDiscountPercentage(coupon.getDiscountPercentage());
			response.setDiscountType(coupon.getDiscountType().name());
			response.setExpiryDate(coupon.getExpiryDate());
			response.setMinimumSpend(coupon.getMinimumSpend());
			response.setStatus(coupon.getStatus().name());

			if (coupon.getAllowedUsers() != null && coupon.getAllowedUsers().size() > 0) {
				StringBuilder allowedUsers = new StringBuilder();
				for (User u : coupon.getAllowedUsers()) {
					if (u != null) {
						allowedUsers.append(u.getEmailId() + " | ");
					}
				}
				response.setAllowedUsers(allowedUsers.toString());
			}

			if (coupon.getExcludedProducts() != null && coupon.getExcludedProducts().size() > 0) {
				StringBuilder excProds = new StringBuilder();
				for (Product p : coupon.getExcludedProducts()) {
					if (p != null) {
						excProds.append(p.toString() + " | ");
					}
				}
				response.setExcludedProducts(excProds.toString());
			}
			if (coupon.getIncludedProducts() != null && coupon.getIncludedProducts().size() > 0) {
				StringBuilder incProds = new StringBuilder();
				for (Product p : coupon.getIncludedProducts()) {
					if (p != null) {
						incProds.append(p.toString() + " | ");
					}
				}
				response.setIncludedProducts(incProds.toString());
			}
		}
		return response;
	}

	public List<OrderDTO> toOrderDTO(Order order) {

		List<OrderDTO> responseList = new LinkedList<OrderDTO>();
		if (order != null) {

			if (order.getOrderItems() != null && order.getOrderItems().size() > 0) {
				for (OrderItem oi : order.getOrderItems()) {
					OrderDTO response = new OrderDTO();
					response.setOrderStatus(order.getOrderStatus().name());
					response.setPaymentMode(order.getPaymentMode().name());
					response.setGrossSales(oi.getRegularAmount());
					response.setNetSales(oi.getPayableAmount());
					response.setNeoCashRedeemed(oi.getNeoCash());
					if (order.getAddress() != null) {
						StringBuilder address = new StringBuilder();
						address.append(order.getAddress().getAddressText().replaceAll(",", "|"));
						address.append("|");
						address.append(order.getAddress().getCity());
						address.append("|");
						address.append(order.getAddress().getState());
						address.append("-");
						address.append(order.getAddress().getPincode());

						response.setAddress(address.toString().replaceAll("\n", " "));
						response.setPhoneNumber(order.getAddress().getMobileNumber());
					}
					response.setProductType(oi.getProduct().getType().name());
					if (oi.getProduct() != null && oi.getProduct().getType() != null
							&& oi.getProduct().getType().equals(ProductType.COURSE)) {

						CourseBatch cb = courseBatchRepository.findById(oi.getProduct().getCommodityId());

						if (cb != null && cb.getCourse() != null && cb.getCourse().getInstitute() != null
								&& cb.getCourse().getInstitute().getName() != null) {
							if (cb.getCourse().getInstitute().getName().length() > 0) {
								response.setInstitute(cb.getCourse().getInstitute().getName());
							} else {
								Set<TeacherDetails> instructors = cb.getCourse().getInstructors();
								StringBuilder instructorName = new StringBuilder();
								if (instructors != null && instructors.size() > 0) {
									for (TeacherDetails td : instructors) {
										instructorName.append(td.getTeacherName());
									}
								}
								if (instructorName != null) {
									response.setInstitute(instructorName.toString());
								}
							}
							StringBuilder productTitle = new StringBuilder();
							productTitle.append(cb.getCourse().getCourseName() + " | ");
							productTitle.append(cb.getStartDate());
							response.setCourse(productTitle.toString());
						}
					} else if (oi.getProduct() != null && oi.getProduct().getType() != null
							&& oi.getProduct().getType().equals(ProductType.UNIT)) {
						Unit unit = unitRepository.findByUnitId(oi.getProduct().getCommodityId());
						StringBuilder productTitle = new StringBuilder();
						if (unit != null && unit.getBatch() != null && unit.getBatch().getCourse() != null
								&& unit.getBatch().getCourse().getCourseName() != null) {
							productTitle.append(unit.getBatch().getCourse().getCourseName() + "|");
							productTitle.append(unit.getBatch().getStartDate() + "|");
							productTitle.append(unit.getTitle());
							response.setCourse(productTitle.toString());
						}
					}

					StringBuilder notesList = new StringBuilder();
					if (order.getOrderNotes() != null && order.getOrderNotes().size() > 0) {
						int i = 0;
						for (OrderNotes on : order.getOrderNotes()) {
							if (on != null) {
								StringBuilder note = new StringBuilder();
								note.append(++i + "." + on.getNoteContent() + "(by " + on.getAddedBy() + " at "
										+ on.getNoteDate() + ")");
								notesList.append("\n");
								notesList.append(note);
							}
						}
						response.setOrderNotes(notesList.toString());
					}

					response.setOrderId(order.getOrderId());
					response.setOrderStatus(order.getOrderStatus().getJsonValue());
					response.setPaymentDate(order.getPaymentDate().toLocalDateTime().toLocalDate());
					response.setPaymentMode(order.getPaymentMode().name());
					response.setRefundAmount(order.getRefundAmount());
					response.setQuantity(1);
					response.setStudentEmail(order.getCustomer().getEmailId());
					response.setStudentName(order.getCustomer().getFullName());

					responseList.add(response);
				}
			}

		}

		return responseList;
	}
}
