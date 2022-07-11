/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ComponentesApp;

import Modelo.Album;
import Modelo.Foto;
import Modelo.Persona;
import Util.ArrayList;
import Util.CircularDoubleLinkedList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author ronal
 */
public class AnadirFotoController implements Initializable {
    private ArrayList<String> cambios = new ArrayList<>();
    private ArrayList<String> valores = new ArrayList<>();
    private Foto photo;
    private Album albumMenu;

    @FXML
    private ComboBox<Foto> cbxFotos;
    @FXML
    private HBox hboxFoto;
    @FXML
    private Label fotoName;
    @FXML
    private Label fotoDesc;
    @FXML
    private Label fotoDate;
    @FXML
    private Label fotoPlace;
    @FXML
    private Label fotoPeople;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
    }    
    
    public void initData(Album a){
        this.albumMenu = a;
        ArrayList<Foto> fotos = new ArrayList<>();
        
        ArrayList<Foto> todasFotos = App.sys.getListaFotosSistema();
        CircularDoubleLinkedList<Foto> albumFotos = a.getFotos();
        
        if(albumFotos == null){
            for(Foto f: todasFotos)
                fotos.addLast(f);
        }
        else{
            for(Foto ads: todasFotos){
                if(!albumFotos.contains(ads))
                    fotos.addLast(ads);
            }
        }        
        
        ObservableList<Foto> fotosComboBox = FXCollections.observableArrayList();
        for(Foto f: fotos)
            fotosComboBox.add(f);
        
        cbxFotos.getItems().setAll(fotosComboBox);
        
        cbxFotos.setOnAction(e ->{
            hboxFoto.getChildren().clear();
            this.photo = cbxFotos.getValue();
            
            fotoName.setText(cbxFotos.getValue().getName().replace("\"", ""));
            fotoDesc.setText(cbxFotos.getValue().getDescription().replace("\"", ""));
            fotoPlace.setText(cbxFotos.getValue().getPlace().replace("\"", ""));
            fotoPeople.setText(construirTextoPersonas(cbxFotos.getValue()));
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            fotoDate.setText(dateFormat.format(cbxFotos.getValue().getFecha().getTime()));
            
            String rutaFoto = cbxFotos.getValue().getRuta().replace("\"", "");
        
        
            ImageView imageView;
            try (FileInputStream cerrar = new FileInputStream(rutaFoto)) {
                Image image = new Image(cerrar);
                imageView = new ImageView(image);
                imageView.setFitHeight(150);
                imageView.setFitWidth(150);
                imageView.setPreserveRatio(true);
                hboxFoto.getChildren().add(imageView); 
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AnadirFotoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AnadirFotoController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            ArrayList<String> fotoAlbumes = cbxFotos.getValue().getAlbum();
            fotoAlbumes.addLast(a.getId());
            
            cambios.addLast("album");
            
            StringBuilder sb = new StringBuilder();
            for(String h: fotoAlbumes){
                sb.append(h);
                sb.append("#");
            }
            
            String newAlbumes = sb.toString().substring(0, sb.toString().length()-1);
            valores.addLast(newAlbumes);
            }
        );
    }

    @FXML
    private void buttonRegresar(ActionEvent event) {
        regresar();
    }

    @FXML
    private void buttonAnadir(ActionEvent event) {
        
        App.sys.eliminaLineaFoto(photo, App.rutaFoto, App.rutaFotofolder);
        App.sys.modificaFoto(photo, cambios, valores);
        App.sys.escribeFoto(photo, App.rutaFoto);
        
        if(albumMenu.getFotos() == null){
            CircularDoubleLinkedList<Foto> fotos = new CircularDoubleLinkedList<>();
            fotos.addLast(photo);
            albumMenu.setFotos(fotos);
        }
        else{
            albumMenu.getFotos().addLast(photo);
        }
        
        App.sys.getListaFotosSistema();
        regresar();
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
    
    private void regresar(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menuAlbum.fxml"));
            Parent root = fxmlLoader.load();
            App.scene.setRoot(root);
        } catch (IOException ex) {
            Logger.getLogger(AnadirFotoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
