/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pruebasconstructora;

/**
 *
 * @author jaime
 */
public class PruebasConstructora {

    public static void main(String[] args) {
        int cantLadrillos = 6000;
        Almacen a = new Almacen(cantLadrillos);
        System.out.println("Se ha creado un almacén con " + cantLadrillos + " de capacidad máxima.");
        System.out.println("");
        
        Thread[] hilos = new Thread[3];
        hilos[0] = new Thread(new Fabrica("FABRICA", a, 13500));
        hilos[1] = new Thread(new Obra("OBRA1", a, 200, 4000));
        hilos[2] = new Thread(new Obra("OBRA2", a, 400, 2000));
        
        for (int i=0; i<3; i++) {
            hilos[i].start();
        }
        
        for (int i=0; i<3; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException E) {
                System.out.println(E);
            }
        }
    }
}



class Almacen {
    private int maxLadrillos;
    private int ladrillos;
    
    public Almacen(int maxLad) {
        this.maxLadrillos = maxLad;
        this.ladrillos = 0;
    }
    
    public synchronized void meterLadrillos(String id, int cantidad) {
        System.out.println("La fábrica " + id + " va a intentar meter " + cantidad + " ladrillos.");
        
        if ((this.ladrillos + cantidad) <= maxLadrillos) {
            System.out.println("Almacenamiento de ladrillos exitoso.");
            ladrillos += cantidad;
        } else {
            System.out.println("NO se han podido almacenar los ladrillos.");
        }
        
        System.out.println("En el almacén hay un total de " + ladrillos + " ladrillos.");
    }
    
    public synchronized void sacarLadrillos(String id, int cantidad) {
        System.out.println("La obra " + id + " va a intentar sacar " + cantidad + " ladrillos.");
        
        if (cantidad <= this.ladrillos) {
            System.out.println("La obra " + id + " ha podido sacar ladrillos.");
            ladrillos -= cantidad;
        } else {
            System.out.println("La obra " + id + " NO ha podido sacar ladrillos.");
        }
        
        System.out.println("En el almacén hay un total de " + ladrillos + " ladrillos.");
    }
}



class Fabrica implements Runnable {
    private String identificador;
    private final Almacen a;
    private int ladrillosCreados;
    private int ladrillosMax;
    
    
    public Fabrica(String id, Almacen alm, int maxLadrillos) {
        this.identificador = id;
        this.a = alm;
        
        ladrillosCreados = 0;
        ladrillosMax = maxLadrillos;
    }
    
    
    @Override
    public void run() {
        System.out.println("La fábrica " + identificador + " empieza a funcionar.");
        
        while (ladrillosCreados < ladrillosMax) {
            
            a.meterLadrillos(identificador, 450);
            ladrillosCreados += 450;
            System.out.println("La fábrica lleva creados " + ladrillosCreados + " ladrillos.");
            
            try {
                Thread.sleep(3000);
            } catch (InterruptedException E) {
                System.out.println(E);
            }
            
        }
        
        System.out.println("La fábrica " + identificador + " ha terminado.");
    }
}

class Obra implements Runnable {
    private String identificador;
    private final Almacen a;
    private int ladrillosGasta;
    private int tiempoEspera;
    private int tiempoTotal;
    
    
    public Obra(String id, Almacen alm, int cantLadrillos, int esperas) {
        this.identificador = id;
        this.a = alm;
        
        this.ladrillosGasta = cantLadrillos;
        this.tiempoEspera = esperas;
        
        this.tiempoTotal = 0;
    }

    
    @Override
    public void run() {
        System.out.println("La obra " + identificador + " ha empezado a trabajar.");
        
        while (tiempoTotal < 120000) {
            a.sacarLadrillos(identificador, ladrillosGasta);
            
            try {
                Thread.sleep(tiempoEspera);
            } catch (InterruptedException E) {
                System.out.println(E);
            }
            
            tiempoTotal += tiempoEspera;
        }
        
        System.out.println("La obra " + identificador + " ha terminado.");
    }
}
