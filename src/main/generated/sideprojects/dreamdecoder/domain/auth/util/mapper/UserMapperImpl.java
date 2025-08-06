package sideprojects.dreamdecoder.domain.auth.util.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import sideprojects.dreamdecoder.domain.auth.model.UserModel;
import sideprojects.dreamdecoder.domain.auth.persistence.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-06T21:46:18+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserModel model) {
        if ( model == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( model.getUsername() );
        user.email( model.getEmail() );
        user.password( model.getPassword() );

        user.createdAt( java.time.LocalDateTime.now() );

        return user.build();
    }
}
