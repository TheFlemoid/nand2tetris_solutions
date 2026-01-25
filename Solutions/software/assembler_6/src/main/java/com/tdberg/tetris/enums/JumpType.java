package com.tdberg.tetris.enums;

/**
 * Enum detailing the various jump types that can occur 
 * in Hack assembly.
 */
public enum JumpType {
    NONE ("000"),
    JGT  ("001"),
    JEQ  ("010"),
    JGE  ("011"),
    JLT  ("100"),
    JNE  ("101"),
    JLE  ("110"),
    JMP  ("111");

    public final String bitString;

    JumpType(final String bitString) {
        this.bitString = bitString;
    }
}
