package com.neostencil.model.repositories;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.neostencil.common.enums.ProductType;
import com.neostencil.model.entities.Product;
import com.neostencil.projections.ProductProjection;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

	Product findById(int id);

	Product findByCommodityIdAndType(int commodityID, ProductType productType);

	List<Product> findByCommodityIdIn(List<Integer> commodityIds);

	// List<Product> findByType(ProductType productType);

	List<ProductProjection> findAllProjectedBy();

	List<ProductProjection> findByType(ProductType productType);

	List<Product> findAllByCreatedAtBetween(Timestamp startDate, Timestamp endDate);

}
