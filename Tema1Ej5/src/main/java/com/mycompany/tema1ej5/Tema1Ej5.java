/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tema1ej5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

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
        // Si quiero validar una IP: vengo que mirar que la cadena tenga
        // CUATRO valores, y que todos estén entre 0 y 255
        
        // La validación va más allá, según rangos (A, B, C, D) hay
        // direcciones que no se pueden usar, pero no vamos a hilar
        // tan fino
        
        // Existe el ping 8.8.8.8 -c 3 para que pare a la cuenta de 3 pings.
        // NO ES LO MISMO que parar a los cinco segundos.
        
        // Para detenerlo a los cinco segundos: método de la clase Process
        // llamado waitFor.
        ProcessBuilder pb = new ProcessBuilder("ping", ip);
        double tiempoTotal = 0.0;
        double contador = 0.0; // Contador para el nº de respuestas recibidas
        double avg;

            
        try {
            // Problemita: PRIMERO ME HE CARGADO EL PROCESO.
            // Y está jodido procesar la salida tras eso.
            // Ñapa para solventarlo: redireccionar la salida
            // a un archivo.
            
            
            // Ñapa: redireccionar la salida del proceso.
            // La redirección se hace con el ProcessBuilder; crear el
            // proceso a ejecutar y redireccionar la salida.
            // El proceso en sí se usa para esperas y para matar el proceso.
            File f = new File("Salida.txt");
            pb.redirectOutput(f);


            // Inicio el proceso.
            Process p = pb.start();
            
            
            // Hay que rodear el método con un try-catch porque puede dar una 
            // excepción de interrupción
            // Controlo la excepción y le digo que espere cinco segundos.
            
            // EJECUTO DURANTE CINCO SEGUNDOS Y, SI SIGUE
            // FUNCIONANDO TRAS ESOS CINCO SEGUNDOS, LO MATO.
            // Devuelve falso si se ha ejecutado pero no se ha
            // llegado a parar a los cinco segundos.

            try {    
                if (!p.waitFor(5, TimeUnit.SECONDS)) { // Le digo que espere durante cinco segundos y pare
                    p.destroy(); // Elimino el proceso
                } 
            } catch (InterruptedException ie) {
                System.out.println("Excepción por interrupción");
            }

            // En este caso estoy pasando texto raw; en lugar de InputStreamReader,
            // tengo que usar FileReader al que le paso el archivo al que he
            // redireccionado la salida
            FileReader lector = new FileReader(f);
            BufferedReader br = new BufferedReader(lector);
            
            
            // LA PRIMERA LÍNEA ES DISTINTA, HAY QUE CONTROLARLO
            // Controlarlo por el número de campos.
            String linea = br.readLine();
            String[] campos;
            String[] campos2;
            
            while (linea != null) {
                campos = linea.split(" +"); // Para separar por espacios
                
                if (campos.length==8) {
                    campos2 = campos[6].split("="); // Para separar lo resultante por el =
                      
                    // El necesario es el segundo elemento, el tiempo
                    // e irlo acumulando
                    tiempoTotal += Double.parseDouble(campos2[1]);
                
                
                    contador++;
                }
                
                
                linea = br.readLine();
            }
            br.close();
            
            avg = tiempoTotal / contador;
            System.out.println("El tiempo medio de respuesta de la IP "+ip+" es de "+avg+" milisegundos.");
            
            
        } catch (IOException ioe) {
            System.out.println("Excepción de ioe");
        } 
            
            
        }      
    }

