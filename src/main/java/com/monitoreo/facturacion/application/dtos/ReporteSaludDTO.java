package com.monitoreo.facturacion.application.dtos;

import jakarta.validation.constraints.*;

public class ReporteSaludDTO {

    @NotBlank(message = "El país es obligatorio")
    @Pattern(regexp = "^(GT|SV|CR|PA|DO)$",
            message = "País inválido. Valores permitidos: GT, SV, CR, PA, DO")
    private String pais;

    private boolean enteTributarioActivo;

    @Min(value = 0, message = "La latencia no puede ser negativa")
    @Max(value = 60000, message = "La latencia no puede superar 60,000ms")
    private int latenciaMs;

    @Min(value = 0, message = "Los documentos pendientes no pueden ser negativos")
    @Max(value = 100000, message = "Los documentos pendientes no pueden superar 100,000")
    private int documentosPendientes;

    public ReporteSaludDTO() {}

    public ReporteSaludDTO(String pais,
                           boolean enteTributarioActivo,
                           int latenciaMs,
                           int documentosPendientes) {
        this.pais                 = pais;
        this.enteTributarioActivo = enteTributarioActivo;
        this.latenciaMs           = latenciaMs;
        this.documentosPendientes = documentosPendientes;
    }

    public String getPais()                    { return pais; }
    public boolean isEnteTributarioActivo()    { return enteTributarioActivo; }
    public int getLatenciaMs()                 { return latenciaMs; }
    public int getDocumentosPendientes()       { return documentosPendientes; }

    public void setPais(String pais)                                  { this.pais = pais; }
    public void setEnteTributarioActivo(boolean enteTributarioActivo) { this.enteTributarioActivo = enteTributarioActivo; }
    public void setLatenciaMs(int latenciaMs)                         { this.latenciaMs = latenciaMs; }
    public void setDocumentosPendientes(int documentosPendientes)     { this.documentosPendientes = documentosPendientes; }
}
