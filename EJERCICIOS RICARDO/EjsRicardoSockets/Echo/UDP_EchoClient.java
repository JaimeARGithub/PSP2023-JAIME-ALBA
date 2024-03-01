package UdpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDP_EchoClient {

  private final static int MAX_BYTES = 1400;          //Establecemos el tamaño máximo del array de bytes a recibir
          
  private final static String COD_TEXTO = "UTF-8";

  public static void main(String[] args) {

    if (args.length < 2) {
      System.err.println("ERROR, indicar: host_servidor puerto");    //Recibimos la IP/Nombre del host y el puerto destino
      System.exit(1);
    }

    String nomHost = args[0];                  //Recogemos el nombre/IP del host
    
    int numPuerto = Integer.parseInt(args[1]); //Recogemos el puerto

    try (DatagramSocket clientSocket = new DatagramSocket();    //Creamos el DatagramSocket
            InputStreamReader isrStdIn = new InputStreamReader(System.in, COD_TEXTO); //Y un BufferedReader a partir de un SteamReader de la entrada estandar
            BufferedReader brStdIn = new BufferedReader(isrStdIn)) {

      String lineaLeida;  //Declaramos una variable para las lineas que vamos leyendo

      System.out.println("Introducir líneas. Línea vacía para terminar.");
      System.out.print("Línea>");

      while ((lineaLeida = brStdIn.readLine()) != null      //Mientras leamos alguna liena y no sea vacia
              && lineaLeida.length() > 0) 
      {

        InetAddress IPServidor = InetAddress.getByName(nomHost);      //Creamos un obejto InetAddress para ese servidor
        
        byte[] b = lineaLeida.getBytes(COD_TEXTO);                    //Transformamos la linea leida en un array de bytes
        
        DatagramPacket paqueteEnviado = new DatagramPacket(b, b.length,IPServidor, numPuerto);
        
        clientSocket.send(paqueteEnviado);

        byte[] datosRecibidos = new byte[MAX_BYTES];   //Creamos un array de Bytes para los datos que recibamos
        
        
        DatagramPacket paqueteRecibido = new DatagramPacket(datosRecibidos, datosRecibidos.length);  //Creamos un datagram PAcket para los datos recibidos
        
        clientSocket.receive(paqueteRecibido);  //Al ejecutar este método se queda esperando(el programa) hasta que reciba un paquete
        
        
        String respuesta=new String(paqueteRecibido.getData(),0, paqueteRecibido.getLength(), COD_TEXTO); //Creamos un String con la respusta

        System.out.println("Datagrama recibido desde "+paqueteRecibido.getAddress().getHostAddress()+" puerto "+paqueteRecibido.getPort()+" mensaje "+respuesta);
        System.out.print("Línea>");

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
