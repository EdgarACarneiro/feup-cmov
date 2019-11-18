package org.feup.cmov.acmecustomer.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingCart implements Serializable {

    private ArrayList<Pair<Product, Integer>> products;

    public static class Pair<F, S> implements Serializable{
        F first;
        S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public F getFirst() {
            return first;
        }

        public void setFirst(F first) {
            this.first = first;
        }

        public S getSecond() {
            return second;
        }

        public void setSecond(S second) {
            this.second = second;
        }
    }

    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product p) {
        System.out.println(this.containsProduct(p));
        if(this.containsProduct(p)) {
            for(int i = 0; i < this.products.size(); i++) {
                if(this.products.get(i).first.getName().equals(p.getName())) {
                    Pair<Product, Integer> newPair = new Pair<>(p, this.products.get(i).getSecond() + 1);
                    this.products.set(i, newPair);
                }
            }
        } else {
            this.products.add(new Pair<>(p, 1));
        }
        System.out.println(this.products);
    }

    public void removeProductQuantity(int position) {
        this.products.set(position, new Pair<>(this.products.get(position).first, this.products.get(position).second - 1));
    }

    public boolean containsProduct(Product p) {
        for(int i = 0; i < this.products.size(); i++) {
            if(this.products.get(i).first.getName().equals(p.getName())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Product> toArrayList() {
        ArrayList<Product> prods = new ArrayList<>();
        for(int i = 0; i < this.products.size(); i++) {
            for(int j = 0; j < this.products.get(i).second; j++) {
                prods.add(this.products.get(i).first);
            }
        }
        return prods;
    }

    public void removeProduct(int position) {
        this.products.remove(position);
    }

    public int amountOfProducts() {
        int amount = 0;
        for(int i = 0; i < this.products.size(); i++) {
            amount += this.products.get(i).second;
        }
        return amount;
    }

    public boolean isFull() {
        return this.amountOfProducts() >= 10;
    }

    public ArrayList<Pair<Product, Integer>> getProducts() {
        return this.products;
    }

    public void setProducts(ArrayList<Pair<Product, Integer>> products) {
        this.products = products;
    }

    public void clearProducts() {
        this.products = new ArrayList<>();
    }

    public double getValue() {
        double value = 0.0;
        for(int i = 0; i < this.products.size(); i++) {
            value += this.products.get(i).first.getFullPrice() * this.products.get(i).second;
        }
        return value;
    }
}
