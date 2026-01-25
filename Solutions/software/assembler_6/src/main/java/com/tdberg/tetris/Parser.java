package com.tdberg.tetris;

import com.tdberg.tetris.enums.InstructionType;
import com.tdberg.tetris.enums.DestType;
import com.tdberg.tetris.enums.JumpType;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

/**
 * Parses the input Hack assembly file line by line, and returns information
 * about each line as it comes into scope.
 */
public class Parser {

    private String filepath;
    private RandomAccessFile sourceFile;
    private String[] inputProgram;
    private String currentInstruction;

    private static final String lRegex = "(?<!\\/\\/\s?)\\(.*\\)";
    private static final String aRegex = "(?<!\\/\\/\s?)@[0-9a-zA-Z_$:.]*[^\s]";

    private long currentFp;

    /**
     * Default constructor
     *
     * @param hackFilePath path to the input Hack assembly program
     */
    public Parser(final String hackFilepath) {
        this.filepath = hackFilepath;

        //try {
        //    sourceFile = new RandomAccessFile(filepath, "r");
        //}catch (IOException e) {
        //    System.out.printf("IOException when parsing input file %s : %s" + 
        //                      "\n\nExiting.\n", filepath, e.getMessage());
        //    System.exit(1);
        //}

        String[] instructions = {"(ITSR0)", "//(ITSR0)", "@test", "D=M", "0;JMP", "@1130"};

        for (int i = 0; i < instructions.length; i++) {
            if (isValidInstruction(instructions[i])) {
                System.out.println(getInstructionType(instructions[i]).name());
            } else {
                System.out.println("Not an instruction");
            }
        }
    }

    ///**
    // * True if more commands (A or C instructions) are left in the input
    // * asm file, false otherwise.
    // *
    // * @return true if more commands remain in the file, false otherwise
    // */
    //public boolean hasMoreCommand() {

    //}

    ///**
    // * Scroll the input file to the next A or C instruction, making that the
    // * current instruction.
    // */
    //public void advance() {

    //}

    /**
     * Returns the InstructionType (A, C, or L) of the current instruction.
     *
     * @return the instruction type of the current instruction
     */
    public InstructionType getInstructionType(final String instruction) {
        if (Pattern.matches(aRegex, instruction)) {
            return InstructionType.A_INSTRUCTION;
        } else if (Pattern.matches(lRegex, instruction)) {
            return InstructionType.L_INSTRUCTION;
        } else {
            String testInstruction = reduceCInstruction(instruction);
            if (testInstruction.contains("=") || testInstruction.contains(";")) {
                return InstructionType.C_INSTRUCTION;
            }
        }

        return InstructionType.NULL_INSTRUCTION;
    }

    ///**
    // * Returns the symbol given by the current instruction.
    // * NOTE: This should ONLY be called for A or L-instructions, and will 
    // *       return gibberish for C-instructions.
    // *
    // * @return the symbol given by the current instruction as a String
    // */
    //public String getSymbol() {

    //}

    ///**
    // * Returns the destination flags (d1, d2, d3) for the current instruction
    // * as a String of 0/1 characters.
    // * NOTE: This should ONLY be called on C-instructions, and will return gibberish
    // *       for A or L instructions.
    // *
    // * @return the destination flags for the current instruction as a String
    // */
    //public String getDest() {

    //}

    ///**
    // * Returns the compute flags (c1-c6) for the current instruction as a
    // * String of 0/1 characters.
    // * NOTE: This should ONLY be called on C-instructions, and will return gibberish
    // *       for A or L instructions.
    // *
    // * @return the compute flags for the current instruction as a String
    // */
    //public String getComputeFlags() {

    //}

    ///**
    // * Returns the jump flags (j1, j2, j3) for this instruction as a String of 
    // * 0/1 characters.
    // * NOTE: This should ONLY be called on C-instructions, and will return gibberish
    // *       for A or L instructions.
    // *
    // * @return the jump flags for the current instruction as a String
    // */
    //public String getJump() {

    //}

    ///**
    // * Returns the current command as a String.
    // *
    // * @return the current command as a String
    // */
    //public String getCurrentCommand() {

    //}

    /**
     * Returns true if the param String is a valid instruction, false otherwise.
     *
     * @param instructionTest String to test
     * @return true if the param String is a valid instruction, false otherwise
     */
    private boolean isValidInstruction(final String instructionTest) {

        // Early return false if string is empty
        if (instructionTest.isEmpty()) {
            return false;
        }

        // A and L instructions are easy to regex, C instructions are not
        if (Pattern.matches(lRegex, instructionTest) ||
            Pattern.matches(aRegex, instructionTest)) {
            return true;
        }

        // If the instruciton contains either '=' or 'j', this could be a C-Instruction
        if (instructionTest.contains("=") || instructionTest.contains(";")) {
            String reducedInstruction = reduceCInstruction(instructionTest);

            if (reducedInstruction.contains("=") || reducedInstruction.contains(";")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Reduces a potential C-Instruction line down to it's standard operation, removing 
     * comments and whitespace.
     * NOTE: If the provided String is not really a C-Instruction (eg. "//D=M+A;JMP",
     *       where this line is actually a comment that looks like a C-Instruction),
     *       this method will return a blank String.
     */
    private String reduceCInstruction(final String cInstruction) {
        char[] instructionArray = cInstruction.toCharArray();
        char[] commentBuffer = new char[2];
        char[] goodBuffer = new char[instructionArray.length];

        String retString = "";

        // Doing a pass through each character of the instruction, breaking if there's a
        // comment start indicator
        if (instructionArray.length >= 2) {
            commentBuffer[0] = instructionArray[0];
            commentBuffer[1] = instructionArray[1];
            boolean commentHit = false;

            for(int i = 2; (i < instructionArray.length) && !commentHit; i++) {
            if (commentBuffer[0] == '/' && commentBuffer[1] == '/') {
                    commentHit = true;
                } else {
                    goodBuffer[i-2] = commentBuffer[0];
                    commentBuffer[0] = commentBuffer[1];
                    commentBuffer[1] = instructionArray[i];
                }
            }

            if (!commentHit) {
                goodBuffer[goodBuffer.length - 2] = commentBuffer[0];
                goodBuffer[goodBuffer.length - 1] = commentBuffer[1];
            }

            // Reassemble the buffer into a String for further work 
            retString = new String(goodBuffer);

            // Get rid of all whitespace in the new String
            retString = retString.replaceAll("\\s+", "");
        }

        return retString;
    }
}

