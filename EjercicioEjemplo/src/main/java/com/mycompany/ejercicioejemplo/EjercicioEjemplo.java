/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejercicioejemplo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jaime
 */
public class EjercicioEjemplo {

    public static void main(String[] args) {
        
        try {
            
            ProcessBuilder pb = new ProcessBuilder("ls", "-ln");
            
            //pb.inheritIO(); No se hace para que no herede salida
            Process p = pb.start(); // Creo el proceso
            
            //System.out.println("Programa principal terminado");
            
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr); // Trato la salida
            // y la paso a un bufferedreader para tratarla línea a línea
            
            String linea = br.readLine();
            while (linea!=null) {
                System.out.println(linea);
                linea = br.readLine();
            }
            
            
        } catch (IOException ioe) {
            System.out.println("Excepción ioe");
        }
        
    }
}
