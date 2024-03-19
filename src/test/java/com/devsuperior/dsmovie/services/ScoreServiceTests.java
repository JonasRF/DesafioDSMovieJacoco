package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserDetailsFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {

	@InjectMocks
	private ScoreService service;
	@Mock
	private UserService userService;
	@Mock
	private ScoreRepository scoreRepository;
	@Mock
	private MovieRepository movieRepository;
	private Long existingId, nonExisitingId;
	private MovieEntity movieEntity;
	private ScoreEntity scoreEntity;
	private ScoreDTO scoreDTO;
	private UserEntity client;
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExisitingId = 2L;

		scoreDTO = ScoreFactory.createScoreDTO();
		client = UserFactory.createUserEntity();
		movieEntity = MovieFactory.createMovieEntity();
		scoreEntity = ScoreFactory.createScoreEntity();

		when(userService.authenticated()).thenReturn(client);
		when(movieRepository.findById(existingId)).thenReturn(Optional.ofNullable(movieEntity));
		when(scoreRepository.saveAndFlush(any())).thenReturn(scoreEntity);
		when(movieRepository.save(any())).thenReturn(movieEntity);
		when(movieRepository.findById(nonExisitingId)).thenReturn(Optional.empty());
	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {

		MovieDTO result = service.saveScore(scoreDTO);

		Assertions.assertNotNull(result);
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {

		MovieEntity movie = MovieFactory.createMovieEntity();
		movie.setId(nonExisitingId);
		UserEntity user = UserFactory.createUserEntity();
		ScoreEntity score = new ScoreEntity();

		score.setMovie(movie);
		score.setUser(user);
		score.setValue(4.5);
		movie.getScores().add(score);

		scoreDTO = new ScoreDTO(score);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			MovieDTO result = service.saveScore(scoreDTO);
		});
	}
}
