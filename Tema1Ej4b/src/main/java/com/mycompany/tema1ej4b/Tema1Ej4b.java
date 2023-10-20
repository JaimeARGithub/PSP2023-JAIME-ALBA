/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej4b;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author jaime
 */
public class Tema1Ej4b {

    public static void main(String[] args) {
        if (args.length!=1) { 
        // Sólo acepta un argumento de entrada, el nº del campo
            System.out.println("Error; introducir un único nombre de usuario.");
            System.exit(0);
        }
        
        // Como parámetro, vamos a pasar la posición del campo
        // La salida tiene 8 campos; el usuario debe introducir un
        // número entre el 1 y el 8 y nosotros lo aplicamos a 0 a 7
        int numCampo = Integer.parseInt(args[0]);
        if (numCampo<1 || numCampo>8) {
            System.out.println("El número de campo debe estar comprendido entre 1 y 8.");
            System.exit(1);
        }
        
        
        ProcessBuilder pb = new ProcessBuilder("ps", "-ef");
        
        
        try {
            
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            
            
            String linea;
            String[] campos;
            // Habría que ver la salida ordenada por el campo que yo le diga
            // Como la clave no se puede repetir, la línea completa va a ser la
            // clave, el campo por el que quiero ordenar va a ser el valor y
            // ordeno por valor
            
            // Me declaro un HashMap para guardar líneas como clave y, como valor,
            // el campo por el que voy a ordenar
            HashMap<String,String> mapLine = new HashMap<>();
            
            
            
            while ((linea=br.readLine()) != null) {
                campos = linea.split(" +");
                    
                // Rellenamos el HashMap con entradas; la línea completa que vaya
                // a leer como clave y el campo por el que se ordena como valor
                mapLine.put(linea, campos[numCampo-1]);
                
            }
            
            br.close();
            // Ordenamos el HashMap por valor (campo elegido)
            List<Entry<String,String>> mapLineOrdenado = new ArrayList<>(mapLine.entrySet());
            mapLineOrdenado.sort(Entry.comparingByValue());
            
            
            for (Entry<String,String> e:mapLineOrdenado) {
                System.out.println(e.getKey()+"----"+e.getValue());
            }
            
            
        } catch (IOException ioe) {
            System.out.println("Excepción IOE");
        }
    }
}
