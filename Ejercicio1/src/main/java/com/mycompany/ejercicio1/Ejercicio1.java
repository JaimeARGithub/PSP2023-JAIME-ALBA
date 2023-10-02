/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejercicio1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author jaime
 */
public class Ejercicio1 {
    // Buscar archivo. Introduzco por parámetro del nombre de un archivo
    // y me dice si existe en el directorio actual.

    public static void main(String[] args) {
        
        // Para cambiar los argumentos: click derecho en la carpeta raíz del
        // proyecto, properties, run y en argumentos meto el nombre del archivo
        
        
        // Primero controlo el número de parámetros.
        // Los parámetros se pasan por línea de comandos.
        
        
        if (args.length!=1) { 
        // Sólo acepta un argumento de entrada, el nombre del archivo
            System.out.println("Error dal introducir el archivo a buscar.");
            System.exit(1);
        } 
        
        String nombre = args[0];
        
        
        boolean existe = false;
        
        try {
            ProcessBuilder pb = new ProcessBuilder("ls", "-ln");
            
            // Si quiero que inicie la búsqueda en un directorio de donde
            // tiene los .class, que es donde empieza:
            pb.directory(new File("/home/jaime"));
            // Se va a usar con frecuencia, para hacer búsquedas en directorios
            // que no sean el del proyecto.
            
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            
            
            String linea = br.readLine();
            
            
            
            while (linea!=null && !existe) {
                
                if (linea.contains(nombre)) {
                    System.out.println(linea);
                    existe = true;
                }
                linea = br.readLine();
                
            }
            
            br.close();
            
            
        } catch (IOException ioe) {
            System.out.println("Excepción IOE");
        }
        
        if (existe) {
            System.out.println("El archivo existe en ese directorio.");
        } else {
            System.out.println("El archivo no existe en ese directorio.");
        }
    }
}
