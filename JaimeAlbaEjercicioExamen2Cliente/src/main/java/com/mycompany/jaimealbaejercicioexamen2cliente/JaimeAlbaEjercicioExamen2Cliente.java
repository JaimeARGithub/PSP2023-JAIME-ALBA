/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.jaimealbaejercicioexamen2cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

/**
 *
 * @author jaime
 */
public class JaimeAlbaEjercicioExamen2Cliente {
    
    public static final int MAX_BYTES = 1400;
    public static final String COD_TEXT = "UTF-8";

    public static void main(String[] args) {
        
        if (args.length != 2) {
            System.out.println("Error de argumentos; hay que pasar el nombre del host y el puerto");
            System.exit(0);
        }
        
        String nomHost = args[0];
        int numPuerto = Integer.parseInt(args[1]);

        
        Thread[] hilos = new Thread[3];
        for (int i=0; i<3; i++) {
            hilos[i] = new Thread(new Lanzanumeros("Hilo_"+(i+1), nomHost, numPuerto));
            hilos[i].start();
        }
        
        for (int i=0; i<3; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
        }
    }
}


class Lanzanumeros implements Runnable {
    private static final int MAX_BYTES = 1400;
    private static final String COD_TEXT = "UTF-8";
    private static String ESTADO="";
    
    private String identificador;
    private String nombreServidor;
    private int numPuerto;
    
    public Lanzanumeros(String id, String nomServ, int numPuer) {
        this.identificador = id;
        this.nombreServidor = nomServ;
        this.numPuerto = numPuer;
    }
    
    
    @Override
    public void run() {
        try (DatagramSocket socketCliente = new DatagramSocket()) {
            System.out.println("Socket para el lanzanúmeros " + this.identificador + " abierto en el puerto " + socketCliente.getLocalPort());
            
            while (!ESTADO.equals("FIN")) {
                
                Random r = new Random();
                int num = r.nextInt(0, 11); // Genera un aleatorio entre el límite inferior incluyendo y el límite superior excluyendo; entre 0 y 10
                System.out.println("Lanzanúmeros " + this.identificador + " envía el número " + num);
                String mensajeEnviar = String.valueOf(num);
                
                
                InetAddress IPServidor = InetAddress.getByName(this.nombreServidor);
                byte[] datosEnviar = mensajeEnviar.getBytes(COD_TEXT);
                DatagramPacket paqueteEnviar = new DatagramPacket(datosEnviar, datosEnviar.length, IPServidor, numPuerto);
                socketCliente.send(paqueteEnviar);
                
                
                
                byte[] datosRecibir = new byte[MAX_BYTES];
                DatagramPacket paqueteRecibir = new DatagramPacket(datosRecibir, datosRecibir.length);
                socketCliente.receive(paqueteRecibir);
                
                String mensajeRecibir = new String(paqueteRecibir.getData(), 0, paqueteRecibir.getLength(), COD_TEXT);
                System.out.println("Lanzanúmeros " + this.identificador + " --- Mensaje recibido desde " + paqueteRecibir.getAddress().getHostAddress() + " con puerto " + paqueteRecibir.getPort() + ": " + mensajeRecibir);
                
                if (mensajeRecibir.equals("FIN")) {
                    this.ESTADO = "FIN";
                    System.out.println("ESPACIO EN SERVIDOR LLENO. TERMINANDO HILOS.");
                    //break;
                }
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    System.out.println(ie);
                }
            }
            
            socketCliente.close();
            System.out.println("Lanzanúmeros " + this.identificador + " terminado.");
            
        } catch (SocketException se) {
            System.out.println(se);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
    
}
