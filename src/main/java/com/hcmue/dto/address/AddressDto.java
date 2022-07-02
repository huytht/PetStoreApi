package com.hcmue.dto.address;


import com.hcmue.entity.Address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
	private String provinceCity;
	
	private String district;
	
	private String wardCommune;
	
	private String exactAddress;
	
	public static AddressDto CreateFromEntity(Address src) { 
		
		AddressDto dto = new AddressDto();
		dto.provinceCity = src.getProvinceCity();
		dto.district = src.getDistrict();
		dto.wardCommune = src.getWardCommune();
		dto.exactAddress = src.getExactAddress();
		
		return dto;
	}
}
