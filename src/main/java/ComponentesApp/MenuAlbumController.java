/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ComponentesApp;

import Modelo.Album;
import Modelo.Foto;
import Util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author ronal
 */
public class MenuAlbumController implements Initializable {
    
    @FXML
    private VBox buttonsAlbum;
    @FXML
    private AnchorPane anchorPaneFotos;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
        try {
            agregarBotones();
            
            double totalFotos = (double) App.sys.getListaFotosSistema().size();
            int numRows = (int) Math.ceil(totalFotos/3);
            
            anchorPaneFotos.getChildren().add(drawBus(numRows, 4));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }  
    
    private void agregarBotones() {
        for(Album a: App.sys.getListaAlbumes()) {
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
    
    public static GridPane drawBus(int rows, int col) throws FileNotFoundException{
        GridPane table = new GridPane();
        table.setHgap(25);
        table.setVgap(25);
        table.setAlignment(Pos.CENTER);

        ArrayList<Foto> fotos = App.sys.getListaFotosSistema();
        int x = 0;
        
        for(int i=0; i<rows; i++){
            for(int j=0;j<col; j++) {
                if(x<fotos.size()){
                    Foto photo = fotos.get(x);
                    String rutaFoto = photo.getRuta().replace("\"", "");
                    Image image = new Image(new FileInputStream(rutaFoto));
                    ImageView imageView = new ImageView(image);
                    imageView.setPreserveRatio(true);
                    imageView.setFitHeight(150); 
                    imageView.setFitWidth(150);
                    
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
    
}
