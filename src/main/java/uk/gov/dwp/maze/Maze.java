package uk.gov.dwp.maze;

import java.util.ArrayList;
import java.util.List;

public class Maze extends MazeFactory {

    private Square[][] maze;
    private boolean[][] explored;
    private Path path;
    private int height;
    private int width;

    public Maze(final Square[][] maze) {
        if (maze == null)
            throw new IllegalArgumentException("Cannot have null map in Maze");
        this.maze = maze;
        this.path = new Path();
        this.height = maze.length;
        this.width = maze[0].length;
        this.explored = new boolean[height][width];
    }

    /**
     * Get the path of the very final route.
     * @return the Path stack that leads to solution.
     */
    public Path getPath() {
        return path;
    }

    /**
     * Mark a coordinate using the passed in flag.
     *
     * @param x the row
     * @param y the column
     * @param visited the flag whether this coordinate is visited.
     */
    public void markExplored(int x, int y, boolean visited) {
        if (!validateCoordinates(x, y))
            throw new IllegalArgumentException("Coordinates x: " + x + ", y: " + y + " is not valid");
        explored[x][y] = visited;
        path.addPath(maze[x][y]);
    }

    /**
     * Whether the coordinate square has been explored already.
     *
     * @param x the row
     * @param y the column
     * @return true - explored or otherwise false
     */
    public boolean isExplored(final int x, final int y) {
        return explored[x][y];
    }

    public boolean [][] getExplored() {
        return explored;
    }

    /**
     * @return instance of the current maze 2-d array, this is not null safe.
     */
    public Square[][] getSquare() {
        return this.maze;
    }

    /**
     * coordinates start from 0 ...
     *
     * @param x the row
     * @param y the column
     * @return the square located at the specific x and y coordinate.
     */
    public Square getSquare(final int x, final int y) {
        if (!validateCoordinates(x, y))
            return null;
        return maze[x][y];
    }

    /**
     * Get the ending square
     *
     * @return {@link uk.gov.dwp.maze.Square} represents the ending point.
     */
    public Square getExitSquare() {
        List<Square> exits = findSquare(SquareState.EXIT);
        return exits.isEmpty() ? null : exits.get(0);
    }

    /**
     * Get the starting square
     *
     * @return {@link uk.gov.dwp.maze.Square} represents the starting point.
     */
    public Square getStartSquare() {
        List<Square> starts = findSquare(SquareState.START);
        return starts.isEmpty() ? null : starts.get(0);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getOpenSpacesCount() {
        return findSquare(SquareState.OPEN).size();
    }

    public int getWallCount() {
        return findSquare(SquareState.WALLED).size();
    }

    public int getStartCount() {
        return findSquare(SquareState.START).size();
    }

    public int getExitCount() {
        return findSquare(SquareState.EXIT).size();
    }

    /**
     * Handy method to count squares for a specific state. Null safe method.
     *
     * @param state {@link uk.gov.dwp.maze.SquareState}
     * @return the occurrences of the SquareState in the map.
     */
    private List<Square> findSquare(final SquareState state) {
        List<Square> results = new ArrayList<Square>();
        for (int i = 0 ; i < getHeight(); i ++) {
            for (int j = 0; j < getWidth(); j++) {
                Square s = getSquare(i, j);
                if (s.getState() == state)
                    results.add(s);
            }
        }
        return results;
    }

    private boolean validateCoordinates(int row, int col) {
        return !(row < 0 || row >= getHeight() || col < 0 || col >= getWidth());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(getWidth() * getHeight());
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                if (maze[row][col].isWalled()) {
                    result.append('X');
                } else if (maze[row][col].isStart()) {
                    result.append('S');
                } else if (maze[row][col].isExit()) {
                    result.append('F');
                } else if (explored[row][col]) {
                    result.append('.');
                } else {
                    result.append(' ');
                }
            }
            result.append('\n');
        }
        return result.toString();
    }
}
