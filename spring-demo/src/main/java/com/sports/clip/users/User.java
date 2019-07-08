package com.sports.clip.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    public enum Role {USER, ADMIN}

    @Id
    private String id;
    @NotEmpty
    @Email
    @Indexed(unique = true)
    private String email;
    @JsonIgnore
    @ToString.Exclude
    private String password;
    @NotNull
    private Role role;
}
