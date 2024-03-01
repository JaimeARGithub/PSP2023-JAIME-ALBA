/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pruebasservidorechoconcurrente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author jaime
 */
public class PruebasServidorEchoConcurrente {

    public static final int MAX_BYTES = 1400;
    public static final String COD_TEXT = "UTF-8";
    
    public static void main(String[] args) {
        
        if (args.length != 1) {
            System.out.println("Error.");
            System.exit(0);
        }
        
        int numPuerto = Integer.parseInt(args[0]);
        
        try (DatagramSocket socketServidor = new DatagramSocket(numPuerto)) {
            
            System.out.println("Abierto servidor en el puerto " + numPuerto);
            System.out.println("Esperando peticiones.");
            System.out.println("");
            
            while (true) {
                byte[] datosRecibidos = new byte[MAX_BYTES];
                DatagramPacket paqueteRecibido = new DatagramPacket(datosRecibidos, datosRecibidos.length);
                
                socketServidor.receive(paqueteRecibido);
                
                
                InetAddress IPCliente = paqueteRecibido.getAddress();
                int puertoCliente = paqueteRecibido.getPort();
                System.out.println("Recibido paquete desde " + IPCliente.getHostAddress() + " con puerto " + puertoCliente);
                
                Thread hilo = new Thread(new ManejadorCliente(socketServidor, paqueteRecibido));
                hilo.start();
            }
            
        } catch (SocketException se) {
            System.out.println(se);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}

class ManejadorCliente implements Runnable{
    public static final int MAX_BYTES = 1400;
    public static final String COD_TEXT = "UTF-8";
    
    private DatagramSocket socket;
    private DatagramPacket paquete;
    
    public ManejadorCliente(DatagramSocket socketCliente, DatagramPacket paqueteCliente) {
        this.socket = socketCliente;
        this.paquete = paqueteCliente;
    }

    @Override
    public void run() {
        try {
            InetAddress IPCliente = paquete.getAddress();
            int puertoCliente = paquete.getPort();
            String mensajeRecibido = new String(paquete.getData(), 0, paquete.getLength(), COD_TEXT);
            
            System.out.println("Mensaje recibido: " + mensajeRecibido);
            String mensajeRespuesta = "###" + mensajeRecibido + "###";
            System.out.println("Enviando respuesta: " + mensajeRespuesta);
            
            byte[] datosEnviar = mensajeRespuesta.getBytes(COD_TEXT);
            DatagramPacket paqueteEnviar = new DatagramPacket(datosEnviar, datosEnviar.length, IPCliente, puertoCliente);
            socket.send(paqueteEnviar);
            
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
    
}
