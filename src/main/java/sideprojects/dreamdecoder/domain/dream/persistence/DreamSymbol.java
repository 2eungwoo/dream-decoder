package sideprojects.dreamdecoder.domain.dream.persistence;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DreamSymbol {
    private String type;
    private String description;
    private String category;
    private String outcome;

    public static DreamSymbol of(String type, String description, String category, String outcome) {
        DreamSymbol symbol = new DreamSymbol();
        symbol.type = type;
        symbol.description = description;
        symbol.category = category;
        symbol.outcome = outcome;
        return symbol;
    }
}