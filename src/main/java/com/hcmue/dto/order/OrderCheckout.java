package com.hcmue.dto.order;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter	
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCheckout {

	private BigDecimal totalPrice;
	
	private Long totalQuantity;
	
	private Long orderStatusId;
	
	private Long orderPaymentId;
}
