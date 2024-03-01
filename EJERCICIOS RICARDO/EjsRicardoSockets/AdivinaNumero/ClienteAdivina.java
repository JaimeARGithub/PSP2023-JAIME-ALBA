/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socketsjava;

import java.net.*;
import java.io.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteAdivina 
{
    public static void main(String[] args) 
    {
        
        System.out.println("Lanzador de adivinadores iniciado");
        
        Thread[] Hilos = new Thread[5];  //Creamos el array de hilos
        
        for (int i = 0; i < 5; i++) 
        {
            ClientThread Cli= new ClientThread(i);      
            
            Thread h = new Thread(Cli);
            h.start();
            Hilos[i] = h;   //Guardamos ese hilo en un array
           
        }
        
        
        for(int i=0;i<5;i++)
        {
           try {
               Hilos[i].join();   //Indicamos que el principal espere a que termine ese hilo
               
           } catch (InterruptedException ex) 
           {
               Logger.getLogger(ClienteAdivina.class.getName()).log(Level.SEVERE, null, ex);
           }
           
    
       }
    
       System.out.println("Lanzador de adivinadores terminado");
        
        
    }
 
}
class ClientThread implements  Runnable 
{
    private int id;

    public ClientThread(int id) {
        this.id = id;
    }

    @Override
    public void run() 
    {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");
            
            int serverPort = 7000;

            Random r= new Random();
   
            int Max=20;
            
            int intento ;
            
            while (true) 
            {
                        
                intento = 1+ r.nextInt(Max);   //Elegimos un número hasta el rango permitido
                
                byte[] sendData = String.valueOf(intento).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
               
                clientSocket.send(sendPacket);

                // Recibir respuesta del servidor
                
                byte[] receiveData = new byte[1024];
                
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                
                clientSocket.receive(receivePacket);
                
                String respuestaServidor = new String(receivePacket.getData(), 0, receivePacket.getLength());

                System.out.println("Hilo " + id + ", Intento: " + intento + ", Respuesta del servidor: " + respuestaServidor);

                if (respuestaServidor.equals("ADIVINADO") || respuestaServidor.equals("FIN")  ) 
                {
                    break; // Salir del bucle si el número ha sido adivinado o se ha recibido mensaje "FIN"
                } 
                
                try { 
                    Thread.sleep(1000);            //Le metemos un segundo de retardo para ver mejor los intentos
                } catch (InterruptedException ex) 
                {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            }

            System.out.println("Hilo " + id + " terminado");
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


