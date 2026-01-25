package com.tdberg.tetris.enums;

/**
 * Enum detailing the various destination types that can occur 
 * in Hack assembly.
 */
public enum DestType {
    NONE ("000"),
    M    ("001"),
    D    ("010"),
    MD   ("011"),
    A    ("100"),
    AM   ("101"),
    AD   ("110"),
    AMD  ("111");

    public final String bitString;

    DestType(final String bitString) {
        this.bitString = bitString;
    }
}
