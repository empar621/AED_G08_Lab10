package ejercicio2;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.GraphPath;

import java.util.Set;

/**
 * Aplicación para modelar y optimizar una red de ciudades interconectadas
 * utilizando la biblioteca JGraphT.
 */
public class CityNetworkApp {

    public static void main(String[] args) {
        // 1. Inicialización de un grafo ponderado no dirigido
        Graph<String, DefaultWeightedEdge> network = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        // 2. Registro y adición de ciudades (Vértices)
        String[] cities = {"Arequipa", "Cusco", "Puno", "Tacna", "Moquegua"};
        for (String city : cities) {
            network.addVertex(city);
        }

        // 3. Registro de carreteras con sus respectivas distancias (Aristas Ponderadas)
        addRoad(network, "Arequipa", "Cusco", 510);
        addRoad(network, "Arequipa", "Moquegua", 230);
        addRoad(network, "Moquegua", "Tacna", 160);
        addRoad(network, "Cusco", "Puno", 390);
        addRoad(network, "Puno", "Tacna", 420);

        // 4. Mostrar reporte general de la red en la consola
        printNetworkReport(network);

        // 5. Cálculo y visualización de la ruta óptima usando el algoritmo de Dijkstra
        calculateAndPrintShortestPath(network, "Arequipa", "Tacna");
    }

    /**
     * Método auxiliar para registrar una carretera bidireccional estableciendo su peso.
     */
    private static void addRoad(Graph<String, DefaultWeightedEdge> graph, String origin, String destination, double distance) {
        DefaultWeightedEdge edge = graph.addEdge(origin, destination);
        if (edge != null) {
            graph.setEdgeWeight(edge, distance);
        }
    }

    /**
     * Imprime en consola la lista de ciudades y las carreteras registradas en el sistema.
     */
    private static void printNetworkReport(Graph<String, DefaultWeightedEdge> graph) {
        System.out.println("=== REPORTE GENERAL DE LA RED DE CIUDADES ===");
        
        // Mostrar lista de ciudades
        System.out.println("\nCiudades Registradas:");
        Set<String> vertices = graph.vertexSet();
        for (String city : vertices) {
            System.out.println(" * " + city);
        }

        // Mostrar las carreteras y su costo en kilómetros
        System.out.println("\nCarreteras Registradas:");
        Set<DefaultWeightedEdge> edges = graph.edgeSet();
        for (DefaultWeightedEdge edge : edges) {
            String source = graph.getEdgeSource(edge);
            String target = graph.getEdgeTarget(edge);
            double weight = graph.getEdgeWeight(edge);
            System.out.printf("   %s <--> %s (%d km)\n", source, target, (int) weight);
        }
        System.out.println("=============================================\n");
    }

    /**
     * Utiliza la suite de algoritmos de JGraphT para calcular la ruta más corta.
     */
    private static void calculateAndPrintShortestPath(Graph<String, DefaultWeightedEdge> graph, String from, String to) {
        System.out.println("=== OPTIMIZACIÓN DE RUTA (DIJKSTRA) ===");
        System.out.println("Origen: " + from);
        System.out.println("Destino: " + to);

        // Instanciar el buscador de caminos mínimos de JGraphT
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);
        GraphPath<String, DefaultWeightedEdge> path = dijkstra.getPath(from, to);

        if (path != null) {
            System.out.println("\nCamino más corto encontrado:");
            // Imprime la secuencia exacta de nodos del recorrido óptimo
            System.out.println(" RUTA: " + path.getVertexList());
            System.out.printf(" COSTO TOTAL: %d km\n", (int) path.getWeight());
        } else {
            System.out.println("\nNo se encontró ninguna ruta transitable entre las ciudades indicadas.");
        }
        System.out.println("=======================================");
    }
}