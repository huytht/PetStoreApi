package com.hcmue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcmue.entity.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {	
	Pet findByName(String name);
}
