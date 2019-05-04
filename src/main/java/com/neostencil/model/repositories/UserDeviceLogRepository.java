package com.neostencil.model.repositories;

import com.neostencil.model.entities.UserDeviceLog;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserDeviceLogRepository extends JpaRepository<UserDeviceLog, Integer> {

}
