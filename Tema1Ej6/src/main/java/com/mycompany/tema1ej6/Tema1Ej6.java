/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej6;

import java.io.BufferedReader;
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
            System.out.println("Error; introducir un directorio y un nombre de archivo");
            System.exit(1);
        } 
        
        String archivo = args[0];
        String directorio = args[1];
        
        // Ésto es de la IA
        AtomicBoolean detenido = new AtomicBoolean(false);
        
        ProcessBuilder pb = new ProcessBuilder("find", directorio, "-name", archivo);
        try {
            Process p = pb.start();
            
            // Ésto también
            ScheduledExecutorService gestor = Executors.newScheduledThreadPool(1);
            gestor.schedule(() -> {
                if (p.isAlive()) {
                    p.destroy();
                    detenido.set(true);
                }
            }, 6, TimeUnit.SECONDS);
            p.waitFor();   
        } catch (IOException ioe) {
            System.out.println("Excepción de tipo IOE");
        } catch (InterruptedException ioe) {
            System.out.println("Excepción de tipo Interrupted");
        }
        
        
        if (detenido.get()) {
            System.out.println("Han pasado seis segundos; proceso detenido.");
        } else {
            
            pb = new ProcessBuilder("find", directorio, "-name", archivo);
            try {
                Process p = pb.start();
                InputStreamReader isr = new InputStreamReader(p.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String linea = br.readLine();
                while (linea!=null) {
                    System.out.println(linea);
                    linea = br.readLine();
                }
                
                
            } catch (IOException ioe) {
                System.out.println("Excepción de tipo IOE");
            } 
            
            
        }
    }
}
