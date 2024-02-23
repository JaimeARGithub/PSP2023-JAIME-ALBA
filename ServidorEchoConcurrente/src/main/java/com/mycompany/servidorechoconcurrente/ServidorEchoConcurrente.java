/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.servidorechoconcurrente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author jaime
 */

// PARA PROBAR EL FUNCIONAMIENTO:
// 1.- Lo ponemos en funcionamiento
// 2.- Cogemos el cliente echo, le ponemos el puerto 4444

public class ServidorEchoConcurrente {

    public static void main(String[] args) {
        int portNumber = 4444;
        byte[] buffer = new byte[1024];
        
        try (DatagramSocket serverSocket = new DatagramSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);
            while (true)
            {
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                
                serverSocket.receive(receivePacket);
                
                System.out.println("Received packet from " + receivePacket.getAddress());
                
                Thread t = new Thread(new ClientHandler(serverSocket, receivePacket));
                t.start();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}


class ClientHandler implements Runnable 
    {
        private DatagramSocket serverSocket;
        private DatagramPacket receivePacket;
        
        public ClientHandler(DatagramSocket serverSocket, DatagramPacket receivePacket) {
            this.serverSocket = serverSocket;
            this.receivePacket = receivePacket;
        }
        
        @Override
        
        public void run() {
            try {
                String inputLine = new String(receivePacket.getData(), 0, receivePacket.getLength());
                inputLine = "###" + inputLine + "###";
                System.out.println("Mensaje Recibido: " + inputLine);
                
                byte[] sendData = inputLine.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
}
