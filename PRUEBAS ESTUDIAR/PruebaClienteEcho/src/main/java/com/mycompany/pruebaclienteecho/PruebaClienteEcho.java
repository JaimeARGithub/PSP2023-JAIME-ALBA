/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pruebaclienteecho;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author jaime
 */
public class PruebaClienteEcho {
    
    public static final int MAX_BYTES = 1400;
    public static final String COD_TEXT = "UTF-8";

    public static void main(String[] args) {
        
        if (args.length != 2) {
            System.out.println("Error; por parámetros deben venir el nombre del servidor y el puerto de destino");
            System.exit(0);
        }
        
        String nomHost = args[0];
        //int numPuerto = Integer.parseInt(args[1]);
        int numPuerto = 3333;
        String linea;
        
        
        try (DatagramSocket socketCliente = new DatagramSocket();
                InputStreamReader isr = new InputStreamReader(System.in, COD_TEXT);
                BufferedReader br = new BufferedReader(isr);) {
            
            
            System.out.println("Abierto cliente en el puerto " + socketCliente.getLocalPort());
            System.out.println("");
            System.out.println("Escriba líneas para recibir datos. Línea vacía para terminar.");
            System.out.print("Línea > ");
            
            while ((linea = br.readLine())!=null && !linea.isBlank()) {
                // Composición del datagrama: datos en formato array de bytes, su longitud, a qué dominio y a qué puerto
                byte[] datosEnviar = linea.getBytes(COD_TEXT);
                InetAddress IPServidor = InetAddress.getByName(nomHost);
                DatagramPacket paqueteEnviar = new DatagramPacket(datosEnviar, datosEnviar.length, IPServidor, numPuerto);
                socketCliente.send(paqueteEnviar);
                
                
                
                // Tras el envío, composición de la recepción: le digo qué va a recibir y lo pongo a esperar
                byte[] datosRecibir = new byte[MAX_BYTES];
                DatagramPacket paqueteRecibir = new DatagramPacket(datosRecibir, datosRecibir.length);
                socketCliente.receive(paqueteRecibir);
                
                System.out.println("");
                System.out.println("Recibido paquete de datos desde " + paqueteRecibir.getAddress().getHostAddress() + ", desde el puerto " + paqueteRecibir.getPort());
                String mensaje = new String(paqueteRecibir.getData(), 0, paqueteRecibir.getLength(), COD_TEXT);
                System.out.println("Mensaje: " + mensaje);
                System.out.println("");
                System.out.println("Línea > ");
            }
            
            
            
        } catch (SocketException se) {
            System.out.println(se);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
