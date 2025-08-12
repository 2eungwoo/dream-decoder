package sideprojects.dreamdecoder.domain.dream.util.mapper;

import org.mapstruct.Mapper;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamEntity;

@Mapper(componentModel = "spring")
public interface DreamMapper {

    DreamEntity toEntity(DreamModel dreamModel);

    DreamModel toModel(DreamEntity dreamEntity);
}
