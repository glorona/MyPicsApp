/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ComponentesApp;

import Modelo.Album;
import Modelo.Camara;
import Modelo.Foto;
import Modelo.Persona;
import Util.CircularDoubleLinkedList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private Foto f;

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
    @FXML
    private Button buttonEliminarFoto;
    @FXML
    private Label camLabel;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void initData(Album a, Foto photo) throws FileNotFoundException, IOException{
        this.f = photo;
        int index = a.getFotos().indexOf(photo);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        photoPane.getChildren().clear();
        
        nameLabel.setText(photo.getName().replace("\"", ""));
        descLabel.setText(photo.getDescription().replace("\"", ""));
        persLabel.setText(construirTextoPersonas(photo));
        fechaLabel.setText(dateFormat.format(photo.getFecha().getTime()));
        lugarLabel.setText(photo.getPlace().replace("\"",""));
        if(photo.getCamid() != null){
            String campros = photo.getCamid().replace("\"", "");
            for(Camara c: App.sys.getListaCamaras()){
                String camlis = c.getId().replace("\"","");
                
                if(camlis.equals(campros)){
                    camLabel.setText(c.toString());
                }
            }
        }
        else{
            camLabel.setText("No establecido");
        }
        String rutaFoto = photo.getRuta().replace("\"", "");
        
        
        ImageView imageView;
        try (FileInputStream cerrar = new FileInputStream(rutaFoto)) {
            Image image = new Image(cerrar);
            imageView = new ImageView(image);
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            imageView.setPreserveRatio(true);
        }
       
        photoPane.getChildren().add(imageView); 
        
        buttonRetroceder.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("albumViewer.fxml"));
                Parent root = fxmlLoader.load();
                
                AlbumViewerController mac = fxmlLoader.<AlbumViewerController>getController();
                mac.initDataCreado(a, index);
                
                App.scene.setRoot(root);
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
        });
    }
    
    public void initDataFoto(Foto photo) throws FileNotFoundException, IOException{
        this.f = photo;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        photoPane.getChildren().clear();
        
        nameLabel.setText(photo.getName().replace("\"", ""));
        descLabel.setText(photo.getDescription().replace("\"", ""));
        persLabel.setText(construirTextoPersonas(photo));
        fechaLabel.setText(dateFormat.format(photo.getFecha().getTime()));
        lugarLabel.setText(photo.getPlace().replace("\"",""));
        if(photo.getCamid() != null){
            String campros = photo.getCamid().replace("\"", "");
            for(Camara c: App.sys.getListaCamaras()){
                String camlis = c.getId().replace("\"","");
                
                if(camlis.equals(campros)){
                    camLabel.setText(c.toString());
                }
            }
        }
        else{
            camLabel.setText("No establecido");
        }
        String rutaFoto = photo.getRuta().replace("\"", "");
        
        
        ImageView imageView;
        try (FileInputStream cerrar = new FileInputStream(rutaFoto)) {
            Image image = new Image(cerrar);
            imageView = new ImageView(image);
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            imageView.setPreserveRatio(true);
        }
       
        photoPane.getChildren().add(imageView);        
        
        buttonRetroceder.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menuAlbum.fxml"));
                Parent root = fxmlLoader.load();
                App.scene.setRoot(root);
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
        });
        
        buttonEliminarFoto.setOnAction(e -> {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("Confirmación");
                alert.setContentText("¿Estas seguro de confirmar la acción?");
                Optional<ButtonType> action = alert.showAndWait();
                
                if (action.get() == ButtonType.OK) {
                    for(String s: photo.getAlbum()){
                        s = s.replace("\"", "");
                        for(Album a: App.sys.getListaAlbumes()){
                            String aid = a.getId().replace("\"","");
                            if(s.equals(aid)){
                                Album a_f = App.sys.getListaAlbumes().get(App.sys.getListaAlbumes().indexOf(a));
                                CircularDoubleLinkedList<Foto> fotosalbum = a_f.getFotos();
                                fotosalbum.remove(fotosalbum.indexOf(photo));
                            }
                        }
                    }
                    App.sys.getListaFotosSistema().remove(App.sys.getListaFotosSistema().indexOf(photo));
                    App.sys.eliminaFoto(photo, App.rutaFoto, App.rutaFotofolder);
                    File imagen = new File(photo.getRuta().replace("\"", ""));
                    imagen.delete();
                    
                    
                    
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("MenuAlbum.fxml"));
                    Parent root = fxmlLoader.load();
                    App.scene.setRoot(root);            
                }
            } catch (IOException ex) {
                Logger.getLogger(FotoViewerController.class.getName()).log(Level.SEVERE, null, ex);
            } 
        
        });
        
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

    @FXML
    private void buttonEditar(ActionEvent event) {
        try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("EditarFoto.fxml"));
                Parent root = fxmlLoader.load();
                
                EditarFotoController efc = fxmlLoader.<EditarFotoController>getController();
                efc.initData(f);
                            
                App.scene.setRoot(root);
            } catch (IOException ex) {
                Logger.getLogger(FotoViewerController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
            Logger.getLogger(FotoViewerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

}
