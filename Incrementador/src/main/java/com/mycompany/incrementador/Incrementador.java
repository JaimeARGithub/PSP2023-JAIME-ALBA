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
        
        // Contador con hilos: programa principal
        // 1.- Crea una instancia del recurso compartido
        Contador c = new Contador(0); 
        
        // 2.- Nos creamos un array de hilos, para controlar qué
        // hilo se está ejecutando en cada momento
        int numeroHilos = 10;
        Thread[] hilos = new Thread[numeroHilos];
        
        for (int i=0; i<numeroHilos; i++) {
        
        // 3.- Se crean y ejecutan    
            
            // Creamos una instancia de la clase sumador; por sí sola no hace nada
            Sumador sum = new Sumador(c,i);
            
            // Creamos un hilo para ese sumador y lo guardamos en una posición
            // del array de hilos
            hilos[i] = new Thread(sum); // Ésto todavía no hace nada
            hilos[i].start();
        }
        
        
        
        // 4.- Necesitamos otro bucle que, para cada uno de los hilos, el
        // hilo principal espere a que finalicen
        
        // Con ésto, el programa principal espera a que terminen los hilos,
        // pero sigue habiendo descuadre en la suma.
        // Problema de la sección crítica
        // Podemos añadir al método un modificador para indicar que sólo puede
        // haber UN HILO a la vez ejecutando sumar. Me voy al método Sumar 
        // de la clase Contador y le meto un synchronized.
        
        // Con ello, los hilos siguen ejecutándose a su aire, pero ya no puede
        // haber dos operando a la vez con el mismo contenido de la memoria.
        // La zona de memoria se bloquea; un hilo no puede trabajar con ella
        // si ya hay otro hilo trabajando con ella
        
        // Problemas: 
        // 1.>> Sincronizar un método RALENTIZA EL FUNCIONAMIENTO. Hay
        // que saber qué métodos sincronizar y cuáles no.
        // 2.>> Si tengo varios métodos sincronizados, como un sumar y
        // restar, puede ocurrir un interbloqueo. Comprobar que el orden
        // de ejecución de hilos no te va a provocar ésto. Ej: para que un
        // hilo complete una tarea necesita acceder dos archivos; el hilo
        // 1 usa el archivo 1 y necesita el 2, y el hilo 2 usa el archivo
        // 2 y necesita el 1. El uno necesita el otro sin soltar el suyo
        // y se quedan bloqueados.
        for (int i=0; i<numeroHilos; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                System.out.println("Excepción de tipo interrupted");
            }
        }
        
        
        
        
        // Queremos que al acabar se muestre la cuenta obtenida
        // El programa principal debe mostrar el resultado
        System.out.printf("El resultado de la cuenta incrementado es %d \n", c.getCuenta());
    }
}



// Clase que implementa el hilo
class Sumador implements Runnable {
    // Este hilo necesita, como mínimo, un ID.
    // String de nombre o nº que lo identifique
    
    // Le voy a decir qué hilo es y pasarle una instancia de la clase Contador
    private int numHilo; // El identificador del hilo
    
    // ES FINAL PORQUE ES LA MISMA INSTANCIA PARA TODOS LOS HILOS.
    // TODOS LOS HILOS TIENEN EL MISMO CONTADOR
    private final Contador c; // Instancia del recurso compartido, de la clase contador
    
    public Sumador(Contador c, int num) {
        this.numHilo = num;
        this.c = c;
    }
    
    
    // El método run se va a llamar igual, pero pueden realizar distintas
    // cosas dependiendo de los parámetros que se le hayan pasado al constructor
    @Override
    // El run() cambia según el hilo.
    public void run() {
        // Es recomendable poner a cada hilo un system out que los
        // identifique cuando trabajen.
        System.out.println(this.numHilo + " iniciado.");
        for (int i=0; i<10000; i++) {
            c.Sumar(); // Se ejecuta el método incrementador 100 veces
        }
        System.out.println(this.numHilo + " terminado.");
    }
    
}



// El programa principal que inctanca la clase compartida y lanza los hilos
// Se trata del recurso compartido, la clase con la que van a tratar todos
// los hilos (un objeto de esa clase)
class Contador{
    private int cuenta;
    
    
    public Contador(int valor) {
        this.cuenta=valor;
    }
    
    
    public synchronized void Sumar() {
            this.cuenta++;
    }
    
    public int getCuenta() {
        return this.cuenta;
    }
}