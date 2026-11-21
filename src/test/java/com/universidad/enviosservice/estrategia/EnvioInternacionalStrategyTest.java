package com.universidad.enviosservice.estrategia;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnvioInternacionalStrategyTest {

    private final EnvioInternacionalStrategy estrategia = new EnvioInternacionalStrategy();

    @ParameterizedTest(name = "peso={0} kg, distancia={1} km -> {2}")
    @CsvSource({
        "3.0, 1000.0, 1850.0",   // peso <= 5, sin recargo
        "5.0, 1000.0, 1850.0",   // limite exacto: peso = 5
        "8.0, 1000.0, 1859.0",   // 5 < peso <= 20
        "20.0, 1000.0, 1895.0",   // limite exacto: peso = 20
        "25.0, 1000.0, 1917.5",   // peso > 20
        "3.0, 5000.0, 9050.0",   // limite exacto: distancia = 5000, sin recargo
        "3.0, 5001.0, 9151.8"   // distancia > 5000, recargo de 100
    })
    void calcular_segunTramoDePesoYDistancia_devuelveElCostoEsperado(double pesoKg, double distanciaKm, double esperado) {
        assertEquals(esperado, estrategia.calcular(pesoKg, distanciaKm), 0.01);
    }
}
