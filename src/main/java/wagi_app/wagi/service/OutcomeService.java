package wagi_app.wagi.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wagi_app.wagi.entity.Outcome;
import wagi_app.wagi.repository.OutcomeRepository;

@Service
@Transactional
public class OutcomeService {
    private final OutcomeRepository outcomeRepository;

    @Autowired
    public OutcomeService(OutcomeRepository outcomeRepository) {
        this.outcomeRepository = outcomeRepository;
    }

    public Outcome createOutcome(Outcome outcome) {
        return outcomeRepository.save(outcome);
    }
}
