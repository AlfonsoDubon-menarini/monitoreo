package com.monitoreo.facturacion.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "nodo_facturacion")
public class NodoFacturacionEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2)
    private String pais;

    @Column(nullable = false)
    private boolean enteTributarioActivo;

    @Column(nullable = false)
    private int latenciaMs;

    @Column(nullable = false)
    private int documentosPendientes;

    @Column(nullable = false)
    private Instant timestamp;

    public NodoFacturacionEntidad() {}

    public NodoFacturacionEntidad(String pais,
                                  boolean enteTributarioActivo,
                                  int latenciaMs,
                                  int documentosPendientes,
                                  Instant timestamp) {
        this.pais                 = pais;
        this.enteTributarioActivo = enteTributarioActivo;
        this.latenciaMs           = latenciaMs;
        this.documentosPendientes = documentosPendientes;
        this.timestamp            = timestamp;
    }

    public Long getId()                        { return id; }
    public String getPais()                    { return pais; }
    public boolean isEnteTributarioActivo()    { return enteTributarioActivo; }
    public int getLatenciaMs()                 { return latenciaMs; }
    public int getDocumentosPendientes()       { return documentosPendientes; }
    public Instant getTimestamp()              { return timestamp; }
}
