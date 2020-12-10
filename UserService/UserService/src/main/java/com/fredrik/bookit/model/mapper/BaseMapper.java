package com.fredrik.bookit.model.mapper;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Named;

public interface BaseMapper<D, E> {

	// Entity to DTO
	D mapEntityToDTO(E entity, @Context CycleAvoidingMappingContext context);

	// This is the method I really use for the conversions
	@Named("originalFromDtoToEntity")
	default D mapEntityToDTO(final E entity) {
		return mapEntityToDTO(entity, new CycleAvoidingMappingContext());
	}

	// DTO to entity
	E mapDTOToEntity(final D dto, @Context CycleAvoidingMappingContext context);

	// This is the method I really use for the conversions
	@Named("originalFromDtoToEntity")
	default E mapDTOToEntity(final D dto) {
		return mapDTOToEntity(dto, new CycleAvoidingMappingContext());
	}


	// Lists
	List<D> mapEntitiesToDTOs(List<E> entities, @Context CycleAvoidingMappingContext context);

	// This is the method used for the conversions
	@Named("originalFromDtoToEntity")
	default List<D> mapEntitiesToDTOs(final List<E> entities) {
		return mapEntitiesToDTOs(entities, new CycleAvoidingMappingContext());
	}

	List<E> mapDTOsToEntities(List<D> dtos, @Context CycleAvoidingMappingContext context);

	// This is the method used for the conversions
	@Named("originalFromDtoToEntity")
	default List<E> mapDTOsToEntities(final List<D> dtos) {
		return mapDTOsToEntities(dtos, new CycleAvoidingMappingContext());
	}
	
}