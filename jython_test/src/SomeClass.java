/**
 * A very boring Java class.
 *
 * @author sp611
 *
 */
public final class SomeClass {
    
    private final String name;

    public SomeClass(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getNanoTime() {
        return System.nanoTime();
    }

}
