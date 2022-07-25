package com.hcmue.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmue.entity.OrderItem;
import com.hcmue.repository.OrderItemRepository;
import com.hcmue.service.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService{
	
	@Autowired
    private OrderItemRepository orderItemRepository;

    public void addOrderedProducts(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }
}
