package udp_server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.io.File;
import java.util.Scanner;


public class DnsServer {

  private final static int MAX_BYTES = 1400;
  private final static String COD_TEXTO = "UTF-8";

  public static void main(String[] args) {

    if (args.length < 1) {
      System.err.println("ERROR, indicar: puerto.");    //Pedimos como parámetro el puerto de escucha, 5000
      System.exit(1);
    }

    int numPuerto = Integer.parseInt(args[0]);   //Recibimos el número de puerto de escucha del servidor

    try (DatagramSocket serverSocket = new DatagramSocket(numPuerto) ) //Creamos un socket UDP para enviar y recibir datagramas
    {
      System.out.printf("Creado socket DNS server Iterativo para puerto %s.\n", numPuerto);

      //Volcamos en un HashMap el cotenido del archivo
      
       HashMap <String,String> Domin = new HashMap<String,String>();   //Creamos el hashmap para volcar el contenido del archivo Dominios.txt
     
       Scanner scanner = new Scanner(new File("/home/joaquin/Documentos/Dominios.txt"));
       
       String linea;   // Variable string para ir guardando las lineas del archivo
        
       String[] campos; // Array de string para guardar los campos de la linea
       
       while (scanner.hasNextLine() )     //Comprobamos si hay mas lineas
       {
           linea = scanner.nextLine();   //Extraemos la linea actual
           
           campos =linea.split(" ");     //PArtimos la linea en campos
           
           Domin.put(campos[0],campos[1]);   // Creamos una entrada en el HashMap (Dominio ,IP)
       
       }
       
      //Codigo de inicio del servidor      
 
      while (true) {

        System.out.println("Esperando peticiones");

        byte[] datosRecibidos = new byte[MAX_BYTES];   //Creamos el array de bytes para los datos recibidos
        
        DatagramPacket paqueteRecibido
                = new DatagramPacket(datosRecibidos, datosRecibidos.length);   //Creamos un datagrama vacio para recibir datos

        serverSocket.receive(paqueteRecibido);   //Estamos a la espera de recibir datagramas. El programa se detiene hasta que llega algun paquete

        
        String DomPedido = new String(paqueteRecibido.getData(),0, paqueteRecibido.getLength(), COD_TEXTO);    //Convertimos los datos del paquete recibido a un String

        String Respuesta="";  //Respuesta que devolverá el servidor  
        
        if (Domin.containsKey(DomPedido) )    //Si el Dominio Pedido está en el HashMap
        {
            Respuesta=Domin.get(DomPedido);     //Obtenemos el valor(Ip) para el Dominio solicitado
        }
        else   //Ese dominio no estaba en el archivo
        {
            Respuesta="NO ENCONTRADO";   //Indicamos que no hemos encontrado la IP 
        }    
        
     
        byte[] b = Respuesta.getBytes(COD_TEXTO);   // Convertimos la respuesta en un array de Bytes
        
        InetAddress  IPCliente=paqueteRecibido.getAddress();    //Cogemos la Ip del remitente del datagrama recibido
        
        int puertoCliente =paqueteRecibido.getPort();    // Y el puerto
        
        DatagramPacket paqueteEnviado = new DatagramPacket(b, b.length,IPCliente, puertoCliente);  //Construimos el paquete con la rspuesta
        
        serverSocket.send(paqueteEnviado);   //Lo enviamos

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
