package com.hcmue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hcmue.config.PaypalPaymentIntent;
import com.hcmue.config.PaypalPaymentMethod;
import com.hcmue.service.PaypalService;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PaypalServiceImpl implements PaypalService{

	@Autowired
	private APIContext apiContext;
	
	@Value("${paypal.client.id}")
	private String clientId;
	
	@Value("${paypal.client.secret}")
	private String clientSecret;
	
	public Payment createPayment(
			Double total,
			String currency,
			PaypalPaymentMethod method,
			PaypalPaymentIntent intent,
			String description,
			String cancelUrl,
			String successUrl) throws PayPalRESTException{
		apiContext = new APIContext(clientId, clientSecret, "sandbox");
		Amount amount = new Amount();
		amount.setCurrency(currency);
		amount.setTotal(String.format("%.2f", total));
		Transaction transaction = new Transaction();
		transaction.setDescription(description);
		transaction.setAmount(amount);
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);
		Payer payer = new Payer();
		payer.setPaymentMethod(method.toString());
		Payment payment = new Payment();
		payment.setIntent(intent.toString());
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		payment.setRedirectUrls(redirectUrls);
		apiContext.setMaskRequestId(true);
		return payment.create(apiContext);
	}
	
	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);
		return payment.execute(apiContext, paymentExecute);
	}

}
