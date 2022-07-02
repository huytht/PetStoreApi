package com.hcmue.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.hcmue.dto.order.OrderItemDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`order`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_tracking_number")
	private String orderTrackingNumber;
	
	@Column(name = "total_price")
	private BigDecimal totalPrice;
	
	@Column(name = "total_quantity")
	private Long totalQuantity;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "order_status_id")
	private OrderStatus orderStatus;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "order_payment_id")
	private Payment payment;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "user_id", nullable = true)
	private AppUser user;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="order")
	private Set<OrderItem> orderItems;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="shipping_address_id", referencedColumnName = "id")
	private Address shippingAddress;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="billing_address_id", referencedColumnName = "id")
	private Address billingAddress;
	
	@Column(name = "order_date", updatable = false)
	@CreationTimestamp
	private Date orderDate;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private Date lastUpdated;
	
	public void add(OrderItem item) {
		if (item != null) {
			if (orderItems == null) {
				orderItems = new HashSet<OrderItem>();
			}
		}
		orderItems.add(item);
		item.setOrder(this);
	}
	

}
