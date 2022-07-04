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
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author ronal
 */
public class MenuController implements Initializable {
    
    @FXML
    private VBox buttonsAlbum;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        agregarBotones();
    }  
    
    private void agregarBotones() {
        for(Album a: App.sys.getListaAlbumes()) {
            Button tempButton = new Button(a.getName());
            tempButton.setOnAction((ActionEvent e) -> {
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menuAlbum.fxml"));
                    Parent root = fxmlLoader.load();
                    MenuAlbumController mac = fxmlLoader.<MenuAlbumController>getController();
                    if(a.getFotos() == null){
                        mac.initDataNuevo(a);
                    }
                    else{
                        mac.initDataCreado(a, 0);
                    }
                    App.scene.setRoot(root);
                } catch (IOException ex) {
                    }
            });          
            buttonsAlbum.getChildren().add(tempButton);
            }
        }
    

    @FXML
    private void crearAlbum(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("crearAlbum.fxml"));
        Parent root = fxmlLoader.load();
        App.scene.setRoot(root);
    }
    
}
