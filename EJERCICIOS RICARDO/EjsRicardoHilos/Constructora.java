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

class Almacen
{
   private int NumLadrillos;       // Numero de ladrillos actual en el almacen
   private int MaxLadrillos ;      //Maximo de ladrillos permitido en el almacen

   public Almacen(int NumLadrillos,int MaxLadrillos)
   {
      this.NumLadrillos=NumLadrillos;
      this.MaxLadrillos=MaxLadrillos;
   
   }

   public synchronized void Guardar(int Cant)
   {
      if  ( (this.NumLadrillos+Cant)<=this.MaxLadrillos )   //Si se permite gurdar esa cantidad
      {
           this.NumLadrillos+=Cant;
           
           System.out.println("Se han guardado "+Cant+" ladrillos.  Cantidad actual :"+this.NumLadrillos);
      }   
      else  //Indicamos que estamos en el límite de saldo
      {
           System.out.println("Almacenaje no permitido , capacidad al límite:"+this.NumLadrillos);
      }    
      
   
   }
   
   public synchronized void Retirar(int Cant, String obra)
   {
      if  ( (this.NumLadrillos-Cant)>=0 )   //Si hay ladrillos suficientes
      {
           this.NumLadrillos-=Cant;   //Decrementamos la cantidad
           
           System.out.println("La obra "+obra+" retira  "+Cant+" Saldo actual:"+this.NumLadrillos);
      }   
      else  //Indicamos que estamos en el límite de saldo
      {
           System.out.println("Retirada no permitida, material insuficiente:"+this.NumLadrillos);
      }    
      
   
   }
   
   public int getLadrillos()
   {
   
      return this.NumLadrillos;
   }
  
}        
        

class Fabrica implements Runnable 
{
    
    private final Almacen a;
    
    private int Lote = 450;   //El número de ladrillos por lote
    
    private int NumLadri;   //Número de ladrillos que llevamos fabricados
    
    private int MaxFabri = 13500; // Máximo numero de ladrillos que puede fabricar
    
    public Fabrica(Almacen a,int NumLadri)
    {       
        this.a=a;
        this.NumLadri=NumLadri;
        
    }
   
    @Override
  public void run() 
  {
      while ( this.NumLadri<this.MaxFabri )
      {
          a.Guardar(Lote);
          
          this.NumLadri+=Lote;   //Incrementamos el numero de ladrillos en otro lote
          
          try {
             
               Thread.sleep(3000);    //Esperamos 2 seg
                  
          } catch (InterruptedException ex) {
              Logger.getLogger(Depositario.class.getName()).log(Level.SEVERE, null, ex);
          }
      
      }
      
    
  }  

}        

class Obra implements Runnable {
    
    private final Almacen a;
    private String Nombre;
    private int TiempoTra;  // Tiempo en segundos que lleva trabajando
    private int Retira;     // Cantidad de ladrillos a retirar
    private int TiemDescanso; // Tiempo de descanso
    
    public Obra(Almacen a, String Nombre, int Retira, int TiemDescanso) {
        this.a = a;
        this.Nombre = Nombre;
        this.TiempoTra = 0;
        this.Retira = Retira;
        this.TiemDescanso = TiemDescanso;
    }

    @Override
    public void run() {
        int MaxTiempo = 120; // Tiempo máximo en segundos que la obra puede funcionar

        while (this.TiempoTra < MaxTiempo) {
            a.Retirar(Retira, this.Nombre);

            try {
                Thread.sleep(TiemDescanso * 1000); // Esperamos esos segundos

                TiempoTra += TiemDescanso; // Incrementamos el tiempo de trabajo con el de la espera

            } catch (InterruptedException ex) {
                Logger.getLogger(Obra.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}


public class Constructora {

	/**
	 * Proceso principal de la constructora
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		try {
			// Crear almacen
			Almacen almacen = new Almacen(0, 15000);

			// Crear fabrica y obras
			Fabrica fabrica = new Fabrica(almacen, 0);
			Obra obra1 = new Obra(almacen, "Obra1", 200, 4);
			Obra obra2 = new Obra(almacen, "Obra2", 400, 2);

			// Arrancar la fabrica y las obras
			Thread fabricaThread = new Thread(fabrica);
			Thread obra1Thread = new Thread(obra1);
			Thread obra2Thread = new Thread(obra2);

			fabricaThread.start();
			obra2Thread.start();
			obra1Thread.start();

			// Esperar a que terminen
			fabricaThread.join();
			obra1Thread.join();
			obra2Thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw e;
		}

	}

}