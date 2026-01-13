package org.lulunoel2016.gGWave.hooks;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.lulunoel2016.gGWave.GGWave;

public class VaultHook implements Listener {

    private final GGWave plugin;
    private Economy economy = null;
    private boolean vaultEnabled = false;

    public VaultHook(GGWave plugin) {
        this.plugin = plugin;
    }

    /**
     * Configure Vault et l'économie si disponible
     */
    public void     setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().info("Vault n'est pas installé. Les récompenses en argent seront désactivées.");
            return;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().warning("Vault est installé mais aucun plugin d'économie n'a été détecté !");
            plugin.getLogger().warning("Installez un plugin d'économie (EssentialsX, CMI, etc.) pour activer les récompenses en argent.");
            return;
        }

        economy = rsp.getProvider();
        vaultEnabled = true;
        plugin.getLogger().info("Vault détecté ! Économie gérée par : " + economy.getName());
    }
    @EventHandler
    public void onServiceRegister(ServiceRegisterEvent event) {
        if (event.getProvider().getService() == Economy.class) {
            setupEconomy();
        }
    }

    /**
     * Vérifie si Vault est disponible et configuré
     * @return true si Vault est prêt à être utilisé
     */
    public boolean isEnabled() {
        return vaultEnabled && economy != null;
    }

    /**
     * Donne de l'argent à un joueur
     * @param player Le joueur
     * @param amount Le montant à donner
     * @return true si la transaction a réussi
     */
    public boolean giveMoney(Player player, double amount) {
        if (!isEnabled()) {
            return false;
        }

        try {
            economy.depositPlayer(player, amount);
            return true;
        } catch (Exception e) {
            plugin.getLogger().warning("Erreur lors du dépôt d'argent pour " + player.getName() + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Retire de l'argent à un joueur
     * @param player Le joueur
     * @param amount Le montant à retirer
     * @return true si la transaction a réussi
     */
    public boolean takeMoney(Player player, double amount) {
        if (!isEnabled()) {
            return false;
        }

        try {
            if (economy.has(player, amount)) {
                economy.withdrawPlayer(player, amount);
                return true;
            }
            return false;
        } catch (Exception e) {
            plugin.getLogger().warning("Erreur lors du retrait d'argent pour " + player.getName() + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtient le solde d'un joueur
     * @param player Le joueur
     * @return Le solde du joueur
     */
    public double getBalance(Player player) {
        if (!isEnabled()) {
            return 0;
        }

        try {
            return economy.getBalance(player);
        } catch (Exception e) {
            plugin.getLogger().warning("Erreur lors de la récupération du solde de " + player.getName() + ": " + e.getMessage());
            return 0;
        }
    }

    /**
     * Formate un montant en utilisant le format de la monnaie
     * @param amount Le montant
     * @return Le montant formaté
     */
    public String format(double amount) {
        if (!isEnabled()) {
            return String.format("%.2f$", amount);
        }

        try {
            return economy.format(amount);
        } catch (Exception e) {
            return String.format("%.2f$", amount);
        }
    }

    /**
     * Obtient le nom de la devise (singulier)
     * @return Le nom de la devise
     */
    public String getCurrencyName() {
        if (!isEnabled()) {
            return "dollar";
        }

        try {
            return economy.currencyNameSingular();
        } catch (Exception e) {
            return "dollar";
        }
    }

    /**
     * Obtient le nom de la devise (pluriel)
     * @return Le nom de la devise au pluriel
     */
    public String getCurrencyNamePlural() {
        if (!isEnabled()) {
            return "dollars";
        }

        try {
            return economy.currencyNamePlural();
        } catch (Exception e) {
            return "dollars";
        }
    }
}