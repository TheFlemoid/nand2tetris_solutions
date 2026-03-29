// Testing various assembly functions

// Program start, init the stack pointer
@256
D=A
@SP
M=D

// Push constant 14 on the stack
@14
D=A
@SP
A=M
M=D
@SP
D=M+1
M=D

// Push constant 7 on the stack
@7
D=A
@SP
A=M
M=D
@SP
D=M+1
M=D

// Push constant 329 on the stack
@329
D=A
@SP
A=M
M=D
@SP
D=M+1
M=D

// Add two operands
@SP
A=M
A=A-1
D=M
A=A-1
A=M
D=D+A
@SP
A=M
A=A-1
A=A-1
M=D
D=A+1
@SP
M=D

// Sub two operands
@SP
A=M
A=A-1
D=M
A=A-1
A=M
D=A-D 
@SP
A=M
A=A-1
A=A-1
M=D
D=A+1
@SP
M=D

// Negate one operand
@SP
A=M
A=A-1
D=-M
M=D

// NOT one operand
@SP
A=M
A=A-1
D=!M
M=D

// Push constant 42 on the stack
@42
D=A
@SP
A=M
M=D
@SP
D=M+1
M=D

// Push constant 42 on the stack
@42
D=A
@SP
A=M
M=D
@SP
D=M+1
M=D

// Check equality
// Sub two operands
@SP
A=M
A=A-1
D=M
A=A-1
A=M
D=A-D 
@SP
A=M
A=A-1
A=A-1
M=D
D=A+1
@SP
M=D
// NOT one operand
@SP
A=M
A=A-1
D=!M
M=D


// Program end, infinite loop
(END)
@END
0;JMP

