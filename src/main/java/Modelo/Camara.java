/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author glorona
 */
public class Camara {
    private String id;
    private String modelo;
    private String tipo;
    
    
    public Camara(String id, String model, String ty){ 
        this.id = id;
        this.modelo = model;
        this.tipo = ty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return tipo.replace("\"", "") + " " + modelo.replace("\"", "");
    }
    
    
    
}
