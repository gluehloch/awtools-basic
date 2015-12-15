package de.awtools.basic.groovy;

class Operator implements IOperator {

    public int add(int x, int y) {
        x + y
    }

    static void main(args) {
        println new Operator().add(2, 3)
    }

}