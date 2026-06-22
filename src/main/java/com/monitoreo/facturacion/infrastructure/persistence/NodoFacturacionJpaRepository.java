package com.monitoreo.facturacion.infrastructure.persistence;

import com.monitoreo.facturacion.application.dtos.ResumenRegionalDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional; // ◄ Asegúrate de tener este import

public interface NodoFacturacionJpaRepository extends JpaRepository<NodoFacturacionEntidad, Long> {

    // ✔ SOLUCIÓN AL ERROR 1: Declarar el query method para el último reporte
    Optional<NodoFacturacionEntidad> findTopByPaisOrderByTimestampDesc(String pais);

    // Consulta del GROUP BY analizada en pasos anteriores
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
}
