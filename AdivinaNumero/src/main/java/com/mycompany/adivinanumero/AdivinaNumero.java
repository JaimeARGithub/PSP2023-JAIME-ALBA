/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.adivinanumero;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author jaime
 */
public class AdivinaNumero {

    /*Enunciado: crear un programa que lance 10 hilos para intentar adivinar 
    un número. El programa principal elegirá un número al azar comprendido entre 
    el 1 y el 50. Cada hilo realizará un intento de adivinación. Si este intento 
    es correcto, mostrará "El hilo X ha acertado" y terminará todos los hilos. 
    El programa principal mostrará al final el hilo ganador y el número que ha acertado.

    Situación:
    -Lanzar 10 hilos
    -java.util.Random
    -nº entre 1 y 50. Hilo busca número.
    -Si acierta, termina. Si el hilo falla, el hilo espera entre 1 y 3 segundos 
    antes de poder hacer otro intento.
    -Programa principal con random elige un número y lo muestro. Los hilos no lo
    ven. El nº va a estar como recurso compartido; los hilos sólo pueden preguntar 
    si ese es el número.
    -Usar el random para elegir el número y para los tiempos de espera. El random 
    que usa el main para elegir el número es el mismo que hay que meter en el método 
    run. 
    -QUE LOS HILOS NO REPITAN NÚMEROS. En la clase compartida no solamente debe 
    estar el número aleatorio; también aquellos que no son, los hilos que se han 
    dicho (ArrayList)
    */
    public static void main(String[] args) {
        Random random = new Random();
        int numeroAleatorio = random.nextInt(50 - 1 + 1) + 1;
        System.out.println("El número aleatorio elegido es: "+numeroAleatorio);
        
        Numero num = new Numero(numeroAleatorio);
        
        int numHilos = 10;
        Thread[] hilos = new Thread[numHilos];
        
        for (int i=0; i<numHilos; i++) {
            Adivinador adiv = new Adivinador(num, i+1);
            hilos[i] = new Thread(adiv);
            hilos[i].start();
        }
        
        for (int i=0; i<numHilos; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                System.out.println("Excepción de tipo interrupted");
            }
        }
        
        System.out.println("Programa principal terminado.");
    }
}

class Adivinador implements Runnable {
    // Atributos: el recurso compartido y un identificador
    private final Numero n;
    private int numHilo;
        
        
    // En el constructor se inicializan ambos
    public Adivinador(Numero num, int id) {
        this.n = num;
        this.numHilo = id;
    }
        
        
    // Sobreescribir el método .run()
    @Override
    public void run() {
        Random random = new Random();
        int numProbar;
        int numSegundos;
            
        System.out.println("Hilo número " + this.numHilo + " iniciado.");
            
        while (!n.getAdivinado()) {
            numProbar = random.nextInt(50 - 1 + 1) + 1;
            while (n.getAparecidos().contains(numProbar)) {
                numProbar = random.nextInt(50 - 1 + 1) + 1;
            }
            System.out.println("El hilo número "+this.numHilo+" lo va a intentar con el número "+numProbar);
                
            if (numProbar == n.getNumero()) {
                n.setAdivinado();
                System.out.println("¡Conseguido! ¡El hilo número " +this.numHilo+ " ha adivinado el número!");
            } else {
                System.out.println("¡El hilo "+this.numHilo+" ha fallado!");
                n.añadeNumero(numProbar);
                numSegundos = random.nextInt(3 - 1 + 1) + 1;
                try {
                    Thread.sleep(numSegundos*1000);
                } catch (InterruptedException IE) {
                    System.out.println(IE);
                }
            }
        }
            
        System.out.println("Hilo número " + this.numHilo + " terminado.");
    }
}
    
    
class Numero {
    // Atributos: el número aleatorio, un ArrayList con los que ya han aparecido
    // y un interruptor que indica si ya se ha adivinado o no el número
    private int numero;
    private ArrayList<Integer> aparecidos;
    private boolean adivinado;
        
    // Constructor
    public Numero(int num) {
        this.numero=num;
        this.aparecidos = new ArrayList<Integer>();
        this.adivinado=false;
    }
        
    // Métodos: 
    // -Uno para acceder al valor del número (getter)
    // -Un getter para ver si el número se ha adivinado, y un setter para
    // marcarlo como adivinado cuando haga falta; el setter lo sincronizo 
    // por si varios hilos intentan usarlo a la vez
    // -Uno que añada al ArrayList números no coincidentes (varios hilos 
    // van a intentar acceder a él a la vez, meterle el synchronized), y otro
    // que devuelva todo el ArrayList, para poder acceder a los números que
    // ya han aparecido
    public int getNumero() {
       return this.numero;
    }
    // ---------------------------------------------------------------------
        
    public boolean getAdivinado() {
        return this.adivinado;
    }
        
    public synchronized void setAdivinado() {
        this.adivinado=true;
    }
    // ---------------------------------------------------------------------
        
    public ArrayList<Integer> getAparecidos() {
        return this.aparecidos;
    }
        
    public synchronized void añadeNumero(int num) {
        aparecidos.add(num);
    }
}
