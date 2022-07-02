package com.hcmue.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "province_city")
	private String provinceCity;
	
	@Column(name = "district")
	private String district;
	
	@Column(name = "ward_commune")
	private String wardCommune;
	
	@Column(name = "exact_address")
	private String exactAddress;
	
	@Column(name = "zip_code")
	private String zipCode;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	private Order order;
	
}
