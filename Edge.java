package fifteenpuzzle;



public class Edge {
    private FifteenPuzzle start;
    private FifteenPuzzle end;

    public Edge(FifteenPuzzle startV, FifteenPuzzle endV, Integer inputWeight) {
        this.start = startV;
        this.end = endV;
    }

    public FifteenPuzzle getStart() {
        return this.start;
    }

    public FifteenPuzzle getEnd() {
        return this.end;
    }

 
    

}