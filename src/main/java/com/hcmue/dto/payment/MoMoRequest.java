package com.hcmue.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoMoRequest {
	private String partnerCode;
	private String partnerName;
	private String storeId;
	private String requestType;
	private String ipnUrl;
	private String redirectUrl;
	private String orderId;
	private double amount;
	private String lang;
	private boolean autoCapture;
	private String orderInfo;
	private String requestId;
	private String extraData;
	private String signature;
}
