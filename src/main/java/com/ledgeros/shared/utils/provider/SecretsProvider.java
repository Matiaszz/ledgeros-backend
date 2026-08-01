package com.ledgeros.shared.utils.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.Map;

@Slf4j
public class SecretsProvider {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final SecretsManagerClient CLIENT = SecretsManagerClient.builder()
            .region(Region.of(System.getenv().getOrDefault("AWS_REGION", "us-east-1")))
            .build();

    // Cache em memória durante o ciclo de vida da instância Lambda
    private static String cachedJwtSecret;

    public static synchronized String getJwtSecret() {
        if (cachedJwtSecret != null) {
            return cachedJwtSecret;
        }


        String envJwtSecret = System.getenv("JWT_SECRET");
        if (envJwtSecret != null && !envJwtSecret.isBlank()) {
            cachedJwtSecret = envJwtSecret;
            return cachedJwtSecret;
        }

        String secretName = System.getenv().getOrDefault("SECRET_NAME", "ledgeros/dev/jwt-secret");

        try {
            GetSecretValueRequest request = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse response = CLIENT.getSecretValue(request);
            Map<String, String> map =
                    MAPPER.readValue(response.secretString(),
                            new TypeReference<>() {});

            cachedJwtSecret = map.get("jwt-secret");
            log.info("Secret '{}' carregada com sucesso do AWS Secrets Manager.", secretName);
            return cachedJwtSecret;

        } catch (Exception e) {
            log.error("Erro ao buscar secret no Secrets Manager. Usando fallback.", e);
            return System.getenv().getOrDefault("JWT_SECRET", "secreto_dev_fallback");
        }
    }
}
