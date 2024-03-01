/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socketsjava;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServidorEchoConcurrente {

    public static void main(String[] args) throws IOException {
        
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
    
    private static class ClientHandler implements Runnable 
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
                System.out.println("Mensaje Recibido: " + inputLine);

                inputLine="#"+inputLine+"#";
                
                byte[] sendData = inputLine.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}

