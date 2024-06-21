
package com.scientia.daos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ArchivoPlanoNIO {
    
    private final Path miArchivo;
    private final String FIN_LINEA = System.getProperty("line.separator");

    public ArchivoPlanoNIO(String nombrecito) throws IOException { //La throws es la solución al error que va a aparecer con "Files.createFile(miArchivo);"
        miArchivo = Paths.get(nombrecito);                          //la alternativa a esto es rodear con TryCatch el bloque, o el statement
        
        if (!Files.exists(miArchivo)) {
            System.out.println("Papi, ya te lo creo, relax");
            
            Files.createFile(miArchivo);
            
        }else{
            System.out.println("Uyy lo encontre !!" + nombrecito);
        }
    }
  
    public String[] obtenerDatos() throws IOException{
        String contenido, lineasContenido[]; 
        
        if (Files.size(miArchivo)>0) {
            contenido = new String(Files.readAllBytes(miArchivo));
            lineasContenido = contenido.split(FIN_LINEA);
            
        } else {
            lineasContenido = new String[0];
        }
        return lineasContenido;
    }
    
    public void agregarDato(String filita) throws IOException{
        List<String> listaLineas = new ArrayList<>();
        listaLineas.add(filita);
        Files.write(miArchivo, listaLineas, StandardOpenOption.APPEND);
                
                
    }
    
    public void resetear()throws IOException{
        Files.delete(miArchivo);
        Files.createFile(miArchivo);
    }
    
    
    
}
