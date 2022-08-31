package dev.soulcatcher.dtos;

import dev.soulcatcher.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewAccountRequest {
    private String nickname;
    private User user;
}
