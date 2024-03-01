/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pruebaservidoradivinaconcur;

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
public class PruebaServidorAdivinaConcur {
    
    public static final int MAX_BYTES = 1400;
    public static final String COD_TEXT = "UTF-8";

    public static void main(String[] args) {
        
        if (args.length != 1) {
            System.out.println("Error de argumentos.");
            System.exit(0);
        }
        
        int numPuerto = Integer.parseInt(args[0]);
        
        Random r = new Random();
        int numSecreto = r.nextInt(0,21);
        boolean adivinado = false;
        System.out.println("El número secreto elegido es " + numSecreto);
        System.out.println("");
        
        try (DatagramSocket socketServidor = new DatagramSocket(numPuerto)) {
            System.out.println("Servidor abierto a la escucha del puerto " + numPuerto);
            System.out.println("Esperando peticiones");
            System.out.println("");
            System.out.println("");
            
            
            while (true) {
                byte[] datosRecibir = new byte[MAX_BYTES];
                DatagramPacket paqueteRecibir = new DatagramPacket(datosRecibir, datosRecibir.length);
                socketServidor.receive(paqueteRecibir);
                
                
                InetAddress IPCliente = paqueteRecibir.getAddress();
                int puertoCliente = paqueteRecibir.getPort();
                String mensajeCliente = new String(paqueteRecibir.getData(), 0, paqueteRecibir.getLength(), COD_TEXT);
                System.out.println("Recibido intento desde " + IPCliente.getHostAddress() + " con puerto " + puertoCliente + ": " + mensajeCliente);
                
                
                String mensajeRespuesta = "";
                if (!adivinado) {
                    
                    if (Integer.parseInt(mensajeCliente) == numSecreto) {
                        mensajeRespuesta = mensajeRespuesta + "ACERTADO";
                        adivinado = true;
                    } else {
                        mensajeRespuesta = mensajeRespuesta + "FALLADO";
                    }
                    
                } else {
                    mensajeRespuesta = mensajeRespuesta + "FIN";
                }
                
                
                byte[] datosEnviar = mensajeRespuesta.getBytes(COD_TEXT);
                DatagramPacket paqueteEnviar = new DatagramPacket(datosEnviar, datosEnviar.length, IPCliente, puertoCliente);
                socketServidor.send(paqueteEnviar);
            }
            
            
        } catch (SocketException se) {
            System.out.println(se);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
