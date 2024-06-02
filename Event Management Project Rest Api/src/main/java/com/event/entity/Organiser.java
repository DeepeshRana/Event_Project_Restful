package com.event.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organisers")
@NoArgsConstructor
@Getter
@Setter
public class Organiser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long orgId;

	@Column(name = "first_name", nullable = false)
	private String orgName;

	@Column(name = "email")
	private String email;

	@OneToMany(mappedBy = "organiser", cascade = CascadeType.ALL)
	private List<Event> events = new ArrayList<>();
}
