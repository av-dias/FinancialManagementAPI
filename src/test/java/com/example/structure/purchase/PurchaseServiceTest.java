package com.example.structure.purchase;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
        locations = "classpath:test.properties")
class PurchaseServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private HttpHeaders headers;

    @BeforeEach
    private void set_up(){
        String url = "/api/v1/login";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                // Add query parameter
                .queryParam("username", "al.vrdias@gmail.com")
                .queryParam("password", "123");

        ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
        JSONObject json = new JSONObject(response.getBody());

        System.out.println(json.getString("access_token"));

        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, "Bearer " + json.getString("access_token"));

        this.headers = headers;
    }

    @Test
    void addMobilePurchases() {
        String url = "/api/v1/purchase/mobile/user/update/purchases/";
        //String requestJson = "{\"queriedQuestion\":\"Is there pain in your hand?\"}";
        String requestJson = "[{\"name\":\"Tesco\", \"type\":\"Supermarket\", \"value\": 10, \"dop\":\"2023-06-10\"},{\"name\":\"Ikea\", \"type\":\"Home\", \"value\": 500, \"dop\":\"2023-05-23\"}]";

        HttpEntity<String> requestEntity = new HttpEntity<String>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/purchase/mobile/user/1/update/purchases/", HttpMethod.POST ,requestEntity, String.class);
        System.out.println(response.getBody());
    }
}