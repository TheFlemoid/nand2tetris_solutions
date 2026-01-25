// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.

// Initialize variables
@16384
D=A
@R3
M=D

// Check the keyboard register, if nothing is pressed it
// will be 0x00, otherwise it will be something other then 0x00
(LOOP)
@24576
D=M
@WHITE
D;JEQ
@BLACK
0;JMP

// Set a VRAM word to white (0x00)
(WHITE)
D=0
@R3
A=M
M=D
@INC
0;JMP

// Set a VRAM word to black (0xFF)
(BLACK)
D=-1
@R3
A=M
M=D
@INC
0;JMP

// Increment R3, and check that we're still in VRAM
(INC)
@R3
D=M+1
M=D
@24576
D=A
@R3
D=D-M
@RESET
D;JEQ
@LOOP
0;JMP

// Reset R3 back to the start of VRAM
(RESET)
@16384
D=A
@R3
M=D
@LOOP
0;JMP

