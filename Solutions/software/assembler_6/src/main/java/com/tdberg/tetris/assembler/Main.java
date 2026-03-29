package com.tdberg.tetris.assembler;

public class Main {

    private static String ASM_EXTENSION = "asm";
    private static String HACK_EXTENSION = "hack";

    /**
     * Application main.
     */
    public static void main(String[] args) {

        // Input validation
        if (args.length < 1) {
            printErrorMessage();
            System.exit(1);
        }

        String inputFilePath = args[0];
        int dotIndex = inputFilePath.lastIndexOf(".");
        String extensionString = inputFilePath.substring(dotIndex + 1);

        if (!extensionString.equals(ASM_EXTENSION)) {
            printErrorMessage();
            System.exit(1);
        }

        // This is just a bunch of cruft to form the output filename and filepath
        // given the input filepath (now that we know the input file path is valid)
        String fileName = inputFilePath;
        String[] pathArray = {inputFilePath};
        if (inputFilePath.contains("/")) {
            pathArray = inputFilePath.split("/");
            fileName = pathArray[pathArray.length - 1];
        }

        String[] fileNameArray = fileName.split(ASM_EXTENSION);
        String outputFileName = String.format("%s%s", fileNameArray[0], HACK_EXTENSION);
        pathArray[pathArray.length - 1] = outputFileName;

        StringBuilder outputPathBuilder = new StringBuilder();

        for (int i = 0; i < pathArray.length; i++) {
            outputPathBuilder.append(pathArray[i]);
            
            if (i != pathArray.length - 1) {
                outputPathBuilder.append("/");
            }
        }
        String outputFilePath = outputPathBuilder.toString();

        // This is where actual assembly happens
        SymbolTable symbolTable = new SymbolTable();
        Parser parser = new Parser(inputFilePath);
        Code assembler = new Code(parser, symbolTable, outputFilePath);
        assembler.assemble();

        //symbolTable.printSymbolTable();
        //System.out.printf("\n---------------------\n");
        //System.out.printf("Symbol Table Size: %d\n", symbolTable.size());
    }

    /**
     * Prints the error message displayed to the user if their input parameters
     * are invalid for some reason.
     */
    public static void printErrorMessage() {
        System.out.println("Input .asm file must be passed into this assembler.");
        System.out.printf("\t./hackAssembler.sh Example.asm\n");
        System.out.printf("Exiting.\n\n");
    }
}
