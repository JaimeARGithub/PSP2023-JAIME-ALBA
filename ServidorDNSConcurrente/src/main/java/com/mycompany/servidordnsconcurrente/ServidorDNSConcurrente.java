/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.servidordnsconcurrente;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaime
 */
// CONCURRENTE: se va creando y lanzando una respuesta por cada petición que tenga que atender
public class ServidorDNSConcurrente {
    
    private final static int MAX_BYTES = 1400;
    private static final int BUFFER_SIZE = 1400;
    
    private final static String COD_TEXTO="UTF-8";
    private static final String DNS_FILE = "./dominios.txt";
    
    // los hilos no van a escribir en el hashmap, sólo van a leer
    private static final Map<String,String> dnsMap = new HashMap<>();
    
    
    
    // Cargar en el hashmap los datos del archivo
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
    
    

    public static void main(String[] args) throws IOException {
        
        loadDNSData();
        
        
        if (args.length < 1) {
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
            
            
            // ASPECTO PRINCIPAL DE LA CONCURRENCIA:
            // A PARTIR DE CUANDO SE RECIBE EL PAQUETE, SE CREA EL HILO 
            
            
            
            // Siempre que se hace una petición, se crea un hilo
            System.out.println("Peticion de consulta para el dominio " + domain);

            // Al hilo le pasamos el socket, el paquete con los datos, y el archivo DNS
            ClientRequestHandler clientHandler = new ClientRequestHandler(socket, paqueteRecibido, dnsMap);
            
            
            // Se crea la clase que implementa la interfaz y se lanza
            Thread thread = new Thread(clientHandler);
            thread.start();
            
            
            
            // EL SERVIDOR SOLAMENTE RECIBE PAQUETES Y CREA HILOS, PERO NO CONTESTA
            // EL ENCARGADO DE CONTESTAR ES EL HILO
        }
    }
}


// PARA CONTESTAR, EL HILO NECESITA
// -EL SOCKET
// -EL PAQUETE CON LOS DATOS
// -EL HASHMAP CON LA INFORMACIÓN
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


        
        String respuesta = dnsMap.getOrDefault(domain, "IP NOT FOUND"); 
        
        
        
        byte[] responseBuffer=new byte[BUFFER_SIZE]; ;   //Array de bytes con la ip de respuesta
        
        try 
        {
            responseBuffer = respuesta.getBytes(COD_TEXTO);    //Convertimos el string en array de bytes
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        //Construimos el Datragrama de respuesta con el contenido y los datos del remitente del paquete recibido
        DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length, this.paqueteRecibido.getAddress(),this.paqueteRecibido.getPort());

        
        
        try 
        {
            socket.send(responsePacket);
            System.out.println("Sent response with IP " + respuesta);
        } catch (IOException e) {
            System.out.println("Error sending response: " + e.getMessage());
        }
    }
    
    // ¡¡¡PROBLEMA!!!
    // Cuello de botella; el socket por el que salen los paquetes es siempre el mismo.
    // Hay en el classroom un código que no entra en el examen que permite comparar los
    // tiempos de respuesta con el servidor iterativo y con el servidor concurrente.
    
    // Posible error y que ya le ha pasado a Ricardo: no puedo lanzar a funcionar dos
    // servidores al mismo puerto
    // Se va a apreciar mejor la diferencia de tiempos cuando haya varias entradas y salidas;
    // con esta dimensión de código, casi es más rápido el iterativo
}
