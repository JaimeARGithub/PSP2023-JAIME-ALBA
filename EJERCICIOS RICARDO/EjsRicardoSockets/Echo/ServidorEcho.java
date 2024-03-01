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


public class ServidorEcho {

  private final static int MAX_BYTES = 1400;
  private final static String COD_TEXTO = "UTF-8";

  public static void main(String[] args) {

    if (args.length < 1) {
      System.err.println("ERROR, indicar: puerto.");    //Pedimos como parámetro el puerto de escucha
      System.exit(1);
    }

    int numPuerto = Integer.parseInt(args[0]);

    try (DatagramSocket serverSocket = new DatagramSocket(numPuerto)) 
    {
      System.out.printf("Creado socket de datagramas para puerto %s.\n", numPuerto);

      while (true) {

        System.out.println("Esperando datagramas.");

        byte[] datosRecibidos = new byte[MAX_BYTES];   //Creamos el array de bytes para los datos recibidos
        
        DatagramPacket paqueteRecibido
                = new DatagramPacket(datosRecibidos, datosRecibidos.length);   //Creamos un datagrama vacio para recibir datos

        serverSocket.receive(paqueteRecibido);   //Estamos a la espera de recibir datagramas

        
        String lineaRecibida = new String(paqueteRecibido.getData(),
                0, paqueteRecibido.getLength(), COD_TEXTO);    //Convertimos los datos del paquete recibido a un String

        InetAddress IPCliente = paqueteRecibido.getAddress();
        int puertoCliente = paqueteRecibido.getPort();
        System.out.println("Recibido datagrama desde "+IPCliente.getHostAddress()+" puerto"+ puertoCliente+" mensaje "+lineaRecibida);

        String respuesta = "#" + lineaRecibida + "#";  //Concatemos el simbolo # para comprobar que ha hecho el eco

        byte[] b = respuesta.getBytes(COD_TEXTO);   // Convertimos la respuesta en un array de Bytes
        
        
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
