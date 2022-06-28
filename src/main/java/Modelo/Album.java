package Modelo;

import Util.*;

public class Album
{
    private String name;
    private String description;
    private CircularDoubleLinkedList<Foto> fotos;

    public Album(String name, String description, CircularDoubleLinkedList<Foto> fotos) {
        this.name = name;
        this.description = description;
        this.fotos = fotos;
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
