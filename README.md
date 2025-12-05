Java Découverte JUnit — Mini‑banque (base pédagogique)

Ce dépôt propose une base de projet Java (Maven) qui met en œuvre JUnit 5 pour enseigner les tests unitaires.
Sujet choisi: mini‑banque avec comptes, conversions de devises et opérations simples.

Objectifs pédagogiques
- Comprendre les principes de base des tests unitaires avec JUnit 5.
- Savoir structurer un projet Maven orienté tests.
- Découvrir les annotations JUnit utiles: @Test, @BeforeEach, @AfterEach, @Nested, @DisplayName, @ParameterizedTest, @CsvSource, @Disabled, @Tag, etc.
- Apprendre à écrire des assertions claires et expressives (JUnit et AssertJ).

Pré‑requis
- Java 17+
- Maven 3.8+

Lancer les tests
- Tous les tests: mvn test
- Un test précis: mvn -Dtest=BankAccountTest test

Structure du projet
- src/main/java/com/example/bank
  - Currency: liste des devises (EUR, USD, GBP)
  - CurrencyConverter: interface de conversion
  - SimpleCurrencyConverter: implémentation simple à taux fixes
  - OverdraftException: exception de découvert
  - BankAccount: compte bancaire avec dépôt, retrait, virement, intérêts
  - Bank: registre d’accounts (pour l’exercice)
- src/test/java/com/example/bank
  - BankAccountTest: démonstrations JUnit (complet)
  - SimpleCurrencyConverterTest: démonstrations JUnit (complet)
  - BankTest: squelette d’exercices (à compléter par l’étudiant)

Chemin pédagogique proposé
1) Lire le code de BankAccount et SimpleCurrencyConverter; exécuter les tests associés.
2) Parcourir les tests pour repérer:
   - Mise en place/retour à l’état initial (@BeforeEach/@AfterEach)
   - Tests paramétrés (@ParameterizedTest, @CsvSource)
   - Vérification d’exceptions (assertThrows)
   - Messages d’assertion utiles et AssertJ pour assertions fluides
3) Compléter BankTest en suivant les TODO et retirer progressivement @Disabled.
4) Proposer puis ajouter de nouveaux cas limites (montants négatifs, arrondis, virements multiples, taux change absents, etc.).

Consignes pour les exercices (dans BankTest)
- Implémentez des tests unitaires pour Bank:
  - Ouverture de compte (valide/invalide)
  - Recherche par identifiant
  - Agrégations: totalHoldings dans une devise cible (utiliser un CurrencyConverter)
  - Cas limites: comptes vides, conversions manquantes, arrondis
- Privilégiez des tests atomiques, lisibles et indépendants.
- Donnez des noms explicites aux méthodes de test (@DisplayName utile).
- Commentez vos intentions si nécessaire.

Critères d’évaluation (suggestion)
- Couverture fonctionnelle des cas significatifs (happy path + erreurs)
- Qualité des assertions et des messages d’échec
- Organisation et lisibilité
- Respect des bonnes pratiques JUnit

Bonnes pratiques JUnit (rappel)
- Un test = une idée
- Arrange-Act-Assert (Given-When-Then)
- Tests déterministes et rapides
- Réduire les dépendances externes; mocker si nécessaire

Licence
- Libre utilisation à des fins pédagogiques.