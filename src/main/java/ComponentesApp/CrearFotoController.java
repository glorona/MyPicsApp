/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ComponentesApp;

import Modelo.Album;
import Modelo.Camara;
import Modelo.Foto;
import Modelo.Persona;
import Util.ArrayList;
import Util.CircularDoubleLinkedList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.controlsfx.control.CheckComboBox;

/**
 * FXML Controller class
 *
 * @author ronal
 */
public class CrearFotoController implements Initializable {
    
    private ArrayList<Path> paths = new ArrayList<>(); 
    private ArrayList<String> ps = new ArrayList<>(); 
    private Camara cam;

    @FXML
    private TextField txtNameNewFoto;
    @FXML
    private TextField txtPlaceNewFoto;
    @FXML
    private TextField txtDescNewFoto;
    @FXML
    private TextField txtDateNewFoto;
    @FXML
    private CheckComboBox<Persona> checkBox;
    @FXML
    private HBox hboxFoto;
    @FXML
    private Button buttonCancelar;
    @FXML
    private ComboBox<Camara> comboCam;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        checkComboBoxMenu();
        ObservableList<Camara> camaras = FXCollections.observableArrayList();
        for(Camara c: App.sys.getListaCamaras())
            camaras.add(c);
        comboCam.getItems().setAll(camaras);
        
        comboCam.setOnAction(e -> {
            this.cam = comboCam.getValue();
            if(comboCam.getSelectionModel().isEmpty()){
                this.cam = null;
            }
        });
    }    
    
    public void initDataMenu(){
        buttonCancelar.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menuAlbum.fxml"));
                Parent root = fxmlLoader.load();                
                App.scene.setRoot(root);
                } catch (IOException ex) {
            }
        });
    }
    
    
    public void initDataFotoC(Album a, Foto photo){
        
        int index = a.getFotos().indexOf(photo);
        
        buttonCancelar.setOnAction(e -> {
            regresarMenuDataC(a, index);
        });
    }
    
    public void initDataFotoN(Album a){
        buttonCancelar.setOnAction(e -> {
            regresarMenuDataN(a);
        });
    }
    
    private String fotoId(){
        int a = -1;
        for(Foto f: App.sys.getListaFotosSistema()){
            if(App.sys.getListaFotosSistema().size() != 0){
            int b = Integer.parseInt(f.getPhotoid().replace("\"", "").substring(1));
            if(b > a){
                a = b;
            }
            }
            else{
                a=-1;
            }
        }
        int numId = a+1;
        String id = "f";
        return "\""+id.concat(String.valueOf(numId))+"\"";
    }
    
    
    
    private void regresarMenuDataN(Album a){
        try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("albumViewer.fxml"));
                Parent root = fxmlLoader.load();
                
                AlbumViewerController mac = fxmlLoader.<AlbumViewerController>getController();
                mac.initDataNuevo(a);
                
                App.scene.setRoot(root);
            } catch (IOException ex) {
            }
    }
    
    private void regresarMenuDataC(Album a, int index){
        try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("albumViewer.fxml"));
                Parent root = fxmlLoader.load();
                
                AlbumViewerController mac = fxmlLoader.<AlbumViewerController>getController();
                mac.initDataCreado(a, index);
                
                App.scene.setRoot(root);
            } catch (IOException ex) {
        }
    }
    
    private void checkComboBoxMenu(){
        final ObservableList<Persona> people = FXCollections.observableArrayList();
        for(Persona p: App.sys.getListaPersonas())
            people.add(p);

        checkBox.getItems().setAll(people);
        
        checkBox.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change<? extends Persona> c) -> {
            
            ArrayList<String> ppl = new ArrayList<>();
            
            while(!ps.isEmpty()){
                ps.removeLast();
            }
                
            for(int i=0; i<App.sys.getListaPersonas().size();i++){
                if(checkBox.getCheckModel().isChecked(i)){
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

    @FXML
    private void buttonCargarFoto(ActionEvent event) {
        final ObservableList<String> fileExtensions = FXCollections.observableArrayList();
        fileExtensions.add("*.png");
        fileExtensions.add("*.jpg");
        fileExtensions.add("*.jpeg");
            
        final FileChooser fc = new FileChooser();
        
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo de Image", fileExtensions));
        File f = fc.showOpenDialog(null);

        if(f != null){
            try {
                hboxFoto.getChildren().clear();
                for(Path x: paths){
                    paths.remove(paths.indexOf(x));
                }
                
                String fileName = f.getName();
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, f.getName().length());
                
                Path pathDestino = Paths.get("Archivos/Fotos/archivosfotos/" + fotoId().replace("\"", "") + "." + fileExtension);
                Path pathOrigen = Paths.get(f.getAbsolutePath());                            

                this.paths.addLast(pathDestino);
                this.paths.addLast(pathOrigen);
                
                Image image = new Image(new FileInputStream(pathOrigen.toString()));
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(250); 
                imageView.setFitWidth(250);
                imageView.setPreserveRatio(true);

                hboxFoto.getChildren().add(imageView);
                hboxFoto.setAlignment(Pos.CENTER);


            } catch (FileNotFoundException ex) {
            }
        }
    }

    @FXML
    private void buttonAceptar(MouseEvent event) throws IOException {
        try {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            if(txtNameNewFoto.getText().equals("") || txtDescNewFoto.getText().equals("") || txtPlaceNewFoto.getText().equals("") || txtDateNewFoto.getText().equals("") || checkBox.getCheckModel().getCheckedItems().isEmpty() ){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                if(txtNameNewFoto.getText().equals("")){
                    txtNameNewFoto.setText("");
                    alert.setHeaderText("Error de Nombre");
                    alert.setContentText("Nombre no puede estar vacio.");
                }
                if(txtDescNewFoto.getText().equals("")){
                    alert.setHeaderText("Error de Descripcion");
                    alert.setContentText("Descripcion no puede estar vacio.");
                    
                }
                if(txtPlaceNewFoto.getText().equals("")){
                    alert.setHeaderText("Error de Lugar");
                    alert.setContentText("Lugar no puede estar vacio.");
                    
                }
                if(txtDateNewFoto.getText().equals("")){
                    alert.setHeaderText("Error de Fecha");
                    alert.setContentText("Fecha no puede estar vacio.");
                }
                if(checkBox.getCheckModel().getCheckedItems().size() == 0){
                    alert.setHeaderText("No hay personas en esta foto");
                    alert.setContentText("Por favor, agregue personas que estan en la foto.");
                }
                alert.show();
                   
            }
            else{
            String id = fotoId();
            String name = "\"" + txtNameNewFoto.getText() + "\"";
            
            String desc = "\"" + txtDescNewFoto.getText() + "\"";
            String place = "\"" + txtPlaceNewFoto.getText() + "\"";
            String fecha = txtDateNewFoto.getText();
            
            Path pathOrigen = this.paths.get(1);
            Path pathDestino = this.paths.get(0);
            String route = "\"" + pathDestino.toString() + "\"";

            ArrayList<String> al = new ArrayList<>();
            al.addLast("\"a0\"");

            Calendar cal = Calendar.getInstance();
            cal.setTime(df.parse(fecha));
            if(cam != null){
                String cid = cam.getId();
                System.out.println(cid);
                cid = cid.replace("\"","");
                Foto f = new Foto(id,name,place,route,al,ps,desc,cal,cid);
                App.sys.getListaFotosSistema().addLast(f);
                
                
                if(App.sys.getListaAlbumes().get(0).getFotos() == null){
                CircularDoubleLinkedList<Foto> fotos = new CircularDoubleLinkedList<>();
                fotos.addLast(f);
                App.sys.getListaAlbumes().get(0).setFotos(fotos);
                }
                else{
                App.sys.getListaAlbumes().get(0).getFotos().addLast(f);
                }

                Files.copy(pathOrigen, pathDestino);
                App.sys.escribeFoto(f, App.rutaFoto);
            }
            
            
            
            
            else{
            Foto f = new Foto(id,name,place,route,al,ps,desc,cal);
            App.sys.getListaFotosSistema().addLast(f);
            if(App.sys.getListaAlbumes().get(0).getFotos() == null){
                CircularDoubleLinkedList<Foto> fotos = new CircularDoubleLinkedList<>();
                fotos.addLast(f);
                App.sys.getListaAlbumes().get(0).setFotos(fotos);
            }
            else{
                App.sys.getListaAlbumes().get(0).getFotos().addLast(f);
            }
            
            Files.copy(pathOrigen, pathDestino);
            App.sys.escribeFoto(f, App.rutaFoto);
            }
            
            
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menuAlbum.fxml"));
            Parent root = fxmlLoader.load();                
            App.scene.setRoot(root);
            }
            
        } catch (IOException | ParseException ex) {
            Logger.getLogger(CrearFotoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}
