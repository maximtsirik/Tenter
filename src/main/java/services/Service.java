package services;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Service {
    private static final Map<String, String> SMALLDICTIONARY = readCSV("small");
    private static final Map<String, String> BIGDICTIONARY = readCSV("big");

    public static String buildNumber(final String number) {
        String result = "";
        LinkedList<String> rankedList = rankNumber(number);
        while (!rankedList.isEmpty()) {
            if (rankedList.size() > 2) {
                int z = rankedList.size() * 3 - 3;
                result += countBillions(rankedList.pollFirst(), z);
            } else if (rankedList.size() == 2 && rankedList.getLast().equals("000")){
                result += countThousands(rankedList.pollFirst()).replaceFirst(" ноль", "");
                return result;
            } else if (rankedList.size() == 2) {
                result += countThousands(rankedList.pollFirst()) + " ";
            } else if (rankedList.size() == 1) {
                result += countHundreds(rankedList.pollFirst());
            }
        }
        return result;
    }

    public static LinkedList<String> rankNumber(final String number) {
        char[] array = number.toCharArray();
        List<Character> list = new ArrayList<>();
        LinkedList<String> numberList = new LinkedList<>();

        for (char c : array) { list.add(c); }

        for (int i = 0; i < number.length(); i += 3) {
            if (list.size() % 3 == 0) {
                String s = "";
                s += list.get(list.size() - 3);
                s += list.get(list.size() - 2);
                s += list.get(list.size() - 1);
                numberList.add(s);
                list.remove(list.size() - 1);
                list.remove(list.size() - 1);
                list.remove(list.size() - 1);
            } else if (list.size() % 3 == 1) {
                numberList.add(i, String.valueOf(list.get(0)));
                list.remove(0);
            } else if (list.size() % 3 == 2) {
                String s = "";
                s += list.get(0);
                s += list.get(1);
                numberList.add(s);
                list.remove(0);
                list.remove(0);
            }
        }
        if (numberList.getFirst().length() < 3) {
            String buffer = numberList.pollFirst();
            Collections.reverse(numberList);
            numberList.addFirst(buffer);
        } else Collections.reverse(numberList);
        return numberList;
    }

    public static String countBillions(final String number, int countOfZeros) {
        String result = "";
        if (number.equals("000")){
            return result = "";
        }

        String postfix = "";
        char[] array = number.toCharArray();


        byte last = Byte.parseByte(String.valueOf(array[array.length - 1]));
        byte preLast = 0;
        if (number.length() > 1) {
            preLast = Byte.parseByte(String.valueOf(array[array.length - 2]));
        }

        if (last == 1 && !(preLast == 1)) {
            postfix = " " + BIGDICTIONARY.get(String.valueOf(countOfZeros)) + " ";
        } else if (last >= 2 && last <= 4 && !(preLast == 1)) {
            postfix = " " + BIGDICTIONARY.get(String.valueOf(countOfZeros)) + "а ";
        } else postfix = " " + BIGDICTIONARY.get(String.valueOf(countOfZeros)) + "ов ";

        result += countHundreds(number) + postfix;

        return result;
    }

    public static String countThousands(final String number) {
        String result = "";
        if (number.equals("000")){
            return result = "";
        }

        String postfix = "";
        char[] array = number.toCharArray();

        byte last = Byte.parseByte(String.valueOf(array[array.length - 1]));
        byte preLast = 0;
        if (number.length() > 1) {
            preLast = Byte.parseByte(String.valueOf(array[array.length - 2]));
        }

        if (last == 1 && !(preLast == 1)) {
            postfix = " тысяча";
        } else if (last >= 2 && last <= 4 && !(preLast == 1)) {
            postfix = " тысячи";
        } else postfix = " тысяч";

        if (last == 1 && !(preLast == 1)) {
            result += countHundreds(number) + postfix;
            return result.replaceFirst("один", "одна");
        }
        if (last == 2 && !(preLast == 1)) {
            result += countHundreds(number) + postfix;
            return result.replaceFirst("два", "две");
        }

        result += countHundreds(number) + postfix;

        return result;
    }

    public static String countHundreds(final String string) {
        String result = "";
        String number = "";
        if (string.length() != 1) {
            number = StringUtils.removeStart(string, "0");
            number = StringUtils.removeStart(number, "0");
        } else number = string;

        if (number.equals("000")){
            return result = "";
        }

        if (number.length() <= 3 && SMALLDICTIONARY.containsKey(number)) {
            result = SMALLDICTIONARY.get(number);
        } else if (number.length() == 2 && !SMALLDICTIONARY.containsKey(number)) {
            char[] array = number.toCharArray();
            String a = String.valueOf(array[0]);
            String b = String.valueOf(array[1]);
            result = SMALLDICTIONARY.get(a + "0") + " " + SMALLDICTIONARY.get(b);
        } else if (number.length() == 3 && !SMALLDICTIONARY.containsKey(number)) {
            char[] array = number.toCharArray();
            if (String.valueOf(array[1]).equals("1")) {
                String a = String.valueOf(array[0]);
                String s = StringUtils.join(array[1], array[2]);
                result = SMALLDICTIONARY.get(a + "00") + " " + SMALLDICTIONARY.get(s);
            } else if (String.valueOf(array[1]).equals("0")){
                String a = String.valueOf(array[0]);
                String c = String.valueOf(array[2]);
                result = SMALLDICTIONARY.get(a + "00") + " " + SMALLDICTIONARY.get(c);
            } else {
                String a = String.valueOf(array[0]);
                String b = String.valueOf(array[1]);
                String c = String.valueOf(array[2]);
                result = SMALLDICTIONARY.get(a + "00") + " " + SMALLDICTIONARY.get(b + "0") + " " + SMALLDICTIONARY.get(c);
            }
        }
        return result;
    }

    public static Map<String, String> readCSV(final String type) {
        String path = "";
        if (type.equals("small")) {
            path = "src/main/resources/CountToTen.csv";
        } else if (type.equals("big")) {
            path = "src/main/resources/BigNumbersNames.csv";
        }
        String line;
        Map<String, String> smallNumbersDictionary = new HashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                smallNumbersDictionary.put(values[0], values[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return smallNumbersDictionary;
    }
}
