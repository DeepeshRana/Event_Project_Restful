package com.event.dto;

import java.util.Date;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RegistrationDto {

	private Integer regId;

	@NotEmpty(message = "Please Enter your Name")
	private String name;

	private EventDto event;

	private StudentDto student;
}
