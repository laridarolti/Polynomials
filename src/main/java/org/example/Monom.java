package org.example;

public class Monom {
    private int exp;
    private double coef;

    public Monom(int exp,double coef) {
        this.coef=coef;
        this.exp=exp;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public double getCoef() {
        return coef;
    }

    public void setCoef(double coef) {
        this.coef = coef;
    }
}
