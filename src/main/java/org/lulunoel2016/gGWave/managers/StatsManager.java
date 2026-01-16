package org.lulunoel2016.gGWave.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.lulunoel2016.gGWave.GGWave;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class StatsManager {
    
    private final GGWave plugin;
    private File statsFile;
    private FileConfiguration statsConfig;
    
    // Statistiques en mémoire
    private int totalWavesLaunched = 0;
    private int totalParticipations = 0;
    private double totalMoneyDistributed = 0;
    private final Map<UUID, Integer> playerGGCount = new HashMap<>();
    private final List<WaveRecord> waveHistory = new ArrayList<>();
    
    public StatsManager(GGWave plugin) {
        this.plugin = plugin;
        setupStatsFile();
        loadStats();
    }
    
    /**
     * Configure le fichier de statistiques
     */
    private void setupStatsFile() {
        statsFile = new File(plugin.getDataFolder(), "stats.yml");
        if (!statsFile.exists()) {
            try {
                statsFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Impossible de créer le fichier stats.yml: " + e.getMessage());
            }
        }
        statsConfig = YamlConfiguration.loadConfiguration(statsFile);
    }
    
    /**
     * Charge les statistiques depuis le fichier
     */
    public void loadStats() {
        totalWavesLaunched = statsConfig.getInt("total-waves", 0);
        totalParticipations = statsConfig.getInt("total-participations", 0);
        totalMoneyDistributed = statsConfig.getDouble("total-money", 0);
        
        // Charger les stats des joueurs
        if (statsConfig.contains("players")) {
            for (String uuidString : statsConfig.getConfigurationSection("players").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidString);
                int count = statsConfig.getInt("players." + uuidString);
                playerGGCount.put(uuid, count);
            }
        }
    }
    
    /**
     * Sauvegarde les statistiques dans le fichier
     */
    public void saveStats() {
        statsConfig.set("total-waves", totalWavesLaunched);
        statsConfig.set("total-participations", totalParticipations);
        statsConfig.set("total-money", totalMoneyDistributed);
        
        // Sauvegarder les stats des joueurs
        for (Map.Entry<UUID, Integer> entry : playerGGCount.entrySet()) {
            statsConfig.set("players." + entry.getKey().toString(), entry.getValue());
        }
        
        try {
            statsConfig.save(statsFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Impossible de sauvegarder stats.yml: " + e.getMessage());
        }
    }
    
    /**
     * Enregistre une nouvelle vague
     */
    public void recordWave(String playerName, int participants, double moneyDistributed) {
        totalWavesLaunched++;
        totalParticipations += participants;
        totalMoneyDistributed += moneyDistributed;
        
        WaveRecord record = new WaveRecord(playerName, participants, moneyDistributed, System.currentTimeMillis());
        waveHistory.add(record);
        
        // Garder seulement les 100 dernières vagues en mémoire
        if (waveHistory.size() > 100) {
            waveHistory.remove(0);
        }
        
        saveStats();
    }
    
    /**
     * Enregistre qu'un joueur a dit GG
     */
    public void recordPlayerGG(UUID playerUuid) {
        playerGGCount.put(playerUuid, playerGGCount.getOrDefault(playerUuid, 0) + 1);
    }
    
    /**
     * Obtient le nombre de GG d'un joueur
     */
    public int getPlayerGGCount(UUID playerUuid) {
        return playerGGCount.getOrDefault(playerUuid, 0);
    }
    
    /**
     * Obtient le top 10 des joueurs les plus actifs
     */
    public List<Map.Entry<UUID, Integer>> getTopPlayers(int limit) {
        List<Map.Entry<UUID, Integer>> sortedList = new ArrayList<>(playerGGCount.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        return sortedList.subList(0, Math.min(limit, sortedList.size()));
    }
    
    // Getters
    public int getTotalWavesLaunched() {
        return totalWavesLaunched;
    }
    
    public int getTotalParticipations() {
        return totalParticipations;
    }
    
    public double getTotalMoneyDistributed() {
        return totalMoneyDistributed;
    }
    
    public double getAverageParticipants() {
        if (totalWavesLaunched == 0) return 0;
        return (double) totalParticipations / totalWavesLaunched;
    }
    
    public List<WaveRecord> getRecentWaves(int limit) {
        int size = waveHistory.size();
        int start = Math.max(0, size - limit);
        return new ArrayList<>(waveHistory.subList(start, size));
    }
    
    /**
     * Classe interne pour enregistrer une vague
     */
    public static class WaveRecord {
        public final String playerName;
        public final int participants;
        public final double moneyDistributed;
        public final long timestamp;
        
        public WaveRecord(String playerName, int participants, double moneyDistributed, long timestamp) {
            this.playerName = playerName;
            this.participants = participants;
            this.moneyDistributed = moneyDistributed;
            this.timestamp = timestamp;
        }
    }
}
