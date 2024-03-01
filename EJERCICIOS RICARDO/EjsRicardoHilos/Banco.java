/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tema2hilos;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;



/**
 *
 * @author joaquin
 */

class Cuenta
{
   private int Saldo;   //Saldo de la cuenta
   private int Max;     //Maximo permitido

   public Cuenta(int Saldo,int Max)
   {
      this.Max=Max;
      this.Saldo=Saldo;
   
   }

   public synchronized void Ingresar(int Cant)
   {
      if  ( (this.Saldo+Cant)<=this.Max )   //Si se permite el ingreso
      {
           this.Saldo+=Cant;
           
           System.out.println("El depositario ingresa "+Cant+" Saldo actual:"+this.Saldo);
      }   
      else  //Indicamos que estamos en el límite de saldo
      {
           System.out.println("Ingreso no permitido, saldo al límite:"+this.Saldo);
      }    
      
   
   }
   
   public synchronized void Retirar(int Cant)
   {
      if  ( (this.Saldo-Cant)>=0 )   //Si hay saldo suficiente
      {
           this.Saldo-=Cant;   //Decrementamos la cantidad
           
           System.out.println("El cobrador retira  "+Cant+" Saldo actual:"+this.Saldo);
      }   
      else  //Indicamos que estamos en el límite de saldo
      {
           System.out.println("Cobro no permitido, saldo insuficiente:"+this.Saldo);
      }    
      
   
   }
   
   public int getSaldo()
   {
   
      return this.Saldo;
   }
  
}        
        

class Depositario implements Runnable 
{
    
    private final Cuenta c;
    
    private int EsperaT ;
    
    public Depositario(Cuenta c,int EsperaT)
    {
        this.c=c;
        this.EsperaT=EsperaT;
        
    }
   
    @Override
  public void run() 
  {
      
      int MaxEsperaDep=90;    //Fijamos la espera máxima en 90 segundos
      
      while ( EsperaT< MaxEsperaDep )         //Mientras no llevemos 90 segundos de espera
      {
           c.Ingresar(500);    //Ingresamos 500
          
          try {
             
               Thread.sleep(2000);    //Esperamos 2 seg
               EsperaT+=2;               //Incrementamos la espera dos segundos
          } catch (InterruptedException ex) {
              Logger.getLogger(Depositario.class.getName()).log(Level.SEVERE, null, ex);
          }
      
      }
      
    
  }  

}        

class Cobrador implements Runnable 
{
    
    private final Cuenta c;
    
    private int CobradoTot ;   //Cantidad total que lleva cobrada
    
    public Cobrador(Cuenta c,int CobradoTot)
    {
        this.c=c;
        this.CobradoTot=CobradoTot;
        
    }
   
    @Override
  public void run() 
  {
      
      while ( CobradoTot<6000 )         //Mientras no llevemos 90 segundos de espera
      {
           c.Retirar(300);
          
          try {
             
               Thread.sleep(3000);    //Esperamos 3 seg
               
               CobradoTot+=300;  //Incremen
               
          } catch (InterruptedException ex) {
              Logger.getLogger(Depositario.class.getName()).log(Level.SEVERE, null, ex);
          }
      
      }
      
    
  }  

}        


public class Banco {
    
    public static void main(String[] args) 
    {
        int MaxCuenta= 5000;  //Cantidad Máxima que puede haber en la cuenta
        
        
        
        Cuenta c = new Cuenta(0,MaxCuenta  );   //Creamos la instancia de la cuenta
        
        System.out.println("---------Programa principal iniciado ------------------------  ");
        
        System.out.println();
        
        
        Thread cobrad= new Thread(new Cobrador(c,0) );    //Creamos una hilo cobrador
        
        Thread deposi= new Thread(new Depositario(c,0) );    //Creamos una hilo despositario
        
        //Lanzamos los hilos cobrador y depositario
        
        cobrad.start();     
        
        deposi.start();
        
        //Hacemos que el principal espere a los hilos
        
        try {
            cobrad.join();
            deposi.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        System.out.println();
        
        System.out.println("---------Programa principal concluido ------------------------  ");
        
        System.out.println("---------El saldo final es:    "+c.getSaldo()  );
        
   
    } 
    
}
