/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.probarhashmap;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStreamReader;
import java.io.File;

import java.util.HashMap;
// Trabajo con HashMap, pero necesito las otras dos
// El HashMap guarda la información, pero para trabajar con él, o no me da
// los métodos que quiero, o los que me ofrece no son operativos; en función
// de la operación a hacer, se convierte a estas dos estructuras y se trabaja
// con ellas
// Se transforma en ellas y es posible, por ejemplo, ordenarlo
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Map;

import java.util.Collection;
import java.util.Comparator;

import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author jaime
 */
public class ProbarHashMap {

    public static void main(String[] args) {
        // Declaro el HashMap y lo voy a usar para guardar datos de personas:
        // nombre de persona y edad
        // Clave: nombre, valor: edad
        // En el <> meto los tipos que van a tener la clave y el valor
        
        // Si el HashMap lo estoy construyendo mediante una estructura previa,
        // como un ArrayList, lo meto en el paréntesis
        // Si no le paso nada, está vacío
//        HashMap<String,Integer> personas = new HashMap<>();

        LinkedHashMap<String,Integer> personas = new LinkedHashMap<>();
        // La principal diferencia con un array y un array list es que la clave
        // y el valor pueden tener tipos diferentes
        
        // LA CLAVE NO PUEDE REPETIRSE, los valores sí.
        // Para guardar datos:
        personas.put("ZJuan", 55);
        personas.put("XLuis", 33);
        personas.put("APaquito", 44);
        
        // Al insertar un elemento, estoy insertando una entrada en el HashMap.
        // Cada par clave-valor se representa en el HashMap con un objeto 
        // de tipo Map.entry
        // Para acceder a los elementos del HashMap; si por ejemplo quiero ver
        // la edad de Juan
        personas.get("Juan"); // Devuélveme el valor para esa clave
        System.out.println(personas.get("Juan"));
        
        
        // Lo recorremos.
        // Para cada persona, recorremos una entrada que es de tipo Map.entry
        // Usamos un for-each
        // Voy iterando por cada uno de los pares clave-valor
        for(Map.Entry<String,Integer> e: personas.entrySet()) {
            System.out.println("Elemento con clave " + e.getKey() + " y valor " + e.getValue());
        }
        // No los ordena ni por clave, ni por valor, ni por orden de inserción
        
        
        // Una estructura parecida al HashMap que me va a ser más útil para
        // poder ordenarla va a ser el LinkedHashMap.
        // Leer un poco más arriba para encontrarlo.
        // Guarda y muestra los datos en el mismo orden en que los estoy insertando.
        // Si el orden de entrada importa, hay que usar LinkedHashMap sí o sí.
        
        
        
        // Cuestión: es muy frecuente que haya ordenar por clave o por valor.
        // Por ejemplo, ordenar la estructura anterior por nombre o por edad.
        // Sucede que no hay métodos en el HashMap que hagan ordenaciones por
        // clave y por valor.
        // Mediante la interface Collection se pueden ordenar colecciones.
        // Hay que instanciar un comparator y decirle que ordene por uno de
        // los elementos. La forma tradicional es ésta.
        
        
        // Formas más intuitivas: primero, ordenar por clave. Convertir el
        // HashMap o el LinkedMap en un TreeMap.
        // Un TreeMap es una estructura que, por defecto, ORDENA POR CLAVE.
        // Igual que LinkedHashMap ordena por defecto por orden de entrada,
        // TreeMap ordena por defecto por clave. Hay que convertir el HashMap
        // o el LinkedHashMap en un TreeMap.
        TreeMap<String,Integer> persOrd = new TreeMap<>(personas);
        // Si quiero guardarlo por clave invertida:
//        TreeMap<String,Integer> persOrd = new TreeMap<>(Collection.reverseOrder());
//        persOrd.putAll(personas);
        
        


        // HashMap los guarda al azar, y LinkedHashMap los guarda por orden de
        // llegada; en principio, no puedo crear un LinkedHashMap a partir de
        // un HashMap
        System.out.println("Ordenación del TreeMap:");
        for(Map.Entry<String,Integer> e: persOrd.entrySet()) {
            System.out.println("Elemento con clave " + e.getKey() + " y valor " + e.getValue());
        }
        
        
        // La opción restante es ordenar por valor.
        // Hay que convertir el HashMap o LinkedHashMap a ArrayList, el
        // ArrayList a List y entonces ordenarlo con el método sort.
        List<Entry<String,Integer>> personasLista = new ArrayList<>(personas.entrySet());
        personasLista.sort(Entry.comparingByValue());
        
        System.out.println("Ordenación de la lista comparando por valor:");
        for(Entry<String,Integer> e: personasLista) {
            System.out.println("Elemento con clave " + e.getKey() + " y valor " + e.getValue());
        }
        
        
        // Si lo que quiero es ordenar descendente: la interfaz Comparator tiene
        // un método que permite ordenar en orden descendente
        personasLista.sort(Entry.comparingByValue(Comparator.reverseOrder()));
        System.out.println("Ordenación de la lista comparando por valor invertido:");
        for(Entry<String,Integer> e: personasLista) {
            System.out.println("Elemento con clave " + e.getKey() + " y valor " + e.getValue());
        }
    }
}
