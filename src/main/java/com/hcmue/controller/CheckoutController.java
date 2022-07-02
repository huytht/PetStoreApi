package com.hcmue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.HttpResponse;
import com.hcmue.dto.HttpResponseError;
import com.hcmue.dto.HttpResponseSuccess;
import com.hcmue.dto.purchase.Purchase;
import com.hcmue.dto.purchase.PurchaseResponse;
import com.hcmue.service.CheckoutService;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {
	private CheckoutService checkoutService;

	@Autowired
	public CheckoutController(CheckoutService checkoutService) {
		this.checkoutService = checkoutService;
	}
	
	@PostMapping("/purchase")
	public ResponseEntity<HttpResponse> placeOrder(@RequestBody Purchase purchase) {
		
		AppServiceResult<PurchaseResponse> result = checkoutService.placeOrder(purchase);
		
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PurchaseResponse>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

}
