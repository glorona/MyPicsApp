/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ComponentesApp;

import Modelo.Album;
import Modelo.Foto;
import Util.ArrayList;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ronal
 */
public class CrearFotoController implements Initializable {


    @FXML
    private ComboBox<?> comboBoxPhotosExist;
    @FXML
    private Button buttonCargarFoto;
    @FXML
    private TextField txtNameNewFoto;
    @FXML
    private TextField txtPlaceNewFoto;
    @FXML
    private TextField txtDescNewFoto;
    @FXML
    private TextField txtDateNewFoto;
    @FXML
    private ComboBox<?> comboBoxPeople1;
    @FXML
    private ComboBox<?> comboBoxPeople2;
    @FXML
    private ComboBox<?> comboBoxPeople3;
    @FXML
    private Button buttonAgregarFoto;
    @FXML
    private Button buttonAceptar;
    @FXML
    private Button buttonCancelarE;
    @FXML
    private Button buttonCancelarL;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void initDataFotoC(Album a, Foto photo){
        
        /*ArrayList<Foto> fotosCombo = new ArrayList<>();
        
        for(Foto p: a.getFotos()){
            if(p.equals(p))
        }*/
        
        int index = a.getFotos().indexOf(photo);
        
        buttonCancelarE.setOnAction(e -> {
            regresarMenuDataC(a, index);            
        });
        
        buttonCancelarL.setOnAction(e -> {
            regresarMenuDataC(a, index);
        });
    }
    
    public void initDataFotoN(Album a){
        buttonCancelarE.setOnAction(e -> {
            regresarMenuDataN(a);
        });
        
        buttonCancelarL.setOnAction(e -> {
            regresarMenuDataN(a);
        });
    }
    
    @FXML
    private void buttonAceptar(ActionEvent event) {
    }

    @FXML
    private void buttonCargarFoto(ActionEvent event) {
    }

    @FXML
    private void buttonAgregarFoto(ActionEvent event) {
    }

    private void regresarMenuDataN(Album a){
        try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menuAlbum.fxml"));
                Parent root = fxmlLoader.load();
                
                MenuAlbumController mac = fxmlLoader.<MenuAlbumController>getController();
                mac.initDataNuevo(a);
                
                App.scene.setRoot(root);
            } catch (IOException ex) {
            }
    }
    
    private void regresarMenuDataC(Album a, int index){
        try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menuAlbum.fxml"));
                Parent root = fxmlLoader.load();
                
                MenuAlbumController mac = fxmlLoader.<MenuAlbumController>getController();
                mac.initDataCreado(a, index);
                
                App.scene.setRoot(root);
            } catch (IOException ex) {
            }
    }
    
}
