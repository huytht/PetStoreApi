package com.hcmue.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
@ToString
public class AppUser implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;
	
	@Column(name = "enabled")
	private Boolean enabled;
	
	@Column(name = "account_non_locked")
	private Boolean accountNonLocked;

	@JsonIgnoreProperties("appUsers")
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "app_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<AppRole> appRoles = new HashSet<>();

	@JsonIgnoreProperties("appUser")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "userinfo_id", referencedColumnName = "id")
	private UserInfo userInfo;
	
	@JsonIgnoreProperties("appUser")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "appUser")
	private Set<VerificationToken> verificationTokens;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<Order> orders;
	
	@CreationTimestamp
	@Column(name = "date_new")
	private Date dateNew;	

	@Column(name = "user_new")
	private String userNew;

	@UpdateTimestamp
	@Column(name = "date_edit")
	private Date dateEdit;

	@Column(name = "user_edit")
	private String userEdit;

}
