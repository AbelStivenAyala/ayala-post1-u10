package com.universidad.enviosservice.estrategia;

/** Strategy: cada metodo de envio calcula su costo base con una formula propia. */
public interface EstrategiaEnvio {
    double calcular(double pesoKg, double distanciaKm);
}
