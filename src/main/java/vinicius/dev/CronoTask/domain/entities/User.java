package vinicius.dev.CronoTask.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.UUID;

@Data
@AllArgsConstructor
public class User
{
    private final UUID id;
    private String name;
    private String email;
    private String password;

    public static User create( String name, String email, String password )
    {
        return new User(
                UUID.randomUUID( ),
                name,
                email,
                password
        );
    }
}
