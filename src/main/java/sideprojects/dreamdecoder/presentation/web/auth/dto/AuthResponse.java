package sideprojects.dreamdecoder.presentation.web.auth.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
    String token,
    String username,
    String email
) {
}
