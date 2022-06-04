package com.hcmue.dto.product;

import java.util.ArrayList;
import java.util.List;

import com.hcmue.dto.breed.BreedDto;
import com.hcmue.dto.origin.OriginDto;
import com.hcmue.entity.Origin;
import com.hcmue.entity.Pet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {
	
	private Long id;

	private String name;
	
	private Long amount;

	private List<OriginDto> origins = new ArrayList<OriginDto>();
	
	private String description;

	private String imagePath;
	
	private Boolean gender;
	
	private Boolean status;

	private BreedDto breed;
	
	public static PetDto CreateFromEntity(Pet src) {
		PetDto petDto = new PetDto();

		petDto.id = src.getId();
		petDto.name = src.getName();
		petDto.amount = src.getAmount();
		if (src.getOrigins() != null)
			for (Origin origin : src.getOrigins()) {
				petDto.origins.add(OriginDto.CreateFromEntity(origin));
			}
		petDto.description = src.getDescription();
		petDto.imagePath = src.getImagePath();
		petDto.status = src.getStatus();
		petDto.gender = src.getStatus();
		petDto.breed = BreedDto.CreateFromEntity(src.getBreed());

		return petDto;
	}
}
