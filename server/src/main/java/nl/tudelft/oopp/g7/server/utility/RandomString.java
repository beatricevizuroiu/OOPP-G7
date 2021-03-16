package nl.tudelft.oopp.g7.server.utility;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * A class to generate random strings.
 * Original credit to: https://stackoverflow.com/a/41156/14195045
 */
public class RandomString {
    public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER = UPPER.toLowerCase(Locale.ROOT);
    public static final String DIGITS = "0123456789";
    public static final String ALPHANUMERIC = UPPER + LOWER + DIGITS;

    private final Random random;
    private final char[] symbols;
    private final char[] buf;

    /**
     * Create a random string generator for strings with length containing only symbols from symbols.
     * @param length The length of the strings to be generated.
     * @param random The random to use for generation.
     * @param symbols The symbols to use as a String.
     */
    public RandomString(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create a random string generator for strings with length containing only symbols from symbols.
     * @param length The length of the random strings.
     * @param symbols The symbols to use in the string.
     */
    public RandomString(int length, String symbols) {
        this(length, new SecureRandom(), symbols);
    }

    /**
     * Create a random string generator for strings with length.
     * @param length The length of the random strings.
     */
    public RandomString(int length) {
        this(length, new SecureRandom(), ALPHANUMERIC);
    }

    /**
     * Generate a random string.
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }
}

