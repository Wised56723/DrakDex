package com.luis.drakdex.service; 

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DndApiService { 

    private final String DND_API_URL = "https://www.dnd5eapi.co/api/monsters";

    public Object buscarMonstrosExternos() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> resposta = restTemplate.getForEntity(DND_API_URL, Object.class);
        return resposta.getBody();
    }
}