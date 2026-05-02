package com.corretora.service;

import com.corretora.model.CotacaoMercado;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Consulta preços reais via Alpha Vantage GLOBAL_QUOTE.
 * Chave gratuita: 25 req/dia — cadastre em alphavantage.co
 */
@Service
public class AlphaVantageService {

    @Value("${alphavantage.api.key:demo}")
    private String apiKey;

    private static final String BASE_URL = "https://www.alphavantage.co/query";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Retorna cotação completa de um ativo.
     * Ações BR: adiciona .SAO ao ticker (PETR4 → PETR4.SAO)
     */
    public CotacaoMercado buscarCotacao(String ticker) {
        try {
            String symbol = ticker.contains(".") ? ticker : ticker + ".SAO";
            String url = BASE_URL + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode quote = root.get("Global Quote");

            if (quote == null || quote.isEmpty() || quote.get("05. price") == null) {
                System.out.println("⚠️ Alpha Vantage: sem dados para " + symbol);
                return null;
            }

            BigDecimal preco = new BigDecimal(quote.get("05. price").asText());
            BigDecimal variacao = safeBD(quote, "09. change");
            BigDecimal variacaoPercent = parsePercent(quote.get("10. change percent").asText());
            Long volume = safeLong(quote, "06. volume");
            String ultimoPregao = quote.get("07. latest trading day").asText();
            BigDecimal abertura = safeBD(quote, "02. open");
            BigDecimal fechamento = safeBD(quote, "08. previous close");

            return new CotacaoMercado(ticker.toUpperCase(), preco, variacao,
                    variacaoPercent, volume, ultimoPregao, abertura, fechamento);

        } catch (Exception e) {
            System.out.println("❌ Erro Alpha Vantage [" + ticker + "]: " + e.getMessage());
            return null;
        }
    }

    private BigDecimal safeBD(JsonNode node, String field) {
        try { return new BigDecimal(node.get(field).asText()); } catch (Exception e) { return BigDecimal.ZERO; }
    }

    private Long safeLong(JsonNode node, String field) {
        try { return Long.parseLong(node.get(field).asText()); } catch (Exception e) { return 0L; }
    }

    private BigDecimal parsePercent(String s) {
        try { return new BigDecimal(s.replace("%", "").trim()); } catch (Exception e) { return BigDecimal.ZERO; }
    }
}
