/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pruebasencuesta;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author jaime
 */
public class PruebasEncuesta {

    public static void main(String[] args) {
        int numZonas = 20;
        int numOpciones = 9;
        
        HashMap<String,Integer> hashZonas = new HashMap<String,Integer>();
        HashMap<String,Integer> hashOpc = new HashMap<String,Integer>();
        
        for (int i=0; i<numZonas; i++) {
            hashZonas.put("ZONA"+(i+1), 0);
        }
        
        for (int i=0; i<numOpciones; i++) {
            hashOpc.put("OPCION"+(i+1), 0);
        }
        
        Respuestas r = new Respuestas(hashZonas, hashOpc);
        
        
        System.out.println("EMPIEZAN LOS ENCUESTADORES.");
        System.out.println("");
        System.out.println("//////////////////////////////");
        System.out.println("/////////////////////////////");
        System.out.println("");
        Thread[] hilos = new Thread[numZonas];
        for (int i=0; i<numZonas; i++) {
            hilos[i] = new Thread(new Encuestador(r, (i+1)));
            hilos[i].start();
        }
        
        for (int i=0; i<numZonas; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException E) {
                System.out.println(E);
            }
        }
        
        
        HashMap<String,Integer> datosZonas = r.getRespZona();
        HashMap<String,Integer> datosOpc = r.getRespOpc();
        
        System.out.println("RESULTADOS.");
        System.out.println("");
        System.out.println("//////////////////////////////");
        System.out.println("/////////////////////////////");
        System.out.println("");
        
        for (int i=0; i<numZonas; i++) {
            System.out.println("El encuestador ZONA" + (i+1) + " ha entrevistado a " + datosZonas.get("ZONA"+(i+1)) + " personas.");
        }
        
        System.out.println("");
        System.out.println("//////////////////////////////");
        System.out.println("/////////////////////////////");
        System.out.println("");
        
        for (int i=0; i<numOpciones; i++) {
            System.out.println("La respuesta OPCION" + (i+1) + " se ha obtenido " + datosOpc.get("OPCION"+(i+1)) + " veces.");
        }
    }
}

class Encuestador implements Runnable {
    private final Respuestas respuestas;
    private String identificador;
    
    public Encuestador (Respuestas resp, int ident) {
        this.respuestas = resp;
        this.identificador = "ZONA"+ident;
    }
    
    @Override
    public void run() {
        Random r = new Random();
        int numPersonas = r.nextInt(100, 301);
        System.out.println("El encuestador " + identificador + " va a encuestar a " + numPersonas + " personas.");
        int opc;
        
        for (int i=0; i<numPersonas; i++) {
            opc = r.nextInt(0,10);
            
            if (opc != 0) {
                respuestas.anotaRespuesta(identificador, "OPCION"+opc);
            }
        }
    }
    
}

class Respuestas {
    private HashMap<String,Integer> respZona;
    private HashMap<String,Integer> respOpc;
    
    public Respuestas(HashMap<String,Integer> hashZonas, HashMap<String,Integer> hashOpc) {
        this.respZona = hashZonas;
        this.respOpc = hashOpc;
    }
    
    public synchronized void anotaRespuesta(String zona, String opcion) {
        respZona.put(zona, respZona.get(zona)+1);
        respOpc.put(opcion, respOpc.get(opcion)+1);
    }
    
    public HashMap<String,Integer> getRespZona() {
        return this.respZona;
    }
    
    public HashMap<String,Integer> getRespOpc() {
        return this.respOpc;
    }
}
