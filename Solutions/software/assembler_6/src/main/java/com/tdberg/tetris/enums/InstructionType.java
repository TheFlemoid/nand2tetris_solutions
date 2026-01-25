package com.tdberg.tetris.enums;

/**
 * Enum detailing the various instruction types that can occur in Hack
 * assembly.
 */
public enum InstructionType {
    NULL_INSTRUCTION, // Not an instruction
    A_INSTRUCTION,    // Address instruction
    C_INSTRUCTION,    // Command instruction
    L_INSTRUCTION;    // Link (pseudo instruction) instruction

    InstructionType() {
    }
}
