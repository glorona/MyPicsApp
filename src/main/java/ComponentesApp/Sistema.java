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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
                 //System.out.println("Linea:" + linea);
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
    
    public boolean eliminaFoto(Foto f, String ruta, String directorio){
        //buscar la foto por su id en el archivo
        //sacar la foto de lista del sistema
        //borrar la linea de la foto en el archivo
        
        try(InputStream input = new URL("file:" + ruta).openStream()){
             File archespecifico = new File(new URL("file:" + ruta).toString());
             File archtemporal = new File(directorio + "archivotemp.txt");
             BufferedReader lector = new BufferedReader(new InputStreamReader(input));
             BufferedWriter bw = new BufferedWriter(new FileWriter(archtemporal));
             String linea = null;
             while((linea = lector.readLine())!= null){
                 String[] datos = linea.split("#");
                 String id = datos[0];
                 if(id.equals(f.getPhotoid())){
                     System.out.println("Encontre id");
                     continue;
                 }
                 bw.write(linea + System.getProperty("line.separator"));
                 
                 
                
             
             }
             bw.close();
             lector.close();
             Path raiz = Paths.get(ruta);
             Path destino = Paths.get(directorio + "archivotemp.txt");
             Files.move(destino, raiz, StandardCopyOption.REPLACE_EXISTING);
             
             for(int i=0; i<listaFotosSistema.size(); i++){
                if(listaFotosSistema.get(i).getPhotoid().equals(f.getPhotoid())){
                    listaFotosSistema.remove(i);
                }
                i++;
             }
             return true;
             
         }
         catch(IOException ex){
             java.lang.System.out.println(ex.getMessage());
             
         }
         return false;
        
        
    }
    public boolean eliminaPersona(Persona p, String ruta, String directorio){
        //buscar la persona por su id en el archivo
        //sacar la persona de lista del sistema
        //borrar la linea de la persona en el archivo
        
        try(InputStream input = new URL("file:" + ruta).openStream()){
             File archespecifico = new File(new URL("file:" + ruta).toString());
             File archtemporal = new File(directorio + "archivotemp.txt");
             BufferedReader lector = new BufferedReader(new InputStreamReader(input));
             BufferedWriter bw = new BufferedWriter(new FileWriter(archtemporal));
             String linea = null;
             while((linea = lector.readLine())!= null){
                 String[] datos = linea.split("#");
                 String id = datos[0];
                 if(id.equals(p.getId())){
                     System.out.println("Encontre id");
                     continue;
                 }
                 bw.write(linea + System.getProperty("line.separator"));
                 
                 
                
             
             }
             bw.close();
             lector.close();
             Path raiz = Paths.get(ruta);
             Path destino = Paths.get(directorio + "archivotemp.txt");
             Files.move(destino, raiz, StandardCopyOption.REPLACE_EXISTING);
             
             for(int i=0; i<listaPersonas.size(); i++){
                if(listaPersonas.get(i).getId().equals(p.getId())){
                    listaPersonas.remove(i);
                }
                i++;
             }
             return true;
             
         }
         catch(IOException ex){
             java.lang.System.out.println(ex.getMessage());
             
         }
         return false;
        
        
    }
    
    public boolean eliminaAlbum(Album a, String ruta, String directorio){
        //buscar album por su id en el archivo
        //sacar album de lista del sistema
        //borrar la linea del album en el archivo
        
        try(InputStream input = new URL("file:" + ruta).openStream()){
             File archespecifico = new File(new URL("file:" + ruta).toString());
             File archtemporal = new File(directorio + "archivotemp.txt");
             BufferedReader lector = new BufferedReader(new InputStreamReader(input));
             BufferedWriter bw = new BufferedWriter(new FileWriter(archtemporal));
             String linea = null;
             while((linea = lector.readLine())!= null){
                 String[] datos = linea.split("#");
                 String id = datos[0];
                 if(id.equals(a.getId())){
                     System.out.println("Encontre id");
                     continue;
                 }
                 bw.write(linea + System.getProperty("line.separator"));
                 
                 
                
             
             }
             bw.close();
             lector.close();
             Path raiz = Paths.get(ruta);
             Path destino = Paths.get(directorio + "archivotemp.txt");
             Files.move(destino, raiz, StandardCopyOption.REPLACE_EXISTING);
             
             for(int i=0; i<listaAlbumes.size(); i++){
                if(listaAlbumes.get(i).getId().equals(a.getId())){
                    listaAlbumes.remove(i);
                }
                i++;
             }
             return true;
             
         }
         catch(IOException ex){
             java.lang.System.out.println(ex.getMessage());
             
         }
         return false;
        
        
    }
    
    //coloca las fotos existentes en los distintos albumes
    public void colocaFotosAlbum(ArrayList<Album> listaAlbum, ArrayList<Foto> listaFotos){
        for(Foto f:listaFotos){
            //System.out.println(f);
            //recorrer la lista de albumid de cada foto
            for(int i=0; i<f.getAlbum().size();i++){
                //verificar el id y obtener album de lista de albumes
                String albumid_act = f.getAlbum().get(i);
                //System.out.println(albumid_act);
                for(Album a: listaAlbum){ 
                    //System.out.println(a.getId());
                    if(albumid_act.equals(a.getId())){
                        //System.out.println("igual");
                        if(a.getFotos()!= null){
                            //System.out.println("agregar foto");
                            a.getFotos().addLast(f);
                        }
                        else{
                            //System.out.println("crear album");
                            a.setFotos(new CircularDoubleLinkedList<Foto>());
                            a.getFotos().addLast(f);
                            //System.out.println(a.getFotos());
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

