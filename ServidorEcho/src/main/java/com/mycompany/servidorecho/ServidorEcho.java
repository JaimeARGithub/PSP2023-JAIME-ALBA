/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.servidorecho;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author jaime
 */
public class ServidorEcho {

    private final static int MAX_BYTES = 1400;
    private final static String COD_TEXTO="UTF-8";
    
    
    public static void main(String[] args) {
        
        // parámetros: solamente el puerto, 5000
        if (args.length < 1) {
            System.out.println("ERROR, indicar: puerto.");
            System.exit(1);
        }
        
        
        int numPuerto = Integer.parseInt(args[0]);
        
        
        try (DatagramSocket serverSocket = new DatagramSocket(numPuerto)) {
            
            System.out.printf("Creado socket de datagramas para puerto %s.\n", numPuerto);
            
            while (true) {
                System.out.println("Esperando datagramas.");
                
                // array de bytes para el datagrama que va a recibir el paquete
                byte[] datosRecibidos = new byte[MAX_BYTES];
                
                // el cliente enviaba y recibía
                // el servidor hace lo contrario: recibe y envía
                DatagramPacket paqueteRecibido = new DatagramPacket(datosRecibidos, datosRecibidos.length);
                serverSocket.receive(paqueteRecibido);
                
                // tras recibir, se contesta
                String lineaRecibida = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength(), COD_TEXTO);
                
                InetAddress IPCliente = paqueteRecibido.getAddress();
                int puertoCliente = paqueteRecibido.getPort();
                
                System.out.println("Recibido datagrama desde " + IPCliente.getHostAddress() + " puerto " + puertoCliente + " mensaje " + lineaRecibida);
                
                String respuesta = "#" + lineaRecibida + "#";
                
                byte[] b = respuesta.getBytes(COD_TEXTO);
                
                DatagramPacket paqueteEnviado = new DatagramPacket(b, b.length, IPCliente, puertoCliente);
                
                serverSocket.send(paqueteEnviado);
                
                
                // CLIENTE Y SERVIDOR PUEDEN EJECUTARSE A LA VEZ DESDE DISTINTOS PROYECTOS SIN PROBLEMAS
                
                // Para probarlo: 
                // 1.- Lanzar servidor. Parámetro: el puerto, 5000
                // 2.- Esperando datagramas.
                // 3.- Ejecutar el cliente. Parámetros: localhost 5000
                // 4.- En una pestaña está el cliente, y en la otra, el servidor. Desde el cliente mando "Hola".
                // 5.- Al instante se recibe respuesta por parte del servidor.
                // 6.- (Opcional) abro terminal, echo "Soy yo" | nc localhost -u 5000, al instante debería recibir respuesta
                // 7.- Hasta que no lo detenga, el servidor sigue usando el puerto 5000. Si con él ON entiendo hacer nc -ul 5000, mensaje de error.
                // 8.- Si lo hago al revés, excepción de socket.
            }
            
        } catch (SocketException ex) {
            System.out.println("Excepción de socket");
        } catch (IOException ioe) {
            System.out.println("Excepción IO");
        }
    }
}
