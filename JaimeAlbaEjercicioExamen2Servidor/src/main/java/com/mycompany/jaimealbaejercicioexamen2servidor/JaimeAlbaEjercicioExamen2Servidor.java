/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.jaimealbaejercicioexamen2servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

/**
 *
 * @author jaime
 */
public class JaimeAlbaEjercicioExamen2Servidor {
    
    public static final int MAX_BYTES = 1400;
    public static final String COD_TEXT = "UTF-8";

    public static void main(String[] args) {
        
        if (args.length != 1) {
            System.out.println("Error de argumentos; el único argumento debe ser el número del puerto.");
            System.exit(0);
        }
        
        int numPuerto = Integer.parseInt(args[0]);
        
        
        ArrayList<Integer> listaNumeros = new ArrayList<Integer>();
        System.out.println("Inicializada la lista de números. Tamaño: " + listaNumeros.size());
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
                System.out.println("Recibido número desde " + IPCliente.getHostAddress() + " con puerto " + puertoCliente + ", número: " + mensajeCliente);
                
                
                String mensajeRespuesta = "";
                if (listaNumeros.size() < 30) {
                    
                    listaNumeros.add(Integer.parseInt(mensajeCliente));
                    System.out.println("En el servidor quedan " + (30 - listaNumeros.size()) + " huecos.");
                    
                    
                    if (listaNumeros.size() == 30) {
                        mensajeRespuesta = mensajeRespuesta + "FIN";
                        System.out.println("Espacio en servidor lleno. Atendiendo clientes restantes.");
                    } else {
                        mensajeRespuesta = mensajeRespuesta + "MAS";
                    }
                    
                } else {
                    mensajeRespuesta = mensajeRespuesta + "FIN";
                }

                
                
                byte[] datosEnviar = mensajeRespuesta.getBytes(COD_TEXT);
                DatagramPacket paqueteEnviar = new DatagramPacket(datosEnviar, datosEnviar.length, IPCliente, puertoCliente);
                socketServidor.send(paqueteEnviar);
                
                
                if (listaNumeros.size() == 30) {
                    break;
                }
            }
            
            
            System.out.println("");
            socketServidor.close();
            System.out.println("Espacio en servidor completo. Números recibidos: ");
            for(Integer e:listaNumeros) {
                System.out.print(e+" ");
            }
            
            
        } catch (SocketException se) {
            System.out.println(se);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
