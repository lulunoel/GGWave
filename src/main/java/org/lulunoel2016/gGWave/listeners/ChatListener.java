package org.lulunoel2016.gGWave.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.lulunoel2016.gGWave.GGWave;
import org.lulunoel2016.gGWave.managers.GGWaveManager;

public class ChatListener implements Listener {
    
    private final GGWave plugin;
    private final GGWaveManager manager;
    
    public ChatListener(GGWave plugin, GGWaveManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!manager.isWaveActive()) {
            return;
        }
        
        String originalMessage = event.getMessage();
        String processedMessage = manager.processGGMessage(event.getPlayer(), originalMessage);
        
        // Si le message a été modifié (contient "gg")
        if (processedMessage != null) {
            event.setMessage(processedMessage);
        }
    }
}
