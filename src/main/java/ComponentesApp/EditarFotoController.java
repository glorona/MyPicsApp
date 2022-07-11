/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ComponentesApp;

import Modelo.Camara;
import Modelo.Foto;
import Modelo.Persona;
import Util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.controlsfx.control.CheckComboBox;

/**
 * FXML Controller class
 *
 * @author ronal
 */
public class EditarFotoController implements Initializable {
    
    private ArrayList<String> ps = new ArrayList<>(); 
    private ArrayList<String> cambios = new ArrayList<>();
    private ArrayList<String> valores = new ArrayList<>();
    private Foto photo;
    
    private Camara cam;
    
    @FXML
    private CheckBox chkName;
    @FXML
    private CheckBox chkDesc;
    @FXML
    private CheckBox chkDate;
    @FXML
    private CheckBox chkPlace;
    @FXML
    private CheckBox chkPeople;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtDesc;
    @FXML
    private TextField txtDate;
    @FXML
    private TextField txtPlace;
    @FXML
    private CheckComboBox<Persona> checkPeople;
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
    @FXML
    private CheckBox chkCam;
    @FXML
    private ComboBox<Camara> checkCam;
    @FXML
    private Label fotoCam;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        checkComboBoxMenu();
        ObservableList<Camara> camaras = FXCollections.observableArrayList();
        for(Camara c: App.sys.getListaCamaras())
            camaras.add(c);
        checkCam.getItems().setAll(camaras);
        
        checkCam.setOnAction(e -> {
            this.cam = checkCam.getValue();
            if(checkCam.getSelectionModel().isEmpty()){
                this.cam = null;
            }
        });
        
    }    
    
    public void initData(Foto f) throws ParseException{
        this.photo = f;
        
        fotoName.setText(f.getName().replace("\"",""));
        fotoDesc.setText(f.getDescription().replace("\"",""));
        fotoPlace.setText(f.getPlace().replace("\"",""));
        fotoPeople.setText(construirTextoPersonas(f));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        fotoDate.setText(dateFormat.format(f.getFecha().getTime()));
        if(f.getCamid() != null){
        fotoCam.setText(f.getCamid().replace("\"",""));
        }
        else{
            fotoCam.setText("No establecido");
        }
    }
    
    private void checkComboBoxMenu(){
        final ObservableList<Persona> people = FXCollections.observableArrayList();
        for(Persona p: App.sys.getListaPersonas())
            people.add(p);

        checkPeople.getItems().setAll(people);
        
        checkPeople.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change<? extends Persona> c) -> {
            
            ArrayList<String> ppl = new ArrayList<>();
            
            while(!ps.isEmpty()){
                ps.removeLast();
            }
                
            for(int i=0; i<App.sys.getListaPersonas().size();i++){
                if(checkPeople.getCheckModel().isChecked(i)){
                    ppl.addLast(App.sys.getListaPersonas().get(i).getId());
                }
            }
                       
            for(int i = 0; i < ppl.size(); i++){
                if(!ps.contains(ppl.get(i))){
                    ps.addLast(ppl.get(i));
                }
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
    private void buttonRegresar(ActionEvent event) {
        regresar();
    }

    @FXML
    private void buttonAceptar(ActionEvent event) {
        boolean isName = chkName.isSelected();
        boolean isDesc = chkDesc.isSelected();
        boolean isPlace = chkPlace.isSelected();
        boolean isDate = chkDate.isSelected();
        boolean isPeople = chkPeople.isSelected();
        boolean isCam = chkCam.isSelected();
        
        String newName = "\"" + txtName.getText() + "\"";
        String newDesc = "\"" + txtDesc.getText() + "\"";
        String newPlace = "\"" + txtPlace.getText() + "\"";
        String newDate = txtDate.getText();
        
        
        if(isName){
            cambios.addLast("name");
            valores.addLast(newName);
        }
        
        if(isDesc){
            cambios.addLast("desc");
            valores.addLast(newDesc);
        }
        
        if(isPlace){
            cambios.addLast("place");
            valores.addLast(newPlace);
        }
        
        if(isDate){
            cambios.addLast("date");
            valores.addLast(newDate);
        }
        
        if(isPeople){
            cambios.addLast("person");
            StringBuilder sb = new StringBuilder();
            for(String a: ps){
                sb.append(a);
                sb.append("#");
            }
            String newPeople = sb.toString().substring(0, sb.toString().length()-1);
            valores.addLast(newPeople);
        }
        if(isCam){
            cambios.addLast("cam");
            valores.addLast(cam.toString());
            
        }
        
        App.sys.modificaFoto(photo, cambios, valores);
        App.sys.eliminaLineaFoto(photo, App.rutaFoto, App.rutaFotofolder);
        App.sys.escribeFoto(photo, App.rutaFoto);
        
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
