package com.monitoreo.facturacion.infrastructure.adapters.rest;

import com.monitoreo.facturacion.application.dtos.ReporteSaludDTO;
import com.monitoreo.facturacion.application.dtos.ResumenRegionalDTO;
import com.monitoreo.facturacion.application.ports.input.ProcesarReporteSaludUseCase;
import com.monitoreo.facturacion.application.ports.output.RepositorioSaludPais;
import com.monitoreo.facturacion.domain.model.EstadoSalud;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat; // ◄ NUEVO IMPORT
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime; // ◄ NUEVO IMPORT
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
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
            @Valid @RequestBody ReporteSaludDTO dto) {

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

    // ──► MÉTODO MODIFICADO CON SOPORTE PARA PARÁMETROS DE FECHA OPTIONALES
    @GetMapping("/estado-regional/resumen")
    public ResponseEntity<List<ResumenRegionalDTO>> obtenerResumenDashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        // Pasamos los filtros al puerto de salida (salida agnóstica de la arquitectura hexagonal)
        List<ResumenRegionalDTO> resumen = repositorio.obtenerResumenRegionalPorFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(resumen);
    }
}
