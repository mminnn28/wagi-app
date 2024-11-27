package wagi_app.wagi.entity;


import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "user_table")
@Builder
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long user_id;

    @Column(name = "username")
    private String username;
}