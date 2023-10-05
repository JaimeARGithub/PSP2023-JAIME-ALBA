/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jaime
 */
public class Tema1Ej5 {

    public static void main(String[] args) {
        if (args.length!=1) { 
        // Sólo acepta un argumento de entrada, la IP
            System.out.println("Error; introducir una única IP.");
            System.exit(1);
        } 
        
        String ip = args[0];
        ProcessBuilder pb = new ProcessBuilder("ping", ip, "-w", "5");
        Double tiempoTotal = 0.0;
        Double contador = 0.0;
        Double avg;
        int cuentaLíneas=0;
        
        
        
        try {
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            
            String linea = br.readLine();
            while (linea != null) {
                cuentaLíneas++;
                linea = br.readLine();
            }    
            br.close();
            
        } catch (IOException ioe) {
            System.out.println("Excepción de ioe");
        } 
        
        
        if (cuentaLíneas == 1) {
            System.out.println("Error. No se ha recibido respuesta.");
        } else {
            
            try {
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            
            String linea = br.readLine();
            String campos[];
            while (linea != null) {
                campos = linea.split(" |=");
                
                if (campos.length==11) {
                    contador++;
                    System.out.println(contador+" --- "+campos[campos.length-2]);
                    tiempoTotal = tiempoTotal + Double.parseDouble(campos[campos.length-2]);
                }
                
                
                linea = br.readLine();
            }
            br.close();
            
            avg = tiempoTotal / contador;
            System.out.println("El tiempo medio de respuesta es de "+avg+" milisegundos.");
            
            
            } catch (IOException ioe) {
                System.out.println("Excepción de ioe");
            } 
            
            
        }      
    }
}
