package com.presentation;

import com.domain.salad.SimpleSalad;
import com.domain.vegetable.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ConsolePresentation implements CommandLineRunner {
    private static Map<Integer, String> vegetablesMap;
    private static List<Integer> alreadyPutted;
    private static final Logger logger = LoggerFactory.getLogger(ConsolePresentation.class);

    @Override
    public void run(String... args) throws Exception {
        main(args);
    }

    public static void main(String[] args) {
        logger.debug("Application successfully started");
        System.out.println("This is cooking program\n" +
                "Available list of vegetables:\n" +
                "1: Carrot\n" +
                "2: Cucumber\n" +
                "3: Kale\n" +
                "4: Onion\n" +
                "5: Tomato\n\n" +
                "In order to cook simple salad, please print vegetable number and its weight in grams.\n" +
                "If you want to end input and want to cook salad just print END.\n");


        List<AbstractVegetable> vegetableList = getSaladComponentsFromInput();
        SimpleSalad salad = new SimpleSalad(vegetableList);
        salad.makeSalad();

        System.out.println("Calorific value of salad is " + salad.countCalories());

        for (AbstractVegetable v : salad.sortByWeight()) {
            System.out.println("Vegetable " + v.getName() + " weight: " + v.getWeight());
        }
        int[] calorificRange = getCalorificRange();

        for (AbstractVegetable v : salad.getVegetablesWithCaloriesRange(calorificRange[0], calorificRange[1])) {
            System.out.println("Vegetable " + v.getName() + ". CalorificValue: " + v.getCalorificValue());
        }

        if (salad.getVegetablesWithCaloriesRange(calorificRange[0], calorificRange[1]).isEmpty()) {
            System.out.println("There is no vegetables according to your chose");
        }
    }

    private static List<AbstractVegetable> getSaladComponentsFromInput() {
        Scanner scanner = new Scanner(System.in);
        String input;
        int vegetableNumber;
        int vegetableWeight;
        List<AbstractVegetable> vegetableList = new ArrayList<>();
        vegetablesMap = new HashMap<>();
        vegetablesMap.put(1, "Carrot");
        vegetablesMap.put(2, "Cucumber");
        vegetablesMap.put(3, "Kale");
        vegetablesMap.put(4, "Onion");
        vegetablesMap.put(5, "Tomato");
        logger.debug("Initiated vegetableList");
        alreadyPutted = new ArrayList<>();

        while (true) {
            input = scanner.nextLine().trim();

            if (input.toUpperCase().equals("END")) {
                logger.info("User chose to END his work with program");
                if (vegetableList.isEmpty()) {
                    System.exit(0);
                }
                System.out.println("You have ended putting vegetables to your salad");
                break;
            } else if (input.equals("")) {
                logger.info("user did not enter any  input value ");
                System.out.println("Please enter values: 1 - vegetable; 2 - weight");
                continue;
            }

            String[] params = input.split("\\s+");
            if (params.length != 2) {
                logger.error("user entered wrong number of params. Number of params is {}", params.length);
                System.out.println("Please enter valid number of params");
                continue;
            }

            vegetableNumber = Integer.parseInt(params[0]);
            vegetableWeight = Integer.parseInt(params[1]);
            if (!isValidNumberAndWeight(vegetableNumber, vegetableWeight)) {
                continue;
            }

            alreadyPutted.add(vegetableNumber);
            System.out.println("Vegetable " + getVegetableMap().get(vegetableNumber) + " will be added to your salad");

            switch (vegetableNumber) {
                case (1):
                    vegetableList.add(new Carrot(vegetableWeight));
                    break;
                case (2):
                    vegetableList.add(new Cucumber(vegetableWeight));
                    break;
                case (3):
                    vegetableList.add(new Kale(vegetableWeight));
                    break;
                case (4):
                    vegetableList.add(new Onion(vegetableWeight));
                    break;
                case (5):
                    vegetableList.add(new Tomato(vegetableWeight));
                    break;
                default:
                    break;
            }

            if (alreadyPutted.size() == 5) {
                logger.debug("User filled out his salad with all vegetables");
                System.out.println("It is 5 types of vegetables in salad");
                break;
            }

        }
        return vegetableList;
    }

    private static int[] getCalorificRange() {
        Scanner scanner = new Scanner(System.in);
        String input;
        String[] tempStringArray;
        int[] calorificRange = new int[2];

        System.out.println("Please enter calorific range in decimal format: MIN_CALORIFIC MAX_CALORIFIC\n");

        while (true) {
            input = scanner.nextLine().trim();

            if (input.equals("")) {
                logger.warn("User did not input range of calorific value");
                System.out.println("Please enter range");
                continue;
            }

            tempStringArray = input.split("\\s+");
            if (tempStringArray.length != 2) {
                logger.error("User entered {} params. It is wrong quantity.", tempStringArray.length);
                System.out.println("You entered wrong quantity of params");
                continue;
            }
            for (int i = 0; i < 2; i++) {
                calorificRange[i] = Integer.parseInt(tempStringArray[i]);
            }
            if (calorificRange[0] == 0 || calorificRange[1] == 0) {
                logger.error("user entered params not in decimal format");
                System.out.println("Please enter params in decimal format");
            } else if (calorificRange[0] < 1 || calorificRange[1] < 1) {
                logger.error("user entered negative numbers or zero");
                System.out.println("Range can not be negative or zero");
            } else {
                break;
            }
        }
        return calorificRange;
    }

    private static boolean isValidNumberAndWeight(int vegetableNumber, int vegetableWeight) {
        Map<Integer, String> vegetablesMap = getVegetableMap();

        if (vegetableNumber > 5 || vegetableNumber == 0) {
            System.out.println("Please enter valid number of vegetable");
            return false;
        } else if (vegetableWeight == 0) {
            System.out.println("Please enter weight of vegetable in decimal");
            return false;
        } else if (getAlreadyPutted().contains(vegetableNumber)) {
            System.out.println("Wrong number. You already have  " + vegetablesMap.get(vegetableNumber) + " in your salad");
            return false;
        } else if (vegetableNumber < 1 || vegetableWeight < 1) {
            System.out.println("Vegetable number and weight can not be zero or negative");
            return false;
        }
        return true;
    }

    private static Map<Integer, String> getVegetableMap() {

        return vegetablesMap;
    }

    private static List<Integer> getAlreadyPutted() {

        return alreadyPutted;
    }
}
