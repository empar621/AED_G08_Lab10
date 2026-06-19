package ejercicio2;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.GraphPath;

import java.util.Set;

public class RedCiudades {

    // Grafo ponderado no dirigido: Vértices (String), Aristas (DefaultWeightedEdge)
    private final Graph<String, DefaultWeightedEdge> grafo;

    public RedCiudades() {
        this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    }

    public void agregarCiudad(String ciudad) {
        if (!grafo.containsVertex(ciudad)) {
            grafo.addVertex(ciudad);
        }
    }

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

    public Set<String> obtenerCiudades() {
        return grafo.vertexSet();
    }

    public void mostrarConexiones() {
        System.out.println("\n--- CARRETERAS REGISTRADAS ---");
        for (DefaultWeightedEdge arista : grafo.edgeSet()) {
            String origen = grafo.getEdgeSource(arista);
            String destino = grafo.getEdgeTarget(arista);
            double peso = grafo.getEdgeWeight(arista);
            System.out.printf("%s <---> %s : %.0f km\n", origen, destino, peso);
        }
    }

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