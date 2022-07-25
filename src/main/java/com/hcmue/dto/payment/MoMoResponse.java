package com.hcmue.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MoMoResponse {
	private String partnerCode;
	private String orderId;
	private String requestId;
	private Long amount;
	private Long responseTime;
	private String message;
	private Integer resultCode;
	private String payUrl;
}
