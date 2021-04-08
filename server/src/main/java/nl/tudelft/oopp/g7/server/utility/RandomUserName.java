package nl.tudelft.oopp.g7.server.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RandomUserName {
    Logger logger = LoggerFactory.getLogger(Config.class);

    private static List<String> animalList;
    private static List<String> colorList;

    /**
     * Constructor for the RandomUserName class.
     */
    public RandomUserName() {
        animalList = new ArrayList<>();
        colorList = new ArrayList<>();
        loadUserNameFiles(new File("./animals.txt"), animalList);
        loadUserNameFiles(new File("./colors.txt"), colorList);
    }

    /**
     * Loads the contents of the file to a list.
     * @param nameFile The file whose contents will be read.
     * @param nameList The list that will be filled.
     */
    public void loadUserNameFiles(File nameFile, List<String> nameList) {
        try {
            Scanner scanner = new Scanner(nameFile);

            while (scanner.hasNextLine()) {
                nameList.add(scanner.nextLine());
            }

            scanner.close();
        } catch (IOException e) {
            logger.error("An error occurred while loading the config", e);
        }
    }

    /**
     * Picks a random option from each list and returns a custom username.
     * @return A custom user name with "anonymous color animal".
     */
    public static String getRandomUserName() {
        Random random = new Random();

        String randomAnimal = animalList.get(random.nextInt(593));
        String randomColor = colorList.get(random.nextInt(22));

        return String.format("Anonymous %s %s", randomColor, randomAnimal);
    }
}
