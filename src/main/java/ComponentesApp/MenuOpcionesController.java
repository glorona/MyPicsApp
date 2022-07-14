/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ComponentesApp;

import Modelo.Camara;
import Modelo.Foto;
import Modelo.Persona;
import Util.ArrayList;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ronal
 */
public class MenuOpcionesController implements Initializable {
    private Persona person;
    private Camara cam;

    @FXML
    private Button buttonRegresar;
    @FXML
    private ComboBox<Persona> personas;
    @FXML
    private ComboBox<Persona> personas1;
    @FXML
    private TextField txtNombrePersona;
    @FXML
    private TextField txtNuevoNombre;
    @FXML
    private Label nombreActual;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtModelo;
    @FXML
    private ComboBox<Camara> cbxCamara;
    @FXML
    private Label marcaCam;
    @FXML
    private Label modeloCam;
    @FXML
    private TextField txtNuevaMarca;
    @FXML
    private TextField txtNuevoModelo;
    @FXML
    private CheckBox chkMarca;
    @FXML
    private CheckBox chkModelo;
    @FXML
    private ComboBox<Camara> cbxCamara2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Persona> people = FXCollections.observableArrayList();
        for(Persona p: App.sys.getListaPersonas())
            people.add(p);
        
        ObservableList<Camara> camaras = FXCollections.observableArrayList();
        for(Camara c: App.sys.getListaCamaras())
            camaras.add(c);
        
        
        personas.getItems().setAll(people);
        
        personas.setOnAction(e -> { 
            nombreActual.setText(personas.getValue().getName().replace("\"", ""));
            this.person = personas.getValue();
        });
        
        personas1.getItems().setAll(people);
        
        personas1.setOnAction(e -> { 
            this.person = personas1.getValue();
        });
        
        cbxCamara.getItems().setAll(camaras);
        
        cbxCamara.setOnAction(e -> {
            marcaCam.setText(cbxCamara.getValue().getTipo().replace("\"", ""));
            modeloCam.setText(cbxCamara.getValue().getModelo().replace("\"", ""));
            this.cam = cbxCamara.getValue();
        });
        
        cbxCamara2.getItems().setAll(camaras);
        
        cbxCamara2.setOnAction(e -> {
            this.cam = cbxCamara2.getValue();
        });
        
        
        
    }    

    @FXML
    private void buttonRegresar(ActionEvent event) {
        regresar();
    }

    @FXML
    private void buttonCrear(ActionEvent event) {
        String nombre = "\"" + txtNombrePersona.getText() + "\"";
        String id = "\"" + App.sys.getLastPerID(App.sys.getListaPersonas()) + "\"";
        Persona p = new Persona(id, nombre);
        
        App.sys.getListaPersonas().addLast(p);
        App.sys.escribePersona(p, App.rutaPersona);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText("Se ha creado la persona " + txtNombrePersona.getText());
        alert.showAndWait();
        
        regresar();
    }

    @FXML
    private void buttonEliminar(ActionEvent event) {
        boolean confirmaFotoex = false;
        for (Foto f:App.sys.getListaFotosSistema()){
            if(f.getPeople().contains(person.getId())){
                confirmaFotoex = true;
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Error en eliminacion.");
                alert.setTitle("Info");
                alert.setContentText("No se puede eliminar a la persona " + person.getName() + "ya que aparece en una o mas fotos.");
                alert.showAndWait();
            }
        }
        
        if(!confirmaFotoex){
        App.sys.getListaPersonas().remove(App.sys.getListaPersonas().indexOf(person));
        App.sys.eliminaPersona(person, App.rutaPersona, App.rutaPersonasfolder);
        App.sys.reload();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText("Se ha eliminado a la persona " + person.getName());
        alert.showAndWait();
        
        regresar();
        }
    }

    @FXML
    private void buttonModificar(ActionEvent event) {
        String nombre = "\"" + txtNuevoNombre.getText() + "\"";
        
        ArrayList<String> cambios = new ArrayList<>();
        cambios.addLast("name");
        
        ArrayList<String> valores = new ArrayList<>();
        valores.addLast(nombre);
        
        App.sys.modificaPersona(person, cambios, valores);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText("Se ha modificado a la persona " + person.getName());
        alert.showAndWait();
        
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

    @FXML
    private void buttonCrearCam(ActionEvent event) {
        String marca = "\"" + txtMarca.getText() + "\"";
        String modelo = "\"" + txtModelo.getText() + "\"";
        String id = "\"" + App.sys.getLastCamID(App.sys.getListaCamaras()) + "\"";
        
        Camara newcam = new Camara(id, modelo, marca);
        
        App.sys.getListaCamaras().addLast(newcam);
        App.sys.escribeCamara(newcam, App.rutaCamara);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText("Se ha creado la cámara " + newcam.getTipo().replace("\"", "") + " " + newcam.getModelo().replace("\"", ""));
        alert.showAndWait();
        
        regresar();
    }

    @FXML
    private void buttonModificarCam(ActionEvent event) {
        boolean isMarca = chkMarca.isSelected();
        boolean isModelo = chkModelo.isSelected();
        
        String marca = "\"" + txtNuevaMarca.getText() + "\"";
        String modelo = "\"" + txtNuevoModelo.getText() + "\"";
        
        if(isMarca && isModelo){
            ArrayList<String> cambios = new ArrayList<>();
            cambios.addLast("modelo");
            cambios.addLast("type");

            ArrayList<String> valores = new ArrayList<>();
            valores.addLast(modelo);
            valores.addLast(marca);

            App.sys.modificaCamara(this.cam, cambios, valores);
        }
        
        if(isMarca && !isModelo){
            ArrayList<String> cambios = new ArrayList<>();
            cambios.addLast("type");
            
            ArrayList<String> valores = new ArrayList<>();
            valores.addLast(marca);
            
            App.sys.modificaCamara(this.cam, cambios, valores);
        }
        
        if(!isMarca && isModelo){
            ArrayList<String> cambios = new ArrayList<>();
            cambios.addLast("modelo");
            
            ArrayList<String> valores = new ArrayList<>();
            valores.addLast(modelo);
            
            App.sys.modificaCamara(this.cam, cambios, valores);
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText("Se ha modificado a la camara " + cam.getTipo().replace("\"", "") + " " + cam.getModelo().replace("\"", ""));
        alert.showAndWait();
        
        regresar();
    }

    @FXML
    private void buttonEliminarCam(ActionEvent event) {
         boolean confirmaFotoex = false;
         
        for (Foto f:App.sys.getListaFotosSistema()){
            
            String fid = f.getCamid().replace("\"","");
            if(fid.equals(cam.getId().replace("\"", ""))){
                confirmaFotoex = true;
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Error en eliminacion.");
                alert.setTitle("Info");
                alert.setContentText("No se puede eliminar camara" + cam.toString() + "ya que esta siendo usada en una o mas fotos.");
                alert.showAndWait();
            }
        }
        if(!confirmaFotoex){
        App.sys.getListaCamaras().remove(App.sys.getListaCamaras().indexOf(cam));
        App.sys.eliminaCamara(cam, App.rutaCamara, App.rutaCamarafolder);
        App.sys.reload();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText("Se ha eliminado a la camara " + cam.getTipo().replace("\"", "") + " " + cam.getModelo().replace("\"", ""));
        alert.showAndWait();
        
        regresar();
        }
    }
    
    
}
