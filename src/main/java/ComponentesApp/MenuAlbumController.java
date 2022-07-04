/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ComponentesApp;

import Modelo.Album;
import Modelo.Foto;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author ronal
 */
public class MenuAlbumController implements Initializable {
    
    @FXML
    private Label albumName;
    @FXML
    private Label albumDescription;
    @FXML
    private HBox hboxFotos;
    @FXML
    private Button buttonAnteriorFoto;
    @FXML
    private Button buttonSiguienteFoto;
    @FXML
    private Button buttonAnadirFoto;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
    **/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void initDataCreado(Album a, int index) throws FileNotFoundException{
        albumName.setText(a.getName().replace("\"",""));
        albumDescription.setText(a.getDescription().replace("\"", ""));
        ListIterator <Foto> listaFotos = a.getFotos().listIterator(index);
        colocarFoto(a, listaFotos.next());
        
        buttonAnteriorFoto.setOnAction(e -> {
            if(listaFotos.hasPrevious()){    
                try {
                    colocarFoto(a, listaFotos.previous());
                } catch (FileNotFoundException ex) {
                }
            }
        });
        
        buttonSiguienteFoto.setOnAction(e -> {
            if(listaFotos.hasNext()){    
                try {
                    colocarFoto(a, listaFotos.next());
                } catch (FileNotFoundException ex) {
                }
            }
        });
        
        buttonAnadirFoto.setOnAction(e ->{ 
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("crearFoto.fxml"));
                Parent root = fxmlLoader.load();

                CrearFotoController cfc = fxmlLoader.<CrearFotoController>getController();
                cfc.initDataFotoC(a, a.getFotos().get(index));

                App.scene.setRoot(root);
            } catch (IOException ex) {
            }
        
        });
    }
    
    public void initDataNuevo(Album a) throws FileNotFoundException{
        albumName.setText(a.getName().replace("\"",""));
        albumDescription.setText(a.getDescription().replace("\"", ""));
        buttonAnadirFoto.setOnAction(e ->{ 
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("crearFoto.fxml"));
                Parent root = fxmlLoader.load();

                CrearFotoController cfc = fxmlLoader.<CrearFotoController>getController();
                cfc.initDataFotoN(a);

                App.scene.setRoot(root);
            } catch (IOException ex) {
            }
        
        });
    }
    
    @FXML
    private void regresarMenu(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
        Parent root = fxmlLoader.load();
        App.scene.setRoot(root);
    }

    private void colocarFoto(Album a, Foto f) throws FileNotFoundException{
        hboxFotos.getChildren().clear();
        String rutaFoto = f.getRuta().replace("\"", "");
        Image image = new Image(new FileInputStream(rutaFoto));
        ImageView imageView = new ImageView(image);
        imageView.setOnMouseClicked(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fotoViewer.fxml"));
                Parent root = fxmlLoader.load();

                FotoViewerController fvc = fxmlLoader.<FotoViewerController>getController();
                fvc.initData(a, f);

                App.scene.setRoot(root);
            } catch (IOException ex) {
            }
        });
        imageView.setFitHeight(150); 
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
       
        hboxFotos.getChildren().add(imageView);
    }

    
}
