package org.lulunoel2016.gGWave.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PixelArtRenderer {
    
    // Cache pour éviter de télécharger plusieurs fois le même skin
    private static final Map<String, BufferedImage> skinCache = new HashMap<>();
    
    // Liste des APIs disponibles pour télécharger les skins (avec fallback)
    private static final String[] SKIN_APIS = {
        "https://mc-heads.net/skin/%s",           // API 1: mc-heads.net
        "https://minotar.net/skin/%s",            // API 2: minotar.net
        "https://crafatar.com/skins/%s",          // API 3: crafatar.com
        "https://sessionserver.mojang.com/session/minecraft/profile/%s" // API 4: Mojang officiel
    };
    
    /**
     * Rend la tête d'un joueur en pixel art dans le chat
     * @param player Le joueur dont on veut afficher la tête
     * @param size Taille du pixel art (recommandé: 8)
     * @return Liste de lignes de texte représentant le pixel art
     */
    public static List<String> renderPlayerHead(Player player, int size) {
        List<String> lines = new ArrayList<>();
        
        try {
            // Récupérer l'UUID sans tirets
            String uuid = player.getUniqueId().toString().replace("-", "");
            System.out.println("[GGWave] Génération du pixel art pour le joueur " + player.getName() + " (UUID: " + uuid + ")");
            
            BufferedImage skin = skinCache.get(uuid);
            
            if (skin == null) {
                // Essayer de télécharger le skin depuis différentes APIs
                skin = downloadSkinWithFallback(uuid);
                
                if (skin != null) {
                    skinCache.put(uuid, skin);
                    System.out.println("[GGWave] Skin mis en cache pour " + player.getName());
                } else {
                    System.out.println("[GGWave] Impossible de télécharger le skin, utilisation du fallback");
                }
            } else {
                System.out.println("[GGWave] Skin récupéré depuis le cache pour " + player.getName());
            }
            
            if (skin == null) {
                return createFallbackPixelArt(player.getName(), size);
            }
            
            // Extraire la face (8x8 pixels sur le skin 64x64)
            BufferedImage face = extractFace(skin);
            
            // Redimensionner si nécessaire
            if (size != 8) {
                face = resizeImage(face, size, size);
            }
            
            // Convertir en lignes de chat avec des couleurs
            for (int y = 0; y < size; y++) {
                StringBuilder line = new StringBuilder("  "); // Indentation
                
                for (int x = 0; x < size; x++) {
                    Color pixelColor = new Color(face.getRGB(x, y), true);
                    
                    // Gérer la transparence
                    if (pixelColor.getAlpha() < 50) {
                        line.append("  ");
                    } else {
                        // Utiliser █ pour un meilleur rendu
                        ChatColor chatColor = ChatColor.of(new Color(
                            pixelColor.getRed(),
                            pixelColor.getGreen(),
                            pixelColor.getBlue()
                        ));
                        line.append(chatColor).append("█");
                    }
                }
                
                lines.add(line.toString());
            }
            
        } catch (Exception e) {
            System.out.println("[GGWave] Erreur lors de la génération du pixel art: " + e.getMessage());
            e.printStackTrace();
            return createFallbackPixelArt(player.getName(), size);
        }
        
        return lines;
    }
    
    /**
     * Télécharge le skin en essayant plusieurs APIs (fallback)
     */
    private static BufferedImage downloadSkinWithFallback(String uuid) {
        // Retirer les tirets de l'UUID si présents
        String cleanUuid = uuid.replace("-", "");
        
        for (String apiUrl : SKIN_APIS) {
            try {
                String url = String.format(apiUrl, cleanUuid);
                System.out.println("[GGWave] Tentative de téléchargement du skin depuis: " + url);
                
                BufferedImage skin = downloadImage(url);
                
                if (skin != null && skin.getWidth() > 0 && skin.getHeight() > 0) {
                    // Vérifier que c'est bien un skin Minecraft valide
                    if (skin.getWidth() == 64 && (skin.getHeight() == 64 || skin.getHeight() == 32)) {
                        System.out.println("[GGWave] Skin téléchargé avec succès depuis: " + url);
                        return skin;
                    } else {
                        System.out.println("[GGWave] Taille de skin invalide: " + skin.getWidth() + "x" + skin.getHeight());
                    }
                }
            } catch (Exception e) {
                System.out.println("[GGWave] Erreur avec l'API " + apiUrl + ": " + e.getMessage());
                // Continuer avec l'API suivante
                continue;
            }
        }
        
        System.out.println("[GGWave] Toutes les APIs ont échoué, utilisation du fallback");
        return null;
    }
    
    /**
     * Télécharge depuis l'API Mojang officielle (plus complexe mais plus fiable)
     */
    private static BufferedImage downloadFromMojangAPI(String uuid) {
        try {
            // Étape 1 : Obtenir le profil du joueur
            String profileUrl = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid;
            URL url = new URL(profileUrl);
            
            // Note: Cette méthode nécessiterait un parser JSON
            // Pour l'instant, on retourne null et on utilise le fallback
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Extrait la partie "face" du skin Minecraft
     * La face est à (8, 8) et fait 8x8 pixels sur un skin 64x64
     */
    private static BufferedImage extractFace(BufferedImage skin) {
        // Sur un skin Minecraft standard, la face est à (8, 8) avec une taille de 8x8
        int faceX = 8;
        int faceY = 8;
        int faceSize = 8;
        
        // Vérifier la taille du skin
        if (skin.getWidth() == 64 && skin.getHeight() == 64) {
            // Skin moderne (post 1.8)
            return skin.getSubimage(faceX, faceY, faceSize, faceSize);
        } else if (skin.getWidth() == 64 && skin.getHeight() == 32) {
            // Vieux skin (pre 1.8)
            return skin.getSubimage(faceX, faceY, faceSize, faceSize);
        } else {
            // Taille inconnue, essayer quand même
            try {
                return skin.getSubimage(faceX, faceY, faceSize, faceSize);
            } catch (Exception e) {
                // Si ça échoue, prendre toute l'image et la redimensionner
                return resizeImage(skin, 8, 8);
            }
        }
    }
    
    /**
     * Télécharge une image depuis une URL avec timeout
     */
    private static BufferedImage downloadImage(String urlString) {
        try {
            URL url = new URL(urlString);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            
            // Configurer la connexion
            connection.setConnectTimeout(5000); // 5 secondes
            connection.setReadTimeout(5000);    // 5 secondes
            connection.setRequestProperty("User-Agent", "GGWave-Plugin/1.0");
            connection.setInstanceFollowRedirects(true);
            
            // Vérifier le code de réponse
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("[GGWave] Code de réponse HTTP: " + responseCode + " pour " + urlString);
                return null;
            }
            
            BufferedImage img = ImageIO.read(connection.getInputStream());
            
            // Vérifier que l'image est valide
            if (img != null && img.getWidth() > 0 && img.getHeight() > 0) {
                return img;
            }
        } catch (java.net.SocketTimeoutException e) {
            System.out.println("[GGWave] Timeout lors du téléchargement de: " + urlString);
        } catch (IOException e) {
            System.out.println("[GGWave] Erreur IO: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Redimensionne une image avec interpolation nearest-neighbor
     */
    private static BufferedImage resizeImage(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }
    
    /**
     * Crée un pixel art de fallback si le téléchargement échoue
     */
    private static List<String> createFallbackPixelArt(String playerName, int size) {
        List<String> lines = new ArrayList<>();
        
        // Générer une couleur basée sur le nom du joueur
        int hash = Math.abs(playerName.hashCode());
        Color baseColor = new Color(
            100 + (hash % 156),
            100 + ((hash >> 8) % 156),
            100 + ((hash >> 16) % 156)
        );
        
        ChatColor mainColor = ChatColor.of(baseColor);
        ChatColor darkColor = ChatColor.of(baseColor.darker());
        ChatColor lightColor = ChatColor.of(baseColor.brighter());
        
        // Créer un motif simple de tête
        // Pattern: 0=vide, 1=foncé, 2=normal, 3=clair
        int[][] pattern = {
            {0, 0, 1, 1, 1, 1, 0, 0},
            {0, 1, 2, 2, 2, 2, 1, 0},
            {1, 2, 2, 3, 3, 2, 2, 1},
            {1, 2, 3, 2, 2, 3, 2, 1},
            {1, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 3, 2, 2, 3, 2, 1},
            {0, 1, 2, 3, 3, 2, 1, 0},
            {0, 0, 1, 1, 1, 1, 0, 0}
        };
        
        int displaySize = Math.min(size, 8);
        
        for (int y = 0; y < displaySize; y++) {
            StringBuilder line = new StringBuilder("  ");
            for (int x = 0; x < displaySize; x++) {
                int value = pattern[y][x];
                ChatColor color;
                
                switch (value) {
                    case 0:
                        line.append("  ");
                        continue;
                    case 1:
                        color = darkColor;
                        break;
                    case 2:
                        color = mainColor;
                        break;
                    case 3:
                        color = lightColor;
                        break;
                    default:
                        color = mainColor;
                }
                
                line.append(color).append("█");
            }
            lines.add(line.toString());
        }
        
        return lines;
    }
    
    /**
     * Vide le cache des skins
     */
    public static void clearCache() {
        skinCache.clear();
    }
}
