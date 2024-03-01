/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pruebaservidorecho;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author jaime
 */
public class PruebaServidorEcho {
    
    public static final int MAX_BYTES = 1400;
    public static final String COD_TEXT = "UTF-8";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Error. El parámetro debe ser el puerto de escucha.");
            System.exit(0);
        }
        
        int numPuerto = Integer.parseInt(args[0]);
        
        try (DatagramSocket socketServidor = new DatagramSocket(numPuerto)) {
            
            System.out.println("Abierto servidor a la escucha en el puerto " + numPuerto);
            System.out.println("Esperando peticiones.");
            System.out.println("");
            System.out.println("");
            
            while (true) {
                byte[] datosRecibir = new byte[MAX_BYTES];
                DatagramPacket paqueteRecibir = new DatagramPacket(datosRecibir, datosRecibir.length);
                socketServidor.receive(paqueteRecibir);
                
                InetAddress IPCliente = paqueteRecibir.getAddress();
                int puertoCliente = paqueteRecibir.getPort();
                System.out.println("Mensaje recibido desde " + IPCliente.getHostAddress() + ", con puerto " + puertoCliente);
                String mensajeRecibir = new String(paqueteRecibir.getData(), 0, paqueteRecibir.getLength(), COD_TEXT);
                System.out.println("Mensaje recibido: " + mensajeRecibir);
                System.out.println("");
                
                String respuesta = "###" + mensajeRecibir + "###";
                System.out.println("Redactando y enviando respuesta: " + respuesta);
                byte[] datosEnviar = respuesta.getBytes(COD_TEXT);
                DatagramPacket paqueteEnviar = new DatagramPacket(datosEnviar, datosEnviar.length, IPCliente, puertoCliente);
                socketServidor.send(paqueteEnviar);
                System.out.println("");
                System.out.println("");
                System.out.println("Esperando peticiones.");
            }
            
        } catch (SocketException se) {
            System.out.println(se);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
