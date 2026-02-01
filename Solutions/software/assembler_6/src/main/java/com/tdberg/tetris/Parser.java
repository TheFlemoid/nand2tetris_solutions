/**
 * Assembler parser implementation for Hack assembly, made
 * during completion of The Elements of Computing Systems.
 *
 * Created by Trey Dahlberg, 2026.
 */
package com.tdberg.tetris.assembler;

import com.tdberg.tetris.assembler.enums.InstructionType;
import com.tdberg.tetris.assembler.enums.CompType;
import com.tdberg.tetris.assembler.enums.DestType;
import com.tdberg.tetris.assembler.enums.JumpType;

import java.io.EOFException;
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
    private InstructionType currentInstructionType;
    private String currentInstruction;

    private static final String lRegex = "(?<!\\/\\/\s?)\\(.*\\)";
    private static final String aRegex = "(?<!\\/\\/\s?)@[0-9a-zA-Z_$:.]*[^\s]";

    private long sourceFileLength;
    private long currentFp = 0;

    /**
     * Default constructor
     *
     * @param hackFilePath path to the input Hack assembly program
     */
    public Parser(final String hackFilepath) {
        this.filepath = hackFilepath;

        try {
            sourceFile = new RandomAccessFile(filepath, "r");
            sourceFileLength = sourceFile.length();
        }catch (IOException e) {
            System.out.printf("IOException when parsing input file %s : %s" + 
                              "\n\nExiting.\n", filepath, e.getMessage());
            System.exit(1);
        }
    }

    /**
     * True if more instructions are left in the input asm file, 
     * false otherwise.
     *
     * @return true if more instructions remain in the file, false otherwise
     */
    public boolean hasMoreInstructions() {

        String line;

        // Walk through the file, line by line until either:
        //  - we find a valid instruction, or
        //  - we reach the file end
        while(currentFp < sourceFileLength) {
            try {
                sourceFile.seek(currentFp);
                line = sourceFile.readLine();

                if (isValidInstruction(line)) {
                    return true;
                }else {
                    currentFp = sourceFile.getFilePointer();
                }
            }catch (EOFException e) {
                return false;
            }catch (IOException e) {
                System.out.println("IOException when parsing source " + 
                                   "file: " + e.getMessage());
                System.exit(1);
            }
        }

        return false;
    }

    /**
     * Scroll the input file to the next A or C instruction, making that the
     * current instruction.
     */
    public void advance() {
        try {
            sourceFile.seek(currentFp);
            currentInstruction = sourceFile.readLine();
            currentInstructionType = getInstructionType();
            currentFp = sourceFile.getFilePointer();
        }catch (EOFException e) {
            System.out.println("EOF Exception when advancing, this should" +
                               "not happen: " + e.getMessage());
        }catch (IOException e) {
            System.out.println("IOException when parsing source " + 
                               "file: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Returns the InstructionType (A, C, or L) of the current instruction.
     *
     * @return the instruction type of the current instruction
     */
    public InstructionType getInstructionType() {
        if (Pattern.matches(aRegex, currentInstruction)) {
            return InstructionType.A_INSTRUCTION;
        } else if (Pattern.matches(lRegex, currentInstruction)) {
            return InstructionType.L_INSTRUCTION;
        } else {
            String testInstruction = reduceCInstruction(currentInstruction);
            if (testInstruction.contains("=") || testInstruction.contains(";")) {
                return InstructionType.C_INSTRUCTION;
            }
        }

        return InstructionType.NULL_INSTRUCTION;
    }

    /**
     * Returns the symbol given by the current instruction.
     * NOTE: This should ONLY be called for A or L-instructions, and will 
     *       return gibberish for C-instructions.
     *
     * @return the symbol given by the current instruction as a String
     */
    public String getSymbol() {
        String retVal = "";
        
        if (currentInstructionType == InstructionType.A_INSTRUCTION) {
            String symbolString = currentInstruction.replace("@", "");
            boolean isConstant = false;

            try {
                Integer testInt = Integer.parseInt(symbolString);
                isConstant = true;

                String binaryString = String.format("%15s", 
                        Integer.toBinaryString(testInt));
                binaryString = binaryString.replace(" ", "0");
                retVal = binaryString;
            } catch(NumberFormatException e) {
                isConstant = false;
            }

            if (!isConstant) {
                // TODO: Parse variables into symbol table here
            }
        }else if (currentInstructionType == InstructionType.L_INSTRUCTION) {
                // TODO: Parse symbols into symbol table here
        }else {
            System.out.printf("getSymbol() called on incorrect instruction type:" + 
                              "%s\nExiting.\n", currentInstructionType.name());
            System.exit(1);
        }

        // TODO: Return symbol as String of binary
        return retVal;
    }

    /**
     * Returns the destination flags (d1, d2, d3) for the current instruction
     * as a String of 0/1 characters.
     * NOTE: This should ONLY be called on C-instructions, and will return non
     *       valid gibberish for A or L instructions.
     *
     * @return the destination flags for the current instruction as a String
     */
    public String getDest() {
        String retVal = "";

        if (!currentInstruction.contains("=")) {
            retVal = DestType.NONE.bitString;
        } else {
            String[] instPartArray = currentInstruction.split("=");

            for (DestType destType : DestType.values()) {
                if (instPartArray[0].equals(destType.mnemonic)) {
                    retVal = destType.bitString;
                }
            }

            if (retVal.equals("")) {
                System.out.printf("Syntax error, unknown DST type encountered " + 
                                  "%s at location %d. Exiting.\n\n", 
                                  instPartArray[0], currentFp);
                System.exit(1);
            }
        }

        return retVal;
    }

    /**
     * Returns the compute flags (c1-c6) for the current instruction as a
     * String of 0/1 characters.
     * NOTE: This should ONLY be called on C-instructions, and will return gibberish
     *       for A or L instructions.
     *
     * @return the compute flags for the current instruction as a String
     */
    public String getComputeFlags() {
        String retVal = "";
        String[] instPartArray;

        // Setting the index of the compute segment based on whether there is an
        // '=' character, a ';' character, or both since either of those can be
        // absent in a given instruction.
        int compIndex;
        if (currentInstruction.contains("=") && currentInstruction.contains(";")) {
            instPartArray = currentInstruction.split("=;");
            compIndex = 1;
        } else if (currentInstruction.contains("=")) {
            instPartArray = currentInstruction.split("=");
            compIndex = 1;
        } else if (currentInstruction.contains(";")) {
            instPartArray = currentInstruction.split(";");
            compIndex = 0;
        } else {
            // Don't think theres any way we could actually get here, and even if
            // we could it'd be a useless instruction since it wouldn't be jumping
            // or storing anything, but regardless we'll handle it.
            instPartArray = new String[1]; 
            instPartArray[0] = currentInstruction;
            compIndex = 0;
        }

        for (CompType compType : CompType.values()) {
            if (instPartArray[compIndex].equals(compType.mnemonic)) {
                retVal = compType.bitString;
            }
        }

        if (retVal.equals("")) {
            System.out.printf("Syntax error, unknown compute type " +
                              "encountered %s at location %d. Exiting.\n\n",
                              instPartArray[0], currentFp);
            System.exit(1);
        }

        return retVal;
    }

    /**
     * Returns the jump flags (j1, j2, j3) for this instruction as a String of 
     * 0/1 characters.
     * NOTE: This should ONLY be called on C-instructions, and will return gibberish
     *       for A or L instructions.
     *
     * @return the jump flags for the current instruction as a String
     */
    public String getJump() {
        String retVal = "";

        if (!currentInstruction.contains(";")) {
            retVal = JumpType.NONE.bitString;
        } else {
            String[] instPartArray = currentInstruction.split(";");

            for (JumpType jumpType : JumpType.values()) {
                if (instPartArray[1].equals(jumpType.mnemonic)) {
                    retVal = jumpType.bitString;
                }
            }

            if (retVal.equals("")) {
                System.out.printf("Syntax error, unknown JMP type encountered " + 
                                  "%s at location %d. Exiting.\n\n", 
                                  instPartArray[1], currentFp);
                System.exit(1);
            }
        }

        return retVal;
    }

    /**
     * Returns the current instruction as a String.
     *
     * @return the current instruction as a String
     */
    public String getCurrentInstruction() {
        return currentInstruction;
    }

    /**
     * Returns the InstructionType of the current instruction.
     *
     * @return the type of the current instruction as an InstructionType enum
     */
    public InstructionType getCurrentInstructionType() {
        return currentInstructionType;
    }

    /**
     * Returns true if the param String is a valid instruction, false otherwise.
     *
     * @param instructionTest String to test
     * @return true if the param String is a valid instruction, false otherwise
     */
    private boolean isValidInstruction(final String instructionTest) {

        if (instructionTest == null) {
            return false;
        }

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

