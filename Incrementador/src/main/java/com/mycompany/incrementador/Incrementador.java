/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.incrementador;

/**
 *
 * @author jaime
 */
public class Incrementador {

    public static void main(String[] args) {
        
        // Con esta traza de código hacemos un contador normalito.
        /*
        Contador C = new Contador(0);
        
        for(int i=0; i<100000; i++) {
            C.incrementa();
        }
        System.out.println("El resultado final es "+C.getCuenta());
        
        System.out.println("-------------------------");
        System.out.println("Cambio de hilos.");
        System.out.println("-------------------------");
        */
        
        // Contador con hilos:
        int valor = 0;
        Contador c = new Contador(valor);
        
        Thread h1 = new Thread(c);
        Thread h2 = new Thread(c);
        Thread h3 = new Thread(c);
        Thread h4 = new Thread(c);
        
        h1.start();
        h2.start();
        h3.start();
        h4.start();
        
    }
}




class Contador implements Runnable {
    private int cuenta;
    
    
    public Contador(int valor) {
        this.cuenta=valor;
    }
    
    
    public void incrementa() {
        for (int i = 0; i<50000; i++) {
            this.cuenta++;
        }
    }
    
    public int getCuenta() {
        return this.cuenta;
    }

    @Override
    public void run() {
        incrementa();
        System.out.printf("Hilo %d: El resultado final es %d\n", Thread.currentThread().getId(), getCuenta());
    }
}
