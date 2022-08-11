package com.hcmue.service;

import com.hcmue.config.PaypalPaymentIntent;
import com.hcmue.config.PaypalPaymentMethod;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PaypalService {
	public Payment createPayment(Double total, String currency, PaypalPaymentMethod method, PaypalPaymentIntent intent,
								String description, String cancelUrl, String successUrl) throws PayPalRESTException;
		
	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
