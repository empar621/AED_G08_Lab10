package ejercicio2;

public class MainEjercicio2 {
    public static void main(String[] args) {
        // 1. Instanciar el modelado de la red
        RedCiudades red = new RedCiudades();

        // 2. Registrar las ciudades de ejemplo (Vértices)
        red.agregarCiudad("Arequipa");
        red.agregarCiudad("Cusco");
        red.agregarCiudad("Puno");
        red.agregarCiudad("Tacna");
        red.agregarCiudad("Moquegua");

        // 3. Registrar las conexiones/carreteras de ejemplo (Aristas con peso)
        red.agregarCarretera("Arequipa", "Cusco", 510);
        red.agregarCarretera("Arequipa", "Moquegua", 230);
        red.agregarCarretera("Moquegua", "Tacna", 160);
        red.agregarCarretera("Cusco", "Puno", 390);
        red.agregarCarretera("Puno", "Tacna", 420);

        // 4. Mostrar la lista de ciudades
        System.out.println("--- LISTA DE CIUDADES EN LA RED ---");
        for (String ciudad : red.obtenerCiudades()) {
            System.out.println("• " + ciudad);
        }

        // 5. Mostrar todas las carreteras
        red.mostrarConexiones();

        // 6. Ejecutar pruebas de Dijkstra para calcular caminos más cortos
        // Ejemplo 1: De Arequipa a Tacna (Debería ir por Moquegua -> 230 + 160 = 390km)
        red.calcularCaminoMasCorto("Arequipa", "Tacna");

        // Ejemplo 2: De Cusco a Tacna (Debería ir por Puno -> 390 + 420 = 810km)
        red.calcularCaminoMasCorto("Cusco", "Tacna");
    }
}