#
# Script to perform automated testing for assignment 1 of AA, semester 2 2019
#
# The provided Python script will be the same one used to test your implementation.  
# We will be testing your code on the core teaching servers (titan, jupiter etc), so please try your code there.  
# The script first compiles your Java code, runs one of the two implementations then runs a series of test.  
# Each test consists of sequence of operations to execute, whose results will be saved to file, then compared against 
# the expected output.  If output from the tested implementation is the same as expected (script is tolerant for
# some formatting differences, but please try to stick to space separated output), then we pass that test.
# Otherwise, difference will be printed via 'diff' (if in verbose mode, see below).
#
# Usage, assuming you are in the directory where the test script " assign1TestScript.py" is located.
#
# > python assign1TestScript.py [-v] [-f input filename] <codeDirectory> <name of implementation to test> <list of input files to test on>
#
# options:
#
#    -v : verbose mode
#    -f [filename] : If specified, the file 'filename' will be passed to the Java framework to load as the initial tree.
#
# Input:
#
#   code directory : directory where the Java files reside.  E.g., if directory specified is Assign1-s1234,
#        then Assign1-s1234/TreeTester.java should exist.  This is also where the script
#        expects your program to be compiled and created in, e.g., Assign1-s1234/TreeTester.class.
#   name of implementation to test: This is the name of the implementation to test.  The names
#        should be the same as specified in the script or in TreeTester.java
#   input files: these are the input files, where each file is a list of commands to execute.  
#        IMPORTANT, the expected output file for the print operation must be in the same directory
#        as the input files, and the should have the same basename - e.g., if we have input operation
#        file of "test1.in", then we should have expected file "test1.exp".
#
#
# As an example, I can run the code like this when testing code directory "Assign1-s1234",
# all my input and expected files are located in a directory called "tests" 
# and named test1.in and testing for sample implementation:
#
# > python assign1TestScript.py -v   Assign1-s1234    sample     tests/test1.in
#
# Another example if running test2.in and using the BSP Tree as my initial one:
# > python assign1TestScript.py -v -f BSP_combined.txt   Assign1-s1234    sample     tests/test2.in
#
#
#
# Jeffrey Chan, 2016
# Phuc Chu, 2018
#
import difflib
import getopt
import os
import os.path
import platform
import re
import shutil
import subprocess as sp
import sys

DELIMITER = r"[\s,;|]*"


def main():
    # process command line arguments
    try:
        # option list
        sOptions = "vf:s:"
        # get options
        optList, remainArgs = getopt.gnu_getopt(sys.argv[1:], sOptions)
    except getopt.GetoptError, err:
        print >> sys.stderr, str(err)
        usage(sys.argv[0])

    bVerbose = False
    bInputFile = False
    sInputFile = ""
    bHasSourceCodeDir = False
    sSourceCodeDir = ""
    sOrigPath = os.getcwd()

    for opt, arg in optList:
        if opt == "-v":
            bVerbose = True
        elif opt == "-f":
            bInputFile = True
            sInputFile = os.path.join(sOrigPath, arg)
        elif opt == "-s":
            # our source code directory to copy jar files etc if missing
            bHasSourceCodeDir = True
            sSourceCodeDir = arg
        else:
            usage(sys.argv[0])

    if len(remainArgs) < 3:
        usage(sys.argv[0])

    # code directory
    sCodeDir = remainArgs[0]
    os.chdir(sCodeDir)
    # which implementation to test (see TreeTester.java for the implementation strings)
    sImpl = remainArgs[1]
    # set of input files that contains the operation commands
    lsInFile = remainArgs[2:]

    # check implementation
    setValidImpl = {"seqtree", "linktree", "sample"}
    if sImpl not in setValidImpl:
        print >> sys.stderr, sImpl + " is not a valid implementation name."
        sys.exit(1)

    # compile the skeleton java files
    sClassPath = "-cp .:jopt-simple-5.0.2.jar:sample.jar"
    sOs = platform.system()
    if sOs == "Windows":
        sClassPath = "-cp .;jopt-simple-5.0.2.jar;sample.jar"

    sCompileCommand = "javac " + sClassPath + " *.java"
    # print sCompileCommand
    sExec = "TreeTester"

    # whether executable was compiled and constructed
    bCompiled = False

    # check if have all the necessary files
    sTreeTester = "TreeTester.java"
    sBSPTree = "BSPTree.java"
    sJar1 = "jopt-simple-5.0.2.jar"
    sJar2 = "sample.jar"
    if bHasSourceCodeDir:
        if not os.path.isfile(sTreeTester):
            shutil.copy(os.path.join(sSourceCodeDir, sTreeTester), ".")
        if not os.path.isfile(sBSPTree):
            shutil.copy(os.path.join(sSourceCodeDir, sBSPTree), ".")
        if not os.path.isfile(sJar1):
            shutil.copy(os.path.join(sSourceCodeDir, sJar1), ".")
        if not os.path.isfile(sJar2):
            shutil.copy(os.path.join(sSourceCodeDir, sJar2), ".")

    # compile
    proc = sp.Popen(sCompileCommand, shell=True, stderr=sp.PIPE)
    (sStdout, sStderr) = proc.communicate()
    print sStderr

    # check if executable was constructed
    if not os.path.isfile(sExec + ".class"):
        print >> sys.stderr, sExec + ".java didn't compile successfully."
    else:
        bCompiled = True

    # variable to store the number of tests passed
    passedNum = 0
    # lsTestPassed = [False for x in range(len(lsInFile))]
    lsTestPassed = []
    print ""

    if bCompiled:
        # loop through each input test file
        for (_, sInLoopFile) in enumerate(lsInFile):
            sInFile = os.path.join(sOrigPath, sInLoopFile)
            sTestName = os.path.splitext(os.path.basename(sInFile))[0]
            # sOutputFile = os.path.join(sCodeDir, sTestName + "-" + sImpl + ".out")
            sOutputFile = os.path.join(sTestName + "-" + sImpl + ".out")
            sExpectedFile = os.path.splitext(sInFile)[0] + ".exp"

            # check if expected files exist
            if not os.path.isfile(sExpectedFile):
                print >> sys.stderr, sExpectedFile + " is missing."
                continue

            sCommand = os.path.join(
                "java " + sClassPath + " " + sExec + " " + sImpl + " " + sOutputFile)
            if bInputFile:
                sCommand = os.path.join(
                    "java " + sClassPath + " " + sExec + ' ' + sImpl + " -f " + sInputFile + " " + sOutputFile)
            # print sCommand

            # NOTE: following command used by my dummy code to test possible output (don't replace above)
            #                 lCommand = os.path.join(sCodeDir, sExec + " " + sExpectedFile + ".test")
            if bVerbose:
                print "Testing: " + sCommand
            with open(sInFile, "r") as fIn:
                proc = sp.Popen(sCommand, shell=True, stdin=fIn, stderr=sp.PIPE)
                # proc = sp.Popen(sCommand, shell=True, stdin=sp.PIPE, stdout=sp.PIPE, stderr=sp.PIPE)

                # (sStdout, sStderr) = proc.communicate("a hello\np\nq")
                (sStdout, sStderr) = proc.communicate()

                # if len(sStderr) > 0:
                if False:
                    print >> sys.stderr, "Cannot execute " + sInFile
                    print >> sys.stderr, "Error message from java program: " + sStderr
                else:
                    if bVerbose and len(sStderr) > 0:
                        print >> sys.stderr, "\nWarnings and error messages from running java program:\n" + sStderr

                    # compare expected with output
                    bPassed = evaluate(sExpectedFile, sOutputFile)
                    if bPassed:
                        passedNum += 1
                        # vTestPassed[j] = True
                        lsTestPassed.append(sTestName)
                    else:
                        # print difference if failed
                        if bVerbose:
                            if not bPassed:
                                print >> sys.stderr, "Difference between output and expected:"
                                proc = sp.Popen("diff -y " + sOutputFile + " " + sExpectedFile, shell=True)
                                proc.communicate()
                                print >> sys.stderr, ""

    # change back to original path
    os.chdir(sOrigPath)

    print "\nSUMMARY: " + sExec + " has passed " + str(passedNum) + " out of " + str(len(lsInFile)) + " tests."
    # print "PASSED: " + ", ".join([str(x+1) for (x,y) in enumerate(vTestPassed) if y == True]) + "\n"
    print "PASSED: " + ", ".join(lsTestPassed) + "\n"


#######################################################################################################

def read_file(filename):
    try:
        with open(filename, 'r') as f:
            return re.split(DELIMITER, f.read().strip().lower() + ' ')
    except IOError as e:
        print >> sys.stderr, str(e)
        usage(sys.argv[0])


def evaluate(sExpectedFile, sOutputFile):
    expected = read_file(sExpectedFile)
    actual = read_file(sOutputFile)
    diff = difflib.unified_diff(expected, actual)
    return len(list(diff)) <= 0


def usage(sProg):
    print >> sys.stderr, sProg + "[-v] <code directory> <name of implementation to test> <list of test input files>"
    sys.exit(1)


if __name__ == "__main__":
    main()
