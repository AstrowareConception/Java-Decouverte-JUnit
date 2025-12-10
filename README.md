### Java Découverte JUnit — Mini‑banque (base pédagogique)

Ce dépôt propose une base de projet Java (Maven) qui met en œuvre JUnit 5 pour apprendre, par la pratique, les tests unitaires. Le thème fil rouge est une mini‑banque avec comptes, devises et opérations simples.

---

### Sommaire
- Présentation rapide du projet
- Objectifs pédagogiques et prérequis
- Lancer les tests (Maven)
- Vocabulaire et notions clés (définitions)
- JUnit 5 en pratique: annotations essentielles
- Patterns et style de test (AAA / Given‑When‑Then)
- Écrire des assertions efficaces (JUnit vs AssertJ)
- Tests paramétrés et données d’exemple
- Tester les exceptions
- Organisation des tests et indépendance
- Sujet détaillé: « Mini‑banque » (modèle et règles)
- Exercices guidés (dans `BankTest`)
- Stratégie de tests et cas limites
- Critères d’évaluation et feedback
- FAQ / pièges fréquents
- Aller plus loin (extensions)

---

### Présentation rapide du projet
- Build: Maven (Java 17)
- Tests: JUnit 5 (Jupiter) + AssertJ
- Domaine: comptes bancaires, devises EUR/USD/GBP, conversion simple, découvert interdit, intérêts simples.

Structure du projet
- `src/main/java/com/example/bank`
  - `Currency`: énumération des devises (EUR, USD, GBP)
  - `CurrencyConverter`: contrat de conversion
  - `SimpleCurrencyConverter`: implémentation pédagogique (taux fixes, arrondi 2 décimales)
  - `OverdraftException`: exception de découvert
  - `BankAccount`: dépôt, retrait, virement (conversion possible), intérêts
  - `Bank`: registre de comptes, agrégation des avoirs (`totalHoldings`)
- `src/test/java/com/example/bank`
  - `BankAccountTest`: tests de démonstration (complet)
  - `SimpleCurrencyConverterTest`: tests de démonstration (complet)
  - `BankTest`: squelette d’exercices (à compléter)

---

### Objectifs pédagogiques
- Comprendre les bases des tests unitaires et leur intérêt (qualité, refactoring, documentation vivante).
- Savoir structurer un projet Maven et exécuter les tests JUnit 5.
- Maîtriser les annotations JUnit les plus utiles: `@Test`, `@BeforeEach`, `@AfterEach`, `@DisplayName`, `@Nested`, `@ParameterizedTest`, `@CsvSource`, `@Disabled`.
- Écrire des assertions claires et robustes avec JUnit et AssertJ.
- Apprendre à concevoir des tests indépendants, déterministes et rapides.

Pré‑requis
- Java 17+
- Maven 3.8+

---

### Lancer les tests
- Tous les tests
```
mvn test
```
- Un test précis
```
mvn -Dtest=BankAccountTest test
```
- Un seul cas de test (méthode)
```
mvn -Dtest=BankAccountTest#deposit_shouldIncreaseBalance test
```

Astuce: Les rapports Surefire sont générés dans `target/surefire-reports`.

---

### Vocabulaire et notions clés
- Test unitaire: code qui vérifie de façon automatique le comportement d’une unité (classe/méthode) en isolant les dépendances.
- Assertion: vérification d’un résultat attendu (ex.: `assertEquals(42, out)`).
- Fixture: état de départ commun aux tests (préparé dans `@BeforeEach`).
- Idempotence: relancer les tests doit produire le même résultat — pas d’état partagé caché.
- Déterminisme: pas d’aléatoire ni dépendance réseau/horloge non contrôlée.

---

### JUnit 5 en pratique: annotations essentielles
- `@Test`: marque une méthode comme test.
- `@BeforeEach` / `@AfterEach`: préparation et nettoyage avant/après chaque test (garantit l’indépendance).
- `@DisplayName`: nom lisible pour le rapport.
- `@Nested`: regroupe des cas autour d’un contexte commun.
- `@Disabled`: désactive temporairement un test (ex.: exercices à compléter).
- `@ParameterizedTest` + `@CsvSource`: répète le même test avec plusieurs jeux de données.

Exemple extrait du projet
```java
@BeforeEach
void setUp() {
    eur = new BankAccount("A1", "Alice", Currency.EUR, new BigDecimal("100.00"));
}

@Test
@DisplayName("Retrait au‑delà du solde lève OverdraftException")
void withdraw_overdraft_throws() {
    OverdraftException ex = assertThrows(OverdraftException.class,
        () -> eur.withdraw(new BigDecimal("100.01")));
    assertThat(ex.getMessage()).containsIgnoringCase("insuffisant");
}
```

---

### Patterns et style de test (AAA / Given‑When‑Then)
- Arrange (Given): préparer les données et l’environnement.
- Act (When): exécuter l’action à tester.
- Assert (Then): vérifier le résultat attendu.

Exemple
```java
// Arrange
BankAccount acc = new BankAccount("A1", "Alice", Currency.EUR, new BigDecimal("100.00"));
// Act
acc.deposit(new BigDecimal("50.00"));
// Assert
assertEquals(new BigDecimal("150.00"), acc.getBalance());
```

---

### Écrire des assertions efficaces
- JUnit (statique): `assertEquals`, `assertTrue`, `assertThrows`, `assertAll`, etc.
- AssertJ (fluide): `assertThat(out).isEqualTo(...)`, `contains`, `hasSize`, etc.

Conseils
- Un message d’échec explicite aide au diagnostic.
- Préférez `assertAll` pour regrouper plusieurs vérifications d’un même scénario.

Exemple `assertAll`
```java
assertAll(
    () -> assertNotNull(acc),
    () -> assertSame(acc, bank.getById("A1")),
    () -> assertEquals(new BigDecimal("100.00"), acc.getBalance())
);
```

---

### Tests paramétrés et données d’exemple
Utilisez `@ParameterizedTest` et `@CsvSource` pour varier les données sans dupliquer le code.

Extrait
```java
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
```

---

### Tester les exceptions
Trois étapes: déclencher, capturer, vérifier le type et le message.

```java
OverdraftException ex = assertThrows(OverdraftException.class,
    () -> eur.withdraw(new BigDecimal("100.01")));
assertThat(ex.getMessage()).containsIgnoringCase("insuffisant");
```

---

### Organisation des tests et indépendance
- Chaque test part d’une fixture fraîche (`@BeforeEach`).
- Pas d’ordre d’exécution supposé: les tests doivent réussir isolément.
- Évitez les dépendances réseau/FS non nécessaires.

---

### Sujet détaillé: « Mini‑banque »
Règles du domaine (extrait)
- Un `BankAccount` a un `id`, un `owner`, une `currency` et un `balance` en `BigDecimal`.
- `deposit` et `withdraw` refusent les montants nuls/négatifs; `withdraw` lève `OverdraftException` si solde insuffisant.
- `transferTo` débite la source puis crédite la cible, avec conversion si devises différentes (via `CurrencyConverter`).
- `applyInterest` applique des intérêts simples: `balance *= (1 + rate%)`, arrondi à 2 décimales.
- `Bank` ouvre des comptes (`openAccount`) et calcule la somme des avoirs avec `totalHoldings(target, converter)`.

---

### Exercices guidés (fichier `src/test/java/com/example/bank/BankTest.java`)
Le fichier est initialement annoté `@Disabled` pour que vous activiez les tests au fur et à mesure.

1) Ouverture de compte valide
- Ouvrez un compte via `bank.openAccount("A1", "Alice", EUR, 100.00)`
- Vérifiez:
  - l’objet retourné n’est pas `null`
  - `bank.getById("A1")` renvoie le même objet (identité)
  - `getBalance()` vaut `100.00` et `getCurrency()` est `EUR`
- Groupez avec `assertAll`.

2) Ouverture avec identifiant dupliqué doit échouer
- Créez un premier compte `A1`, puis tentez d’en créer un second `A1`.
- Attendez `IllegalArgumentException` (utilisez `assertThrows`).

3) Agrégation des avoirs (`totalHoldings`) dans une devise cible
- Créez plusieurs comptes: ex. `EUR:100.00`, `USD:10.00`, `GBP:10.00`.
- Utilisez `SimpleCurrencyConverter`.
- Calculez le total en EUR et comparez au résultat attendu.

4) Cas vide
- Sans aucun compte, `totalHoldings` doit retourner `BigDecimal.ZERO`.

Astuce: Commencez par enlever `@Disabled` sur un test à la fois pour progresser étape par étape.

---

### Stratégie de tests et cas limites
- Montants invalides (nuls, négatifs) pour `deposit`/`withdraw`/`convert`.
- Arrondis à 2 décimales et effets cumulés (intérêts successifs).
- Virements inter‑devises: exactitude de la conversion et des soldes.
- Absence de taux de change (dans d’autres implémentations possibles du convertisseur, que faut‑il tester?).
- Résilience aux identifiants vides ou propriétaires manquants (déjà couverts par le constructeur de `BankAccount`).

---

### Critères d’évaluation (suggestion)
- Couverture fonctionnelle: chemins heureux + erreurs représentatives.
- Qualité des assertions: messages clairs, `assertAll` quand pertinent, AssertJ bien utilisé.
- Lisibilité et organisation: `@DisplayName` parlant, méthode = une idée.
- Respect des bonnes pratiques JUnit: indépendance, déterminisme, rapidité.

Barème indicatif
- Base (tests passent, cas principaux) — 10/20
- Cas d’erreurs pertinents + paramétrés — 5/20
- Qualité/clareté (noms, messages, structure) — 5/20

---

### FAQ / pièges fréquents
- « Mes tests dépendent de l’ordre » → utilisez `@BeforeEach` et évitez l’état partagé statique.
- « BigDecimal ne tombe pas pile » → fixez l’échelle (`setScale(2, HALF_UP)`) comme dans le code.
- « Les conversions donnent des valeurs étranges » → vérifiez les taux dans `SimpleCurrencyConverter`.
- « Un test casse parfois » → éliminez l’aléatoire/horloge, ou injectez une horloge fixe.

---

### Exercices bonus — Aller plus loin

Ces exercices sont facultatifs mais fortement recommandés pour progresser au‑delà des bases. Ils introduisent des pratiques professionnelles et des outils fréquemment utilisés.

1) Convertisseur configurable + tests avec doubles (mocks)  
Objectif: découpler la logique métier des données de taux.  
Consignes:
- Créez une implémentation `ConfigurableCurrencyConverter` qui reçoit une `Map<(from,to), rate>` via le constructeur (ou une interface `RateProvider`).
- Écrivez des tests qui couvrent: taux manquant (exception claire), taux non positif, arrondi, chemins heureux.  
- Bonus: introduisez un mock pour `RateProvider` (ex.: Mockito) et testez les interactions (appel des méthodes avec bons arguments).
Critères de réussite: tests robustes, messages d’erreur explicites, couverture des branches d’erreur.  
Pistes: ajoutez la dépendance Mockito en scope `test` dans le `pom.xml` et utilisez `when(...).thenReturn(...)`.

2) Tags JUnit et profils Maven  
Objectif: catégoriser et exécuter sélectivement des tests.  
Consignes:
- Annotez certains tests longs avec `@Tag("slow")` et d’autres avec `@Tag("unit")`.
- Créez un profil Maven `slow-tests` qui active l’exécution des tests `@Tag("slow")` (configuration Surefire).  
Critères: exécuter `mvn test` n’inclut pas les « slow », mais `mvn test -P slow-tests` oui.

3) Injection d’horloge (`Clock`) et tests temporels déterministes  
Objectif: éliminer la dépendance au temps réel.  
Consignes:
- Ajoutez un champ `Clock` (java.time) dans les classes qui en auraient besoin (si vous introduisez des opérations datées, intérêts journaliers, etc.).
- Passez une `Clock.fixed(...)` en test pour rendre les résultats déterministes.  
Critères: tests répétables, pas d’utilisation directe de `Instant.now()` hors production.

4) Tests paramétrés avancés et sources de données  
Objectif: enrichir les cas sans dupliquer le code.  
Consignes:
- Utilisez `@MethodSource` pour générer des jeux de données complexes (objets, combinaisons de devises et montants).
- Ajoutez un `@CsvFileSource` qui lit des cas depuis un fichier CSV placé dans `src/test/resources`.  
Critères: lisibilité, couverture de combinaisons pertinentes.

5) Tests dynamiques (`@TestFactory`)  
Objectif: générer des tests à la volée à partir d’un catalogue de scénarios.  
Consignes:
- Écrivez une méthode `@TestFactory` qui parcourt une liste de scénarios (ex.: transferts multi‑devises) et génère un `DynamicTest` par scénario.  
Critères: rapports de tests parlants (noms dynamiques), pas de duplication de logique.

6) Stratégie d’arrondi et précision  
Objectif: documenter et verrouiller le comportement d’arrondi.  
Consignes:
- Ajoutez des tests qui confirment l’utilisation de `HALF_UP` et l’échelle à 2 décimales dans toutes les opérations monétaires (dépôt, retrait, conversion, intérêts).  
Critères: tests échouent si l’échelle ou le mode d’arrondi change.

7) Mutation testing (PIT)  
Objectif: mesurer la robustesse des tests au‑delà de la couverture ligne.  
Consignes:
- Intégrez PIT (pitest) via le plugin Maven et générez un rapport.  
- Renforcez les tests jusqu’à obtenir un bon score de mutations tuées.  
Critères: score de mutation amélioré, discussions sur les survivants pertinents.

8) Couverture et qualité  
Objectif: visualiser les zones non testées.  
Consignes:
- Ajoutez JaCoCo (plugin Maven) pour produire un rapport de couverture.  
- Identifiez les classes/méthodes faiblement couvertes et écrivez des tests ciblés.  
Critères: amélioration mesurable de la couverture, sans tests superficiels.

9) Golden Master pour refactoring  
Objectif: sécuriser un refactor sans spécification complète.  
Consignes:
- Capturez le comportement actuel d’une méthode (ex.: `totalHoldings`) avec des jeux d’entrée variés (snapshot des sorties).  
- Refactorez l’implémentation (structure interne) et vérifiez que les sorties restent identiques.  
Critères: tests protègent le comportement observé tout en permettant d’améliorer le code.

10) Tests de propriétés (property‑based)  
Objectif: découvrir des cas limites automatiquement.  
Consignes:
- Avec jqwik (ou junit‑quickcheck), exprimez des propriétés comme:  
  `convert(a, X, X) == a` (identité), `convert(a, X, Y)` est monotone pour `a >= 0`,  
  et éventuellement `convert(convert(a, X, Y), Y, X)` ~ `a` (dans la précision d’arrondi).  
Critères: propriétés stables, espaces d’entrée bornés pour éviter des faux positifs liés aux arrondis.

11) Données aléatoires reproductibles  
Objectif: générer des scénarios variés mais déterministes.  
Consignes:
- Utilisez un `Random`/`SplittableRandom` initialisé avec une seed fixe pour créer des comptes et montants aléatoires.  
- Journalisez la seed quand un test échoue pour rejouer le cas.  
Critères: aucun test « flaky », seed affichée en cas d’échec.

12) Intégration CI et filtrage des tests  
Objectif: automatiser l’exécution des tests.  
Consignes:
- Ajoutez un workflow CI (GitHub Actions) ou GitLab CI qui exécute `mvn -q -B test` et publie les rapports Surefire/JaCoCo.  
- Filtrez les tags (`unit` par défaut, `slow` la nuit).  
Critères: pipeline vert, rapports disponibles en artefacts.

13) Conception orientée tests (TDD)  
Objectif: itérer par petits pas.  
Consignes:
- Choisissez une nouvelle fonctionnalité (ex.: frais de virement, plafonds quotidiens, catégories de comptes) et implémentez‑la en TDD:  
  cycle « rouge → vert → refactor ».  
Critères: commits fréquents, historiques clairs, tests lisibles.

Note: la plupart de ces exercices ne nécessitent pas de modifier le code existant; vous pouvez travailler dans de nouvelles classes de test et, si besoin, ajouter des dépendances en scope `test` dans le `pom.xml`.

---

### Licence
Libre utilisation à des fins pédagogiques.