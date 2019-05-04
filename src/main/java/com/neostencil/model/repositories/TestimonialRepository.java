package com.neostencil.model.repositories;

import com.neostencil.model.entities.Testimonial;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestimonialRepository extends JpaRepository<Testimonial, Integer> {

  Testimonial findById(int id);

  List<Testimonial> findAllByPositionIsGreaterThanOrderByPosition(Integer position);

  List<Testimonial> findAllByPositionIsGreaterThanAndPositionIsLessThanOrderByPosition(int greaterThanPosition,int lessThanEqualPosition);
}
