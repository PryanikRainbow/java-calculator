package com.shpp.p2p.cs.aprianikova.assignment11;

import java.util.ArrayList;
import java.util.Objects;

/** The class responsible for calculating the expression. */
public class Calculator implements Constants {

    /* Elements of expression. During the calculation, the elements will change. */
    private ArrayList<Object> expression = new ArrayList<>();

    /**
     * The main method of the class responsible for the calculation.
     * First, the value of the method parameter is copied into the ArrayList "expression".
     * Calculates subexpressions in brackets, if any.
     * Calculates the expression.
     * @return result of expression.
     */
    public double calculate(ArrayList<Object> parsedFormula) {
        expression = parsedFormula;

        checkBrackets();
        calculateSubexpression( expression);
        return (double) expression.get(0);
    }

    /**
     * Calculates expressions in brackets, rewrites the expression.
     * Determine the subexpression that must be calculated first.
     * i - current index.
     * Elements of the analyzed subexpression are rewritten to null
     * and added to ArrayList<Object> elementsBetweenBrackets.
     * Removes all nulls from the main formula. After calculating the subexpression,
     * add the result according to the index analyzed last (index former "(" ).
     */
    private void checkBrackets() {
        while (expression.indexOf(')') > 0) {
            ArrayList<Object> elementsBetweenBrackets = new ArrayList<>();
            int i = expression.indexOf(')');
            while (i != '(') {
               /* if brackets are enclosed */
                if (expression.get(i) == null) {
                    i--;
                    continue;
                }
                if (i == expression.indexOf(')')) {
                    expression.set(i, null);
                } else if (expression.get(i).equals('(')) {
                    expression.set(i, null);
                    break;
                } else {
                    elementsBetweenBrackets.add(0, expression.get(i));
                    expression.set(i, null);
                }
                i--;
                if (i < 0) {
                    i++;
                    break;
                }
            }
            /* double colon (::) operator */
            expression.removeIf(Objects::isNull);

            ArrayList <Object> resultSubexpression = calculateSubexpression( elementsBetweenBrackets);
            /* if the brackets are not empty */
           if (resultSubexpression.size() == 1) expression.add(i, resultSubexpression.get(0));
        }
    }

    /**
     * Expression calculation without brackets.
     * If elements of the formula > 1, try to perform mathematical operations,
     * taking into account their priority.
     * @return result of subexpression.
     */
    private ArrayList<Object> calculateSubexpression( ArrayList<Object> subexpression) {

            if (subexpression.size() > 1) {
                checkFunctions(subexpression);
                checkPowerOrSqrt(subexpression);
                checkMultyOrDiv(subexpression);
                checkAdditionOrSubtraction(subexpression);
        }
        return subexpression;
    }

    /**
     * Checking for the presence of functions in the formula.
     * If the function is found,
     * perform the appropriate mathematical operation and rewrite the expression.
     */
    private void checkFunctions(ArrayList<Object> subexpression) {
        for (int i = subexpression.size()-1; i >= 0; i--) {
            Object objectCurrent = subexpression.get(i);
            if (!objectCurrent.equals("sqrt") && objectCurrent instanceof String) {
                if (objectCurrent.equals("sin")) sinAction(i, subexpression);
                else if (objectCurrent.equals("cos")) cosAction(i, subexpression);
                else if (objectCurrent.equals("tan")) tanAction(i, subexpression);
                else if (objectCurrent.equals("atan")) atanAction(i, subexpression);
                else if (objectCurrent.equals("log2")) log2Action(i, subexpression);
                else if (objectCurrent.equals("log10")) log10Action(i, subexpression);
            }
        }
    }

    /**
     * Checking for the presence of finding the root and exponentiation in the formula.
     * Search until these mathematical operations are completed.
     * Orientation to this last mathematical operations found (calculation from right to left).
     * Determines the last action of these two, calculates it and rewrites the formula.
     */
    private void checkPowerOrSqrt(ArrayList<Object> subexpression) {
        while (subexpression.contains("sqrt") || subexpression.contains('^')) {
            if(subexpression.contains("sqrt") && subexpression.contains('^')) {
                int iSqrt = subexpression.lastIndexOf("sqrt");
                int iPower = subexpression.lastIndexOf('^');

                if (iSqrt > iPower) sqrtAction(iSqrt, subexpression);
                else if (iPower > iSqrt) powerAction(iPower, subexpression);
            } else if (subexpression.contains("sqrt") && !subexpression.contains('^')) {
                int i = subexpression.lastIndexOf("sqrt");
                sqrtAction(i, subexpression);
            } else if (!subexpression.contains("sqrt") && subexpression.contains('^')) {
                int i = subexpression.lastIndexOf('^');
                powerAction(i, subexpression);
            }
        }

    }

    /**
     * Checking for the presence of finding the multiplication and division signs in the formula.
     * Search until these mathematical operations are completed.
     * Orientation to this first mathematical operations found.
     * Determines the first action of these two, calculates it and rewrites the formula.
     */
    private void checkMultyOrDiv(ArrayList<Object> subexpression) {
        while (subexpression.contains('*') || subexpression.contains('/')) {
            if(subexpression.contains('*') && subexpression.contains('/')) {
                int iMultiplication = subexpression.indexOf('*');
                int iDivision = subexpression.lastIndexOf('/');

                if (iMultiplication < iDivision) multiplicationAction(iMultiplication,subexpression);
                else if (iDivision < iMultiplication) divisionAction(iDivision,subexpression);
            } else if (subexpression.contains('*') && !subexpression.contains('/')) {
                int i = subexpression.indexOf('*');
                multiplicationAction(i,subexpression);
            } else if (!subexpression.contains('*') && subexpression.contains('/')) {
                int i = subexpression.indexOf('/');
                divisionAction(i, subexpression);
            }

        }
    }

    /**
     * Checking for the presence of finding the addition and subtraction signs in the formula.
     * Search until these mathematical operations are completed.
     * Orientation to this first mathematical operations found.
     * Determines the first action of these two, calculates it and rewrites the formula.
     */
    private void checkAdditionOrSubtraction(ArrayList<Object> subexpression) {
        while (subexpression.contains('+') || subexpression.contains('-')) {
            if(subexpression.contains('+') && subexpression.contains('-')) {
                int iAddition = subexpression.indexOf('+');
                int iSubtraction = subexpression.indexOf('-');

                if (iAddition < iSubtraction) additionAction(iAddition,subexpression);
                else if (iSubtraction < iAddition) subtractionAction(iSubtraction,subexpression);
            } else if (subexpression.contains('+') && !subexpression.contains('-')) {
                int i = subexpression.indexOf('+');
                additionAction(i,subexpression);
            } else if (!subexpression.contains('+') && subexpression.contains('-')) {
                int i = subexpression.indexOf('-');
                subtractionAction(i, subexpression);
            }

        }
    }

    /**
     * Will try to calculate the sin of the number following the next index (after the sin index).
     * Rewrites the expression.
     * If the value does not support type double - invalid value, close the program.
     * @param i - index of current sign or function.
     * @param subexpression - list of elements of analyzed expression.
     */
    private void sinAction(int i, ArrayList<Object> subexpression) {
        try {
            double sinResult = Math.sin((Double)subexpression.get(i+1));
            subexpression.remove(i);
            subexpression.remove(i);
            subexpression.add(i,sinResult);
        } catch (Exception e) {
            System.out.println("Error: Invalid value of operation sin");
            System.exit(0);
        }
    }

    /**
     * Will try to calculate the cos of the number following the next index (after the sin index).
     * Rewrites the expression.
     * If the value does not support type double - invalid value, close the program.
     * @param i - index of current sign or function.
     * @param subexpression - list of elements of analyzed expression.
     */
    private void cosAction(int i, ArrayList<Object> subexpression) {
        try {
            double cosResult = Math.cos((Double)subexpression.get(i+1));
            subexpression.remove(i);
            subexpression.remove(i);
            subexpression.add(i,cosResult);
        } catch (Exception e) {
            System.out.println("Error: Invalid value of operation cos");
            System.exit(0);
        }
    }

    /**
     * Will try to calculate the tan of the number following the next index (after the sin index).
     * Rewrites the expression.
     * If the value does not support type double - invalid value, close the program.
     * @param i - index of current sign or function.
     * @param subexpression - list of elements of analyzed expression.
     */
    private void tanAction(int i, ArrayList<Object> subexpression) {
        try {
            double tanResult = Math.tan((Double)subexpression.get(i+1));
            subexpression.remove(i);
            subexpression.remove(i);
            subexpression.add(i,tanResult);
        } catch (Exception e) {
            System.out.println("Error: Invalid value of operation tan");
            System.exit(0);
        }
    }

    /**
     * Will try to calculate the atan of the number following the next index (after the sin index).
     * Rewrites the expression.
     * If the value does not support type double - invalid value, close the program.
     * @param i - index of current sign or function.
     * @param subexpression - list of elements of analyzed expression.
     */
    private void atanAction(int i, ArrayList<Object> subexpression) {
        try {
            double atanResult = Math.atan((Double)subexpression.get(i+1));
            subexpression.remove(i);
            subexpression.remove(i);
            subexpression.add(i,atanResult);
        } catch (Exception e) {
            System.out.println("Error: Invalid value of operation atan");
            System.exit(0);
        }
    }

    /**
     * Will try to calculate the log2 of the number following the next index (after the sin index).
     * Rewrites the expression.
     * If the value does not support type double - invalid value, close the program.
     * @param i - index of current sign or function.
     * @param subexpression - list of elements of analyzed expression.
     */
    private void log2Action(int i, ArrayList<Object> subexpression) {
        try {
            double log2Result = Math.log((Double)subexpression.get(i+1)) / Math.log(2);
            if (Double.isNaN(log2Result)) {
                System.out.println("Error: result of log2 = " + log2Result );
                System.exit(0);
            }
            subexpression.remove(i);
            subexpression.remove(i);
            subexpression.add(i,log2Result);
        } catch (Exception e) {
            System.out.println("Error: Invalid value of operation log2");
            System.exit(0);
        }

    }

    /**
     * Will try to calculate the log10 of the number following the next index (after the sin index).
     * Rewrites the expression.
     * If the value does not support type double - invalid value, close the program.
     * @param i - index of current sign or function.
     * @param subexpression - list of elements of analyzed expression.
     */
    private void log10Action(int i, ArrayList<Object> subexpression) {
        try {
            double log10Result = Math.log10((Double) subexpression.get(i + 1));
            if (Double.isNaN(log10Result)) {
                System.out.println("Error: result of log10 = " + log10Result);
                System.exit(0);
            }
            subexpression.remove(i);
            subexpression.remove(i);
            subexpression.add(i, log10Result);
        } catch (Exception e) {
            System.out.println("Error: Invalid value of operation log10");
            System.exit(0);
        }
    }

    /**
     * Will try to calculate the sqrt of the number following the next index (after the sin index).
     * Rewrites the expression.
     * If the value does not support type double - invalid value, close the program.
     * @param i - index of current sign or function.
     * @param subexpression - list of elements of analyzed expression.
     */
    private void sqrtAction(int i, ArrayList<Object> subexpression) {
        try {
            double sqrtResult = Math.sqrt((Double)subexpression.get(i+1));
            if (Double.isNaN(sqrtResult)) {
                System.out.println("Error: result of sqrt = " + sqrtResult );
                System.exit(0);
            }
            subexpression.remove(i);
            subexpression.remove(i);
            subexpression.add(i,sqrtResult);
        } catch (Exception e) {
            System.out.println("Error: Invalid value of operation sqrt");
            System.exit(0);
        }
    }

    /**
     * Will try to calculate the power of the number.
     * Rewrites the expression.
     * If the value does not support type double - invalid value, close the program.
     * @param i - index of current sign or function.
     * @param subexpression - list of elements of analyzed expression.
     */
    private void powerAction(int i, ArrayList<Object> subexpression) {
        try {
            double powerResult = Math.pow((Double)subexpression.get(i-1), (Double)subexpression.get(i+1));
            subexpression.remove(i-1);
            subexpression.remove(i-1);
            subexpression.remove(i-1);
            subexpression.add(i-1,powerResult);
        } catch (Exception e) {
            System.out.println("Error: Invalid value of operation to power");
            System.exit(0);
        }
    }

    /**
     * Will try to calculate the division of the numbers.
     * Rewrites the expression.
     * If the value does not support type double - invalid value, close the program.
     * @param i - index of current sign or function.
     * @param subexpression - list of elements of analyzed expression.
     */
    private void divisionAction(int i, ArrayList<Object> subexpression) {
        try {
            double divisionResult = (Double)subexpression.get(i-1) / (Double)subexpression.get(i+1);
            if (Double.isInfinite(divisionResult)) {
                System.out.println("Error: division by zero!");
                System.exit(0);
            }
            subexpression.remove(i-1);
            subexpression.remove(i-1);
            subexpression.remove(i-1);
            subexpression.add(i-1,divisionResult);
        } catch (Exception e) {
            System.out.println("Error: Invalid value of operation multiplication");
            System.exit(0);
        }
    }

    /**
     * Will try to calculate the multiplication of the numbers.
     * Rewrites the expression.
     * If the value does not support type double - invalid value, close the program.
     * @param i - index of current sign or function.
     * @param subexpression - list of elements of analyzed expression.
     */
    private void multiplicationAction(int i, ArrayList<Object> subexpression) {
        try {
            double multiplicationResult = (Double)subexpression.get(i-1) * (Double)subexpression.get(i+1);
            subexpression.remove(i-1);
            subexpression.remove(i-1);
            subexpression.remove(i-1);
            subexpression.add(i-1,multiplicationResult);
        } catch (Exception e) {
            System.out.println("Error: Invalid value of operation multiplication");
            System.exit(0);
        }
    }

    /**
     * Will try to calculate the subtraction of the numbers.
     * Rewrites the expression.
     * If the value does not support type double - invalid value, close the program.
     * @param i - index of current sign or function.
     * @param subexpression - list of elements of analyzed expression.
     */
    private void subtractionAction(int i, ArrayList<Object> subexpression) {
        try {
            double subtractionResult = (Double)subexpression.get(i-1) - (Double)subexpression.get(i+1);
            subexpression.remove(i-1);
            subexpression.remove(i-1);
            subexpression.remove(i-1);
            subexpression.add(i-1,subtractionResult);
        } catch (Exception e) {
            System.out.println("Error: Invalid value of operation subtraction");
            System.exit(0);
        }

    }

    /**
     * Will try to calculate the addition of the numbers.
     * Rewrites the expression.
     * If the value does not support type double - invalid value, close the program.
     * @param i - index of current sign or function.
     * @param subexpression - list of elements of analyzed expression.
     */
    private void additionAction(int i, ArrayList<Object> subexpression) {
        try {
            double additionResult = (Double)subexpression.get(i-1) + (Double)subexpression.get(i+1);
            subexpression.remove(i-1);
            subexpression.remove(i-1);
            subexpression.remove(i-1);
            subexpression.add(i-1,additionResult);
        } catch (Exception e) {
            System.out.println("Error: Invalid value of operation addition");
            System.exit(0);
        }
    }

}
