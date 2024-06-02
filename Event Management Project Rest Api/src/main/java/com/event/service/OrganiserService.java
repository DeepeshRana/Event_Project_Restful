package com.event.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.event.dto.CustomPage;
import com.event.dto.OrganiserDto;

public interface OrganiserService {
	OrganiserDto createOrganiser(OrganiserDto organiserDto);

	OrganiserDto updateOrganiser(OrganiserDto organiserDto, Integer orgId);

	// List<OrganiserDto> getAllOrganisers();

	OrganiserDto getOrganiserById(Integer orgId);

	void deleteOrganiser(Integer orgId);

	CustomPage<OrganiserDto> getAllOrganisers(int page, int size);
}
