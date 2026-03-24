package com.monitoreo.facturacion.application.dtos;

public class ReporteSaludDTO {

    private String pais;
    private boolean enteTributarioActivo;
    private int latenciaMs;
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

    public String getPais()                 { return pais; }
    public boolean isEnteTributarioActivo() { return enteTributarioActivo; }
    public int getLatenciaMs()              { return latenciaMs; }
    public int getDocumentosPendientes()    { return documentosPendientes; }

    public void setPais(String pais)                               { this.pais = pais; }
    public void setEnteTributarioActivo(boolean enteTributarioActivo) { this.enteTributarioActivo = enteTributarioActivo; }
    public void setLatenciaMs(int latenciaMs)                      { this.latenciaMs = latenciaMs; }
    public void setDocumentosPendientes(int documentosPendientes)  { this.documentosPendientes = documentosPendientes; }
}
