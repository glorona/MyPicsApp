/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ComponentesApp;

import Modelo.Album;
import Modelo.Foto;
import Modelo.Persona;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ronal
 */
public class FotoViewerController implements Initializable {

    @FXML
    private AnchorPane photoPane;
    @FXML
    private Label nameLabel;
    @FXML
    private Label descLabel;
    @FXML
    private Label persLabel;
    @FXML
    private Button buttonRetroceder;
    @FXML
    private Label fechaLabel;
    @FXML
    private Label lugarLabel;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void initData(Album a, Foto photo) throws FileNotFoundException{
        int index = a.getFotos().indexOf(photo);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        photoPane.getChildren().clear();
        
        nameLabel.setText(photo.getName().replace("\"", ""));
        descLabel.setText(photo.getDescription().replace("\"", ""));
        persLabel.setText(construirTextoPersonas(photo));
        fechaLabel.setText(dateFormat.format(photo.getFecha().getTime()));
        lugarLabel.setText(photo.getPlace().replace("\"",""));
        
        String rutaFoto = photo.getRuta().replace("\"", "");
        Image image = new Image(new FileInputStream(rutaFoto));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(150); 
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
       
        photoPane.getChildren().add(imageView);
        
        buttonRetroceder.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menuAlbum.fxml"));
                Parent root = fxmlLoader.load();
                
                MenuAlbumController mac = fxmlLoader.<MenuAlbumController>getController();
                mac.initDataCreado(a, index);
                
                App.scene.setRoot(root);
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
        });
    }
    
    @FXML
    private void eliminarFotoAlbum(ActionEvent event) {
    }
    
    private String construirTextoPersonas(Foto photo){
        StringBuilder sb = new StringBuilder();
        for(String p: photo.getPeople()){
            for(Persona a: App.sys.getListaPersonas()){
                if(p.equals(a.getId())){
                    sb.append(a.getName().replace("\"", ""));
                    sb.append(", ");
                }
                
            }
        }
        return sb.toString().substring(0, sb.toString().length()-2);
    }
}
