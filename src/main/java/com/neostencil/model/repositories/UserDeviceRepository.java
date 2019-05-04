package com.neostencil.model.repositories;

import com.neostencil.model.entities.UserDevice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Integer> {

	List<UserDevice> findByFingerPrintOrderByDeviceIDDesc(String fingerPrint);
}
