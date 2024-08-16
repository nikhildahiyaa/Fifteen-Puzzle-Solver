package fifteenpuzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class StateGraph {
  private Map<FifteenPuzzle, List<FifteenPuzzle>> adjList = new HashMap<>();
  HashMap<FifteenPuzzle, Integer> indexes = new HashMap<>();
  int index = -1;

  public void addEdge(FifteenPuzzle source, FifteenPuzzle destination) {
    if (!adjList.containsKey(source)) {
      addVertex(source);
    }

    if (!adjList.containsKey(destination)) {
      addVertex(destination);
    }

    adjList.get(source).add(destination);
  
  }

  public void hasVertex(FifteenPuzzle vertex) {
    if (adjList.containsKey(vertex)) {
      System.out.println("The Graph contains " + vertex + " as a vertex");
    } else {
      System.out.println("The Graph does not contain " + vertex + " as a vertex");
    }
  }

  public Boolean hasEdge(FifteenPuzzle source, FifteenPuzzle destination) {
    if (adjList.get(source).contains(destination)) {
      return true;
    } else {
      return false;
    }
  }

  public String printGraph() {
    StringBuilder builder = new StringBuilder();

    for (FifteenPuzzle vertex : adjList.keySet()) {
      builder.append(vertex.toString() + ": ");
      for (FifteenPuzzle node : adjList.get(vertex)) {
        builder.append(node.toString() + " ");
      }
      builder.append("\n");
    }
    return builder.toString();
  }

  private void addVertex(FifteenPuzzle vertex) {
    adjList.put(vertex, new LinkedList<FifteenPuzzle>());
    indexes.put(vertex, ++index);
  }
  
  public void removeVertex(FifteenPuzzle vertex) {
    adjList.remove(vertex);
    indexes.remove(vertex);

    Set<FifteenPuzzle> set = adjList.keySet();
    Iterator<FifteenPuzzle> iterator = set.iterator();

    while (iterator.hasNext()) {
      FifteenPuzzle v = iterator.next();
      List<FifteenPuzzle> list = adjList.get(v);
      for (int i = 0; i < list.size(); i++) {
        if (list.contains(vertex))
          list.remove(vertex);
      }
    }
  }

  public int edgeValue(FifteenPuzzle node, FifteenPuzzle neighbour){
    int val = 0;
    
    val = node.getCost() + 1;
    
    return val;
  }
	
}