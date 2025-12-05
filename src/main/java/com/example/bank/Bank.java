package com.example.bank;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Registre simple de comptes bancaires.
 */
public class Bank {
    private final Map<String, BankAccount> accounts = new HashMap<>();

    /**
     * Ouvre un compte et l'ajoute au registre.
     * @throws IllegalArgumentException si id déjà existant
     */
    public BankAccount openAccount(String id, String owner, Currency currency, BigDecimal initialBalance) {
        Objects.requireNonNull(id, "id");
        if (accounts.containsKey(id)) {
            throw new IllegalArgumentException("id déjà utilisé");
        }
        BankAccount acc = new BankAccount(id, owner, currency, initialBalance);
        accounts.put(id, acc);
        return acc;
    }

    public BankAccount getById(String id) {
        return accounts.get(id);
    }

    public Collection<BankAccount> getAll() {
        return Collections.unmodifiableCollection(accounts.values());
    }

    /**
     * Calcule la somme des avoirs dans une devise cible en utilisant le convertisseur.
     */
    public BigDecimal totalHoldings(Currency target, CurrencyConverter converter) {
        Objects.requireNonNull(target, "devise cible requise");
        Objects.requireNonNull(converter, "convertisseur requis");
        return accounts.values().stream()
                .map(a -> a.getCurrency() == target
                        ? a.getBalance()
                        : converter.convert(a.getBalance(), a.getCurrency(), target))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
