package com.tdberg.tetris.assembler;

import com.tdberg.tetris.assembler.enums.InstructionType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Code {

    private Parser parser;
    private String outputFilePath;
    Path outputFile;

    private static StringBuilder instBuilder = new StringBuilder();
    private static String C_INST_PREFIX="111";
    private static String A_INST_PREFIX="0";

    /**
     * Default constructor
     *
     * @param parser Hack assembly parser to parse the input asm file with.
     */
    public Code(final Parser parser, final String outputFilePath) {
        this.parser = parser;
        this.outputFilePath = outputFilePath;
        
        try {
            outputFile = Paths.get(outputFilePath);
            Files.deleteIfExists(outputFile);
            Files.createFile(outputFile);
        }catch (IOException e) {
            System.out.printf("IOException when opening output %s file" + 
                    " for writing: %s\n" + outputFilePath, e.getMessage());
            System.out.printf("Exiting.\n");
            System.exit(1);
        }
    }

    /**
     * Assemble the input asm file into a binary hack file.
     */
    public void assemble() {
        InstructionType currentInstType;

        while (parser.hasMoreInstructions()) {
            parser.advance();

            // Reset the instruction StringBuilder
            instBuilder.setLength(0);
            
            currentInstType = parser.getCurrentInstructionType();

            switch(currentInstType) {
                case A_INSTRUCTION -> {
                    instBuilder.append(A_INST_PREFIX);
                    instBuilder.append(parser.getSymbol());
                }
                case C_INSTRUCTION -> {
                    instBuilder.append(C_INST_PREFIX);
                    instBuilder.append(parser.getComputeFlags());
                    instBuilder.append(parser.getDest());
                    instBuilder.append(parser.getJump());
                }
                case L_INSTRUCTION -> {
                    // TODO: Implement SymbolTable
                }
                case NULL_INSTRUCTION -> {
                    System.out.println("Assemble called on null instruction, " +
                        "shouldn't happen: " + parser.getCurrentInstruction());
                    System.out.println("Exiting.");
                    System.exit(1);
                }
            }

            writeInstruction(instBuilder.toString());
        }
    }

    /**
     * Writes the bytes of the param string to the output .hack file.
     *
     * @param writeString String to write
     */
    private void writeInstruction(final String writeString) {
        System.out.println("Writing: " + writeString);
        try {
            Files.write(outputFile, writeString.getBytes(), StandardOpenOption.APPEND);
            Files.write(outputFile, "\n".getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            System.out.printf("IOException when opening output %s file" + 
                    " for writing: %s\n" + outputFilePath, e.getMessage());
            System.out.printf("Exiting.\n");
            System.exit(1);
        }
    }
}
