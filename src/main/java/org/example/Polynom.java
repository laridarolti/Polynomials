package org.example;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Map;


public class Polynom extends PolynomialCalculator  {
    private TreeMap<Integer, Double> polynomial;

    public Polynom(TreeMap<Integer, Double> polynomial) {
        this.polynomial = polynomial;
    }

    public TreeMap<Integer, Double> getTreeMap() {
        return polynomial;
    }

    public void setTreeMap(TreeMap<Integer, Double> polynomial) {
        this.polynomial = polynomial;
    }

    public Polynom() {
        // empty treemap, with reverse order
        polynomial = new TreeMap<>(Collections.reverseOrder());
    }

    public Polynom(Monom monomial) {
        this.polynomial = new TreeMap<>();
        this.polynomial.put(monomial.getExp(), monomial.getCoef());
    }

    public void addMonomial(Monom monomial) {
        if (monomial.getCoef() != 0) {
            polynomial.put(monomial.getExp(), monomial.getCoef());
        }
    }

    @Override
    public String toString() {
        return "Polynom{" +
                "polynomial=" + polynomial +
                '}';
    }


}