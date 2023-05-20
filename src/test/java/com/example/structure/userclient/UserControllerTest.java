package com.example.structure.userclient;

import org.aspectj.lang.annotation.Before;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
        locations = "classpath:test.properties")
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpEntity<Void> requestEntity;

    /*@BeforeEach
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

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        this.requestEntity = requestEntity;
    }

    @Test
    void getUserStatistics_basic() throws Exception{
        ResponseEntity<String> response2 = restTemplate.exchange("/api/v1/user/1/purchase/statistics/month/10", HttpMethod.GET ,requestEntity, String.class);
        System.out.println(response2.getBody());
    }*/
}