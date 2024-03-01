/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pruebabanco;

/**
 *
 * @author jaime
 */
public class PruebaBanco {

    public static void main(String[] args) {
        Banco b = new Banco();
        
        Thread[] hilos = new Thread[2];
        hilos[0] = new Thread(new Pagador(b));
        hilos[1] = new Thread(new Cobrador(b));
        
        for (int i=0; i<2; i++) {
            hilos[i].start();
        }
        
        for (int i=0; i<2; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException E) {
                System.out.println(E);
            }
        }
    }
}

class Pagador implements Runnable {
    private String identificador;
    private int tiempoTotal;
    private int tiempoEspera;
    private final Banco b;
    
    public Pagador(Banco banco) {
        this.identificador = "Pagador";
        this.tiempoTotal = 0;
        tiempoEspera = 2000;
        this.b = banco;
    }
    
    @Override
    public void run() {
        while (tiempoTotal < 90000) {
            
            b.ingresar(identificador, 500);
            
            try {
                Thread.sleep(tiempoEspera);
            } catch (InterruptedException E) {
                System.out.println(E);
            }
            
            tiempoTotal += tiempoEspera;
            System.out.println("El pagador" + identificador + " lleva esperados " + (tiempoTotal/1000) + " segundos.");
        }
        
        System.out.println("El pagador " + identificador + " se ha ido del banco.");
    }
}

class Cobrador implements Runnable {
    private String identificador;
    private int sumaTotal;
    private int sumaRetiro;
    private final Banco b;
    
    public Cobrador(Banco banco) {
        this.identificador = "Cobrador";
        this.sumaTotal = 0;
        this.sumaRetiro = 300;
        this.b = banco;
    }
    
    @Override
    public void run() {
        while (sumaTotal < 6000) {
            
            sumaTotal += b.retirar(identificador, sumaRetiro);
            
            try {
                Thread.sleep(3000);
            } catch (InterruptedException E) {
                System.out.println(E);
            }
        }
        
        System.out.println("El cobrador " + identificador + " se ha ido del banco.");
    }
}

class Banco {
    private int dinero;
    
    public Banco() {
        this.dinero = 0;
    }
    
    public synchronized void ingresar(String agente, int ingreso) {
        System.out.println("El agente " + agente + " va a intentar ingresar " + ingreso + "€");
        
        if ((this.dinero + ingreso) <= 5000) {
            System.out.println("Ingresado con éxito");
            this.dinero += ingreso;
            
        } else {
            System.out.println("No se ha podido hacer el ingreso");
        }
        
        System.out.println("En el banco hay " + this.dinero + " euros.");
    }
    
    public synchronized int retirar(String agente, int retiro) {
        System.out.println("El agente " + agente + " va a intentar retirar " + retiro + "€");
        int cantidad=0;
        
        if (this.dinero >= 300) {
            this.dinero -= retiro;
            System.out.println("Retirado con éxito");
            cantidad=retiro;
        } else {
            System.out.println("No se ha podido hacer el retiro");
        }
        
        System.out.println("En el banco hay " + this.dinero + " euros.");
        return cantidad;
    }
}
