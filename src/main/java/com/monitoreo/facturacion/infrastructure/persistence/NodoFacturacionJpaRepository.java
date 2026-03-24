package com.monitoreo.facturacion.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NodoFacturacionJpaRepository
        extends JpaRepository<NodoFacturacionEntidad, Long> {

    Optional<NodoFacturacionEntidad> findTopByPaisOrderByTimestampDesc(String pais);
}
