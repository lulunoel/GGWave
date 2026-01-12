# 💰 Guide Vault - Récompenses en argent

## Qu'est-ce que Vault ?

Vault est une API qui permet aux plugins de communiquer avec les systèmes d'économie. GGWave utilise Vault pour donner de l'argent aux joueurs qui disent "GG".

## Installation

### 1. Installer Vault

Téléchargez Vault depuis :
- SpigotMC : https://www.spigotmc.org/resources/vault.34315/
- Bukkit : https://dev.bukkit.org/projects/vault

Placez `Vault.jar` dans votre dossier `plugins/`

### 2. Installer un plugin d'économie

Vault seul ne suffit pas ! Vous devez aussi installer un plugin qui gère l'économie. Choisissez-en un :

#### EssentialsX (Recommandé) ✅
- Le plus populaire et stable
- Téléchargement : https://essentialsx.net/downloads.html
- Fichiers nécessaires : `EssentialsX.jar`

#### CMI
- Plugin premium très complet
- Site : https://www.zrips.net/cmi/

#### Autres options
- **iConomy** (ancien mais stable)
- **TheNewEconomy** (moderne)
- **CraftConomy** (serveur Bungeecord)

### 3. Redémarrer le serveur

Après avoir installé Vault + plugin d'économie, redémarrez complètement le serveur.

## Vérification de l'installation

### Dans les logs du serveur

Cherchez ces messages au démarrage :

```
[GGWave] GGWave plugin activé avec succès !
[GGWave] Vault détecté ! Économie gérée par : Essentials Economy
[GGWave] ✓ Vault activé - Récompenses en argent disponibles
```

### Si Vault n'est pas détecté

```
[GGWave] Vault n'est pas installé. Les récompenses en argent seront désactivées.
```
→ Installez Vault

```
[GGWave] Vault est installé mais aucun plugin d'économie n'a été détecté !
[GGWave] ✗ Vault non disponible - Récompenses en argent désactivées
```
→ Installez un plugin d'économie (EssentialsX recommandé)

## Configuration des récompenses

Dans `config.yml` :

```yaml
wave:
  rewards:
    # Argent (nécessite Vault)
    money: 100  # Montant en $ (ou autre devise)
    
    # Items
    item:
      material: "DIAMOND"
      amount: 1
    
    # Commandes
    commands:
      - "give %player% minecraft:emerald 5"
```

### Exemples de montants

```yaml
# Petit montant
money: 10

# Montant moyen
money: 100

# Gros montant
money: 1000

# Avec décimales
money: 50.5
```

## Fonctionnement

### Quand un joueur dit "GG"

1. Le plugin vérifie si Vault est activé
2. Si oui, il ajoute le montant au compte du joueur
3. Le joueur reçoit un message avec le montant formaté

### Messages reçus

**Avec Vault activé** :
```
Vous avez reçu 100$ pour avoir dit GG !
```

**Sans Vault** :
```
Vous auriez reçu 100$ (Vault non installé)
```

## Personnalisation de la devise

La devise affichée dépend de votre plugin d'économie :

### EssentialsX

Dans `plugins/Essentials/config.yml` :

```yaml
economy:
  currency-symbol: '$'
  currency-name-singular: 'dollar'
  currency-name-plural: 'dollars'
```

Exemples :
```yaml
# Euros
currency-symbol: '€'
currency-name-singular: 'euro'
currency-name-plural: 'euros'

# Points
currency-symbol: '⭐'
currency-name-singular: 'point'
currency-name-plural: 'points'

# Pièces d'or
currency-symbol: '🪙'
currency-name-singular: 'pièce'
currency-name-plural: 'pièces'
```

Le plugin GGWave utilisera automatiquement votre configuration !

## Commandes utiles

### Vérifier le solde d'un joueur

```bash
# EssentialsX
/balance <joueur>
/bal <joueur>

# CMI
/balance <joueur>
```

### Donner/Retirer de l'argent manuellement

```bash
# EssentialsX
/eco give <joueur> <montant>
/eco take <joueur> <montant>
/eco set <joueur> <montant>

# CMI
/cmi money give <joueur> <montant>
```

### Voir qui a de l'argent

```bash
# EssentialsX
/balancetop
/baltop

# CMI
/cmi baltop
```

## Dépannage

### Problème : "Vault non détecté"

**Solution** :
1. Vérifiez que `Vault.jar` est dans `plugins/`
2. Redémarrez le serveur
3. Vérifiez les logs pour des erreurs Vault

### Problème : "Aucun plugin d'économie détecté"

**Solution** :
1. Installez EssentialsX
2. Vérifiez que `EssentialsX.jar` est dans `plugins/`
3. Redémarrez le serveur
4. Vérifiez que l'économie est activée dans EssentialsX

### Problème : Les joueurs ne reçoivent pas d'argent

**Vérifications** :
1. `/ggwave info` pendant une vague
2. Vérifier les logs pour des erreurs
3. Tester manuellement : `/eco give <joueur> 100`
4. Vérifier que le montant est bien configuré dans `config.yml`

### Problème : Montant incorrect

**Cause** : Format de nombre invalide dans la config

**Solution** :
```yaml
# ❌ Incorrect
money: "100"  # Guillemets = texte

# ✅ Correct
money: 100    # Nombre
money: 100.5  # Nombre avec décimale
```

## Compatibilité

### Plugins d'économie supportés

✅ **EssentialsX** (recommandé)
✅ **CMI**
✅ **iConomy**
✅ **TheNewEconomy**
✅ **CraftConomy**
✅ Tout plugin supporté par Vault

### Versions de Minecraft

- ✅ 1.16.5+
- ✅ 1.17+
- ✅ 1.18+
- ✅ 1.19+
- ✅ 1.20+
- ✅ 1.21+

### Serveurs

- ✅ Spigot
- ✅ Paper
- ✅ Purpur
- ✅ Folia (avec cette version du plugin)

## Configuration avancée

### Récompenses progressives

```yaml
# Vague courte = petite récompense
wave:
  duration: 60
  rewards:
    money: 25

# Vague longue = grosse récompense
wave:
  duration: 600
  rewards:
    money: 200
```

### Combiner argent + items + commandes

```yaml
rewards:
  money: 100
  item:
    material: "DIAMOND"
    amount: 1
  commands:
    - "give %player% minecraft:emerald 5"
    - "xp add %player% 100 points"
```

Le joueur recevra TOUT !

### Désactiver l'argent mais garder les items

```yaml
rewards:
  # money: 100  ← Commentez ou supprimez cette ligne
  item:
    material: "DIAMOND"
    amount: 1
```

## Exemple de configuration complète

```yaml
wave:
  duration: 300
  
  rewards:
    # 100$ par GG
    money: 100
    
    # 1 diamant
    item:
      material: "DIAMOND"
      amount: 1
    
    # 5 émeraudes + 100 XP
    commands:
      - "give %player% minecraft:emerald 5"
      - "xp add %player% 100 points"
```

Résultat : Chaque joueur qui dit "GG" reçoit :
- 💰 100$
- 💎 1 diamant
- 💚 5 émeraudes
- ⭐ 100 points d'XP

---

## Support

Si vous avez des problèmes avec Vault :
1. Vérifiez la version de Vault (dernière recommandée)
2. Vérifiez que votre plugin d'économie fonctionne (`/balance`)
3. Consultez les logs du serveur
4. Testez les commandes d'économie manuellement

**Vault est optionnel** : Si vous ne voulez pas d'argent, supprimez simplement la ligne `money:` de la config !

---

**Profitez des récompenses automatiques ! 💰**