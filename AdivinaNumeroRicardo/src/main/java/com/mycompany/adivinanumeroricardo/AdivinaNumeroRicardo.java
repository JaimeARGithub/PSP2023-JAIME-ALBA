/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.adivinanumeroricardo;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author jaime
 */
public class AdivinaNumeroRicardo {

    public static void main(String[] args) {
        
        int numeroHilos = 5;
        
        Random r = new Random();
        
        Numero num = new Numero(1 + r.nextInt(19));
        
        System.out.println("El número elegido ha sido: " + num.getNumero());
        
        Thread[] hilos = new Thread[numeroHilos];
        
        for (int i=0; i<numeroHilos; i++) {
        
            Adivinador adi = new Adivinador(num, "Hilo"+i);
            
            hilos[i] = new Thread(adi); 
            hilos[i].start();
        }
        for (int i=0; i<numeroHilos; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                System.out.println("Excepción de tipo interrupted");
            }
        }
       
        System.out.println("Se acabó");
    }
}


class Adivinador implements Runnable {
    private String nomHilo; 
    private final Numero n; // Instancia del recurso compartido, de la clase contador
    
    public Adivinador(Numero n, String nombre) {
        this.nomHilo = nombre;
        this.n = n;
    }
    
    @Override
    public void run() {
        Random r = new Random();
        
        int tiempo = 1000 + r.nextInt(4000);
        int num = 1 + r.nextInt(19);
        
        while(n.isAcertado()==false){
            
            try{
                Thread.sleep(tiempo);
            }catch(InterruptedException ex){
                System.out.println("no");
            }
            
            n.Intenta(num, this.nomHilo);
            
            tiempo = 1000 + r.nextInt(4000);
            num = 1 + r.nextInt(19);
            
        }
                
        System.out.println(this.nomHilo + " terminado.");
    }
    
}

//El numero a adivinar y los metoso de acceso
class Numero{
    private int num;
    private boolean acertado;
    
    private ArrayList<Integer> numeros = new ArrayList<Integer>();
    
    public Numero(int valor) {
        this.num=valor;
        this.acertado=false;
    }
    
    public synchronized void Intenta(int num, String nombre){
        if(this.isAcertado()==false && (!numeros.contains(num))){
            System.out.println("El hilo "+nombre+" dice el "+num);
            if(this.getNumero()==num){
                System.out.println("El hilo "+nombre+" ha acertado. EL número era el "+num);
                this.setAcertado();
            }else{
                System.out.println("El hilo "+nombre+" no ha acertado.");
                numeros.add(num);
            }
        }
    }

    public boolean isAcertado() {
        return acertado;
    }

    public synchronized void setAcertado() {
        this.acertado = true;
    }
    
    public int getNumero() {
        return this.num;
    }
    
    
    /**
     * Enunciado del ejercicio nuevo: programa que simule una encuesta. Crear hilos que
     * van a ser encuestadores. Cada hilo irá a una zona y le preguntará a gente, las
     * respuestas pueden ser 1, 2, 3... cada encuestador le va a preguntar a un número de
     * personas.
     * 
     * Hay que registrar, en cada zona, qué respuestas han dado. Crear hashmaps: cada encuestador
     * tiene que preguntar a entre 100 y 300 personas; contabilizar el número de respuestas de
     * cada opción Y el número de respuestas obtenidas en cada zona.
     * 
     * Guardar, en cada zona: respuestas por zonas. En la zona 1 han respondido 110 personas, en
     * la zona 2 230, en la zona 3 150, en la zona 4 160... (al azar) Guardar cuánta gente ha respondido en
     * cada zona y, en cada zona, qué respuesta se ha dado a cada pregunta. Decir cuánta
     * gente ha respondido en cada zona, y en cada zona, cuántas respuestas de cada tipo se han dado;
     * aleatoriamente pueden responder de la respuesta 1 a la respuesta 10.
     * 
     * Lanzo hilos para las personas, una vez sepa la cantidad, la respuesta va a ser aleatoria entre
     * 1 y 10.
     * Mostrar resultado: en la zona 1 han respondido X personas, en la zona 2 han respondido Y personas,
     * hasta que no me contesten 100 no me voy de la zona, cuando me contestan 300 me voy sí o sí.
     * Mostrar el número total de cada opción de respuesta elegida y el número de respuestas por zona.
     */
}
