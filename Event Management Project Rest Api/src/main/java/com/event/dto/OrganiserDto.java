package com.event.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrganiserDto {

	private long orgId;

	@NotEmpty
	@Size(min = 3, max = 20, message = "Name should be min 3 characters and max 20 characters")
	private String orgName;

	@NotEmpty
	@Email(message = "Please provide a valid email address")
	private String email;

}
