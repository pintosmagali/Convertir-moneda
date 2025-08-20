package com.alura.conversor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.TreeMap;

public class Servicio {

    private static final String BASE = "https://v6.exchangerate-api.com/v6/";
    // API Key hardcodeada (solo para TP)
    private static final String API_KEY = "180aa048d7d4bad9ff56af5c";

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /** Convierte usando la cotización más reciente de ExchangeRate-API */
    public double convertir(String desde, String hacia, double monto) {
        Map<String, Double> rates = obtenerTasas(desde.toUpperCase());
        Double tasa = rates.get(hacia.toUpperCase());
        if (tasa == null) {
            // puente por USD si no hay directa
            if (!"USD".equalsIgnoreCase(desde) && !"USD".equalsIgnoreCase(hacia)) {
                double aUsd = convertir(desde, "USD", monto);
                return convertir("USD", hacia, aUsd);
            }
            throw new IllegalArgumentException("No hay tasa para " + desde + " → " + hacia);
        }
        return monto * tasa;
    }

    /** Descarga tasas para una base */
    public Map<String, Double> obtenerTasas(String base) {
        String url = BASE + API_KEY + "/latest/" + base.toUpperCase();

        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(15))
                    .header("User-Agent", "Conversor-Java/1.0")
                    .GET()
                    .build();

            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() != 200) {
                throw new RuntimeException("HTTP " + res.statusCode() + " al consultar: " + url);
            }

            JsonObject json = JsonParser.parseString(res.body()).getAsJsonObject();

            // La API devuelve result: success | error
            if (!json.has("result") || !"success".equalsIgnoreCase(json.get("result").getAsString())) {
                throw new RuntimeException("Respuesta no exitosa: " + res.body());
            }

            // Campo correcto en ExchangeRate-API
            JsonObject cr = json.getAsJsonObject("conversion_rates");
            if (cr == null) {
                throw new RuntimeException("Falta 'conversion_rates' en la respuesta: " + res.body());
            }

            Map<String, Double> mapa = new TreeMap<>();
            for (String k : cr.keySet()) {
                mapa.put(k, cr.get(k).getAsDouble());
            }
            return mapa;

        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener tasas: " + e.getMessage(), e);
        }
    }
}