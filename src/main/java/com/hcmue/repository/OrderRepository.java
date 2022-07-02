package com.hcmue.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcmue.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {	
	public Page<Order> findAllByUserIdAndOrderStatusIdOrderByOrderDateDesc(Long userId, Long orderStatusId, Pageable pageable);
	public Page<Order> findAllByUserIdOrderByOrderDateDesc(Long userId, Pageable pageable);
}
