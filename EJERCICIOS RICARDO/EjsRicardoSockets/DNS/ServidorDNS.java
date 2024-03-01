/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socketsjava;

import java.io.IOException;
import java.net.SocketException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;





public class ServidorDNS {

  private final static int MAX_BYTES = 1400;
  private final static String COD_TEXTO = "UTF-8";

  public static void main(String[] args) {

    
        
        if (args.length < 1) {
            System.err.println("ERROR, indicar: puerto.");    //Pedimos como parámetro el puerto de escucha 3000
            System.exit(1);
        }
        
        int numPuerto = Integer.parseInt(args[0]);
            
        try (DatagramSocket serverSocket = new DatagramSocket(numPuerto))
        {
            //Volcamos el contenido del archvio dominios.txt al un HashMap
            
             File  f = new File ("/home/joaquin/Dominios.txt");
        
             Scanner sc = new Scanner(f);
            
             HashMap<String,String> Dominios = new HashMap<String,String>();
            
             String linea;
             
             String[] campos ;
             
             while ( sc.hasNextLine() )
             {
                  linea=sc.nextLine();
                  
                  campos=linea.split(" ");
                  
                  Dominios.put(campos[0],campos[1]);
                  
             }
             
           
            System.out.printf("Creado servidor DNS en el puerto %s.\n", numPuerto);
            
            while (true) {
                
                System.out.println("Esperando peticiones DNS.");
                
                byte[] datosRecibidos = new byte[MAX_BYTES];   //Creamos el array de bytes para los datos recibidos
                
                DatagramPacket paqueteRecibido
                        = new DatagramPacket(datosRecibidos, datosRecibidos.length);   //Creamos un datagrama vacio para recibir datos
                
                serverSocket.receive(paqueteRecibido);   //Estamos a la espera de recibir datagramas
                
                String Respuesta;
                
                String nombreDominio = new String(paqueteRecibido.getData(),
                        0, paqueteRecibido.getLength(), COD_TEXTO);    //Convertimos los datos del paquete recibido a un String
                
                System.out.println("Recibido petición para el dominio "+nombreDominio);
                
                Respuesta=Dominios.getOrDefault(nombreDominio,"IP NOT FOUND");
                
                //Retornamos la respuesta al Cliente 
               
                InetAddress IPCliente = paqueteRecibido.getAddress();
                int puertoCliente = paqueteRecibido.getPort();
                
                byte[] b = Respuesta.getBytes(COD_TEXTO);   // Convertimos la respuesta en un array de Bytes
                
                DatagramPacket paqueteEnviado = new DatagramPacket(b, b.length,IPCliente, puertoCliente);
                
                serverSocket.send(paqueteEnviado);
                
            }
        } catch (SocketException ex) {
            System.out.println("Excepción de sockets");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Excepción de E/S");
            ex.printStackTrace();
        }
        
  }

}
