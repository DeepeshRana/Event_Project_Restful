package com.event.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eventId;

	@Column(name = "event_title", length = 100, nullable = false)
	private String title;

	@Column(length = 10000)
	private String content;

	@NotNull
	private LocalDateTime localDateTime;

	@ManyToOne
	private Organiser organiser;

	@NotNull(message = "Enter the maxAttendees in event")
    @Positive(message = "maxAttendees must be a positive value")
    private int maxAttendees;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
	private Set<Registration> registrations = new HashSet<>();
}
