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
    @FXML
    private CustomTextField txtbuscaAlbum;
    @FXML
    private CheckComboBox<String> chkBuscaAlbum;

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
        chkBuscaAlbum.getItems().setAll(cambios);
        
        chkBoxBusqueda.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change<? extends String> c) -> {
            for(int i=0; i<cambio.size(); i++){
                if(chkBoxBusqueda.getCheckModel().isChecked(i) && !(opcionesBusqueda.contains(cambio.get(i)))){

                    
                    opcionesBusqueda.addLast(cambio.get(i));
                }
            }
        
        });
        
        
        
        
        chkBuscaAlbum.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change<? extends String> c) -> {
            for(int i=0; i<cambio.size(); i++){
                if(chkBuscaAlbum.getCheckModel().isChecked(i) && !(opcionesBusqueda.contains(cambio.get(i)))){
                    
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
                        imageView.setFitHeight(200);
                        imageView.setFitWidth(200);
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
        boolean flagbusca = true;
        String busqueda = txtBuscaFoto.getText();
        
        String[] datos = busqueda.split(",");
        
        ArrayList<String> datosBusqueda = new ArrayList<>();
        
        
        for(String d: datos){
            if(!(d.equals(""))){
            datosBusqueda.addLast(d);
            }
        }
        if(datosBusqueda.isEmpty()){
            flagbusca = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error de Busqueda");
            alert.setContentText("Los valores de los parametros a buscar, tienen que estar separados por comas. Ejemplo: nombre,fecha");
            alert.show();
        }
        int valuebusqueda = datosBusqueda.size();
        if(flagbusca){
        if(datosBusqueda.size() == 1){
            System.out.println("Busqueda simple");
            
            ArrayList<Foto> resultadosBFotos = App.sys.buscaSimpleFoto(opcionesBusqueda.getLast(), datosBusqueda.getLast(), App.sys.getListaFotosSistema());
            for(Foto f: resultadosBFotos){
                System.out.println(f.getPhotoid());
            }
            double totalFotos = (double) resultadosBFotos.size();
            int numRows = (int) Math.ceil(totalFotos/3);
            if(numRows == 0){
                numRows = 1;
            }
            
            paneFotos.setContent(drawBus(numRows, 4, resultadosBFotos));
        }
        if(datosBusqueda.size() > 1){
            ArrayList<String> opbn = new ArrayList<String>();
             for(int i=opcionesBusqueda.size()-valuebusqueda;i<opcionesBusqueda.size();i++){
                opbn.addLast(opcionesBusqueda.get(i));
               
            }
            System.out.println("Busqueda compleja");
            ArrayList<Foto> resultadosFotos = new ArrayList<Foto>();
            resultadosFotos = App.sys.buscaComplexFoto(opbn,datosBusqueda,App.sys.getListaFotosSistema());

            double totalFotos = (double) resultadosFotos.size();
            int numRows = (int) Math.ceil(totalFotos/3);
            if(numRows == 0){
                numRows = 1;
            }
            
            paneFotos.setContent(drawBus(numRows, 4, resultadosFotos));
        }
        }
    }

    @FXML
    private void buttonBuscaAlbum(ActionEvent event) throws IOException{
        boolean flagbusca = true;
        String busqueda = txtbuscaAlbum.getText();
        
        String[] datos = busqueda.split(",");
        
        ArrayList<String> datosBusqueda = new ArrayList<>();
        
        for(String d: datos){
            if(!(d.equals(""))){
            datosBusqueda.addLast(d);
            }
        }
        int valuebusqueda = datosBusqueda.size();
        //itere i=opcionesBusqueda.size()-valuebusqueda;i<opcionesBusqueda.size()-1;i++
        if(datosBusqueda.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error de Busqueda");
            alert.setContentText("Los valores de los parametros a buscar, tienen que estar separados por comas. Ejemplo: nombre,fecha");
            alert.show();
        }
        
        if(flagbusca){
        if(datosBusqueda.size() == 1){

            ArrayList<String> resultadosBAlbum = App.sys.buscaSimpleAlbum(opcionesBusqueda.getLast(), datosBusqueda.getLast(), App.sys.getListaAlbumes());
            ArrayList<Foto> resultadosBFoto = App.sys.buscaSimpleFoto(opcionesBusqueda.getLast(), datosBusqueda.getLast(), App.sys.getListaFotosSistema());
            
            CircularDoubleLinkedList<Foto> resultadosBFotoCircular = new CircularDoubleLinkedList<>();
            
            for(Foto f: resultadosBFoto){
                resultadosBFotoCircular.addLast(f);
            }
            
            ArrayList<Album> listaRes = new ArrayList<Album>();
            for(Album a: App.sys.getListaAlbumes()){
                String aid = a.getId();
                aid = aid.replace("\"", "");
                for(String id: resultadosBAlbum){
                    String idnew = id.replace("\"", "");
                    //System.out.println(id);
                    if(idnew.equals(aid)){
                        listaRes.addLast(a);
                    }
                
                }
                
            }
            
            ArrayList<Album> listaResFiltro = new ArrayList<Album>();
            for(Album a: listaRes){
                if(!listaResFiltro.contains(a)){
                    listaResFiltro.addLast(a);
                }
            }
            
            
            
            App.sys.albumTemp = new Album("atemp", "Resultados de Búsqueda", "Resultados de la búsqueda realizada", resultadosBFotoCircular);
            listaResFiltro.addLast(App.sys.albumTemp);
            
            for(Album asd: listaResFiltro){
                System.out.println(asd.getId());
            }
            
            buttonsAlbum.getChildren().clear();
            agregarBotones(listaResFiltro);
        }
        
        if(datosBusqueda.size() > 1){
            ArrayList<String> opbn = new ArrayList<String>();
            ArrayList<String> resultadosBAlbum = new ArrayList<String>();
            
            for(int i=opcionesBusqueda.size()-valuebusqueda;i<opcionesBusqueda.size();i++){
                opbn.addLast(opcionesBusqueda.get(i));
            }
            
            ArrayList<String> opbnc = new ArrayList<>();
            ArrayList<String> datosBusquedac = datosBusqueda;
            
            for(int i=0; i<opbn.size();i++){
                opbnc.addLast(opbn.get(i));
                datosBusquedac.addLast(datosBusqueda.get(i));
            }
            
            resultadosBAlbum = App.sys.buscaComplexAlbum(opbn, datosBusqueda, App.sys.getListaAlbumes());
            
            ArrayList<Foto> resultadosBFoto = App.sys.buscaComplexFoto(opbnc, datosBusquedac, App.sys.getListaFotosSistema());
            
            
            
            CircularDoubleLinkedList<Foto> resultadosBFotoCircular = new CircularDoubleLinkedList<>();
            
            for(Foto f: resultadosBFoto){
                resultadosBFotoCircular.addLast(f);
            }
                        
            ArrayList<Album> listaRes = new ArrayList<Album>();
            for(Album a: App.sys.getListaAlbumes()){
                String aid = a.getId();
                aid = aid.replace("\"", "");
                for(String id: resultadosBAlbum){
                    String idnew = id.replace("\"", "");
                    //System.out.println(id);
                    if(idnew.equals(aid)){
                        listaRes.addLast(a);
                    }
                }
            }
            
            ArrayList<Album> listaResFiltro = new ArrayList<Album>();
            for(Album a: listaRes){
                if(!listaResFiltro.contains(a)){
                    listaResFiltro.addLast(a);
                }
            }
            
            
            App.sys.albumTemp = new Album("atemp", "Resultados de Búsqueda", "Resultados de la búsqueda realizada", resultadosBFotoCircular);
            listaResFiltro.addLast(App.sys.albumTemp);
            
            for(Album asd: listaResFiltro){
                System.out.println(asd.getId());
            }
                        
            buttonsAlbum.getChildren().clear();
            
            agregarBotones(listaResFiltro);
            
        }
        }
        
    }

    @FXML
    private void buttonReload(ActionEvent event) throws IOException {
            refresh();
        
    }

    @FXML
    private void buttonActualizarAlbum(ActionEvent event) throws IOException {
        refreshAlbum();
    }
    
    private void refreshAlbum() throws IOException{
        opcionesBusqueda = new ArrayList<String>();
        chkBuscaAlbum.getCheckModel().clearChecks();
        txtbuscaAlbum.setText("");
        buttonsAlbum.getChildren().clear();
        agregarBotones(App.sys.getListaAlbumes());
    }
    
    private void refresh() throws IOException{
        
        double totalFotos = (double) App.sys.getListaFotosSistema().size();
            int numRows = (int) Math.ceil(totalFotos/3);
            if(numRows == 0){
                numRows = 1;
            }
            chkBoxBusqueda.getCheckModel().clearChecks();
            opcionesBusqueda = new ArrayList<String>();
            txtBuscaFoto.setText("");
            paneFotos.setContent(drawBus(numRows, 4, App.sys.getListaFotosSistema()));
    }
    
}
