# GGWave Plugin - Minecraft 1.21.11

Plugin Minecraft qui permet de lancer des vagues de félicitations (GG) avec pixel art, dégradés de couleurs et récompenses !

## 🎮 Fonctionnalités

- **Vague de GG personnalisée** : Lance une vague de félicitations pour un joueur spécifique
- **Pixel Art** : Affiche la tête du joueur en pixel art coloré dans le chat
- **Messages personnalisables** : Titre, message de remerciement, lien boutique configurables
- **Dégradé de couleur animé** : Les messages "GG" sont automatiquement colorés avec un dégradé arc-en-ciel qui évolue dans le temps
- **Système de récompenses** : Les joueurs qui disent "GG" reçoivent des récompenses (argent, items, commandes)
- **Durée configurable** : Définissez la durée de la vague
- **Entièrement configurable** : Tous les paramètres sont modifiables dans le fichier `config.yml`

## 📋 Prérequis

- **Serveur Minecraft** : Spigot, Paper ou fork compatible
- **Version** : 1.21.11
- **Java** : 17 ou supérieure
- **Vault** (optionnel) : Pour les récompenses en argent

## 📦 Installation

1. Téléchargez le fichier `GGWave.jar`
2. Placez-le dans le dossier `plugins` de votre serveur
3. Redémarrez le serveur
4. Le fichier `config.yml` sera créé automatiquement dans `plugins/GGWave/`
5. Configurez le plugin selon vos besoins
6. Utilisez `/ggwave reload` pour recharger la configuration

## 🎯 Commandes

| Commande | Description | Permission |
|----------|-------------|------------|
| `/ggwave start <joueur>` | Lance une vague de GG pour le joueur spécifié | `ggwave.start` |
| `/ggwave stop` | Arrête la vague en cours | `ggwave.stop` |
| `/ggwave info` | Affiche les informations de la vague active | `ggwave.info` |
| `/ggwave reload` | Recharge la configuration | `ggwave.reload` |
| `/ggwave help` | Affiche l'aide | - |

**Aliases** : `/gwave`, `/ggw`

## 🔑 Permissions

| Permission | Description | Défaut |
|------------|-------------|--------|
| `ggwave.*` | Accès à toutes les commandes | OP |
| `ggwave.start` | Lancer une vague | OP |
| `ggwave.stop` | Arrêter une vague | OP |
| `ggwave.info` | Voir les informations | OP |
| `ggwave.reload` | Recharger la config | OP |

## ⚙️ Configuration

Le fichier `config.yml` vous permet de personnaliser tous les aspects du plugin :

```yaml
wave:
  # Durée de la vague en secondes
  duration: 300
  
  # Messages affichés
  title: "&6&l✦ GG WAVE ✦"
  shop-message: "&aMerci pour votre achat sur notre boutique !"
  shop-link: "https://votreboutique.com"
  
  # Couleurs du dégradé (format hexadécimal)
  gradient-colors:
    - "#FF0000"  # Rouge
    - "#FF7F00"  # Orange
    - "#FFFF00"  # Jaune
    - "#00FF00"  # Vert
    - "#0000FF"  # Bleu
    - "#4B0082"  # Indigo
    - "#9400D3"  # Violet
  
  # Configuration du pixel art
  pixel-art:
    enabled: true
    size: 8
  
  # Récompenses
  rewards:
    money: 100  # Nécessite Vault
    item:
      material: "DIAMOND"
      amount: 1
    commands:
      - "give %player% minecraft:emerald 5"
```

## 🎨 Comment ça marche ?

1. **Lancement** : Un admin utilise `/ggwave start <joueur>`
2. **Affichage** : Un message élaboré apparaît dans le chat avec :
    - Le pixel art de la tête du joueur
    - Un message de remerciement personnalisé
    - Le lien vers la boutique
3. **Participation** : Les joueurs tapent "GG" dans le chat
4. **Transformation** : Le mot "gg" est automatiquement transformé en "GG" avec un dégradé de couleur animé
5. **Récompenses** : Chaque joueur reçoit sa récompense une seule fois
6. **Fin** : Après la durée configurée, la vague se termine automatiquement

## 🛠️ Compilation depuis les sources

Si vous voulez compiler le plugin vous-même :

```bash
# Cloner le projet
git clone <votre-repo>
cd GGWave

# Compiler avec Maven
mvn clean package

# Le fichier JAR sera dans target/GGWave-1.0.0.jar
```

## 🔧 Structure du projet

```
GGWave/
├── src/main/java/org/lulunoel2016/gGWave/
│   ├── GGWave.java                    # Classe principale
│   ├── commands/
│   │   └── GGWaveCommand.java         # Gestion des commandes
│   ├── listeners/
│   │   └── ChatListener.java          # Écoute des messages du chat
│   ├── managers/
│   │   └── GGWaveManager.java         # Gestion de la logique des vagues
│   └── utils/
│       ├── ColorGradient.java         # Génération de dégradés
│       └── PixelArtRenderer.java      # Rendu du pixel art
├── src/main/resources/
│   ├── config.yml                     # Configuration
│   └── plugin.yml                     # Métadonnées du plugin
└── pom.xml                            # Configuration Maven
```

## 💡 Exemples d'utilisation

### Lancer une vague pour un joueur qui a acheté un rang
```
/ggwave start Notch
```

### Arrêter une vague si nécessaire
```
/ggwave stop
```

### Voir combien de personnes ont participé
```
/ggwave info
```

## 🐛 Problèmes connus

- Le pixel art nécessite une connexion internet pour télécharger les skins
- Si le téléchargement échoue, un pixel art de fallback est utilisé

## 📝 Support et contribution

Pour signaler un bug ou proposer une amélioration, n'hésitez pas à ouvrir une issue !

## 📄 Licence

Ce plugin est distribué sous licence MIT. Vous êtes libre de le modifier et de le redistribuer.

## 🙏 Crédits

- Développé pour Minecraft 1.21.11
- Utilise l'API Spigot
- Skins fournis par Crafatar

---

**Bon jeu et que les GG pleuvent ! 🎉**