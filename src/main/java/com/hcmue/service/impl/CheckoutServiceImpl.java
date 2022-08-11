package com.hcmue.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmue.constant.AppError;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.order.OrderCheckout;
import com.hcmue.dto.order.OrderItemDto;
import com.hcmue.dto.purchase.Purchase;
import com.hcmue.dto.purchase.PurchaseResponse;
import com.hcmue.entity.AppUser;
import com.hcmue.entity.Order;
import com.hcmue.entity.OrderItem;
import com.hcmue.entity.Product;
import com.hcmue.repository.AppUserRepository;
import com.hcmue.repository.OrderRepository;
import com.hcmue.repository.OrderStatusRepository;
import com.hcmue.repository.PaymentRepository;
import com.hcmue.repository.ProductRepository;
import com.hcmue.service.CheckoutService;
import com.hcmue.service.OrderItemService;
import com.hcmue.util.AppUtils;

@Service
public class CheckoutServiceImpl implements CheckoutService {

	private final Logger logger = LoggerFactory.getLogger(CheckoutServiceImpl.class);
	private AppUserRepository appUserRepository;
	private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderStatusRepository orderStatusRepository;
    private OrderItemService orderItemService;
    private PaymentRepository paymentRepository;

    @Autowired
    public CheckoutServiceImpl(AppUserRepository appUserRepository, OrderRepository orderRepository, 
    							ProductRepository productRepository, OrderStatusRepository orderStatusRepository, 
    							PaymentRepository paymentRepository, OrderItemService orderItemService) {
        this.appUserRepository = appUserRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.paymentRepository = paymentRepository;
        this.orderItemService = orderItemService;
    }

	@SuppressWarnings("unused")
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
			OrderCheckout orderCheckout = purchase.getOrder();
			Order order = new Order();
			
			// generate tracking number using UUID 
			String orderTrackingNumber = UUID.randomUUID().toString();
			order.setOrderTrackingNumber(orderTrackingNumber);
			order.setOrderStatus(orderStatusRepository.findById(orderCheckout.getOrderStatusId()).orElse(null));
			order.setPayment(paymentRepository.findById(orderCheckout.getOrderPaymentId()).orElse(null));
			order.setTotalPrice(orderCheckout.getTotalPrice());
			order.setTotalQuantity(orderCheckout.getTotalQuantity());
			
			// get address
			order.setBillingAddress(purchase.getBillingAddress());
			order.setShippingAddress(purchase.getShippingAddress());

			// get customer info
			order.setUser(appUser);			

			Order insertedOrder = orderRepository.save(order);
			
			// check status of each product
			for(OrderItemDto item : purchase.getOrderItems()) {
				Product product = productRepository.findById(item.getProductId()).orElse(null);
				if (product != null) {
					if (!product.getStatus()) {
						logger.warn("product: " + product.getId() + " is out of stock!");
						
						return new AppServiceResult<PurchaseResponse>(false, AppError.Validattion.errorCode(),
								"Sản phẩm: " + product.getName() + " đã hết hàng!", null);
					} else if (product.getAmount() - item.getQuantity() < 0) {
						logger.warn("product: " + product.getId() + " is exceed the quantity in stock!");
						
						return new AppServiceResult<PurchaseResponse>(false, AppError.Validattion.errorCode(),
								"Sản phẩm: " + product.getName() + " có số lượng vượt quá số lượng tồn!", null);
					} else {
						OrderItem orderItem = new OrderItem(item.getPrice(), item.getQuantity(), item.getProductId(), insertedOrder.getId());
						orderItemService.addOrderedProducts(orderItem);
					}
				} else {
					logger.warn("product: " + item.getProductId() + " is not exist!");
					
					return new AppServiceResult<PurchaseResponse>(false, AppError.Validattion.errorCode(),
							"Sản phẩm: " + item.getProductId() + " không tồn tại!", null);
				}	
			}
		
			return new AppServiceResult<PurchaseResponse>(true, 0, "Succeed!", new PurchaseResponse(orderTrackingNumber, insertedOrder.getOrderDate()));
		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<PurchaseResponse>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	
}
