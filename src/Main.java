import java.util.LinkedList;

public class Main {
    public static Integer[] task1(Integer[] inArray) {
        LinkedList<Integer> linkedList = new LinkedList<Integer>();
        for (int i = inArray.length-1; i >=0 ; i--) {
            if (inArray[i]!=4){
                linkedList.addFirst(inArray[i]);}
            else {
                break;
            }
        }
        if (inArray.length == linkedList.size()){
            throw new RuntimeException("В массиве нет четверки");
        } else {
            return linkedList.toArray(new Integer[linkedList.size()]);
        }
    };
    public static boolean task2(Integer[] inArray) {
        boolean one = false, four = false;
        for (int i:inArray
        ) {
            if (i == 1) one = true;
            else
            if (i == 4) four = true;
        }
        return (one && four);
    };


    public static void main(String[] args) {


    }

}

