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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;



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
    @FXML private Button backButton;

    // Pokémon de reserva
    @FXML private ImageView backupPokemon1;
    @FXML private ImageView backupPokemon2;
    
    // Detalles de los ataques (contenedores y labels)
    @FXML private VBox attack1Container;
    @FXML private Label attack1Name;
    @FXML private Label attack1Damage;
    @FXML private Label attack1Stamina;
    @FXML private Label attack1Type;
    
    @FXML private VBox attack2Container;
    @FXML private Label attack2Name;
    @FXML private Label attack2Damage;
    @FXML private Label attack2Stamina;
    @FXML private Label attack2Type;
    
    @FXML private VBox attack3Container;
    @FXML private Label attack3Name;
    @FXML private Label attack3Damage;
    @FXML private Label attack3Stamina;
    @FXML private Label attack3Type;
    
    @FXML private VBox attack4Container;
    @FXML private Label attack4Name;
    @FXML private Label attack4Damage;
    @FXML private Label attack4Stamina;
    @FXML private Label attack4Type;
    
    // Datos de la batalla que se pasan desde la vista anterior.
    private String battleMap;
    private Pokemon[] playerTeam;

    // Pokémon enemigo (se puede obtener de la base de datos)
    private HashMap<String, Object> enemyPokemon;
    private ArrayList<HashMap<String, Object>> enemyTeam = new ArrayList<>();
    private int currentEnemyIndex = 0;
    
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

    //Inicializamos el objeto Random
    Random rd = new Random();
    
    /**
     * Inicializa la escena de batalla, carga los datos del DTO, el fondo del mapa,
     * el equipo del jugador y los Pokémon enemigos.
     */
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

                    // Cargar 3 enemigos aleatorios al inicio
                    AppData db = AppData.getInstance();
                    enemyTeam = db.query("SELECT * FROM pokemon ORDER BY RANDOM() LIMIT 3");
                    currentEnemyIndex = 0;
                    loadEnemyPokemon(currentEnemyIndex);

                    backupPokemon1.setOnMouseClicked(_ -> switchActivePokemonByIndex(1));
                    backupPokemon2.setOnMouseClicked(_ -> switchActivePokemonByIndex(2));
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
        } 

        // Cargar la imagen del mapa
        try {
            setImageFromResource(battleBackground, imagePath, "No se encontró la imagen del mapa: ");
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
        setImageFromResource(backupPokemon1, backupPath1, "No se encontró la imagen de reserva 1: ");

        // Cargar Pokémon de reserva 2
        String backupPath2 = infoPokemon2.get(0).get("image_front").toString();
        setImageFromResource(backupPokemon2, backupPath2, "No se encontró la imagen de reserva 2: ");

        if (playerTeam[1].isDefeated()) {
            backupPokemon1.setOpacity(0.5);
            backupPokemon1.setDisable(true);
        } else {
            backupPokemon1.setOpacity(1.0);
            backupPokemon1.setDisable(false);
        }
        if (playerTeam[2].isDefeated()) {
            backupPokemon2.setOpacity(0.5);
            backupPokemon2.setDisable(true);
        } else {
            backupPokemon2.setOpacity(1.0);
            backupPokemon2.setDisable(false);
        }
    }
    
    /**
     * Establece el Pokémon activo del jugador actualizando su información:
     * nombre, imagen (usando "image_back"), y salud.
     * Además, se actualizan los detalles de los ataques de ejemplo.
     */
    public void setActivePokemon() {
        Pokemon active = playerTeam[0];
        AppData db = AppData.getInstance();
        ArrayList<HashMap<String, Object>> pokemonInfo = db.query(String.format("SELECT * FROM pokemon WHERE name = '%s'", active.getName()));
        HashMap<String, Object> pokemon = pokemonInfo.get(0);

        active.setType(pokemon.get("type").toString());
        playerPokemonName.setText(active.getName());
        String backPath = pokemon.get("image_back").toString();
        setImageFromResource(playerPokemonImage, backPath, "No se encontró la imagen del Pokémon activo (back): ");

        // Solo asigna los valores máximos una vez, si los campos actuales no están inicializados
        int maxHP = Integer.parseInt(pokemon.get("max_hp").toString());
        int maxStamina = Integer.parseInt(pokemon.get("stamina").toString());
        playerMaxHP = maxHP;
        playerMaxStamina = maxStamina;

        // Si el Pokémon nunca fue usado, inicializa sus valores actuales
        if (active.getCurrentHP() == 0) active.setCurrentHP(maxHP);
        if (active.getCurrentStamina() == 0) active.setCurrentStamina(maxStamina);

        // Usa los valores actuales almacenados en el objeto
        playerCurrentHP = active.getCurrentHP();
        playerCurrentStamina = active.getCurrentStamina();

        updatePlayerHealthLabel();
        updatePlayerStaminaLabel(playerCurrentStamina, playerMaxStamina);

        updateAttackDetails();
    }
    
    /**
     * Establece el Pokémon enemigo, actualizando su información:
     * nombre, imagen (usando "image_front") y salud.
     * @param index Índice del Pokémon enemigo en la lista enemyTeam.
     */
    private void loadEnemyPokemon(int index) {
        if (index >= enemyTeam.size()) {
            showAlert("¡Has derrotado a todos los Pokémon enemigos!", Alert.AlertType.INFORMATION);
            // Aquí puedes terminar la batalla, volver al menú, etc.
            return;
        }
        enemyPokemon = enemyTeam.get(index);
        enemyPokemonName.setText(enemyPokemon.get("name").toString());
        String frontPath = enemyPokemon.get("image_front").toString();
        setImageFromResource(enemyPokemonImage, frontPath, "No se encontró la imagen del Pokémon enemigo (front): ");
        enemyCurrentHP = Integer.parseInt(enemyPokemon.get("max_hp").toString());
        enemyMaxHP = Integer.parseInt(enemyPokemon.get("max_hp").toString());
        enemyCurrentStamina = Integer.parseInt(enemyPokemon.get("stamina").toString());
        enemyMaxStamina = Integer.parseInt(enemyPokemon.get("stamina").toString());
        updateEnemyHealthLabel();
        updateEnemyStaminaLabel(enemyCurrentStamina, enemyMaxStamina);
    }

    /**
     * Cambia al siguiente Pokémon enemigo en la lista. Si no quedan más, muestra un mensaje de victoria.
     */
    private void nextEnemyPokemon() {
        currentEnemyIndex++;
        if (currentEnemyIndex < enemyTeam.size()) {
            showAlert("¡El enemigo ha cambiado de Pokémon!", Alert.AlertType.INFORMATION);
            loadEnemyPokemon(currentEnemyIndex);
        } else {
            showAlert("¡Has derrotado a todos los Pokémon enemigos!", Alert.AlertType.INFORMATION);
            // Todos los Pokémon enemigos han sido derrotados
            openAttackResultView(true); // true = victoria del jugador
            // Aquí puedes terminar la batalla, volver al menú, etc.
        }
    }

    /**
     * Cambia el Pokémon activo del jugador por uno de los de reserva al hacer clic en su imagen.
     * @param event Evento de ratón asociado al clic en el ImageView.
     */
    @FXML
    private void switchActivePokemon(MouseEvent event) {
        ImageView clicked = (ImageView) event.getSource();

        if (clicked == backupPokemon1) {
            switchActivePokemonByIndex(1);
        } else if (clicked == backupPokemon2) {
            switchActivePokemonByIndex(2);
        }
    }

    /**
     * Cambia el Pokémon activo por el de la banca indicado por el índice.
     * Guarda el estado del Pokémon activo antes de intercambiarlo.
     * @param benchIndex Índice del Pokémon de la banca (1 o 2).
     */
    private void switchActivePokemonByIndex(int benchIndex) {
        if (benchIndex < 1 || benchIndex > 2) return;
        if (playerTeam[benchIndex].isDefeated()) {
            showAlert("¡Ese Pokémon ya está derrotado y no puede combatir!", Alert.AlertType.WARNING);
            return;
        }
        // Guarda el estado del Pokémon activo actual
        playerTeam[0].setCurrentHP(playerCurrentHP);
        playerTeam[0].setCurrentStamina(playerCurrentStamina);

        // Intercambia el Pokémon activo con el de la banca
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
        Pokemon activePokemon = playerTeam[0];  // Suponiendo que playerTeam está definido
        AppData db = AppData.getInstance();

        String query = String.format(
            "SELECT pt.name, pt.damage, pt.stamina_cost, pt.type " +
            "FROM Pokemon p JOIN PokemonAttacks pt ON p.name = pt.pokemon_name " +
            "WHERE pt.pokemon_name = '%s'", activePokemon.getName());

        ArrayList<HashMap<String, Object>> pokemonInfo = db.query(query);

        if (pokemonInfo.size() < 4) {
            System.err.println("El Pokémon no tiene 4 ataques registrados en la base de datos.");
            return;
        }

        // Helper para asignar datos
        setAttackData(attack1Name, attack1Damage, attack1Stamina, attack1Type,pokemonInfo.get(0));
        setAttackData(attack2Name, attack2Damage, attack2Stamina, attack2Type,pokemonInfo.get(1));
        setAttackData(attack3Name, attack3Damage, attack3Stamina, attack3Type,pokemonInfo.get(2));
        setAttackData(attack4Name, attack4Damage, attack4Stamina, attack4Type,pokemonInfo.get(3));

        // Opcional: Agregar manejo de clics
        attack1Container.setOnMouseClicked(_ -> handleAttackSelection(1));
        attack2Container.setOnMouseClicked(_ -> handleAttackSelection(2));
        attack3Container.setOnMouseClicked(_ -> handleAttackSelection(3));
        attack4Container.setOnMouseClicked(_ -> handleAttackSelection(4));
    }

    /**
     * Asigna los datos de un ataque a los labels correspondientes.
     * @param nameLabel Label para el nombre del ataque.
     * @param damageLabel Label para el daño del ataque.
     * @param staminaLabel Label para el coste de estamina del ataque.
     * @param attackData HashMap con los datos del ataque.
     */
    private void setAttackData(Label nameLabel, Label damageLabel, Label staminaLabel, Label typeLabel, HashMap<String, Object> attackData) {
        String name = (String) attackData.get("name");
        int damage = Integer.parseInt(attackData.get("damage").toString());
        int stamina = Integer.parseInt(attackData.get("stamina_cost").toString());
        String type = (String) attackData.get("type");

        nameLabel.setText(name);
        damageLabel.setText("Daño: " + damage);
        staminaLabel.setText("Sta: " + stamina);
        typeLabel.setText("Tipo: " + type);
    }

    
    /**
     * Maneja la selección de un ataque (índice 1 a 4). Simula la aplicación de daño
     * sobre el Pokémon enemigo y, si aún no ha sido derrotado, provoca un contraataque.
     * @param attackIndex Índice del ataque seleccionado (1 a 4).
     */
    public void handleAttackSelection(int attackIndex) {
        int damage = 0;
        int staminaCost = 0;
        String attackType = "";
        try {
            switch (attackIndex) {
                case 1:
                    damage = Integer.parseInt(attack1Damage.getText().replace("Daño: ", ""));
                    staminaCost = Integer.parseInt(attack1Stamina.getText().replace("Sta: ", ""));
                    attackType = attack1Type.getText().replace("Tipo: ", "");
                    break;
                case 2:
                    damage = Integer.parseInt(attack2Damage.getText().replace("Daño: ", ""));
                    staminaCost = Integer.parseInt(attack2Stamina.getText().replace("Sta: ", ""));
                    attackType = attack2Type.getText().replace("Tipo: ", "");
                    break;
                case 3:
                    damage = Integer.parseInt(attack3Damage.getText().replace("Daño: ", ""));
                    staminaCost = Integer.parseInt(attack3Stamina.getText().replace("Sta: ", ""));
                    attackType = attack3Type.getText().replace("Tipo: ", "");
                    break;
                case 4:
                    damage = Integer.parseInt(attack4Damage.getText().replace("Daño: ", ""));
                    staminaCost = Integer.parseInt(attack4Stamina.getText().replace("Sta: ", ""));
                    attackType = attack4Type.getText().replace("Tipo: ", "");
                    break;
                default:
                    System.out.println("Ataque inválido");
                    return;
            }

            String enemyType = enemyPokemon.get("type").toString();

            double multiplier = getTypeMultiplier(attackType, enemyType);
            int finalDamage = (int) Math.round(damage * multiplier);

            // Verificar si hay estamina suficiente
            if (playerCurrentStamina < staminaCost) {
                System.out.println("¡No tienes suficiente estamina para este ataque!");
                return;
            }

            // Aplicar daño
            enemyCurrentHP -= finalDamage;
            playerCurrentStamina -= staminaCost;

            if (enemyCurrentHP < 0) enemyCurrentHP = 0;

            updateEnemyHealthLabel();
            updatePlayerStaminaLabel(playerCurrentStamina, playerMaxStamina);

            System.out.println("Ataque " + attackIndex + " hizo " + damage + " de daño. Estamina restante: " + playerCurrentStamina);

            if (enemyCurrentHP <= 0 || enemyCurrentStamina <= 0) {
                System.out.println("¡El enemigo ha sido derrotado!");
                showAlert("¡Has derrotado al Pokémon enemigo!", Alert.AlertType.INFORMATION);
                nextEnemyPokemon();
                return;
            } else {
                enemyCounterAttack(rd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Simula el contraataque del enemigo aplicando un daño aleatorio al Pokémon del jugador.
     * @param rd Objeto Random para seleccionar el ataque enemigo.
     */
    private void enemyCounterAttack(Random rd) {     
        AppData db = AppData.getInstance();
        String query = String.format(
            "SELECT name, damage, stamina_cost, type FROM PokemonAttacks WHERE pokemon_name = '%s'",
            enemyPokemon.get("name").toString()
        );

        ArrayList<HashMap<String, Object>> llista = db.query(query);
        int enemyAttackIndex = rd.nextInt(llista.size());
        HashMap<String, Object> enemyAttack = llista.get(enemyAttackIndex);

        int enemyAtk = Integer.parseInt(enemyAttack.get("damage").toString());
        String enemyAttackType = enemyAttack.get("type").toString();
        double multiplier = getTypeMultiplier(enemyAttackType, playerTeam[0].getType());
        int finalDamage = (int) Math.round(enemyAtk * multiplier);
        playerCurrentHP -= finalDamage;
        int staminaCost = Integer.parseInt(enemyAttack.get("stamina_cost").toString());
        enemyCurrentStamina -= staminaCost;
        updateEnemyStaminaLabel(enemyCurrentStamina, enemyMaxStamina);

        if (playerCurrentHP < 0) playerCurrentHP = 0;
        updatePlayerHealthLabel();
        System.out.println("Salud jugador: " + playerCurrentHP + "/" + playerMaxHP);
        if (enemyCurrentStamina <= 0) {
            System.out.println("¡El Pokémon enemigo se ha quedado sin estamina!");
            nextEnemyPokemon();
            return;
        }
        if (playerCurrentHP <= 0 || playerCurrentStamina <= 0) {
            System.out.println("¡Tu Pokémon ha sido derrotado!");
            showAlert("¡Tu Pokémon ha sido derrotado!", Alert.AlertType.WARNING);
            playerTeam[0].setDefeated(true); // Marcar como derrotado
            forceSwitchPokemon();
            return;
        }
    }

    private void forceSwitchPokemon() {
        // Busca el primer Pokémon de la banca que NO esté derrotado
        for (int i = 1; i < playerTeam.length; i++) {
            if (!playerTeam[i].isDefeated()) {
                switchActivePokemonByIndex(i);
                showAlert("¡Has enviado a " + playerTeam[0].getName() + " al combate!", Alert.AlertType.INFORMATION);
                return;
            }
        }
        // Si todos están derrotados, termina la batalla
        showAlert("¡Todos tus Pokémon han sido derrotados!", Alert.AlertType.ERROR);
        // Aquí puedes volver al menú o mostrar pantalla de derrota
        // Si todos están derrotados, termina la batalla
        openAttackResultView(false); // false = derrota del jugador
    }

    // Abre la vista de resultados de la batalla
    private void openAttackResultView(boolean playerWon) {
        try {
            // 1. Preparar datos para la base de datos
            String trainerName = Player.getInstance().getName(); 
            String mapName = (battleMap != null && !battleMap.trim().isEmpty()) ? 
                            battleMap : "Desconocido";
            String result = playerWon ? "Victoria" : "Derrota";
            String currentTime = new java.sql.Timestamp(System.currentTimeMillis()).toString();
            
            // 2. Validar y registrar en la base de datos
            try {
                AppData db = AppData.getInstance();
                // Usamos el método update estándar con parámetros escapados
                String query = String.format(
                    "INSERT INTO BattleHistory (trainer, map, result, battle_date) VALUES ('%s', '%s', '%s', '%s')",
                    escapeSql(trainerName),
                    escapeSql(mapName),
                    escapeSql(result),
                    currentTime
                );
                db.update(query);
            } catch (Exception dbError) {
                System.err.println("Error al guardar en historial de batallas: " + dbError.getMessage());
                // No detenemos el flujo aunque falle el registro
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/ViewAttackResult.fxml"));
            Parent root = loader.load();
            
            ControllerBattleResult controller = loader.getController();
            if (controller == null) {
                throw new IllegalStateException("El controlador de ViewAttackResult.fxml no se inicializó correctamente");
            }
            
            controller.setBattleResult(playerWon, playerTeam);
            
            Stage currentStage = (Stage) battleBackground.getScene().getWindow();
            Scene newScene = new Scene(root);
            newScene.getRoot().prefWidth(currentStage.getWidth());
            newScene.getRoot().prefHeight(currentStage.getHeight());
            currentStage.setScene(newScene);
            currentStage.centerOnScreen();
            
        } catch (IOException e) {
            System.err.println("Error crítico al cargar la vista de resultados:");
            e.printStackTrace();
            
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("No se pudo cargar la pantalla de resultados");
            errorAlert.setContentText("Por favor, reinicie la aplicación.");
            errorAlert.showAndWait();
        } catch (Exception unexpectedError) {
            System.err.println("Error inesperado al mostrar resultados:");
            unexpectedError.printStackTrace();
        }
    }

    // Método auxiliar para escapar caracteres especiales en SQL
    private String escapeSql(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''");
    }


    /**
     * Actualiza la visualización de la vida del jugador en un Label.
     */
    private void updatePlayerHealthLabel() {
        updateStatLabel(playerHealthLabel, playerCurrentHP, playerMaxHP, "playerHealthLabel");
    }

    /**
     * Actualiza la visualización de la vida del enemigo en un Label.
     */
    private void updateEnemyHealthLabel() {
        updateStatLabel(enemyHealthLabel, enemyCurrentHP, enemyMaxHP, "enemyHealthLabel");
    }

    /**
     * Actualiza la visualización de la estamina del jugador en un Label.
     * @param currentStamina Valor actual de estamina.
     * @param maxStamina Valor máximo de estamina.
     */
    private void updatePlayerStaminaLabel(int currentStamina, int maxStamina) {
        updateStatLabel(playerStaminaLabel, currentStamina, maxStamina, "playerStaminaLabel");
    }

    /**
     * Actualiza la visualización de la estamina del enemigo en un Label.
     * @param currentStamina Valor actual de estamina.
     * @param maxStamina Valor máximo de estamina.
     */
    private void updateEnemyStaminaLabel(int currentStamina, int maxStamina) {
        updateStatLabel(enemyStaminaLabel, currentStamina, maxStamina, "enemyStaminaLabel");
    }

    /**
     * Devuelve el multiplicador de daño según la efectividad del tipo de ataque contra el tipo del Pokémon enemigo.
     * @param attackType Tipo del ataque (por ejemplo, "Fuego").
     * @param enemyType Tipo del Pokémon enemigo (por ejemplo, "Planta").
     * @return Multiplicador de daño (ej: 2.0 si es muy eficaz, 0.5 si es poco eficaz, 1.0 si es normal).
     */
    private double getTypeMultiplier(String attackType, String enemyType) {
        AppData db = AppData.getInstance();
        // Aquí puedes implementar una consulta a la base de datos para obtener los multiplicadores
        ArrayList<HashMap<String, Object>> llista = db.query("SELECT * FROM TypeEffectiveness WHERE attack_type = '" + attackType + "' AND target_type = '" + enemyType + "'");
        if (llista.size() > 0) {
            double multiplier = Double.parseDouble(llista.get(0).get("multiplier").toString());
            return multiplier;
        }
        // Por defecto, daño normal
        return 1.0;
    }

    /**
     * Muestra una alerta en pantalla con el mensaje y tipo especificados.
     * @param message Mensaje a mostrar.
     * @param type Tipo de alerta (INFORMATION, ERROR, WARNING, etc.).
     */
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Error" : "Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Actualiza el texto de un Label con el formato "actual / máximo".
     * @param label Label a actualizar.
     * @param current Valor actual.
     * @param max Valor máximo.
     * @param labelName Nombre del label (para mensajes de error).
     */
    private void updateStatLabel(Label label, int current, int max, String labelName) {
        if (label != null) {
            label.setText(current + " / " + max);
        } else {
            System.err.println(labelName + " is not initialized.");
        }
    }

    /**
     * Asigna una imagen a un ImageView a partir de una ruta de recurso.
     * Si no se encuentra la imagen, muestra un mensaje de error.
     * @param imageView ImageView donde se asignará la imagen.
     * @param resourcePath Ruta del recurso de la imagen.
     * @param errorMsg Mensaje de error si no se encuentra la imagen.
     */
    private void setImageFromResource(ImageView imageView, String resourcePath, String errorMsg) {
        URL url = getClass().getResource(resourcePath);
        if (url != null) {
            imageView.setImage(new Image(url.toExternalForm()));
        } else {
            System.err.println(errorMsg + ": " + resourcePath);
        }
    }

    /**
     * Maneja el evento del botón "Back" para volver a la vista anterior.
     */
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
