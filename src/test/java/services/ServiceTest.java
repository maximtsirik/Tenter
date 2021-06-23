package services;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    private static final Map<String, String> SMALLDICTIONARY = Service.readCSV("small");

    @Test
    void buildNumber() {
        String s = "один триллион один миллиард один миллион одна тысяча сто один";
        assertEquals(s, Service.buildNumber("1001001001101"));
        for (Map.Entry<String, String> entry : SMALLDICTIONARY.entrySet()){
            assertEquals(entry.getValue(), Service.buildNumber(entry.getKey()));
        }
        assertEquals("одна тысяча", Service.buildNumber("1000"));
        assertEquals("один миллион одна тысяча", Service.buildNumber("1001000"));
    }

    @Test
    @RepeatedTest(50)
    void rankNumber() {
        String number = randomNumber();
        if (number.length() % 3 == 0){
            assertEquals(number.length() / 3, Service.rankNumber(number).size());
        } else assertEquals((number.length() / 3) + 1, Service.rankNumber(number).size());
    }

    @Test
    void countBillions() {
        assertEquals("", Service.countBillions("000", 9));
        assertEquals("один миллиард ", Service.countBillions("001", 9));
        assertEquals("сорок два нониллиона ", Service.countBillions("042", 30));
        assertEquals("пятьсот двенадцать третригинтиллионов ", Service.countBillions("512", 102));
    }

    @Test
    void countThousands() {
        assertEquals("", Service.countThousands("000"));
        assertEquals("одна тысяча", Service.countThousands("001"));
        assertEquals("сорок две тысячи", Service.countThousands("042"));
        assertEquals("пятьсот двенадцать тысяч", Service.countThousands("512"));
    }

    @Test
    void countHundreds() {
        assertEquals("ноль", Service.countHundreds("000"));
        for (Map.Entry<String, String> entry : SMALLDICTIONARY.entrySet()){
            assertEquals(entry.getValue(), Service.buildNumber(entry.getKey()));
        }
    }

    private static String randomNumber () {
        String result ="10";
        for (int i = 0; i < (int) (Math.random() * 100); i++) {
            result += String.valueOf((int) (Math.random() * 100));
        }
        return result;
    }

}