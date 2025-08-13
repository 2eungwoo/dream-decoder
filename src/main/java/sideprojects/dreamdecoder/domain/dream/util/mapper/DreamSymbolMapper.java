package sideprojects.dreamdecoder.domain.dream.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sideprojects.dreamdecoder.domain.dream.persistence.DreamSymbol;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;

@Mapper(componentModel = "spring")
public interface DreamSymbolMapper {

    // DreamType -> DreamSymbol
    @Mapping(target = "type",        expression = "java(dreamType.name())")
    @Mapping(target = "description", expression = "java(dreamType.getDescription())")
    @Mapping(target = "category",    expression = "java(dreamType.getCategory().getDescription())")
    @Mapping(target = "outcome",     expression = "java(dreamType.getOutcome().getDescription())")
    DreamSymbol toSymbol(DreamType dreamType);

    // DreamSymbol -> DreamType (문자열->enum)
//    @Mapping(target = ".", expression = "java(symbol == null || symbol.getType() == null ? null : DreamType.valueOf(symbol.getType()))")
//    DreamType toType(DreamSymbol symbol);
    default DreamType toType(DreamSymbol symbol) {
        return (symbol == null || symbol.getType() == null)
            ? null
            : DreamType.valueOf(symbol.getType());
    }


}