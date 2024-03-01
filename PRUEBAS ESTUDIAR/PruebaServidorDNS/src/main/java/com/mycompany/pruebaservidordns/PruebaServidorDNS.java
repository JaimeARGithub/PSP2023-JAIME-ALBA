/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pruebaservidordns;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author jaime
 */
public class PruebaServidorDNS {
    
    public static final int MAX_BYTES = 1400;
    public static final String COD_TEXT = "UTF-8";

    public static void main(String[] args) {
        
        if (args.length != 1) {
            System.out.println("Error; el único argumento debe ser el puerto.");
            System.exit(0);
        }
        
        int numPuerto = Integer.parseInt(args[0]);
        HashMap<String,String> direcciones = new HashMap<String,String>();
        
        
        try (DatagramSocket socketServidor = new DatagramSocket(numPuerto)) {
            
            File f = new File("./dominios.txt");
            Scanner sc = new Scanner(f);
            String linea;
            String[] campos;
            
            while (sc.hasNext()) {
                linea = sc.nextLine();
                System.out.println(linea);
                campos = linea.split(" ");
                direcciones.put(campos[0], campos[1]);
            }
            
            System.out.println("Abierto servidor DNS en el puerto " + numPuerto + ". Esperando peticiones.");
            System.out.println("");
            
            
            while (true) {
                byte[] datosRecibir = new byte[MAX_BYTES];
                DatagramPacket paqueteRecibir = new DatagramPacket(datosRecibir, datosRecibir.length);
                socketServidor.receive(paqueteRecibir);
                // Se queda a la espera hasta que reciba algo
                
                InetAddress IPCliente = paqueteRecibir.getAddress();
                int puertoCliente = paqueteRecibir.getPort();
                String mensajeRecibir = new String(paqueteRecibir.getData(), 0, paqueteRecibir.getLength(), COD_TEXT);
                System.out.println("Recibida petición: " + mensajeRecibir + ", desde la IP " + IPCliente.getHostAddress() + " con puerto " + paqueteRecibir.getPort());
                
                String mensajeEnviar = direcciones.getOrDefault(mensajeRecibir, "ERROR: NO ENCONTRADO");
                System.out.println("Enviando respuesta: " + mensajeEnviar);
                
                byte[] datosEnviar = mensajeEnviar.getBytes(COD_TEXT);
                DatagramPacket paqueteEnviar = new DatagramPacket(datosEnviar, datosEnviar.length, IPCliente, puertoCliente);
                socketServidor.send(paqueteEnviar);
                
                
                
                System.out.println("");
                System.out.println("");
                System.out.println("Esperando peticiones.");
                System.out.println("");
            }
            
            
        } catch (SocketException se) {
            System.out.println(se);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
