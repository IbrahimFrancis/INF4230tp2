package inf4230.connect5.ia;

import inf4230.connect5.Position;

public class PositionWithEval{
    private Position pos;
    private int eval = 0;

    public PositionWithEval(Position pos, int eval){
        this.pos = pos;
        this.eval = eval;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public int getEval() {
        return eval;
    }

    public void setEval(int eval) {
        this.eval = eval;
    }
}