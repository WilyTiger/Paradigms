package expression;

public class Variable<T> implements TripleExpression<T> {
    private final String name;

    public Variable(String newName) {
        name = newName;
    }

    public T evaluate(T x, T y, T z) {
        switch (name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }
        return x;
    }
}
