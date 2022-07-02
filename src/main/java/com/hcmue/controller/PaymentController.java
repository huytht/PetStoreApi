package com.hcmue.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.hcmue.config.PaypalPaymentIntent;
import com.hcmue.config.PaypalPaymentMethod;
import com.hcmue.service.PaypalService;
import com.hcmue.service.impl.PaypalServiceImpl;
import com.hcmue.service.impl.ProductServiceImpl;
import com.hcmue.util.AppUtils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

//@RestController
@Controller
//@RequestMapping("/pay")
public class PaymentController {
	
	public static final String URL_PAYPAL_SUCCESS = "pay/success";
	public static final String URL_PAYPAL_CANCEL = "pay/cancel";
	
	private final Logger logger = LoggerFactory.getLogger(PaypalServiceImpl.class);
	
	@Autowired
	private PaypalService paypalService;

	@GetMapping("/")
	public String index(){
		return "index";
	}
	
	@PostMapping("/pay")
	public String pay(HttpServletRequest request, @RequestParam("price") double price ){
		String cancelUrl = AppUtils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
		String successUrl = AppUtils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
		try {
			Payment payment = paypalService.createPayment(
					price,
					"USD",
					PaypalPaymentMethod.paypal,
					PaypalPaymentIntent.sale,
					"payment description",
					cancelUrl,
					successUrl);
			for(Links links : payment.getLinks()){
				if(links.getRel().equals("approval_url")){
					return "redirect:" + links.getHref();
				}
			}
		} catch (PayPalRESTException e) {
			logger.error(e.getMessage());
		}
		return "redirect:/";
	}
	
	@GetMapping(URL_PAYPAL_CANCEL)
	public String cancelPay(){
		return "cancel";
	}
	
	@GetMapping(URL_PAYPAL_SUCCESS)
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if(payment.getState().equals("approved")){
				return "success";
			}
		} catch (PayPalRESTException e) {
			logger.error(e.getMessage());
		}
		return "redirect:/";
	}
}
