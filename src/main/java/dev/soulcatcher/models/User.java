package dev.soulcatcher.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "`users`")
public class User {
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String password;
    @OneToMany(mappedBy = "accountId")
    private List<Account> accounts;
    @Column(name = "first_name", unique = false)
    private String firstName;
    @Column(name = "last_name", unique = false)
    private String lastName;
}
