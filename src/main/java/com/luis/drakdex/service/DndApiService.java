package com.luis.drakdex.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DndApiService {

    private final String DND_API_URL = "https://www.dnd5eapi.co/api/monsters";

    // Método 1: Listar todos os monstros (Renomeado para inglês para bater com o Controller)
    public Object getMonsters() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> resposta = restTemplate.getForEntity(DND_API_URL, Object.class);
        return resposta.getBody();
    }

    // Método 2: Buscar detalhes de um monstro específico (Adicionado agora)
    public Object getMonsterByIndex(String index) {
        RestTemplate restTemplate = new RestTemplate();
        String url = DND_API_URL + "/" + index;
        ResponseEntity<Object> resposta = restTemplate.getForEntity(url, Object.class);
        return resposta.getBody();
    }
}