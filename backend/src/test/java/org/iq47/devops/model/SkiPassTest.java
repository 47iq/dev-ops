package org.iq47.devops.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkiPassPojoTest {

    @Test
    void gettersAndSetters_shouldWorkCorrectly() {
        SkiPass pass = new SkiPass();
        pass.setId(1L);
        pass.setPassType("WEEKEND");
        pass.setPrice(99.9);
        pass.setDuration(2);

        assertAll(
                () -> assertEquals(1L, pass.getId()),
                () -> assertEquals("WEEKEND", pass.getPassType()),
                () -> assertEquals(99.9, pass.getPrice()),
                () -> assertEquals(2, pass.getDuration())
        );
    }

    @Test
    void equalsAndHashCode_shouldDependOnAllFieldsGeneratedByLombok() {
        SkiPass p1 = new SkiPass();
        p1.setId(1L);
        p1.setPassType("DAY");
        p1.setPrice(50.0);
        p1.setDuration(1);

        SkiPass p2 = new SkiPass();
        p2.setId(1L);
        p2.setPassType("DAY");
        p2.setPrice(50.0);
        p2.setDuration(1);

        SkiPass p3 = new SkiPass();
        p3.setId(2L);                 // одно поле отличается
        p3.setPassType("DAY");
        p3.setPrice(50.0);
        p3.setDuration(1);

        // p1 и p2 идентичны
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        // p1 и p3 различаются
        assertNotEquals(p1, p3);
        assertNotEquals(p1.hashCode(), p3.hashCode());
    }
}
