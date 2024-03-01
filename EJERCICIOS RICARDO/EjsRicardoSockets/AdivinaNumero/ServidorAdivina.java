/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package socketsjava;

import java.net.*;
import java.util.HashMap;
import java.io.*;

public class ServidorAdivina 
{
    public static void main(String[] args) 
    {
        try 
        {
            DatagramSocket serverSocket = new DatagramSocket(7000);

            System.out.println("Servidor iniciado en el puerto:"+serverSocket.getLocalPort());

            // Generar número aleatorio entre 1 y 20
            int numeroSecreto = (int) (Math.random() * 20) + 1;

            System.out.println("Número secreto: " + numeroSecreto);

            // Recibir los intentos de los clientes
            byte[] receiveData = new byte[1024];

            boolean Adivinado=false; //Variable para indicar que el número ha sido adivinado
            
            while (true) 
            {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                
                serverSocket.receive(receivePacket);

                String mensajeCliente = new String(receivePacket.getData(), 0, receivePacket.getLength());

                InetAddress clienteDireccion = receivePacket.getAddress();
                
                int clientePuerto = receivePacket.getPort();

                //clientesConectados.put(clientePuerto,clienteDireccion); // Agregar la puerto y dirección del cliente al HashMap
                
                if (!Adivinado)   //Si el numero no ha sido adivinado procesamos ese intento de adivinar
                {
                        if (Integer.parseInt(mensajeCliente) == numeroSecreto) 
                        {
                            // Enviar mensaje "ADIVINADO" al cliente que ha adivinado el número

                            String mensaje = "ADIVINADO"; // Incluir la dirección del cliente en el mensaje
                            
                            Adivinado=true; //Indicamos que el numero ha sido adivinado
                            
                            byte[] sendData = mensaje.getBytes();

                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clienteDireccion,
                                    clientePuerto);

                            serverSocket.send(sendPacket);


                        } 
                        else //Tenemos que enviar un mensaje indicando que ha fallado
                        {
                          String mensaje = "FALLADO"; // Incluir la dirección del cliente en el mensaje
                          byte[] sendData = mensaje.getBytes();

                          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clienteDireccion,
                                    clientePuerto);

                           serverSocket.send(sendPacket);

                        } 
                        
                }         
                else   //Hay que enviar el mensaje de fin a los clientes para que se detengan
                {
                  
                   
                   byte[] sendDataFin = "FIN".getBytes();
                   
                   DatagramPacket sendPacketFin = new DatagramPacket(sendDataFin, sendDataFin.length,
                                                                     clienteDireccion, clientePuerto);
                            
                   System.out.println("Enviando mensaje fin al cliente con puerto: " + clientePuerto);
                            
                   serverSocket.send(sendPacketFin);
                
                }    
                               
                
            }
           
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}


