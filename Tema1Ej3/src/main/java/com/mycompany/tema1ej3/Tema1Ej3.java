/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej3;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jaime
 */
public class Tema1Ej3 {

    // Está un poco chapucero voy avisando, pero lo que
    // es funcionar funciona
    
    public static void main(String[] args) {
        
        if (args.length!=1) { 
        // Sólo acepta un argumento de entrada, el nombre del archivo
            System.out.println("Error; introducir un único comando.");
            System.exit(1);
        } 
        
        String comando = args[0];
        Boolean existeComando=false;

        
        ProcessBuilder pb = new ProcessBuilder("man", comando);
        
        // Una opción es con man.
        // Opción b: los ejecutables del sistema están en /bin; comprobar
        // si el comando existe en la carpeta /bin. Concatenar /bin con el
        // nombre del comando y comprobar que existe (que sea un archivo
        // válido).
        
        pb.directory(new File("/home/jaime"));
        
        try {
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String linea = br.readLine();
            
            if (linea!=null) {
                existeComando = true;
            } 
            
            br.close();
            
        } catch (IOException ioe) {
            System.out.println("Excepción IOE");
        }
        
        
        
        if (!existeComando) {
            System.out.println("Error. El comando introducido no existe.");
        } else {
            
            ProcessBuilder pb2 = new ProcessBuilder(comando);
            pb2.directory(new File("/home/jaime"));
            
            try {
                Process p2 = pb2.start();
                InputStreamReader isr2 = new InputStreamReader(p2.getInputStream());
                BufferedReader br2 = new BufferedReader(isr2);
                String linea = br2.readLine();
                while (linea != null) {
                    System.out.println(linea);
                    linea = br2.readLine();
                }
                br2.close();
                
                
            } catch (IOException ioe) {
                System.out.println("Excepción de tipo IOE");
            }
        }
    }
}
