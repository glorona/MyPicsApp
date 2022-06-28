package Modelo;

import Util.*;
import java.util.Calendar;

public class Foto
{
    //Aparecen en la table view
    private String name;
    private String place; //Lugar donde se tomo la foto
    private String keyWord = new String();
    private ArrayList<Album> album;
    private Calendar fecha;
    private ArrayList<Persona> people = new ArrayList(); //Lista de personas en la foto

    
    private String description;

    private String comment;

    //constructor minimo
    public Foto(String n, String p, ArrayList<Album> al, ArrayList<Persona> ps, String desc, Calendar f){
        this.name = n;
        this.place = p;
        this.description = desc;
        this.fecha = f;
        this.people = ps;
        this.album = al;
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

    public ArrayList<Album> getAlbum() {
        return album;
    }

    public void setAlbum(ArrayList<Album> album) {
        this.album = album;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Persona> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Persona> people) {
        this.people = people;
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
    

}
   
    
   
    