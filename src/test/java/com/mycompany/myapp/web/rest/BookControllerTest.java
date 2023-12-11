package com.mycompany.myapp.web.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.domain.Orden;
import com.mycompany.myapp.domain.enumeration.Modo;
import com.mycompany.myapp.repository.OrdenRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdenRepository mockRepository;

    private Orden orden;

    @Before
    public void init() {
        orden = new Orden().cliente(1108).accionId(1).accion("AAPL").operacion("COMPRA").cantidad(1).modo(Modo.FINDIA);
        when(mockRepository.findById(anyLong())).thenReturn(Optional.of(orden));
    }

    @Test
    public void find_ordenById_OK() throws Exception {
        mockMvc
            .perform(get("/api/ordens/{id}", 1L))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(orden.getId().intValue())))
            .andExpect(jsonPath("$.cliente", is(1108)))
            .andExpect(jsonPath("$.accionId", is(1)))
            .andExpect(jsonPath("$.accion", is("AAPL")))
            .andExpect(jsonPath("$.operacion", is("COMPRA")))
            .andExpect(jsonPath("$.cantidad", is(1)))
            .andExpect(jsonPath("$.modo", is("FINDIA")));

        verify(mockRepository, times(1)).findById(anyLong());
    }
}
