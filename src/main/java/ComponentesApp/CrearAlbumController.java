/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ComponentesApp;

import Modelo.Album;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ronal
 */
public class CrearAlbumController implements Initializable {

    @FXML
    private Button buttonCreateAlbum;
    @FXML
    private TextField txtNameNewAlbum;
    @FXML
    private TextField txtDescNewAlbum;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonCreateAlbum.setOnAction(e -> {
            try {
                int numId = (Integer.parseInt(App.sys.getListaAlbumes().getLast().getId().replace("\"", "").substring(1))) + 1;
                String id = "a";
                String name = txtNameNewAlbum.getText();
                String description = txtDescNewAlbum.getText();
                
                Album album = new Album("\""+id.concat(String.valueOf(numId))+"\"", "\""+name+"\"", "\""+description+"\"", null);
                App.sys.getListaAlbumes().addLast(album);

                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menuAlbum.fxml"));
                Parent root = fxmlLoader.load();
                App.scene.setRoot(root);
            } catch (IOException ex) {
            }
        });
    }    

    @FXML
    private void buttonCancelar(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menuAlbum.fxml"));
            Parent root = fxmlLoader.load();
            App.scene.setRoot(root);
        } catch (IOException ex) {
        }
    }

    
}
