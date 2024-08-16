package fifteenpuzzle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Solver {

	private static List<FifteenPuzzle> buildPath(AStarNodeWrapper nodeWrapper) {
		List<FifteenPuzzle> path = new ArrayList<>();
		while (nodeWrapper != null) {
		  path.add(nodeWrapper.getNode());
		  nodeWrapper = nodeWrapper.getPredecessor();
		}
		Collections.reverse(path);
		return path;
	  }

	public static List<FifteenPuzzle> findShortestPath(
      StateGraph graph, FifteenPuzzle source) {

		Map<FifteenPuzzle, AStarNodeWrapper> nodeWrappers = new HashMap<>();
		TreeSet<AStarNodeWrapper> queue = new TreeSet<>();
		Set<FifteenPuzzle> shortestPathFound = new HashSet<FifteenPuzzle>();
	
		// Add source to queue
		//int tile = source.curSolved().size()+1;
		
		AStarNodeWrapper sourceWrapper =
			new AStarNodeWrapper(source, null, 0.0, source.getHeuristic());
		nodeWrappers.put(source, sourceWrapper);
		queue.add(sourceWrapper);
		//source.score(tile);
		double prevScore = source.getScore();
		while (!queue.isEmpty()) {
			AStarNodeWrapper nodeWrapper = queue.pollFirst();
			FifteenPuzzle node = nodeWrapper.getNode();
			shortestPathFound.add(node);

			// Have we reached the target? --> Build and return the path
			if (node.isSolved()) {
			  return buildPath(nodeWrapper);
			}
			// The algorithm should not choose states that lower the score
			// Iterate over all neighbors
			AdjacentVerticies adj = new AdjacentVerticies();
			Set<FifteenPuzzle> neighbors = adj.adjNodes(node);
			
			for (FifteenPuzzle neighbor : neighbors) {
			  // Ignore neighbor if shortest path already found
		

			  if (shortestPathFound.contains(neighbor)) {
				continue;
			  }
			  //update cost
			  double totalCostFromStart = nodeWrapper.getTotalCostFromStart() + 1;
	  
			  // Neighbor not yet discovered?
			  AStarNodeWrapper neighborWrapper = nodeWrappers.get(neighbor);
			  if (neighborWrapper == null) {
				neighborWrapper =
					new AStarNodeWrapper(
						neighbor, nodeWrapper, totalCostFromStart, neighbor.getHeuristic());
				nodeWrappers.put(neighbor, neighborWrapper);
				queue.add(neighborWrapper);
			  }
	  
			  // Neighbor discovered, but total cost via current node is lower?
			  // --> Update costs and predecessor
			  else if (totalCostFromStart < neighborWrapper.getTotalCostFromStart()) {
				// The position in the TreeSet won't change automatically;
				// we have to remove and reinsert the node.
				// Because TreeSet uses compareTo() to identity a node to remove,
				// we have to remove it *before* we change the cost!
				queue.remove(neighborWrapper);
				neighborWrapper.setTotalCostFromStart(totalCostFromStart);
				neighborWrapper.setPredecessor(nodeWrapper);
				queue.add(neighborWrapper);
			  }
			}
		  }
	  
		  // All nodes were visited but the target was not found

	return null;
	  }

	public static void main(String[] args) throws IOException, BadBoardException {
		System.out.println("number of arguments: " + args.length);
		for (int i = 0; i < args.length; i++) {
		System.out.println(args[i]);
		}

		if (args.length < 2) {
			System.out.println("File names are not specified");
			System.out.println("usage: java " + MethodHandles.lookup().lookupClass().getName() + " input_file output_file");
			return;
		}
		FifteenPuzzle in = new FifteenPuzzle(args[0]);	
		StateGraph graph = new StateGraph();
		try {
		
		List<FifteenPuzzle> solvedList = Solver.findShortestPath(graph, in);

		Iterator<FifteenPuzzle> it = solvedList.iterator();
		String funcString = ""; //String from the list derived from shortestPath
		while(it.hasNext()){
			funcString += it.next().move;
		}
		funcString = funcString.substring(0, funcString.length()-1);
		//System.out.println(outString.length());
		
			System.out.println("solution");
			System.out.println(funcString);
			System.out.println("Moves Required " + (solvedList.size()-1));
			System.out.println(solvedList.get(solvedList.size()-1));
			File out = new File(args[1]);
			// passing file instance in filewriter
		FileWriter wr = new FileWriter(out);
			// calling writer.write() method with the string
		wr.write(funcString);
			// flushing the writer
		wr.flush();
			// closing the writer
		wr.close();
		}
		catch (NullPointerException e) {
			// TODO: handle exception
		}

	}
}