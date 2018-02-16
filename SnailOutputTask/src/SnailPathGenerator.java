import java.awt.*;

/**
 * Created by Viktoria on 09.02.2018.
 */
public class SnailPathGenerator {
    private int rows;
    private int columns;
    private Point leftTopCorner;
    private Point rightTopCorner;
    private Point rightBottomCorner;
    private Point leftBottomCorner;
    private Point delta;
    private Point currentPosition;
    private int order;

    public SnailPathGenerator(Matrix m) {
        this.rows = m.rows;
        this.columns = m.columns;
        this.leftTopCorner  = new Point(1, 0);
        this.rightTopCorner = new Point(0, columns - 1);
        this.rightBottomCorner = new Point(rows - 1, columns - 1);
        this.leftBottomCorner = new Point(rows - 1, 0);
        this.delta = new Point(0, 1);
        this.currentPosition = new Point(0, -1);
        this.order = 1;
    }

    public Point getNext(){

        currentPosition.translate(delta.x, delta.y);

        if(order == 0 && currentPosition.equals(leftTopCorner)){
            delta.setLocation(0, 1);
            leftTopCorner.translate(1, 1);
            order++;
        }

        if(order == 1 && currentPosition.equals(rightTopCorner)){
            delta.setLocation(1, 0);
            rightTopCorner.translate(1, -1);
            order++;
        }

        if(order == 2 && currentPosition.equals(rightBottomCorner)){
            delta.setLocation(0, -1);
            rightBottomCorner.translate(-1, -1);
            order++;
        }

        if(order == 3 && currentPosition.equals(leftBottomCorner)){
            delta.setLocation(-1, 0);
            leftBottomCorner.translate(-1, 1);
            order = 0;
        }

        return currentPosition;
    }
}
