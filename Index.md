# 🎮 GGWave Plugin - Index des fichiers

Bienvenue dans le plugin GGWave pour Minecraft 1.21.11 !

## 📂 Structure du projet

```
GGWave-Plugin/
├── 📄 README.md              - Documentation complète du plugin
├── 📄 QUICKSTART.md          - Guide de démarrage rapide (5 minutes)
├── 📄 CONFIGURATION.md       - Guide de configuration détaillé
├── 📄 DOCUMENTATION.html     - Documentation visuelle (ouvrir dans un navigateur)
├── 🔧 build.sh               - Script de compilation
├── 📦 pom.xml                - Configuration Maven
├── 🚫 .gitignore             - Fichiers à ignorer par Git
│
└── src/
    └── main/
        ├── java/org/lulunoel2016/gGWave/
        │   ├── GGWave.java                    - Classe principale
        │   ├── commands/
        │   │   └── GGWaveCommand.java         - Gestion des commandes
        │   ├── listeners/
        │   │   └── ChatListener.java          - Écoute du chat
        │   ├── managers/
        │   │   └── GGWaveManager.java         - Logique des vagues
        │   └── utils/
        │       ├── ColorGradient.java         - Dégradés de couleur
        │       └── PixelArtRenderer.java      - Rendu pixel art
        │
        └── resources/
            ├── config.yml                      - Configuration du plugin
            └── plugin.yml                      - Métadonnées Spigot
```

## 🚀 Démarrage rapide

### 1. Compilation
```bash
# Avec le script fourni
./build.sh

# Ou avec Maven directement
mvn clean package
```

Le fichier JAR sera généré dans `target/GGWave-1.0.0.jar`

### 2. Installation
1. Copiez `GGWave-1.0.0.jar` dans le dossier `plugins/` de votre serveur
2. Redémarrez le serveur
3. Éditez `plugins/GGWave/config.yml`
4. Utilisez `/ggwave reload`

### 3. Utilisation
```
/ggwave start <joueur>
```

## 📚 Documentation

| Fichier | Description |
|---------|-------------|
| **README.md** | Documentation complète avec toutes les informations |
| **QUICKSTART.md** | Guide rapide pour démarrer en 5 minutes |
| **CONFIGURATION.md** | Explication détaillée de tous les paramètres |
| **DOCUMENTATION.html** | Version visuelle de la documentation (à ouvrir dans un navigateur) |

## 🎯 Fonctionnalités principales

- ✨ **Vagues de GG personnalisées** avec pixel art et animations
- 🎨 **Dégradés de couleur** animés pour les messages "GG"
- 🎁 **Système de récompenses** (argent, items, commandes)
- ⚙️ **Entièrement configurable** via `config.yml`
- 🖼️ **Pixel art automatique** de la tête du joueur
- ⏱️ **Durée personnalisable** pour chaque vague

## ⌨️ Commandes essentielles

```
/ggwave start <joueur>  - Lancer une vague
/ggwave stop            - Arrêter la vague
/ggwave info            - Voir les statistiques
/ggwave reload          - Recharger la config
/ggwave help            - Afficher l'aide
```

Alias disponibles : `/gwave`, `/ggw`

## 🔑 Permissions

- `ggwave.*` - Toutes les permissions
- `ggwave.start` - Lancer une vague
- `ggwave.stop` - Arrêter une vague
- `ggwave.reload` - Recharger la config
- `ggwave.info` - Voir les infos

## 📋 Prérequis

- Serveur : Spigot, Paper ou fork (1.21.11)
- Java : 17+
- Maven : Pour la compilation
- Vault : Optionnel (pour les récompenses en argent)

## 🎨 Exemple de configuration

```yaml
wave:
  duration: 300
  title: "&6&l✦ GG WAVE ✦"
  shop-message: "&aMerci pour votre achat !"
  shop-link: "https://votreboutique.com"
  
  gradient-colors:
    - "#FF0000"
    - "#FFFF00"
    - "#00FF00"
    - "#0000FF"
  
  pixel-art:
    enabled: true
    size: 8
  
  rewards:
    money: 100
    item:
      material: "DIAMOND"
      amount: 1
    commands:
      - "give %player% minecraft:emerald 5"
```

## 🛠️ Développement

### Structure du code

- **GGWave.java** : Point d'entrée du plugin
- **GGWaveCommand.java** : Gestion des commandes et tab completion
- **ChatListener.java** : Interception des messages du chat
- **GGWaveManager.java** : Logique métier des vagues
- **ColorGradient.java** : Génération de dégradés de couleur
- **PixelArtRenderer.java** : Conversion des skins en pixel art

### Compiler

```bash
# Méthode 1 : Script fourni
chmod +x build.sh
./build.sh

# Méthode 2 : Maven
mvn clean package

# Résultat
target/GGWave-1.0.0.jar
```

## 🐛 Problèmes courants

### Le plugin ne se charge pas
- Vérifiez la version de votre serveur (1.21.11)
- Vérifiez Java 17+
- Consultez `logs/latest.log`

### Le pixel art ne s'affiche pas
- Vérifiez la connexion internet du serveur
- Essayez de réduire `pixel-art.size` à 8

### Les couleurs ne fonctionnent pas
- Format hexadécimal requis : `#RRGGBB`
- Minimum 2 couleurs dans `gradient-colors`

### Les récompenses ne sont pas données
- Pour l'argent : Installez Vault
- Pour les items : Vérifiez le nom du matériau
- Pour les commandes : Testez-les manuellement

## 📦 Fichiers téléchargeables

- **GGWave-Plugin/** : Projet source complet
- **GGWave-Plugin-Source.zip** : Archive du projet

## 📞 Support

Pour toute question :
1. Consultez d'abord les fichiers README et CONFIGURATION
2. Vérifiez les logs du serveur
3. Testez avec la configuration par défaut

## 📄 Licence

Ce plugin est distribué sous licence MIT. Vous êtes libre de le modifier et de le redistribuer.

---

**Développé pour Minecraft 1.21.11**  
**Version : 1.0.0**  
**Auteur : Lulunoel2016**

🎉 **Bon jeu et que les GG pleuvent !** 🎉