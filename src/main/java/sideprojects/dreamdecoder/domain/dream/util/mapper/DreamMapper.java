package sideprojects.dreamdecoder.domain.dream.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sideprojects.dreamdecoder.domain.dream.model.DreamModel;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamEntity;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamEmotion;

@Mapper(componentModel = "spring", uses = { DreamSymbolMapper.class })
public interface DreamMapper {

    @Mapping(target = "dreamSymbols", source = "dreamTypes")
    @Mapping(target = "dreamEmotion", source = "dreamEmotion")
    @Mapping(target = "tags", source = "tags")
    DreamEntity toEntity(DreamModel dreamModel);

    @Mapping(target = "dreamTypes", source = "dreamSymbols")
    @Mapping(target = "dreamEmotion", source = "dreamEmotion")
    @Mapping(target = "tags", source = "tags")
    DreamModel toModel(DreamEntity dreamEntity);

}
