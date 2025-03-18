package com.bbv.object;

public interface Mana {
    int getMax();
    int getNow();
    void setMax(int value);
    void setNow(int value);
    void consume(int value);
    void add(int value);
}
