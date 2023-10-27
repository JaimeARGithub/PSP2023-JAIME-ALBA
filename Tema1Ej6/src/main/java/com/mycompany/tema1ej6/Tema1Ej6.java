/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author jaime
 */
public class Tema1Ej6 {

    public static void main(String[] args) {
        
        if (args.length!=2) { 
        // Sólo acepta dos argumentos de entrada, el archivo y el directorio
            System.out.println("Error; introducir un directorio de partida y un nombre de archivo a buscar");
            System.exit(0);
        } 
        
        String directorio = args[0];
        String archivo = args[1];
        
        // Sintaxis del find: find /home/Escritorio -name Salida.txt
        // El comando, desde dónde buscar y el nombre del archivo
        File dir = new File(directorio);
        
        // Posible validación: comprobar que el directorio existe
        // Crear variable File y .exists()
        if (!dir.isDirectory()) {
            System.out.println("El directorio introducido no existe.");
            System.exit(1);
        }
        
        
        ProcessBuilder pb = new ProcessBuilder("find", directorio, "-name", archivo);
        File fSalida = new File("Salida.txt");
        pb.redirectOutput(fSalida);
        
        try {
            Process p = pb.start();
            try {
                if (!p.waitFor(6, TimeUnit.SECONDS)) {
                    p.destroy();
                }
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            
            // Y mostrar por pantalla la salida del archivo.
            FileReader lector = new FileReader("Salida.txt");
            BufferedReader br = new BufferedReader(lector);
            
            // Y LA COSA DE LEER LÍNEA A LÍNEA.
            
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
