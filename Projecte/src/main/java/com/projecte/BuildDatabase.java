package com.projecte;
// ./run.sh com.projecte.BuildDatabase

public class BuildDatabase {

    public static void main(String[] args) {
        AppData db = AppData.getInstance();
        db.connect("../data/pokemons.sqlite");

        System.out.println("\nIniciar les dades de la base de dades:");
        initData();

        db.close();
    }

    public static void initData() {
        AppData db = AppData.getInstance();

        db.update("DROP TABLE IF EXISTS ItemReward");
        db.update("DROP TABLE IF EXISTS ItemEffect");
        db.update("DROP TABLE IF EXISTS BattlePokemon");
        db.update("DROP TABLE IF EXISTS Battle");
        db.update("DROP TABLE IF EXISTS GameStats");
        db.update("DROP TABLE IF EXISTS ItemInventory");
        db.update("DROP TABLE IF EXISTS Item");
        db.update("DROP TABLE IF EXISTS TypeEffectiveness");
        db.update("DROP TABLE IF EXISTS PokemonAttack");
        db.update("DROP TABLE IF EXISTS Attack");   
        db.update("DROP TABLE IF EXISTS PlayerPokemon");
        db.update("DROP TABLE IF EXISTS Player");
        db.update("DROP TABLE IF EXISTS Pokemon");

        db.update("""
            CREATE TABLE Pokemon (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                image_path TEXT
            )
        """);

        db.update("""
            CREATE TABLE Player (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT
            )
        """);

        db.update("""
            CREATE TABLE PlayerPokemon (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                pokemon_id INTEGER NOT NULL,
                nickname TEXT,
                max_hp INTEGER NOT NULL,
                attack INTEGER NOT NULL,
                stamina INTEGER NOT NULL,
                unlocked BOOLEAN DEFAULT 0,
                FOREIGN KEY (pokemon_id) REFERENCES Pokemon(id)
            )
        """);

        db.update("""
            CREATE TABLE Attack (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                damage INTEGER NOT NULL,
                stamina_cost INTEGER NOT NULL
            )
        """);

        db.update("""
            CREATE TABLE PokemonAttack (
                pokemon_id INTEGER NOT NULL,
                attack_id INTEGER NOT NULL,
                PRIMARY KEY (pokemon_id, attack_id),
                FOREIGN KEY (pokemon_id) REFERENCES Pokemon(id),
                FOREIGN KEY (attack_id) REFERENCES Attack(id)
            )
        """);

        db.update("""
            CREATE TABLE TypeEffectiveness (
                attack_type TEXT PRIMARY KEY NOT NULL,
                target_type TEXT NOT NULL,
                multiplier REAL NOT NULL
            )
        """);

        db.update("""
            CREATE TABLE Item (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                effect_type TEXT NOT NULL,
                effect_value INTEGER
            )
        """);

        db.update("""
            CREATE TABLE ItemInventory (
                item_id INTEGER PRIMARY KEY,
                quantity INTEGER DEFAULT 0,
                FOREIGN KEY (item_id) REFERENCES Item(id)
            )
        """);

        db.update("""
            CREATE TABLE GameStats (
                id INTEGER PRIMARY KEY CHECK (id = 1),
                total_experience INTEGER DEFAULT 0,
                battles_played INTEGER DEFAULT 0,
                max_win_streak INTEGER DEFAULT 0,
                current_win_streak INTEGER DEFAULT 0
            )
        """);

        db.update("""
            CREATE TABLE Battle (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT NOT NULL,
                map TEXT,
                winner TEXT CHECK(winner IN ('Player', 'Computer'))
            )
        """);

        db.update("""
            CREATE TABLE BattlePokemon (
                battle_id INTEGER NOT NULL,
                is_player BOOLEAN NOT NULL,
                pokemon_id INTEGER NOT NULL,
                PRIMARY KEY (battle_id, is_player, pokemon_id),
                FOREIGN KEY (battle_id) REFERENCES Battle(id),
                FOREIGN KEY (pokemon_id) REFERENCES PlayerPokemon(id)
            )
        """);

        db.update("""
            CREATE TABLE ItemEffect (
                player_pokemon_id INTEGER NOT NULL,
                item_id INTEGER NOT NULL,
                active BOOLEAN DEFAULT 1,
                PRIMARY KEY (player_pokemon_id, item_id),
                FOREIGN KEY (player_pokemon_id) REFERENCES PlayerPokemon(id),
                FOREIGN KEY (item_id) REFERENCES Item(id)
            )
        """);

        db.update("""
            CREATE TABLE ItemReward (
                battle_id INTEGER NOT NULL,
                item_id INTEGER NOT NULL,
                quantity INTEGER DEFAULT 1,
                PRIMARY KEY (battle_id, item_id),
                FOREIGN KEY (battle_id) REFERENCES Battle(id),
                FOREIGN KEY (item_id) REFERENCES Item(id)
            )
        """);

        System.out.println("Base de datos creada correctamente.");

        // try {
        //     // Insertar Pokémon
        //     String content = Files.readString(Paths.get("../data/pokemons.json"));
        //     JSONArray pokemons = new JSONArray(content);
        //     for (Object o : pokemons) {
        //         JSONObject p = (JSONObject) o;
        //         db.update(String.format(
        //             "INSERT INTO Pokemon (name, type, image_path) VALUES ('%s', '%s', '%s')",
        //             p.getString("name").replace("'", "''"),
        //             p.getString("type").replace("'", "''"),
        //             p.optString("image_path", "").replace("'", "''")
        //         ));
        //     }
        //     System.out.println("Pokémon insertados correctamente.");
        //     // Insertar Ataques
        //     content = Files.readString(Paths.get("../data/attacks.json"));
        //     JSONArray attacks = new JSONArray(content);
        //     for (Object o : attacks) {
        //         JSONObject a = (JSONObject) o;
        //         db.update(String.format(
        //             "INSERT INTO Attack (name, type, damage, stamina_cost) VALUES ('%s', '%s', %d, %d)",
        //             a.getString("name").replace("'", "''"),
        //             a.getString("type"),
        //             a.getInt("damage"),
        //             a.getInt("stamina_cost")
        //         ));
        //     }
        //     System.out.println("Ataques insertados correctamente.");

        //     // Vincular Pokémon con ataques
        //     content = Files.readString(Paths.get("../data/pokemon_attacks.json"));
        //     JSONArray pokAtts = new JSONArray(content);
        //     for (Object o : pokAtts) {
        //         JSONObject pa = (JSONObject) o;
        //         db.update(String.format(
        //             "INSERT INTO PokemonAttack (pokemon_id, attack_id) VALUES (%d, %d)",
        //             pa.getInt("pokemon_id"), 
        //              pa.getInt("attack_id")
        //         ));
        //     }
        //     System.out.println("Vínculos de Pokémon y ataques insertados correctamente.");
        
        //     // Efectividad de tipos
        //     // content = Files.readString(Paths.get("../data/type_effectiveness.json"));
        //     // JSONArray typeEff = new JSONArray(content);
        //     // for (Object o : typeEff) {
        //     //     JSONObject te = (JSONObject) o;
        //     //     db.update(String.format(
        //     //         "INSERT INTO TypeEffectiveness (attack_type, target_type, multiplier) VALUES ('%s', '%s', %.1f)",
        //     //         te.getString("attack_type"),
        //     //         te.getString("target_type"),
        //     //         te.getDouble("multiplier")
        //     //     ));
        //     // }
        //     // System.out.println("Efectividad de tipos insertada correctamente.");
        
        //     // Items
        //     content = Files.readString(Paths.get("../data/items.json"));
        //     JSONArray items = new JSONArray(content);
        //     for (Object o : items) {
        //         JSONObject it = (JSONObject) o;
        //         db.update(String.format(
        //             "INSERT INTO Item (name, effect_type, effect_value) VALUES ('%s', '%s', %d)",
        //             it.getString("name").replace("'", "''"),
        //             it.getString("effect_type"),
        //             it.getInt("effect_value")
        //         ));
        //     }
        //     System.out.println("Items insertados correctamente.");
        
        //     // Inventario de Items
        //     content = Files.readString(Paths.get("../data/item_inventory.json"));
        //     JSONArray inv = new JSONArray(content);
        //     for (Object o : inv) {
        //         JSONObject i = (JSONObject) o;
        //         db.update(String.format(
        //             "INSERT INTO ItemInventory (item_id, quantity) VALUES (%d, %d)",
        //             i.getInt("item_id"),
        //             i.getInt("quantity")
        //         ));
        //     }
        //     System.out.println("Inventario de items insertado correctamente.");
        
        //     System.out.println("Datos cargados correctamente desde JSON.");
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        
    }
}
