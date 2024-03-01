/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socketsjava;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.SocketException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class ClienteEcho {

  private final static int MAX_BYTES = 1400;
  private final static String COD_TEXTO = "UTF-8";

  public static void main(String[] args) {

    
    String nomHost = "localhost";
    
    int numPuerto = 4444;

    try (DatagramSocket clientSocket = new DatagramSocket();  //Creamos el "paquete" o sobre en el que enviar los datos
            InputStreamReader isrStdIn = new InputStreamReader(System.in, COD_TEXTO);
            BufferedReader brStdIn = new BufferedReader(isrStdIn)) 
    {

      String lineaLeida;
      
      System.out.println("Socket cliente conectado desde el puerto: "+clientSocket.getLocalPort());
     
      System.out.println("Introducir líneas. Línea vacía para terminar.");
      System.out.print("Línea>");

      while ((lineaLeida = brStdIn.readLine()) != null && lineaLeida.length() > 0)   //Mientras haya introducido una linea
      {

        InetAddress IPServidor = InetAddress.getByName(nomHost); //Especificamos la dirección de destino en ese objeto inet address
        
        byte[] b = lineaLeida.getBytes(COD_TEXTO);  //Hay que convertir el strin en array de bytes
        
        DatagramPacket paqueteEnviado = new DatagramPacket(b, b.length,IPServidor, numPuerto); //Creamo el datagrama para ese destinatario

        clientSocket.send(paqueteEnviado);  //Enviamos ese datagrama al destinatario

        byte[] datosRecibidos = new byte[MAX_BYTES];  //Creamos el array de bytes para recibir la respuesta
       
        DatagramPacket paqueteRecibido
                = new DatagramPacket(datosRecibidos, datosRecibidos.length); //Creamos un datagrama para procesar la respuesta
        
        clientSocket.receive(paqueteRecibido);  //El programa espera a que llegue un datagrama
        
        String respuesta = new String(paqueteRecibido.getData(),0, paqueteRecibido.getLength(), COD_TEXTO);  //Convertimos el array de bytes del mensaje de respuesta a un String

        System.out.println("Datagrama recibido de la dirección"+ paqueteRecibido.getAddress().getHostAddress() + "puerto" +paqueteRecibido.getPort() );
                
        System.out.println("El mensaje recibido es:"+respuesta);
        
        System.out.print("Línea>");

      }

    } catch (SocketException ex) 
    {
      System.out.println("Excepción de sockets");
      ex.printStackTrace();
    } catch (IOException ex) 
    {
      System.out.println("Excepción de E/S");
      ex.printStackTrace();
    }
  }
}

