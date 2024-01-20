package test;

import java.util.ArrayList;
import java.util.List;

public class NeighbourTest {
    List<int[]> idList = new ArrayList<>();

    NeighbourTest(){
        System.out.println("eggs");
        idList.add(new int[]{3,2,1});
        idList.add(new int[]{1,2,3});
        idList.add(new int[]{4,5,6});


        int[] neighbour = new int[]{1,2,3};
        for(int[] id: idList) {
            if (neighbour[0] == id[0] && neighbour[1] == id[1] && neighbour[2] == id[2]){
                System.out.println(idList.indexOf(id));
            }
        }
    }

    public static void main(String[] args){
        NeighbourTest neighbourTest = new NeighbourTest();
        //should output index of 1 instead of 0
    }
}

