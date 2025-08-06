package sideprojects.dreamdecoder.domain.auth.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sideprojects.dreamdecoder.domain.auth.model.UserModel;
import sideprojects.dreamdecoder.domain.auth.persistence.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    User toEntity(UserModel model);
}