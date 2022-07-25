package com.hcmue.dto.purchase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.hcmue.dto.order.OrderCheckout;
import com.hcmue.dto.order.OrderItemDto;
import com.hcmue.entity.Address;
import com.hcmue.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.hcmue.entity.AppUser;

@Setter	
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Purchase {
	private AppUser appUser;
	private Address shippingAddress;
	private Address billingAddress;
	private OrderCheckout order;
	private Set<OrderItemDto> orderItems;
}
