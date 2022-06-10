package com.hcmue.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hcmue.entity.AppUserProductId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserProductId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "app_user_id")
	private Long appUserId;
	
	@Column(name = "product_id")
	private Long productId;
}
