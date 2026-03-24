package com.monitoreo.facturacion.infrastructure.adapters.rest;

import com.monitoreo.facturacion.application.dtos.ReporteSaludDTO;
import com.monitoreo.facturacion.application.ports.input.ProcesarReporteSaludUseCase;
import com.monitoreo.facturacion.application.ports.output.RepositorioSaludPais;
import com.monitoreo.facturacion.domain.model.EstadoSalud;
import com.monitoreo.facturacion.domain.model.NodoFacturacion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MonitoreoController {

    private final ProcesarReporteSaludUseCase procesarReporte;
    private final RepositorioSaludPais repositorio;

    public MonitoreoController(ProcesarReporteSaludUseCase procesarReporte,
                               RepositorioSaludPais repositorio) {
        this.procesarReporte = procesarReporte;
        this.repositorio     = repositorio;
    }

    @PostMapping("/reportes-salud")
    public ResponseEntity<Map<String, Object>> recibirReporte(
            @RequestBody ReporteSaludDTO dto) {

        EstadoSalud estado = procesarReporte.procesar(dto);

        return ResponseEntity.accepted().body(Map.of(
                "pais",              dto.getPais(),
                "estado_calculado",  estado.name(),
                "mensaje",           "Reporte procesado correctamente"
        ));
    }

    @GetMapping("/estado-regional")
    public ResponseEntity<List<Map<String, Object>>> obtenerEstadoRegional() {

        List<Map<String, Object>> respuesta = repositorio.buscarTodos()
                .stream()
                .map(nodo -> Map.<String, Object>of(
                        "pais",                nodo.getPais(),
                        "ente_tributario",     nodo.isEnteTributarioActivo(),
                        "latencia_ms",         nodo.getLatenciaMs(),
                        "documentos_pendientes", nodo.getDocumentosPendientes(),
                        "ultima_actualizacion",  nodo.getTimestamp().toString()
                ))
                .toList();

        return ResponseEntity.ok(respuesta);
    }
}
