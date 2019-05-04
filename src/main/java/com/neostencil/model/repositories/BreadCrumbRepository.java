package com.neostencil.model.repositories;

import com.neostencil.model.entities.BreadCrumb;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BreadCrumbRepository extends JpaRepository<BreadCrumb, Integer> {

  LinkedList<BreadCrumb> findByParentId(int id);

  BreadCrumb findByBreadcrumbId(int id);

  BreadCrumb findByText(String text);

  BreadCrumb findByLink(String link);

  List<BreadCrumb> findAllBy();

}
