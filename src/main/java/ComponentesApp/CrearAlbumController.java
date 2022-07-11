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
import javafx.scene.control.Alert;
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
                if(txtNameNewAlbum.getText().equals("") || txtDescNewAlbum.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                if(txtNameNewAlbum.getText().equals("")){
                    txtNameNewAlbum.setText("");
                    alert.setHeaderText("Error de Nombre");
                    alert.setContentText("Nombre no puede estar vacio.");
                }
                if(txtDescNewAlbum.getText().equals("")){
                    alert.setHeaderText("Error de Descripcion");
                    alert.setContentText("Descripcion no puede estar vacio.");
                    
                }
                
                alert.show();
                   
                }
                else{
                int numId = (Integer.parseInt(App.sys.getListaAlbumes().getLast().getId().replace("\"", "").substring(1))) + 1;
                String id = "a";
                String name = txtNameNewAlbum.getText();
                String description = txtDescNewAlbum.getText();
                
                Album album = new Album("\""+id.concat(String.valueOf(numId))+"\"", "\""+name+"\"", "\""+description+"\"", null);
                App.sys.getListaAlbumes().addLast(album);
                App.sys.escribeAlbum(album, App.rutaAlbum);

                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menuAlbum.fxml"));
                Parent root = fxmlLoader.load();
                App.scene.setRoot(root);
                }
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
