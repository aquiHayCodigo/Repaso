import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Scanner;

public class App {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        boolean menu = true;
        while (menu) {
            System.out.println("- - - - - - - - - - - - - - -");
            System.out.println("1. Tiempo ejecución");
            System.out.println("2. Contar procesos activos");
            System.out.println("3. Procesar fichero");
            System.out.println("4. Filtrar lineas en archivos");
            System.out.println("5. Procesar lista de archivos");
            System.out.print("Escribe una opción: ");
            int opc = Integer.parseInt(sc.nextLine());
            System.out.println("- - - - - - - - - - - - - - -");
            switch (opc) {
                case 1:
                    contarTiempoV2();
                    break;

                case 2:
                    contarProcesosActivosV2();
                    break;

                case 3:
                    String numeros = "src\\numeros.txt";
                    String salida = "src\\salida_ordenada.txt";
                    procesarFicheroV2(numeros, salida);
                    break;

                case 4:
                    String entrada = "src\\txt\\entrada.txt";
                    String resultado = "src\\txt\\resultado_filtrado.txt";
                    String palabra = "Java";
                    filtrarTextoV2(entrada, resultado, palabra);
                    break;

                case 5:
                    String arch1 = "src\\txt\\archivo1.txt";
                    String arch2 = "src\\txt\\archivo2.txt";
                    List<String> archivos = List.of(arch1, arch2);
                    procesarLista(archivos);
                    break;

                default:
                    break;
            }

        }
    }

    public static void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void contarTiempo() {
        limpiarPantalla();
        String archivo = "src\\txt\\recordatorio.txt";
        File file = new File(archivo);
        try {
            if (file.exists()) {
                file.createNewFile();
            }
            ProcessBuilder pb = new ProcessBuilder("notepad.exe", archivo);
            long inicio = System.currentTimeMillis();

            System.out.println("Abriendo archivo " + archivo);
            Process p = pb.start(); 
            int exitCode = p.waitFor();
            long fin = System.currentTimeMillis();
            long tiempo = (inicio - fin) / 1000;
            if (exitCode == 0) {
                System.out.println("El proceso ha sido ejecutado.");
                System.out.println("Ha durado " + tiempo + " segundos.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static long contarTiempoV2(){
        limpiarPantalla();
        long tiempo=0;
        String recordatorio = "src\\txt\\recordatorio.txt";
        ProcessBuilder pb = new ProcessBuilder("notepad.exe", recordatorio);
        try {
            long inicio = System.currentTimeMillis();
            System.out.println("Abriendo archivo " + recordatorio );
            Process p = pb.start();
            int exitCode = p.waitFor();
            long fin = System.currentTimeMillis();
            tiempo = fin - inicio;
            if(exitCode==0){
                System.out.println("Proceso ejecutado con duración de: " + tiempo + " milisegundos");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return tiempo;
    }

    public static int contarProcesosActivos() {
        limpiarPantalla();
        int cant = 0;
        ProcessBuilder pb1 = new ProcessBuilder("cmd", "/c", "tasklist");
        ProcessBuilder pb2 = new ProcessBuilder("cmd", "/c", "find /c /v \"\"");
        List<ProcessBuilder> pbs = List.of(pb1, pb2);
        try {
            List<Process> procesos = ProcessBuilder.startPipeline(pbs);
            Process last = procesos.getLast();
            BufferedReader br = new BufferedReader(new InputStreamReader(last.getInputStream()));
            String linea;
            linea = br.readLine();
            cant = Integer.parseInt(linea);
            System.out.println(cant);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cant;
    }


    public static int contarProcesosActivosV2(){
        limpiarPantalla();
        int cant = 0;
        ProcessBuilder pb1 = new ProcessBuilder ( "cmd", "/c", "tasklist");
        ProcessBuilder pb2 = new ProcessBuilder ( "cmd", "/c", "find /c /v \"\"");
        List<ProcessBuilder> pbs = List.of(pb1, pb2);
        try {
            List<Process> procesos = ProcessBuilder.startPipeline(pbs);
            Process last = procesos.getLast();
            BufferedReader br = new BufferedReader(new InputStreamReader(last.getInputStream()));
            String linea = br.readLine();
            cant  = Integer.parseInt(linea);
            System.out.println("Procesos activos en total: " + cant);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cant;
    }


    public static void procesarFichero(String archivoEntrada, String archivoSalida) {
        limpiarPantalla();
        File entrada = new File(archivoEntrada);
        File salida = new File(archivoSalida);
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "sort");
        pb.redirectInput(entrada);
        pb.redirectOutput(salida);

        try {
            Process p = pb.start();
            int exitCode = p.waitFor();
            if (exitCode == 0) {
                System.out.println("El proceso se ha ejecutado en " + salida);
            } else {
                System.out.println("El proceso ha tenido un error");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void procesarFicheroV2(String archivoEntrada, String archivoSalida){
        limpiarPantalla();
        ProcessBuilder pb = new ProcessBuilder("cmd" , "/c", "sort");
        try (
            Process p = pb.start();
            BufferedReader br1 = new BufferedReader(new FileReader(archivoEntrada));
            BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

            BufferedReader br2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(archivoSalida));
        ){
            String linea;
            while((linea= br1.readLine())!=null){
                bw1.write(linea);
                bw1.newLine();
            }
            bw1.flush();
            bw1.close();
            while ((linea=br2.readLine())!=null) {
                bw2.write(linea);
                bw2.newLine();
            }
            bw2.flush();
            bw2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int filtrarTexto(String ficheroEntrada, String ficheroSalida, String palabra) {
        limpiarPantalla();
        int cant = 0;
        ProcessBuilder pb1 = new ProcessBuilder("cmd", "/c", "findstr", palabra);
        try (
                Process p1 = pb1.start();
                BufferedReader br1 = new BufferedReader(new FileReader(ficheroEntrada));
                BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(p1.getOutputStream()));

                BufferedReader br2 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
                BufferedWriter bw2 = new BufferedWriter(new FileWriter(ficheroSalida));

        ) {
            String linea;
            while ((linea = br1.readLine()) != null) {
                bw1.write(linea);
                bw1.newLine();
            }
            bw1.flush();
            bw1.close();
            while ((linea = br2.readLine()) != null) {
                System.out.println(linea);
                bw2.write(linea);
                bw2.newLine();
                cant++;
            }
            int exitCode = p1.waitFor();
            if (exitCode == 0) {
                System.out.println("El proceso ha sido ejecutado con éxito");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cant;
    }

    public static void filtrarTextoV2(String ficheroEntrada, String ficheroSalida, String palabra){
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "findstr", palabra);
        pb.redirectInput(new File(ficheroEntrada));
        pb.redirectOutput(new File(ficheroSalida));
        try (
            Process p = pb.start();
        ){
            int exitCode = p.waitFor();
            if(exitCode == 0){
                System.out.println("El proceso con numero " + p.pid() + " ha sido ejecutado");
            } else{
                System.out.println("[ERROR] El proceso no se ha executado correctamente");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void procesarLista(List<String> archivos) {
        int cant = 0;
        String bat = "src\\bat\\analizar_lineas.bat";
        for (String archivo : archivos) {
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", bat, archivo);
            try (
                    Process proceso = pb.start();
                    BufferedReader br = new BufferedReader(new InputStreamReader(proceso.getInputStream()));) {
                String linea;

                while ((linea = br.readLine()) != null) {
                    cant += Integer.parseInt(linea);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(cant);

    }

}
