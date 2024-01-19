/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.encuesta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author jaime
 */

/*
En clase: hay que hacer una encuesta. Un hilo hace una serie de preguntas y anota las respuestas.

Hay que usar el random; queremos que no todos los hilos entren a la vez a hacer lo mismo, para indicarles
que esperen un tiempo y hagan algo diferente.

Cosas a usar:
-thread.sleep para retenerlos
-random para elegir una opción de las posibles
-definir las estructuras de datos que en la clase compartida vamos a necesitar para almacenar la información:
vbles individuales, arraylists, hashmaps... en el ejemplo anterior tenía que controlar que el número no
estuviera ya dicho, para que entrase bien por la condición

Hay que hacer un hilo que, con un número al azar entre 100 y 300 (nº de entrevistados, con un random en ese
rango); el nº de preguntas totales será el nº de personas entrevistadas.

Hay 20 zonas, en cada zona hay un encuestador, cada encuestador va a hacer entre 100 y 300 preguntas.
Respuestas: respuesta_0, respuesta_1... random entre 0 y 10 para encontrar la respuesta que se va a dar.
Las respuestas las guardo en formatos string. Concateno string con el número.
Hay que contabilizar cada respuesta.

Ejemplo: hilo1 va a entrevistar a 180 personas. Ya sabemos que el total de respuestas 


En total: contabilizar las respuestas totales que se dan en cada zona y el nº de respuestas por opción.
Dos hashmaps: uno guarda el total de respuestas dadas (en todas las zonas) y otro guarda cuánta gente
ha respondido en cada zona (la respuesta 0 equivale a no haber respondido; el total de respuestas dadas
vale menos que el total de gente preguntada.)

Sincronizar los métodos correspondientes.
*/

public class Encuesta {

    public static void main(String[] args) {
        
        int NumeroZonas = 20; // nº de zonas, a cada una se le asigna un encuestador
        int NumeroRespuestas = 9; // nº respuestas, las válidas son del 1 al 9
        
        HashMap<Integer, Integer> RespZona = new HashMap<Integer, Integer>();
        // El HashMap de respuestas por opción va a tener como claves del 1 al 9
        HashMap<String, Integer> RespOpc = new HashMap<String, Integer>();
        
        
        // Inicializamos el HashMap de las zonas
        for (int i=1; i<=NumeroZonas; i++) {
            RespZona.put(i, 0);
        }
        
        String resp = "respuesta_"; // todas las respuestas se representan con una string + la opción respondida
        // Inicializamos el HashMap de las respuestas
        for (int i=1; i<=NumeroRespuestas; i++) {
            RespOpc.put(resp+i, 0);
        }
        
        
        // Ahora hay que instanciar el recurso compartido y pasarle los HashMaps inicializados
        Respuestas Resp = new Respuestas(RespZona, RespOpc);
        

        // Finalmente, creamos un array de hilos, uno por cada zona
        Thread[] hilos = new Thread[NumeroZonas];
        
        for (int i=0; i<NumeroZonas; i++) {
        
            Encuestador Enc = new Encuestador((i+1), Resp);
            
            hilos[i] = new Thread(Enc); 
            hilos[i].start();
        }
        
        
        // Indicamos al programa principal que espere a que termine la ejecución de los hilos
        for (int i=0; i<NumeroZonas; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                System.out.println("Excepción de tipo interrupted");
            }
        }
       
        System.out.println("Se acabó");
        
        
        // Para terminar, mostramos los resultados de la encuesta
        System.out.println("----------Resultados por zona----------");
        for (int i=0; i<NumeroZonas; i++) {
            System.out.println("Para la zona " +(i+1)+ " se han obtenido " +Resp.getRespZona().get(i)+ " respuestas.");
        }
        
        
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        
        System.out.println("----------Resultados por respuesta----------");
        for (int i=0; i<9; i++) {
            System.out.println("La respuesta número "+(i+1)+" se ha recibido un total de "+Resp.getRespOpc().get("respuesta_"+i)+" veces." );
        }
    }
}


// Cada hilo es un encuestador
class Encuestador implements Runnable {
    // Datos: nº zona asignada y la instancia del recurso compartido con la que va a interactuar
    private int NumZona; 
    private final Respuestas Res;
    
    public Encuestador(int NumZona, Respuestas Res) {
        this.NumZona = NumZona;
        this.Res = Res;
    }
    
    
    @Override
    public void run() {
        int CantEncuestados; // Variable que registra el número de gente a la que vamos a preguntar
        Random r = new Random();
        CantEncuestados = 100 + r.nextInt(200);
        // Si todos fuesen a responder del 1 al 9, le diría que cantidad de respuestas para
        // la zona tal = buscar la cantidad de personas a las que se ha preguntado
        // No será así; se contempla la opción 0 como que no se haya dado una respeusta
        
        
        // A la entrada del bucle tengo que elegir una opción entre 0 y 10, si la opción es
        // 0 no se registra como respuesta dada
        System.out.println("Encuestador en la zona " + this.NumZona + " empieza.");
        int Nres; // indicador del número de respuestas que me van a dar
        
        
        // A esa cantidad de encuestados le voy a preguntar; un for
        for (int i=0; i<CantEncuestados; i++) {
            // Para cada uno de esos encuestados, preguntamos
            Nres = r.nextInt(10); // indicamos que elija un número del 0 al 9
            
            if (Nres > 0) { // una respuesta mayor que 0 indica que nos han querido responder algo
                            // si no, el intento no se anota
                            
                // En el HashMap buscamos el elemento cuya clave (string) coincide con la
                // respuesta y anotarla ahí
                Res.AnotaRespuesta(this.NumZona, "respuesta_"+Nres);
            }
        }
                
        System.out.println("Encuestador en la zona " + this.NumZona + " termina. Hemos preguntado a " +CantEncuestados+" personas.");
    }
    
}

// Clase recurso compartido: va a guardar las respuestas por zona
// y las respuestas por opción
class Respuestas{
    // En el primero guardamos el número de la zona y las respuestas por zona
    // Zona 1 tiene X respuestas, zona 2 tiene Y respuestas...
    // La clave es la zona, el valor es la cantidad de respuestas dadas en esa zona
    private HashMap<Integer, Integer> RespZona;
    
    // HashMap para almacenar la cantidad de respuestas dadas para cada opción
    // Respuesta1 tiene X veces usada, Respuesta2 tiene Y veces usada...
    private HashMap<String, Integer> RespOpc;
    
    
    
    // En el principal hay que rellenar el HashMap con un bucle
    // Inicializarlo: meter las 20 zonas y las 10 respuestas en las claves, pero dejar las respuestas a cero
    
    public Respuestas(HashMap RespZona, HashMap RespOpc) { // Pasamos como parámetros los hashmaps de los atributos
        this.RespZona = RespZona;
        this.RespOpc = RespOpc;
    }
    
    
    public synchronized void AnotaRespuesta(int zona, String respuesta) {
        // Para la zona que estemos indicando:
        // -Accedemos al valor almacenado de respuestas totales EN ESA ZONA
        // -Sumamos 1 y lo sobreescribimos
        int valorZ = RespZona.get(zona);
        RespZona.put(zona, valorZ++);
        
        
        // Para la respuesta que estemos indicando:
        // -Accedemos al valor almacenado de respuestas totales PARA ESA RESPUESTA
        // -Sumamos 1 y lo sobreescribimos
        int valorR = RespOpc.get(respuesta);
        RespOpc.put(respuesta, valorR++);
    }

    
    public HashMap<Integer, Integer> getRespZona() {
        return RespZona;
    }

    public HashMap<String, Integer> getRespOpc() {
        return RespOpc;
    }
}

/**
 * Siguiente ejercicio: realizar un programa que realiza una carrera de coches
 * --clase carrera: al crearse se indica la distancia a recorrer entre 100 y 1000 metros, termina
 * cuando un coche llega a la meta (recurso compartido), va a tener una distancia y un ganador
 * 
 * --coches: los hilos, van a avanzar, tienen nombre, nº metros que avanzan (entre 1 y 50), por cada
 * iteración los coches avanzan los metros aleatorios que les salgan, cuando un coche llegue a la 
 * distancia hay que poner que ha ganado y decir ha ganado el coche tal
 * 
 * --el programa principal crea los coches, 3: Ford, Opel y Seat
 * --al acabar hay que mostrar el podio: primero, segundo y tercero, según distancia recorrida cada uno
 */
