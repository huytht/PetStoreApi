package com.hcmue.dto.origin;

import com.hcmue.entity.Origin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OriginDto {

	private String name;

	public static OriginDto CreateFromEntity(Origin src) {
		OriginDto dto = new OriginDto();

		dto.name = src.getName();

		return dto;
	}
}
