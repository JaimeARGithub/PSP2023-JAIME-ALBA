/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej3b;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jaime
 */
public class Tema1Ej3b {

    public static void main(String[] args) {
        if (args.length!=1) { 
        // Sólo acepta un argumento de entrada, el nombre del archivo
            System.out.println("Error; introducir un único comando.");
            System.exit(1);
        } 
        
        String comando = args[0]; // Recojo el parámetro recibido
        comando = "/bin/"+comando; // Lo concateno con /bin
        File archivo = new File(comando);

        if (!archivo.exists()) {
            System.out.println("Error. El comando introducido no es válido.");
            System.exit(1);
        }
        
        
        
        try {
            // Dile que no le mola usar el args[0], pero para no tener que
            // usar otra variable
            // Igualmente funciona si utilizo la ruta absoluta
            ProcessBuilder pb = new ProcessBuilder(comando);
            
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String linea = br.readLine();
            
            while (linea!=null) {
                System.out.println(linea);
                linea = br.readLine();
            } 
            
            br.close();
            
        } catch (IOException ioe) {
            System.out.println("Excepción IOE");
        }

    }
}
