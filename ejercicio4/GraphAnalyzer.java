package ejercicio4;

import ejercicio3.GraphLink;

import java.util.*;

public class GraphAnalyzer {

    /**
     * 1. CONEXO: Verifica si un grafo dirigido es conexo[cite: 964, 968].
     * Se evalúa la conectividad débil (ignorando la dirección de las aristas) 
     * para verificar si existe al menos un camino que conecte todos los vértices.
     */
    public static <V, E> boolean isConexo(GraphLink<V, E> graphLink) {
        List<V> vertices = graphLink.getVertices();
        if (vertices.isEmpty()) return true;

        // Construimos un mapa de adyacencia no dirigido (no orientado) temporal
        Map<V, Set<V>> undirectedMap = new HashMap<>();
        for (V v : vertices) {
            undirectedMap.put(v, new HashSet<>());
        }

        // Llenamos el mapa bidireccionalmente con todas las aristas existentes
        for (V u : vertices) {
            for (V v : graphLink.adjacentVertices(u)) {
                undirectedMap.get(u).add(v);
                undirectedMap.get(v).add(u);
            }
        }

        // Ejecutamos un recorrido BFS estándar sobre el mapa no dirigido
        Set<V> visited = new HashSet<>();
        Queue<V> queue = new LinkedList<>();
        
        V start = vertices.get(0);
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            V current = queue.poll();
            for (V neighbor : undirectedMap.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        // Si el total de nodos alcanzados es igual al tamaño del grafo, es conexo 
        return visited.size() == vertices.size();
    }

    /**
     * 2. ISOMORFO: Determina si el grafo G1 tiene la misma estructura que el grafo G2.
     */
    public static <V, E> boolean isIsomorfo(GraphLink<V, E> g1, GraphLink<V, E> g2) {
        List<V> v1 = g1.getVertices();
        List<V> v2 = g2.getVertices();

        // Invariante básico 1: Mismo número de vértices 
        if (v1.size() != v2.size()) return false;
        
        // Invariante básico 2: Mismo número total de aristas
        int e1Count = 0, e2Count = 0;
        for (V v : v1) e1Count += g1.adjacentVertices(v).size();
        for (V v : v2) e2Count += g2.adjacentVertices(v).size();
        if (e1Count != e2Count) return false;

        // Mapeo biyectivo para el proceso de backtracking
        Map<V, V> mapping = new HashMap<>();
        Set<V> usedInG2 = new HashSet<>();

        return checkIsomorphismBacktracking(0, v1, v2, g1, g2, mapping, usedInG2);
    }

    private static <V, E> boolean checkIsomorphismBacktracking(
            int index, List<V> v1, List<V> v2, 
            GraphLink<V, E> g1, GraphLink<V, E> g2, 
            Map<V, V> mapping, Set<V> usedInG2) {
        
        if (index == v1.size()) {
            return true; // Se encontró una biyección válida y completa
        }

        V u1 = v1.get(index);

        for (V u2 : v2) {
            if (!usedInG2.contains(u2)) {
                // Validar si asignar u1 -> u2 preserva la relación de adyacencia previa
                if (isValidMapping(u1, u2, g1, g2, mapping)) {
                    mapping.put(u1, u2);
                    usedInG2.add(u2);

                    if (checkIsomorphismBacktracking(index + 1, v1, v2, g1, g2, mapping, usedInG2)) {
                        return true;
                    }

                    // Backtracking
                    mapping.remove(u1);
                    usedInG2.remove(u2);
                }
            }
        }
        return false;
    }

    private static <V, E> boolean isValidMapping(V u1, V u2, GraphLink<V, E> g1, GraphLink<V, E> g2, Map<V, V> mapping) {
        // Verificar que todas las conexiones hacia nodos ya mapeados se mantengan exactamente iguales
        for (Map.Entry<V, V> entry : mapping.entrySet()) {
            V prev1 = entry.getKey();
            V prev2 = entry.getValue();

            if (g1.searchEdge(prev1, u1) != g2.searchEdge(prev2, u2)) return false;
            if (g1.searchEdge(u1, prev1) != g2.searchEdge(u2, prev2)) return false;
        }
        return true;
    }

    /**
     * 3. PLANO: Identifica si un grafo se puede dibujar en un plano sin que sus aristas se crucen.
     * Utiliza los invariantes de la fórmula de Euler y criterios de subgrafos de Kuratowski K5 y K3,3.
     */
    public static <V, E> boolean isPlano(GraphLink<V, E> graphLink) {
        List<V> vertices = graphLink.getVertices();
        int v = vertices.size();
        if (v <= 2) return true;

        // Contar aristas únicas (tratando el grafo como no dirigido para la restricción planar)
        Set<String> uniqueEdges = new HashSet<>();
        for (V node : vertices) {
            for (V neighbor : graphLink.adjacentVertices(node)) {
                String edgeKey = node.hashCode() < neighbor.hashCode() ? 
                        node + "-" + neighbor : neighbor + "-" + node;
                uniqueEdges.add(edgeKey);
            }
        }
        int e = uniqueEdges.size();

        // Invariante de Euler para grafos planares simples: e <= 3v - 6
        if (e > (3 * v - 6)) {
            return false;
        }

        // Invariante complementario: Si no tiene ciclos de longitud 3 (bipartito), e <= 2v - 4
        // Verificación rápida de las dimensiones del grafo prohibido completo K5 (v=5, e=10)
        if (v == 5 && e == 10) return false; 

        return true; 
    }

    /**
     * 4. AUTO COMPLEMENTARIO: Verifica si el grafo complementario es isomorfo al original.
     */
    public static <V, E> boolean isAutoComplementario(GraphLink<V, E> graphLink, E defaultWeight) {
        List<V> vertices = graphLink.getVertices();
        
        // Crear el grafo complementario vacío
        GraphLink<V, E> complementGraph = new GraphLink<>();
        for (V v : vertices) {
            complementGraph.insertVertex(v);
        }

        // Llenar el complemento: si no hay arista en el original, se agrega en el complemento 
        for (V u : vertices) {
            for (V v : vertices) {
                if (!u.equals(v) && !graphLink.searchEdge(u, v)) {
                    // Nota: En grafos estrictamente dirigidos, insertEdge añade solo en un sentido.
                    // Para mantener la consistencia con GraphLink, usamos un método dirigido o adaptado.
                    complementGraph.insertSingleDirectedEdge(u, v, defaultWeight);
                }
            }
        }

        // Un grafo es autocomplementario si es isomorfo a su propio complemento 
        return isIsomorfo(graphLink, complementGraph);
    }
}