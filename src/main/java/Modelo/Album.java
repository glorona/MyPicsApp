package Modelo;

import Util.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Album
{   private String id;
    private String name;
    private String description;
    private CircularDoubleLinkedList<Foto> fotos;

    public Album(String aid, String name, String description, CircularDoubleLinkedList<Foto> fotos) {
        this.id = aid;
        this.name = name;
        this.description = description;
        this.fotos = fotos; 
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CircularDoubleLinkedList<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(CircularDoubleLinkedList<Foto> fotos) {
        this.fotos = fotos;
    }
       
    
}
