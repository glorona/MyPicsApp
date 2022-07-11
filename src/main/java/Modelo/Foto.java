package Modelo;

import Util.*;

import java.util.Calendar;

public class Foto
{
    //Aparecen en la table view
    private String photoid;
    private String name;
    private String place; //Lugar donde se tomo la foto
    private String keyWord = new String();
    private ArrayList<String> albumes;
    private Calendar fecha;
    private ArrayList<String> peopleid = new ArrayList(); //Lista de personas en la foto
    private String camid;
    private String ruta;
    private String description;

    private String comment;

    //constructor minimo
    public Foto(String id,String n, String p, String rut, ArrayList<String> al, ArrayList<String> ps, String desc, Calendar f){
        this.photoid = id;
        this.name = n;
        this.place = p;
        this.ruta = rut;
        this.description = desc;
        this.fecha = f;
        this.peopleid = ps;
        this.albumes = al;
    }
    
    public Foto(String id,String n, String p, String rut, ArrayList<String> al, ArrayList<String> ps, String desc, Calendar f, String cam){
        this.photoid = id;
        this.name = n;
        this.place = p;
        this.ruta = rut;
        this.description = desc;
        this.fecha = f;
        this.peopleid = ps;
        this.albumes = al;
        this.camid = cam;
    }

    public String getCamid() {
        return camid;
    }

    public void setCamid(String camid) {
        this.camid = camid;
    }
    
    
    
    public String getPhotoid() {
        return photoid;
    }

    public void setPhotoid(String photoid) {
        this.photoid = photoid;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public ArrayList<String> getAlbum() {
        return albumes;
    }

    public void setAlbum(ArrayList<String> album) {
        this.albumes = album;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public ArrayList<String> getPeople() {
        return peopleid;
    }

    public void setPeople(ArrayList<String> people) {
        this.peopleid = people;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    @Override
    public String toString(){
        return name.replace("\"", "");
    }
}
   
    
   
    