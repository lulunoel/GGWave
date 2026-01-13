# ⚠️ IMPORTANT - Support Folia

## Vous utilisez Folia !

## Différences avec Spigot/Paper

Folia utilise un système de threading différent qui nécessite :
- Utilisation de `GlobalRegionScheduler` au lieu de `BukkitScheduler`
- Utilisation de `EntityScheduler` pour les actions sur les joueurs
- Les commandes doivent être exécutées sur le thread approprié

## Corrections apportées

✅ **Scheduling des tâches** : Utilise maintenant `Bukkit.getGlobalRegionScheduler()`
✅ **Distribution des items** : Utilise `player.getScheduler()` pour être thread-safe
✅ **Exécution des commandes** : Les commandes s'exécutent sur le thread global

## Installation

1. Recompilez le plugin avec les fichiers corrigés :
```bash
mvn clean package
```

2. Remplacez l'ancien JAR par le nouveau dans `plugins/`

3. Redémarrez le serveur

## Test

```
/ggwave start <joueur>
```

Puis dans le chat, tapez "gg" - vous devriez maintenant recevoir vos récompenses sans erreur !

## Notes importantes pour Folia

- Les performances peuvent varier selon la région du joueur
- Les tâches sont maintenant distribuées sur différents threads
- Le plugin est maintenant thread-safe et compatible Folia

---


**Version compatible : Folia 1.21.11**
