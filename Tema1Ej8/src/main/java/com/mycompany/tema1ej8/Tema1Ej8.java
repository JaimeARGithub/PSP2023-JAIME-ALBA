/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author jaime
 */
public class Tema1Ej8 {

    public static void main(String[] args) {
        Scanner TECLADO = new Scanner(System.in);
        System.out.println("Introduce un nombre de dominio.");
        String nombre = TECLADO.nextLine();
        
        while (!nombre.isEmpty() && !nombre.isBlank()) {
            ProcessBuilder pb = new ProcessBuilder("whois", nombre);
            try {
                Process p = pb.start();
                p.waitFor();
                InputStreamReader isr = new InputStreamReader (p.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String linea = br.readLine();
                
                while (linea!=null) {
                    System.out.println(linea);
                    linea = br.readLine();
                }
                
                
            } catch (IOException ioe) {
                System.out.println("Excepción de tipo IOE.");
            } catch (InterruptedException ie) {
                System.out.println("Excepción de tipo Interrupted.");
            }
            
            System.out.println("Introduce un nombre de dominio.");
            nombre = TECLADO.nextLine();
        }
        
        System.out.println("Proceso terminado.");
            
            
    }
}
