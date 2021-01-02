package com.example.cardwars;

import java.util.Comparator;

public class CompareByPoints implements Comparator<Player_Winner> {

    @Override
    public int compare(Player_Winner o1, Player_Winner o2) {
        return o2.getMax_score()-o1.getMax_score();
    }
}
