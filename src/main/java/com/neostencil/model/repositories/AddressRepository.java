package com.neostencil.model.repositories;

import com.neostencil.model.entities.Address;
import com.neostencil.model.entities.User;
import com.neostencil.projections.AddressProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {

  Address findByAddressId(int id);

  List<Address> findByUser(User u);
  
  List<Address> findByUserAndDefaultAddress(User u,boolean defaultAddress);
  
  List<AddressProjection> findAllProjectedByUser(User u);

 // Address findByUserAndDefaultAddress(User user,boolean defaultAddress);
}
