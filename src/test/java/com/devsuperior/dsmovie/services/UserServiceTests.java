package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.tests.UserDetailsFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserServiceTests {

	@InjectMocks
	private UserService service;
	@Mock
	private UserRepository userRepository;
	@Mock
	private CustomUserUtil customUserUtil;
	private UserEntity userEntity;
	private List<UserDetailsProjection> userDetails;
	private String existingUserName, nonExistingUserName;

	@BeforeEach
	void setUp() throws Exception {
		existingUserName = "maria@gmail.com";
		nonExistingUserName = "teste@gmail.com";

		userEntity = UserFactory.createUserEntity();
		userDetails = UserDetailsFactory.createCustomClientUser(existingUserName);

		when(userRepository.searchUserAndRolesByUsername(existingUserName)).thenReturn(userDetails);
		when(userRepository.searchUserAndRolesByUsername(nonExistingUserName)).thenReturn(new ArrayList<>());

		when(userRepository.findByName(existingUserName)).thenReturn(userEntity);
		when(userRepository.findByName(nonExistingUserName)).thenThrow(UsernameNotFoundException.class);

	}
	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {

		when(customUserUtil.getLoggedUsername()).thenReturn(existingUserName);

		UserEntity result = service.authenticated();

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), existingUserName);
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {

		doThrow(ClassCastException.class).when(customUserUtil).getLoggedUsername();

		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.authenticated();
		});
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {

		UserDetails result = service.loadUserByUsername(existingUserName);

		Assertions.assertEquals(result.getUsername(), existingUserName);
		Assertions.assertNotNull(result);
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {

		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.loadUserByUsername(nonExistingUserName);
		});
	}
}
