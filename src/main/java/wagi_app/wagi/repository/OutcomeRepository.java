package wagi_app.wagi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wagi_app.wagi.entity.Outcome;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutcomeRepository extends JpaRepository<Outcome, Long> {
    Optional<Outcome> findById(Long id);
    List<Outcome> findByCategory(String category);
    List<Outcome> findByCategoryOrderByCreatedAtDesc(String category);
}
