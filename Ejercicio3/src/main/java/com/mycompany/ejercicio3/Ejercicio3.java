/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejercicio3;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jaime
 */
public class Ejercicio3 {

    public static void main(String[] args) {
        // Cambio: contar el número de líneas en las que haya archivos
        // con la extensión .odt y luego lo muestre por pantalla. El
        // directorio se pasa por parámetro.
        
        if (args.length!=1) { 
        // Sólo acepta un argumento de entrada, el nombre del archivo
            System.out.println("Error dal introducir el archivo a buscar.");
            System.exit(1);
        } 
        
        String directorio = args[0];
        int contador = 0;
        
        
        try {
            ProcessBuilder pb = new ProcessBuilder("ls", "-ln");
            pb.directory(new File(directorio));

            
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            
            
            String linea = br.readLine();
            while (linea!=null) {
                
                String[] campos = linea.split(" +");
                
                if (campos.length==9) {
                    String nombreArchivo = campos[8];
                    String[] nombreArchivoArray = nombreArchivo.split("\\.");
                    
                    if (nombreArchivoArray.length==2) {
                        if (nombreArchivoArray[1].equals("odt")) {
                            contador++;
                            System.out.println(campos[8]);
                        }
                    }
                    
                }
                
                linea = br.readLine();
                
            }
            
            System.out.println(contador);
            br.close();
            
            
        } catch (IOException ioe) {
            System.out.println("Excepción IOE");
        }

    }
}
