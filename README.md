## Assumptions
1) assume the explorer robot can only move 1 step in one of 4 directions. Valid moves are:
    a) Up: (x,y) -> (x,y-1)
    b) Right East: (x,y) -> (x+1,y)
    c) Down: (x,y) -> (x,y+1)
    d) Left: (x,y) -> (x-1,y)
    note: positions are noted in zero-based coordinates

2) assume robot can only move through spaces " " within the maze
3) the explorer should search for a path from the starting position "S" to the "F" position until it finds one or until it exhausts all possibilities. 
In addition, it should mark the path it finds (if any) in the maze.
4) assume the maze map is sealed completed in a way that no empty spaces are allowed in each edge of the maze map.
5) assume the explorer will randomly select an open route to proceed if there were more than one adjacent empty spaces to current square/cell.
6) assume explorer can move backward and backward movement option is counted as valid for a chosen location.
7) assume only one final route is required to print out in the console. Output for each steps taken is not available at the moment.
8) assume console system.out is accepted method of presenting the output.
9) assume no maven assembly and package automation work required.
10) lastly assume I would have got more time to get this done better. :P

## TODO
1) adding output in console for each exploration step this agents takes
2) maybe rewrite the code to find the adjacent open routes (not happy about it)
3) may be refactoring the code to accept explorer strategies - random route strategy, all possible routes strategy or shortest path strategy for example.
4) extract out the printing code out and make it a helper class.
5) adding javadoc - due to the lack of time I won't submit the code with complete java doc.