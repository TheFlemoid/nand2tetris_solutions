package com.tdberg.tetris.assembler.enums;

/**
 * Enum detailing the various destination types that can occur 
 * in Hack assembly.
 */
public enum DestType {
    NONE ("000", "NONE"),
    M    ("001", "M"),
    D    ("010", "D"),
    MD   ("011", "MD"),
    A    ("100", "A"),
    AM   ("101", "AM"),
    AD   ("110", "AD"),
    AMD  ("111", "AMD");

    public final String bitString;
    public final String mnemonic;

    DestType(final String bitString, final String mnemonic) {
        this.bitString = bitString;
        this.mnemonic = mnemonic;
    }
}
