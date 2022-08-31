package dev.soulcatcher.dtos;

import dev.soulcatcher.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Principal {
    private String authUserId;
    private String username;

    public Principal(User user) {
        this.authUserId = user.getUserId();
        this.username = user.getUsername();
    }
    public Principal(String authUserId, String authUsername) {
        this.authUserId = authUserId;
        this.username = authUsername;
    }
}
