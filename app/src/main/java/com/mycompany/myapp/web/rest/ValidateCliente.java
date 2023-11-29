package com.mycompany.myapp.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.service.HttpRequesties;

@Service
public class ValidateCliente {

    @Autowired
    private HttpRequesties httpRequesties; 

    public boolean validateCliente( Long cliente){
        System.out.println("Cliente ID desde ordencheck: " + cliente);
        String url = "http://192.168.194.254:8000/api/clientes/";
        ResponseEntity response = httpRequesties.getRequest(url);
        System.out.println(response.getBody().getClass());
        return true;
    }
}
