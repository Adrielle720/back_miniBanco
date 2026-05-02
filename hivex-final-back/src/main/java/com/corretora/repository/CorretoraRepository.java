package com.corretora.repository;

import com.corretora.model.Corretora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorretoraRepository extends JpaRepository<Corretora, Long> {}
