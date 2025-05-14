package com.projecte;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ControllerBattleAttack implements Initializable {

    // Fondo de batalla
    @FXML private ImageView battleBackground;
    
    // Información del Pokémon enemigo
    @FXML private Label enemyPokemonName;
    @FXML private ImageView enemyHealthBar;    // Barra de vida (se actualizará dinámicamente)
    @FXML private ImageView enemyStaminaBar;   // Barra de estamina (puedes implementar la actualización)
    @FXML private ImageView enemyPokemonImage;
    
    // Información del Pokémon del jugador
    @FXML private Label playerPokemonName;
    @FXML private ImageView playerHealthBar;     // Barra de vida
    @FXML private ImageView playerStaminaBar;    // Barra de estamina
    @FXML private ImageView playerPokemonImage;
    
    // Pokémon de reserva
    @FXML private ImageView backupPokemon1;
    @FXML private ImageView backupPokemon2;
    
    // Detalles de los ataques (contenedores y labels)
    @FXML private VBox attack1Container;
    @FXML private Label attack1Name;
    @FXML private Label attack1Damage;
    @FXML private Label attack1Type;
    
    @FXML private VBox attack2Container;
    @FXML private Label attack2Name;
    @FXML private Label attack2Damage;
    @FXML private Label attack2Type;
    
    @FXML private VBox attack3Container;
    @FXML private Label attack3Name;
    @FXML private Label attack3Damage;
    @FXML private Label attack3Type;
    
    @FXML private VBox attack4Container;
    @FXML private Label attack4Name;
    @FXML private Label attack4Damage;
    @FXML private Label attack4Type;
    
    // Datos de la batalla que se pasan desde la vista anterior.
    private String battleMap;
    private ArrayList<HashMap<String, Object>> playerTeam;
    private HashMap<String, Object> activePokemon;
    private HashMap<String, Object> enemyPokemon;
    
    // Variables para controlar la salud actual y máxima
    private int playerCurrentHP;
    private int enemyCurrentHP;
    private int playerMaxHP;
    private int enemyMaxHP;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Retrasar la ejecución hasta que la escena esté completamente cargada
        javafx.application.Platform.runLater(() -> {
            // Obtener el Stage actual
            Stage stage = (Stage) battleBackground.getScene().getWindow();

            // Recuperar el mapa seleccionado del Stage
            battleMap = (String) stage.getUserData();

            // Usar el mapa seleccionado para establecer el fondo
            if (battleMap != null) {
                setBattleMap();
                System.out.println("Mapa recibido en ControllerBattleAttack: " + battleMap);
            } else {
                System.err.println("No se recibió ningún mapa en ControllerBattleAttack.");
            }
        });
    }
    
    /**
     * Establece el mapa de batalla y carga el fondo correspondiente.
     * Se verifica que la imagen se encuentre en el classpath para evitar NullPointerException.
     */
    public void setBattleMap() {
        String map = battleMap.trim();
        String imagePath = "";
        if (map.equalsIgnoreCase("Bosque Verde")) {
            imagePath = "/img/maps/battleMaps/desierto.jpg";
        } else if (map.equalsIgnoreCase("Cueva Oscura")) {
            imagePath = "/img/maps/battleMaps/estadios.jpg";
        } else if (map.equalsIgnoreCase("Montaña Roca")) {
            imagePath = "/img/maps/battleMaps/psiquico.jpg";
        } else if (map.equalsIgnoreCase("Nieve Azulona")) {
            imagePath = "/img/maps/battleMaps/roca.png";
        } else if (map.equalsIgnoreCase("Gimnasio Elite")) {
            imagePath = "/img/maps/battleMaps/well.png";
        } else {
            System.err.println("Mapa desconocido: " + map);
            imagePath = "/img/bg/defaultBattleMap.png"; // Imagen predeterminada
        }

        // Cargar la imagen del mapa
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                battleBackground.setImage(new Image(imageUrl.toExternalForm()));
            } else {
                System.err.println("No se encontró la imagen del mapa: " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen del mapa: " + imagePath);
            e.printStackTrace();
        }
    }
    
    /**
     * Establece el equipo del jugador y carga las imágenes de los Pokémon de reserva
     * utilizando el campo "image_back".
     */
    public void setPlayerTeam(ArrayList<HashMap<String, Object>> team) {
        this.playerTeam = team;
        // Actualiza reserva 1
        if(team.size() > 1) {
            HashMap<String, Object> backup1Data = team.get(1);
            String backupPath = backup1Data.get("image_back").toString();
            URL backupUrl = getClass().getResource(backupPath);
            if(backupUrl != null) {
                backupPokemon1.setImage(new Image(backupUrl.toExternalForm()));
            } else {
                System.err.println("No se encontró la imagen de backup: " + backupPath);
            }
        }
        // Actualiza reserva 2
        if(team.size() > 2) {
            HashMap<String, Object> backup2Data = team.get(2);
            String backupPath = backup2Data.get("image_back").toString();
            URL backupUrl = getClass().getResource(backupPath);
            if(backupUrl != null) {
                backupPokemon2.setImage(new Image(backupUrl.toExternalForm()));
            } else {
                System.err.println("No se encontró la imagen de backup: " + backupPath);
            }
        }
    }
    
    /**
     * Establece el Pokémon activo del jugador actualizando su información:
     * nombre, imagen (usando "image_back"), y salud.
     * Además, se actualizan los detalles de los ataques de ejemplo.
     */
    public void setActivePokemon(HashMap<String, Object> pokemon) {
        this.activePokemon = pokemon;
        playerPokemonName.setText(pokemon.get("name").toString());
        String backPath = pokemon.get("image_back").toString();
        URL backUrl = getClass().getResource(backPath);
        if(backUrl != null) {
            playerPokemonImage.setImage(new Image(backUrl.toExternalForm()));
        } else {
            System.err.println("No se encontró la imagen del Pokémon activo (back): " + backPath);
        }
        playerCurrentHP = Integer.parseInt(pokemon.get("vida").toString());
        playerMaxHP = Integer.parseInt(pokemon.get("max_hp").toString());
        updatePlayerHealthBar();
        
        updateAttackDetails();
    }
    
    /**
     * Establece el Pokémon enemigo, actualizando su información:
     * nombre, imagen (usando "image_front") y salud.
     */
    public void setEnemyPokemon(HashMap<String, Object> pokemon) {
        this.enemyPokemon = pokemon;
        enemyPokemonName.setText(pokemon.get("name").toString());
        String frontPath = pokemon.get("image_front").toString();
        URL frontUrl = getClass().getResource(frontPath);
        if(frontUrl != null) {
            enemyPokemonImage.setImage(new Image(frontUrl.toExternalForm()));
        } else {
            System.err.println("No se encontró la imagen del Pokémon enemigo (front): " + frontPath);
        }
        enemyCurrentHP = Integer.parseInt(pokemon.get("vida").toString());
        enemyMaxHP = Integer.parseInt(pokemon.get("max_hp").toString());
        updateEnemyHealthBar();
    }
    
    /**
     * Actualiza los detalles de los ataques del Pokémon activo.
     * En este ejemplo se generan 4 ataques de muestra utilizando el valor de "attack" y "type".
     */
    public void updateAttackDetails() {
        if(activePokemon != null) {
            int baseAttack = Integer.parseInt(activePokemon.get("attack").toString());
            String pokeType = activePokemon.get("type").toString();
            
            attack1Name.setText("Flamethrower");
            attack1Damage.setText("Daño: " + baseAttack);
            attack1Type.setText("Tipo: " + pokeType);
            
            attack2Name.setText("Fire Spin");
            attack2Damage.setText("Daño: " + (baseAttack - 10));
            attack2Type.setText("Tipo: " + pokeType);
            
            attack3Name.setText("Scratch");
            attack3Damage.setText("Daño: 30");
            attack3Type.setText("Tipo: Normal");
            
            attack4Name.setText("Fire Blast");
            attack4Damage.setText("Daño: " + (baseAttack + 10));
            attack4Type.setText("Tipo: " + pokeType);
        }
    }
    
    /**
     * Maneja la selección de un ataque (índice 1 a 4). Simula la aplicación de daño
     * sobre el Pokémon enemigo y, si aún no ha sido derrotado, provoca un contraataque.
     */
    public void handleAttackSelection(int attackIndex) {
        int damage = 0;
        try {
            switch (attackIndex) {
                case 1:
                    damage = Integer.parseInt(attack1Damage.getText().replace("Daño: ", ""));
                    break;
                case 2:
                    damage = Integer.parseInt(attack2Damage.getText().replace("Daño: ", ""));
                    break;
                case 3:
                    damage = Integer.parseInt(attack3Damage.getText().replace("Daño: ", ""));
                    break;
                case 4:
                    damage = Integer.parseInt(attack4Damage.getText().replace("Daño: ", ""));
                    break;
                default:
                    System.out.println("Ataque inválido");
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
        System.out.println("Ataque " + attackIndex + " seleccionado con daño: " + damage);
        enemyCurrentHP -= damage;
        if (enemyCurrentHP < 0) enemyCurrentHP = 0;
        updateEnemyHealthBar();
        System.out.println("Salud enemigo: " + enemyCurrentHP + "/" + enemyMaxHP);
        
        if (enemyCurrentHP <= 0) {
            System.out.println("¡El enemigo ha sido derrotado!");
            // Aquí puedes desencadenar la transición a la vista de resultados, 
            // e insertar el resultado en la base de datos usando un valor permitido ("Ganado" o "Perdido").
        } else {
            enemyCounterAttack();
        }
    }
    
    /**
     * Simula el contraataque del enemigo aplicando un daño fijo al Pokémon del jugador.
     */
    private void enemyCounterAttack() {
        int enemyAtk = 30; // Danho fijo de ejemplo; puede basarse en enemyPokemon.get("attack")
        playerCurrentHP -= enemyAtk;
        if (playerCurrentHP < 0) playerCurrentHP = 0;
        updatePlayerHealthBar();
        System.out.println("Salud jugador: " + playerCurrentHP + "/" + playerMaxHP);
        if (playerCurrentHP <= 0) {
            System.out.println("¡Tu Pokémon ha sido derrotado!");
            // Aquí se maneja el fin de la batalla o el cambio a otro Pokémon.
        }
    }
    
    /**
     * Actualiza la visualización de la barra de vida del jugador 
     * ajustando el ancho proporcionalmente a un ancho base de 120.
     */
    private void updatePlayerHealthBar() {
        double porcentaje = (double) playerCurrentHP / playerMaxHP;
        playerHealthBar.setFitWidth(120 * porcentaje);
    }
    
    /**
     * Actualiza la visualización de la barra de vida del enemigo de forma similar.
     */
    private void updateEnemyHealthBar() {
        double porcentaje = (double) enemyCurrentHP / enemyMaxHP;
        enemyHealthBar.setFitWidth(120 * porcentaje);
    }
}
