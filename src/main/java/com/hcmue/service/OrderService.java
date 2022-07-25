package com.hcmue.service;

import com.hcmue.domain.AppBaseResult;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.order.OrderDto;
import com.hcmue.dto.pagination.PageDto;
import com.hcmue.dto.pagination.PageParam;

public interface OrderService {

	AppServiceResult<PageDto<OrderDto>> getListOrder(Long orderStatus, PageParam pageParam);
	
	AppBaseResult updateOrderStatus(String orderTrackingNumber, Long orderStatusId);
	
	AppServiceResult<OrderDto> getLatestUnpaidOrder(Long userId);
}
