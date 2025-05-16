package com.projecte;
// ./run.sh com.projecte.BuildDatabase

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;


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
        db.update("DROP TABLE IF EXISTS PokemonAttacks");
        db.update("DROP TABLE IF EXISTS Attack");   
        db.update("DROP TABLE IF EXISTS PlayerPokemon");
        db.update("DROP TABLE IF EXISTS Player");
        db.update("DROP TABLE IF EXISTS Pokemon");
        db.update("DROP TABLE IF EXISTS BattleHistory");


        db.update("""
            CREATE TABLE Pokemon (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                image_front TEXT,
                image_back TEXT, 
                nickname TEXT,
                max_hp INTEGER NOT NULL,
                attack INTEGER NOT NULL,
                stamina INTEGER NOT NULL,
                unlocked BOOLEAN DEFAULT 0
            )
        """);

        db.update("""
            CREATE TABLE Player (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                image_path TEXT
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
            CREATE TABLE PokemonAttacks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                damage INTEGER NOT NULL,
                stamina_cost INTEGER NOT NULL,
                pokemon_name TEXT NOT NULL,
                type TEXT NOT NULL,
                FOREIGN KEY (pokemon_name) REFERENCES Pokemon(name)
            )
        """);

        db.update("""
            CREATE TABLE TypeEffectiveness (
                attack_type TEXT NOT NULL,
                target_type TEXT NOT NULL,
                multiplier REAL NOT NULL,
                PRIMARY KEY (attack_type, target_type)
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
                id INTEGER PRIMARY KEY AUTOINCREMENT,
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

        db.update("""
            CREATE TABLE BattleHistory (
                battle_id INTEGER PRIMARY KEY AUTOINCREMENT,
                trainer TEXT NOT NULL,
                map TEXT NOT NULL,
                result TEXT CHECK (result IN ('Ganado', 'Perdido')) NOT NULL,
                battle_date DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """);


        System.out.println("Base de datos creada correctamente.");

        try {
            // Insertar Pokémons
            String content = Files.readString(Paths.get("../data/pokemons.json"));
            JSONArray pokemons = new JSONArray(content);
            for (Object o : pokemons) {
                JSONObject p = (JSONObject) o;
                db.update(String.format(
                    "INSERT INTO Pokemon (id, name, type, image_front, image_back, nickname, max_hp, attack, stamina, unlocked) VALUES (%d, '%s', '%s', '%s', '%s', '%s', %d, %d, %d, %b)",
                    p.getInt("id"),
                    p.getString("name").replace("'", "''"),
                    p.getString("type").replace("'", "''"),
                    p.optString("image_front", "").replace("'", "''"),
                    p.optString("image_back", "").replace("'", "''"),
                    p.optString("nickname", "").replace("'", "''"), // Escapar comillas simples
                    p.getInt("max_hp"),
                    p.getInt("attack"),
                    p.getInt("stamina"),
                    p.getBoolean("unlocked")
                ));
            }
            System.out.println("Pokémons insertados correctamente.");

            // Insertar Ataques
            content = Files.readString(Paths.get("../data/attacks.json"));
            JSONArray attacks = new JSONArray(content);
            for (Object o : attacks) {
                JSONObject a = (JSONObject) o;
                db.update(String.format(
                    "INSERT INTO Attack (name, type, damage, stamina_cost) VALUES ('%s', '%s', %d, %d)",
                    a.getString("name").replace("'", "''"),
                    a.getString("type").replace("'", "''"),
                    a.getInt("damage"),
                    a.getInt("stamina_cost")
                ));
            }
            System.out.println("Ataques insertados correctamente.");

            // Vincular Pokémon con ataques
            content = Files.readString(Paths.get("../data/pokemon_attacks.json"));
            JSONArray pokAtts = new JSONArray(content);
            for (Object o : pokAtts) {
                JSONObject pa = (JSONObject) o;
                db.update(String.format(
                    "INSERT INTO PokemonAttacks (name, damage, stamina_cost, pokemon_name, type) VALUES ('%s', %d, %d, '%s', '%s')",
                    pa.getString("name").replace("'", "''"),
                    pa.getInt("damage"),
                    pa.getInt("staminaCost"),
                    pa.getString("pokemonName").replace("'", "''"),
                    pa.getString("type").replace("'", "''")
                ));
            }
            System.out.println("Vínculos de Pokémon y ataques insertados correctamente.");
        
            String[][] typeEffectiveness = {
                {"Electric", "Bug", "1.0"},
                {"Electric", "Dragon", "0.5"},
                {"Electric", "Fire", "1.0"},
                {"Electric", "Flying", "2.0"},
                {"Electric", "Grass", "0.5"},
                {"Electric", "Ground", "0.0"},
                {"Electric", "Water", "2.0"},
            
                {"Fire", "Fairy", "1.0"},
                {"Fire", "Fire", "0.5"},
                {"Fire", "Grass", "2.0"},
                {"Fire", "Ice", "2.0"},
                {"Fire", "Rock", "0.5"},
                {"Fire", "Steel", "2.0"},
                {"Fire", "Water", "0.5"},
                {"Fire", "Bug", "2.0"},
                {"Fire", "Ground", "1.0"},
            
                {"Ice", "Bug", "1.0"},
                {"Ice", "Dragon", "2.0"},
                {"Ice", "Fairy", "1.0"},
                {"Ice", "Fire", "0.5"},
                {"Ice", "Flying", "2.0"},
                {"Ice", "Grass", "2.0"},
                {"Ice", "Ground", "2.0"},
                {"Ice", "Steel", "0.5"},
                {"Ice", "Water", "0.5"},
            
                {"Poison", "Dragon", "1.0"},
                {"Poison", "Fairy", "2.0"},
                {"Poison", "Ghost", "0.5"},
                {"Poison", "Grass", "2.0"},
                {"Poison", "Ground", "0.5"},
                {"Poison", "Ice", "1.0"},
                {"Poison", "Steel", "0.0"},
            
                {"Water", "Dragon", "0.5"},
                {"Water", "Fairy", "1.0"},
                {"Water", "Flying", "1.0"},
                {"Water", "Grass", "0.5"},
                {"Water", "Ground", "2.0"},
                {"Water", "Ice", "1.0"},
                {"Water", "Rock", "2.0"},
                {"Water", "Water", "0.5"},
                {"Water", "Fire", "2.0"},
            
                {"Dragon", "Dragon", "2.0"},
                {"Dragon", "Steel", "0.5"},
                {"Dragon", "Fairy", "0.0"},
            
                {"Fairy", "Fighting", "2.0"},
                {"Fairy", "Dragon", "2.0"},
                {"Fairy", "Dark", "2.0"},
                {"Fairy", "Fire", "0.5"},
                {"Fairy", "Poison", "0.5"},
                {"Fairy", "Steel", "0.5"},
            
                {"Flying", "Fighting", "2.0"},
                {"Flying", "Bug", "2.0"},
                {"Flying", "Grass", "2.0"},
                {"Flying", "Electric", "0.5"},
                {"Flying", "Rock", "0.5"},
                {"Flying", "Steel", "0.5"},
            
                {"Grass", "Water", "2.0"},
                {"Grass", "Ground", "2.0"},
                {"Grass", "Rock", "2.0"},
                {"Grass", "Fire", "0.5"},
                {"Grass", "Grass", "0.5"},
                {"Grass", "Poison", "0.5"},
                {"Grass", "Flying", "0.5"},
                {"Grass", "Bug", "0.5"},
                {"Grass", "Dragon", "0.5"},
                {"Grass", "Steel", "0.5"},
            
                {"Ground", "Fire", "2.0"},
                {"Ground", "Electric", "2.0"},
                {"Ground", "Poison", "2.0"},
                {"Ground", "Rock", "2.0"},
                {"Ground", "Steel", "2.0"},
                {"Ground", "Grass", "0.5"},
                {"Ground", "Bug", "0.5"},
                {"Ground", "Flying", "0.0"},
            
                {"Rock", "Fire", "2.0"},
                {"Rock", "Ice", "2.0"},
                {"Rock", "Flying", "2.0"},
                {"Rock", "Bug", "2.0"},
                {"Rock", "Fighting", "0.5"},
                {"Rock", "Ground", "0.5"},
                {"Rock", "Steel", "0.5"},
            
                {"Steel", "Ice", "2.0"},
                {"Steel", "Rock", "2.0"},
                {"Steel", "Fairy", "2.0"},
                {"Steel", "Fire", "0.5"},
                {"Steel", "Water", "0.5"},
                {"Steel", "Electric", "0.5"},
                {"Steel", "Steel", "0.5"}
            };            

            for (String[] type : typeEffectiveness) {
                String attackType = type[0];
                String targetType = type[1];
                double multiplier = Double.parseDouble(type[2]);

                db.update("INSERT INTO TypeEffectiveness (attack_type, target_type, multiplier) VALUES ('" 
                    + attackType + "', '" + targetType + "', " + multiplier + ");");
            }
            System.out.println("Efectividad de tipos insertada correctamente.");
        
            // Items
            content = Files.readString(Paths.get("../data/items.json"));
            JSONArray items = new JSONArray(content);
            for (Object o : items) {
                JSONObject it = (JSONObject) o;
                db.update(String.format(
                    "INSERT INTO Item (name, effect_type, effect_value) VALUES ('%s', '%s', %d)",
                    it.getString("name").replace("'", "''"),
                    it.getString("effect_type"),
                    it.getInt("effect_value")
                ));
            }
            System.out.println("Items insertados correctamente.");
        
            // Inventario de Items
            content = Files.readString(Paths.get("../data/item_inventory.json"));
            JSONArray inv = new JSONArray(content);
            for (Object o : inv) {
                JSONObject i = (JSONObject) o;
                db.update(String.format(
                    "INSERT INTO ItemInventory (item_id, quantity) VALUES (%d, %d)",
                    i.getInt("item_id"),
                    i.getInt("quantity")
                ));
            }
            System.out.println("Inventario de items insertado correctamente.");
        
            System.out.println("Datos cargados correctamente desde JSON.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
