package sideprojects.dreamdecoder.domain.dream.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamEntity;

@Mapper(componentModel = "spring", uses = { DreamSymbolMapper.class })
public interface DreamMapper {

    @Mapping(target = "dreamSymbols", source = "dreamTypes")
    DreamEntity toEntity(DreamModel dreamModel);

    @Mapping(target = "dreamTypes", source = "dreamSymbols")
    DreamModel toModel(DreamEntity dreamEntity);

}
