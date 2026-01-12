# Guide de Configuration - GGWave

## 📖 Introduction

Ce guide vous explique comment configurer le plugin GGWave pour personnaliser entièrement vos vagues de félicitations.

## 🎨 Configuration des Couleurs

### Dégradé de Couleur

Le dégradé appliqué au mot "GG" est configurable via la section `gradient-colors` :

```yaml
gradient-colors:
  - "#FF0000"  # Rouge
  - "#FF7F00"  # Orange
  - "#FFFF00"  # Jaune
  - "#00FF00"  # Vert
  - "#0000FF"  # Bleu
  - "#4B0082"  # Indigo
  - "#9400D3"  # Violet
```

**Conseils** :
- Utilisez au moins 2 couleurs pour un dégradé
- Plus vous ajoutez de couleurs, plus le dégradé sera complexe
- Les couleurs doivent être au format hexadécimal (#RRGGBB)

### Exemples de dégradés prédéfinis

**Dégradé Feu** (rouge -> orange -> jaune) :
```yaml
gradient-colors:
  - "#FF0000"
  - "#FF4500"
  - "#FF8C00"
  - "#FFD700"
```

**Dégradé Océan** (bleu foncé -> cyan -> bleu clair) :
```yaml
gradient-colors:
  - "#000080"
  - "#0080FF"
  - "#00BFFF"
  - "#87CEEB"
```

**Dégradé Néon** (rose -> violet -> cyan) :
```yaml
gradient-colors:
  - "#FF00FF"
  - "#8B00FF"
  - "#4B0082"
  - "#00FFFF"
```

## 💬 Personnalisation des Messages

### Titre Principal

```yaml
title: "&6&l✦ GG WAVE ✦"
```

**Codes couleur Minecraft** :
- `&0` - Noir
- `&1` - Bleu foncé
- `&2` - Vert foncé
- `&3` - Cyan foncé
- `&4` - Rouge foncé
- `&5` - Violet
- `&6` - Or
- `&7` - Gris
- `&8` - Gris foncé
- `&9` - Bleu
- `&a` - Vert
- `&b` - Cyan
- `&c` - Rouge
- `&d` - Rose
- `&e` - Jaune
- `&f` - Blanc

**Modificateurs** :
- `&l` - Gras
- `&m` - Barré
- `&n` - Souligné
- `&o` - Italique
- `&r` - Réinitialiser

### Message de Boutique

```yaml
shop-message: "&aMerci pour votre achat sur notre boutique !"
```

Exemples :
```yaml
# Version simple
shop-message: "&e⭐ Merci pour votre soutien !"

# Version élaborée
shop-message: "&6&l✨ &eMerci d'avoir soutenu le serveur ! &6&l✨"

# Version minimaliste
shop-message: "&7Achat effectué avec succès"
```

### Lien de Boutique

```yaml
shop-link: "https://votreboutique.com"
```

Le lien sera automatiquement souligné et cliquable dans le chat.

## 🖼️ Configuration du Pixel Art

```yaml
pixel-art:
  enabled: true  # Activer/désactiver le pixel art
  size: 8        # Taille du pixel art (recommandé: 8)
```

**Options de taille** :
- `8` : Petite taille, rapide à charger (recommandé)
- `16` : Taille moyenne, plus de détails
- `32` : Grande taille, très détaillé (peut être lent)

**Note** : Si le téléchargement du skin échoue, un pixel art de fallback stylisé sera utilisé.

## ⏱️ Durée de la Vague

```yaml
duration: 300  # Durée en secondes
```

Exemples :
- `60` : 1 minute
- `300` : 5 minutes (défaut)
- `600` : 10 minutes
- `1800` : 30 minutes

## 🎁 Configuration des Récompenses

### Argent (avec Vault)

```yaml
rewards:
  money: 100  # Montant en $
```

**Important** : Nécessite le plugin Vault + un plugin d'économie (EssentialsX, CMI, etc.)

### Items

```yaml
rewards:
  item:
    material: "DIAMOND"  # Type d'item
    amount: 1            # Quantité
```

**Matériaux populaires** :
- `DIAMOND` : Diamant
- `EMERALD` : Émeraude
- `GOLD_INGOT` : Lingot d'or
- `IRON_INGOT` : Lingot de fer
- `EXPERIENCE_BOTTLE` : Bouteille d'expérience
- `ENCHANTED_GOLDEN_APPLE` : Pomme dorée enchantée

Liste complète : [Minecraft Materials](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)

### Commandes Personnalisées

```yaml
rewards:
  commands:
    - "give %player% minecraft:emerald 5"
    - "xp add %player% 100 points"
```

**Variables disponibles** :
- `%player%` : Nom du joueur

**Exemples de commandes** :

```yaml
# Donner de l'XP
- "xp add %player% 500 points"

# Donner des permissions temporaires (avec LuckPerms)
- "lp user %player% permission settemp example.vip true 1h"

# Exécuter une commande custom de votre plugin
- "myeconomy give %player% 1000"

# Téléporter le joueur
- "tp %player% 0 100 0"

# Titre personnalisé
- "title %player% title {\"text\":\"Merci !\",\"color\":\"gold\"}"
```

## 🔧 Configuration Avancée

### Exemple : Événement VIP

```yaml
wave:
  duration: 600
  title: "&5&l♛ VIP WAVE ♛"
  shop-message: "&d&lMerci pour votre achat VIP !"
  shop-link: "https://boutique.exemple.com/vip"
  
  gradient-colors:
    - "#8B00FF"
    - "#9400D3"
    - "#FF00FF"
    - "#FF1493"
  
  pixel-art:
    enabled: true
    size: 16
  
  rewards:
    money: 500
    item:
      material: "ENCHANTED_GOLDEN_APPLE"
      amount: 3
    commands:
      - "give %player% minecraft:diamond 5"
      - "xp add %player% 1000 points"
      - "lp user %player% permission settemp vip.temp true 24h"
```

### Exemple : Événement Simple

```yaml
wave:
  duration: 180
  title: "&e&lMERCI !"
  shop-message: "&aAchat effectué"
  shop-link: "https://shop.serveur.com"
  
  gradient-colors:
    - "#FFD700"
    - "#FFA500"
  
  pixel-art:
    enabled: true
    size: 8
  
  rewards:
    money: 50
    item:
      material: "DIAMOND"
      amount: 1
```

### Exemple : Mega Event

```yaml
wave:
  duration: 900
  title: "&c&l⚡ MEGA EVENT ⚡"
  shop-message: "&6&lWOW ! Merci pour ce gros achat ! &e&l✨"
  shop-link: "https://mega.boutique.com"
  
  gradient-colors:
    - "#FF0000"
    - "#FF4500"
    - "#FF8C00"
    - "#FFD700"
    - "#FFFF00"
    - "#00FF00"
    - "#00FFFF"
  
  pixel-art:
    enabled: true
    size: 32
  
  rewards:
    money: 2000
    item:
      material: "NETHER_STAR"
      amount: 5
    commands:
      - "give %player% minecraft:elytra 1"
      - "give %player% minecraft:netherite_ingot 10"
      - "xp add %player% 5000 points"
      - "lp user %player% permission set mega.vip true"
```

## 🐛 Résolution de Problèmes

### Le pixel art ne s'affiche pas
- Vérifiez que `pixel-art.enabled` est sur `true`
- Assurez-vous que le serveur a accès à internet
- Essayez de réduire la taille à `8`

### Les couleurs ne fonctionnent pas
- Vérifiez que vous utilisez bien le format hexadécimal : `#RRGGBB`
- Assurez-vous d'avoir au moins 2 couleurs dans la liste

### Les récompenses ne sont pas données
- Pour l'argent : Vérifiez que Vault est installé
- Pour les items : Vérifiez le nom du matériau (majuscules, underscores)
- Pour les commandes : Testez-les manuellement depuis la console

### Le plugin ne charge pas
- Vérifiez la version de votre serveur (1.21.11)
- Vérifiez la syntaxe YAML du config.yml
- Regardez les logs du serveur pour les erreurs

## 📞 Support

Si vous avez besoin d'aide, vérifiez :
1. Le fichier `README.md` pour les informations générales
2. Les logs du serveur dans `logs/latest.log`
3. La syntaxe YAML de votre config (utilisez https://www.yamllint.com/)

---

**Bon jeu ! 🎮**