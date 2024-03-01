


import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Comparator;


class Carrera                     //Clase que contendrá los recursos compartidos
{
  private int distancia;         // La distancia que tenemos que recorrer para ganar esta carrera
  
  private double  inicio;
  
  private boolean terminado;      //Variable que indica si la carrera ha terminado
 
  // ArrayList<Integer> intentos = new ArrayList<Integer>();
  
  HashMap<String,Integer> Progreso =new HashMap<String,Integer>(); //Estructura para registrar el avance de cada coche
  
  public Carrera(int distancia,boolean terminado)    //Constructor de la clase
  {
     this.distancia=distancia;
     this.terminado=terminado;
  
  }
  
 
  public synchronized void HeGanado(int recorrido,String nombre)  //Comprobamos si con ese recorrido ese coche ha ganado
  {       
    if  (!this.terminado  ) //Si la carrera no ha terminado
    {       
      //Tenemos que registrar el avance de ese coche en el progreso
      
      Progreso.put(nombre,recorrido);  //Registramos en el hashmpa la distancia que ese coche ha recorrido
       
      if ( this.distancia<=recorrido  )   //Comprobamos si con ese recorrido hemos ganado
      {  
        
        System.out.println("<<<<<<<<<<<<<<<El hilo "+nombre+" ha ganado!!!!!!!!!!!!!!" );

        this.terminado=true;      //la carrera ha terminado   
      
       } 
      else //El coche no ha llegado a meta, mostramos su porcentaje de avance
      {
        double porcentaje;  
     
        porcentaje=(recorrido*100)/this.distancia;  //Calculamos el porcentaje de avance que lleva ese coche
        
        System.out.println("El coche "+nombre+" lleva recorrido "+recorrido+"metros el "+porcentaje+"% de la distancia");
       
      }    
      
    }  
      
  }
 

  public synchronized boolean getTerminado()      //Getter de la variable terminado
  {
    return this.terminado;
  }
  
  public synchronized HashMap getProgreso()      //Getter del HashMap con el progreso
  {
    return this.Progreso;
  }

}

class MostrarProgreso implements Runnable 
{
    private final Carrera Car;
    private final int intervalo;

    public MostrarProgreso(Carrera Car, int intervalo) 
    {
        this.Car = Car;
        this.intervalo = intervalo;
    }

    @Override
    public void run() 
    {
        while (!Car.getTerminado()) 
        {
            try {
                Thread.sleep(intervalo);
            } catch (InterruptedException ex) 
            {
                Logger.getLogger(CarrerasII.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("----- Progreso actual -----");
            HashMap<String, Integer> progreso = Car.getProgreso();
            for (Map.Entry<String, Integer> entry : progreso.entrySet()) 
            {
                System.out.println("Coche " + entry.getKey() + ": " + entry.getValue() + " metros");
            }
            System.out.println("---------------------------");
        }
    }
}


class Coche implements Runnable 
{
   private String nombre;      //Nombre del hilos

   private int recorrido;       //distancia que el hilo lleva recorrida 
   
   private final Carrera Car;   //Recurso compartido
   
  public Coche(String nombre,int recorrido,Carrera Car)       //Constructor que asigna el nombre a cada hilo
  {
     this.nombre=nombre;           //Nombre de ese hilo
     this.recorrido=recorrido;     // 
     this.Car=Car;
  
  }

  @Override
  public void run() 
  {
    int avance=0;      //El número de metros que ese coche avanza en cada iteración  
      
    int tiempo=1000;   //Tiempo que cada coche espera entre avance y avance
    
    Random r = new Random();
    
    avance=1+r.nextInt(49);     //Avanzamo entre 1-50
       
    this.recorrido+=avance;    //Incrementamos la distancia recorrida con cada avance
    
    Car.HeGanado(this.recorrido, nombre);
    
    while (!this.Car.getTerminado() )    //Mientras el número no haya terminado
    {    
     
        try {
              Thread.sleep(tiempo);
            
        } catch (InterruptedException ex) 
        {
            Logger.getLogger(Adivinador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       avance=1+r.nextInt(49);     //Avanzamo entre 1-50
       
       this.recorrido+=avance;    //Incrementamos la distancia recorrida con cada avance
       
       Car.HeGanado(this.recorrido, nombre);
        
    }
    
    
    System.out.println("El cocher "+nombre+ " ha terminado ");
  }
  
}

class CarrerasII 
{

  public static void main(String[] args) 
  {
   //Definimos los tres coches que van a correr   
      
      
      
   final int NumHilos=3;
      
   String[] coches = new String[3];  
      
   coches[0]="Opel";
   coches[1]="Ford";   
   coches[2]="Seat";
   
   Random r = new Random();
    
   int distancia=500+r.nextInt(901);             //Elegimos la distancia que hay que recorrer
   
   Carrera Car = new Carrera(distancia,false);   //Creamos la carrera para esa disntancia e indicando que no ha terminado
  
   System.out.println("---Carrera iniciada, hay que recorrer  "+distancia+ " metros. SUERTE-----");
    
   //Creamos y lanzamos los hilos
 
    Thread[] hilos = new Thread[NumHilos];  //Creamos el array de hilos
       
    for (int i = 0; i < NumHilos; i++) 
    {  
      Thread th = new Thread(new Coche(coches[i],0,Car) );  //Creamos ese hilo
      th.start();      //Lo lanzamos
      hilos[i] = th;   //Guardamos ese hilo en un array
    
    }
   
    int intervaloMostrarProgreso = 3000; // Intervalo en milisegundos (5 segundos)
        long tiempoInicio = System.currentTimeMillis();
        long tiempoSiguienteMostrarProgreso = tiempoInicio + intervaloMostrarProgreso;

        while (!Car.getTerminado()) 
        {
            try {
                System.out.println("----- Progreso actual -----");
                HashMap<String, Integer> progreso = Car.getProgreso();
                for (Map.Entry<String, Integer> entry : progreso.entrySet()) {
                    System.out.println("Coche " + entry.getKey() + ": " + entry.getValue() + " metros");
                }
                System.out.println("---------------------------");

                // Calcular el tiempo de espera hasta la siguiente actualización
                long tiempoActual = System.currentTimeMillis();
                long tiempoRestante = tiempoSiguienteMostrarProgreso - tiempoActual;

                // Ajustar el tiempo de espera para evitar acumulación de retrasos
                if (tiempoRestante > 0) {
                    Thread.sleep(tiempoRestante);
                }

                // Actualizar el tiempo para la siguiente actualización
                tiempoSiguienteMostrarProgreso = System.currentTimeMillis() + intervaloMostrarProgreso;

            } catch (InterruptedException ex) {
                Logger.getLogger(Carreras.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    
    
    for (Thread h: hilos)   //Para cade hilo hacemos que el principal espere
    {
      try {
        h.join();
      } catch (InterruptedException e) {
      }
    }
   
    
    
    
    
    System.out.println();
    
    //mostramos el podium
    
    List<Map.Entry<String,Integer>> lista = new ArrayList<>( Car.getProgreso().entrySet() );
    
    lista.sort(Map.Entry.comparingByValue(Comparator.reverseOrder())  );
                                     
    for(Map.Entry<String,Integer> e: lista  )
    {
       System.out.println("Coche:"+e.getKey()+" recorrio "+e.getValue()+" metros");
    }    
    
    System.out.printf("-----Programa principal terminado -----------");
  }

}
