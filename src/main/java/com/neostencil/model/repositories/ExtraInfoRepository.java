package com.neostencil.model.repositories;

import com.neostencil.model.entities.Address;
import com.neostencil.model.entities.ExtraInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtraInfoRepository extends JpaRepository<ExtraInfo, Integer> {

  ExtraInfo findByMobileNumber(String mobileNumber);

}
