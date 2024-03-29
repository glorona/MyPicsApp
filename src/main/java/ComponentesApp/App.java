package ComponentesApp;

import Modelo.Foto;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    static Scene scene;
    
    static Stage mStage;
    
    public static String rutaCamara = "Archivos/Camaras/camaras.txt";
    
    public static String rutaCamarafolder = "Archivos/Camaras/";
    
    public static String rutaAlbum = "Archivos/Album/albumes.txt";
    
    public static String rutaAlbumfolder = "Archivos/Album/";
    
    public static String rutaPersona = "Archivos/Personas/personas.txt";
    
    public static String rutaPersonasfolder = "Archivos/Personas/";
    
    public static String rutaFoto = "Archivos/Fotos/master/fotos.txt";
    
    public static String rutaFotofolder = "Archivos/Fotos/master/";
    
    public static String repoFotos = "Archivos/Fotos/archivosfotos/";
    
    public static Sistema sys = new Sistema();
    
    @Override
    public void start(Stage stage) throws IOException {
        mStage = stage;
        scene = new Scene(loadFXML("menuAlbum"));
        mStage.setScene(scene);
        mStage.sizeToScene();
        mStage.setResizable(false);
        mStage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
        
    }

}