package com.monitoreo.facturacion.infrastructure.persistence;

import com.monitoreo.facturacion.application.dtos.ResumenRegionalDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // ◄ Importado correctamente
import java.time.LocalDateTime; // ◄ Importado correctamente
import java.util.List;
import java.util.Optional;

public interface NodoFacturacionJpaRepository extends JpaRepository<NodoFacturacionEntidad, Long> {

    Optional<NodoFacturacionEntidad> findTopByPaisOrderByTimestampDesc(String pais);

    @Query("""
        SELECT new com.monitoreo.facturacion.application.dtos.ResumenRegionalDTO(
            n.pais, 
            COUNT(n), 
            AVG(n.latenciaMs), 
            SUM(n.documentosPendientes)
        ) 
        FROM NodoFacturacionEntidad n 
        GROUP BY n.pais
    """)
    List<ResumenRegionalDTO> obtenerResumenPorPais();

    // ──► LA NUEVA QUERY HISTÓRICA CON RANGOS REALES:
    @Query("""
        SELECT new com.monitoreo.facturacion.application.dtos.ResumenRegionalDTO(
            n.pais, 
            COUNT(n), 
            AVG(n.latenciaMs), 
            SUM(n.documentosPendientes)
        ) 
        FROM NodoFacturacionEntidad n 
        WHERE (:fechaInicio IS NULL OR n.timestamp >= :fechaInicio) 
          AND (:fechaFin IS NULL OR n.timestamp <= :fechaFin) 
        GROUP BY n.pais
    """)
    List<ResumenRegionalDTO> obtenerResumenRegionalPorFechas(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
}
