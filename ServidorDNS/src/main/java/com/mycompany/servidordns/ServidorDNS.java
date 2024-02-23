/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.servidordns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author jaime
 */
// Hecho con protocolo UDP. No hay conexión directa.

// Se construyen los datagramas; hay redes que no los admiten de más de X tamaño, para
// evitar que nos los fragmenten los fijamos nosotros.
// Abrir un DatagramSocket estoy abriendo una comunicación con la red local, me permite
// enviar o recibir paquetes.

// Como el acceso a almacenamiento secundario es muy lento, el contenido del fichero se
// vuelca en un HashMap; las variables están en memoria principal, que es mucho más rápida.

// El servidor abre el datagramSocket en un puerto, no hace nada más; se queda esperando
// peticiones a ese puerto.

// Bucle while true -> los servidores se ejecutan siempre dentro de un bucle principal
// Bucle infinito, se suelen evitar, pero en servidores se usan

public class ServidorDNS {

    // Vamos a hacer un mini archivo host; tiene las IPs y las nombres de los sitios más habituales
    // El server recibe el nombre del dominio, lo busca y responde con su IP; vapor UDP
    
    
    // el archivo que voy a leer va a ser siempre el mismo, y es almacenamiento secundario
    // el acceso es muy lento
    // antes de arrancar el servidor, hago una apertura y lectura del archivo y
    // me lo guardo todo en un hashmap
    
    // cuando me den un nombre de dominio, lo busco en el hashmap como clave
    // si existe, devuelvo el valor, y si no, lo indico
    
    private final static int MAX_BYTES = 1400;
    private final static String COD_TEXTO="UTF-8";
    
    
    public static void main(String[] args) {
        
        // parámetros: solamente el puerto, 3000
        if (args.length < 1) {
            System.out.println("ERROR, indicar: puerto.");
            System.exit(1);
        }
        
        int numPuerto = Integer.parseInt(args[0]);
        

        
        try (DatagramSocket serverSocket = new DatagramSocket(numPuerto)) {
            
            
            // Volcamos el contenido del archivo dominios.txt a un hashMap
            File f = new File("./dominios.txt");
            Scanner sc = new Scanner(f);
            HashMap<String,String> dominios = new HashMap<String,String>();
            String linea;
            String[] campos;
            while ( sc.hasNextLine() ) {
                linea = sc.nextLine();
                campos = linea.split(" ");
                
                // Al hashMap le ponemos como clave el campo 0 y valor el campo 1
                dominios.put(campos[0], campos[1]);
            }


            
            
            // indicamos que es un servidor DNS
            System.out.printf("Creado servidor DNS en el puerto %s.\n", numPuerto);
            
            
            while (true) {
                System.out.println("Esperando peticiones DNS.");
                // la recepción de datos es igual
                
                
                // array de bytes para el datagrama que va a recibir el paquete
                byte[] datosRecibidos = new byte[MAX_BYTES];
                
                // el cliente enviaba y recibía
                // el servidor hace lo contrario: recibe y envía
                // le indico al servidor que se quede esperando para recibir un paquete
                // cuando el paquete llega, lo procesa, elabora la respuesta y responde
                DatagramPacket paqueteRecibido = new DatagramPacket(datosRecibidos, datosRecibidos.length);
                /// el .receive pone el programa en espera y no sigue hasta que llegue algo
                serverSocket.receive(paqueteRecibido);
                
                
                // tras recibir, se contesta
                // hacen falta los datos, la IP y el puerto de origen; son necesarios
                // porque, a la hora de enviar la respuesta, hay que saber de qué máquina
                // viene y de qué aplicación
                String nombreDominio = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength(), COD_TEXTO);
                
                
                
                // ¡¡A PARTIR DE AQUÍ!!
                // línea recibica es el nombre del dominio, vamos a contestar con su ip
                // busco con la clave que corresponda
                
                // busco el valor para ese nombre de dominio, y si no existe, mando mensaje de error
                System.out.println("Recibida petición para el dominio " + nombreDominio);
                
                String respuesta = dominios.getOrDefault(nombreDominio, "IP NOT FOUND");
                
                
                // Ya tenemos la respuesta; la retornamos al cliente
                // la dirección y el puerto se quedan
                InetAddress IPCliente = paqueteRecibido.getAddress();
                
                // el número de puerto devuelve un entero
                int puertoCliente = paqueteRecibido.getPort();
                
                
                // El envío de la respuesta es igual que en los ejercicios anteriores
                // hay que convertir la String en un array de bytes para poder enviar la respuesta,
                // o me pega un error
                byte[] b = respuesta.getBytes(COD_TEXTO);
                
                // para pasarle todos los datos, hay que indicarle que lo haga desde el inicio hasta el final
                DatagramPacket paqueteEnviado = new DatagramPacket(b, b.length, IPCliente, puertoCliente);
                
                serverSocket.send(paqueteEnviado);
            }
            
            
            // Para ejecutarlo: me voy al cliente. Cambio nomHost por localhost y numPuerto por 3000.
            // Arranco el servidor.
            // Lanzo el cliente.
            
            
            // Problema de estos servidores: son iterativos. Esperan una petición, y hasta
            // que no es respondida, no responde otra.
            // Forma óptima: crear un hilo por cada petición, se encargan de recoger la
            // respuesta y reenviarla.
            // Por cada petición se crea un hilo para la petición DNS; desde Respuesta hasta
            // el final lo hace el hilo.
            
        } catch (SocketException ex) {
            System.out.println("Excepción de socket");
        } catch (IOException ioe) {
            System.out.println("Excepción IO");
        }
    }
}
