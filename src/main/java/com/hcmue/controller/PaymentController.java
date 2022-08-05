package com.hcmue.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.hcmue.constant.AppError;
import com.hcmue.domain.AppBaseResult;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.HttpResponse;
import com.hcmue.dto.HttpResponseError;
import com.hcmue.dto.HttpResponseSuccess;
import com.hcmue.dto.order.OrderDto;
import com.hcmue.dto.pagination.PageDto;
import com.hcmue.dto.payment.MoMoResponse;
import com.hcmue.dto.product.ProductShortDto;
import com.hcmue.entity.AppUser;
import com.hcmue.repository.AppUserRepository;
import com.hcmue.service.MoMoService;
import com.hcmue.service.OrderService;
import com.hcmue.service.PaypalService;
import com.hcmue.service.impl.PaypalServiceImpl;
import com.hcmue.service.impl.ProductServiceImpl;
import com.hcmue.util.AppUtils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

//@RestController
@Controller
@RequestMapping("/pay")
public class PaymentController {
	
	public static final String URL_PAYPAL_SUCCESS = "pay/success";
	public static final String URL_PAYPAL_CANCEL = "pay/cancel";
	
	private final Logger logger = LoggerFactory.getLogger(PaypalServiceImpl.class);
	
	@Autowired
	private PaypalService paypalService;
	
	@Autowired
	private MoMoService momoService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AppUserRepository appUserRepository;

	@GetMapping("/")
	public String index(){
		return "index";
	}
	
	@PostMapping("/paypal")
	public ResponseEntity<HttpResponse> pay(HttpServletRequest request, @RequestParam("amount") double amount, @RequestParam("orderTrackingNumber") String orderTrackingNumber){
		String cancelUrl = AppUtils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
		String successUrl = AppUtils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
		try {
			
			Payment payment = paypalService.createPayment(
					amount,
					"USD",
					PaypalPaymentMethod.paypal,
					PaypalPaymentIntent.sale,
					"payment description",
					cancelUrl,
					successUrl + "?orderTrackingNumber=" + orderTrackingNumber);
			for(Links links : payment.getLinks()){
				if(links.getRel().equals("approval_url")){
					return ResponseEntity.ok(new HttpResponseSuccess<String>(links.getHref()));
				}
			}
		} catch (PayPalRESTException e) {
			logger.error(e.getMessage());
		}
		return ResponseEntity.badRequest().body(new HttpResponseError(null, ""));
	}
	
	@PostMapping("/momo")
	public ResponseEntity<HttpResponse> payByMoMo(HttpServletRequest request, @RequestParam("amount") Long amount, @RequestParam("orderTrackingNumber") String orderTrackingNumber) {
		String cancelUrl = AppUtils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
		String successUrl = AppUtils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
		AppUser appUser = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
		
		if (appUser == null) {
			logger.warn("Not logged in!");
		}
		AppServiceResult<MoMoResponse> result = momoService.createPayment(amount, cancelUrl, successUrl + "?orderTrackingNumber=" + orderTrackingNumber);
		if (result.isSuccess())
			return ResponseEntity.ok(new HttpResponseSuccess<String>(result.getData().getPayUrl()));
		else
			return ResponseEntity.badRequest().body(new HttpResponseError(null, ""));
	}
	
	@GetMapping("/cancel")
	public String cancelPay(){
		return "cancel";
	}
	
	@GetMapping("/success")
	public String successPay(@RequestParam("orderTrackingNumber") String orderTrackingNumber, 
							@RequestParam(name = "paymentId", defaultValue = "") String paymentId, 
							@RequestParam(name = "PayerID", defaultValue = "") String payerId) throws PayPalRESTException{
		Payment payment = paypalService.executePayment(paymentId, payerId);

		orderService.updateOrderStatus(orderTrackingNumber, (long)2);

		return "success";
	}
}
