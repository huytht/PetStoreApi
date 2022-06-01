package com.hcmue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcmue.entity.Pet;
import com.hcmue.entity.PetProduct;

@Repository
public interface PetProductRepository extends JpaRepository<PetProduct, Long> {	
	Pet findByName(String name);
}
