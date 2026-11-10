import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Ejercicio4 {

    public static int filtrarTexto(String ficheroEntrada, String ficheroSalida, String palabra) {
        int cant = 0;

        // Usamos findstr directamente. En Windows, findstr lee de la entrada estándar
        // (stdin)
        // si no le pasamos un archivo como argumento, por lo que le enviamos el
        // contenido por OutputStream.
        ProcessBuilder pb1 = new ProcessBuilder("findstr", palabra);

        try (
                Process p1 = pb1.start();
                BufferedReader br1 = new BufferedReader(new FileReader(ficheroEntrada));
                BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(p1.getOutputStream()));

                BufferedReader br2 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
                BufferedWriter bw2 = new BufferedWriter(new FileWriter(ficheroSalida))) {
            String linea;

            // 1. Leemos el fichero de entrada y lo escribimos en el flujo de entrada del
            // proceso (stdin)
            while ((linea = br1.readLine()) != null) {
                bw1.write(linea);
                bw1.newLine();
            }
            // ¡Crucial! Hacemos flush y cerramos bw1 para indicarle al proceso que ya hemos
            // terminado de enviar datos, evitando que se quede esperando indefinidamente.
            bw1.flush();
            bw1.close();

            // 2. Leemos el resultado que devuelve el proceso (stdout) y lo guardamos en el
            // fichero de salida
            while ((linea = br2.readLine()) != null) {
                System.out.println(linea);
                bw2.write(linea);
                bw2.newLine(); // Opcional: añade salto de línea en el fichero de salida si quieres mantener el
                               // formato
                cant++; // Incrementamos el contador por cada coincidencia encontrada
            }

            // Opcional: Esperar a que el proceso termine correctamente
            p1.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cant;
    }

    public static void main(String[] args) {
        // Ejemplo de llamada
        int total = filtrarTexto("Ejercicio4\\entrada.txt", "Ejercicio4/resultado_filtrado.txt", "Java");
        System.out.println("Filtrado completado. Líneas encontradas: " + total);
    }
}
