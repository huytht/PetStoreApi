package com.hcmue.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hcmue.constant.AppError;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.order.OrderDto;
import com.hcmue.dto.pagination.PageDto;
import com.hcmue.dto.pagination.PageParam;
import com.hcmue.dto.product.ProductShortDto;
import com.hcmue.entity.AppUser;
import com.hcmue.entity.Order;
import com.hcmue.repository.AppUserRepository;
import com.hcmue.repository.OrderRepository;
import com.hcmue.service.OrderService;
import com.hcmue.util.AppUtils;

@Service
public class OrderServiceImpl implements OrderService{

	private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	private OrderRepository orderRepository;
	private AppUserRepository appUserRepository;
	
	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository, AppUserRepository appUserRepository) {
		this.orderRepository = orderRepository;
		this.appUserRepository = appUserRepository;
	}
	
	@Override
	public AppServiceResult<PageDto<OrderDto>> getListOrder(Long orderStatus, PageParam pageParam) {
		
		try {
			AppUser appUser = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
			
			if (appUser == null) {
				logger.warn("Not logged in!");

				return new AppServiceResult<PageDto<OrderDto>>(false, AppError.Validattion.errorCode(),
						"Not logged in!", null);
			}
			
			Page<Order> orders = orderStatus == 0 
									? orderRepository.findAllByUserIdOrderByOrderDateDesc(appUser.getId(), pageParam.getPageable()) 
									: orderRepository.findAllByUserIdAndOrderStatusIdOrderByOrderDateDesc(appUser.getId(), orderStatus, pageParam.getPageable());
			Page<OrderDto> dtoPage = orders.map(item -> OrderDto.CreateFromEntity(item));
			
			return new AppServiceResult<PageDto<OrderDto>>(true, 0, "Succeed!", new PageDto<OrderDto>(dtoPage));
		} catch (Exception e) {
			
			e.printStackTrace();

			return new AppServiceResult<PageDto<OrderDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}


}
