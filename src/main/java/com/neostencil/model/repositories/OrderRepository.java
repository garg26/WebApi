package com.neostencil.model.repositories;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.neostencil.common.enums.ExamSegmentTypes;
import com.neostencil.common.enums.OrderType;
import com.neostencil.common.enums.PaymentModeType;
import com.neostencil.model.entities.Order;
import com.neostencil.model.entities.User;
import com.neostencil.projections.OrderProjection;

public interface OrderRepository
    extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {

  Order findByOrderId(int id);

  List<Order> findByCustomer(User u);

  List<Order> findByPaymentDateBetween(Timestamp start, Timestamp end);

  List<OrderProjection> findAllProjectedByOrderByPaymentDateDesc();

  List<OrderProjection> findAllProjectedBy();
  
  OrderProjection findAllProjectedByOrderId(int orderId);

  List<OrderProjection> findAllProjectedByPaymentDateBetween(Timestamp start, Timestamp end);
  
  /**
   * Query for getting all the paid order details 
   * @param examSegments
   * @param startDate
   * @param endDate
   * @param orderTypes
   * @return
   */
  @Query("select o from Order o where ((o.orderId in (select oi.order.orderId from OrderItem oi where oi.product.id in (select p.id from Product p where p.type like 'UNIT' and p.commodityId in (select u.unitId from Unit u where u.batch.id in (select cb.id from CourseBatch cb where cb.course.id in (select c.id from Course c where c.courseExamSegment in(:examSegmentList))))))) or (o.orderId in (select oi.order.orderId from OrderItem oi where oi.product.id in (select p.id from Product p where p.type like 'COURSE' and p.commodityId in (select cb.id from CourseBatch cb where cb.course.id in (select c.id from Course c where c.courseExamSegment in(:examSegmentList))))))) and o.paymentDate between :startDate and :endDate and o.orderType in (:orderTypes) and o.orderStatus in ('APPROVED','PROCESSED') and o.refundAmount=0 and o.paymentMode not like 'free'")
  List<Order>  findAllPaidOrderDetails(@Param("examSegmentList") List<ExamSegmentTypes> examSegments,@Param("startDate")Timestamp startDate,@Param("endDate")Timestamp endDate,@Param("orderTypes") List<OrderType> orderTypes);
  
  /**
   * Query for getting all the free order details 
   * @param examSegments
   * @param startDate
   * @param endDate
   * @param orderTypes
   * @return
   */
  @Query("select o from Order o where ((o.orderId in (select oi.order.orderId from OrderItem oi where oi.product.id in (select p.id from Product p where p.type like 'UNIT' and p.commodityId in (select u.unitId from Unit u where u.batch.id in (select cb.id from CourseBatch cb where cb.course.id in (select c.id from Course c where c.courseExamSegment in(:examSegmentList))))))) or (o.orderId in (select oi.order.orderId from OrderItem oi where oi.product.id in (select p.id from Product p where p.type like 'COURSE' and p.commodityId in (select cb.id from CourseBatch cb where cb.course.id in (select c.id from Course c where c.courseExamSegment in(:examSegmentList))))))) and o.paymentDate between :startDate and :endDate and o.orderType in (:orderTypes) and o.orderStatus in ('APPROVED','PROCESSED') and o.paymentMode like 'free'")
  List<Order>  findAllFreeOrderDetails(@Param("examSegmentList") List<ExamSegmentTypes> examSegments,@Param("startDate")Timestamp startDate,@Param("endDate")Timestamp endDate,@Param("orderTypes") List<OrderType> orderTypes);
  
  /**
   * 
   * @param startDate
   * @param paymentMode
   * @return
   */
  List<Order> findByPaymentDateBefore(Timestamp startDate);
  
  @Query("select o from Order o where ((o.orderId in (select oi.order.orderId from OrderItem oi where oi.product.id in (select p.id from Product p where p.type like 'UNIT' and p.commodityId in (select u.unitId from Unit u where u.batch.id in (select cb.id from CourseBatch cb where cb.course.id in (select c.id from Course c where c.courseExamSegment in(:examSegmentList))))))) or (o.orderId in (select oi.order.orderId from OrderItem oi where oi.product.id in (select p.id from Product p where p.type like 'COURSE' and p.commodityId in (select cb.id from CourseBatch cb where cb.course.id in (select c.id from Course c where c.courseExamSegment in(:examSegmentList))))))) and o.paymentDate between :startDate and :endDate and o.orderType in (:orderTypes) and o.orderStatus in ('REFUNDED') and o.paymentMode not like 'free'")
  List<Order> findAllRefundOrderDetails(@Param("examSegmentList") List<ExamSegmentTypes> examSegments,@Param("startDate")Timestamp startDate,@Param("endDate")Timestamp endDate,@Param("orderTypes") List<OrderType> orderTypes);
  
  @Query("select o from Order o where ((o.orderId in (select oi.order.orderId from OrderItem oi where oi.product.id in (select p.id from Product p where p.type like 'UNIT' and p.commodityId in (select u.unitId from Unit u where u.batch.id in (select cb.id from CourseBatch cb where cb.course.id in (select c.id from Course c where c.courseExamSegment in(:examSegmentList))))))) or (o.orderId in (select oi.order.id from OrderItem oi where oi.product.id in (select p.id from Product p where p.type like 'COURSE' and p.commodityId in (select cb.id from CourseBatch cb where cb.course.id in (select c.id from Course c where c.courseExamSegment in(:examSegmentList))))))) and o.paymentDate between :startDate and :endDate and o.orderType in (:orderTypes) and o.orderStatus in ('APPROVED') and o.refundAmount >0 and o.paymentMode not like 'free'")
  List<Order> findAllPartialOrderDetails(@Param("examSegmentList") List<ExamSegmentTypes> examSegments,@Param("startDate")Timestamp startDate,@Param("endDate")Timestamp endDate,@Param("orderTypes") List<OrderType> orderTypes);
  
}
