package com.monitoreo.facturacion.application.ports.output;

import com.monitoreo.facturacion.domain.model.NodoFacturacion;

import java.util.List;
import java.util.Optional;

public interface RepositorioSaludPais {
    void guardar(NodoFacturacion nodo);
    Optional<NodoFacturacion> buscarUltimoPorPais(String pais);
    List<NodoFacturacion> buscarTodos();
}
