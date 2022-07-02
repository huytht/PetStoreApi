package com.hcmue.dto.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hcmue.entity.Address;
import com.hcmue.entity.OrderItem;
import com.hcmue.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusDto {
	
	private String name;
	
	public static OrderStatusDto CreateFromEntity(OrderStatus src) {
		OrderStatusDto dto = new OrderStatusDto();

		dto.name = src.getName();
		
		return dto;
	}
}
