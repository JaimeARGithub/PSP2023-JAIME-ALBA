/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaime
 */
public class Tema1Ej7 {

    public static void main(String[] args) {
        if (args.length!=3) {
            System.out.println("Son necesarios tres argumentos.");
            System.exit(0);
        }
        
        String entrada = args[0];
        String pattern = args[1];
        String salida = args[2];
        
        File archEnt = new File(entrada);
        
        if (!archEnt.exists()) {
            System.out.println("No existe el archivo que se intenta usar como entrada.");
            System.exit(1);
        }
        
        ProcessBuilder pb = new ProcessBuilder("grep", pattern, entrada);
        File fSalida = new File(salida);
        pb.redirectOutput(fSalida);
        
        
        try {
            
            Process p = pb.start();
            FileReader lector = new FileReader(fSalida);
            BufferedReader br = new BufferedReader(lector);
            
            String linea = br.readLine();
            while (linea != null) {
                System.out.println(linea);
                linea = br.readLine();
            }
            
            
        } catch (IOException ioe) {
            System.out.println("Excepción de tipo IOE");
        }
    }
}
