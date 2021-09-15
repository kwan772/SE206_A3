import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage stage;
    private static Menu menu = new Menu();
    
    @Override
    public void start(Stage stage) {
        try {
            // Create game files if they don't already exist
            FileController.createGameFiles();

            // Create stage
            App.stage = stage;
            stage.setTitle("Spelling Wiz");

            // Set initial scene to the menu
            Scene scene = new Scene(menu.getRoot(), 1000, 800);
            scene.getStylesheets().add("application.css");
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        launch();
    }

    public static Stage getStage() {
        return stage;
    }

    public static Menu getMenu() {
        return menu;
    }

    public static void returnToMainMenu() {
        getStage().getScene().setRoot(App.getMenu().getRoot());
    }

    public static void quit() {
        Platform.exit();
    }
}
