package com.hcmue.dto.purchase;

import java.util.List;

import com.hcmue.entity.Address;
import com.hcmue.entity.Order;
import com.hcmue.entity.OrderItem;

import lombok.Data;

import com.hcmue.entity.AppUser;

@Data
public class Purchase {
	private AppUser appUser;
	private Address shippingAddress;
	private Address billingAddress;
	private Order order;
	private List<OrderItem> orderItems;
}
