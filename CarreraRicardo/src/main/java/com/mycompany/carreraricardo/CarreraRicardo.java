/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.carreraricardo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author jaime
 */
public class CarreraRicardo {

    public static void main(String[] args) {
        // Definimos los coches que van a correr
        String[] coches = {"Opel", "Ford", "Seat"};
        int numHilos=3;
        
        
        
        Random r = new Random();
        int distancia = 100 + r.nextInt(901); // distancia a recorrer
        Carrera c = new Carrera(distancia, false); // creamos la carrera con esa distancia y sin terminar
        
        
        System.out.println(">>>>>>>>>>Carrera iniciada. Hay que recorrer "+distancia+" metros.<<<<<<<<<<");
        
        // Creamos y lanzamos los hilos
        Thread[] hilos = new Thread[numHilos];
        
        
        for (int i=0; i<numHilos; i++) {
            Coche co = new Coche(coches[i], 0, c);
            hilos[i] = new Thread(co);
            hilos[i].start();
        }
        
        
        try {
            for (Thread e:hilos) {
                e.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Excepción de tipo interrupted.");
        }
        
        
        
        // Terminado el programa principal, mostramos el podium
        // Coger el HashMap y ordenarlo descendente y por valor
        List<Map.Entry<String,Integer>> lista = new ArrayList<>(c.getProgreso().entrySet());
        lista.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        
        for (Map.Entry<String,Integer> e:lista) {
            System.out.println("El coche "+e.getKey()+" ha recorrido "+e.getValue()+" metros.");
        }
        
        
        System.out.println("Programa principal terminado.");
    }
    
    
}

class Coche implements Runnable {

    private String nombre; // nombre del hilo
    private int recorrido; // distancia que el hilo lleva recorrida
    private final Carrera Car; // recurso compartido
    
    
    
    public Coche(String nombre, int recorrido, Carrera car){
        this.nombre = nombre;
        this.recorrido = recorrido;
        this.Car = car;
    }
    
    
    @Override
    public void run() {
        int avance=0; // establecemos el nº de metros que ese coche avanza en cada iteración
        int tiempo=1000; // tiempo que cada coche espera entre avance y avance
        
        Random r = new Random();
        
        avance = 1 + r.nextInt(50); // avanzamos 1-50
        this.recorrido=this.recorrido+avance; // incrementamos la distancia recorrida en cada avance
        
        // Hago un primer avance.
        // Si tras él la carrera no ha terminado, espero un tiempo y vuelvo
        // a hacer un avance.
        this.Car.HeGanado(recorrido, nombre);
        
        while (!this.Car.getTerminado()) {
            
            try {

                Thread.sleep(tiempo);
                
            } catch (InterruptedException e) {
                System.out.println("Excepción de tipo interrupted.");
            }
            
            avance = 1 + r.nextInt(50);
            this.recorrido=this.recorrido+avance;
            this.Car.HeGanado(recorrido, nombre);
        }
        
        System.out.println("El coche "+nombre+" ha terminado.");
    }
    
}


// Clase recurso compartido
// Al crearse, calcula la distancia a recorrer
class Carrera {
    private int distancia; // Distancia a recorrer para ganar la carrera
    private boolean terminado; // Variable que indica que la carrera ha terminado
    
    // Estructura para registrar el avance de cada coche
    private HashMap <String, Integer> progreso = new HashMap<String, Integer>();
    
    // Si queremos imprimir actua
    
    
    public Carrera(int distancia, boolean terminado) {
        this.distancia=distancia;
        this.terminado=terminado;
    }
    

    // La sincronizamos para evitar inconsistencias
    public synchronized boolean getTerminado() {
        return this.terminado;
    }
    
    
    public HashMap getProgreso() {
        return this.progreso;
    }
    
    
    // Recibimos del coche la distancia recorrida y el nombre del coche
    // Comprobamos si con ese recorrido ese coche ha ganado
    public synchronized void HeGanado(int recorrido, String nombre) {
        
        // Si la carrera está sin ganar y ese coche ha recorrido la misma distancia
        // o más de la que había que conseguir
        if (!this.terminado) { // si la carrera no ha terminado
            
            
            // Si no he terminado, es porque el avance se va a realizar
            // Si he terminado, el avance no se computa
            // Tenemos que registrar el avance de cada coche en el progreso
            
            // Consulto si existe una entrada para ese coche;
            // -si existe, la recupero y guardo el avance
            // -si no, la creo y asigno a cero
            // da igual todo, me limito a poner en la clave el nombre del coche y en el valor el recorrido total
            progreso.put(nombre, recorrido);
            
            
            
            // comprobamos si con ese recorrido hemos ganado
            if (recorrido>=this.distancia) {
                System.out.println("¡¡¡¡¡¡¡¡El coche "+nombre+" ha ganado!!!!!!!!");
                this.terminado=true;
            } else {
                
                // La distancia total recorrida por el coche no basta para llegar a meta
                // Mostramos su porcentaje de avance
                double porcentaje = Math.round(((double) recorrido/this.distancia) * 100);
                System.out.println("El coche " +nombre+ " ha recorrido "+recorrido+" metros, el " +porcentaje+ "% de la distancia.");
                
            }
     
        }
    }
    
}
