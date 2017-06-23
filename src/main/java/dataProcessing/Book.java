package dataProcessing;

/**
 * Created by dlo on 19/06/17.
 */
public  class Book {
    private final String name;
    private final String type;
    private final int id;
    public Book(String name, String type, int id) {
        this.name = name;
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName() + ":" +getType();
    }

    public int getId() {
        return this.id;
    }
}
