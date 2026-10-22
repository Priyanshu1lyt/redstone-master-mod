# Redstone Master (Fabric) — Minecraft Java 1.21.11

An expandable client-side guide/library mod with a GUI, search, category filters, material lists, build steps, and command-block examples.

## Features in this starter project
- Press **R** to open the Redstone Master menu.
- Search the included build entries.
- Filter by All, Doors, Farms, Machines, Robots, and Command Builds.
- Read materials, difficulty, notes, and step-by-step instructions.
- Add new entries in `BuildLibrary.java`.

## Important limits
- This starter project is a tutorial/library UI, not a complete schematic renderer or one-click auto-builder yet.
- It does not include every redstone build ever made. The library is intentionally expandable.
- The included flying-machine and door entries are general guides; verify exact layouts in a creative test world.
- Command-block builds require cheats/permissions and may be disabled on multiplayer servers.
- Automatic block placement would need a dedicated schematic format and careful permission/survival-mode rules; this version does not silently place blocks.

## Requirements
- Minecraft Java Edition **1.21.11**
- Java **21**
- Fabric Loader and Fabric API compatible with 1.21.11
- Gradle (or Android IDE that supports Gradle projects)

## Build on a PC
1. Install Java 21 and a current Gradle-compatible IDE (IntelliJ IDEA recommended).
2. Open this folder as a Gradle project.
3. Let Gradle sync the Fabric Loom dependencies.
4. Run `gradlew build` (Windows) or `./gradlew build` (Linux/macOS).
5. Find the mod JAR in `build/libs/`.
6. Put the JAR into the Minecraft instance's `mods` folder with Fabric API installed.

## Android/mobile note
If you're building on Android, use an IDE/app that supports Gradle Java projects and Java 21. Build tooling on mobile can be more difficult; building on a PC is usually easier. After building, copy the JAR into the Fabric instance's `mods` folder.

## Change the menu key
The default key is R. In Minecraft, open **Options → Controls → Key Binds**, search for **Redstone Master**, then assign any key that is free. This is registered as a normal Minecraft key binding.

## Add another build
Add a `new BuildEntry(...)` item to `BuildLibrary.BUILDS` in `BuildLibrary.java`. Include:
- Name
- Category
- Difficulty
- Description
- Steps (`List.of(...)`)
- Materials (`List.of(...)`)
- Command note

Categories should match one of the current filters or you can add a new category button in `RedstoneMasterScreen.java`.

## Troubleshooting
- If Gradle cannot resolve a Fabric version, check the current Fabric Maven versions for Minecraft 1.21.11 and update `gradle.properties`.
- If the GUI crashes due to a mapping/API change, confirm the Minecraft/Yarn/Fabric API versions match your 1.21.11 environment.
- Test in a throwaway creative world before using any command-block examples.
