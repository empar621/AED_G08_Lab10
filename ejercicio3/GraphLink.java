package ejercicio3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GraphLink<V, E> implements Graph<V, E> {

    public static class Vertex<V> {
        private V data;

        public Vertex(V data) {
            this.data = data;
        }

        public V getData() { return data; }
        public void setData(V data) { this.data = data; }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Vertex<?> vertex = (Vertex<?>) obj;
            return data.equals(vertex.data);
        }

        @Override
        public int hashCode() { return data.hashCode(); }

        @Override
        public String toString() { return data.toString(); }
    }

    public static class Edge<V, E> {
        private Vertex<V> destination;
        private E weight;

        public Edge(Vertex<V> destination, E weight) {
            this.destination = destination;
            this.weight = weight;
        }

        public Vertex<V> getDestination() { return destination; }
        public E getWeight() { return weight; }

        @Override
        public String toString() {
            return destination.toString() + "(" + weight + ")";
        }
    }

    public static class AdjList<V, E> {
        private Vertex<V> vertex;
        private List<Edge<V, E>> edges;

        public AdjList(Vertex<V> vertex) {
            this.vertex = vertex;
            this.edges = new LinkedList<>();
        }

        public Vertex<V> getVertex() { return vertex; }
        public List<Edge<V, E>> getEdges() { return edges; }
    }

    // --- Atributos y Constructor de GraphLink ---
    
    private List<AdjList<V, E>> adjacencyLists;

    public GraphLink() {
        this.adjacencyLists = new ArrayList<>();
    }

    // Método de asistencia privado para localizar la lista de un nodo
    private AdjList<V, E> findAdjList(V data) {
        for (AdjList<V, E> adj : adjacencyLists) {
            if (adj.getVertex().getData().equals(data)) {
                return adj;
            }
        }
        return null;
    }

    // --- Implementación de los Métodos del TAD Graph ---

    @Override
    public void insertVertex(V data) {
        if (searchVertex(data)) return; // Evita duplicados
        adjacencyLists.add(new AdjList<>(new Vertex<>(data)));
    }

    @Override
    public void insertEdge(V origin, V destination, E weight) {
        AdjList<V, E> u = findAdjList(origin);
        AdjList<V, E> v = findAdjList(destination);

        if (u == null || v == null) return;

        // Modelamos un grafo no dirigido (idéntico a la red vial del Ejercicio 2)
        u.getEdges().add(new Edge<>(v.getVertex(), weight));
        v.getEdges().add(new Edge<>(u.getVertex(), weight));
    }

    @Override
    public void removeVertex(V data) {
        AdjList<V, E> toRemove = findAdjList(data);
        if (toRemove == null) return;

        // 1. Limpieza de seguridad: remueve todas las aristas entrantes desde otros nodos
        for (AdjList<V, E> adj : adjacencyLists) {
            adj.getEdges().removeIf(edge -> edge.getDestination().getData().equals(data));
        }

        // 2. Elimina la lista de adyacencia propia del nodo
        adjacencyLists.remove(toRemove);
    }

    @Override
    public void removeEdge(V origin, V destination) {
        AdjList<V, E> u = findAdjList(origin);
        AdjList<V, E> v = findAdjList(destination);

        // Remueve la conexión de ida y de vuelta
        if (u != null) {
            u.getEdges().removeIf(edge -> edge.getDestination().getData().equals(destination));
        }
        if (v != null) {
            v.getEdges().removeIf(edge -> edge.getDestination().getData().equals(origin));
        }
    }

    @Override
    public boolean searchVertex(V data) {
        return findAdjList(data) != null;
    }

    @Override
    public boolean searchEdge(V origin, V destination) {
        AdjList<V, E> u = findAdjList(origin);
        if (u == null) return false;

        for (Edge<V, E> edge : u.getEdges()) {
            if (edge.getDestination().getData().equals(destination)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<V> adjacentVertices(V data) {
        List<V> neighbors = new ArrayList<>();
        AdjList<V, E> u = findAdjList(data);
        if (u == null) return neighbors;

        for (Edge<V, E> edge : u.getEdges()) {
            neighbors.add(edge.getDestination().getData());
        }
        return neighbors;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (AdjList<V, E> adj : adjacencyLists) {
            sb.append(adj.getVertex()).append(" -> ");
            for (Edge<V, E> edge : adj.getEdges()) {
                sb.append(edge).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    //
    // 1. Permite obtener la lista limpia de todos los datos/vértices registrados en el sistema
    public List<V> getVertices() {
        List<V> list = new ArrayList<>();
        for (AdjList<V, E> adj : adjacencyLists) {
            list.add(adj.getVertex().getData());
        }
        return list;
    }

    // 2. Inserta una arista en un solo sentido (esencial para construir el grafo complementario dirigido) 
    public void insertSingleDirectedEdge(V origin, V destination, E weight) {
        AdjList<V, E> u = findAdjList(origin);
        AdjList<V, E> v = findAdjList(destination);
        if (u != null && v != null) {
            u.getEdges().add(new Edge<>(v.getVertex(), weight));
        }
    }
}