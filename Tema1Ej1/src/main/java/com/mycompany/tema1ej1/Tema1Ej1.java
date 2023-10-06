/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jaime
 */
public class Tema1Ej1 {

    public static void main(String[] args) {
        // Versión 1: heredando entrada-salida del programa principal
        ProcessBuilder pb = new ProcessBuilder("ls");
        
        // El comando se ejecuta desde la carpeta del proyecto,
        // pero puedo modificarla
        pb.directory(new File("/home/jaime"));
        
        pb.inheritIO(); // Hace que el proceso hijo comparta la E/S con el padre
        // No se controla la salida, directamente la vuelca en pantalla
        
        // El programa principal tiene como entrada el teclado y como salida
        // la pantalla, pero el proceso hijo no tiene por qué; puedo indicarle
        // que herede del padre; le indico que todas sus instancias la hereden
        
        
        // ProcessBuilder permite crear procesos independientes, pero crear
        // un proceso no quiere decir que haya hecho nada aún
        // Hasta que no lo lance, aún no he hecho nada. Primero lo lanzo,
        // lo ejecuto, y cuando invoco el método .start() es cuando se
        // crea el proceso.
        
        
        // ProcessBuilder es la plantilla que me permite crear procesos,
        // y Process son los procesos que voy creando
        
        
        
        try {
            pb.start();
        } catch (IOException ioe) {
            System.out.println("Excepción IOE");
        }
        
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("Cambio de versión");
        System.out.println(" ");
        System.out.println(" ");
        
        
        // Versión 2: empleando el método getInputStream
        // Lo suyo es guardarlos en ejercicios separados
        ProcessBuilder pb2 = new ProcessBuilder("ls");
        pb2.directory(new File("/home/jaime"));
        try {
            
            Process p2 = pb2.start();
            // InputStreamReader isr = new InputStreamReader (canal de entrada))
            // La entrada debería ser la salida del proceso, pero es Input en
            // lugar de Output. Sí, es contraintuitivo.
            InputStreamReader isr = new InputStreamReader(p2.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String linea = br.readLine();
            
            
            // while (  (linea = br.readLine() )!=null   )
            while (linea != null) {
                System.out.println(linea);
                linea = br.readLine();
            }
            br.close();
            
            
        } catch (IOException ioe) {
            System.out.println("Excepción de tipo IOE");
        }
        
    }
}
