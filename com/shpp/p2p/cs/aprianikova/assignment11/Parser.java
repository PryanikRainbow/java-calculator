package com.shpp.p2p.cs.aprianikova.assignment11;

import java.util.ArrayList;

/**
 * The class is responsible for parsing the formula.
 * Breaks a string into formula elements (Brackets, signs, variables, numbers, functions).
 * These elements are stored in an ArrayList <Object>.
 */
public class Parser implements Constants {
    /* All elements of the parsed formula */
    ArrayList<Object> parsedFormula = new ArrayList<>();
    /* All variables of the parsed formula */
    ArrayList<Character> variables = new ArrayList<>();

    /**
     * Divides formulas into expression elements.
     * First, edit the formula to make it easier to parsing (formulaBeforeParsing).
     * "for" - formula parsing.
     * if (i == 0) - definition of the first element of the formula.
     * else - definition of other elements of the scattered formula.
     * Note: 'i' returns the current index.
     * @param formula - string to be parsing (first element of String [arg]).
     */
    protected void parsing(String formula) {
        formula = formulaBeforeParsing(formula);

        for (int i = 0; i < formula.length(); i++) {
            char charCurrent = formula.charAt(i);

            if (i == 0) {
                if (charCurrent == '(') {
                    parsedFormula.add(charCurrent);
                } else if (Character.isLetter(charCurrent)) {
                    i = ifCharIsLetter(formula, i, charCurrent);
                } else if (Character.isDigit(charCurrent)) {
                    i = ifCharIsDigit(formula, i, charCurrent);
                } else {
                    System.out.println("Error: Invalid first character in formula!");
                    System.exit(0);
                }
            } else {
                if (isSign(charCurrent)) {
                    i = ifCharIsSign(formula, i, charCurrent);
                } else if (charCurrent == '(' || charCurrent == ')') {
                    parsedFormula.add(charCurrent);
                } else if (Character.isLetter(charCurrent)) {
                    i = ifCharIsLetter(formula, i, charCurrent);
                } else if (Character.isDigit(charCurrent)) {
                    i = ifCharIsDigit(formula, i, charCurrent);
                } else {
                    System.out.println("Error: Invalid character in formula!");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Edit the formula to make it easier to parsing.
     * Add "0" if the first character is "-", remove all spaces, lowercase letters.
     * @param formula - string to be parsing (first element of String [arg]).
     * @return edited formula.
     */
    protected String formulaBeforeParsing(String formula) {
        if (formula.charAt(0) == '-') {
            formula = "0" + formula;
        }
        formula = formula.replaceAll(" ", "");
        formula = formula.toLowerCase();
        return formula;
    }

    /**
     * If char is letter.
     * Try to find the function(s).
     * If the first letter and the current index match = the index of the corresponding function (string),
     * add the function as an element of the parsed formula, get the next index.
     * Else the letter is a variable, add a variable as an element of the parsed formula.
     * @param i - current index.
     * @return current index of string formula.
     */
    private int ifCharIsLetter(String formula, int i, char charCurrent) {
        if (charCurrent == 's' && formula.indexOf("sin", i) == i) {
            parsedFormula.add("sin");
            i += 2;
        } else if (charCurrent == 's' && formula.indexOf("sqrt", i) == i) {
            parsedFormula.add("sqrt");
            i += 3;
        } else if (charCurrent == 'c' && formula.indexOf("cos", i) == i) {
            parsedFormula.add("cos");
            i += 2;
        } else if (charCurrent == 't' && formula.indexOf("tan", i) == i) {
            parsedFormula.add("tan");
            i += 2;
        } else if (charCurrent == 'a' && formula.indexOf("atan", i) == i) {
            parsedFormula.add("atan");
            i += 3;
        } else if (charCurrent == 'l' && formula.indexOf("log2", i) == i) {
            parsedFormula.add("log2");
            i += 3;
        } else if (charCurrent == 'l' && formula.indexOf("log10", i) == i) {
            parsedFormula.add("log10");
            i += 4;
        } else {
            /* Add to the list of variables if the current variable is new */
            if (!parsedFormula.contains(charCurrent)) variables.add(charCurrent);
            parsedFormula.add(charCurrent);
        }
        return i;
    }

    /**
     * If char is digit.
     * NumberBuilder - field for storing the received number.
     * While - loop for finding digits and point, building a number.
     * Try to add the received number to the parsed formula.
     * Catch  - display an error message, stop the program.
     * @param i - current index.
     * @return i - current index of string formula
     */
    protected int ifCharIsDigit(String formula, int i, char charCurrent) {
        StringBuilder numberBuilder = new StringBuilder();
        while (Character.isDigit(charCurrent) || charCurrent == '.') {
            numberBuilder.append(charCurrent);
            i++;
            if (i == formula.length()) {
                i--;
                break;
            }
            charCurrent = formula.charAt(i);
            /* If the char is not a digit or a point, return to the previous index */
            if (!Character.isDigit(charCurrent) && charCurrent != '.') i--;
        }

        try {
            parsedFormula.add(Double.valueOf(numberBuilder.toString()));
        } catch (NumberFormatException e) {
            System.out.println("A number contains more than one point!");
            System.exit(0);
        }
        return i;
    }

    /**
     * If char is negative number (before "(").
     * NumberBuilder - field for storing the received number.
     * While - loop for building a negative number.
     * Try to add the received number to the parsed formula.
     * Catch  - display an error message, stop the program.
     * @param i - current index.
     * @return i - current index of string formula
     */
    protected int negativeNumber(String formula, int i, char charCurrent) {
        StringBuilder numberBuilder = new StringBuilder();

        while (charCurrent != ')' || Character.isDigit(charCurrent)) {
            numberBuilder.append(charCurrent);
            i++;
            if (i == formula.length()) {
                i--;
                break;
            }
            charCurrent = formula.charAt(i);
            /* If the char is ")", return to the previous index */
            if (charCurrent == ')') i--;
        }

        try {
            parsedFormula.add(Double.valueOf(numberBuilder.toString()));
        } catch (NumberFormatException e) {
            System.out.println("""
                    Error: \s
                    Maybe, a number contains more than one point?
                    Maybe, incorrectly char next to negative number?""");
            System.exit(0);
        }
        return i;
    }

    /**
     * If char is sign.
     * The first "if" is a test for a negative number if the "-" character.
     * If a sign does not precede another sign, add the sign to the parsed formula.
     * Else, display an error message and close the program.
     * @return i - current index of string formula
     */
    private int ifCharIsSign(String formula, int i, char charCurrent) {
        if (charCurrent == '-' && formula.charAt(i - 1) == '(') {
            i = negativeNumber(formula, i, charCurrent);
        } else {
            if (!isSign(formula.charAt(i - 1))) parsedFormula.add(charCurrent);
            else {
                System.out.println("Error: After math sign, another sign!");
                System.exit(0);
            }
        }
        return i;
    }

    /**
     * If the current character is found
     * in the SIGNS array, return true,
     * else - false
     */
    private boolean isSign(char charCurrent) {
        for (char sign : SIGNS) {
            if (sign == charCurrent) return true;
        }
        return false;
    }

    /** @return parsed formula for the current moment in time (ArrayList <Object>). */
    protected ArrayList<Object> getParsedFormula() {
        return parsedFormula;
    }

    /** @return variables of formula (ArrayList <Character>) */
    protected ArrayList<Character> getVariablesList() {
        return variables;
    }

    /**
     * Initialization of variables.
     * Checks the lines entered by the user,
     * starting from index 1 (since index 0 is a formula).
     * Substitute a values into a variables.
     * If not all variables are initialized, close the program.
     */
    public void initializationVariables(String[] args) {

        for (int arg = 1; arg < args.length; arg++) {
            String currentArg = args[arg];
            valueSubstitution(currentArg);
        }

        if (containsLetter()) {
            System.out.println("Error: Uninitialized variable(s)!");
            System.exit(0);
        }
    }

    /**
     * If the length of the analyzed string >= 3,
     * the first character is the variable that is in the parsed formula,
     * the second character "=" - try to initialize the found variable.
     * NumberBuilder - field for storing the received number, i - current index.
     * while - number entry,  starting from the second index and ending with the last.
     * Try to replace the change with the received value.
     * If the value does not support the double type - invalid value, close the program.
     * @param currentArg - the string currently being checked.
     */
    private void valueSubstitution(String currentArg) {
        if (currentArg.length() >= 3 && variables.contains(currentArg.charAt(0)) && currentArg.charAt(1) == '='
                && (currentArg.charAt(2) == '-' || Character.isDigit(currentArg.charAt(2)))) {

            int i = 2;
            StringBuilder numberBuilder = new StringBuilder();

            while (i != currentArg.length()) {
                if (i < currentArg.length()) numberBuilder.append(currentArg.charAt(i));
                i++;
            }

            try {
                for (int j = 0; j < parsedFormula.size(); j++) {
                    Object elFormula = parsedFormula.get(j);
                    if (elFormula.equals(currentArg.charAt(0))) {
                        parsedFormula.set(j, Double.valueOf(numberBuilder.toString()));
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid variable value of " + currentArg.charAt(0));
                System.exit(0);
            }

        }
    }

    /** Checks if a parsed formula has variables */
    private boolean containsLetter() {
        for (Object element : parsedFormula) {
            if (element instanceof Character && Character.isLetter((Character) element)) {
                return true;
            }
        }
        return false;
    }
}
