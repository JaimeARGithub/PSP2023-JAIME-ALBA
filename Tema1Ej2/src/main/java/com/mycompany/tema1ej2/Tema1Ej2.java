/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jaime
 */
public class Tema1Ej2 {

    public static void main(String[] args) {
        // Ha dicho que a lo de la clase RunTime no le hagamos ni caso,
        // es una clase antigua que ya no se usa.
        
        // Primero comprobamos que recibimos exactamente un parámetro
        if (args.length!=1) {
            System.out.println("Error; se requiere de un solo parámetro.");
            System.exit(0);
        }
        
        String dirNombre = args[0];
        File dir = new File(dirNombre);
        
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Error; el directorio no existe o no es válido.");
            // Es bueno poner distintos valores en los exit para que,
            // si sale del programa, sepamos por qué
            System.exit(1);
        }
        
        
        
        try {
            ProcessBuilder pb = new ProcessBuilder("ls");
            pb.directory(dir);
            
            Process p = pb.start();
            
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String linea = br.readLine();
            while (linea!=null) {
                System.out.println(linea);
                linea = br.readLine();
            }
            
            
            
        } catch (IOException ioe) {
            System.out.println("Excepción del tipo IOE");
        }
    }
}
