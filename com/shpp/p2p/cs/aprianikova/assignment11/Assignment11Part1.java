package com.shpp.p2p.cs.aprianikova.assignment11;

import java.util.ArrayList;

/**
 * Main class of calculator console program with support for brackets
 * and some functions (sin, cos, tan, atan, log10, log2, sqrt).
 * Negative numbers are supported if they are written in brackets.
 */
public class Assignment11Part1  {

    /**
     * The main method of the program.
     * If the user has not entered data, close the program.
     * Parsing the formula. If there are variables, give them values.
     * Calculate the parsed expression, display the result on the screen.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Line is empty!");
            System.exit(0);
        }

        Parser parser = new Parser();
        parser.parsing(args[0]);

        ArrayList<Character> variables = parser.getVariablesList();
        if (variables.size() > 0) {
            parser.initializationVariables(args);
        }

        Calculator calculator = new Calculator();
        System.out.println(getStringExpression(parser));
        System.out.println(calculator.calculate(parser.getParsedFormula()));
    }


    /** Convert an expression array string to a regular string */
    private static String getStringExpression(Parser parser) {
        /* Get a array-string formula*/
        String expression = parser.getParsedFormula().toString();

        StringBuilder sbExpression = new StringBuilder(expression);
        /* Remove the square brackets*/
        sbExpression.deleteCharAt(0);
        sbExpression.deleteCharAt(sbExpression.length() - 1);

        /* Remove commas */
        return sbExpression.toString().replaceAll(",","");
    }

}
