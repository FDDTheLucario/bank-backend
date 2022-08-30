package dev.soulcatcher.dtos;

import dev.soulcatcher.models.User;
import lombok.Data;

@Data
public class NewAccountRequest {
    private String nickname;
    private User user;
}
