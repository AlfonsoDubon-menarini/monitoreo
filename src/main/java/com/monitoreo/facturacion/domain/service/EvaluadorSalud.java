package com.monitoreo.facturacion.domain.service;

import com.monitoreo.facturacion.domain.model.EstadoSalud;
import com.monitoreo.facturacion.domain.model.NodoFacturacion;

public class EvaluadorSalud {

    // Umbrales pendientes de validación con el negocio
    // Ajustar sin necesidad de cambiar la lógica de evaluación
    public static final int LATENCIA_WARN_MS      = 500;
    public static final int LATENCIA_CRITICAL_MS  = 1000;

    public static final int DOCS_WARN_UMBRAL      = 50;
    public static final int DOCS_CRITICAL_UMBRAL  = 200;

    public EstadoSalud evaluar(NodoFacturacion nodo) {

        if (esCritical(nodo)) return EstadoSalud.CRITICAL;
        if (esWarn(nodo))     return EstadoSalud.WARN;

        return EstadoSalud.OK;
    }

    private boolean esCritical(NodoFacturacion nodo) {
        return !nodo.isEnteTributarioActivo()
                || nodo.getLatenciaMs()           >= LATENCIA_CRITICAL_MS
                || nodo.getDocumentosPendientes() >= DOCS_CRITICAL_UMBRAL;
    }

    private boolean esWarn(NodoFacturacion nodo) {
        return nodo.getLatenciaMs()           >= LATENCIA_WARN_MS
                || nodo.getDocumentosPendientes() >= DOCS_WARN_UMBRAL;
    }
}
