English Guide
Available for Paper, Bukkit and Spigot servers
What is LinkAcc?
LinkAcc is a Minecraft plugin that lets you link and swap player data between two players.
Perfect for challenges, roleplay, or admin controls — with LinkAcc you manage who gets whose player data.

Features:

Players can send link requests

Target player gets kicked and their player data is transferred

Stats and Advancements are swapped too

Unlink players to return original data

Join-block for linked players until unlinked

Auto-generated language files (de_de and en_us)

Settings configurable via config.yml
Installation
Copy the plugin file (LinkAcc.jar) into your server’s /plugins/ directory.

Restart your server.

The following directories and files will be created automatically:
/plugins/LinkAcc/lang/de_de.lang

/plugins/LinkAcc/lang/en_us.lang

/plugins/LinkAcc/config.yml
Select your preferred language in config.yml:

yaml
language: "en_us"

Done! The plugin is ready to use.
Commands
/link link <player> - Sends a link request to a player.
/link accept  - Accepts a link request —
/link deny - Declines a link request —
/link unlink - Unlinks the linked players.
/link list - Shows all active links on the server.
/link swap - Swaps the player data of two already linked players.
/link block <player|all> - Block requests
/link unblock <player|all> - Unblock requests
Data Handling
The player data files are located in the default folders:

world/playerdata/

world/stats/

world/advancements/
When linking and unlinking, all relevant files are safely swapped and stored.

Language Files
Inside /plugins/LinkAcc/lang/, you’ll find de_de.lang and en_us.lang.
You can customize these files to change in-game messages however you like.

Logs & Debugging
All important actions (like links, errors and data swaps) are logged to your server console.

 Deutsche Anleitung
Für Paper, Bukkit und Spigot Server verfügbar.
Was ist LinkAcc?
LinkAcc ist ein Minecraft-Plugin, mit dem du Spielerdaten zwischen zwei Spielern verlinken und tauschen kannst.
Ob für Challenges, Rollenspiele oder Admin-Zwecke — mit LinkAcc kontrollierst du, wer welche Playerdata besitzt.

Funktionen im Überblick:

Spieler können Verlinkungsanfragen senden

Beim Verlinken wird der Zielspieler gekickt und dessen Playerdata übernommen

Stats und Advancements werden ebenfalls getauscht

Möglichkeit, Verlinkungen wieder aufzuheben und die originalen Daten zurückzugeben

Join-Block für verlinkte Spieler, bis der Unlink erfolgt

Sprachdateien (de_de und en_us) automatisch erstellt

Alle Einstellungen über config.yml steuerbar
Installation
Plugin-Datei (LinkAcc.jar) in den Ordner /plugins/ deines Servers kopieren.

Server neu starten.

Es werden automatisch folgende Verzeichnisse und Dateien erzeugt:
/plugins/LinkAcc/lang/de_de.lang

/plugins/LinkAcc/lang/en_us.lang

/plugins/LinkAcc/config.yml
Wähle in der config.yml deine Sprache:

yaml
language: "de_de"

Fertig! Das Plugin ist einsatzbereit.
Befehle
/link link <Spieler> - Sendet eine Verlinkungsanfrage an den Spieler.
/link accept - Nimmt eine Verlinkungsanfrage an —
/link deny - Lehnen eine Verlinkungsanfrage ab —
/link unlink - Hebt die Verlinkung wieder auf.
/link list - Zeigt alle aktiven Verlinkungen auf dem Server.
/link swap - Tauscht die Playerdata zweier bereits verlinkter Spieler.
/link block <Spieler|all> - Anfragen blockieren
/link unblock <Spieler|all> - Blockierung entfernen
Daten
Die Playerdata-Dateien befinden sich im Standard-Ordner:

world/playerdata/

world/stats/

world/advancements/
Beim Verlinken und Unlinken werden alle relevanten Dateien ausgetauscht und gesichert.

Sprachdateien anpassen
In /plugins/LinkAcc/lang/ kannst du die de_de.lang und en_us.lang nach Belieben anpassen und eigene Messages schreiben.

Logs & Debug
Alle wichtigen Aktionen (wie Verlinkungen, Fehler und Daten-Swaps) werden im Server-Log angezeigt.
