package com.bns.docker.clabdockerdemo.repositories;

import com.bns.docker.clabdockerdemo.entities.Transit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransitRepository extends JpaRepository<Transit, Long> {
}
