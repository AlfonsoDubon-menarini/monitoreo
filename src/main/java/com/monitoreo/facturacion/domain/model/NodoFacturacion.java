package com.monitoreo.facturacion.domain.model;

import java.time.Instant;

public class NodoFacturacion {

    private final String pais;
    private final boolean enteTributarioActivo;
    private final int latenciaMs;
    private final int documentosPendientes;
    private final Instant timestamp;

    public NodoFacturacion(String pais,
                           boolean enteTributarioActivo,
                           int latenciaMs,
                           int documentosPendientes,
                           Instant timestamp) {
        this.pais = pais;
        this.enteTributarioActivo = enteTributarioActivo;
        this.latenciaMs = latenciaMs;
        this.documentosPendientes = documentosPendientes;
        this.timestamp = timestamp;
    }

    public String getPais() { return pais; }
    public boolean isEnteTributarioActivo() { return enteTributarioActivo; }
    public int getLatenciaMs() { return latenciaMs; }
    public int getDocumentosPendientes() { return documentosPendientes; }
    public Instant getTimestamp() { return timestamp; }
}
