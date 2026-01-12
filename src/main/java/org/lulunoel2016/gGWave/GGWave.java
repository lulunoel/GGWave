package org.lulunoel2016.gGWave;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.lulunoel2016.gGWave.commands.GGWaveCommand;
import org.lulunoel2016.gGWave.hooks.VaultHook;
import org.lulunoel2016.gGWave.listeners.ChatListener;
import org.lulunoel2016.gGWave.managers.GGWaveManager;

public final class GGWave extends JavaPlugin {

    private static GGWave instance;
    private GGWaveManager ggWaveManager;
    private VaultHook vaultHook;

    @Override
    public void onEnable() {
        instance = this;

        // Sauvegarder la configuration par défaut si elle n'existe pas
        saveDefaultConfig();

        // Initialiser Vault
        vaultHook = new VaultHook(this);

        // Initialiser le gestionnaire de GG Wave
        ggWaveManager = new GGWaveManager(this);

        // Enregistrer les commandes
        getCommand("ggwave").setExecutor(new GGWaveCommand(this, ggWaveManager));

        // Enregistrer les listeners
        Bukkit.getPluginManager().registerEvents(new ChatListener(this, ggWaveManager), this);

        getLogger().info("GGWave plugin activé avec succès !");

        // Afficher le statut de Vault
        if (vaultHook.isEnabled()) {
            getLogger().info("✓ Vault activé - Récompenses en argent disponibles");
        } else {
            getLogger().info("✗ Vault non disponible - Récompenses en argent désactivées");
        }
    }

    @Override
    public void onDisable() {
        // Arrêter toutes les vagues en cours
        if (ggWaveManager != null) {
            ggWaveManager.stopAllWaves();
        }

        getLogger().info("GGWave plugin désactivé !");
    }

    public static GGWave getInstance() {
        return instance;
    }

    public GGWaveManager getGGWaveManager() {
        return ggWaveManager;
    }

    public VaultHook getVaultHook() {
        return vaultHook;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        if (ggWaveManager != null) {
            ggWaveManager.reloadConfiguration();
        }
    }
}