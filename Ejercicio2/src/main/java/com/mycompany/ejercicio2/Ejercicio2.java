/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejercicio2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jaime
 */
public class Ejercicio2 {

    public static void main(String[] args) {
        
        // Modificaciones en el ejercicio: busca el archivo en el directorio
        // indicado en el código, pero lo hace convirtiendo cada línea que
        // lee en un array de Strings separada por espacios, buscamos el nombre
        // en la columna correspondiente según la cantidad de campos que haya
        // y, una vez localizado un campo cuyo nombre coincida, imprime la
        // línea completa
        
        if (args.length!=1) { 
        // Sólo acepta un argumento de entrada, el nombre del archivo
            System.out.println("Error dal introducir el archivo a buscar.");
            System.exit(1);
        } 
        
        String nombre = args[0];
        boolean encontrado = false;

        
        try {
            ProcessBuilder pb = new ProcessBuilder("ls", "-ln");
            pb.directory(new File("/home/jaime/Descargas"));

            
            
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            
            
            String linea = br.readLine();
            

            while (linea!=null && !encontrado) {
                
                String[] campos = linea.split(" +");
                if (campos.length==9) {
                    if (campos[8].contains(nombre)) {
                        encontrado=true;
                        System.out.println(linea);
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
