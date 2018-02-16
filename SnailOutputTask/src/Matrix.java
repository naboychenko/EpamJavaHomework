import java.awt.*;

/**
 * Created by Viktoria on 07.02.2018.
 */
public class Matrix {

    final public int rows;
    final public int columns;

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public int getData(int i, int j){

        return i * columns + j;
    }

    public void show()
    {
        System.out.println("Matrix:");
        for(int i = 0; i < this.rows; i++)
        {
            for(int j = 0; j < this.columns; j++) {
                System.out.print(getData(i, j) + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void showSnailOrder(){
        System.out.println("Snail order:");
        SnailPathGenerator spg = new SnailPathGenerator(this);
        Point currentPosition;

        for(int i = 0; i < rows * columns; i++){
            currentPosition = spg.getNext();
            System.out.print(getData(currentPosition.x, currentPosition.y) + " ");
        }
    }
}
