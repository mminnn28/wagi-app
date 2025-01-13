package wagi_app.wagi.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class AdmittedUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String admittedId;

    public AdmittedUsers(String studentId) {
        this.admittedId = studentId;
    }

}
