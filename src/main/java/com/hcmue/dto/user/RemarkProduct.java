package com.hcmue.dto.user;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.annotations.UpdateTimestamp;

import com.hcmue.entity.AppUserProduct;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemarkProduct {
	
	@NotNull
	private Long productId;
	
	private String remark;
	
	private Integer rate;
	
	private Boolean favourite;
	
	private Date date;
	
	public static RemarkProduct CreateFromEntity(AppUserProduct src) {
		RemarkProduct dto = new RemarkProduct();
		
		dto.productId = src.getProduct().getId();
		dto.remark = src.getRemark();
		dto.favourite = src.getFavourite();
		dto.date = src.getDateEdit();
		
		return dto;
	}
}
