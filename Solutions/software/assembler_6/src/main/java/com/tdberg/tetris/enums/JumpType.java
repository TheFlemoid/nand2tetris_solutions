package com.tdberg.tetris.assembler.enums;

/**
 * Enum detailing the various jump types that can occur 
 * in Hack assembly.
 */
public enum JumpType {
    NONE ("000", "NONE"),
    JGT  ("001", "JGT"),
    JEQ  ("010", "JEQ"),
    JGE  ("011", "JGE"),
    JLT  ("100", "JLT"),
    JNE  ("101", "JNE"),
    JLE  ("110", "JLE"),
    JMP  ("111", "JMP");

    public final String bitString;
    public final String mnemonic;

    JumpType(final String bitString, final String mnemonic) {
        this.bitString = bitString;
        this.mnemonic = mnemonic;
    }
}
