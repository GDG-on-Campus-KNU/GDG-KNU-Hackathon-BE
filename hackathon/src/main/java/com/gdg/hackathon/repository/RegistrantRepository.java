package com.gdg.hackathon.repository;

import com.gdg.hackathon.domain.Registrant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrantRepository extends JpaRepository<Registrant, Long> {
    boolean existsByStudentId(Long studentId);
}
