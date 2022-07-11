/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ComponentesApp;

import Modelo.Album;
import Modelo.Camara;
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
import java.util.Date;

/**
 *
 * @author glorona
 */
public class Sistema {
    
    private ArrayList<Album> listaAlbumes;
    private ArrayList<Persona> listaPersonas;
    private ArrayList<Foto> listaFotosSistema;
    private ArrayList<Camara> listaCamaras;
    
    public Sistema(){
        this.listaAlbumes = construyeAlbumes(App.rutaAlbum);
        this.listaPersonas = construyePersonas(App.rutaPersona);
        this.listaFotosSistema = construyeFotos(App.rutaFoto);
        this.listaCamaras = construyeCamaras(App.rutaCamara);
        colocaFotosAlbum(listaAlbumes,listaFotosSistema);

    }
    
    
    public void colocaFotoAlbum(Album a, Foto f){
        //SOLO PARA COLOCAR FOTOS YA EXISTENTES EN ALBUMES.
        //recorrer la lista de albumes de la foto
        //verificar si el album id no esta en esta lista de albumes
        //si no esta, agregar y modificar el archivo de la foto
        //en album se debe agregar la foto al final de la lista
        boolean encontrado = false;
        
        for(String id: f.getAlbum()){
            if(id.equals(a.getId())){
                encontrado = true;
            }
        }
        if(encontrado){
            return;
        }
        else{
            f.getAlbum().addLast(a.getId());
            for(String id: f.getAlbum()){
                System.out.println(id);
            }
            ArrayList<String> cambios = new ArrayList<String>();
            cambios.addLast("album");
            modificaFoto(f,cambios,f.getAlbum());
            a.getFotos().addLast(f);
        }
        
        
        
    }
    
    
    //metodos de modificacion
    public void modificaAlbum(Album a, ArrayList<String> cambios, ArrayList<String> valores){
        //recorrer la lista de cambios a realizar
        for(String c: cambios){
            //SOLO SE PUEDEN REALIZAR CAMBIOS DE NOMBRE Y DE DESCRIPCION
            if(c.equals("name")){
                a.setName(valores.get(0));
                valores.remove(0); //como estan puestos en el orden de cambios es decir 
                //Arraylist de cambios: name, desc
                //arraylist de valores: albumnuevo, album a cambiar
                //removemos el primero para recorrer el proximo y definirlo. es decir, es una remocion lineal.
            }
            if(c.equals("desc")){
                a.setDescription(valores.get(0));
                valores.remove(0);
            }
        }
        
    }
    
    
    public void modificaFoto(Foto f, ArrayList<String> cambios, ArrayList<String> valores){
        for(String c: cambios){
            //SOLO SE PUEDEN REALIZAR CAMBIOS DE NOMBRE, LUGAR, ALBUMES EN LOS QUE ESTA, PERSONAS DENTRO DE LA FOTO, DESCRIPCION O FECHA
            //String id,String n, String p, String rut, ArrayList<String> al, ArrayList<String> ps, String desc, Calendar f
            if(c.equals("name")){
                f.setName(valores.get(0));
                valores.remove(0); //como estan puestos en el orden de cambios es decir 
                //Arraylist de cambios: name, desc
                //arraylist de valores: albumnuevo, album a cambiar
                //removemos el primero para recorrer el proximo y definirlo. es decir, es una remocion lineal.
            }
            if(c.equals("place")){
                f.setDescription(valores.get(0));
                valores.remove(0);
            }
            if(c.equals("album")){
                String[] valoresagg = valores.get(0).split("#");
                ArrayList<String> newids = new ArrayList<String>();
                for(int i = 0; i<valoresagg.length;i++){
                    newids.addLast(valoresagg[i]);
                }
                f.setAlbum(newids); //al modificar habra que enviar los albumes en formato a1#a2#a3#a4
                valores.remove(0);
            }
            if(c.equals("person")){
                String[] valoresagg = valores.get(0).split("#");
                ArrayList<String> newper = new ArrayList<String>();
                for(int i = 0; i<valoresagg.length;i++){
                    newper.addLast(valoresagg[i]);
                }
                f.setPeople(newper); //al modificar habra que enviar las fotos en formato f1#f2#f3#f4
                valores.remove(0);
            }
            
            if(c.equals("desc")){
                f.setDescription(valores.get(0));
                valores.remove(0);
                
            }
            if(c.equals("date")){
                try{
                 DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                 Calendar cal = Calendar.getInstance();
                 cal.setTime(df.parse(valores.get(0)));
                 f.setFecha(cal);
                 
                }
                catch(ParseException e){
                    System.out.println(e.getMessage());
                }
                valores.remove(0);
            }
            if(c.equals("cam")){
                f.setCamid(valores.get(0));
                valores.remove(0);
            }
            
            
        }
        
    }
    
    public void modificaPersona(Persona p, ArrayList<String> cambios, ArrayList<String> valores){
        //recorrer la lista de cambios a realizar
        for(String c: cambios){
            //SOLO SE PUEDEN REALIZAR CAMBIOS DE NOMBRE de la persona
            if(c.equals("name")){
                p.setName(valores.get(0));
                valores.remove(0); //como estan puestos en el orden de cambios es decir 
                
            }
        }
        
        
    }
    
    public void eliminaLineaFoto(Foto f, String ruta, String directorio){
        //buscar la foto por su id en el archivo
        //modificar la linea
        //cerrar archivo


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
                     continue;
                 }
                 bw.write(linea + System.getProperty("line.separator"));


            }

             bw.close();
             lector.close();
             Path raiz = Paths.get(ruta);
             Path destino = Paths.get(directorio + "archivotemp.txt");
             Files.move(destino, raiz, StandardCopyOption.REPLACE_EXISTING);

         }
         catch(IOException ex){
             java.lang.System.out.println(ex.getMessage());

         }

    }
    
    public void modificaCamara(Camara cam, ArrayList<String> cambios, ArrayList<String> valores){
        
        for(String c: cambios){
            //SOLO SE PUEDEN REALIZAR CAMBIOS DE NOMBRE DEL MODELO, TIPO DE CAMARA.
            if(c.equals("modelo")){
                cam.setModelo(valores.get(0));
                valores.remove(0); //como estan puestos en el orden de cambios es decir 
                
            }
            if(c.equals("type")){
                String marca = valores.get(0); //canon,nikon,sony,olympus,fujifilm;
                 cam.setTipo(marca);
                 valores.remove(0);
                
            }
        }
        
        
    }
    
    //metodos de busqueda
    public ArrayList<Foto> buscaSimpleFoto(String parametro, String valor, ArrayList<Foto> listaFotos){
        ArrayList<Foto> listaRetorno = new ArrayList<Foto>();
        //se puede buscar por fecha,por personas que salgan en la foto, y por lugar.
        if(parametro.equals("place")){
            for(Foto f: listaFotos){
            if(valor.equals(f.getPlace())){
                listaRetorno.addLast(f);
                
                }
            }
            return listaRetorno;
        }
        if(parametro.equals("people")){
            
            String[] datospeople = valor.split("#"); //formato de envio "p1#p2#p3"
            int confirmaciones_ne = datospeople.length; //obtener la cantidad de confirmaciones necesarias para confirmar que la foto vale
            int confirmaciones_act = 0;
            for(Foto f: listaFotos){
                confirmaciones_act = 0;
                 for(int i = 0; i<datospeople.length;i++){
                     Persona per = null;
                     for(Persona p: listaPersonas){
                         String p_finder = p.getName();
                         p_finder = p_finder.replace("\"", "");
                         if(p_finder.equals(datospeople[i])){
                             
                             per = p;
                         }
                     }
                     String persona_act = per.getId(); //p1
                     persona_act = persona_act.replace("\"", "");
                     //System.out.println(persona_act);
                     for(String idpersona: f.getPeople()){
                         idpersona = idpersona.replace("\"", "");
                         if(idpersona.equals(persona_act)){
                             confirmaciones_act++;
                         }
                     }
                 }
                 if(confirmaciones_ne == confirmaciones_act){
                     listaRetorno.addLast(f);
                 }
                
            }
            return listaRetorno;
        }
        if(parametro.equals("fecha")){
            //la fecha tiene que estar en formato "FechaInicio-FechaFin"
            //Formato fechainicio y fechafin 21.07.2022 DD.MM.YYYY
            String[] datosfechas = valor.split("-");
            try{
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                 Calendar cal = Calendar.getInstance();
                 Date iniciofecha = df.parse(datosfechas[0]);
                 Date finfecha = df.parse(datosfechas[1]);
                 for(Foto f: listaFotos){
                     if(f.getFecha().getTime().after(iniciofecha) && f.getFecha().getTime().before(finfecha)){
                         listaRetorno.addLast(f);
                     }
                 }
                
            }
            catch(ParseException ex){
                System.out.println(ex.getMessage());
            }
            
            return listaRetorno;
        }
        
        //POR HACER: BUSCAR POR CAMARA (MARCA O MODELO)
        if(parametro.equals("camara")){
            Camara cfound = null;
            for(Camara c:listaCamaras){
                    String c_busca = c.getModelo().replace("\"", "");
                    if(c_busca.equals(valor)){
                        cfound = c;
                    }
                    
                }
            for(Foto f: listaFotos){
                if(f.getCamid() != null){
                
                
                //Buscar el id por el nombre
                //coincide toma esa camara
                //busca las fotos que tengan ese id de camara
                
                
                //nombre
                String camPro = f.getCamid().replace("\"","");
                String c_found = cfound.getId().replace("\"", "");
                if(camPro.equals(c_found)){
                    listaRetorno.addLast(f);
                }
                

            }
            }
            return listaRetorno;
            
        }
        
        
        
        
        
        
        return listaRetorno;
        
    }
    
    public ArrayList<String> buscaSimpleAlbum(String parametro, String valor, ArrayList<Album> listaAlbumes){
        //Metodo devuelve una lista de albumes con fotos que contengan el parametro solicitado
        //Se puede buscar por fecha,por personas que salgan en la foto, y por lugar.
            ArrayList<String> listaRetorno = new ArrayList<String>();
            ArrayList<Foto> result = null;
            if(parametro.equals("people")){
                //llamar al metodo de buscar foto ya que va a retornar lista global de fotos que contengan ese parametro solicitado
                result = buscaSimpleFoto("people",valor,listaFotosSistema);
                
            }
            if(parametro.equals("fecha")){
                //llamar al metodo de buscar foto ya que va a retornar lista global de fotos que contengan ese parametro solicitado
                result = buscaSimpleFoto("date",valor,listaFotosSistema);
                
            }
            if(parametro.equals("place")){
                //llamar al metodo de buscar foto ya que va a retornar lista global de fotos que contengan ese parametro solicitado
                result = buscaSimpleFoto("place",valor,listaFotosSistema);
                
                
            }
            
            if(parametro.equals("camara")){
                result = buscaSimpleFoto("camara",valor,listaFotosSistema);
            }
            
            
            //tendriamos que buscar los albumid a los que pertenece cada foto
            for(Album a: listaAlbumes){
                    for(Foto f: result){
                    ArrayList<String> albumid = f.getAlbum();
                    for(String aid: albumid){
                        if(aid.equals(a.getId()) && !(listaRetorno.contains(a.getId()))){
                            listaRetorno.addLast(a.getId());
                           
                            }
                        }   
                    }
              
                    
                }
          
            return listaRetorno;
            
            //por hacer camara modelo y marca
            
        
    
        }
    
    public ArrayList<Foto> buscaComplexFoto(ArrayList<String> parametro, ArrayList<String> valor, ArrayList<Foto> listaFotos){
        ArrayList<Foto> listaRetorno = new ArrayList<Foto>();
        
        //Obtener primer parametro y hacer busqueda inicial
        listaRetorno = buscaSimpleFoto(parametro.get(0),valor.get(0),listaFotosSistema);
        parametro.remove(0);
        valor.remove(0);
        int tamparametrosinit = parametro.size(); //numero de veces a seguir haciendo el filtrado
        if(tamparametrosinit>0){
        for(int i=0; i<tamparametrosinit-1;i++){ //-1 a cambiar por + 0 en el caso de fallo. Itera la cantidad de veces que
            //Agregar todas las fotos localizadas en la primera busqueda a un arrayList
            ArrayList<Foto> nuevaLista = new ArrayList<Foto>();
            for(Foto f: listaRetorno){
                nuevaLista.addLast(f);
                }
            listaRetorno = buscaSimpleFoto(parametro.get(0),valor.get(0),nuevaLista); //volver a realizar la busqueda pero con la lista ya filtrada
            parametro.remove(0); //sacar el parametro y valor ya procesado
            valor.remove(0);
        }
        
        }
        return listaRetorno;
        
        
        
    }
    
    public ArrayList<String> buscaComplexAlbum(ArrayList<String> parametro, ArrayList<String> valor, ArrayList<Album> listaAlbumes){
        ArrayList<String> listaRetorno = new ArrayList<String>();
        ArrayList<Foto> result = null;
        result = buscaComplexFoto(parametro,valor,listaFotosSistema);
        
        for(Album a: listaAlbumes){
                    for(Foto f: result){
                    ArrayList<String> albumid = f.getAlbum();
                    for(String aid: albumid){
                        if(aid.equals(a.getId()) && !(listaRetorno.contains(a.getId()))){
                            listaRetorno.addLast(a.getId());
                           
                            }
                        }   
                    }
              
                    
                }
          
            return listaRetorno;
            
        
        
        
    }

    //Construye Camaras
    public ArrayList<Camara> construyeCamaras(String ruta){
         ArrayList<Camara> listaC = new ArrayList<Camara>();
         try(InputStream input = new URL("file:"+ ruta).openStream()){
             BufferedReader lector = new BufferedReader(new InputStreamReader(input));
             String linea = null;
             while((linea = lector.readLine())!= null){
                 //System.out.println("Linea:" + linea);
                 String[] datos = linea.split("#");
                 String id = datos[0];
                 String modelo = datos[1];
                 String marca = datos[2]; //canon,nikon,sony,olympus,fujifilm;
                 listaC.addLast(new Camara(id,modelo,marca));
                 
             }
         }
         catch(IOException ex){
             System.out.println(ex.getMessage());
             
         }
         return listaC;
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
                 if(datos.length > 8 ){
                     String camid = datos[8];
                     listafotos.addLast(new Foto(id,name,place,route,al,ps,desc,cal,camid));
                }
                 else{
               
                 listafotos.addLast(new Foto(id,name,place,route,al,ps,desc,cal)); //String n, String p, String ruta, ArrayList<Album> al, ArrayList<Persona> ps, String desc, Calendar f
                 }
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
    
    
    //autoasigna id para creacion de objetos
    public String getLastPhID(ArrayList<Foto> listaFotos){
        Foto ultimafoto = listaFotos.getLast();
        String ultid = ultimafoto.getPhotoid();
        int extracomilla = ultid.length() - 1;
        String numget = ultid.substring(ultid.indexOf("f")+1,extracomilla);
        
        int number = Integer.parseInt(numget);
        number++;
        String returning = "f" + String.valueOf(number);
        return returning;
        
    }
    public String getLastAlID(ArrayList<Album> listaAlbum){
        Album ultimoalbum = listaAlbum.getLast();
        String ultid = ultimoalbum.getId();
        int extracomilla = ultid.length() - 1;
        String numget = ultid.substring(ultid.indexOf("a")+1,extracomilla);
        
        int number = Integer.parseInt(numget);
        number++;
        String returning = "a" + String.valueOf(number);
        return returning;
        
    }
    public String getLastPerID(ArrayList<Persona> listaPersona){
        Persona ultimapersona = listaPersona.getLast();
        String ultid = ultimapersona.getId();
        int extracomilla = ultid.length() - 1;
        String numget = ultid.substring(ultid.indexOf("p")+1,extracomilla);
        
        int number = Integer.parseInt(numget);
        number++;
        String returning = "p" + String.valueOf(number);
        return returning;
        
    }
    public String getLastCamID(ArrayList<Camara> listaCams){
        Camara ultimacam = listaCams.getLast();
        String ultid = ultimacam.getId();
        int extracomilla = ultid.length() - 1;
        String numget = ultid.substring(ultid.indexOf("c")+1,extracomilla);
        
        int number = Integer.parseInt(numget);
        number++;
        String returning = "c" + String.valueOf(number);
        return returning;
        
    }
    
    
    
    
    public boolean eliminaCamara(Camara c, String ruta, String directorio){
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
                 if(id.equals(c.getId())){
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
             
             for(int i=0; i<listaCamaras.size(); i++){
                if(listaCamaras.get(i).getId().equals(c.getId())){
                    listaCamaras.remove(i);
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
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            
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
            stb.append(dateFormat.format(f.getFecha().getTime()));
            stb.append("#");
            if(f.getCamid() != null){
                stb.append(f.getCamid());
            }
            stb.append("\n");
            
            FileOutputStream escritor = new FileOutputStream(ruta, true);
          
            OutputStreamWriter output  = new OutputStreamWriter(escritor);
            
            output.write(stb.toString());
            
            output.flush();
            
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
    public boolean escribeCamara(Camara c, String ruta){
        try{
            StringBuilder stb = new StringBuilder();
            stb.append(c.getId());
            stb.append("#");
            stb.append(c.getModelo()); //(String aid, String name, String description
            stb.append("#");
            stb.append(c.getTipo().toString());
            stb.append("\n");
            FileOutputStream escritor = new FileOutputStream(ruta, true); //true para append al archivo
          
            OutputStreamWriter output  = new OutputStreamWriter(escritor);
            

            
            output.write(stb.toString());
            
            output.flush();
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
            FileOutputStream escritor = new FileOutputStream(ruta,true);
          
            OutputStreamWriter output  = new OutputStreamWriter(escritor);
            
            output.write(stb.toString());
            
            output.flush();
            
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
            FileOutputStream escritor = new FileOutputStream(ruta,true);
          
            OutputStreamWriter output  = new OutputStreamWriter(escritor);
            
            output.write(stb.toString());
            
            output.flush();
            
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

    public ArrayList<Camara> getListaCamaras() {
        return listaCamaras;
    }

    public void setListaAlbumes(ArrayList<Album> listaAlbumes) {
        this.listaAlbumes = listaAlbumes;
    }

    public void setListaPersonas(ArrayList<Persona> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    public void setListaFotosSistema(ArrayList<Foto> listaFotosSistema) {
        this.listaFotosSistema = listaFotosSistema;
    }

    public void setListaCamaras(ArrayList<Camara> listaCamaras) {
        this.listaCamaras = listaCamaras;
    }
    
    

}

