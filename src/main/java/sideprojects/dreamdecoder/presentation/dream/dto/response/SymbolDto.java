package sideprojects.dreamdecoder.presentation.dream.dto.response;

import sideprojects.dreamdecoder.domain.dream.persistence.DreamSymbol;
import sideprojects.dreamdecoder.domain.dream.util.enums.DreamType;

public record SymbolDto(
        String type,
        String description,
        String category,
        String outcome
) {
    public static SymbolDto of(DreamType type) {
        return new SymbolDto(
                type.name(),
                type.getDescription(),
                type.getCategory().getDescription(),
                type.getOutcome().getDescription()
        );
    }

    public static SymbolDto of(DreamSymbol dreamSymbol) {
        return new SymbolDto(
                dreamSymbol.getType(),
                dreamSymbol.getDescription(),
                dreamSymbol.getCategory(),
                dreamSymbol.getOutcome()
        );
    }
}
