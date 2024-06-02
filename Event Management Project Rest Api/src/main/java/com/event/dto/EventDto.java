package com.event.dto;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EventDto {

	private Integer eventId;

	@NotEmpty
	@Size(min = 3, max = 20, message = "Title should be min 3 characters and max 20 character")
	private String title;

	@NotEmpty
	@Size(min = 3, max = 2000, message = "Description should be min 3 characters and max 20 characters")
	private String content;

	@Future(message = "Please enter future date not past date")
	@NotNull(message = "Please Enter Date & Time")
	private LocalDateTime localDateTime;
	
	@NotBlank(message = "Enter the maxAttendes in event")
	private int maxAttendes;

	private OrganiserDto organiser;
}
