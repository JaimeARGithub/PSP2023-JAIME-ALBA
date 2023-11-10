/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejemplohilo;

/**
 *
 * @author jaime
 */
public class EjemploHilo {

    public static void main(String[] args) {
        Saludador sa1 = new Saludador("H1");
        Saludador sa2 = new Saludador("H2");
        Saludador sa3 = new Saludador("H3");
        Saludador sa4 = new Saludador("H4");
        
        // Crea los hilos, pero no los lanza
        Thread h1 = new Thread(sa1);
        Thread h2 = new Thread(sa2);
        Thread h3 = new Thread(sa3);
        Thread h4 = new Thread(sa4);
        
        h1.start(); // Iniciamos ejecución del hilo 1
        h2.start(); // Iniciamos ejecución del hilo 2
        
        // SIEMPRE hay que controlar que el programa principal NO PUEDA
        // TERMINAR hasta que hayan acabado los demás hilos.
        
        
        // Hay otros programas en los que tengo que controlar la
        // ejecución de cada hilo.
        // Controlar el orden de ejecución:
        try {
            h1.join(); // Indicamos al principal que espere a que acabe el hilo 1
            h2.join(); // Indicamos al principal que espere a que acabe el hilo 2
        } catch (InterruptedException ie) {
            System.out.println(ie);
        }
        
        System.out.println("CAMBIO DE HILOS");
        System.out.println("CAMBIO DE HILOS");
        System.out.println("CAMBIO DE HILOS");
        
        // Con los métodos .join() me aseguro de que siempre se ejecuten
        // antes de que termine la ejecución del hilo principal.
        
        // Con el .join() le está diciendo al programa principal que espere
        // a que termine ese hilo. Es una llamada al sistema.
        
        
        
        // Si me quiero asegurar completamente del orden en que
        // se ejecutan los hilos: (sincronizar hilos)
        
        h4.start();
        try {
            h4.join();
        } catch (InterruptedException ie) {
            System.out.println(ie);
        }
        
        h3.start();
        try {
            h3.join();
        } catch (InterruptedException ie) {
            System.out.println(ie);
        }
        
        
        System.out.println("Hilo principal terminado.");
        // Normalmente va a haber tres clases: el main,
        // la del hilo y el recurso compartido.
    }
}

class Saludador implements Runnable {
    private final String nombre;
    
    
    Saludador(String nombre) {
        this.nombre = nombre;
    }
    
    
    @Override
    public void run() {
        System.out.printf("Hola, soy el hilo %s \n", this.nombre);
        System.out.printf("Hilo %s terminado. \n", this.nombre);
    }
}
