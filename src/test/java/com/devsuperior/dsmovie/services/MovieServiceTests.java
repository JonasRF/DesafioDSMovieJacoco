package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;
	@Mock
	private MovieRepository movieRepository;
	private MovieEntity movieEntity;
	private MovieDTO movieDTO;
	private PageImpl<MovieEntity> page;
	private long existingId, nonExistingId, dependentyId;

	private String title;

	@BeforeEach
	void setUp() {
		existingId = 1L;
		nonExistingId = 100L;
		dependentyId = 3L;
		title = "Test Movie";

		movieEntity = MovieFactory.createMovieEntity();
		movieDTO = new MovieDTO(movieEntity);
		page = new PageImpl<>(List.of(movieEntity));

		when(movieRepository.searchByTitle(any(), (Pageable) any())).thenReturn(page);

		when(movieRepository.findById(existingId)).thenReturn(Optional.ofNullable(movieEntity));
		when(movieRepository.findById(nonExistingId)).thenReturn(Optional.empty());

		when(movieRepository.getReferenceById(existingId)).thenReturn(movieEntity);
		when(movieRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

		doNothing().when(movieRepository).deleteById(existingId);
		doThrow(EmptyResultDataAccessException.class).when(movieRepository).deleteById(nonExistingId);
		doThrow(DataIntegrityViolationException.class).when(movieRepository).deleteById(dependentyId);

		when(movieRepository.save(any())).thenReturn(movieEntity);
	}

	@Test
	public void findAllShouldReturnPagedMovieDTO() {

		Pageable pageable = PageRequest.of(0, 12);

		Page<MovieDTO> result = service.findAll(title,pageable);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getSize(), 1);
		Assertions.assertEquals(result.iterator().next().getTitle(), title);
	}
	
	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {

		MovieDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingId);
		Assertions.assertEquals(result.getTitle(), movieEntity.getTitle());
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}
	
	@Test
	public void insertShouldReturnMovieDTO() {

		MovieDTO result = service.insert(movieDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), movieEntity.getId());
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {

		MovieDTO result = service.update(existingId, movieDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingId);
		Assertions.assertEquals(result.getTitle(), movieDTO.getTitle());
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, movieDTO);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});

	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {

		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentyId);
		});
	}
}
