package com.monitoreo.facturacion.application.ports.input;

import com.monitoreo.facturacion.application.dtos.ReporteSaludDTO;
import com.monitoreo.facturacion.domain.model.EstadoSalud;

public interface ProcesarReporteSaludUseCase {
    EstadoSalud procesar(ReporteSaludDTO reporte);
}
