/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clienteecho;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.io.UnsupportedEncodingException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author silvia
 */

// Para comunicar dos programas, hacen falta medio, direccionamiento, protocolos
// Subdivididos en capas.

// necesitamos la IP de la máquina a la que nos vamos a conectar y el puerto
// puerto: identifica, dentro del ordenador conectado a inet, a qué programa le paso los datos



// HAY QUE PONER netcat -ul 5000; al servidor no hay que decirle IP, sólo el puerto
// nc -l 5000 -> netcat, listen puerto 5000
// nc -l 5000 escucha con TCP
// nc -ul 5000 escucha con UDP

// para que funcione: en el terminal, netcat -ul 5000
// en los argumentos: localhost 5000


// los puertos del 0 al 1024 están reservados por los sistemas operativos para servicios
// no pueden usarse
// si tengo un servicio que usa un puerto que conozco, mejor evitarlo
public class ClienteEcho {
    
    // pide una palabra, la manda en un paquete, al servidor le llega y espera la respuesta
    // cliente-servidor
    // el cliente es más ligero, sólo pide un recurso
    // nosotros vamosa montar servidores en la misma máquina -> localhost
    
    // para comunicar programas en red:
    // TCP -> orientado a conexión, se envía un flujo de stream la información
    // simula que hay un archivo abierto en la otra máquina
    // más usado
    
    // UDP -> no orientado a conexión, no la crea
    // son mensajes, rollo sms, mandas un mensaje y el otro no se queda esperando respuesta
    
    
    // UDP: la info se envía en forma de datagramas; no se abre un canal de comunicación,
    // sólo se envían mensajes
    // Pueden llegar los paquetes desordenados, y si un paquete se pierde, no se recuepra
    // Es para mandar info muy deprisa si te dan igual las pérdidas
    
    
    // tamaño máximo del datagrama; hay que decirle a dónde va, IP y puerto
    // si se excede, se fragmenta
    private final static int MAX_BYTES=1400;
    // codificación de los caracteres
    private final static String COD_TEXTO="UTF-8";
    
    // echo: pide un mensaje, se lo manda al servidor y el servidor contesta
    
    public static void main(String[]args){
        // se revisan los dos parámetros
        
        if(args.length<2){
          System.err.println("ERROR, indicar: host_servidor puerto");
          System.exit(1);
        }
        
        // la ip puede ponerse en IPv4 o en nombre de dominio
        // se ajustan ip y puerto
        //String nomHost = args[0];
        //int numPuerto = Integer.parseInt(args[1]);
        
        // CAMBIADO PARA PROBAR EL SERVIDOR DNS
        String nomHost = "localhost";
        //int numPuerto = 3000;
        
        // CAMBIADO PARA PROBAR EL SERVIDOR ECHO CONCURRENTE
        int numPuerto = 4444;
        
        
        
        
        // se abre el socket con try with resources; también el buffered reader
        // se crea un socket de tipo datagrama
        try (DatagramSocket clientSocket = new DatagramSocket();
                InputStreamReader isrStdIn = new InputStreamReader(System.in, COD_TEXTO);
                BufferedReader brStdIn = new BufferedReader(isrStdIn)){
            
            
            // string para recoger lo que se lea del buffered reader
            String lineaLeida;
            
            // línea para saber a qué puerto local está conectado el socket
            System.out.println("Socket cliente conectado desde el puerto: " + clientSocket.getLocalPort());
            
            System.out.println("Introducir lineas. Linea vacía para terminar.");
            System.out.print("Linea>");
            
            
            // mientras al leer la línea no sea nulo y la línea tenga caracteres
            while((lineaLeida = brStdIn.readLine()) != null && lineaLeida.length() > 0){
                
                // clase que hay que pasarle al datagrama para decirle a qué puerto y a qué máquina va
                // clase sin constructor, lo que se utiliza al crearla es getByName y la IP de la máquina a la que se conecta
                InetAddress IPServidor = InetAddress.getByName(nomHost);
                // el número de puerto se lo puedo meter directamente al datagrama, pero la IP no
                // la IP se mete a través de la clase InetAddress
                
                // la línea leída no la puedo meter directamente, no puede ir por string
                // hay que meterle un array de bytes con String.getBytes
                byte[] b = lineaLeida.getBytes(COD_TEXTO);
                
                // con todos los datos, se crea el paquete
                // el daragrampacket construye el contenedor con la info que voy a transmitir
                // le indico los datos que va a contener el paquete, cuánto ocupa, la IP y el puerto
                DatagramPacket paqueteEnviado = new DatagramPacket(b, b.length, IPServidor, numPuerto);
                
                
                
                // El connect sólo en TCP
                //clientSocket.connect(IPServidor, numPuerto);
                
                // se envía el paquete con .send
                clientSocket.send(paqueteEnviado);
                
                
                
                // tengo que guardar la respuesta
                // Para la respuesta: creo otro array de bytes del tamaño máximo
                byte[] datosRecibidos = new byte[MAX_BYTES];
                
                // construyo otro datagrama; sólo tengo que decirle el contenido y la longitud
                // la IP y el puerto sólo se ponen si es un  paquete que se vaya a enviar
                DatagramPacket paqueteRecibido = new DatagramPacket(datosRecibidos, datosRecibidos.length);
                
                // llamo al socket para que reciba el paquete
                // al usar el método .receive, se pone el programa en espera hasta que llega un paquete
                // guarda los datos en paqueteRecibido
                clientSocket.receive(paqueteRecibido);
                
                
                // llega un array de bytes
                // para construir la string, cojo los datos del paquete
                // el paquete tiene varios campos: datos, puertos e ip -> le pido sólo los datos, .getData()
                
                // los otros dos parámetros son para decirle de qué parte a qué parte del datagrama quiero sacar la info;
                // en este caso, quiero los datos desde el principio hasta el final
                
                // último parámetro: codificación
                String respuesta = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength(), COD_TEXTO);
                
                // la dirección está en el datagrama, en el método que devuelve la dirección
                // de .getAddress puedo sacar el objeto InetAddress, de él quiero la ip
                // por último saco el número de puerto
                System.out.printf("Datagrama recibido de %s:%d: %s\n", paqueteRecibido.getAddress().getHostAddress(), paqueteRecibido.getPort(), respuesta);
                System.out.println("Linea>");
                
                
                
                // las excepciones de socket saltan cuando el puerto ya está en uso
                
                // para poder contestar: abro otra terminal, nc localhost -u numpuerto
                // numpuerto -> desde el que envía el netbeans
                
                // echo "MuyBien" | nc localhost -u numpuerto
                
                // pasos:
                // 1.- preparo programa de netbeans
                // 2.- abro terminal y meto nc -ul 5000
                // 3.- meto mensaje en netbeans, debería llegar a la terminal
                // 4.- abro otra terminal, echo "MuyBien" | nc localhost -u numpuerto
                // 5.- debería llegar a netbeans
                
                
                // cliente: en una terminal, echo "hola" | nc localhost -u 5000
                // servidor: en otra terminal, nc -ul 5000
            }
            
        } catch (SocketException ex) {
            Logger.getLogger("Excepcion de sockets");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClienteEcho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger("Excepcion de E/S");
        }
    }
}
