package com.monitoreo.facturacion.application.services;

import com.monitoreo.facturacion.application.dtos.ReporteSaludDTO;
import com.monitoreo.facturacion.application.ports.input.ProcesarReporteSaludUseCase;
import com.monitoreo.facturacion.application.ports.output.RepositorioSaludPais;
import com.monitoreo.facturacion.domain.model.EstadoSalud;
import com.monitoreo.facturacion.domain.model.NodoFacturacion;
import com.monitoreo.facturacion.domain.service.EvaluadorSalud;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ServicioProcesarReporte implements ProcesarReporteSaludUseCase {

    private final EvaluadorSalud evaluadorSalud;
    private final RepositorioSaludPais repositorio;

    public ServicioProcesarReporte(EvaluadorSalud evaluadorSalud,
                                   RepositorioSaludPais repositorio) {
        this.evaluadorSalud = evaluadorSalud;
        this.repositorio    = repositorio;
    }

    @Override
    public EstadoSalud procesar(ReporteSaludDTO dto) {
        NodoFacturacion nodo = new NodoFacturacion(
                dto.getPais(),
                dto.isEnteTributarioActivo(),
                dto.getLatenciaMs(),
                dto.getDocumentosPendientes(),
                Instant.now()
        );

        EstadoSalud estado = evaluadorSalud.evaluar(nodo);
        repositorio.guardar(nodo);
        return estado;
    }
}
