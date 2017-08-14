package interactors;

import beans.builders.UserBuilder;
import entities.builders.UserEntityBuilder;
import exceptions.UseCaseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import persistence.UserRepository;

/**
 * Created by u624 on 4/3/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginUseCaseTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginUseCase loginUseCase = new LoginUseCase();

    @Test(expected = UseCaseException.class)
    public void GivenNullUserThenUseCaseExceptionSouldBeThrown() throws UseCaseException {
        loginUseCase.execute(null);
    }

    @Test(expected = UseCaseException.class)
    public void GivenUserWithNullUserNameThenUseCaseExceptionSouldBeThrown() throws UseCaseException {
        loginUseCase.execute(new UserBuilder().setUsername(null).setPasswordHashCode("123").build());
    }

    @Test(expected = UseCaseException.class)
    public void GivenUserWithNullPasswordHashCodeThenUseCaseExceptionSouldBeThrown() throws UseCaseException {
        loginUseCase.execute(new UserBuilder().setUsername("user").setPasswordHashCode(null).build());
    }

    @Test(expected = UseCaseException.class)
    public void GivenUserWithThatDoesNotExistThenUseCaseExceptionSouldBeThrown() throws UseCaseException {
        Mockito.doReturn(null).when(userRepository).findByUsername("user");
        loginUseCase.execute(new UserBuilder().setUsername("user").setPasswordHashCode("123").build());
    }

    @Test(expected = UseCaseException.class)
    public void GivenUserWithPasswordHashCodeNotEqualToRealPasswordHashCodeThenUseCaseExceptionSouldBeThrown() throws UseCaseException {
        Mockito.doReturn(new UserEntityBuilder().setPasswordHashCode("321").build()).when(userRepository).findByUsername("user");
        loginUseCase.execute(new UserBuilder().setUsername("user").setPasswordHashCode("123").build());
    }

    @Test
    public void GivenValidUserWithPasswordHashCodeEqualToRealPasswordHashCodeThenNoExceptionSouldBeThrown() throws UseCaseException {
        Mockito.doReturn(new UserEntityBuilder().setPasswordHashCode("123").build()).when(userRepository).findByUsername("user");
        loginUseCase.execute(new UserBuilder().setUsername("user").setPasswordHashCode("123").build());
    }
}
