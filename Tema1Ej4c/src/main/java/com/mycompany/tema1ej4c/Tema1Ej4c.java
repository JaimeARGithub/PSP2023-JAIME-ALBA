/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej4c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author jaime
 */
public class Tema1Ej4c {

    public static void main(String[] args) throws InterruptedException {
        if (args.length!=1) { 
        // Sólo acepta un argumento de entrada, el nombre del archivo
            System.out.println("Error; introducir un único nombre de usuario.");
            System.exit(1);
        } 
        
        String nombre = args[0];
        double memoria = 0.0;
        
        // Se te puede pedir que el nombre de usuario sea un nombre de usuario válido.
        
        // Comando cat /etc/passwd: contiene los usuarios del sistema
        // La primera columna contiene los usuarios
        // Comando completo para ver solamente los usuarios:
        // cat /etc/passwd | cut -d ":" -f1 | sort
        
        // No es necesario, es pillar las strings de cat /etc/passwd línea por
        // línea, separar en columnas, quedarme con la primera y comprobar que
        // el nombre del usuario coincide con alguno de los valores
        
        
        ProcessBuilder pb = new ProcessBuilder("ps", "aux");
        
        try {
            
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            
            String linea = br.readLine();
            String[] campos;
            while (linea!=null) {
                campos = linea.split(" +");
                if (campos.length==11) {
                    if (nombre.equals(campos[0])) {
                        memoria+=Double.parseDouble(campos[3]);
                    }
                }
                
                
                linea = br.readLine();
            }
            br.close();
            
            
        } catch (IOException ioe) {
            System.out.println("Excepción de tipo IOE");
        }
        
        System.out.printf("El usuario %s está ocupando %.2f memoria",nombre,memoria);
    }
}
