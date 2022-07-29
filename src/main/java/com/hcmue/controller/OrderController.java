package com.hcmue.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcmue.domain.AppBaseResult;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.HttpResponse;
import com.hcmue.dto.HttpResponseError;
import com.hcmue.dto.HttpResponseSuccess;
import com.hcmue.dto.order.OrderDto;
import com.hcmue.dto.pagination.PageDto;
import com.hcmue.dto.pagination.PageParam;
import com.hcmue.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	private OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@GetMapping
	public ResponseEntity<HttpResponse> getOrderList(@RequestParam(name = "order-status", defaultValue = "0") Long orderStatus) {
		
		AppServiceResult<List<OrderDto>> result = orderService.getListAllOrder(orderStatus);
		
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<OrderDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@PostMapping("/confirm")
	public ResponseEntity<HttpResponse> confirmOrder(@RequestParam(name = "order-tracking-number") String orderTrackingNumber) {
		AppBaseResult result = orderService.updateOrderStatus(orderTrackingNumber, (long)3);
		
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@PostMapping("/cancel")
	public ResponseEntity<HttpResponse> cancelOrder(@RequestParam(name = "order-tracking-number") String orderTrackingNumber) {
		AppBaseResult result = orderService.updateOrderStatus(orderTrackingNumber, (long)5);
		
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@DeleteMapping
	public ResponseEntity<HttpResponse> deleteOrder(@RequestParam(name = "order-id") Long orderId) {
		AppBaseResult result = orderService.deleteOrder(orderId);
		
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
