# 🚀 Démarrage Rapide - GGWave Plugin

## Installation en 3 étapes

### 1️⃣ Installation
```bash
# Téléchargez GGWave-1.0.0.jar
# Placez-le dans le dossier plugins/ de votre serveur
# Redémarrez le serveur
```

### 2️⃣ Configuration Basique
Éditez `plugins/GGWave/config.yml` :
```yaml
wave:
  duration: 300  # 5 minutes
  shop-link: "https://votre-boutique.com"  # CHANGEZ CECI !
```

### 3️⃣ Utilisation
```
/ggwave start <pseudo_joueur>
```

C'est tout ! 🎉

---

## Exemple Complet

1. Un joueur achète un VIP sur votre boutique
2. Vous tapez : `/ggwave start Notch`
3. Le serveur affiche :
    - 🎨 La tête de Notch en pixel art
    - 💬 Un message de remerciement
    - 🔗 Le lien de votre boutique
4. Les joueurs tapent "GG" dans le chat
5. Leurs messages sont transformés avec un dégradé arc-en-ciel ✨
6. Ils reçoivent automatiquement des récompenses 🎁

---

## Commandes Essentielles

| Commande | Description |
|----------|-------------|
| `/ggwave start <joueur>` | Lancer une vague |
| `/ggwave stop` | Arrêter la vague |
| `/ggwave info` | Voir les stats |
| `/ggwave reload` | Recharger la config |

---

## Personnalisation Rapide

### Changer la durée
```yaml
duration: 600  # 10 minutes au lieu de 5
```

### Changer les récompenses
```yaml
rewards:
  money: 200        # 200$ au lieu de 100$
  item:
    material: "EMERALD"
    amount: 5       # 5 émeraudes
```

### Changer les couleurs
```yaml
gradient-colors:
  - "#FF0000"  # Rouge
  - "#00FF00"  # Vert
  - "#0000FF"  # Bleu
```

---

## Besoin d'aide ?

📖 Lisez le guide complet : `CONFIGURATION.md`  
📝 Documentation complète : `README.md`  
🐛 Problème ? Vérifiez les logs dans `logs/latest.log`

---

**Bon jeu ! 🎮**