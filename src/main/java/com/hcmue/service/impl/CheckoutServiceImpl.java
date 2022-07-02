package com.hcmue.service.impl;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmue.constant.AppError;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.order.OrderItemDto;
import com.hcmue.dto.product.ProductDto;
import com.hcmue.dto.purchase.Purchase;
import com.hcmue.dto.purchase.PurchaseResponse;
import com.hcmue.entity.AppUser;
import com.hcmue.entity.Order;
import com.hcmue.entity.OrderItem;
import com.hcmue.entity.Product;
import com.hcmue.repository.AppUserRepository;
import com.hcmue.repository.OrderRepository;
import com.hcmue.repository.ProductRepository;
import com.hcmue.service.CheckoutService;
import com.hcmue.util.AppUtils;

@Service
public class CheckoutServiceImpl implements CheckoutService {

	private final Logger logger = LoggerFactory.getLogger(CheckoutServiceImpl.class);
	private AppUserRepository appUserRepository;
	private ProductRepository productRepository;
    private OrderRepository orderRepository;

    @Autowired
    public CheckoutServiceImpl(AppUserRepository appUserRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.appUserRepository = appUserRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

	@Transactional
	@Override
	public AppServiceResult<PurchaseResponse> placeOrder(Purchase purchase) {
			
		try {
			AppUser appUser = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
			
			if (appUser == null) {
				logger.warn("Not logged in!");

				return new AppServiceResult<PurchaseResponse>(false, AppError.Validattion.errorCode(),
						"Not logged in!", null);
			}
			
			// retrieve the order into from DTO
			Order order = purchase.getOrder();
			
			// generate tracking number using UUID 
			String orderTrackingNumber = UUID.randomUUID().toString();
			order.setOrderTrackingNumber(orderTrackingNumber);
			
			// get orderItems
			List<OrderItem> orderItems = purchase.getOrderItems();
			orderItems.forEach(item -> System.out.println(item.toString()));
			
			for(OrderItem item : purchase.getOrderItems()) {
//				System.out.println(item.getPrice());
				Product product = productRepository.findById(item.getProductId()).orElse(null);
				if (product != null)
					if (!product.getStatus()) {
						logger.warn("product: " + product.getId() + " is out of stock!");
						
						return new AppServiceResult<PurchaseResponse>(false, AppError.Validattion.errorCode(),
								"Sản phẩm: " + product.getName() + " đã hết hàng!", null);
					}
					else if (product.getAmount() - item.getQuantity() < 0) {
						logger.warn("product: " + product.getId() + " is exceed the quantity in stock!");
						
						return new AppServiceResult<PurchaseResponse>(false, AppError.Validattion.errorCode(),
								"Sản phẩm: " + product.getName() + " có số lượng vượt quá số lượng tồn!", null);
					} 
					else {
						order.add(item);
					}
				else {
					logger.warn("product: " + item.getProductId() + " is not exist!");
					
					return new AppServiceResult<PurchaseResponse>(false, AppError.Validattion.errorCode(),
							"Sản phẩm: " + item.getProductId() + " không tồn tại!", null);
				}	
			}

			// get address
			order.setBillingAddress(purchase.getBillingAddress());
			order.setShippingAddress(purchase.getShippingAddress());
			
			// get customer info
			appUser.add(order);
			
			// save
			Order insertedOrder = orderRepository.save(order);
			
			return new AppServiceResult<PurchaseResponse>(true, 0, "Succeed!", new PurchaseResponse(orderTrackingNumber, insertedOrder.getOrderDate()));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new AppServiceResult<PurchaseResponse>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	
}
