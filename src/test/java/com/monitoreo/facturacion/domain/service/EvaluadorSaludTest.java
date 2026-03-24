package com.monitoreo.facturacion.domain.service;

import com.monitoreo.facturacion.domain.model.EstadoSalud;
import com.monitoreo.facturacion.domain.model.NodoFacturacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EvaluadorSaludTest {

    private EvaluadorSalud evaluador;

    @BeforeEach
    void setUp() {
        evaluador = new EvaluadorSalud();
    }

    @Test
    void debeRetornarOK_cuandoTodoEstaOperativo() {
        NodoFacturacion nodo = new NodoFacturacion(
                "GT", true, 100, 5, Instant.now()
        );
        assertEquals(EstadoSalud.OK, evaluador.evaluar(nodo));
    }

    @Test
    void debeRetornarWARN_cuandoLatenciaEsAlta() {
        NodoFacturacion nodo = new NodoFacturacion(
                "SV", true, 600, 5, Instant.now()
        );
        assertEquals(EstadoSalud.WARN, evaluador.evaluar(nodo));
    }

    @Test
    void debeRetornarWARN_cuandoDocumentosPendientesSupearnUmbral() {
        NodoFacturacion nodo = new NodoFacturacion(
                "CR", true, 100, 80, Instant.now()
        );
        assertEquals(EstadoSalud.WARN, evaluador.evaluar(nodo));
    }

    @Test
    void debeRetornarCRITICAL_cuandoEnteTributarioCaido() {
        NodoFacturacion nodo = new NodoFacturacion(
                "PA", false, 100, 5, Instant.now()
        );
        assertEquals(EstadoSalud.CRITICAL, evaluador.evaluar(nodo));
    }

    @Test
    void debeRetornarCRITICAL_cuandoLatenciaSupeaUmbralCritico() {
        NodoFacturacion nodo = new NodoFacturacion(
                "DO", true, 1500, 5, Instant.now()
        );
        assertEquals(EstadoSalud.CRITICAL, evaluador.evaluar(nodo));
    }

    @Test
    void debeRetornarCRITICAL_cuandoDocumentosPendientesSuperanUmbralCritico() {
        NodoFacturacion nodo = new NodoFacturacion(
                "GT", true, 100, 250, Instant.now()
        );
        assertEquals(EstadoSalud.CRITICAL, evaluador.evaluar(nodo));
    }
}
