package com.example.bank;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests de BankAccount")
class BankAccountTest {

    BankAccount eur;

    @BeforeEach
    void setUp() {
        eur = new BankAccount("A1", "Alice", Currency.EUR, new BigDecimal("100.00"));
    }

    @AfterEach
    void tearDown(TestInfo info) {
        // Exemple d'utilisation de TestInfo pour journaliser
        assertNotNull(info.getDisplayName());
    }

    @Test
    @DisplayName("Dépôt crédite correctement le solde")
    void deposit_shouldIncreaseBalance() {
        eur.deposit(new BigDecimal("50.00"));
        assertEquals(new BigDecimal("150.00"), eur.getBalance());
    }

    @Test
    @DisplayName("Retrait débite correctement le solde")
    void withdraw_shouldDecreaseBalance() {
        eur.withdraw(new BigDecimal("40.00"));
        assertEquals(new BigDecimal("60.00"), eur.getBalance());
    }

    @Test
    @DisplayName("Retrait au‑delà du solde lève OverdraftException")
    void withdraw_overdraft_throws() {
        OverdraftException ex = assertThrows(OverdraftException.class,
                () -> eur.withdraw(new BigDecimal("100.01")));
        assertThat(ex.getMessage()).containsIgnoringCase("insuffisant");
    }

    @ParameterizedTest(name = "{0}% sur 100.00 devient {1}")
    @CsvSource({
            "0,100.00",
            "5,105.00",
            "10,110.00"
    })
    void applyInterest_parametrized(String ratePercent, String expected) {
        eur.applyInterest(new BigDecimal(ratePercent));
        assertEquals(new BigDecimal(expected), eur.getBalance());
    }

    @Nested
    @DisplayName("Virements")
    class Transfers {
        @Test
        @DisplayName("Virement EUR->EUR sans conversion")
        void transfer_sameCurrency() {
            BankAccount target = new BankAccount("B1", "Bob", Currency.EUR, new BigDecimal("0.00"));
            eur.transferTo(target, new BigDecimal("20.00"), new SimpleCurrencyConverter());
            assertAll(
                    () -> assertEquals(new BigDecimal("80.00"), eur.getBalance()),
                    () -> assertEquals(new BigDecimal("20.00"), target.getBalance())
            );
        }

        @Test
        @DisplayName("Virement EUR->USD avec conversion")
        void transfer_differentCurrency() {
            BankAccount usd = new BankAccount("C1", "Carol", Currency.USD, new BigDecimal("0.00"));
            SimpleCurrencyConverter conv = new SimpleCurrencyConverter();
            eur.transferTo(usd, new BigDecimal("10.00"), conv);
            assertAll(
                    () -> assertEquals(new BigDecimal("90.00"), eur.getBalance()),
                    () -> assertEquals(new BigDecimal("11.00"), usd.getBalance()) // 10 * 1.10
            );
        }
    }
}
