/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.adivinanumeroclienteservidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

/**
 *
 * @author jaime
 */

// EXAMEN DE UN AÑO ANTERIOR
// Adivina un número, pero con hilos; un cliente lanza varios hilos que hacen peticiones
// al servidor de manera concurrente, el servidor puede responder de manera iterativa o
// concurrente

public class AdivinaNumeroClienteServidor {

    public static void main(String[] args) {
        for (int i=0; i<5; i++) {
            // Se crean y lanzan solamente 5 hilos cliente, con el identificador
            ClientThread cli = new ClientThread(i);
            
            Thread h = new Thread(cli);
            
            h.start();
            // No hace falta el join
        }
    }
}

class ClientThread implements Runnable {
    
    private int id;
    
    public ClientThread(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        // Cada cliente crea un Datagram
        
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 7000;
            Random r = new Random();
            int max = 20;
            int intento;
            
            while (true) {
                
                intento = 1 + r.nextInt(20);
                
                // Conversión del intento a array de bytes, lo pasamos a datagrama y se envía al destino
                byte[] sendData = String.valueOf(intento).getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                clientSocket.send(sendPacket);
                
                
                // Recibir la respuesta del servidor
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String respuestaServidor = new String(receivePacket.getData(), 0, receivePacket.getLength());
                
                System.out.println("Hilo "+id+" lo intenta con " +intento+ ", respuesta del servidor: " + respuestaServidor);
                
                // La ejecución termina cuando me digan que he acertado o cuando
                // me digan fin (otro hilo lo ha adivinado)
                if (respuestaServidor.equals("ADIVINADO") || respuestaServidor.equals("FIN")) {
                    break;
                }
            }
            
            clientSocket.close();
            
            
        } catch (IOException e) {
            
            e.printStackTrace();
            
        }
    }
}
