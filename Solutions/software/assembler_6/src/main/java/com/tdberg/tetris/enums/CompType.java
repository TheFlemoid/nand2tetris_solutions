package com.tdberg.tetris.enums;

/**
 * Enum detailing the various compute types that can occur 
 * in Hack assembly.
 */
public enum CompType {
    ZERO        ("0101010", "0"),
    ONE         ("0111111", "1"),
    NEG_ONE     ("0111010", "-1"),
    D           ("0001100", "D"),
    A           ("0110000", "A"),
    NOT_D       ("0001101", "!D"),
    NOT_A       ("0110001", "!A"),
    NEG_D       ("0001111", "-D"),
    NEG_A       ("0110001", "-A"),
    D_PLUS_ONE  ("0011111", "D+1"),
    A_PLUS_ONE  ("0110111", "A+1"),
    D_MINUS_ONE ("0001110", "D-1"),
    A_MINUS_ONE ("0110010", "A-1"),
    D_PLUS_A    ("0000010", "D+A"),
    D_MINUS_A   ("0010011", "D-A"),
    A_MINUS_D   ("0000111", "A-D"),
    D_AND_A     ("0000000", "D&A"),
    D_OR_A      ("0010101", "D|A"),
    M           ("1110000", "M"),
    NOT_M       ("1110001", "!M"),
    NEG_M       ("1110011", "-M"),
    M_PLUS_ONE  ("1110111", "M+1"),
    M_MINUS_ONE ("1110010", "M-1"),
    D_PLUS_M    ("1000010", "D+M"),
    D_MINUS_M   ("1010011", "D-M"),
    M_MINUS_D   ("1000111", "M-D"),
    D_AND_M     ("1000000", "D&M"),
    D_OR_M      ("1010101", "D|M");

    public final String bitString;
    public final String mnemonic;

    CompType(final String bitString, final String mnemonic) {
        this.bitString = bitString;
        this.mnemonic = mnemonic;
    }
}
