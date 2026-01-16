package org.lulunoel2016.gGWave.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.lulunoel2016.gGWave.GGWave;
import org.lulunoel2016.gGWave.managers.GGWaveManager;
import org.lulunoel2016.gGWave.managers.StatsManager;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class GGWaveCommand implements CommandExecutor, TabCompleter {

    private final GGWave plugin;
    private final GGWaveManager manager;

    public GGWaveCommand(GGWave plugin, GGWaveManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return handleHelp(sender);
        }

        switch (args[0].toLowerCase()) {
            case "start":
                return handleStart(sender, args);
            case "stop":
                return handleStop(sender);
            case "reload":
                return handleReload(sender);
            case "info":
                return handleInfo(sender);
            case "stats":
                return handleStats(sender);
            case "history":
                return handleHistory(sender);
            case "help":
                return handleHelp(sender);
            default:
                sender.sendMessage(ChatColor.RED + "Commande inconnue. Utilisez /ggwave help");
                return true;
        }
    }

    private boolean handleStart(CommandSender sender, String[] args) {
        if (!sender.hasPermission("ggwave.start")) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande !");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /ggwave start <joueur>");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Le joueur " + args[1] + " n'est pas en ligne !");
            return true;
        }

        Player senderPlayer = sender instanceof Player ? (Player) sender : null;
        manager.startWave(targetPlayer, senderPlayer);

        return true;
    }

    private boolean handleStop(CommandSender sender) {
        if (!sender.hasPermission("ggwave.stop")) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande !");
            return true;
        }

        if (!manager.isWaveActive()) {
            sender.sendMessage(ChatColor.RED + "Aucune vague n'est active !");
            return true;
        }

        manager.stopWave();
        sender.sendMessage(ChatColor.GREEN + "Vague de GG arrêtée !");

        return true;
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("ggwave.reload")) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande !");
            return true;
        }

        plugin.reloadConfig();
        manager.reloadConfiguration();
        sender.sendMessage(ChatColor.GREEN + "Configuration rechargée avec succès !");

        return true;
    }

    private boolean handleInfo(CommandSender sender) {
        if (!sender.hasPermission("ggwave.info")) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande !");
            return true;
        }

        sender.sendMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        sender.sendMessage(ChatColor.YELLOW + "Informations GG Wave:");
        sender.sendMessage("");

        if (manager.isWaveActive()) {
            sender.sendMessage(ChatColor.GREEN + "Statut: " + ChatColor.WHITE + "Active");
            sender.sendMessage(ChatColor.GREEN + "Temps restant: " + ChatColor.WHITE + manager.getRemainingTime() + "s");
            sender.sendMessage(ChatColor.GREEN + "Participants: " + ChatColor.WHITE + manager.getParticipantCount());
        } else {
            sender.sendMessage(ChatColor.RED + "Statut: " + ChatColor.WHITE + "Inactive");
        }

        // Afficher la file d'attente
        int queueSize = manager.getQueueSize();
        if (queueSize > 0) {
            sender.sendMessage("");
            sender.sendMessage(ChatColor.YELLOW + "File d'attente: " + ChatColor.WHITE + queueSize + " vague(s)");
            List<String> queuedPlayers = manager.getQueuedPlayers();
            for (int i = 0; i < Math.min(5, queuedPlayers.size()); i++) {
                sender.sendMessage(ChatColor.GRAY + "  " + (i + 1) + ". " + queuedPlayers.get(i));
            }
            if (queuedPlayers.size() > 5) {
                sender.sendMessage(ChatColor.GRAY + "  ... et " + (queuedPlayers.size() - 5) + " autre(s)");
            }
        }

        sender.sendMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        return true;
    }

    private boolean handleStats(CommandSender sender) {
        if (!sender.hasPermission("ggwave.stats")) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande !");
            return true;
        }

        StatsManager stats = plugin.getStatsManager();

        sender.sendMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        sender.sendMessage(ChatColor.YELLOW + "Statistiques GGWave:");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW + "Total de vagues: " + ChatColor.WHITE + stats.getTotalWavesLaunched());
        sender.sendMessage(ChatColor.YELLOW + "Total de participations: " + ChatColor.WHITE + stats.getTotalParticipations());
        sender.sendMessage(ChatColor.YELLOW + "Moyenne de participants: " + ChatColor.WHITE + String.format("%.1f", stats.getAverageParticipants()));

        if (plugin.getVaultHook().isEnabled()) {
            String formatted = plugin.getVaultHook().format(stats.getTotalMoneyDistributed());
            sender.sendMessage(ChatColor.YELLOW + "Argent distribué: " + ChatColor.WHITE + formatted);
        }

        sender.sendMessage("");
        sender.sendMessage(ChatColor.GOLD + "Top 5 Joueurs:");

        int rank = 1;
        for (Map.Entry<UUID, Integer> entry : stats.getTopPlayers(5)) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(entry.getKey());
            String playerName = player.getName() != null ? player.getName() : "Inconnu";
            sender.sendMessage(ChatColor.YELLOW + "#" + rank + " " + ChatColor.WHITE + playerName +
                    ChatColor.GRAY + " - " + entry.getValue() + " GG");
            rank++;
        }

        sender.sendMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        return true;
    }

    private boolean handleHistory(CommandSender sender) {
        if (!sender.hasPermission("ggwave.history")) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande !");
            return true;
        }

        StatsManager stats = plugin.getStatsManager();
        List<StatsManager.WaveRecord> recent = stats.getRecentWaves(10);

        if (recent.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "Aucune vague dans l'historique.");
            return true;
        }

        sender.sendMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        sender.sendMessage(ChatColor.YELLOW + "Historique des 10 dernières vagues:");
        sender.sendMessage("");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
        for (StatsManager.WaveRecord record : recent) {
            String date = sdf.format(new Date(record.timestamp));
            String moneyStr = plugin.getVaultHook().isEnabled() ?
                    plugin.getVaultHook().format(record.moneyDistributed) :
                    String.format("%.2f$", record.moneyDistributed);

            sender.sendMessage(ChatColor.YELLOW + date + " " +
                    ChatColor.WHITE + record.playerName +
                    ChatColor.GRAY + " - " + record.participants + " participants - " + moneyStr);
        }

        sender.sendMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        return true;
    }

    private boolean handleHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬ GGWave Commandes ▬▬▬▬▬▬▬▬");
        sender.sendMessage(ChatColor.YELLOW + "/ggwave start <joueur>" + ChatColor.GRAY + " - Lance une vague de GG");
        sender.sendMessage(ChatColor.YELLOW + "/ggwave stop" + ChatColor.GRAY + " - Arrête la vague en cours");
        sender.sendMessage(ChatColor.YELLOW + "/ggwave info" + ChatColor.GRAY + " - Infos sur la vague actuelle");
        sender.sendMessage(ChatColor.YELLOW + "/ggwave stats" + ChatColor.GRAY + " - Statistiques globales");
        sender.sendMessage(ChatColor.YELLOW + "/ggwave history" + ChatColor.GRAY + " - Historique des vagues");
        sender.sendMessage(ChatColor.YELLOW + "/ggwave reload" + ChatColor.GRAY + " - Recharge la configuration");
        sender.sendMessage(ChatColor.YELLOW + "/ggwave help" + ChatColor.GRAY + " - Affiche cette aide");
        sender.sendMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> commands = Arrays.asList("start", "stop", "reload", "info", "stats", "history", "help");
            return commands.stream()
                    .filter(cmd -> cmd.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return null;
    }
}