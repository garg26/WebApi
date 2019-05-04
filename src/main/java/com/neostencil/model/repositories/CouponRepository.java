package com.neostencil.model.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.neostencil.model.entities.Coupon;
import com.neostencil.projections.CouponProjection;

public interface CouponRepository extends JpaRepository<Coupon, Integer>, JpaSpecificationExecutor<Coupon> {

	Coupon findById(int id);

	Coupon findByCodeIgnoreCase(String code);

	List<CouponProjection> findAllProjectedBy();

	CouponProjection findAllProjectedByCode(String code);

	List<Coupon> findAllByCreatedAtBetween(Timestamp startDate, Timestamp endDate);
}
