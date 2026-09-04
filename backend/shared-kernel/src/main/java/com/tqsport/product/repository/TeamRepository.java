package com.tqsport.product.repository;

import com.tqsport.product.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findBySlug(String slug);
}
