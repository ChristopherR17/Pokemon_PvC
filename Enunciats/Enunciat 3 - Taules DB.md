# Pokémon PvP - Taules DB

## Tasques

- Fer el diagrama entitat relació
- Implementar les taules amb **".sqlite"**
- Connectar cada partida amb una base de dades

Cal llegir les dades de la base de dades:

- Al mostrar les estadístiques del menú
- Al mostrar l'historial de batalles
- Al començar una nova batalla

Cal actualitzar les dades cap a la base de dades:

- Al final de cada batalla
- Al gestionar els Pokémons

## Taules

Aquesta és la relació de taules i atributs de la base de dades.

```sql
-- Stores the master list of all Pokémon species available in the game
CREATE TABLE Pokemon (
    id INTEGER PRIMARY KEY AUTOINCREMENT,        -- Unique ID for each species
    name TEXT NOT NULL,                          -- Official Pokémon name
    type TEXT NOT NULL,                          -- Pokémon type (e.g., Fire, Water)
    image_path TEXT                              -- Optional image for display
);

-- Represents each Pokémon owned by the player with custom nickname and stats
CREATE TABLE PlayerPokemon (
    id INTEGER PRIMARY KEY AUTOINCREMENT,        -- Unique ID for player's Pokémon
    pokemon_id INTEGER NOT NULL,                 -- Reference to Pokémon species
    nickname TEXT,                               -- Custom name given by the player
    max_hp INTEGER NOT NULL,                     -- Base HP
    attack INTEGER NOT NULL,                     -- Base Attack
    stamina INTEGER NOT NULL,                    -- Base Stamina
    unlocked BOOLEAN DEFAULT 0,                  -- Whether the player can use it
    FOREIGN KEY (pokemon_id) REFERENCES Pokemon(id)
);

-- All defined attacks in the game
CREATE TABLE Attack (
    id INTEGER PRIMARY KEY AUTOINCREMENT,        -- Unique ID for each attack
    name TEXT NOT NULL,                          -- Name of the attack
    type TEXT NOT NULL,                          -- Type of the attack
    damage INTEGER NOT NULL,                     -- Base damage if successful
    stamina_cost INTEGER NOT NULL                -- Stamina cost to perform the attack
);

-- Many-to-many relation between Pokémon and the attacks they can perform
CREATE TABLE PokemonAttack (
    pokemon_id INTEGER NOT NULL,                 -- Reference to Pokémon species
    attack_id INTEGER NOT NULL,                  -- Reference to an attack
    PRIMARY KEY (pokemon_id, attack_id),
    FOREIGN KEY (pokemon_id) REFERENCES Pokemon(id),
    FOREIGN KEY (attack_id) REFERENCES Attack(id)
);

-- Defines damage multipliers between attacking and defending types
CREATE TABLE TypeEffectiveness (
    attack_type TEXT NOT NULL,                   -- Type of the attack
    target_type TEXT NOT NULL,                   -- Type of the defending Pokémon
    multiplier REAL NOT NULL,                    -- Multiplier (e.g., 2.0, 0.5)
    PRIMARY KEY (attack_type, target_type)
);

-- Master list of available items
CREATE TABLE Item (
    id INTEGER PRIMARY KEY AUTOINCREMENT,        -- Unique ID
    name TEXT NOT NULL UNIQUE,                   -- Item name
    effect_type TEXT NOT NULL,                   -- Type of effect (e.g., attack boost)
    effect_value INTEGER                         -- Magnitude of the effect
);

-- Tracks how many of each item the player owns
CREATE TABLE ItemInventory (
    item_id INTEGER PRIMARY KEY,                 -- Item reference
    quantity INTEGER DEFAULT 0,                  -- Quantity available
    FOREIGN KEY (item_id) REFERENCES Item(id)
);

-- Persistent player statistics across all battles
CREATE TABLE GameStats (
    id INTEGER PRIMARY KEY CHECK (id = 1),       -- Only one row allowed
    total_experience INTEGER DEFAULT 0,          -- Total accumulated experience
    battles_played INTEGER DEFAULT 0,            -- Total number of battles
    max_win_streak INTEGER DEFAULT 0,            -- Best consecutive wins
    current_win_streak INTEGER DEFAULT 0         -- Current ongoing win streak
);

-- Stores global information for each battle played
CREATE TABLE Battle (
    id INTEGER PRIMARY KEY AUTOINCREMENT,        -- Unique ID for the battle
    date TEXT NOT NULL,                          -- Date and time of the battle
    map TEXT,                                    -- Map where battle took place
    winner TEXT CHECK(winner IN ('Player', 'Computer'))  -- Result of the battle
);

-- Registers all Pokémon (player and AI) that participated in a battle
CREATE TABLE BattlePokemon (
    battle_id INTEGER NOT NULL,                  -- Battle reference
    is_player BOOLEAN NOT NULL,                  -- TRUE if belongs to the player
    pokemon_id INTEGER NOT NULL,                 -- Reference to PlayerPokemon
    PRIMARY KEY (battle_id, is_player, pokemon_id),
    FOREIGN KEY (battle_id) REFERENCES Battle(id),
    FOREIGN KEY (pokemon_id) REFERENCES PlayerPokemon(id)
);

-- Tracks temporary item effects applied before the next battle
CREATE TABLE ItemEffect (
    player_pokemon_id INTEGER NOT NULL,          -- Pokémon affected
    item_id INTEGER NOT NULL,                    -- Item used
    active BOOLEAN DEFAULT 1,                    -- TRUE if the effect is pending
    PRIMARY KEY (player_pokemon_id, item_id),
    FOREIGN KEY (player_pokemon_id) REFERENCES PlayerPokemon(id),
    FOREIGN KEY (item_id) REFERENCES Item(id)
);

-- Records items rewarded after a battle
CREATE TABLE ItemReward (
    battle_id INTEGER NOT NULL,                  -- Related battle
    item_id INTEGER NOT NULL,                    -- Item awarded
    quantity INTEGER DEFAULT 1,                  -- Number of items received
    PRIMARY KEY (battle_id, item_id),
    FOREIGN KEY (battle_id) REFERENCES Battle(id),
    FOREIGN KEY (item_id) REFERENCES Item(id)
);
```