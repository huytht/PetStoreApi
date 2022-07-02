package com.hcmue.service;

import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.purchase.Purchase;
import com.hcmue.dto.purchase.PurchaseResponse;

public interface CheckoutService {

	AppServiceResult<PurchaseResponse> placeOrder(Purchase purchase);
}
