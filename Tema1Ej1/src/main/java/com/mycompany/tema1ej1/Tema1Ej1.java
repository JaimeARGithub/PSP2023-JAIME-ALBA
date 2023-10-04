/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jaime
 */
public class Tema1Ej1 {

    public static void main(String[] args) {
        // Versión 1: heredando entrada-salida del programa principal
        ProcessBuilder pb = new ProcessBuilder("ls");
        pb.directory(new File("/home/jaime"));
        pb.inheritIO();
        
        try {
            pb.start();
        } catch (IOException ioe) {
            System.out.println("Excepción IOE");
        }
        
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("Cambio de versión");
        System.out.println(" ");
        System.out.println(" ");
        
        // Versión 2: empleando el método getInputStream
        ProcessBuilder pb2 = new ProcessBuilder("ls");
        pb2.directory(new File("/home/jaime"));
        try {
            
            Process p2 = pb2.start();
            InputStreamReader isr = new InputStreamReader(p2.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String linea = br.readLine();
            
            while (linea != null) {
                System.out.println(linea);
                linea = br.readLine();
            }
            br.close();
            
            
        } catch (IOException ioe) {
            System.out.println("Excepción de tipo IOE");
        }
        
    }
}
