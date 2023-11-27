package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.OrdenTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrdenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orden.class);
        Orden orden1 = getOrdenSample1();
        Orden orden2 = new Orden();
        assertThat(orden1).isNotEqualTo(orden2);

        orden2.setId(orden1.getId());
        assertThat(orden1).isEqualTo(orden2);

        orden2 = getOrdenSample2();
        assertThat(orden1).isNotEqualTo(orden2);
    }
}
