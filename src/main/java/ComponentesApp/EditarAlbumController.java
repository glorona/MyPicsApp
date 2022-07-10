/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ComponentesApp;

import Modelo.Album;
import Util.ArrayList;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ronal
 */
public class EditarAlbumController implements Initializable {
    private Album album;
    private ArrayList<String> cambios = new ArrayList<>();
    private ArrayList<String> valores = new ArrayList<>();

    @FXML
    private Label albumName;
    @FXML
    private Label albumDesc;
    @FXML
    private TextField txtNewName;
    @FXML
    private TextField txtNewDesc;
    @FXML
    private CheckBox chkName;
    @FXML
    private CheckBox chkDesc;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void initData(Album a){
        this.album = a;
        
        albumName.setText(a.getName().replace("\"", ""));
        albumDesc.setText(a.getDescription().replace("\"", ""));
    }
    
    @FXML
    private void buttonRegresar(ActionEvent event) {
        regresar();
    }

    @FXML
    private void buttonAceptar(ActionEvent event) {
        boolean isName = chkName.isSelected();
        boolean isDesc = chkDesc.isSelected();
        
        String newName = "\"" + txtNewName.getText() + "\"";
        String newDesc = "\"" + txtNewDesc.getText() + "\"";
        
        if(isName){
            cambios.addLast("name");
            valores.addLast(newName);
        }
        
        if(isDesc){
            cambios.addLast("desc");
            valores.addLast(newDesc);
        }
        
        App.sys.modificaAlbum(album, cambios, valores);
        regresar();
    }
    
    private void regresar(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menuAlbum.fxml"));
            Parent root = fxmlLoader.load();
            App.scene.setRoot(root);
        } catch (IOException ex) {
        }
    }
}
