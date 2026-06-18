package ejercicio2;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.GraphPath;

import java.util.Set;

/**
 * Clase que modela la red de ciudades y carreteras utilizando JGraphT.
 */
public class RedCiudades {

    // Grafo ponderado no dirigido: Vértices (String), Aristas (DefaultWeightedEdge)
    private final Graph<String, DefaultWeightedEdge> grafo;

    /**
     * Constructor que inicializa el grafo simple ponderado.
     */
    public RedCiudades() {
        this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    }

    /**
     * Agrega una nueva ciudad (vértice) al grafo.
     * @param ciudad Nombre de la ciudad.
     */
    public void agregarCiudad(String ciudad) {
        if (!grafo.containsVertex(ciudad)) {
            grafo.addVertex(ciudad);
        }
    }

    /**
     * Agrega una carretera (arista) bidireccional con una distancia (peso).
     * @param origen Ciudad de origen.
     * @param destino Ciudad de destino.
     * @param distancia Distancia en kilómetros.
     */
    public void agregarCarretera(String origen, String destino, double distancia) {
        // Validar que ambas ciudades existan antes de conectarlas
        if (grafo.containsVertex(origen) && grafo.containsVertex(destino)) {
            DefaultWeightedEdge arista = grafo.addEdge(origen, destino);
            if (arista != null) {
                grafo.setEdgeWeight(arista, distancia);
            }
        } else {
            System.out.println("Error: Una o ambas ciudades no existen en la red.");
        }
    }

    /**
     * Retorna el conjunto de ciudades registradas.
     */
    public Set<String> obtenerCiudades() {
        return grafo.vertexSet();
    }

    /**
     * Muestra en consola todas las conexiones (carreteras) y sus distancias.
     */
    public void mostrarConexiones() {
        System.out.println("\n--- CARRETERAS REGISTRADAS ---");
        for (DefaultWeightedEdge arista : grafo.edgeSet()) {
            String origen = grafo.getEdgeSource(arista);
            String destino = grafo.getEdgeTarget(arista);
            double peso = grafo.getEdgeWeight(arista);
            System.out.printf("%s <---> %s : %.0f km\n", origen, destino, peso);
        }
    }

    /**
     * Calcula y muestra el camino más corto entre dos ciudades usando el algoritmo de Dijkstra.
     * @param origen Ciudad de partida.
     * @param destino Ciudad de llegada.
     */
    public void calcularCaminoMasCorto(String origen, String destino) {
        System.out.println("\n--- CÁLCULO DE RUTA ÓPTIMA ---");
        
        if (!grafo.containsVertex(origen) || !grafo.containsVertex(destino)) {
            System.out.println("Error: Las ciudades especificadas no existen.");
            return;
        }

        // Instancia del algoritmo de Dijkstra provisto por JGraphT
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(grafo);
        GraphPath<String, DefaultWeightedEdge> camino = dijkstra.getPath(origen, destino);

        if (camino != null) {
            System.out.println("Origen: " + origen);
            System.out.println("Destino: " + destino);
            System.out.println("Camino óptimo: " + camino.getVertexList());
            System.out.printf("Costo total (Distancia): %.0f km\n", camino.getWeight());
        } else {
            System.out.println("No existe una ruta disponible entre " + origen + " y " + destino);
        }
    }
}