package com.hcmue.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hcmue.constant.AppError;
import com.hcmue.domain.AppBaseResult;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.order.OrderDto;
import com.hcmue.dto.order.OrderStatusDto;
import com.hcmue.dto.pagination.PageDto;
import com.hcmue.dto.pagination.PageParam;
import com.hcmue.entity.AppUser;
import com.hcmue.entity.Order;
import com.hcmue.entity.OrderStatus;
import com.hcmue.repository.AppUserRepository;
import com.hcmue.repository.OrderRepository;
import com.hcmue.repository.OrderStatusRepository;
import com.hcmue.service.OrderService;
import com.hcmue.util.AppUtils;

@Service
public class OrderServiceImpl implements OrderService{

	private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	private OrderRepository orderRepository;
	private AppUserRepository appUserRepository;
	private OrderStatusRepository orderStatusRepository;
	
	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository, AppUserRepository appUserRepository, OrderStatusRepository orderStatusRepository) {
		this.orderRepository = orderRepository;
		this.appUserRepository = appUserRepository;
		this.orderStatusRepository = orderStatusRepository;
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
	
	@Override
	public AppServiceResult<List<OrderDto>> getListAllOrder(Long orderStatus) {
		
		try {
			
			List<Order> orders = orderStatus == 0 
									? orderRepository.findAllByOrderByOrderDateDesc() 
									: orderRepository.findAllByOrderStatusIdOrderByOrderDateDesc(orderStatus);
			List<OrderDto> dtoList = new ArrayList<OrderDto>();
			if (orders != null && orders.size() > 0)
				orders.forEach(item -> dtoList.add(OrderDto.CreateFromEntity(item)));
					
			
			return new AppServiceResult<List<OrderDto>>(true, 0, "Succeed!", dtoList);
		} catch (Exception e) {
			
			e.printStackTrace();

			return new AppServiceResult<List<OrderDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppBaseResult updateOrderStatus(String orderTrackingNumber, Long orderStatusId) {
		try {
			
			Order order = orderRepository.findByOrderTrackingNumber(orderTrackingNumber);
			
			if (order == null) {
				logger.warn("Order id is not exist!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Order id is not exist!");
			}
			
			OrderStatus orderStatus = orderStatusRepository.findById(orderStatusId).orElse(null);
			
			if (orderStatus == null) {
				logger.warn("Order status id is not exist!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Order status id is not exist!");
			}
			
			order.setOrderStatus(orderStatus);
			
			orderRepository.save(order);
			
			return AppBaseResult.GenarateIsSucceed();
		} catch (Exception e) {
			e.printStackTrace();
			
			return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), AppError.Unknown.errorMessage());
		}
	}

	@Override
	public AppServiceResult<OrderDto> getLatestUnpaidOrder(Long userId) {
		try {
			
			List<Order> orderUnpaidList = orderRepository.findOrderUnpaidByAppUser(userId);
			
			OrderDto result = OrderDto.CreateFromEntity(orderUnpaidList.get(0));
			
			return new AppServiceResult<OrderDto>(true, 0, "success", result);
		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<OrderDto>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}


}
