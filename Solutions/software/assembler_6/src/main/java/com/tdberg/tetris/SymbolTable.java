package com.tdberg.tetris.assembler;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private static HashMap<String, Integer> symbolMap = new HashMap<>();

    // Array of the predefined symbols that are allowed at any time
    private static String[] RESERVED_SYMBOLS = {"SP",
                                                "LCL",
                                                "ARG",
                                                "THIS",
                                                "THAT",
                                                "SCREEN", 
                                                "KEYBOARD"};

    // Array mapping reserved registers to their predefined symbols
    private static Integer[] RESERVED_REGISTERS = {0,
                                                1,
                                                2,
                                                3,
                                                4,
                                                16384,
                                                24576};

    /**
     * Default constructor
     */
    public SymbolTable() {
        // Add mappings for the R0 - R15 symbols
        for (int i = 0; i < 16; i++) {
            String regString = String.format("R%d", i);
            symbolMap.put(regString, (Integer)i);
        }

        // Add the predefined mappings
        for (int i = 0; i < RESERVED_SYMBOLS.length; i++) {
            symbolMap.put(RESERVED_SYMBOLS[i], RESERVED_REGISTERS[i]);
        }
    }

    /**
     * Adds the param symbol to the symbol map with the param address.
     *
     * @param symbol Symbol to add
     * @param address Address to associate with the symbol
     */
    public void addEntry(final String symbol, final int address) {
        symbolMap.put(symbol, (Integer)address);
    }

    /**
     * Returns true if the symbol map contains the param symbol, 
     * false otherwise.
     *
     * @param symbol to test for
     * @return true if the symbol map contains the param symbol,
     *         false otherwise
     */
    public boolean contains(final String symbol) {
        return symbolMap.containsKey(symbol);
    }

    /**
     * Returns the address of the param symbol if it is contained
     * in the map, or -1 if it is not.
     *
     * @param symbol Symbol whose address to retrieve
     * @return the address of the param symbol, or -1 if the symbol
     *         is not in the table
     */
    public int getAddress(final String symbol) {

        int retVal = symbolMap.containsKey(symbol) ? symbolMap.get(symbol) : -1;

        return retVal;
    }

    /**
     * Prints the symbol table to stdout.
     * Useful for debugging.
     */
    public void printSymbolTable() {
        System.out.printf("              Symbol                |      Reg     \n");
        System.out.printf("---------------------------------------------------\n");

        for (String symbol : symbolMap.keySet()) {
            System.out.printf("%1$" + 35 + "s |", symbol);
            System.out.printf(" %-13d\n", (long)symbolMap.get(symbol));
        }
    }
}
