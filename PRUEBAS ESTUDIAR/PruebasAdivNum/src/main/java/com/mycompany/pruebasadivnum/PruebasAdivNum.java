/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pruebasadivnum;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author jaime
 */
public class PruebasAdivNum {

    public static void main(String[] args) {
        Random r = new Random();
        int numSecreto = 1 + r.nextInt(50);
        Numero n = new Numero(numSecreto);
        System.out.println("¡El número secreto es el " + numSecreto + "!");
        
        Thread[] hilos = new Thread[5];
        
        for (int i=0; i<5; i++) {
            Adivinador adiv = new Adivinador("Adivinador "+(i+1), n);
            hilos[i] = new Thread(adiv);
            hilos[i].start();
        }
        
        for (int i=0; i<5; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
    
    
}

    class Adivinador implements Runnable {
        private String nombreAdiv;
        private final Numero num;
        
        public Adivinador(String nombre, Numero n) {
            this.nombreAdiv = nombre;
            this.num = n;
        }

        @Override
        public void run() {
            Random r = new Random();
            int tiempo = 1000;
            int numPrueba;
            
            while (!num.getAdivinado()) {
                numPrueba = 1 + r.nextInt(50);

                num.comprueba(nombreAdiv, numPrueba);
                
                try {
                    Thread.sleep(tiempo);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
        
    }
    
    class Numero {
        private int numero;
        private boolean adivinado;
        private ArrayList<Integer> mencionados;
        
        public Numero(int num) {
            this.numero=num;
            this.adivinado=false;
            this.mencionados = new ArrayList<Integer>();
        }
        
        public boolean getAdivinado() {
            return this.adivinado;
        }
        
        public synchronized void comprueba(String nombreAdiv, int numPrueba) {
            if (!adivinado && !mencionados.contains(numPrueba)) {
                System.out.println("El hilo " + nombreAdiv + " lo va a intentar con el número " + numPrueba);
                
                if (numPrueba == this.numero) {
                    System.out.println("¡El adivinador " + nombreAdiv + " ha adivinado el número!");
                    this.adivinado = true;
                } else {
                    System.out.println("El adivinador " + nombreAdiv + " ha fallado.");
                    this.mencionados.add(numPrueba);
                }
            }
        }
    }
