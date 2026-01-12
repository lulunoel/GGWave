#!/bin/bash

# Script de compilation GGWave Plugin
# Ce script compile le plugin et copie le JAR dans le dossier de sortie

echo "================================================"
echo "  Compilation du plugin GGWave"
echo "================================================"
echo ""

# Vérifier si Maven est installé
if ! command -v mvn &> /dev/null; then
    echo "❌ Erreur: Maven n'est pas installé !"
    echo "   Installez Maven depuis https://maven.apache.org/"
    exit 1
fi

echo "✓ Maven détecté"
echo ""

# Nettoyer les builds précédents
echo "🧹 Nettoyage des builds précédents..."
mvn clean

echo ""
echo "🔨 Compilation en cours..."
echo ""

# Compiler le plugin
mvn package

# Vérifier si la compilation a réussi
if [ $? -eq 0 ]; then
    echo ""
    echo "================================================"
    echo "  ✅ Compilation réussie !"
    echo "================================================"
    echo ""
    echo "Le fichier JAR se trouve dans :"
    echo "  target/GGWave-1.0.0.jar"
    echo ""
    echo "Pour l'utiliser :"
    echo "  1. Copiez le fichier dans le dossier plugins/ de votre serveur"
    echo "  2. Redémarrez le serveur"
    echo "  3. Configurez le fichier config.yml dans plugins/GGWave/"
    echo ""
else
    echo ""
    echo "================================================"
    echo "  ❌ Erreur lors de la compilation"
    echo "================================================"
    echo ""
    echo "Vérifiez les messages d'erreur ci-dessus"
    exit 1
fi
