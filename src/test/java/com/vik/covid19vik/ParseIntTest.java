package com.vik.covid19vik;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParseIntTest {
    @Test
    void testStringToInt() {
        String sixteen = "16.0";
        int teen = Integer.parseInt(sixteen);
        assertEquals(16, teen);
    }
}
