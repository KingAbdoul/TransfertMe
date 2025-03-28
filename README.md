# 🔄 TransfertMe

Application Android permettant le transfert d'argent de manière sécurisée tout en envisageant d'interconnecter divers systèmes de paiement existants plus tard, en utilisant Firebase pour l'authentification et le stockage des données.

## 📚 Table des Matières
- [Fonctionnalités](#-fonctionnalités)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Utilisation](#-utilisation)
- [Technologies Utilisées](#-technologies-utilisées)
- [Contribuer](#-contribuer)

## 🚀 Fonctionnalités

### 🔒 Authentification Sécurisée
- Gestion des comptes utilisateurs via **Firebase Authentication**.

### 💸 Transfert d'Argent Instantané
- Transactions chiffrées pour une sécurité maximale.
- Interface intuitive pour des opérations rapides.

### 📜 Historique des Transactions
- Archivage chronologique de toutes les transactions.

### 🔔 Notifications en Temps Réel
- Alertes push immédiates pour chaque action (réception, confirmation de transfert).

### 🛠 Support Utilisateur 24/7
- Centre d'aide intégré avec FAQ.

## 📋 Prérequis

- **Environnement de Développement** :
  - Android Studio (dernière version recommandée)
  - Java JDK 17+
- **Compte Firebase** activé avec un projet configuré.

## 🔧 Installation

1. **Cloner le dépôt :**
   ```bash
   git clone https://github.com/votre-utilisateur/transfertme.git


2. **Ouvrir le projet dans Android Studio :**
- Lancer Android Studio > Open Existing Project > Sélectionner le dossier cloné.

3. **Configurer Firebase :**
- Télécharger le fichier google-services.json depuis la console Firebase.
- Placer le fichier dans : app/ (Contactez-nous pour obtenir le fichier).

4. **Synchroniser les dépendances :**
- Cliquer sur Sync Project with Gradle Files dans Android Studio.

5. **Exécuter l'application :**
- Brancher un appareil Android (mode développeur activé) ou utiliser l'émulateur.
- Cliquer sur Run 'app' (🔼 Icône de lecture).

## 📱 Utilisation

### Création de Compte / Connexion
1. Ouvrir l'application et choisir S'inscrire ou Se connecter.
2. Remplir les champs (email, mot de passe...) et valider.

### Effectuer un Transfert
Dans l'onglet Effectuer une transaction:
1. Sélectionner un opérateur, entrer le numéro de l'expéditeur
2. sélectionner un opérateur, entrer le numéro du destinataire.
3. Entrer le montant et confirmer avec Envoyer.

### Consulter l'Historique
1. Naviguer vers Historique pour visualiser toutes les transactions.

## 🛠 Technologies Utilisées  

| **Technologies**                                                                 |
|---------------------------------------------------------------------------------|
| Firebase Authentication, Cloud Firestore, Firebase Cloud Messaging             |
| XML (UI), Java 17 (logique métier)                                             |
| Android Studio, Firebase Console, Git                                          |

## 🤝 Contribuer
N'hésitez pas à ouvrir un ticket ou à soumettre une demande d'ajout pour toute amélioration ou suggestion :​
Forker le dépôt :
Créez une copie via le bouton Fork en haut à droite.
Créer une branche :
   ```bash
git checkout -b fonctionnalite/ma-nouvelle-fonctionnalite
```

Développer et tester :
Assurez-vous de respecter les conventions de code existantes.
Ajoutez des tests unitaires si nécessaire.

Valider les changements :
   ```bash
git commit -m ajout d'une fonctionnalité révolutionnaire
```

Pousser vers GitHub :
   ```bash
git push origin fonctionnalite/ma-nouvelle-fonctionnalite
```

- Ouvrir une Pull Request :
Rendez-vous sur GitHub et cliquez sur Compare & Pull Request.
Décrivez précisément vos modifications et les problèmes résolus.
