/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej4a;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jaime
 */
public class Tema1Ej4a {

    public static void main(String[] args) {
        
        if (args.length!=1) { 
        // Sólo acepta un argumento de entrada, el nombre del archivo
            System.out.println("Error; introducir un único comando.");
            System.exit(1);
        } 
        
        String nombre = args[0];
        
        ProcessBuilder pb = new ProcessBuilder("ps", "-ef");
        
        try {
            
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            
            String linea = br.readLine();
            String[] campos;
            while (linea!=null) {
                campos = linea.split(" +");
                if (campos.length==8) {
                    if (campos[0].equals(nombre)) {
                        System.out.println(campos[7]);
                    }
                }
                
                linea = br.readLine();
            }
            
            br.close();
            
        } catch (IOException ioe) {
            System.out.println("Excepción IOE");
        }
        
    }
}
