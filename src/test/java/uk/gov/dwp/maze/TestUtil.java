package uk.gov.dwp.maze;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by kcai on 28/10/2014.
 */
public class TestUtil {

    public static Object getField(Object target, String name, Class<?> type) {
        Field field = findField(target.getClass(), name, type);
        makeAccessible(field);
        return getField(field, target);
    }

    public static Field findField(Class<?> clazz, String name, Class<?> type) {
        Class<?> searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    public static Object getField(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException ex) {

        }
           return null;
    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * Simple and handy method to reset the exit square to any point chosen in the map. This is for testing only.
     *
     * @param squares the maze map.
     * @param existingExitPoint the existing exit square in the maze map.
     * @param xOrdinal the new x ordinal for the new exit square
     * @param yOrdinal the new y ordinal for the new exit square.
     * @param state the state the previous exit point becomes to
     */
    public static void setExitSquare(Square[][] squares, Square existingExitPoint, int xOrdinal, int yOrdinal, SquareState state) {
        int yLength = squares[0].length - 1;
        if (xOrdinal < 0 || xOrdinal> squares.length - 1 || yOrdinal < 0 || yOrdinal > yLength) {
            return; // does nothing.
        }

        int x = existingExitPoint.getRow();
        int y = existingExitPoint.getColumn();

        //replacing existing square with a wall.
        squares[x][y] = new Square(x, y, state);

        //setting the new x and y the new exit point.
        squares[xOrdinal][yOrdinal] = new Square(xOrdinal,yOrdinal, 'F');
        System.out.println(squares);
    }

    public static void setSquare(Square[][] squares, int xOrdinal, int yOrdinal, SquareState state) {
        squares[xOrdinal][yOrdinal] = new Square(xOrdinal, yOrdinal, state);
    }
}