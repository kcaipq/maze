package uk.gov.dwp.maze;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.containsString;

/**
 * Created by kcai on 28/10/2014.
 */
public class ExplorerTest {

    private Maze maze;
    private Explorer ex;

    @Before
    public void before() {
        resetMaze();
    }

    @Test
    public void testExplorerHasMaze() {
        Object m = TestUtil.getField(ex, "maze", Maze.class);

        Assert.assertNotNull(m);
        Assert.assertTrue(m instanceof Maze);
    }

    @Test
    public void testExplorerMazeNullSafety() {
        try {
            new Explorer(null);
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString("Null maze in explorer"));
        }
    }

    @Test
    public void testExplorerCanBeDroppedAtStartPoint() {
        Square startSquare = maze.getStartSquare();
        Assert.assertEquals(3, startSquare.getRow());
        Assert.assertEquals(3, startSquare.getColumn());

        ex.exploreMaze();

        String finalPathString = ex.printExplorersPath();
        Square[][] outputPath = MazeFactory.buildMazeMapFromString(finalPathString);

        Square outputSquare = MazeUtil.findSquareByState(outputPath, SquareState.START);
        Assert.assertEquals(3, outputSquare.getRow());
        Assert.assertEquals(3, outputSquare.getColumn());

        Assert.assertEquals(outputSquare, startSquare);
    }

    @Test
    public void testExplorerCanMoveForward() {
        // setting the new exit square
        TestUtil.setExitSquare(maze.getSquare(), maze.getExitSquare(), 3, 4, SquareState.WALLED);
        ex.exploreMaze();

        Assert.assertNotNull(maze.getPath().getPaths());
        Assert.assertEquals(2, maze.getPath().getPaths().size());
        Assert.assertEquals("(3, 4, EXIT)",  maze.getPath().getPaths().pop().toString());
        Assert.assertEquals("(3, 3, START)", maze.getPath().getPaths().pop().toString());

        resetMaze();
        ex = new Explorer(maze);
        TestUtil.setExitSquare(maze.getSquare(), maze.getExitSquare(), 3, 5, SquareState.OPEN);
        ex.exploreMaze();

        Assert.assertNotNull(maze.getPath().getPaths());
        Assert.assertEquals(3, maze.getPath().getPaths().size());
        Assert.assertEquals("(3, 5, EXIT)",  maze.getPath().getPaths().pop().toString());
        Assert.assertEquals("(3, 4, OPEN)",  maze.getPath().getPaths().pop().toString());
        Assert.assertEquals("(3, 3, START)", maze.getPath().getPaths().pop().toString());
    }

    @Test
    public void testExplorerCanTurn() {
        TestUtil.setExitSquare(maze.getSquare(), maze.getExitSquare(), 5, 11, SquareState.WALLED);
        ex.exploreMaze();

        Assert.assertEquals(11, maze.getPath().getPaths().size());
        Assert.assertTrue(maze.getPath().getPaths().contains(new Square(3, 11, SquareState.OPEN))); // last straight square
        Assert.assertTrue(maze.getPath().getPaths().contains(new Square(4, 11, SquareState.OPEN))); // first square after turned.
    }

    /**
     * assume explorer can move backward and backward movement option is counted as valid for a chosen location.
     */
    @Test
    public void testMovementOptionsForChosenLocation() {
        Set<Square> options = ex.getMovementOptions(maze, 6, 11);
        Assert.assertNotNull(options);
        Assert.assertEquals(3, options.size());
        Assert.assertTrue(options.contains(new Square(5, 11, SquareState.OPEN)));// left open square
        Assert.assertTrue(options.contains(new Square(6, 10, SquareState.OPEN)));// left open square
        Assert.assertTrue(options.contains(new Square(7, 11, SquareState.OPEN)));// down open square
    }

    /**
     * Explorer should randomly choose one open square to proceed.
     *
     * Location (6, 11) has two options, so explorer can choose to go down or left.
     * Down: dead end, it will come back up and eventually go to the left.
     * Left: happy path
     */
    @Test
    public void testExplorerFaceDeadAndHappyPaths(){
        TestUtil.setExitSquare(maze.getSquare(), maze.getExitSquare(), 6, 8, SquareState.WALLED);
        ex.exploreMaze();
        Assert.assertFalse(maze.getPath().getPaths().contains(new Square(6, 12, SquareState.OPEN))); // final path doesn't contain the squares lead to the dead end.
    }

    @Test
    public void testExplorerFaceMultipleHappyPaths() {
        TestUtil.setSquare(maze.getSquare(), 5, 10, SquareState.OPEN); // modify map to create location where there are two happy paths.
        TestUtil.setExitSquare(maze.getSquare(), maze.getExitSquare(), 6, 7, SquareState.WALLED);
        ex.exploreMaze();

        // final happy path either contains left square or down square, see below traffic coming from top -> down
        //                    *
        //                    *
        //                *(*)*
        //                   (*)
        Assert.assertTrue(maze.getPath().getPaths().contains(new Square(5, 10, SquareState.OPEN)) ||
                maze.getPath().getPaths().contains(new Square(6, 11, SquareState.OPEN)));

    }

    @Test
    public void testExplorerKeepsRecordOfExploredPath() {
        ex.exploreMaze();
        Assert.assertNotNull(maze.getExplored());
        Assert.assertTrue(maze.getExplored().length > 0);
    }

    @Test
    public void testExplorerKeepsRecordOfFinalHappyPath() {
        ex.exploreMaze();
        Assert.assertNotNull(maze.getPath().getPaths());
        Assert.assertTrue(maze.getPath().getPaths().size() > 0);
        Assert.assertEquals(74, maze.getPath().getPaths().size());
        //Exit exists in the top of the path stack.
        Assert.assertEquals(new Square(14, 1, SquareState.EXIT),  maze.getPath().getPaths().peek());
    }

    @Test
    public void testWallBetweenStartAndExitShouldHaveNoSolution() {
        TestUtil.setSquare(maze.getSquare(), 3, 4, SquareState.WALLED);
        TestUtil.setSquare(maze.getSquare(), 3, 5, SquareState.EXIT);
        TestUtil.setSquare(maze.getSquare(), 14, 1, SquareState.WALLED);

        ex.exploreMaze();
        Assert.assertNotNull(maze.getPath().getPaths());
        Assert.assertEquals(0, maze.getPath().getPaths().size());
    }

    private void resetMaze() {
        MazeBuilder builder = new FileMazeBuilder("src/test/resources/maze.txt");
        maze = builder.build();
        ex = new Explorer(maze);
    }
}
