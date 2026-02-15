package com.tdberg.tetris.assembler;

import com.tdberg.tetris.assembler.enums.InstructionType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Code {

    private Parser parser;
    private SymbolTable symbolTable;
    private String outputFilePath;
    Path outputFile;

    private static StringBuilder instBuilder = new StringBuilder();
    private static String C_INST_PREFIX="111";
    private static String A_INST_PREFIX="0";

    /**
     * Default constructor
     *
     * @param parser Hack assembly parser to parse the input asm file with.
     * @param symbolTable SymbolTable to use for memory mapping
     */
    public Code(final Parser parser, final SymbolTable symbolTable, 
            final String outputFilePath) {

        this.parser = parser;
        this.symbolTable = symbolTable;
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

        addLabelsToSymbolTable();
    }

    /**
     * Assemble the input asm file into a binary hack file.
     */
    public void assemble() {
        InstructionType currentInstType;
        int nextFreeMemory = 16; // Book instructed to start at mem addr 16

        while (parser.hasMoreInstructions()) {
            parser.advance();

            // Reset the instruction StringBuilder
            instBuilder.setLength(0);
            
            currentInstType = parser.getCurrentInstructionType();

            switch(currentInstType) {
                case A_INSTRUCTION -> {
                    String addressString = parser.getSymbol();

                    // If the symbol isn't constant (aka this is a user defined
                    // var), we check the symbol table to see if this has been
                    // defined previously, adding it if it hasn't been.
                    if (!parser.isSymbolConstant()) {
                        int address;
                        if (symbolTable.contains(addressString)) {
                            address = symbolTable.getAddress(addressString);
                        } else {
                            symbolTable.addEntry(addressString, nextFreeMemory);
                            address = nextFreeMemory;
                            nextFreeMemory++;
                        }

                        addressString = String.format("%15s", 
                                Integer.toBinaryString((Integer)address));
                        addressString = addressString.replace(" ", "0");
                    }

                    instBuilder.append(A_INST_PREFIX);
                    instBuilder.append(addressString);

                    writeInstruction(instBuilder.toString());
                }
                case C_INSTRUCTION -> {
                    instBuilder.append(C_INST_PREFIX);
                    instBuilder.append(parser.getComputeFlags());
                    instBuilder.append(parser.getDest());
                    instBuilder.append(parser.getJump());

                    writeInstruction(instBuilder.toString());
                }
                case L_INSTRUCTION -> {
                    // NOP, since we already added all labels to the symbol table
                }
                case NULL_INSTRUCTION -> {
                    System.out.println("Assemble called on null instruction, " +
                        "shouldn't happen: " + parser.getCurrentInstruction());
                    System.out.println("Exiting.");
                    System.exit(1);
                }
            }
        }
    }

    /**
     * Walks through the entire program, filling out the symbol table with the
     * instruction line number of each L-instruction (ie. (LOOP)).
     */
    private void addLabelsToSymbolTable() {
        int currentLine = 0;

        while(parser.hasMoreInstructions()) {
            parser.advance();

            switch(parser.getCurrentInstructionType()) {
                case A_INSTRUCTION -> {
                    currentLine++;
                }
                case C_INSTRUCTION -> {
                    currentLine++;
                }
                case L_INSTRUCTION -> {
                    String instString = parser.getSymbol();
                    if (!symbolTable.contains(instString)) {
                        symbolTable.addEntry(instString, currentLine);
                    } else {
                        System.out.printf("Label (%s) appears in multiple locations." +
                                          "  This is invalid.\n", instString);
                        System.exit(1);
                    }
                }
            }
        }

        // Reset the source file back to the beginning to prep for the actual
        // assembly pass
        parser.reset();
    }

    public void populateSymbolTable() {

    }

    /**
     * Writes the bytes of the param string to the output .hack file.
     *
     * @param writeString String to write
     */
    private void writeInstruction(final String writeString) {
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
