package com.universidad.enviosservice.estrategia;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnvioOvernightStrategyTest {

    private final EnvioOvernightStrategy estrategia = new EnvioOvernightStrategy();

    @ParameterizedTest(name = "peso={0} kg, distancia={1} km -> {2}")
    @CsvSource({
        "3.0, 100.0, 220.0",   // peso <= 5
        "5.0, 100.0, 220.0",   // limite exacto: peso = 5
        "8.0, 100.0, 227.5",   // 5 < peso <= 20
        "20.0, 100.0, 257.5",   // limite exacto: peso = 20
        "25.0, 100.0, 272.5",   // peso > 20
        "3.0, 300.0, 620.0"   // limite exacto: distancia = 300, todavia permitido
    })
    void calcular_segunTramoDePesoYDistancia_devuelveElCostoEsperado(double pesoKg, double distanciaKm, double esperado) {
        assertEquals(esperado, estrategia.calcular(pesoKg, distanciaKm), 0.01);
    }

    @ParameterizedTest
    @ValueSource(doubles = {301.0, 1000.0})
    void calcular_distanciaMayorA300Km_lanzaExcepcion(double distanciaKm) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> estrategia.calcular(3.0, distanciaKm));
        assertEquals("OVERNIGHT no disponible para mas de 300 km", ex.getMessage());
    }
}
