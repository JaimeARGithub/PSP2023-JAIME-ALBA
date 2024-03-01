/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socketsjava;

import java.net.*;

public class DNSClientCompar 
{
    
   private final static int MAX_BYTES = 1400;
   private final static String COD_TEXTO = "UTF-8"; 
    
   public static void main(String[] args) throws Exception 
   {
      String concurrentHostname = "localhost"; // Cambiar esto al nombre del servidor DNS concurrente
      String iterativeHostname = "localhost"; // Cambiar esto al nombre del servidor DNS iterativo
      
      String domainName = "google.com"; // Cambiar esto al nombre de dominio que se desea consultar
      
      int numPeticiones = 10; // Cambiar esto al número de solicitudes DNS que se desea enviar
      
      // Configurar las direcciones IP de los servidores DNS
      
      InetAddress concurrentIPAddress = InetAddress.getByName(concurrentHostname);
     
      InetAddress iterativeIPAddress = InetAddress.getByName(iterativeHostname);
      
      // Definir los números de puerto de los servidores DNS
      
      int PuertoIterativo = 3000; // Puerto del servidor DNS iterativo
      
      int PuertoConcurrente = 4001; // Puerto del servidor DNS concurrente
      
      // Crear los sockets de datagrama UDP
      
      DatagramSocket concurrentSocket = new DatagramSocket();
      
      DatagramSocket iterativeSocket = new DatagramSocket();
      
      // Enviar las solicitudes DNS y medir los tiempos de respuesta
      
      for (int i = 0; i < numPeticiones; i++) 
      {
         // Enviar la solicitud DNS al servidor DNS concurrente y medir el tiempo de respuesta
          
         long startTimeConcurrent = System.currentTimeMillis();   //Empezamos a contar el tiempo del servidor concurrente
         
         byte[] sendData = domainName.getBytes(COD_TEXTO );  
         
         
         DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, concurrentIPAddress, PuertoConcurrente);
         
         concurrentSocket.send(sendPacket);
         
         byte[] receiveData = new byte[MAX_BYTES];
         
         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
         
         concurrentSocket.receive(receivePacket);
         
         long endTimeConcurrent = System.currentTimeMillis();    //Cogemos el tiempo de respusta de la petición concurrente
         
         
         
         long responseTimeConcurrent = endTimeConcurrent - startTimeConcurrent;    //Calculamos el tiempo que ha tardado en responder
         
         System.out.println("Concurrent DNS response time: " + responseTimeConcurrent + "ms");  //Mostramos la respuesta de esa petición
         
         // Enviar la solicitud DNS al servidor DNS iterativo y medir el tiempo de respuesta
         
         long startTimeIterative = System.currentTimeMillis();   //Anotamos la marca de tiempo de inicio para el iterativo
         
         sendData = domainName.getBytes();
         
         sendPacket = new DatagramPacket(sendData, sendData.length, iterativeIPAddress, PuertoIterativo );
         
         iterativeSocket.send(sendPacket);
         
         receiveData = new byte[MAX_BYTES];
         
         receivePacket = new DatagramPacket(receiveData, receiveData.length);
         
         iterativeSocket.receive(receivePacket);
         
         long endTimeIterative = System.currentTimeMillis();
         
         long responseTimeIterative = endTimeIterative - startTimeIterative;   //Calculamos el intervalo de la respuesta iterativa
         
         System.out.println("Iterative DNS response time: " + responseTimeIterative + "ms");
         
         // Calcular la diferencia entre los tiempos de respuesta de los dos servidores DNS
         long responseTimeDifference = responseTimeConcurrent - responseTimeIterative;
         
         System.out.println("Difference in response times: " + responseTimeDifference + "ms\n");
      }
      
      // Cerrar los sockets de datagrama UDP
      concurrentSocket.close();
      iterativeSocket.close();
   }
}


