package wagi_app.wagi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wagi_app.wagi.entity.AdmittedUsers;

@Repository
public interface AdmittedUsersRepository extends JpaRepository<AdmittedUsers, String> {
    boolean existsByAdmittedId(String admittedId);
    void save(String admittedId);

}
