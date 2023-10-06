/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej4b;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jaime
 */
public class Tema1Ej4b {

    public static void main(String[] args) {
        
        if (args.length!=1) { 
            // Para simplificarlo, en lugar de un nombre metemos
            // nº de campo
            // Ejemplo: ordenar por campo 0 (númerom de usuario)
            System.out.println("Error; introducir un único número de campo.");
            System.exit(1);
        }
        // Necesito una estructura que me permita tener el campo como parámetro
        // de ordenación, y por otro lado, la línea entera (la String)
        // Permite ordenar por clave o por valor.
        
        // Estructura Java: HashMap. Clave-Valor. Ejemplo:
        // HashMap<String, String> Lineas
        // Lineas{root] = "root 1, 8, ..."
        // Lineas[joaquin] = "joaquin 2, 4, ..."
        
        // Problema: hay campos que NO son únicos.
        // El HashMap tiene clave y valor; el valor se puede repetir,
        // pero la clave no.
        
        // Lineas[1] = "..."
        // Meto como clave el campo 1 (el PID, no se repite) y como valor
        // la línea entera -> Lineas.sort();
        
        // Meto la línea como clave (las líneas son únicas, no se repiten)
        // y el valor va a ser el nombre del usuario; a la hora de ordenar el
        // HashMap, puedo ordenar por CLAVE o por VALOR.
        // Lo que meta como clave NUNCA puede repetirse.
        // En las claves meto las líneas completas, que NO se repiten, y en
        // valor meto los nombres de usuario, que SÍ se repiten. Y ordeno
        // por valor.
        
        // HashMap implementa un método nativo para ordenar por clave, pero
        // no por valor.
        // Dice que lo va a subir él, pero intentar hacerlo yo por mi cuenta.
        
        
        
        String nombre = args[0];
        
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
