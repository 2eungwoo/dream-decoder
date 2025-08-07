package sideprojects.dreamdecoder.domain.dream.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sideprojects.dreamdecoder.domain.dream.model.Dream;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamEntity;

@Mapper(componentModel = "spring")
public interface DreamMapper {

    DreamEntity toEntity(Dream dream);

    Dream toModel(DreamEntity dreamEntity);
}
