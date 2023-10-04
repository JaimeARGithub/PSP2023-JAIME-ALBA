/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej4c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jaime
 */
public class Tema1Ej4c {

    public static void main(String[] args) throws InterruptedException {
        if (args.length!=1) { 
        // Sólo acepta un argumento de entrada, el nombre del archivo
            System.out.println("Error; introducir un único comando.");
            System.exit(1);
        } 
        
        String nombre = args[0];
        double memoria = 0;
        
        ProcessBuilder pb = new ProcessBuilder("ls");
        
        try {
            
            Process p = pb.start();
            p.waitFor();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            
            String linea = br.readLine();
            String[] campos;
            while (linea!=null) {
                System.out.println(linea);
                linea = br.readLine();
            }
            br.close();
            
            
        } catch (IOException ioe) {
            System.out.println("Excepción de tipo IOE");
        }
    }
}
