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
Les deux lettres ont la même couleur qui évolue progressivement au fil de la vague.

```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#FF0000"  # Rouge (début)
  - "#0000FF"  # Bleu (fin)
```

Timeline :
```
Début de la vague    : GG (rouge)
Milieu de la vague   : GG (violet)
Fin de la vague      : GG (bleu)
```

### Configuration

```yaml
wave:
  # Mode de dégradé
  gradient-mode: "progressive"  # ou "per-letter"
  
  # Couleurs du dégradé
  gradient-colors:
    - "#FF0000"  # Couleur de départ
    - "#0000FF"  # Couleur d'arrivée
    # Note: En mode "progressive", seules les 2 premières couleurs sont utilisées
```

### Exemples de configurations

#### Rouge → Jaune (chaleur croissante)
```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#FF0000"  # Rouge
  - "#FFFF00"  # Jaune
```

#### Bleu → Rose (vibe chill)
```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#0000FF"  # Bleu
  - "#FF1493"  # Rose
```

#### Vert → Cyan (nature)
```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#00FF00"  # Vert
  - "#00FFFF"  # Cyan
```

#### Or → Blanc (premium)
```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#FFD700"  # Or
  - "#FFFFFF"  # Blanc
```

#### Noir → Or (élégance)
```yaml
gradient-mode: "progressive"
gradient-colors:
  - "#000000"  # Noir
  - "#FFD700"  # Or
```

### Comportement

**Mode per-letter** :
- Animation cyclique (5 secondes)
- Utilise toutes les couleurs de la liste
- Les lettres ont des couleurs différentes
- Effet dynamique et flashy

**Mode progressive** :
- Évolution linéaire sur toute la durée de la vague
- Utilise seulement les 2 premières couleurs
- Les deux lettres ont la même couleur
- Effet calme et élégant

### Comparaison visuelle

**Vague de 5 minutes avec mode "progressive" (Rouge → Bleu)** :
```
0:00  → GG (rouge pur)
1:15  → GG (rouge-violet)
2:30  → GG (violet)
3:45  → GG (bleu-violet)
5:00  → GG (bleu pur)
```

**Même vague avec mode "per-letter"** :
```
Toutes les 5 secondes, les couleurs tournent :
G (rouge) G (vert) → G (vert) G (bleu) → G (bleu) G (rouge) → ...
```

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
