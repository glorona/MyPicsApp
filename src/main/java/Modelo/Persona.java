/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author glorona
 */
public class Persona {
    
    
    private String id;
    private String name;
    
    public Persona(String pid,String n){
        this.id = pid;
        this.name = n;
    }
    
    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
     
    
}
