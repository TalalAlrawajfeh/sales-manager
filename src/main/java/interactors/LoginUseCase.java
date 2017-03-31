package interactors;

import adapters.UseCase;
import beans.User;
import entities.UserEntity;
import exceptions.UseCaseException;
import org.springframework.beans.factory.annotation.Autowired;
import persistence.UserRepository;

import java.util.Objects;

/**
 * Created by u624 on 3/24/17.
 */
public class LoginUseCase implements UseCase<User> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(User user) throws UseCaseException {
        UserEntity userEntity = userRepository.findByUsername(user.getUsername());
        validateUser(user, userEntity);
    }

    private void validateUser(User user, UserEntity userEntity) throws UseCaseException {
        if (Objects.isNull(userEntity) || !userEntity.getPasswordHashCode().equals(user.getPasswordHashCode())) {
            throw new UseCaseException("Invalid username or password");
        }
    }
}
