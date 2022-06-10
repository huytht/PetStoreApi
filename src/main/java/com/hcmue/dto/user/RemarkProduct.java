package com.hcmue.dto.user;

import javax.validation.constraints.NotNull;

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
}
