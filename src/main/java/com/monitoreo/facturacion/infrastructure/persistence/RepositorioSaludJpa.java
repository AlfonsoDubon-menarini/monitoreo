package com.monitoreo.facturacion.infrastructure.persistence;

import com.monitoreo.facturacion.application.dtos.ResumenRegionalDTO;
import com.monitoreo.facturacion.application.ports.output.RepositorioSaludPais;
import com.monitoreo.facturacion.domain.model.NodoFacturacion;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class RepositorioSaludJpa implements RepositorioSaludPais {

    private final NodoFacturacionJpaRepository jpaRepository;

    public RepositorioSaludJpa(NodoFacturacionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void guardar(NodoFacturacion nodo) {
        NodoFacturacionEntidad entidad = new NodoFacturacionEntidad(
                nodo.getPais(),
                nodo.isEnteTributarioActivo(),
                nodo.getLatenciaMs(),
                nodo.getDocumentosPendientes(),
                nodo.getTimestamp()
        );
        jpaRepository.save(entidad);
    }

    @Override
    public Optional<NodoFacturacion> buscarUltimoPorPais(String pais) {
        return jpaRepository
                .findTopByPaisOrderByTimestampDesc(pais)
                .map(this::toDomain);
    }

    @Override
    public List<NodoFacturacion> buscarTodos() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    // ✔️ EL MÉTODO VIEJO RECLAMADO SE ELIMINÓ DE AQUÍ PORQUE YA NO ESTÁ EN LA INTERFAZ

    @Override
    public List<ResumenRegionalDTO> obtenerResumenRegionalPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return jpaRepository.obtenerResumenRegionalPorFechas(fechaInicio, fechaFin);
    }

    private NodoFacturacion toDomain(NodoFacturacionEntidad e) {
        return new NodoFacturacion(
                e.getPais(),
                e.isEnteTributarioActivo(),
                e.getLatenciaMs(),
                e.getDocumentosPendientes(),
                e.getTimestamp()
        );
    }
}
