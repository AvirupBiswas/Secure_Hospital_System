package com.asu.project.hospital.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "user")
public class User {
	@Id
	@Column(name = "userId", nullable = false)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String userId;

	@Column(name = "firstName")
	@NotNull(message = "Cannot be null")
	@NotEmpty(message = "Required*")
	@Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
	private String firstName;

	@Column(name = "lastName")
	@NotNull(message = "Required*")
	@NotEmpty(message = "Required*")
	@Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
	private String lastName;

	@Column(name = "email")
	@NotNull(message = "Required*")
	@NotEmpty(message = "Required*")
	@Email
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email address is invalid")
	private String email;

	@Column(name = "roles")
	@NotNull(message = "Required*")
	@NotEmpty(message = "Please select a role")
	private String role;

	@Column(name = "password")
	@NotNull(message = "Required*")
	@NotEmpty(message = "Required*")
	@Size(min = 6, max = 60, message = "Password must be at least 6 characters long")
	private String password;

	public boolean isActive() {
		return active;
	}

	private boolean active;

	public User(@JsonProperty("userId") String userId, @JsonProperty("firstName") String firstName,
			@JsonProperty("lastName") String lastName, @JsonProperty("email") String email,
			@JsonProperty("role") String role, @JsonProperty("password") String password,
			@JsonProperty("active") boolean active) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
		this.password = password;
		this.active = true;

	}

	public User() {

	}

	public String getUserId() {
		return userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
	}

	public String getUserName() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public boolean getActive() {
		return active;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
