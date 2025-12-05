package com.example.bank;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("À activer par les étudiants une fois les TODO réalisés")
@DisplayName("EXERCICES — Tests de Bank (à compléter)")
class BankTest {

    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
    }

    @Test
    @DisplayName("Ouverture de compte valide")
    void openAccount_valid() {
        // TODO: ouvrir un compte et vérifier que:
        //  - l'objet retourné n'est pas null
        //  - getById retourne le même compte
        //  - le solde initial et la devise sont corrects
        //  Indice: utilisez assertAll pour grouper plusieurs assertions
        // BankAccount acc = bank.openAccount(...);
        // assertAll(
        //     () -> assertNotNull(acc),
        //     () -> assertSame(acc, bank.getById("...")),
        //     () -> assertEquals(new BigDecimal("..."), acc.getBalance())
        // );
    }

    @Test
    @DisplayName("Ouverture de compte avec id dupliqué doit échouer")
    void openAccount_duplicateId_fails() {
        // TODO: créer un premier compte puis tenter d'en créer un second avec le même id
        //  Attendez-vous à IllegalArgumentException via assertThrows
    }

    @Test
    @DisplayName("totalHoldings calcule la somme dans la devise cible")
    void totalHoldings_basic() {
        // TODO: créer plusieurs comptes en devises différentes, puis vérifier la somme dans une devise cible
        //  Utilisez SimpleCurrencyConverter pour la conversion
        //  Exemple d'idée:
        //  - EUR: 100.00, USD: 10.00, GBP: 10.00
        //  - Calculez le total en EUR et comparez au résultat attendu
    }

    @Test
    @DisplayName("totalHoldings avec 0 compte retourne 0")
    void totalHoldings_empty_returnsZero() {
        // TODO: vérifier que le total vaut BigDecimal.ZERO quand aucun compte n'est présent
    }
}
