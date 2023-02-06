package griffith;
/*
 * opt : Joshan JOhn
 * Student no: 3092883
 * 26/01/2023
 * 
 */

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
    public static void main(String[] args) {
        initGame();
    }

    public static void initGame() {

        Scanner sn = new Scanner(System.in); // scanner object
        Random rand = new Random(); // random object

        long highScore[] = { 0, 0, 0 }; // array of long for highscore
        int score = 0; // variable to store score after each game
        int win = 0; // variable to store total number of won games
        int choice; // variable to choice

        /*
         * array of easy words
         */
        String easy[] = { "young", "java", "python", "html", "cake", "school", "stop", "the", "what", "which", "map",
                "gun" };
        /*
         * array of hard words
         */
        String hard[] = { "memory", "morning", "possible", "javascript", "computer", "system", "development",
                "advertisement" };

        /*
         * Hungman Game loop
         */
        while (true) {

            menu(); // print menu
            System.out.println("Enter the choice");

            try {
                choice = sn.nextInt(); // inputing choice
            } catch (Exception ex) {
                // since only numbers are accepted
                System.out.println("\n! only choice number !\n");

                sleep(2000); // wait for 2 seconds
                initGame(); // calling again if it's not a number
                break;
            }

            /*
             * if the inputted choice is 1
             */
            if (choice == 1) {
                /*
                 * E A S Y M O D E
                 */
                while (true) {
                    /*
                     * creating an instance of methord Hungman
                     * store the returning array of integers in a variable called instance
                     */
                    int[] instance = HungmanGame(sn, rand, easy); // calling HungmanGame method
                    score += instance[0]; // retriving score from instance variable and updating to score variable
                    /*
                     * if the second value in instance variable 1 implies game won else fail
                     * the win variable is get updated when based on instance second value
                     */
                    win += (instance[1] == 1) ? 1 : 0;

                    System.out.print("Score = " + score); // printing score
                    System.out.println("\t\tTotal win = " + win); // printing total game won
                    sleep(1000); // wait for 1 second

                    if (instance[1] == 0) {
                        /*
                         * if second value of is zero
                         * 
                         */
                        System.out.print("start a new game (y/n)? "); // asking for a new game to continue next word
                        String opt = sn.next(); // inputing user option

                        if ((opt.toLowerCase()).equals("y") || opt.toLowerCase().equals("yes")) {
                            /*
                             * if user want to start new game
                             * the existing HighScore is update with score earn in latest game
                             */
                            highScore = updateHighScore(score, highScore); // calling updateHighScore method
                            score = 0; // resetting score variable
                            continue;

                        } else {
                            /*
                             * if user doesn't want to start new game or other game
                             * it update high score byy calling updateHighScore method
                             * and reset score variable, then break entire loop
                             */
                            highScore = updateHighScore(score, highScore);
                            score = 0;
                            win = 0;

                            break;

                        }
                    }
                }

            } else if (choice == 2) {
                /*
                 * H A R D M O D E
                 */
                while (true) {
                    int[] instance = HungmanGame(sn, rand, hard); // calling Hungman

                    score += instance[0]; // retriving score from instance variable and updating to score variable

                    /*
                     * if the second value in instance variable 1 implies game won else fail
                     * the win variable is get updated when based on instance second value
                     */
                    win += (instance[1] == 1) ? 1 : 0;

                    System.out.print("Score = " + score); // printing score
                    System.out.println("\t\tTotal win = " + win); // printing total game won
                    sleep(1000); // wait 1 second

                    if (instance[1] == 0) {
                        /*
                         * if second value of is zero
                         * 
                         */
                        System.out.print("start a new game (y/n)? "); // asking for a new game to continue next word
                        String opt = sn.next(); // inputing user option

                        if ((opt.toLowerCase()).equals("y") || opt.toLowerCase().equals("yes")) {
                            /*
                             * if user want to start new game
                             * the existing HighScore is update with score earn in latest game
                             */
                            highScore = updateHighScore(score, highScore); // calling updateHighScore method
                            score = 0; // resetting score variable
                            continue;

                        } else {
                            /*
                             * if user doesn't want to start new game or other game
                             * it update high score byy calling updateHighScore method
                             * and reset score variable, then break entire loop
                             */
                            highScore = updateHighScore(score, highScore);
                            score = 0;
                            win = 0;
                            break;

                        }
                    }
                }
            } else if (choice == 3) {
                /*
                 * displaying Highes Score
                 */
                displayHighScore(highScore); // calling displayHighScore method
            } else if (choice == 4) {
                /*
                 * display the rules of game and a guid on how to play the whole game
                 */
                rules(); // calling rules method
            } else if (choice == 5) {
                // break whole game loop
                break;
            } else {
                /*
                 * in case the choice number exceeds
                 * then, it loop back again
                 */
                System.out.println("\nEnter Choice number ");
                continue;
            }

        }
        System.out.println("\nC L O S I N G....");
        sleep(1000);
    }

    /*
     * H U N G M A N G A M E P R O C E S S
     */

    public static int[] HungmanGame(Scanner sn, Random rand, String[] words) {
        /*
         * Hungman Game
         * Scanner object, Random object, and String array as parameters
         * return score and a number indicating the indivigual game status as an integer
         * array
         */
        int score = 0; // declaring score variable for individual game
        int life = 7; // total life
        String guessedLetters = ""; // store guessed letters

        // generate a radom word out of array of words
        String randWord = words[rand.nextInt(0, words.length)];
        // creating array for secret word
        char secretWord[] = new char[randWord.length()];

        // fill every index of secret word with underscore(_) by looping through each
        // index values
        for (int i = 0; i < secretWord.length; i++) {
            secretWord[i] = '-'; // replacing with underscore(_)
        }
        /*
         * after generating secret/guessing word with underscore
         * we start playing and time is stored
         */
        Instant startTime = Instant.now(); // game starting time
        Instant endTime = Instant.now(); // game end time()

        /*
         * looping the game based on life availablity
         */
        while (life > 0) {

            int count = 0; // flag for checking inputter letter/ correct guessing
            /*
             * Generating Hungman Image
             * generating based on life
             */
            genHungmanImg(life);

            // displaying secreat word hint
            System.out.println("  " + new String(secretWord));

            System.out.print("guess a letter: ");
            String cheat = sn.next(); // inputing String
            char letter = cheat.charAt(0); // extracting first character from String

            /*
             * Cheact code checking
             */
            try {
                if (cheat.equals("exit")) {
                    /*
                     * if cheat is exit
                     * break the loop
                     */
                    break;
                } else if (cheat.equals("restart")) {
                    /*
                     * if the cheat is restart
                     * start from again by calling main method
                     */
                    main(words); // calling main method by simply passing word string array
                }
            } catch (Exception e) {
                /*
                 * if the input raise any exception
                 * contiue the loop
                 */
                continue;
            }

            /*
             * check repeated letters and updating count
             * checking the letter contains in guessedLetters String by getting indexOf
             * method
             * which will return a integer >= zero if it contains or return a negative value
             */
            if (!(guessedLetters.indexOf(letter) < 0)) {
                /*
                 * if the guessedLetters contains letter
                 */
                System.out.println("repeated"); // printing its repeated
                /*
                 * printing guessed words by calling displayGuessed method
                 * and continue the Hugman game process
                 */
                System.out.print("guessed : ");
                displayGueesed(guessedLetters); // calling displayGuessed method
                continue;
            } else {
                /*
                 * the letter is appended to guessedLetters
                 */
                guessedLetters += letter;
            }

            /*
             * looping from 0 to length of secret word
             */
            for (int i = 0; i < secretWord.length; i++) {
                /*
                 * cheching the inputted letter matches with
                 * the respective index value of random word generated
                 */
                if (randWord.charAt(i) == letter) {
                    secretWord[i] = letter; // the respective secret word array value is updated with letter
                    count++; // count is updated
                }
            }

            // printing guessed words
            System.out.print("guessed : ");
            displayGueesed(guessedLetters);

            // checking count variable
            if (count == 0) {
                /*
                 * if the count is 0
                 * then, random doesn't contain letter
                 */
                System.out.println("incorrect\n");
                /*
                 * decreasing life for incorrect guess
                 */
                life--;
            } else {
                System.out.println("correct Guess !\n");
            }

            // checking life variable
            if (life == 0) {
                /*
                 * if the life is 0
                 * then, it means game over
                 */
                sleep(1000); // wait for 1 second
                System.out.println("\t\tGAME OVER!"); // printing game over
                /*
                 * generating Hungman image
                 * based on life
                 */
                genHungmanImg(life);
                sleep(3000); // wait for 3 seconds
                System.out.println("secret word was " + "\"" + randWord + "\""); // revealing the secret word
                sleep(1000); // wait for 1 second
                guessedLetters = ""; // clearing/reset the guessedLetters string
                /*
                 * existing the Hungman Game process since the game is over
                 * and returning score and 0
                 */
                return new int[] { score, 0 };

            } else {
                /*
                 * otherwise, if life not over then,
                 * check the guessing word is equal to random word by comparing to randword
                 * if the two arrays are equal the the compare method return 0
                 */
                if (Arrays.compare(secretWord, randWord.toCharArray()) == 0) {
                    /*
                     * end time is recorded
                     */
                    endTime = Instant.now();
                    /*
                     * Score method generate score based on two instance of time
                     */
                    score = Score(startTime, endTime);
                    genHungmanImg(life); // generating hungman image
                    System.out.println("  " + new String(secretWord)); // printing the word
                    /*
                     * reset guessedLetters string
                     */
                    guessedLetters = "";
                    System.out.print("Congratulations you Won the game!\n");
                    sleep(1000); // wait 1 second
                    /*
                     * returing score and 1
                     */
                    return new int[] { score, 1 };

                }
            }
        }
        /*
         * returning score and 0
         */
        return new int[] { score, 0 };

    }

    private static void sleep(int duration) {
        /*
         * To wait for given duration
         * accept integer duration as parameter
         */
        try {
            Thread.sleep(duration);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private static int Score(Instant start, Instant end) {
        /*
         * It generate a score based on two time instance
         * time < 30sec - 200 points
         * time < 60sec - 150 points
         * time < 120sec - 100 points
         * time >120sec - 50 point
         */
        Duration time = Duration.between(start, end); // get extract the time in seconds
        if (time.getSeconds() < 120) { // less than 120 seconds
            if (time.getSeconds() < 60) { // less than 60 seconds
                if (time.getSeconds() < 30) { // less than 30 seconds
                    /*
                     * return 200 (less than 30 seconds)
                     */
                    return 200;
                } else {
                    /*
                     * return 150 (between 30 and 60 seconds)
                     */
                    return 150;
                }
            } else {
                /*
                 * return 100 (between 60 and 120 seconds)
                 */
                return 100;
            }
        } else {
            /*
             * return 50 (more than 120 seconds)
             */
            return 50;
        }
    }

    private static void genHungmanImg(int life) {
        /*
         * generate images depending on remaining life
         */

        if (life == 6) {
            firstImg();
        } else if (life == 5) {
            secondImg();
        } else if (life == 4) {
            thirdImg();
        } else if (life == 3) {
            forthImg();
        } else if (life == 2) {
            fifthImg();
        } else if (life == 1 || life == 0) {
            sixthImg();
        } else {
            initImg();
        }
    }

    private static long[] updateHighScore(long score, long[] arr) {
        /*
         * method to update high score
         * accept score and array to update high score
         * it returns a long array
         */

        arr[0] = score; // appending to the first position of array
        /*
         * to get highest of score
         * we sort the array of scores
         */
        Arrays.sort(arr);

        return arr;
    }

    private static void displayGueesed(String guees) {
        /*
         * method to display guessed letters
         * by changing the string to charcter array and
         * loop through each character
         */
        for (char alpha : guees.toCharArray()) {

            System.out.print(alpha + " "); // printing the charaters
        }
        System.out.println("");
    }

    private static void menu() {
        /*
         * method displaying the menu
         */
        System.out.println("\n=========================================");
        System.out.println("=             H U N G M A N             =");
        System.out.println("=                                       =");
        System.out.println("=   [1] Easy Mode                       =");
        System.out.println("=   [2] Hard Mode                       =");
        System.out.println("=   [3] High Score                      =");
        System.out.println("=   [4] Rules                           =");
        System.out.println("=   [5] Close                           =");
        System.out.println("=                                       =");
        System.out.println("=========================================");
    }

    private static void initImg() {
        /*
         * hungman image
         */
        System.out.println("\n--------");
        System.out.println("|       ");
        System.out.println("|       ");
        System.out.println("|       ");
        System.out.println("|       ");
    }

    private static void firstImg() {
        /*
         * hungman image
         */
        System.out.println("--------");
        System.out.println("|   O   ");
        System.out.println("|       ");
        System.out.println("|       ");
        System.out.println("|       ");
    }

    private static void secondImg() {
        /*
         * hungman image
         */
        System.out.println("--------");
        System.out.println("|   O   ");
        System.out.println("|  /    ");
        System.out.println("|       ");
        System.out.println("|       ");
    }

    private static void thirdImg() {
        /*
         * hungman image
         */
        System.out.println("--------");
        System.out.println("|   O   ");
        System.out.println("|  /|   ");
        System.out.println("|       ");
        System.out.println("|       ");
    }

    private static void forthImg() {
        /*
         * hungman image
         */
        System.out.println("--------");
        System.out.println("|   O   ");
        System.out.println("|  /|\\ ");
        System.out.println("|       ");
        System.out.println("|       ");
    }

    private static void fifthImg() {
        /*
         * hungman image
         */
        System.out.println("--------");
        System.out.println("|   O   ");
        System.out.println("|  /|\\ ");
        System.out.println("|  /    ");
        System.out.println("|       ");

    }

    private static void sixthImg() {
        /*
         * hungman image
         */
        System.out.println("\n--------");
        System.out.println("|   O     ");
        System.out.println("|  /|\\   ");
        System.out.println("|  / \\   ");
        System.out.println("|         ");
    }

    private static void displayHighScore(long[] highScore) {
        /*
         * method to display high score
         * accept long array of highScore
         */
        System.out.println("\n\t\t***************************");
        System.out.println("\t\t*   H I G H   S C O R E   *");
        System.out.println("\t\t***************************");
        /*
         * since the array is sorted in ascending order
         * the last value is always greater
         */
        System.out.println("\t\t\t> " + highScore[2]); // printing the high score
        sleep(2000);
    }

    private static void rules() {
        /*
         * method to display rules
         */
        System.out.println("\t\t##########################################################");
        System.out.println("\t\t#               H U N G M A N                            #");
        System.out.println("\t\t#                                                        #");
        System.out.println("\t\t#  GENERAL                                               #");
        sleep(1000);
        System.out.println("\t\t# > Need to fill the dash by guessing each letter        #");
        sleep(400);
        System.out.println("\t\t# > Player can have maximum 7 life or chances to guess   #");
        sleep(400);
        System.out.println("\t\t# > Player can use cheats or keywords while guessing     #");

        System.out.println("\t\t#                                                        #");
        System.out.println("\t\t# CHEATS (KEYWORDS)                                      #");
        sleep(1000);
        System.out.println("\t\t# exit : stop the game ask to continue for new game/word #");
        sleep(400);
        System.out.println("\t\t# restart : restart the game                             #");
        System.out.println("\t\t#                                                        #");
        System.out.println("\t\t# GAME MODE                                              #");
        sleep(1000);
        System.out.println("\t\t# - Easy mode (simple words generated by computer)       #");
        sleep(400);
        System.out.println("\t\t# - Hard Mode (Difficult words generated by computer)    #");
        sleep(400);
        System.out.println("\t\t#                                                        #");
        System.out.println("\t\t# SCORE                                                  #");
        sleep(1000);
        System.out.println("\t\t# - 200 : if player completly guess word within 30 sec   #");
        sleep(400);
        System.out.println("\t\t# - 150 : if player completly guess word within 1 min    #");
        sleep(400);
        System.out.println("\t\t# - 100 : if player completly guess word within 2 min    #");
        sleep(400);
        System.out.println("\t\t# - 50  : if player completly guess word morethan 2 min  #");
        System.out.println("\t\t#                                                        #");
        System.out.println("\t\t##########################################################");
        sleep(2000);
    }
}
