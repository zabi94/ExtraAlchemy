# Extra Alchemy
This is a Minecraft mod for Fabric that adds multiple new potions and small tweaks and trinkets related to potions and brewing.

## Features
- Many potions, too many to list here
- Potion Bags to keep all your potions in the same inventory and use them without effort.
- Potion Vials, an alternative stackable format for drinkable potions.
- Potion Rings, to constantly apply a status effect at the cost of some XP.
- Brewing Stand fueling through heat. Placing hot blocks underneath a brewing stand, or two blocks below with a conductive block in the middle.

## Configuration
#### General
Most features can be toggled in the configuration menu of this mod. Manual synchronization with server is required for the moment.

#### Potions
Every potion recipe in the brewing stand can be individually toggled through the "Potions" config menu. Existing potions and effect won't be removed.

#### Heat sources and Heat conductors
Heated blocks and Heat conductors can all be configured through Minecraft datapack's tag system. [Example](https://github.com/zabi94/ExtraAlchemy/tree/1.16-fabric/src/main/resources/data/extraalchemy/tags/blocks)

#### Rings
Rings can be configured through datapack recipes similar to the ones included in this mod. Cost, length and reapplication time are all defined there. Ring recipes are automatically synchronized to the clients. Setting a ring to produce a potion without status effects (eg: awkward, water, mundane, thick) will disable the recipe instead. [Example](https://github.com/zabi94/ExtraAlchemy/tree/1.16-fabric/src/main/resources/data/extraalchemy/recipes/ring_recipes)

## Support
The main development focus is now 1.15 and 1.16, with version 1.12 only updated to fix bugs and version 1.14 discontinued due to low interest and the high effort required.
A version for the forge mod loader is NOT available and will probably never be. **Ports to the forge mod loader are not allowed**, and I don't want to get in touch with people asking me for permissions, wishing to discuss, asking my reasons or any other 1.13+ forge related topic.

## Dependencies
This mod on 1.14+ depends on [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api). Please install the most recent file for the Minecraft version you're trying to use.
This mod bundles Cloth Config to provide an easy to use configuration screen when ModMenu is installed
