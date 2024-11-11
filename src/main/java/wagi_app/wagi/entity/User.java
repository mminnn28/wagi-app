package wagi_app.wagi.entity;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id;

    @NotNull
    private String username;

    @NotNull
    @Column(unique = true)
    private int studentId;

    private String major;

    @NotNull
    private String password;

    private boolean hasRole;
}
