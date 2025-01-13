package wagi_app.wagi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wagi_app.wagi.entity.AdmittedUsers;
import wagi_app.wagi.repository.AdmittedUsersRepository;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdmittedUsersRepository admittedUsersRepository;

    @Transactional
    public void approvedUser(String studentId) throws Exception {
        if (admittedUsersRepository.existsByAdmittedId(studentId)) {
            throw new IllegalArgumentException("이미 등록되어있습니다.");
        }
        AdmittedUsers admittedUsers = new AdmittedUsers(studentId);
        admittedUsersRepository.save(admittedUsers);
    }
}
