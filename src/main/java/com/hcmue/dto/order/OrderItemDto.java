package com.hcmue.dto.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hcmue.entity.Address;
import com.hcmue.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
	
	private BigDecimal price;
	
	private String imageUrl;
	
	private int quantity;
	
	private Long productId;
	
	private String name;
	
	public static OrderItemDto CreateFromEntity(OrderItem src) {
		OrderItemDto dto = new OrderItemDto();
		dto.price = src.getPrice();
		dto.quantity = src.getQuantity();
		if (src.getProduct() != null) {
			dto.name = src.getProduct().getName();
			dto.imageUrl = src.getProduct().getProductImages().iterator().next().getImagePath();	
		}
		
		
		return dto;
	}
}
