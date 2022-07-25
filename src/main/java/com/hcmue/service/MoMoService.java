package com.hcmue.service;

import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.payment.MoMoResponse;

public interface MoMoService {
	public AppServiceResult<MoMoResponse> createPayment(Long amount, String notifyUrl, String returnUrl);
}
