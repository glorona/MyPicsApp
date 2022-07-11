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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.textfield.CustomTextField;

/**
 * FXML Controller class
 *
 * @author ronal
 */
public class MenuAlbumController implements Initializable {
    ArrayList<String> opcionesBusqueda = new ArrayList<>();
    
    @FXML
    private VBox buttonsAlbum;
    @FXML
    private AnchorPane anchorPaneFotos;
    @FXML
    private CustomTextField txtBuscaFoto;
    @FXML
    private CheckComboBox<String> chkBoxBusqueda;
    @FXML
    private ScrollPane paneFotos;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        App.sys.getListaFotosSistema();
        
        ArrayList<String> cambio = new ArrayList<>();
        cambio.addLast("place");
        cambio.addLast("people");
        cambio.addLast("fecha");
        cambio.addLast("camara");
        
        final ObservableList<String> cambios = FXCollections.observableArrayList();
        
        for(String c: cambio)
            cambios.add(c);
        
        
        chkBoxBusqueda.getItems().setAll(cambios);
        
        chkBoxBusqueda.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change<? extends String> c) -> {
            for(int i=0; i<cambio.size(); i++){
                if(chkBoxBusqueda.getCheckModel().isChecked(i)){
                    opcionesBusqueda.addLast(cambio.get(i));
                }
            }
        
        });
        
        try {
            agregarBotones(App.sys.getListaAlbumes());
            
            double totalFotos = (double) App.sys.getListaFotosSistema().size();
            int numRows = (int) Math.ceil(totalFotos/3);
            if(numRows == 0){
                numRows = 1;
            }
            
            paneFotos.setContent(drawBus(numRows, 4, App.sys.getListaFotosSistema()));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(MenuAlbumController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    private void agregarBotones(ArrayList<Album> listaAlbumes) {
        for(Album a: listaAlbumes) {
            Button tempButton = new Button(a.getName().replace("\"", ""));
            tempButton.setOnAction((ActionEvent e) -> {
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("albumViewer.fxml"));
                    Parent root = fxmlLoader.load();
                    AlbumViewerController avc = fxmlLoader.<AlbumViewerController>getController();
                    if(a.getFotos() == null){
                        avc.initDataNuevo(a);
                    }
                    else{
                        avc.initDataCreado(a, 0);
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
    
        
    public static GridPane drawBus(int rows, int col, ArrayList<Foto> fotoDraw) throws FileNotFoundException, IOException{
        GridPane table = new GridPane();
        table.setHgap(25);
        table.setVgap(25);
        table.setAlignment(Pos.CENTER);

        ArrayList<Foto> fotos = fotoDraw;
        int x = 0;
        
        for(int i=0; i<rows; i++){
            for(int j=0;j<col; j++) {
                if(x<fotos.size()){
                    Foto photo = fotos.get(x);
                    String rutaFoto = photo.getRuta().replace("\"", "");
                    ImageView imageView;
                    try (FileInputStream cerrar = new FileInputStream(rutaFoto)) {
                        Image image = new Image(cerrar);
                        imageView = new ImageView(image);
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(150);
                        imageView.setFitWidth(150);
                    }
                    
                    imageView.setOnMouseClicked(e -> {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fotoViewer.fxml"));
                            Parent root = fxmlLoader.load();

                            FotoViewerController fvc = fxmlLoader.<FotoViewerController>getController();
                            fvc.initDataFoto(photo);

                            App.scene.setRoot(root);
                        } catch (IOException ex) {
                        }
                    });

                    //add them to the GridPane
                    table.add(imageView, j, i); //  (child, columnIndex, rowIndex)
                    x++;
                }
                else{
                    Button buttonAnadirFoto = new Button("+");
                    buttonAnadirFoto.setMinSize(150, 150);

                    buttonAnadirFoto.setOnMouseClicked(e -> {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("crearFoto.fxml"));
                            Parent root = fxmlLoader.load();

                            CrearFotoController cfc = fxmlLoader.<CrearFotoController>getController();
                            cfc.initDataMenu();

                            App.scene.setRoot(root);
                        } catch (IOException ex) {
                        }
                    });
                    //add them to the GridPane
                    table.add(buttonAnadirFoto, j, i); //  (child, columnIndex, rowIndex)
                    return table;
                }
            }
        }
        return table;
    }

    @FXML
    private void menuOpciones(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menuOpciones.fxml"));
            Parent root = fxmlLoader.load();
            App.scene.setRoot(root);
        } catch (IOException ex) {
        }
    }

    @FXML
    private void buttonBuscar(ActionEvent event) throws IOException {
        String busqueda = txtBuscaFoto.getText();
        
        String[] datos = busqueda.split(",");
        
        ArrayList<String> datosBusqueda = new ArrayList<>();
        
        for(String d: datos)
            datosBusqueda.addLast(d);
        
        if(datosBusqueda.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error de Busqueda");
            alert.setContentText("Los valores de los parametros a buscar, tienen que estar separados por comas. Ejemplo: nombre,fecha");
            alert.show();
        }
        
        if(datosBusqueda.size() == 1){
            
            ArrayList<Foto> resultadosBFotos = App.sys.buscaSimpleFoto(opcionesBusqueda.get(0), datosBusqueda.get(0), App.sys.getListaFotosSistema());
            
            double totalFotos = (double) resultadosBFotos.size();
            int numRows = (int) Math.ceil(totalFotos/3);
            if(numRows == 0){
                numRows = 1;
            }
            
            paneFotos.setContent(drawBus(numRows, 4, resultadosBFotos));
        }
    }
    
}
