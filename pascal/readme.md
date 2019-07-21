<!-- TOC -->

- [命令](#命令)
- [资源](#资源)

<!-- /TOC -->

# 命令

```bash
# Chapter 2: Framework I: Compiler and Interpreter
java -classpath "*/leecode/pascal/target/classes" xianzhan.pascal.Pascal compile "*/leecode/pascal/target/classes/hello.pas"

# Chapter 3: Scanning
java -classpath "*/leecode/pascal/target/classes" xianzhan.pascal.Pascal compile "*/leecode/pascal/target/classes/scannertest.txt"

# Chapter 4: The Symbol Table
# Program 4: Pascal Cross-Referencer I
java -classpath "*/leecode/pascal/target/classes" xianzhan.pascal.Pascal compile -x "*/leecode/pascal/target/classes/scannertest.txt"

# Chapter 5: Pascal Syntax Checker I
java -classpath "*/leecode/pascal/target/classes" xianzhan.pascal.Pascal execute -i "*/leecode/pascal/target/classes/assignments.txt"

# Chapter 6: Simple Interpreter I
java -classpath "*/leecode/pascal/target/classes" xianzhan.pascal.Pascal execute "*/leecode/pascal/target/classes/assignments.txt"

# Chapter 7: Parsing the  REPEAT Statement
java -classpath "*/leecode/pascal/target/classes" xianzhan.pascal.Pascal compile -i "*/leecode/pascal/target/classes/repeat.txt"

# Chapter 8: Interpreting Control Statements
java -classpath "*/leecode/pascal/target/classes" xianzhan.pascal.Pascal execute "*/leecode/pascal/target/classes/repeat.txt"

# Chapter 9: Parsing Declarations
java -classpath "*/leecode/pascal/target/classes" xianzhan.pascal.Pascal execute -x "*/leecode/pascal/target/classes/declarations.txt"

# Chapter 10: Pascal Syntax Checker III
java -classpath "*/leecode/pascal/target/classes" xianzhan.pascal.Pascal compile -i "*/leecode/pascal/target/classes/block.txt"

# Chapter 11: Pascal Syntax Checker IV
java -classpath "*/leecode/pascal/target/classes" xianzhan.pascal.Pascal compile -ix "*/leecode/pascal/target/classes/routines.pas"

# Chapter 12: Program 12-1: Pascal Interpreter
java -classpath "*/leecode/pascal/target/classes" xianzhan.pascal.Pascal execute "*/leecode/pascal/target/classes/newton.pas"
java -classpath "*/leecode/pascal/target/classes" xianzhan.pascal.Pascal execute -lafcr "*/leecode/pascal/target/classes/newton.pas"
```

# 资源

《Writing Compilers and Interpreters》 --- Ronald Mak