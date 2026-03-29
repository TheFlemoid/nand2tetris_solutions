package com.tdberg.tetris.vmtranslator.enums;

/**
 * Enum detailing the various stack machine command types.
 */
public enum CommandType {
    INVALID,
    C_ARITHMETIC,
    C_PUSH,
    C_POP,
    C_LABEL,
    C_GOTO,
    C_IF,
    C_FUNCTION,
    C_RETURN,
    C_CALL;

    CommandType() {
    }
}
