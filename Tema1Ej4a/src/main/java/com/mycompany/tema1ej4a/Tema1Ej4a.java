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
            System.out.println("Error; introducir un único nombre de usuario.");
            System.exit(1);
        }
        
        // Para validar que un usuario existe, hay un archvo en el que
        // están todos los usuarios del sistema.
        // /etc/passwd
        // Compruebo que el usuario existe ahí para validarlo.
        
        // Meter cat /etc/passwd devuelve por salida los usuarios y su info
        // Si quiero verificar que un usuario existe en Java, puedo hacer un
        // split con ":" y compruebo que el nombre del usuario a validar
        // coincide con alguna de las columnas 0.
        
        
        String nombre = args[0];
        
        // ps por sí solo no me muestra a los usuarios
        ProcessBuilder pb = new ProcessBuilder("ps", "-ef");
        
        try {
            
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            
            String linea = br.readLine();
            String[] campos;
            
            System.out.println("Procesos para el usuario "+nombre+":");
            System.out.println("");
            System.out.println("");
            while (linea!=null) {
                campos = linea.split(" +");
                    
                    // No meto el check de que el array tenga 8 elementos
                    // porque algunos tienen espacios en el nombre y salen
                    // con un tamaño mayor que 8
                    
                    // Una solución sería crearme una función que imprimiese
                    // todos los campos desde el 7 hasta el final, al
                    // margen de su tamaño
                    
                    // Busco que el campo 0 coincida con el nombre del usuario
                    if (campos[0].equals(nombre)) {
                        System.out.println(campos[7]);
                    }
                
                linea = br.readLine();
            }
            
            br.close();
            
            
            System.out.println("Programa principal terminado.");
            
        } catch (IOException ioe) {
            System.out.println("Excepción IOE");
        }
        
    }
}
