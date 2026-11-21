package com.universidad.enviosservice.estrategia;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnvioExpressStrategyTest {

    private final EnvioExpressStrategy estrategia = new EnvioExpressStrategy();

    @ParameterizedTest(name = "peso={0} kg, distancia={1} km -> {2}")
    @CsvSource({
        "3.0, 100.0, 156.0",   // peso <= 5, sin recargo
        "5.0, 100.0, 156.0",   // limite exacto: peso = 5
        "8.0, 100.0, 161.85",   // 5 < peso <= 20
        "20.0, 100.0, 185.25",   // limite exacto: peso = 20
        "25.0, 100.0, 198.25",   // peso > 20
        "3.0, 500.0, 780.0",   // limite exacto: distancia = 500, sin recargo
        "3.0, 501.0, 814.06"   // distancia > 500, recargo de 25 antes del 30% express
    })
    void calcular_segunTramoDePesoYDistancia_devuelveElCostoEsperado(double pesoKg, double distanciaKm, double esperado) {
        assertEquals(esperado, estrategia.calcular(pesoKg, distanciaKm), 0.01);
    }
}
