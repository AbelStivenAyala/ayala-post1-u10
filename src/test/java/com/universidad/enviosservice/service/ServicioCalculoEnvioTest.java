package com.universidad.enviosservice.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServicioCalculoEnvioTest {

    private final ServicioCalculoEnvio servicio = new ServicioCalculoEnvio();

    // ---- Suite inicial (Parte 1) ----

    @Test
    void calculaEnvioEstandarPesoBajo() {
        double costo = servicio.calcularCosto("ESTANDAR", 3.0, 100.0, "REGULAR", false);
        assertEquals(50.0, costo, 0.01);
    }

    @Test
    void aplicaDescuentoClientePremium() {
        double costo = servicio.calcularCosto("ESTANDAR", 3.0, 100.0, "PREMIUM", false);
        assertEquals(45.0, costo, 0.01);
    }

    // ---- Ramas que la suite inicial dejaba sin cubrir (Parte 2) ----

    @Test
    void aplicaDescuentoClienteCorporativo() {
        double costo = servicio.calcularCosto("ESTANDAR", 3.0, 100.0, "CORPORATIVO", false);
        assertEquals(42.5, costo, 0.01);
    }

    @Test
    void aplicaRecargoPorFragilidad() {
        double costo = servicio.calcularCosto("ESTANDAR", 3.0, 100.0, "REGULAR", true);
        assertEquals(57.5, costo, 0.01);
    }

    @Test
    void combinaFragilidadYDescuentoCorporativo() {
        double costo = servicio.calcularCosto("ESTANDAR", 3.0, 100.0, "CORPORATIVO", true);
        assertEquals(48.875, costo, 0.01);
    }

    @Test
    void delegaEnLaEstrategiaExpress() {
        double costo = servicio.calcularCosto("EXPRESS", 3.0, 100.0, "REGULAR", false);
        assertEquals(156.0, costo, 0.01);
    }

    @Test
    void delegaEnLaEstrategiaOvernight() {
        double costo = servicio.calcularCosto("OVERNIGHT", 3.0, 100.0, "REGULAR", false);
        assertEquals(220.0, costo, 0.01);
    }

    @Test
    void delegaEnLaEstrategiaInternacional() {
        double costo = servicio.calcularCosto("INTERNACIONAL", 3.0, 1000.0, "REGULAR", false);
        assertEquals(1850.0, costo, 0.01);
    }

    @Test
    void rechazaMetodoDeEnvioNoSoportado() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> servicio.calcularCosto("DRON", 3.0, 100.0, "REGULAR", false));
        assertEquals("Metodo de envio no soportado: DRON", ex.getMessage());
    }

    @Test
    void propagaElRechazoDeOvernightParaMasDe300Km() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.calcularCosto("OVERNIGHT", 3.0, 301.0, "REGULAR", false));
    }
}
