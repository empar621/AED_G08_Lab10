package ejercicio2;

public class MainEjercicio2 {
    public static void main(String[] args) {
        // modelado de la red
        RedCiudades red = new RedCiudades();

        // 2. registro ciudades 
        red.agregarCiudad("Arequipa");
        red.agregarCiudad("Cusco");
        red.agregarCiudad("Puno");
        red.agregarCiudad("Tacna");
        red.agregarCiudad("Moquegua");

        // 3. registro conexiones carreteras con peso
        red.agregarCarretera("Arequipa", "Cusco", 510);
        red.agregarCarretera("Arequipa", "Moquegua", 230);
        red.agregarCarretera("Moquegua", "Tacna", 160);
        red.agregarCarretera("Cusco", "Puno", 390);
        red.agregarCarretera("Puno", "Tacna", 420);

       
        System.out.println("--- LISTA DE CIUDADES EN LA RED ---");
        for (String ciudad : red.obtenerCiudades()) {
            System.out.println("• " + ciudad);
        }

        red.mostrarConexiones();

        red.calcularCaminoMasCorto("Arequipa", "Tacna");

        red.calcularCaminoMasCorto("Cusco", "Tacna");
    }
}