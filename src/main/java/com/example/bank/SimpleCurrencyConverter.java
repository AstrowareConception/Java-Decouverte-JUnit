package com.example.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;

/**
 * Implémentation simple avec des taux fixes.
 * Taux arbitraires pour l'enseignement (ne pas utiliser en prod!).
 */
public class SimpleCurrencyConverter implements CurrencyConverter {

    private final Map<Currency, Map<Currency, BigDecimal>> rates = new EnumMap<>(Currency.class);

    public SimpleCurrencyConverter() {
        for (Currency c : Currency.values()) {
            rates.put(c, new EnumMap<>(Currency.class));
            rates.get(c).put(c, BigDecimal.ONE);
        }
        // Taux de référence (exemple pédagogique)
        // 1 EUR = 1.10 USD, 1 EUR = 0.85 GBP
        setRate(Currency.EUR, Currency.USD, new BigDecimal("1.10"));
        setRate(Currency.EUR, Currency.GBP, new BigDecimal("0.85"));

        // Dériver l'inverse
        setRate(Currency.USD, Currency.EUR, BigDecimal.ONE.divide(new BigDecimal("1.10"), 10, RoundingMode.HALF_UP));
        setRate(Currency.GBP, Currency.EUR, BigDecimal.ONE.divide(new BigDecimal("0.85"), 10, RoundingMode.HALF_UP));

        // USD <-> GBP (via EUR approximatif)
        BigDecimal usdToEur = getRate(Currency.USD, Currency.EUR);
        BigDecimal eurToGbp = getRate(Currency.EUR, Currency.GBP);
        setRate(Currency.USD, Currency.GBP, usdToEur.multiply(eurToGbp));
        BigDecimal gbpToEur = getRate(Currency.GBP, Currency.EUR);
        BigDecimal eurToUsd = getRate(Currency.EUR, Currency.USD);
        setRate(Currency.GBP, Currency.USD, gbpToEur.multiply(eurToUsd));
    }

    public void setRate(Currency from, Currency to, BigDecimal rate) {
        if (from == null || to == null) throw new IllegalArgumentException("Devises requises");
        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Taux invalide");
        rates.get(from).put(to, rate);
    }

    public BigDecimal getRate(Currency from, Currency to) {
        BigDecimal r = rates.get(from).get(to);
        if (r == null) throw new IllegalStateException("Taux absent: " + from + "->" + to);
        return r;
    }

    @Override
    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        if (amount == null) throw new IllegalArgumentException("Montant requis");
        if (amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Montant négatif");
        if (from == null || to == null) throw new IllegalArgumentException("Devises requises");
        BigDecimal rate = getRate(from, to);
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
