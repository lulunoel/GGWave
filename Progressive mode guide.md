# 🎨 Mode Progressif - Guide Visuel

## Comment ça marche ?

Le mode progressif fait passer les **GG** par toutes les couleurs que vous configurez, dans l'ordre, sur toute la durée de la vague.

## Exemple Simple

### Configuration
```yaml
wave:
  duration: 300  # 5 minutes
  gradient-mode: "progressive"
  gradient-colors:
    - "#FF0000"  # 1. Rouge
    - "#FFFF00"  # 2. Jaune
    - "#00FF00"  # 3. Vert
    - "#0000FF"  # 4. Bleu
```

### Timeline de la vague
```
┌─────────────────────────────────────────────────────────┐
│ Minute 0:00  →  GG en ROUGE                             │
│                                                          │
│ Minute 1:15  →  GG en ORANGE (transition rouge→jaune)   │
│                                                          │
│ Minute 2:30  →  GG en JAUNE                             │
│                                                          │
│ Minute 3:45  →  GG en VERT CLAIR (transition jaune→vert)│
│                                                          │
│ Minute 5:00  →  GG en BLEU                              │
└─────────────────────────────────────────────────────────┘
```

## Formule

Pour une vague de **N minutes** avec **X couleurs** :

- Chaque couleur dure environ : `N / (X-1)` minutes
- Le plugin interpole entre chaque paire de couleurs

Exemple avec 5 minutes et 4 couleurs :
- Rouge → Jaune : minutes 0 à 2.5
- Jaune → Vert : minutes 2.5 à 5

## Comparaison : 2 couleurs vs 7 couleurs

### Avec 2 couleurs
```yaml
gradient-colors:
  - "#FF0000"  # Rouge
  - "#0000FF"  # Bleu
```
```
0%    25%   50%   75%   100%
Rouge → Violet → Bleu
```
Transition simple et directe

### Avec 7 couleurs (arc-en-ciel)
```yaml
gradient-colors:
  - "#FF0000"  # Rouge
  - "#FF7F00"  # Orange
  - "#FFFF00"  # Jaune
  - "#00FF00"  # Vert
  - "#00FFFF"  # Cyan
  - "#0000FF"  # Bleu
  - "#8B00FF"  # Violet
```
```
0%    17%   33%   50%   67%   83%   100%
Rouge → Orange → Jaune → Vert → Cyan → Bleu → Violet
```
Transition riche avec beaucoup de nuances

## Préréglages recommandés

### 🔥 Intensité croissante (Feu)
```yaml
gradient-colors:
  - "#8B0000"  # Marron
  - "#FF0000"  # Rouge
  - "#FF4500"  # Orange
  - "#FFA500"  # Orange vif
  - "#FFD700"  # Or
```
Effet : Le feu s'intensifie au fil de la vague

### 🌊 Profondeur océanique
```yaml
gradient-colors:
  - "#000033"  # Noir bleuté (profondeur)
  - "#000080"  # Bleu marine
  - "#0000FF"  # Bleu
  - "#4169E1"  # Bleu royal
  - "#87CEEB"  # Bleu ciel (surface)
```
Effet : Remontée des profondeurs vers la surface

### 🌅 Coucher de soleil
```yaml
gradient-colors:
  - "#FF4500"  # Orange-rouge
  - "#FF6347"  # Tomate
  - "#FF7F50"  # Corail
  - "#FFA500"  # Orange
  - "#FFD700"  # Or
  - "#FFFFE0"  # Jaune clair
```
Effet : Magnifique transition de coucher de soleil

### 💎 Premium (Or → Diamant)
```yaml
gradient-colors:
  - "#FFD700"  # Or
  - "#F0E68C"  # Kaki
  - "#E0FFFF"  # Cyan clair
  - "#B0E0E6"  # Bleu poudre
  - "#87CEEB"  # Bleu ciel
  - "#FFFFFF"  # Blanc (diamant)
```
Effet : De l'or au diamant, passage par tous les tons précieux

### 🎮 Gaming RGB
```yaml
gradient-colors:
  - "#FF0000"  # Rouge
  - "#FF00FF"  # Magenta
  - "#0000FF"  # Bleu
  - "#00FFFF"  # Cyan
  - "#00FF00"  # Vert
```
Effet : Style RGB gaming

### 🌈 Arc-en-ciel complet
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
Effet : Toutes les couleurs de l'arc-en-ciel !

## Astuces

### Pour un effet subtil
Utilisez 2-3 couleurs proches :
```yaml
gradient-colors:
  - "#FF0000"  # Rouge
  - "#FF3333"  # Rouge clair
  - "#FF6666"  # Rose
```

### Pour un effet spectaculaire
Utilisez 5-7 couleurs contrastées :
```yaml
gradient-colors:
  - "#FF0000"
  - "#00FF00"
  - "#0000FF"
  - "#FFFF00"
  - "#FF00FF"
```

### Pour un thème cohérent
Choisissez des couleurs d'une même palette :
```yaml
# Thème "Forêt"
gradient-colors:
  - "#2F4F2F"  # Vert foncé
  - "#228B22"  # Vert forêt
  - "#32CD32"  # Vert citron
  - "#90EE90"  # Vert clair
```

## Différence avec le mode "per-letter"

### Mode "per-letter"
- Les deux lettres ont des couleurs DIFFÉRENTES
- Animation cyclique toutes les 5 secondes
- Effet dynamique

### Mode "progressive"
- Les deux lettres ont la MÊME couleur
- Évolution linéaire sur toute la vague
- Effet élégant et fluide

## Conseil final

**Plus de couleurs = Transition plus riche et fluide**

Commencez avec 3-4 couleurs, puis ajoutez-en pour enrichir l'effet !

---

**Testez différentes combinaisons et trouvez votre style ! 🎨**