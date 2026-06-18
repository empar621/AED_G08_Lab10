package ejercicio3;

public class CityApp {
    public static void main(String[] args) {
        // grafo personalizado para manejar Ciudades (String) y Distancias (Integer)
        Graph<String, Integer> customGraph = new GraphLink<>();

        System.out.println("=== 1. INSERTANDO CIUDADES Y CARRETERAS ===");
        customGraph.insertVertex("Arequipa");
        customGraph.insertVertex("Cusco");
        customGraph.insertVertex("Puno");
        customGraph.insertVertex("Tacna");
        customGraph.insertVertex("Moquegua");

        customGraph.insertEdge("Arequipa", "Cusco", 510);
        customGraph.insertEdge("Arequipa", "Moquegua", 230);
        customGraph.insertEdge("Moquegua", "Tacna", 160);
        customGraph.insertEdge("Cusco", "Puno", 390);
        customGraph.insertEdge("Puno", "Tacna", 420);

        System.out.println(customGraph);

        System.out.println("=== 2. PROBANDO BÚSQUEDAS ===");
        System.out.println("¿Existe Cusco?: " + customGraph.searchVertex("Cusco"));
        System.out.println("¿Existe conexión Moquegua - Tacna?: " + customGraph.searchEdge("Moquegua", "Tacna"));
        System.out.println("Ciudades adyacentes a Arequipa: " + customGraph.adjacentVertices("Arequipa"));
        System.out.println();

        System.out.println("=== 3. ELIMINANDO LA CARRETERA MOQUEGUA - TACNA ===");
        customGraph.removeEdge("Moquegua", "Tacna");
        System.out.println(customGraph);

        System.out.println("=== 4. ELIMINANDO LA CIUDAD DE PUNO COMPLETAMENTE ===");
        customGraph.removeVertex("Puno");
        System.out.println(customGraph);
    }
}