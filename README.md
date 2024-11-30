# **Quine-McCluskey implementation using Java for EEE 4334 Fall 2024**  
## **Collaborators**: Steven Huynh & Gabriel Martin  
### **Intro**  
This is an implementation of the Quine-McCluskey prime implicant generation method in Java.  
This repo includes the implementation itself (**QuineMcCluskey.java**) that can be used to run an individual .pla test case,  
as well as a script (**QuineMcCluskeyTestRunner.java**) that can be used to run a batch of .pla test cases located in the **test_cases/** subdirectory.

### **Directory & File Overview**:  
**test_cases/**  
Contains .pla test cases to be ran with QMTestRunner script  

**combined_output.pla**  
Contains the output of all test cases in test_cases/ from most recent run  

**QuineMcCluskey.class**  
Class file generated as a result of compiling QuineMcCluskey.java  

**QuineMcCluskey.java**  
Java file containing the implementation of the Quine-McCluskey prime implicant generation method  

**QuineMcCLuskeyTestRunner.class**  
Class file generated as a result of compiling QuineMcCluskeyTestRunner.java  

**QuineMcCluskeyTestRunner.java**  
Java file responsible for scripting the execution of QM using test cases found in test_cases/  
