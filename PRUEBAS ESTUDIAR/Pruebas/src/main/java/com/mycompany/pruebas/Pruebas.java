/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pruebas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author jaime
 */
public class Pruebas {

    public static void main(String[] args) {
        Random r = new Random();
        int distancia = 500 + r.nextInt(501);
        System.out.println("Se inicia una carrera de " + distancia + " metros.");
        String[] nombres = {"Peugeot", "Citroën", "Nissan"};
        
        Recorrido c = new Recorrido(distancia);
        
        Thread[] hilos = new Thread[3];
        
        for (int i=0; i<3; i++) {
            hilos[i] = new Thread(new Coche(nombres[i], c));
            hilos[i].start();
        }
        
        for (int i=0; i<3; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        
        System.out.println("");
        System.out.println("");
        System.out.println("");
        
        TreeMap<String,Integer> podio = c.getPodio();
        List<Entry<String,Integer>> podioLista = new ArrayList<>(podio.entrySet());
        podioLista.sort(Entry.comparingByValue(Comparator.reverseOrder()));
        
        for (Entry<String,Integer> aux:podioLista) {
            System.out.println("El coche " + aux.getKey() + " ha recorrido " + aux.getValue() + " metros.");
        }
    }
}

class Coche implements Runnable {
    private String nombre;
    private int distRecorrida;
    private final Recorrido carrera;
    
    public Coche(String nom, Recorrido carr) {
        this.nombre=nom;
        this.distRecorrida=0;
        this.carrera=carr;
    }
    
    @Override
    public void run() {
        Random r = new Random();
        int distAvance;
        
        while (!carrera.getCompletado()) {
            distAvance = 1 + r.nextInt(50);
            distRecorrida += distAvance;
            System.out.println("El coche " + nombre + " hace un avance de " + distAvance + " metros. Ya lleva " + distRecorrida + "m");
            
            carrera.comprueba(nombre, distRecorrida);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
    
}

class Recorrido {
    private int distancia;
    private boolean completado;
    private TreeMap<String,Integer> podio;
    
    public Recorrido(int dist) {
        this.distancia=dist;
        this.completado=false;
        podio = new TreeMap<String,Integer>();
    }
    
    public boolean getCompletado() {
        return this.completado;
    }
    
    public TreeMap<String,Integer> getPodio() {
        return this.podio;
    }
    
    public synchronized void comprueba(String nombreCoche, int distRec) {
        if (!this.completado) {
            podio.put(nombreCoche, distRec);
            
            if (distRec >= distancia) {
                this.completado=true;
                System.out.println("¡Ha ganado el coche "+nombreCoche+"!");
            } else {
                double progreso = ((double)distRec/(double)distancia) * 100;
                System.out.printf("El coche %s lleva un %.2f por ciento del recorrido.\n",nombreCoche,progreso);

            }
        }
    }
}
