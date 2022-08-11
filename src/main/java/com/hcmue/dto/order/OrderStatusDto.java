package com.hcmue.dto.order;

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
	
	private Long id;
	
	private String name;
	
	public static OrderStatusDto CreateFromEntity(OrderStatus src) {
		OrderStatusDto dto = new OrderStatusDto();
		
		dto.id = src.getId();
		dto.name = src.getName();
		
		return dto;
	}
}
