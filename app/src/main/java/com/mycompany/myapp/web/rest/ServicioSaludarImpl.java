package com.mycompany.myapp.web.rest;

import org.springframework.stereotype.Service;

@Service("ServicioSaludar")
public class ServicioSaludarImpl implements ServicioSaludar{
    @Override
    public String saludar(String texto){
        return texto;
    }
}
