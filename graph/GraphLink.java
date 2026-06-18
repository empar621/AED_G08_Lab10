package graph;

import listlinked.ListLinked;

import java.util.ArrayList;
import java.util.Stack;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class GraphLink<E> {
    private ListLinked<AdjList<E>> graph;

    public GraphLink() {
        this.graph = new ListLinked<>();
    }
    
    // 6. Insertar vertice
    public void insertVertex(E data) {
        Vertex<E> vertex = new Vertex<>(data);
        graph.addLast(new AdjList<>(vertex));
    }
   
    // 7. Buscar vertice
    private AdjList<E> findVertex(E data) {
        for (int i = 0; i < graph.size(); i++) {
            AdjList<E> adj = graph.get(i);
            if (adj.getVertex().getData().equals(data)) {
                return adj;
            }
        }
        return null;
    }

    // 8. Insertar arista (grafo no dirigido)
    public void insertEdge(E origin, E destination) {
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);

        if (v1 == null || v2 == null) {
            return;
        }

        v1.getEdges().addLast(new Edge<>(v2.getVertex()));
        v2.getEdges().addLast(new Edge<>(v1.getVertex()));
    }

    // 9. Mostrar el grafo
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < graph.size(); i++) {
            AdjList<E> adj = graph.get(i);
            sb.append(adj.getVertex()).append(" -> ");
            for (int j = 0; j < adj.getEdges().size(); j++) {
                sb.append(adj.getEdges().get(j)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    
    /**
     * 1. Inserta una arista ponderada de forma bidireccional entre dos vértices.
     */
    public void insertEdgeWeight(E origin, E destination, int weight) {
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);

        if (v1 == null || v2 == null) {
            return;
        }

        // Al ser un grafo no dirigido, la conexión con su respectivo peso se añade en ambos sentidos
        v1.getEdges().addLast(new Edge<>(v2.getVertex(), weight));
        v2.getEdges().addLast(new Edge<>(v1.getVertex(), weight));
    }

    /**
     * 2. Verifica si el grafo es conexo (todos los vértices están conectados por algún camino).
     */
    public boolean isConexo() {
        if (graph.size() == 0) {
            return true;
        }

        Set<E> visited = new HashSet<>();
        Queue<E> queue = new LinkedList<>();

        // Tomamos el primer vértice registrado como punto de partida
        E startVertex = graph.get(0).getVertex().getData();
        queue.add(startVertex);
        visited.add(startVertex);

        // Recorrido BFS clásico para expandir la búsqueda por niveles
        while (!queue.isEmpty()) {
            E current = queue.poll();
            AdjList<E> adjList = findVertex(current);

            if (adjList != null) {
                for (int i = 0; i < adjList.getEdges().size(); i++) {
                    E neighbor = adjList.getEdges().get(i).getDestination().getData();
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Si el número de nodos alcanzados es igual al total del grafo, es conexo
        return visited.size() == graph.size();
    }

    /**
     * Método de soporte privado que ejecuta el Algoritmo de Dijkstra.
     * Calcula y mapea las relaciones de predecesores óptimos para construir caminos mínimos.
     */
    private Map<E, E> runDijkstra(E start, E target) {
        Map<E, Integer> distances = new HashMap<>();
        Map<E, E> predecessors = new HashMap<>();
        Set<E> settled = new HashSet<>();

        // Inicializamos todas las distancias en "infinito"
        for (int i = 0; i < graph.size(); i++) {
            distances.put(graph.get(i).getVertex().getData(), Integer.MAX_VALUE);
        }
        distances.put(start, 0);

        while (settled.size() < graph.size()) {
            // Encontrando el nodo no evaluado con la distancia mínima acumulada
            E current = null;
            int minDistance = Integer.MAX_VALUE;

            for (E node : distances.keySet()) {
                if (!settled.contains(node) && distances.get(node) < minDistance) {
                    minDistance = distances.get(node);
                    current = node;
                }
            }

            // Si ya no hay nodos alcanzables o llegamos al destino, detenemos el análisis
            if (current == null || current.equals(target)) {
                break;
            }

            settled.add(current);
            AdjList<E> adjList = findVertex(current);

            if (adjList != null) {
                for (int i = 0; i < adjList.getEdges().size(); i++) {
                    Edge<E> edge = adjList.getEdges().get(i);
                    E neighbor = edge.getDestination().getData();

                    if (!settled.contains(neighbor)) {
                        int newDistance = distances.get(current) + edge.getWeight();
                        if (newDistance < distances.get(neighbor)) {
                            distances.put(neighbor, newDistance);
                            predecessors.put(neighbor, current); // Guardamos la ruta óptima
                        }
                    }
                }
            }
        }
        return predecessors;
    }

    /**
     * 3. Determina la ruta más corta entre dos nodos y la retorna en un ArrayList.
     */
    public ArrayList<E> shortPath(E v, E z) {
        Map<E, E> predecessors = runDijkstra(v, z);
        ArrayList<E> path = new ArrayList<>();

        // Si el destino no es el origen y no tiene predecesor registrado, no hay camino posible
        if (!v.equals(z) && !predecessors.containsKey(z)) {
            return path;
        }

        // Reconstruimos el camino desde el destino regresando hasta el origen
        E current = z;
        while (current != null) {
            path.add(0, current); // Se inserta al inicio de la lista para ordenar de origen a destino
            current = predecessors.get(current);
        }
        return path;
    }

    /**
     * 4. Ejecuta Dijkstra y retorna la ruta más corta ordenada dentro de un Stack (Pila).
     */
    public Stack<E> Dijsktra(E v, E w) {
        Map<E, E> predecessors = runDijkstra(v, w);
        Stack<E> pathStack = new Stack<>();

        if (!v.equals(w) && !predecessors.containsKey(w)) {
            return pathStack;
        }

        // Reconstruimos el camino empujando los elementos al Stack desde el destino al origen
        E current = w;
        while (current != null) {
            pathStack.push(current);
            current = predecessors.get(current);
        }

        // Nota: Como el camino se lee desde 'w' hacia 'v', al introducirlos en la pila,
        // el origen 'v' quedará en la cima (Top). Al hacer .pop(), saldrán en orden correcto.
        return pathStack;
    }
}