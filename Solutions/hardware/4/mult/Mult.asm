// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
// The algorithm is based on repetitive addition.

// Initialize R2 to 0
@R2
M=0

// Check if R0 or R1 is 0, if it is then end
@R0
D=M
@END
D;JEQ

@R1
D=M
@END
D;JEQ

// Add R0 to itself R1 times, decrementing R1 by 1 each loop
(LOOP)
@R0
D=M
@R2
M=D+M
@R1
D=M-1
@END
D;JEQ
@R1
M=D
@LOOP
0;JMP

// End loop
(END)
@END
0;JMP
