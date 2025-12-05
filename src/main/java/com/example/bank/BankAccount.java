package com.example.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Compte bancaire simple (solde en BigDecimal, devise fixe).
 */
public class BankAccount {
    private final String id;
    private final String owner;
    private final Currency currency;
    private BigDecimal balance;

    public BankAccount(String id, String owner, Currency currency, BigDecimal initialBalance) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id requis");
        if (owner == null || owner.isBlank()) throw new IllegalArgumentException("propriétaire requis");
        if (currency == null) throw new IllegalArgumentException("devise requise");
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("solde initial invalide");
        this.id = id;
        this.owner = owner;
        this.currency = currency;
        this.balance = initialBalance.setScale(2, RoundingMode.HALF_UP);
    }

    public String getId() { return id; }
    public String getOwner() { return owner; }
    public Currency getCurrency() { return currency; }
    public BigDecimal getBalance() { return balance; }

    public void deposit(BigDecimal amount) {
        requirePositive(amount);
        balance = balance.add(scale(amount));
    }

    public void withdraw(BigDecimal amount) {
        requirePositive(amount);
        BigDecimal scaled = scale(amount);
        if (balance.compareTo(scaled) < 0) {
            throw new OverdraftException("Solde insuffisant");
        }
        balance = balance.subtract(scaled);
    }

    /**
     * Virement d'un compte à un autre. Si les devises diffèrent, utilise un convertisseur.
     */
    public void transferTo(BankAccount target, BigDecimal amount, CurrencyConverter converter) {
        Objects.requireNonNull(target, "compte cible requis");
        requirePositive(amount);
        BigDecimal scaled = scale(amount);
        // Débiter la source
        this.withdraw(scaled);
        // Créditer la cible (converti si devise différente)
        BigDecimal credit = this.currency == target.currency
                ? scaled
                : converter.convert(scaled, this.currency, target.currency);
        target.deposit(credit);
    }

    /**
     * Applique des intérêts simples: balance *= (1 + rate)
     */
    public void applyInterest(BigDecimal annualRatePercent) {
        if (annualRatePercent == null) throw new IllegalArgumentException("taux requis");
        BigDecimal rate = annualRatePercent.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
        balance = balance.multiply(BigDecimal.ONE.add(rate)).setScale(2, RoundingMode.HALF_UP);
    }

    private static void requirePositive(BigDecimal amount) {
        if (amount == null) throw new IllegalArgumentException("montant requis");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("montant <= 0");
    }

    private static BigDecimal scale(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
