package com.monitoreo.facturacion.infrastructure.adapters.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MonitoreoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ─── POST /api/v1/reportes-salud ─────────────────────────────────────────

    @Test
    void debeRetornar202_cuandoReporteEsValido() throws Exception {
        mockMvc.perform(post("/api/v1/reportes-salud")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "pais": "GT",
                          "ente_tributario_activo": true,
                          "latencia_ms": 320,
                          "documentos_pendientes": 5
                        }
                        """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.pais").value("GT"))
                .andExpect(jsonPath("$.estado_calculado").value("OK"))
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void debeRetornarWARN_cuandoLatenciaEsAlta() throws Exception {
        mockMvc.perform(post("/api/v1/reportes-salud")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "pais": "SV",
                          "ente_tributario_activo": true,
                          "latencia_ms": 750,
                          "documentos_pendientes": 5
                        }
                        """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.estado_calculado").value("WARN"));
    }

    @Test
    void debeRetornarCRITICAL_cuandoEnteTributarioCaido() throws Exception {
        mockMvc.perform(post("/api/v1/reportes-salud")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "pais": "DO",
                          "ente_tributario_activo": false,
                          "latencia_ms": 100,
                          "documentos_pendientes": 5
                        }
                        """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.estado_calculado").value("CRITICAL"));
    }

    // ─── Validaciones ────────────────────────────────────────────────────────

    @Test
    void debeRetornar422_cuandoPaisEsInvalido() throws Exception {
        mockMvc.perform(post("/api/v1/reportes-salud")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "pais": "HN",
                          "ente_tributario_activo": true,
                          "latencia_ms": 100,
                          "documentos_pendientes": 5
                        }
                        """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.codigo").value("VALIDACION_FALLIDA"))
                .andExpect(jsonPath("$.errores").isArray());
    }

    @Test
    void debeRetornar422_cuandoLatenciaEsNegativa() throws Exception {
        mockMvc.perform(post("/api/v1/reportes-salud")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "pais": "CR",
                          "ente_tributario_activo": true,
                          "latencia_ms": -50,
                          "documentos_pendientes": 5
                        }
                        """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.codigo").value("VALIDACION_FALLIDA"));
    }

    @Test
    void debeRetornar400_cuandoPayloadEsMalformado() throws Exception {
        mockMvc.perform(post("/api/v1/reportes-salud")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "pais": "GT"
                          "ente_tributario_activo": true
                        }
                        """)) // 👈 Sin coma aquí para romper la sintaxis del JSON adrede
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("PAYLOAD_INVALIDO"));
    }

    // ─── GET /api/v1/estado-regional ─────────────────────────────────────────

    @Test
    void debeRetornarListaVacia_cuandoNoHayReportes() throws Exception {
        mockMvc.perform(get("/api/v1/estado-regional"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void debeRetornarRegistro_despuesDeEnviarReporte() throws Exception {
        mockMvc.perform(post("/api/v1/reportes-salud")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "pais": "PA",
                          "ente_tributario_activo": true,
                          "latencia_ms": 400,
                          "documentos_pendientes": 10
                        }
                        """))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/v1/estado-regional"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pais").value("PA"));
    }
}
