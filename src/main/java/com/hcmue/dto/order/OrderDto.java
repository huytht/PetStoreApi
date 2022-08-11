package com.hcmue.dto.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hcmue.dto.address.AddressDto;
import com.hcmue.entity.Order;
import com.hcmue.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
	
	private Long id;

	private String orderTrackingNumber;
	
	private Date orderDate;
	
	private BigDecimal totalPrice;
	
	private Long totalQuantity;
	
	private OrderStatusDto orderStatus;
	
	private Long paymentId;
	
	private List<OrderItemDto> orderItems = new ArrayList<OrderItemDto>();
	
	private AddressDto shippingAddress;

	public static OrderDto CreateFromEntity(Order src) {
		OrderDto dto = new OrderDto();

		dto.id = src.getId();
		dto.orderTrackingNumber = src.getOrderTrackingNumber();
		dto.orderDate = src.getOrderDate();
		dto.totalPrice = src.getTotalPrice();
		dto.totalQuantity = src.getTotalQuantity();
		dto.orderStatus = OrderStatusDto.CreateFromEntity(src.getOrderStatus());
		if (src.getPayment() != null)
			dto.paymentId = src.getPayment().getId();
		if (!src.getOrderItems().isEmpty())
			for (OrderItem item : src.getOrderItems()) {
				dto.orderItems.add(OrderItemDto.CreateFromEntity(item));
			}
		dto.shippingAddress =  AddressDto.CreateFromEntity(src.getShippingAddress());
		return dto;
	}
}
