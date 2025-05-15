package com.projecte;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class ControllerBattleAttack implements Initializable {

    // Fondo de batalla
    @FXML private ImageView battleBackground;
    
    // Información del Pokémon enemigo
    @FXML private Label enemyPokemonName;
    @FXML private Label enemyHealthLabel;    // Barra de vida (se actualizará dinámicamente)
    @FXML private Label enemyStaminaLabel;   // Barra de estamina (puedes implementar la actualización)
    @FXML private ImageView enemyPokemonImage;
    
    // Información del Pokémon del jugador
    @FXML private Label playerPokemonName;
    @FXML private Label playerHealthLabel;     // Barra de vida
    @FXML private Label playerStaminaLabel;    // Barra de estamina
    @FXML private ImageView playerPokemonImage;
    
    // Botón de retroceso
    @FXML 
    private Button backButton;


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
    private Pokemon[] playerTeam;

    // Pokémon enemigo (se puede obtener de la base de datos)
    private HashMap<String, Object> enemyPokemon;
    
    // Variables para controlar la salud actual y máxima
    private int playerCurrentHP;
    private int enemyCurrentHP;
    private int playerMaxHP;
    private int enemyMaxHP;

    // variables para controlar la estamina actual y máxima
    private int playerCurrentStamina;
    private int enemyCurrentStamina;
    private int playerMaxStamina;
    private int enemyMaxStamina;

    //Informacion del pokemon activo
    private ArrayList<HashMap<String, Object>> pokemonInfo;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Retrasar la ejecución hasta que la escena esté completamente cargada
        javafx.application.Platform.runLater(() -> {
            // Obtener el Stage actual
            Stage stage = (Stage) battleBackground.getScene().getWindow();

            // Recuperar el DTO del Stage
            Object data = stage.getUserData();
            if (data instanceof BattleDataDTO) {
                BattleDataDTO battleData = (BattleDataDTO) data;
                // Extraer el mapa y los Pokémon seleccionados
                battleMap = battleData.getBattleMap();
                playerTeam = battleData.getPlayerTeam();

                // Usar el mapa seleccionado para establecer el fondo
                if (battleMap != null) {
                    setBattleMap();
                    System.out.println("Mapa recibido en ControllerBattleAttack: " + battleMap);
                } else {
                    System.err.println("No se recibió ningún mapa en ControllerBattleAttack.");
                }

                // Llamar a setPlayerTeam para cargar los Pokémon de reserva
                if (playerTeam != null) {
                    setPlayerTeam();
                    setActivePokemon();
                    setEnemyPokemon();

                    backupPokemon1.setOnMouseClicked(_ -> switchActivePokemonByIndex(1));
                    backupPokemon2.setOnMouseClicked(_ -> switchActivePokemonByIndex(2));

                    // Añade listeners a los VBoxes de ataques
                    attack1Container.setOnMouseClicked(_ -> handleAttackVBoxClick(0));
                    attack2Container.setOnMouseClicked(_ -> handleAttackVBoxClick(1));
                    attack3Container.setOnMouseClicked(_ -> handleAttackVBoxClick(2));
                    attack4Container.setOnMouseClicked(_ -> handleAttackVBoxClick(3));
                    
                }
            } else {
                System.err.println("No se recibió el DTO esperado en ControllerBattleAttack.");
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
            imagePath = "/img/maps/battleMaps/psiquico.png";
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
    public void setPlayerTeam() {
        Pokemon[] team = this.playerTeam;

        AppData db = AppData.getInstance();
        ArrayList<HashMap<String, Object>> infoPokemon1 = db.query(String.format("SELECT * FROM pokemon WHERE name = '%s'", team[1].getName()));
        ArrayList<HashMap<String, Object>> infoPokemon2 = db.query(String.format("SELECT * FROM pokemon WHERE name = '%s'", team[2].getName()));

        // Cargar Pokémon de reserva 1
        String backupPath1 = infoPokemon1.get(0).get("image_front").toString();
        URL backupUrl1 = getClass().getResource(backupPath1);
        if (backupUrl1 != null) {
            backupPokemon1.setImage(new Image(backupUrl1.toExternalForm()));
        } else {
            System.err.println("No se encontró la imagen de reserva 1: " + backupPath1);
        }

        // Cargar Pokémon de reserva 2
        String backupPath2 = infoPokemon2.get(0).get("image_front").toString();
        URL backupUrl2 = getClass().getResource(backupPath2);
        if (backupUrl2 != null) {
            backupPokemon2.setImage(new Image(backupUrl2.toExternalForm()));
        } else {
            System.err.println("No se encontró la imagen de reserva 2: " + backupPath2);
        }
    }
    
    /**
     * Establece el Pokémon activo del jugador actualizando su información:
     * nombre, imagen (usando "image_back"), y salud.
     * Además, se actualizan los detalles de los ataques de ejemplo.
     */
    public void setActivePokemon() {
        Pokemon active = playerTeam[0]; // Suponiendo que el primer Pokémon es el activo
        AppData db = AppData.getInstance();
        ArrayList<HashMap<String, Object>> llista = db.query(String.format("SELECT * FROM pokemon WHERE name = '%s'", active.getName()));
        HashMap<String, Object> pokemon = llista.get(0);

        playerPokemonName.setText(active.getName());
        String backPath = pokemon.get("image_back").toString();
        URL backUrl = getClass().getResource(backPath);
        if(backUrl != null) {
            playerPokemonImage.setImage(new Image(backUrl.toExternalForm()));
        } else {
            System.err.println("No se encontró la imagen del Pokémon activo (back): " + backPath);
        }
        playerCurrentHP = Integer.parseInt(pokemon.get("max_hp").toString());
        playerMaxHP = Integer.parseInt(pokemon.get("max_hp").toString());
        updatePlayerHealthLabel();

        // acar la stamina del jugador
        playerCurrentStamina = Integer.parseInt(pokemon.get("stamina").toString());
        playerMaxStamina = Integer.parseInt(pokemon.get("stamina").toString());
        updatePlayerStaminaLabel(playerCurrentStamina, playerMaxStamina);

        updateAttackDetails();
    }
    
    /**
     * Establece el Pokémon enemigo, actualizando su información:
     * nombre, imagen (usando "image_front") y salud.
     */
    public void setEnemyPokemon() {
        Random rd = new Random();
        AppData db = AppData.getInstance();
        ArrayList<HashMap<String, Object>> enemyList = db.query(String.format("SELECT * FROM pokemon WHERE id = %d", rd.nextInt(1, 31)));

        enemyPokemon = enemyList.get(0);
        enemyPokemonName.setText(enemyPokemon.get("name").toString());
        String frontPath = enemyPokemon.get("image_front").toString();
        URL frontUrl = getClass().getResource(frontPath);
        if(frontUrl != null) {
            enemyPokemonImage.setImage(new Image(frontUrl.toExternalForm()));
        } else {
            System.err.println("No se encontró la imagen del Pokémon enemigo (front): " + frontPath);
        }
        enemyCurrentHP = Integer.parseInt(enemyPokemon.get("max_hp").toString());
        enemyMaxHP = Integer.parseInt(enemyPokemon.get("max_hp").toString());
        updateEnemyHealthLabel();

        // Aplicar la stamina del enemigo
        enemyCurrentStamina = Integer.parseInt(enemyPokemon.get("stamina").toString());
        enemyMaxStamina = Integer.parseInt(enemyPokemon.get("stamina").toString());
        updateEnemyStaminaLabel(enemyCurrentStamina, enemyMaxStamina);
    }

    @FXML
    private void switchActivePokemon(MouseEvent event) {
        ImageView clicked = (ImageView) event.getSource();

        if (clicked == backupPokemon1) {
            switchActivePokemonByIndex(1);
        } else if (clicked == backupPokemon2) {
            switchActivePokemonByIndex(2);
        }
    }

    private void switchActivePokemonByIndex(int benchIndex) {
        if (benchIndex < 1 || benchIndex > 2) return;

        Pokemon temp = playerTeam[0];
        playerTeam[0] = playerTeam[benchIndex];
        playerTeam[benchIndex] = temp;

        setPlayerTeam();
        setActivePokemon();
    }


    /**
     * Actualiza los detalles de los ataques del Pokémon activo.
     * En este ejemplo se generan 4 ataques de muestra utilizando el valor de "attack" y "type".
     */
    public void updateAttackDetails() {
        Pokemon activePokemon = playerTeam[0];
        AppData db = AppData.getInstance();
        pokemonInfo = db.query(String.format("SELECT pt.name, p.type, p.stamina, pt.damage, pt.stamina_cost FROM Pokemon p JOIN PokemonAttacks pt ON p.name = pt.pokemon_name WHERE pt.pokemon_name = '%s'", activePokemon.getName()));
        
        if (pokemonInfo.size() >= 4) {
        // Ataque 1
        HashMap<String, Object> attack1 = pokemonInfo.get(0);
        attack1Name.setText((String) attack1.get("name"));
        attack1Damage.setText("Daño: " + attack1.get("damage"));
        attack1Type.setText("Sta: " + attack1.get("stamina_cost"));

        // Ataque 2
        HashMap<String, Object> attack2 = pokemonInfo.get(1);
        attack2Name.setText((String) attack2.get("name"));
        attack2Damage.setText("Daño: " + attack2.get("damage"));
        attack2Type.setText("Sta: " + attack2.get("stamina_cost"));

        // Ataque 3
        HashMap<String, Object> attack3 = pokemonInfo.get(2);
        attack3Name.setText((String) attack3.get("name"));
        attack3Damage.setText("Daño: " + attack3.get("damage"));
        attack3Type.setText("Sta: " + attack3.get("stamina_cost"));

        // Ataque 4
        HashMap<String, Object> attack4 = pokemonInfo.get(3);
        attack4Name.setText((String) attack4.get("name"));
        attack4Damage.setText("Daño: " + attack4.get("damage"));
        attack4Type.setText("Sta: " + attack4.get("stamina_cost"));
        } else {
            System.err.println("El Pokémon no tiene 4 ataques registrados en la base de datos.");
        }
    }

    /**
     * Maneja el click sobre un VBox de ataque.
     * @param attackIndex El índice del ataque (0 a 3)
     */
    private void handleAttackVBoxClick(int attackIndex) {
        // Obtén el daño del ataque seleccionado
        int damage = 0;
        String type = "";
        int stamina = 0;

        switch (attackIndex) {
            case 0:
                damage = Integer.parseInt(attack1Damage.getText().replace("Daño: ", ""));
                // type = attack1Type.getText().replace("Tipo: ", "");
                stamina = Integer.parseInt(attack1Type.getText().replace("Sta: ", ""));
                break;
            case 1:
                damage = Integer.parseInt(attack2Damage.getText().replace("Daño: ", ""));
                // type = attack2Type.getText().replace("Tipo: ", "");
                stamina = Integer.parseInt(attack2Type.getText().replace("Sta: ", ""));
                break;
            case 2:
                damage = Integer.parseInt(attack3Damage.getText().replace("Daño: ", ""));
                // type = attack3Type.getText().replace("Tipo: ", "");
                stamina = Integer.parseInt(attack3Type.getText().replace("Sta: ", ""));
                break;
            case 3:
                damage = Integer.parseInt(attack4Damage.getText().replace("Daño: ", ""));
                // type = attack4Type.getText().replace("Tipo: ", "");
                stamina = Integer.parseInt(attack4Type.getText().replace("Sta: ", ""));
                break;
        }

        // Resta el daño a la vida del enemigo y actualiza la etiqueta
        enemyCurrentHP -= damage;
        playerCurrentStamina -= stamina;
        if (enemyCurrentHP <= 0) enemyCurrentHP = 0;
        updatePlayerStaminaLabel(playerCurrentStamina, playerMaxStamina);
        updateEnemyHealthLabel();

        System.out.println("Has usado un ataque de tipo " + type + " con daño " + damage + ". Vida del enemigo: " + enemyCurrentHP + "/" + enemyMaxHP);

        // Aquí puedes añadir lógica extra, como comprobar si el enemigo ha sido derrotado, etc.
    }
    
    /**
     * Maneja la selección de un ataque (índice 1 a 4). Simula la aplicación de daño
     * sobre el Pokémon enemigo y, si aún no ha sido derrotado, provoca un contraataque.
     */
    // public void handleAttackSelection(int attackIndex) {
    //     int damage = 0;
    //     try {
    //         switch (attackIndex) {
    //             case 1:
    //                 damage = Integer.parseInt(attack1Damage.getText().replace("Daño: ", ""));
    //                 break;
    //             case 2:
    //                 damage = Integer.parseInt(attack2Damage.getText().replace("Daño: ", ""));
    //                 break;
    //             case 3:
    //                 damage = Integer.parseInt(attack3Damage.getText().replace("Daño: ", ""));
    //                 break;
    //             case 4:
    //                 damage = Integer.parseInt(attack4Damage.getText().replace("Daño: ", ""));
    //                 break;
    //             default:
    //                 System.out.println("Ataque inválido");
    //                 return;
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return;
    //     }
        
    //     System.out.println("Ataque " + attackIndex + " seleccionado con daño: " + damage);
    //     enemyCurrentHP -= damage;
    //     if (enemyCurrentHP < 0) enemyCurrentHP = 0;
    //     updateEnemyHealthLabel();
    //     System.out.println("Salud enemigo: " + enemyCurrentHP + "/" + enemyMaxHP);
        
    //     if (enemyCurrentHP <= 0) {
    //         System.out.println("¡El enemigo ha sido derrotado!");
    //         // Aquí puedes desencadenar la transición a la vista de resultados, 
    //         // e insertar el resultado en la base de datos usando un valor permitido ("Ganado" o "Perdido").
    //     } else {
    //         enemyCounterAttack();
    //     }
    // }
    
    /**
     * Simula el contraataque del enemigo aplicando un daño fijo al Pokémon del jugador.
     */
    private void enemyCounterAttack() {
        int enemyAtk = 30; // Danho fijo de ejemplo; puede basarse en enemyPokemon.get("attack")
        playerCurrentHP -= enemyAtk;
        if (playerCurrentHP < 0) playerCurrentHP = 0;
        updatePlayerHealthLabel();
        System.out.println("Salud jugador: " + playerCurrentHP + "/" + playerMaxHP);
        if (playerCurrentHP <= 0) {
            System.out.println("¡Tu Pokémon ha sido derrotado!");
            // Aquí se maneja el fin de la batalla o el cambio a otro Pokémon.
            
        }
    }
    
    /**
     * Actualiza la visualización de la vida del jugador en un Label.
     */
    private void updatePlayerHealthLabel() {
        playerHealthLabel.setText(playerCurrentHP + " / " + playerMaxHP);
    }

    /**
     * Actualiza la visualización de la vida del enemigo en un Label.
     */
    private void updateEnemyHealthLabel() {
        enemyHealthLabel.setText(enemyCurrentHP + " / " + enemyMaxHP);
    }

    /**
     * Actualiza la visualización de la estamina del jugador en un Label.
     */
    private void updatePlayerStaminaLabel(int currentStamina, int maxStamina) {
        if (playerStaminaLabel != null) {
            playerStaminaLabel.setText(currentStamina + " / " + maxStamina);
        } else {
            System.err.println("playerStaminaLabel is not initialized.");
        }
    }

    /**
     * Actualiza la visualización de la estamina del enemigo en un Label.
     */
    private void updateEnemyStaminaLabel(int currentStamina, int maxStamina) {
        if (enemyStaminaLabel != null) {
            enemyStaminaLabel.setText(currentStamina + " / " + maxStamina);
        } else {
            System.err.println("enemyStaminaLabel is not initialized.");
        }
    }

    @FXML
    private void handleBackButton() {
    System.out.println("Botón 'Back' presionado. Navegando hacia la vista anterior...");
    // Aquí implementa la navegación a la vista anterior
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/viewMaps.fxml")); 
        Scene newScene = new Scene(loader.load());
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(newScene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Error al cargar la vista anterior: " + e.getMessage());
    }
    }
}
