package com.example.bank;

import java.math.BigDecimal;

/**
 * Convertisseur de devises.
 */
public interface CurrencyConverter {
    /**
     * Convertit un montant d'une devise vers une autre.
     * @param amount montant non nul et >= 0
     * @param from devise source
     * @param to devise cible
     * @return montant converti (arrondi à 2 décimales, HALF_UP)
     */
    BigDecimal convert(BigDecimal amount, Currency from, Currency to);
}
