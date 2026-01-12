package org.lulunoel2016.gGWave.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.lulunoel2016.gGWave.GGWave;
import org.lulunoel2016.gGWave.managers.GGWaveManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "start":
                return handleStart(sender, args);

            case "stop":
                return handleStop(sender);

            case "reload":
                return handleReload(sender);

            case "info":
                return handleInfo(sender);

            case "help":
                sendHelp(sender);
                return true;

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
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Joueur introuvable : " + args[1]);
            return true;
        }

        Player senderPlayer = sender instanceof Player ? (Player) sender : null;
        if (senderPlayer == null) {
            // Depuis la console
            manager.startWave(targetPlayer, null);
            sender.sendMessage(ChatColor.GREEN + "Vague de GG lancée pour " + targetPlayer.getName() + " !");
        } else {
            manager.startWave(targetPlayer, senderPlayer);
        }

        return true;
    }

    private boolean handleStop(CommandSender sender) {
        if (!sender.hasPermission("ggwave.stop")) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande !");
            return true;
        }

        if (!manager.isWaveActive()) {
            sender.sendMessage(ChatColor.RED + "Aucune vague de GG n'est active actuellement !");
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

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Commandes GG Wave:");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GREEN + "/ggwave start <joueur>" + ChatColor.GRAY + " - Lancer une vague de GG");
        sender.sendMessage(ChatColor.GREEN + "/ggwave stop" + ChatColor.GRAY + " - Arrêter la vague en cours");
        sender.sendMessage(ChatColor.GREEN + "/ggwave info" + ChatColor.GRAY + " - Voir les informations");
        sender.sendMessage(ChatColor.GREEN + "/ggwave reload" + ChatColor.GRAY + " - Recharger la configuration");
        sender.sendMessage(ChatColor.GREEN + "/ggwave help" + ChatColor.GRAY + " - Afficher cette aide");
        sender.sendMessage(ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("start", "stop", "reload", "info", "help");
            return subCommands.stream()
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return completions;
    }
}