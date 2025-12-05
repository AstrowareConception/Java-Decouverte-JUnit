package com.example.bank;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests du convertisseur SimpleCurrencyConverter")
class SimpleCurrencyConverterTest {

    @ParameterizedTest(name = "{0} {1} -> {2} = {3}")
    @CsvSource({
            "100.00, EUR, USD, 110.00",
            "100.00, EUR, GBP, 85.00",
            "100.00, USD, EUR, 90.91"
    })
    void convert_shouldUseConfiguredRates(String amount, Currency from, Currency to, String expected) {
        SimpleCurrencyConverter conv = new SimpleCurrencyConverter();
        BigDecimal out = conv.convert(new BigDecimal(amount), from, to);
        assertEquals(new BigDecimal(expected), out);
    }

    @Test
    void convert_negativeAmount_throws() {
        SimpleCurrencyConverter conv = new SimpleCurrencyConverter();
        assertThrows(IllegalArgumentException.class, () -> conv.convert(new BigDecimal("-1"), Currency.EUR, Currency.USD));
    }
}
