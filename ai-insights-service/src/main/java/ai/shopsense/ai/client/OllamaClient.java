package ai.shopsense.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class OllamaClient {

    private final RestClient restClient;
    private final String baseUrl;

    public OllamaClient(@Value("${ollama.base-url:http://localhost:11434}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.restClient = RestClient.create();
    }

    public String generate(String prompt) {
        var request = Map.of(
                "model", "llama3.1:8b-q4_0",
                "prompt", prompt,
                "stream", false
        );
        var response = restClient.post()
                .uri(baseUrl + "/api/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(Map.class);
        return response == null ? "" : (String) response.getOrDefault("response", "No data");
    }
}
