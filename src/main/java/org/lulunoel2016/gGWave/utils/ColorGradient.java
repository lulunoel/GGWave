package org.lulunoel2016.gGWave.utils;

import net.md_5.bungee.api.ChatColor;

import java.awt.Color;
import java.util.List;

public class ColorGradient {

    /**
     * Applique un dégradé de couleur à un texte
     * @param text Le texte à colorier
     * @param hexColors Liste de couleurs hex pour le dégradé
     * @param offset Offset pour animer le dégradé (0.0 à 1.0)
     * @param bold Si true, applique le gras
     * @return Le texte coloré
     */
    public static String applyGradient(String text, List<String> hexColors, double offset, boolean bold) {
        if (text.isEmpty() || hexColors.isEmpty()) {
            return text;
        }

        StringBuilder result = new StringBuilder();
        int textLength = text.length();

        for (int i = 0; i < textLength; i++) {
            char c = text.charAt(i);

            // Calculer la position dans le dégradé avec l'offset
            double position = ((double) i / textLength + offset) % 1.0;

            // Obtenir la couleur à cette position
            ChatColor color = getColorAt(position, hexColors);

            // Ajouter le gras si demandé
            if (bold) {
                result.append(ChatColor.BOLD).append(color).append(c);
            } else {
                result.append(color).append(c);
            }
        }

        return result.toString();
    }

    /**
     * Applique un dégradé de couleur à un texte (avec gras par défaut)
     * @param text Le texte à colorier
     * @param hexColors Liste de couleurs hex pour le dégradé
     * @param offset Offset pour animer le dégradé (0.0 à 1.0)
     * @return Le texte coloré
     */
    public static String applyGradient(String text, List<String> hexColors, double offset) {
        return applyGradient(text, hexColors, offset, true);
    }

    /**
     * Obtient une couleur à une position donnée dans le dégradé
     * @param position Position dans le dégradé (0.0 à 1.0)
     * @param hexColors Liste de couleurs hex
     * @return La couleur ChatColor
     */
    private static ChatColor getColorAt(double position, List<String> hexColors) {
        if (hexColors.size() == 1) {
            return ChatColor.of(hexColors.get(0));
        }

        // Calculer entre quelles couleurs on se trouve
        double scaledPosition = position * (hexColors.size() - 1);
        int index1 = (int) Math.floor(scaledPosition);
        int index2 = Math.min(index1 + 1, hexColors.size() - 1);
        double localPosition = scaledPosition - index1;

        // Interpoler entre les deux couleurs
        Color color1 = hexToColor(hexColors.get(index1));
        Color color2 = hexToColor(hexColors.get(index2));

        int r = (int) (color1.getRed() + (color2.getRed() - color1.getRed()) * localPosition);
        int g = (int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * localPosition);
        int b = (int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * localPosition);

        return ChatColor.of(new Color(r, g, b));
    }

    /**
     * Convertit une couleur hex en Color
     * @param hex Couleur au format hex (#RRGGBB)
     * @return Color
     */
    private static Color hexToColor(String hex) {
        hex = hex.replace("#", "");
        try {
            return new Color(
                    Integer.parseInt(hex.substring(0, 2), 16),
                    Integer.parseInt(hex.substring(2, 4), 16),
                    Integer.parseInt(hex.substring(4, 6), 16)
            );
        } catch (Exception e) {
            return Color.WHITE;
        }
    }

    /**
     * Crée un dégradé arc-en-ciel pour un texte
     * @param text Le texte à colorier
     * @param offset Offset pour l'animation
     * @return Le texte coloré
     */
    public static String rainbowGradient(String text, double offset) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // Calculer la teinte en fonction de la position et de l'offset
            float hue = (float) (((double) i / text.length() + offset) % 1.0);
            Color color = Color.getHSBColor(hue, 1.0f, 1.0f);

            ChatColor chatColor = ChatColor.of(color);
            result.append(ChatColor.BOLD).append(chatColor).append(c);
        }

        return result.toString();
    }

    /**
     * Interpole entre deux couleurs hexadécimales
     * @param hex1 Première couleur (#RRGGBB)
     * @param hex2 Deuxième couleur (#RRGGBB)
     * @param progress Progression entre 0.0 (couleur 1) et 1.0 (couleur 2)
     * @return La couleur interpolée
     */
    public static ChatColor interpolateColors(String hex1, String hex2, double progress) {
        Color color1 = hexToColor(hex1);
        Color color2 = hexToColor(hex2);

        // Limiter la progression entre 0 et 1
        progress = Math.max(0.0, Math.min(1.0, progress));

        // Interpoler chaque composante
        int r = (int) (color1.getRed() + (color2.getRed() - color1.getRed()) * progress);
        int g = (int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * progress);
        int b = (int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * progress);

        return ChatColor.of(new Color(r, g, b));
    }
}