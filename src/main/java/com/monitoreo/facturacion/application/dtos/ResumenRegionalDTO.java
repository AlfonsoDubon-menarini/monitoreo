package com.monitoreo.facturacion.application.dtos;

public record ResumenRegionalDTO(
        String pais,
        Long totalProcesado,
        Double latenciaPromedio,
        Long totalPendientes
) {}
