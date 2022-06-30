/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ComponentesApp;

import Modelo.Album;
import Modelo.Foto;
import Modelo.Persona;
import Util.ArrayList;
import Util.CircularDoubleLinkedList;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author glorona
 */
public class Sistema {
    
    private ArrayList<Album> listaAlbumes;
    private ArrayList<Persona> listaPersonas;
    private ArrayList<Foto> listaFotosSistema;
    
    public Sistema(){
        this.listaAlbumes = construyeAlbumes(App.rutaAlbum);
        this.listaPersonas = construyePersonas(App.rutaPersona);
        this.listaFotosSistema = construyeFotos(App.rutaFoto);
        
        colocaFotosAlbum(listaAlbumes,listaFotosSistema);
        
    }
    
    
    //Construye Albumes previamente Cargados, pero vacios para ser asociados a las fotos luego
    public ArrayList<Album> construyeAlbumes(String ruta){
        
        ArrayList<Album> listaA = new ArrayList<Album>();
         try(InputStream input = new URL("file:"+ ruta).openStream()){
             BufferedReader lector = new BufferedReader(new InputStreamReader(input));
             String linea = null;
             while((linea = lector.readLine())!= null){
                 System.out.println("Linea:" + linea);
                 String[] datos = linea.split("#");
                 String id = datos[0];
                 String nombre = datos[1];
                 String descripcion = datos[2];
                 listaA.addLast(new Album(id,nombre,descripcion,null));
                 
             }
         }
         catch(IOException ex){
             System.out.println(ex.getMessage());
             
         }
         return listaA;
     }
    
    //Construye Personas previamente Cargados
    public ArrayList<Persona> construyePersonas(String ruta){
        ArrayList<Persona> listaP = new ArrayList<Persona>();
        try(InputStream input = new URL("file:" + ruta).openStream()){
             BufferedReader lector = new BufferedReader(new InputStreamReader(input));
             String linea = null;
             while((linea = lector.readLine())!= null){
                 String[] datos = linea.split("#");
                 String pid = datos[0];
                 String nombre = datos[1];
                 listaP.addLast(new Persona(pid,nombre));
                 
             }
         }
         catch(IOException ex){
             System.out.println(ex.getMessage());
             
         }
         return listaP;
        
        
    }
        
    //Construye Fotos previamente cargadas
    public ArrayList<Foto> construyeFotos(String ruta){
        ArrayList<Foto> listafotos = new ArrayList<Foto>();
         try(InputStream input = new URL("file:" + ruta).openStream()){
             BufferedReader lector = new BufferedReader(new InputStreamReader(input));
             String linea = null;
             while((linea = lector.readLine())!= null){
                 String[] datos = linea.split("#");
                 String id = datos[0];
                 String name = datos[1];
                 String place = datos[2];
                 String route =  datos[3];
                 //dudoso pero weno
                 String[] datosalbumes = datos[4].split(",");
                 ArrayList<String> al = new ArrayList<String>();
                 for(int i = 0; i<datosalbumes.length;i++){
                     al.addLast(datosalbumes[i]);
                 
                 }
                 String[] datospersonas = datos[5].split(",");
                 ArrayList<String> ps = new ArrayList<String>();
                 for(int i = 0; i<datospersonas.length;i++){
                     ps.addLast(datospersonas[i]);
                 
                 }
                 
                 String desc = datos[6];
                 DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                 Calendar cal = Calendar.getInstance();
                 cal.setTime(df.parse(datos[7]));
               
                 listafotos.addLast(new Foto(id,name,place,route,al,ps,desc,cal)); //String n, String p, String ruta, ArrayList<Album> al, ArrayList<Persona> ps, String desc, Calendar f
                
             }
         }
         catch(ParseException ex){
             java.lang.System.out.println(ex.getMessage());
             
         }
         catch(IOException ex){
             java.lang.System.out.println(ex.getMessage());
             
         }
         return listafotos;
         
     }
    
    //coloca las fotos existentes en los distintos albumes
    public void colocaFotosAlbum(ArrayList<Album> listaAlbum, ArrayList<Foto> listaFotos){
        for(Foto f:listaFotos){
            //recorrer la lista de albumid de cada foto
            for(int i=0; i<f.getAlbum().size();i++){
                //verificar el id y obtener album de lista de albumes
                String albumid_act = f.getAlbum().get(i);
                for(Album a: listaAlbum){ 
                    if(albumid_act.equals(a.getId())){
                        if(a.getFotos()!= null){
                            a.getFotos().addLast(f);
                        }
                        else{
                            a.setFotos(new CircularDoubleLinkedList<Foto>());
                            a.getFotos().addLast(f);
                        }
                    }
                }
                
            }
        }
        
    }
    
    
    //Escribir en archivos respectivos si se crea una nueva foto
    
    public boolean escribeFoto(Foto f, String ruta){
        try{
            StringBuilder stb = new StringBuilder();
            stb.append(f.getPhotoid());
            stb.append("#");
            stb.append(f.getName()); //String id,String n, String p, String rut, ArrayList<String> al, ArrayList<String> ps, String desc, Calendar f
            stb.append("#");
            stb.append(f.getPlace());
            stb.append("#");
            stb.append(f.getRuta());
            stb.append("#");
            for(String aid:f.getAlbum()){
                stb.append(aid);
                stb.append(",");
            }
            stb.delete(stb.length()-1,stb.length());
            stb.append("#");
            for(String aid:f.getPeople()){
                stb.append(aid);
                stb.append(",");
            }
            stb.delete(stb.length()-1,stb.length());
            stb.append("#");
            stb.append(f.getDescription());
            stb.append("#");
            //parse calendar
            stb.append(f.getFecha().toString());
            stb.append("\n");
            FileOutputStream escritor = new FileOutputStream(ruta);
          
            OutputStreamWriter output  = new OutputStreamWriter(escritor);
            
            output.write(stb.toString());
            
            output.close();
            
            return true;
            
            
            
        }
        catch (IOException e){
            java.lang.System.out.println(e.getMessage());
            
        }
        catch(Exception e){
            java.lang.System.out.println(e.getMessage());
            
        }
        return false;
        
        
    }
    
    //Escribe en archivos si se crea un nuevo album
    public boolean escribeAlbum(Album a, String ruta){
        try{
            StringBuilder stb = new StringBuilder();
            stb.append(a.getId());
            stb.append("#");
            stb.append(a.getName()); //(String aid, String name, String description
            stb.append("#");
            stb.append(a.getDescription());
            stb.append("\n");
            FileOutputStream escritor = new FileOutputStream(ruta);
          
            OutputStreamWriter output  = new OutputStreamWriter(escritor);
            
            output.write(stb.toString());
            
            output.close();
            
            return true;
            
            
            
        }
        catch (IOException e){
            java.lang.System.out.println(e.getMessage());
            
        }
        catch(Exception e){
            java.lang.System.out.println(e.getMessage());
            
        }
        return false;
        
        
        
    }
    
    public boolean escribePersona(Persona p, String ruta){
        try{
            StringBuilder stb = new StringBuilder();
            stb.append(p.getId());
            stb.append("#");
            stb.append(p.getName()); //(String aid, String name, String description
            stb.append("\n");
            FileOutputStream escritor = new FileOutputStream(ruta);
          
            OutputStreamWriter output  = new OutputStreamWriter(escritor);
            
            output.write(stb.toString());
            
            output.close();
            
            return true;
            
            
            
        }
        catch (IOException e){
            java.lang.System.out.println(e.getMessage());
            
        }
        catch(Exception e){
            java.lang.System.out.println(e.getMessage());
            
        }
        return false;
        
    }

    public ArrayList<Album> getListaAlbumes() {
        return listaAlbumes;
    }

    public ArrayList<Persona> getListaPersonas() {
        return listaPersonas;
    }

    public ArrayList<Foto> getListaFotosSistema() {
        return listaFotosSistema;
    }

}
