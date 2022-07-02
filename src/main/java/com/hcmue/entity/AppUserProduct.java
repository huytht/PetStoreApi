package com.hcmue.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.hcmue.entity.AppUserProduct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user_product")
public class AppUserProduct implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private AppUserProductId appUserProductId;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name = "app_user_id")
	private AppUser appUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("productId")
	@JoinColumn(name = "product_id")
	private Product product;
	
	@Column(name = "rate")
	private Integer rate;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "favourite")
	private Boolean favourite;
	
	@CreationTimestamp
	@Column(name = "date_new")
	private Date dateNew;
	
	@UpdateTimestamp
	@Column(name = "date_edit")
	private Date dateEdit;
}
