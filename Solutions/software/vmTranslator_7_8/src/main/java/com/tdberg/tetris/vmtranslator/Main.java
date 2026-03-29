package com.tdberg.tetris.vmtranslator;

public class Main {

    private static String VM_EXTENSION = "vm";
    private static String ASM_EXTENSION = "asm";

    /**
     * Application main.
     */
    public static void main(String[] args) {

        System.out.println("Hello");

        // Input validation
        if (args.length < 1) {
            printErrorMessage();
            System.exit(1);
        }

        // TODO (FLD 29Mar26): For now, just handling the case where the input 
        //                     is a file need to handle directory and multi 
        //                     file translations later.
        String inputFilePath = args[0];
        int dotIndex = inputFilePath.lastIndexOf(".");
        String extensionString = inputFilePath.substring(dotIndex + 1);

        if (!extensionString.equals(VM_EXTENSION)) {
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

        String[] fileNameArray = fileName.split(VM_EXTENSION);
        String outputFileName = String.format("%s%s", fileNameArray[0], ASM_EXTENSION);
        pathArray[pathArray.length - 1] = outputFileName;

        StringBuilder outputPathBuilder = new StringBuilder();

        for (int i = 0; i < pathArray.length; i++) {
            outputPathBuilder.append(pathArray[i]);
            
            if (i != pathArray.length - 1) {
                outputPathBuilder.append("/");
            }
        }
        String outputFilePath = outputPathBuilder.toString();

        //SymbolTable symbolTable = new SymbolTable();
        //Parser parser = new Parser(inputFilePath);
        //Code assembler = new Code(parser, symbolTable, outputFilePath);
        //assembler.assemble();
    }

    /**
     * Prints the error message displayed to the user if their input parameters
     * are invalid for some reason.
     */
    public static void printErrorMessage() {
        System.out.println("Input .vm file(s) must be passed into this interpreter.");
        System.out.printf("\t./hackVM.sh Example.vm or\n");
        System.out.printf("\t./hackVM.sh /path/to/files/\n");
        System.out.printf("Exiting.\n\n");
    }
}
