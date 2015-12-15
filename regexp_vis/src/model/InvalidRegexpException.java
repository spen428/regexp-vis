package model;

/**
 * Exception thrown during parsing of an invalid regular expression
 */
public class InvalidRegexpException extends Exception {
    public InvalidRegexpException(String msg)
    {
        super(msg);
    }
}
