public class MatchStat {
    public final int index;
    public final int compares;
    public final int preprocess;

    public MatchStat(int index, int compares, int preprocess) {
        this.index = index;
        this.compares = compares;
        this.preprocess = preprocess;
    }

    public int total() { return compares + preprocess; }
}

