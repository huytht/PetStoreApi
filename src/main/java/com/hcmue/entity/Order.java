package com.hcmue.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
	
	@Column(name = "order_date")
	@CreationTimestamp
	private Date orderDate;
	
	@Column(name = "expected_delivery_date")
	private Date expectedDeliveryDate;
	
	@Column(name = "total_amount")
	private BigDecimal totalAmount;
	
	@Column(name = "total_quantity")
	private Long totalQuantity;
	
	@Column(name = "order_status")
	private int orderStatus;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "user_id", nullable = true)
	private AppUser user;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="order")
	private List<OrderItem> orderItems;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="shipping_address_id", referencedColumnName = "id")
	private Address shippingAddress;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="billing_address_id", referencedColumnName = "id")
	private Address billingAddress;
	

}
