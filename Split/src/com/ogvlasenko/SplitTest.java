package com.ogvlasenko;

public class SplitTest {

        public static void main(String args[]) {
            String Str = new String("KIEV\\OGVlasenko");

            System.out.println("Возвращаемое значение: ");
            for (String retval : Str.split("\\\\")) {
                System.out.println(retval);
            }


            System.out.println("First: " + Str.split("\\\\")[0]);
            System.out.println("Second: " + Str.split("\\\\")[1]);
        }

}
