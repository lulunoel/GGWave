package org.lulunoel2016.gGWave.managers;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.lulunoel2016.gGWave.GGWave;
import org.lulunoel2016.gGWave.hooks.VaultHook;
import org.lulunoel2016.gGWave.utils.ColorGradient;
import org.lulunoel2016.gGWave.utils.PixelArtRenderer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GGWaveManager {

    private final GGWave plugin;
    private boolean waveActive = false;
    private long waveStartTime;
    private int waveDuration; // en secondes
    private Set<UUID> playersWhoSaidGG;
    private ScheduledTask waveTask;
    private ScheduledTask bossbarTask;
    private BossBar activeBossBar;
    private Player currentWavePlayer; // Joueur pour lequel la vague est lancée

    // Cooldown pour éviter le spam
    private final Map<UUID, Long> lastGGTime = new HashMap<>();
    private int ggCooldown = 3; // secondes entre chaque GG

    // File d'attente pour les vagues
    private final Queue<WaveRequest> waveQueue;

    // Messages variés
    private List<String> randomGGMessages;

    // Configuration
    private List<String> gradientColors;
    private String shopMessage;
    private String shopLink;
    private String titleMessage;
    private Map<String, Object> rewards;
    private boolean pixelArtEnabled;
    private int pixelArtSize;

    // Style du texte GG
    private boolean ggBold;
    private boolean ggItalic;
    private boolean ggUnderline;

    // Mode de dégradé
    private String gradientMode; // "per-letter" ou "progressive"

    // Effets visuels
    private boolean bossbarEnabled;
    private boolean titleEnabled;
    private boolean soundEnabled;

    // Classe interne pour représenter une demande de vague
    private static class WaveRequest {
        final Player targetPlayer;
        final Player sender;

        WaveRequest(Player targetPlayer, Player sender) {
            this.targetPlayer = targetPlayer;
            this.sender = sender;
        }
    }

    public GGWaveManager(GGWave plugin) {
        this.plugin = plugin;
        this.playersWhoSaidGG = ConcurrentHashMap.newKeySet();
        this.waveQueue = new java.util.concurrent.ConcurrentLinkedQueue<>();
        loadConfiguration();
    }

    public void loadConfiguration() {
        FileConfiguration config = plugin.getConfig();

        // Durée de la vague (en secondes)
        waveDuration = config.getInt("wave.duration", 300);

        // Messages
        shopMessage = ChatColor.translateAlternateColorCodes('&',
                config.getString("wave.shop-message", "&aMerci pour votre achat !"));
        shopLink = config.getString("wave.shop-link", "https://votreboutique.com");
        titleMessage = ChatColor.translateAlternateColorCodes('&',
                config.getString("wave.title", "&6&l✦ GG WAVE ✦"));

        // Couleurs du dégradé pour "GG"
        gradientColors = config.getStringList("wave.gradient-colors");
        if (gradientColors.isEmpty()) {
            gradientColors = Arrays.asList("#FF0000", "#FF7F00", "#FFFF00", "#00FF00", "#0000FF", "#4B0082", "#9400D3");
        }

        // Mode de dégradé
        gradientMode = config.getString("wave.gradient-mode", "per-letter");
        if (!gradientMode.equals("per-letter") && !gradientMode.equals("progressive")) {
            plugin.getLogger().warning("Mode de dégradé invalide: " + gradientMode + ". Utilisation de 'per-letter' par défaut.");
            gradientMode = "per-letter";
        }

        // Style du texte GG
        ggBold = config.getBoolean("wave.gg-style.bold", true);
        ggItalic = config.getBoolean("wave.gg-style.italic", false);
        ggUnderline = config.getBoolean("wave.gg-style.underline", false);

        // Effets visuels
        bossbarEnabled = config.getBoolean("wave.bossbar.enabled", true);
        titleEnabled = config.getBoolean("wave.effects.title", true);
        soundEnabled = config.getBoolean("wave.effects.sound", true);

        // Messages variés
        randomGGMessages = config.getStringList("wave.random-messages");
        if (randomGGMessages.isEmpty()) {
            randomGGMessages = Arrays.asList(
                    "&aGG {player} !",
                    "&aFélicitations {player} !",
                    "&aBravo {player} pour ton achat !",
                    "&aMerci {player} !",
                    "&aSuper {player} !"
            );
        }

        // Cooldown
        ggCooldown = config.getInt("wave.gg-cooldown", 3);

        // Configuration du pixel art
        pixelArtEnabled = config.getBoolean("wave.pixel-art.enabled", true);
        pixelArtSize = config.getInt("wave.pixel-art.size", 8);

        // Récompenses
        rewards = new HashMap<>();
        ConfigurationSection rewardsSection = config.getConfigurationSection("wave.rewards");
        if (rewardsSection != null) {
            for (String key : rewardsSection.getKeys(false)) {
                rewards.put(key, rewardsSection.get(key));
            }
        } else {
            // Récompenses par défaut
            rewards.put("money", 100);
            rewards.put("item.material", "DIAMOND");
            rewards.put("item.amount", 1);
        }
    }

    public void reloadConfiguration() {
        loadConfiguration();
    }

    public void startWave(Player targetPlayer, Player sender) {
        if (waveActive) {
            // Ajouter à la file d'attente
            waveQueue.offer(new WaveRequest(targetPlayer, sender));

            String message = ChatColor.YELLOW + "Une vague est déjà en cours. " +
                    ChatColor.GOLD + targetPlayer.getName() + ChatColor.YELLOW +
                    " a été ajouté à la file d'attente (" + waveQueue.size() + " en attente)";

            if (sender != null) {
                sender.sendMessage(message);
            }
            Bukkit.broadcastMessage(ChatColor.GRAY + "[GG Wave] " + targetPlayer.getName() +
                    " a été ajouté à la file d'attente !");
            return;
        }

        waveActive = true;
        waveStartTime = System.currentTimeMillis();
        playersWhoSaidGG.clear();
        currentWavePlayer = targetPlayer; // Sauvegarder le joueur

        // Diffuser le message initial avec pixel art et informations
        broadcastWaveStart(targetPlayer);

        // Afficher le titre et jouer le son
        showTitleAndSound(targetPlayer);

        // Démarrer la bossbar
        startBossBar(targetPlayer);

        // Planifier l'arrêt automatique de la vague (compatible Folia)
        waveTask = Bukkit.getGlobalRegionScheduler().runDelayed(plugin, (task) -> {
            stopWave();
            // Lancer la prochaine vague si il y en a
            processNextWaveInQueue();
        }, waveDuration * 20L);

        if (sender != null) {
            sender.sendMessage(ChatColor.GREEN + "Vague de GG lancée pour " + targetPlayer.getName() + " !");
        }
    }

    private void processNextWaveInQueue() {
        if (!waveQueue.isEmpty()) {
            WaveRequest nextWave = waveQueue.poll();

            if (nextWave.targetPlayer.isOnline()) {
                // Attendre 3 secondes avant de lancer la prochaine vague
                Bukkit.getGlobalRegionScheduler().runDelayed(plugin, (task) -> {
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Lancement de la prochaine vague en file d'attente...");
                    Bukkit.broadcastMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                    Bukkit.broadcastMessage("");

                    startWave(nextWave.targetPlayer, nextWave.sender);
                }, 60L); // 3 secondes (60 ticks)
            } else {
                // Le joueur est déconnecté, passer au suivant
                if (nextWave.sender != null && nextWave.sender.isOnline()) {
                    nextWave.sender.sendMessage(ChatColor.RED + nextWave.targetPlayer.getName() +
                            " s'est déconnecté, passage à la vague suivante...");
                }
                processNextWaveInQueue();
            }
        }
    }

    private void broadcastWaveStart(Player targetPlayer) {
        // Ligne de séparation supérieure
        String separator = ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";
        Bukkit.broadcastMessage(separator);
        Bukkit.broadcastMessage("");

        // Titre centré
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "                    " + titleMessage));
        Bukkit.broadcastMessage("");

        // Pixel art avec message à droite (si activé)
        if (pixelArtEnabled) {
            // Générer le pixel art de manière asynchrone pour ne pas bloquer le serveur
            Bukkit.getAsyncScheduler().runNow(plugin, (task) -> {
                List<String> pixelArt = PixelArtRenderer.renderPlayerHead(targetPlayer, pixelArtSize);

                // Une fois généré, afficher sur le thread principal
                Bukkit.getGlobalRegionScheduler().run(plugin, (mainTask) -> {
                    displayWaveContent(pixelArt, targetPlayer);
                });
            });
        } else {
            // Si pas de pixel art, afficher seulement les messages
            displayWaveContentNoPixelArt(targetPlayer);
        }
    }

    private void displayWaveContent(List<String> pixelArt, Player targetPlayer) {
        // Préparer les messages à afficher à droite
        List<String> rightMessages = new ArrayList<>();
        rightMessages.add(ChatColor.translateAlternateColorCodes('&', "&e⭐ " + targetPlayer.getName() + " &fa effectué un achat !"));
        rightMessages.add("");
        rightMessages.add(ChatColor.translateAlternateColorCodes('&', shopMessage));
        rightMessages.add("");
        rightMessages.add(ChatColor.translateAlternateColorCodes('&', "&b&n" + shopLink));
        rightMessages.add("");
        rightMessages.add(ChatColor.translateAlternateColorCodes('&', "&a&l➜ &7Tapez &eGG &7dans le chat !"));
        rightMessages.add(ChatColor.translateAlternateColorCodes('&', "&7Temps: &e" + waveDuration + "s"));

        // Afficher le pixel art avec les messages à côté
        int maxLines = Math.max(pixelArt.size(), rightMessages.size());
        for (int i = 0; i < maxLines; i++) {
            String leftPart = i < pixelArt.size() ? pixelArt.get(i) : "          "; // 10 espaces
            String rightPart = i < rightMessages.size() ? rightMessages.get(i) : "";

            // Combiner les deux parties avec un espacement
            Bukkit.broadcastMessage(leftPart + "   " + rightPart);
        }

        Bukkit.broadcastMessage("");

        // Ligne de séparation inférieure
        String separator = ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";
        Bukkit.broadcastMessage(separator);
    }

    private void displayWaveContentNoPixelArt(Player targetPlayer) {
        // Si pas de pixel art, afficher seulement les messages
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                "  &e⭐ " + targetPlayer.getName() + " &fa effectué un achat !"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                "  " + shopMessage));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                "  &b&n" + shopLink));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                "  &a&l➜ &7Tapez &eGG &7dans le chat pour féliciter et recevoir une récompense !"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                "  &7Temps restant: &e" + waveDuration + " secondes"));
        Bukkit.broadcastMessage("");

        // Ligne de séparation inférieure
        String separator = ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";
        Bukkit.broadcastMessage(separator);
    }

    /**
     * Affiche le titre et joue le son au démarrage de la vague
     */
    private void showTitleAndSound(Player targetPlayer) {
        if (!titleEnabled && !soundEnabled) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            // Afficher le titre
            if (titleEnabled) {
                Component title = Component.text("GG WAVE")
                        .color(TextColor.color(255, 215, 0));
                Component subtitle = Component.text("Pour " + targetPlayer.getName())
                        .color(TextColor.color(255, 255, 255));

                Title titleObj = Title.title(
                        title,
                        subtitle,
                        Title.Times.times(
                                Duration.ofMillis(500),  // fade in
                                Duration.ofMillis(3000), // stay
                                Duration.ofMillis(500)   // fade out
                        )
                );

                player.showTitle(titleObj);
            }

            // Jouer le son
            if (soundEnabled) {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        }
    }

    /**
     * Démarre la bossbar de progression
     */
    private void startBossBar(Player targetPlayer) {
        if (!bossbarEnabled) return;

        // Créer la bossbar
        activeBossBar = BossBar.bossBar(
                Component.text("GG Wave pour " + targetPlayer.getName())
                        .color(TextColor.color(255, 215, 0)),
                1.0f,
                BossBar.Color.YELLOW,
                BossBar.Overlay.PROGRESS
        );

        // Afficher à tous les joueurs
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showBossBar(activeBossBar);
        }

        // Task pour mettre à jour la progression
        bossbarTask = Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, (task) -> {
            if (!waveActive || activeBossBar == null) {
                task.cancel();
                return;
            }

            long elapsed = System.currentTimeMillis() - waveStartTime;
            float progress = 1.0f - ((float) elapsed / (waveDuration * 1000L));
            progress = Math.max(0, Math.min(1, progress));

            activeBossBar.progress(progress);

            int remaining = (int) ((waveDuration * 1000L - elapsed) / 1000);
            activeBossBar.name(Component.text(
                    "GG Wave • " + playersWhoSaidGG.size() + " participants • " + remaining + "s"
            ).color(TextColor.color(255, 215, 0)));

        }, 1L, 20L); // Update toutes les secondes
    }

    /**
     * Arrête la bossbar
     */
    private void stopBossBar() {
        if (activeBossBar != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.hideBossBar(activeBossBar);
            }
            activeBossBar = null;
        }

        if (bossbarTask != null && !bossbarTask.isCancelled()) {
            bossbarTask.cancel();
            bossbarTask = null;
        }
    }

    public boolean isWaveActive() {
        return waveActive;
    }

    public String processGGMessage(Player player, String message) {
        if (!waveActive) {
            return null;
        }

        // Vérifier si le message contient "gg" (insensible à la casse)
        String lowerMessage = message.toLowerCase();
        if (!lowerMessage.contains("gg")) {
            return null;
        }

        // Donner la récompense si le joueur n'a pas encore dit GG
        if (!playersWhoSaidGG.contains(player.getUniqueId())) {
            playersWhoSaidGG.add(player.getUniqueId());

            // Enregistrer dans les statistiques
            plugin.getStatsManager().recordPlayerGG(player.getUniqueId());

            giveReward(player);
        }

        // Créer le message avec dégradé de couleur
        String ggText = createGradientGG();

        // Remplacer "gg" dans le message par la version colorée
        String processedMessage = message.replaceAll("(?i)gg", ggText);

        return processedMessage;
    }

    private String createGradientGG() {
        if (gradientMode.equals("progressive")) {
            // Mode progressif : utilise seulement les 2 premières couleurs
            // et évolue au fil du temps
            return createProgressiveGradientGG();
        } else {
            // Mode par lettre : chaque lettre a une couleur différente
            return createPerLetterGradientGG();
        }
    }

    private String createPerLetterGradientGG() {
        // Mode classique : chaque lettre a une couleur différente avec animation
        long elapsed = System.currentTimeMillis() - waveStartTime;
        double progress = (elapsed % 5000) / 5000.0; // Cycle de 5 secondes

        // Appliquer le dégradé avec le gras selon la config
        String ggText = ColorGradient.applyGradient("GG", gradientColors, progress, ggBold);

        // Appliquer les styles supplémentaires si configurés
        if (ggItalic || ggUnderline) {
            StringBuilder styledGG = new StringBuilder();

            // Parcourir chaque caractère
            for (int i = 0; i < ggText.length(); i++) {
                char c = ggText.charAt(i);
                styledGG.append(c);

                // Après chaque caractère visible, ajouter les styles
                if (c == 'G' && i + 1 < ggText.length()) {
                    if (ggItalic) {
                        styledGG.append(ChatColor.ITALIC);
                    }
                    if (ggUnderline) {
                        styledGG.append(ChatColor.UNDERLINE);
                    }
                }
            }
            return styledGG.toString();
        }

        return ggText;
    }

    private String createProgressiveGradientGG() {
        // Mode progressif : les deux lettres ont la même couleur
        // qui évolue progressivement à travers TOUTES les couleurs de la liste

        if (gradientColors.isEmpty()) {
            // Fallback au mode classique si pas de couleurs
            return createPerLetterGradientGG();
        }

        // Calculer la progression depuis le début de la vague
        long elapsed = System.currentTimeMillis() - waveStartTime;
        long duration = waveDuration * 1000L; // Durée en millisecondes
        double progress = Math.min(1.0, (double) elapsed / duration);

        // Calculer la position dans la liste de couleurs
        // progress varie de 0.0 à 1.0 sur toute la durée de la vague
        // On veut passer par toutes les couleurs de la liste
        double scaledProgress = progress * (gradientColors.size() - 1);

        // Déterminer entre quelles couleurs on se trouve
        int colorIndex1 = (int) Math.floor(scaledProgress);
        int colorIndex2 = Math.min(colorIndex1 + 1, gradientColors.size() - 1);

        // Calculer la progression locale entre ces deux couleurs
        double localProgress = scaledProgress - colorIndex1;

        // Obtenir les deux couleurs
        String color1Hex = gradientColors.get(colorIndex1);
        String color2Hex = gradientColors.get(colorIndex2);

        // Interpoler entre les deux couleurs
        ChatColor currentColor = ColorGradient.interpolateColors(color1Hex, color2Hex, localProgress);

        // Construire le texte GG avec la couleur actuelle
        StringBuilder result = new StringBuilder();

        if (ggBold) {
            result.append(ChatColor.BOLD);
        }
        if (ggItalic) {
            result.append(ChatColor.ITALIC);
        }
        if (ggUnderline) {
            result.append(ChatColor.UNDERLINE);
        }

        result.append(currentColor).append("GG");

        return result.toString();
    }

    private void giveReward(Player player) {
        // Donner de l'argent (si Vault est présent)
        if (rewards.containsKey("money")) {
            double amount = ((Number) rewards.get("money")).doubleValue();

            VaultHook vaultHook = plugin.getVaultHook();
            if (vaultHook.isEnabled()) {
                if (vaultHook.giveMoney(player, amount)) {
                    String formattedAmount = vaultHook.format(amount);
                    player.sendMessage(ChatColor.GREEN + "Vous avez reçu " + formattedAmount + " pour avoir dit GG !");
                } else {
                    plugin.getLogger().warning("Impossible de donner " + amount + "$ à " + player.getName());
                }
            } else {
                // Vault non disponible, juste afficher un message
                player.sendMessage(ChatColor.GOLD + "Vous auriez reçu " + amount + "$ (Vault non installé)");
            }
        }

        // Donner des items (safe depuis n'importe quel thread)
        if (rewards.containsKey("item.material")) {
            String materialName = (String) rewards.get("item.material");
            int amount = rewards.containsKey("item.amount") ? (int) rewards.get("item.amount") : 1;

            try {
                Material material = Material.valueOf(materialName.toUpperCase());
                ItemStack item = new ItemStack(material, amount);

                // Exécuter sur le thread de la région du joueur (Folia)
                player.getScheduler().run(plugin, (task) -> {
                    player.getInventory().addItem(item);
                    player.sendMessage(ChatColor.GREEN + "Vous avez reçu " + amount + "x " + materialName + " !");
                }, null);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Matériel invalide dans la configuration: " + materialName);
            }
        }

        // Exécuter des commandes personnalisées (sur le thread global)
        if (rewards.containsKey("commands")) {
            @SuppressWarnings("unchecked")
            List<String> commands = (List<String>) rewards.get("commands");
            for (String command : commands) {
                String finalCommand = command.replace("%player%", player.getName());

                // Exécuter sur le thread global (Folia)
                Bukkit.getGlobalRegionScheduler().run(plugin, (task) -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand);
                });
            }
        }
    }

    public void stopWave() {
        if (!waveActive) {
            return;
        }

        // Calculer l'argent distribué
        double moneyDistributed = 0;
        if (rewards.containsKey("money") && plugin.getVaultHook().isEnabled()) {
            double amount = ((Number) rewards.get("money")).doubleValue();
            moneyDistributed = amount * playersWhoSaidGG.size();
        }

        // Enregistrer les statistiques
        if (currentWavePlayer != null) {
            plugin.getStatsManager().recordWave(
                    currentWavePlayer.getName(),
                    playersWhoSaidGG.size(),
                    moneyDistributed
            );
        }

        waveActive = false;

        // Arrêter la bossbar
        stopBossBar();

        if (waveTask != null && !waveTask.isCancelled()) {
            waveTask.cancel();
        }

        // Message de fin
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                "  &6&l✦ FIN DE LA GG WAVE ✦"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                "  &7Total de participants: &e" + playersWhoSaidGG.size()));
        Bukkit.broadcastMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        Bukkit.broadcastMessage("");

        playersWhoSaidGG.clear();
        currentWavePlayer = null;
    }

    public void stopAllWaves() {
        if (waveActive) {
            stopWave();
        }
    }

    public int getRemainingTime() {
        if (!waveActive) {
            return 0;
        }
        long elapsed = (System.currentTimeMillis() - waveStartTime) / 1000;
        return Math.max(0, waveDuration - (int) elapsed);
    }

    public int getParticipantCount() {
        return playersWhoSaidGG.size();
    }

    public int getQueueSize() {
        return waveQueue.size();
    }

    public List<String> getQueuedPlayers() {
        List<String> players = new ArrayList<>();
        for (WaveRequest request : waveQueue) {
            if (request.targetPlayer.isOnline()) {
                players.add(request.targetPlayer.getName());
            }
        }
        return players;
    }
}