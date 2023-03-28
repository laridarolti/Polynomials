package org.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.*;


public class PolynomialCalculator extends JFrame implements ActionListener {
    private JTextField inputField1, inputField2;
    private JButton addButton, subtractButton, multiplyButton, integrateButton, differentiateButton, divideButton;
    private JLabel resultLabel;

    public PolynomialCalculator() {
        super("Polynomial Calculator");

        inputField1 = new JTextField(20);
        inputField2 = new JTextField(20);
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        subtractButton = new JButton("Subtract");
        subtractButton.addActionListener(this);
        multiplyButton = new JButton("Multiply");
        multiplyButton.addActionListener(this);
        divideButton = new JButton("Divide");
        divideButton.addActionListener(this);
        integrateButton = new JButton("Integrate");
        integrateButton.addActionListener(this);
        differentiateButton = new JButton("Differentiate");
        differentiateButton.addActionListener(this);
        resultLabel = new JLabel("");

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Enter first polynomial:"));
        panel.add(inputField1);
        panel.add(new JLabel("Enter second polynomial:"));
        panel.add(inputField2);
        panel.add(addButton);
        panel.add(subtractButton);
        panel.add(multiplyButton);
        panel.add(divideButton);
        panel.add(integrateButton);
        panel.add(differentiateButton);
        panel.add(resultLabel);

        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setSize(300,400);
    }


    public Polynom stringToPolynomial(String input){
        String[] poly = input.replace("^", "").split("((?=\\+)|(?=\\-)|x)");
        // should now get reverse order
        Polynom polynom = new Polynom();

        // create array with exponents and coefficients from the first polynomial, they are already unique
        for (int i = 0; i < poly.length; i += 2) {
            var coefficient = Double.parseDouble(poly[i]);
            var exponent = Integer.parseInt(poly[i + 1]);
            polynom.getTreeMap().put(exponent, coefficient);
        }
        return polynom;
    }

    public Polynom addPolynomials(Polynom poly1, Polynom poly2) {

        for (Map.Entry<Integer, Double> entry2 : poly2.getTreeMap().entrySet()) {
            Integer exp2 = entry2.getKey();
            Double coeff2 = entry2.getValue();

            if(poly1.getTreeMap().containsKey(exp2)){
                // get key
                var oldCoeff = poly1.getTreeMap().get(exp2);
                var newCoeff = coeff2+oldCoeff;
                if (newCoeff!=0){
                    poly1.getTreeMap().put(exp2, newCoeff);
                }
            }
            else {
                poly1.getTreeMap().put(exp2, coeff2);
            }
        }
        return poly1;
    }


    public Polynom subtractPolynomials(Polynom poly1, Polynom poly2) {

        for (Map.Entry<Integer, Double> entry2 : poly2.getTreeMap().entrySet()) {
            Integer exp2 = entry2.getKey();
            Double coeff2 = entry2.getValue();

            if(poly1.getTreeMap().containsKey(exp2)){
                // get key
                var oldCoeff = poly1.getTreeMap().get(exp2);
                var newCoeff = oldCoeff-coeff2;
                if (newCoeff!=0){
                    poly1.getTreeMap().put(exp2, newCoeff);
                }
            }
            else {
                poly1.getTreeMap().put(exp2, -coeff2);
            }
        }
        return poly1;
    }


    public Polynom multiplyPolynomials(Polynom poly1, Polynom poly2) {

        Polynom polynom = new Polynom();

        for (Map.Entry<Integer, Double> entry1 : poly1.getTreeMap().entrySet()) {
            Integer exp1 =entry1.getKey();
            Double coeff1 = entry1.getValue();
            for (Map.Entry<Integer, Double> entry2 : poly2.getTreeMap().entrySet()) {
                Integer exp2 = entry2.getKey();
                Double coeff2 = entry2.getValue();

                var newCoeff = coeff1 * coeff2;
                var newExp = exp1 + exp2;

                if(polynom.getTreeMap().containsKey(newExp)){
                    // get key
                    var oldCoeff = polynom.getTreeMap().get(newExp);
                    polynom.getTreeMap().put(newExp, newCoeff+oldCoeff);
                }
                else {
                    polynom.getTreeMap().put(newExp, newCoeff);
                }
            }
        }
        return polynom;
    }


    public Polynom dividePolynomials(String input1, String input2) {

        Polynom poly_deimpartit = stringToPolynomial(input1);
        Polynom poly_impartitor = stringToPolynomial(input2);
        Polynom poly_cat = new Polynom();

        // DEIMPARTIT = IMPARTTOR * CAT + REST
        Polynom rest = new Polynom(); // initializam
        int i = 0;
        do{
            // get coefficient and exponent of i th term in deimpartit and impartitor
            var coeff1 = poly_deimpartit.getTreeMap().entrySet().stream().skip(i)
                    .map(Map.Entry::getValue).findFirst().get();
            var exp1 = poly_deimpartit.getTreeMap().entrySet().stream().skip(i)
                    .map(Map.Entry::getKey).findFirst().get();
            var coeff2 = poly_impartitor.getTreeMap().entrySet().stream().skip(0)
                    .map(Map.Entry::getValue).findFirst().get();
            var exp2 = poly_impartitor.getTreeMap().entrySet().stream().skip(0)
                    .map(Map.Entry::getKey).findFirst().get();

            var coeff = coeff1/coeff2;
            var exp = exp1 - exp2;
            if (coeff !=0 && exp >= 0){
                poly_cat.getTreeMap().put(exp, coeff);
            }
            else if (exp < 0){
                break;
            }

            // has to be a new polynomial every time
            Polynom poly_to_subtract = new Polynom();
            poly_to_subtract = multiplyPolynomials(poly_impartitor, poly_cat);
            System.out.println("poly to subtract is " + poly_to_subtract.getTreeMap());

            // calculate rest
            rest = subtractPolynomials(poly_deimpartit, poly_to_subtract);
            poly_deimpartit = rest;
            System.out.println("rest is "+ rest.getTreeMap());

            i+=1;
            System.out.println("catul este "+ poly_cat.getTreeMap());
        } while (rest.getTreeMap().size()>0);

        return poly_cat;
    }


    public Polynom integratePolynomial(String input1) {

        String[] poly1 = input1.replace("^", "").split("((?=\\+)|(?=\\-)|x)");
        Polynom polynom = new Polynom();
        // create array with exponents and coefficients from the first polynomial, they are already unique
        for (int i = 0; i < poly1.length; i += 2) {
            var coefficient = Double.parseDouble(poly1[i]);
            var exponent = Integer.parseInt(poly1[i + 1]);

            Monom monom = new Monom(exponent+1, coefficient / (exponent + 1));
            polynom.addMonomial(monom);
            System.out.println(polynom.getTreeMap());

        }
        return polynom;
    }


    public Polynom differentiatePolynomial(String input1) {

        String[] poly1 = input1.replace("^", "").split("((?=\\+)|(?=\\-)|x)");
        Polynom polynom = new Polynom();
        // create array with exponents and coefficients from the first polynomial, they are already unique
        for (int i = 0; i < poly1.length; i += 2) {
            var coefficient = Double.parseDouble(poly1[i]);
            var exponent = Integer.parseInt(poly1[i + 1]);
            if (exponent != 0){
                Monom monom = new Monom(exponent-1, coefficient * exponent );
                polynom.addMonomial(monom);
                System.out.println(polynom.getTreeMap());
            }
        }
        return polynom;
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String input1 = inputField1.getText();
            String input2 = inputField2.getText();
            Polynom result = addPolynomials(stringToPolynomial(input1),stringToPolynomial(input2));
            resultLabel.setText("Result: " + result);
        } else if (e.getSource() == subtractButton) {
            String input1 = inputField1.getText();
            String input2 = inputField2.getText();
            Polynom result = subtractPolynomials(stringToPolynomial(input1),stringToPolynomial(input2));
            resultLabel.setText("Result: " + result);
        } else if (e.getSource() == multiplyButton) {
            String input1 = inputField1.getText();
            String input2 = inputField2.getText();
            Polynom result = multiplyPolynomials(stringToPolynomial(input1),stringToPolynomial(input2));
            resultLabel.setText("Result: " + result);
        }
        else if (e.getSource() == divideButton) {
            String input1 = inputField1.getText();
            String input2 = inputField2.getText();
            Polynom result = dividePolynomials(input1,input2);
            resultLabel.setText("Result: " + result);
        }
        else if (e.getSource() == integrateButton) {
            String input1 = inputField1.getText();
            Polynom result = integratePolynomial(input1);
            resultLabel.setText("Result: " + result);
        }
        else if (e.getSource() == differentiateButton) {
            String input1 = inputField1.getText();
            Polynom result = differentiatePolynomial(input1);
            resultLabel.setText("Result: " + result);
        }
    }
    public static void main(String[] args) {
        PolynomialCalculator newPolynomialCalc= new PolynomialCalculator();
    }

    ///// INPUT EXAMPLES
    // +4x^2+1x^1+3x^0 +1x^3+4x^4
    // -4x^2-1x^1-3x^0 -1x^3-4x^4
    // +1x^3+2x^2 +2x^1+5x^0
    // +4x^2-5x^1-21x^0 +1x^1-3x^0
    //1x^2+2x^1+1x^0 1x^1+1x^0
    //1x^2+6x^1+9x^0 1x^1+3x^0
    //1x^2-6x^1+9x^0 1x^1-3x^0

} ////contine si derivata si integrala
