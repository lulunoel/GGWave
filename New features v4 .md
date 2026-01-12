# 🆕 Nouvelles Fonctionnalités v4

## 1. 📋 File d'attente des vagues

### Comment ça marche ?

Quand une vague est déjà en cours et qu'une nouvelle est lancée, elle se met **automatiquement en file d'attente** !

```
[Vague 1 en cours] 
     ↓
/ggwave start Player2 → Ajouté à la file
     ↓
/ggwave start Player3 → Ajouté à la file
     ↓
[Vague 1 se termine]
     ↓
[Pause de 3 secondes]
     ↓
[Vague 2 démarre automatiquement]
```

### Avantages

✅ **Plus besoin d'attendre** : Lancez les vagues quand vous voulez
✅ **Traitement automatique** : Les vagues se lancent une par une
✅ **Notifications** : Les joueurs voient qu'ils sont en attente
✅ **Intelligent** : Skip les joueurs déconnectés

### Commandes

```bash
# Lancer une vague (ajoute à la file si une est en cours)
/ggwave start <joueur>

# Voir la file d'attente
/ggwave info
```

### Exemple de sortie

```
▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
Informations GG Wave:

Statut: Active
Temps restant: 245s
Participants: 12

File d'attente: 3 vague(s)
  1. Player2
  2. Player3
  3. Player4
▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
```

### Message dans le chat

Quand une vague est ajoutée à la file :
```
[GG Wave] Player2 a été ajouté à la file d'attente !
```

Entre deux vagues :
```
▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
Lancement de la prochaine vague en file d'attente...
▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
```

---

## 2. 🌈 Mode de dégradé progressif

### Deux modes disponibles

#### Mode "per-letter" (classique)
Chaque lettre a une couleur différente qui change constamment.

```yaml
gradient-mode: "per-letter"
gradient-colors:
  - "#FF0000"  # Rouge
  - "#00FF00"  # Vert
  - "#0000FF"  # Bleu
```

Résultat : **G** (rouge) **G** (vert) → **G** (vert) **G** (bleu) → ...

#### Mode "progressive" (nouveau) ⭐
Les deux lettres ont la même couleur qui évolue progressivement à travers **TOUTES** les couleurs au fil de la vague.

```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#FF0000"  # Rouge
  - "#FFFF00"  # Jaune
  - "#00FF00"  # Vert
  - "#0000FF"  # Bleu
```

Timeline d'une vague de 5 minutes :
```
0:00  → GG (rouge)
1:15  → GG (orange, transition rouge→jaune)
2:30  → GG (jaune)
3:45  → GG (vert clair, transition jaune→vert)
5:00  → GG (bleu, fin de la transition)
```

Le plugin passe **progressivement** par toutes les couleurs dans l'ordre !

### Configuration

```yaml
wave:
  # Mode de dégradé
  gradient-mode: "progressive"  # ou "per-letter"
  
  # En mode "progressive", utilise TOUTES les couleurs
  gradient-colors:
    - "#FF0000"  # Couleur 1
    - "#FFFF00"  # Couleur 2
    - "#00FF00"  # Couleur 3
    - "#0000FF"  # Couleur 4
    # Vous pouvez en ajouter autant que vous voulez !
```

### Exemples de configurations

#### Arc-en-ciel complet (7 couleurs)
```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#FF0000"  # Rouge
  - "#FF7F00"  # Orange
  - "#FFFF00"  # Jaune
  - "#00FF00"  # Vert
  - "#0000FF"  # Bleu
  - "#4B0082"  # Indigo
  - "#9400D3"  # Violet
```
→ Passe par toutes les couleurs de l'arc-en-ciel progressivement !

#### Feu intense (4 couleurs)
```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#8B0000"  # Rouge foncé
  - "#FF0000"  # Rouge vif
  - "#FF4500"  # Orange-rouge
  - "#FFD700"  # Or
```
→ Effet de feu qui s'intensifie !

#### Océan profond (5 couleurs)
```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#000080"  # Bleu marine
  - "#0000FF"  # Bleu
  - "#1E90FF"  # Bleu dodger
  - "#00BFFF"  # Bleu ciel
  - "#87CEEB"  # Bleu clair
```
→ Des profondeurs vers la surface !

#### Coucher de soleil (6 couleurs)
```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#FF4500"  # Orange-rouge
  - "#FF6347"  # Tomate
  - "#FF7F50"  # Corail
  - "#FFA500"  # Orange
  - "#FFD700"  # Or
  - "#FFFF00"  # Jaune
```
→ Magnifique transition de coucher de soleil !

#### Nuit étoilée (4 couleurs)
```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#000000"  # Noir
  - "#191970"  # Bleu nuit
  - "#483D8B"  # Violet ardoise
  - "#9370DB"  # Violet moyen
```
→ De la nuit noire aux premières lueurs !

#### Simple 2 couleurs
```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#FF0000"  # Rouge
  - "#0000FF"  # Bleu
```
→ Transition simple et efficace rouge → violet → bleu

### Comportement

**Mode per-letter** :
- Animation cyclique (5 secondes)
- Utilise toutes les couleurs de la liste
- Les lettres ont des couleurs différentes
- Effet dynamique et flashy

**Mode progressive** :
- Évolution linéaire sur toute la durée de la vague
- Utilise **TOUTES** les couleurs de la liste
- Les deux lettres ont la même couleur
- Passe par chaque couleur dans l'ordre
- Transition fluide entre chaque couleur
- Effet calme et élégant

### Exemples détaillés

**Vague de 5 minutes avec 4 couleurs** :
```yaml
gradient-colors:
  - "#FF0000"  # Rouge
  - "#FFFF00"  # Jaune  
  - "#00FF00"  # Vert
  - "#0000FF"  # Bleu
```

Timeline :
```
0:00  → Rouge pur
0:45  → Rouge-orange (transition 1→2)
1:15  → Orange (milieu transition 1→2)
2:00  → Jaune-orange
2:30  → Jaune pur
3:00  → Jaune-vert (transition 2→3)
3:45  → Vert (milieu transition 2→3)
4:15  → Vert-cyan
5:00  → Bleu pur
```

**Plus vous ajoutez de couleurs, plus la transition est riche !**

### Comparaison visuelle

**3 couleurs vs 7 couleurs** :

Avec 3 couleurs (Rouge, Vert, Bleu) :
- 0% → Rouge
- 50% → Vert
- 100% → Bleu
- Transitions rapides entre couleurs

Avec 7 couleurs (arc-en-ciel) :
- Transitions plus douces
- Plus de nuances
- Effet plus fluide et professionnel

### Combinaison avec les styles

Les deux modes fonctionnent avec tous les styles :

```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#FF0000"
  - "#0000FF"

gg-style:
  bold: true
  italic: true
  underline: false
```

Résultat : **GG** en gras et italique, couleur évoluant de rouge à bleu

---

## Cas d'usage recommandés

### File d'attente
- ✅ Boutiques avec beaucoup d'achats simultanés
- ✅ Événements avec plusieurs gagnants
- ✅ Serveurs avec beaucoup de joueurs
- ✅ Cérémonies de récompenses

### Mode "progressive"
- ✅ Événements VIP/Premium (effet élégant)
- ✅ Vagues longues (5-10 minutes)
- ✅ Thème cohérent (ex: rouge→jaune pour "feu")
- ✅ Meilleure lisibilité

### Mode "per-letter"
- ✅ Événements festifs (effet dynamique)
- ✅ Vagues courtes (1-3 minutes)
- ✅ Maximum d'impact visuel
- ✅ Style arcade/gaming

---

## Configuration complète exemple

```yaml
wave:
  duration: 300
  
  # File d'attente (automatique)
  # Pas de configuration nécessaire !
  
  # Mode progressif avec dégradé or→blanc
  gradient-mode: "progressive"
  gradient-colors:
    - "#FFD700"  # Or
    - "#FFFFFF"  # Blanc
  
  gg-style:
    bold: true
    italic: true
    underline: false
```

---

**Testez les deux modes et choisissez votre préféré ! 🎨**
