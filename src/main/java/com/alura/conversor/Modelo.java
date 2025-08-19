package com.alura.conversor;

import java.time.LocalDateTime;

public class Modelo {
    private final String desde;
    private final String hacia;
    private final double monto;
    private final double resultado;
    private final LocalDateTime fecha = LocalDateTime.now();

    public Modelo(String desde, String hacia, double monto, double resultado) {
        this.desde = desde;
        this.hacia = hacia;
        this.monto = monto;
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return String.format("%.2f %s = %.2f %s  (%s)", monto, desde, resultado, hacia, fecha);
    }
}