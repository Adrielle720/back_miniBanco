package com.corretora.repository;

import com.corretora.model.CotacaoMercado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CotacaoMercadoRepository extends JpaRepository<CotacaoMercado, Long> {
    List<CotacaoMercado> findByTickerOrderByConsultadoEmDesc(String ticker);
    Optional<CotacaoMercado> findFirstByTickerOrderByConsultadoEmDesc(String ticker);
}
