/*
 
En este código, se crea un nuevo hilo para manejar cada solicitud de cliente. 
La tarea que se ejecuta en el hilo es prácticamente la misma que en la versión iterativa, 
pero ahora está encapsulada dentro de un objeto Runnable que se pasa al constructor del objeto Thread. 
Este hilo se inicia utilizando el método start().


 */
package udp_server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DNsServerConcurrente 
{
    private static final String DNS_FILE = "/home/joaquin/Documentos/Dominios.txt";
    private static final Map<String, String> dnsMap = new HashMap<>();
    private static final int BUFFER_SIZE = 1400;
    
    private final static String COD_TEXTO = "UTF-8";

    public static void main(String[] args) throws IOException 
    {
        loadDNSData();
        
         if (args.length < 1) 
         {
            System.err.println("ERROR, indicar: puerto.");    //Pedimos como parámetro el puerto de escucha, 5000
            System.exit(1);
         }

        int numPuerto = Integer.parseInt(args[0]);   //Recibimos el número de puerto de escucha del servidor


        DatagramSocket socket = new DatagramSocket(numPuerto);
        
        System.out.println("DNS Server started on port:"+numPuerto);

        while (true) 
        {
            byte[] b = new byte[BUFFER_SIZE];   //Creamos el array de bytes para guardar la petición
            
            DatagramPacket paqueteRecibido = new DatagramPacket(b, BUFFER_SIZE);  //Creamos un datagrama para recibir las peticiones
            
            socket.receive(paqueteRecibido);    // Nos ponemos en espera hasta que llegue un paquete

            String domain = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength(),COD_TEXTO);
            
            System.out.println("Peticion de consulta para el dominio " + domain);

            // Crear un nuevo hilo para manejar la solicitud del cliente
            
            ClientRequestHandler clientHandler = new ClientRequestHandler(socket, paqueteRecibido, dnsMap);
            
            
            Thread thread = new Thread(clientHandler);    //Creamos un thread para ese tipo de hilo
            
            thread.start();   //Iniciamos ese hilo manejador de petición
        }
    }

    private static void loadDNSData() throws IOException 
    {
        BufferedReader reader = new BufferedReader(new FileReader(DNS_FILE));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            String domain = parts[0];
            String ip = parts[1];
            dnsMap.put(domain, ip);
        }
        reader.close();
    }
}

class ClientRequestHandler implements Runnable 
{
    private DatagramSocket socket;                   //Socket creado en el principal para enviar y recibie
    private DatagramPacket paqueteRecibido;          //Paquete recibido
    private Map<String, String> dnsMap;              //Hashmap con los paraes dominio,IP
    
    private String COD_TEXTO = "UTF-8";
    
    private int BUFFER_SIZE = 1400;
    
    
    public ClientRequestHandler(DatagramSocket socket, DatagramPacket paqueteRecibido, Map<String, String> dnsMap) {
        this.socket = socket;
        this.paqueteRecibido = paqueteRecibido;
        this.dnsMap = dnsMap;
    }

    @Override
    
    public void run() 
    {
        String domain = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());   //Sacamos el contenido del datagrama a un string
       
        System.out.println("Procesando petición para el dominio " + domain);

        String Respuesta="";  //Respuesta que devolverá el servidor  
        
        if (dnsMap.containsKey(domain) )    //Si el Dominio Pedido está en el HashMap
        {
            Respuesta=dnsMap.get(domain);     //Obtenemos el valor(Ip) para el Dominio solicitado
        }
        else   //Ese dominio no estaba en el archivo
        {
            Respuesta="NO ENCONTRADO";   //Indicamos que no hemos encontrado la IP 
        }    
        
        
        byte[] responseBuffer=new byte[BUFFER_SIZE]; ;   //Array de bytes con la ip de respuesta
        
        try 
        {
            responseBuffer = Respuesta.getBytes(COD_TEXTO);    //Convertimos el string en array de bytes
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Construimos el Datragrama de respuesta con el contenido y los datos del remitente del paquete recibido
        
        DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length, this.paqueteRecibido.getAddress(),this.paqueteRecibido.getPort());

        try 
        {
            socket.send(responsePacket);
            System.out.println("Sent response with IP " + Respuesta);
        } catch (IOException e) {
            System.out.println("Error sending response: " + e.getMessage());
        }
    }
}

