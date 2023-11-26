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
            System.out.println("Error, debe introducir tres parámetros: archivo de búsqueda, patrón y archivo de salida.");
            System.exit(0);
        }
        
        
        String dirBase = new String("/home/jaime/Descargas/");
        
        String entrada = new String(dirBase.concat(args[0]));
        String pattern = args[1]; // Recogemos el patrón de búsqueda
        String salida = new String(dirBase.concat(args[2])); // Recogemos el archivo de salida
        
        File archEnt = new File(entrada);
        
        if (!archEnt.exists() || archEnt.isDirectory()) {
            System.out.println("Error; el archivo no existe o es un directorio.");
            System.exit(1);
        }
        
        ProcessBuilder pb = new ProcessBuilder("grep", pattern, entrada);
        File fSalida = new File(salida);
        
        
        // En el caso de que quiera indicarle en qué directorio buscar:
        pb.directory(new File(dirBase));
        
        // Si nos vamos a andar moviendo de directorio, hay que tener en cuenta una
        // cosa: a la hora de crear objetos File en Java, el parámetro no es el nombre
        // del archivo, sino su ruta. Podemos crearnos una String con un cacho de
        // ruta que luego sumarle al nombre del archivo o podemos sencillamente
        // usarlo completo en todas partes.
        // Para nuestro caso, mejor lo de crearnos una String con el trozo de ruta.
        
        // String dirBase = new String("/home/jaime"); y concatenárselo a todos
        // los nombres de archivos
        // También se pasa como parámetro al pb.directory
        
        
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
