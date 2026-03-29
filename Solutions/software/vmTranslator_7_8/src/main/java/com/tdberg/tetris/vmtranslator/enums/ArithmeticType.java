package com.tdberg.tetris.vmtranslator.enums;

/**
 * Enum detailing the various arithmetic and logic operations.
 */
public enum ArithmeticType {
    INVALID ("invalid"),
    ADD ("add"),
    SUB ("sub"),
    NEG ("neg"),
    EQ ("eq"),
    GT ("gt"),
    LT ("lt"),
    AND ("and"),
    OR ("or"),
    NOT ("not");

    public final String command;

    ArithmeticType(final String command) {
        this.command = command;
    }
}
