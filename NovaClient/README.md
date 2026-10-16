# Nova Client (Fabric 1.21.11)

## Bauen
Java 21 und Internet noetig (Gradle laedt Minecraft + Fabric).

    ./gradlew build        (Windows: gradlew.bat build)

Die fertige Mod liegt danach in `build/libs/novaclient-1.0.0.jar`.
In den `mods`-Ordner legen, zusammen mit Fabric Loader und Fabric API fuer 1.21.11.

## Benutzen
- **Rechts-Shift**: Menue auf/zu (Taste ist in den Steuerungs-Optionen aenderbar)
- Modul anklicken = an/aus. Einstellungen werden in `config/novaclient.properties` gespeichert.
- **C** halten: Zoom

## Module
COMBAT: Hitboxes, Eye Line, Target Color, Players Only
MISC: Toggle Sprint, Zoom, CPS Counter
RENDER: HUD, Coordinates, Fullbright
