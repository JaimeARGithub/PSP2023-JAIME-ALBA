/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.carrera;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaime
 */

/**
 * Ejercicio: realizar un programa que realiza una carrera de coches
 * --clase carrera: al crearse se indica la distancia a recorrer entre 100 y 1000 metros, termina
 * cuando un coche llega a la meta (recurso compartido), va a tener una distancia y un ganador
 * 
 * --coches: los hilos, van a avanzar, tienen nombre, nº metros que avanzan (entre 1 y 50), por cada
 * iteración los coches avanzan los metros aleatorios que les salgan, cuando un coche llegue a la 
 * distancia hay que poner que ha ganado y decir ha ganado el coche tal
 * 
 * --el programa principal crea los coches, 3: Ford, Opel y Seat
 * --al acabar hay que mostrar el podio: primero, segundo y tercero, según distancia recorrida cada uno
 */


public class Carrera {

    public static void main(String[] args) {
        Random r = new Random();
        int metrosCarrera = 100 + r.nextInt(901);
        Recorrido carrera = new Recorrido(metrosCarrera);
        System.out.println("Carrera iniciada. El recorrido es de " +metrosCarrera+ " metros.");
        
        String[] nombres = {"Ford", "Opel", "Seat"};
        
        
        Thread[] hilos = new Thread[3];
        
        for (int i=0; i<3; i++) {
            Coche coc = new Coche(nombres[i], carrera);
            
            hilos[i] = new Thread(coc);
            hilos[i].start();
        }
        
        for (int i=0; i<3; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                System.out.println("Excepción de tipo interrupted");
            }
        }
        
        carrera.getPodio();
    }
}

class Coche implements Runnable {
    private String nombreCoche;
    private final Recorrido rec;
    private int distanciaRecorrida;
    
    public Coche(String nombre, Recorrido rec) {
        this.nombreCoche=nombre;
        this.rec=rec;
        this.distanciaRecorrida=0;
    }
    
    @Override
    public void run() {
        Random r = new Random();
        int metrosAvance;
        
        
        while (!rec.getGanada()) {
            metrosAvance = 1 + r.nextInt(50);
            this.distanciaRecorrida = this.distanciaRecorrida + metrosAvance;
            System.out.println("El coche "+this.nombreCoche+" lleva recorridos "+this.distanciaRecorrida+" metros.");
            
            if (distanciaRecorrida > rec.getDistancia()) {
                rec.setGanada();
                rec.setGanador(nombreCoche);
                System.out.println("¡Ha ganado el coche "+this.nombreCoche+"!");
            }
                
                
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Coche.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        rec.añadePodio(nombreCoche, distanciaRecorrida);
        
    }
    
}


class Recorrido {
    private int distancia;
    private String ganador;
    private boolean ganada;
    private LinkedHashMap<String, Integer> podio;
    
    public Recorrido(int metros) {
        this.distancia = metros;
        this.ganada= false;
        this.podio = new LinkedHashMap<String, Integer>();
    }
    
    public int getDistancia() {
        return this.distancia;
    }
    
    public boolean getGanada() {
        return this.ganada;
    }
    
    public synchronized void setGanada() {
        this.ganada=true;
    }
    
    public synchronized void setGanador(String nombreGanador) {
        this.ganador = nombreGanador;
    }
    
    public synchronized void añadePodio(String nombreCoche, int distanciaRecorrida) {
        podio.put(nombreCoche, distanciaRecorrida);
    }

    public void getPodio() {
        List<Map.Entry<String,Integer>> podioLista = new ArrayList<>(this.podio.entrySet());
        podioLista.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        
        int puesto = 1;
        for(Map.Entry<String,Integer> e: podioLista) {
            System.out.println("En la posición "+puesto+", el coche "+e.getKey()+", con una distancia recorrida de "+e.getValue()+" metros.");
            puesto++;
        }
    }
}
