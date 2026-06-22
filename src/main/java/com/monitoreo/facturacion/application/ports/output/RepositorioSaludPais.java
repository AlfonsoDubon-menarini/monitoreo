package com.monitoreo.facturacion.application.ports.output; // ◄ ¡ESTA LÍNEA DICE PORTS.OUTPUT!

import com.monitoreo.facturacion.application.dtos.ResumenRegionalDTO;
import com.monitoreo.facturacion.domain.model.NodoFacturacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RepositorioSaludPais { // ◄ REVISA QUE DIGA "interface" Y NO "class"

    void guardar(NodoFacturacion nodo);

    Optional<NodoFacturacion> buscarUltimoPorPais(String pais);

    List<NodoFacturacion> buscarTodos();

    // Dentro de com.monitoreo.facturacion.application.ports.output.RepositorioSaludPais
    List<ResumenRegionalDTO> obtenerResumenRegionalPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
