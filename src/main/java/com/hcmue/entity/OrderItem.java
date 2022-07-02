package com.hcmue.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "price")
	private BigDecimal price;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name="quantity")
	private int quantity;
	
	@Column(name="pet_id")
	private Long petId;
	
	@Column(name="pet_product_id")
	private Long petProductId;
	
	@ManyToOne
	@JoinColumn(name="order_id")
	private Order order;
}
