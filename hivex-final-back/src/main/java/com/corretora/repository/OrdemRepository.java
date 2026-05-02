package com.corretora.repository;

import com.corretora.model.Ordem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdemRepository extends JpaRepository<Ordem, Long> {
    List<Ordem> findByClienteId(Long clienteId);
    List<Ordem> findByAtivoId(Long ativoId);
}
