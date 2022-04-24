/*
    Prime Numbers: Writes in a file prime numbers between 0 and a user-defined max value.
    Copyright (C) 2022  Francesco Marras

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses/gpl-3.0.html.
*/

package NumeriPrimi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author Francesco Marras
 */
public class PrimeNumbers {
    public byte run(String[] args) {
        byte endStatus;

        // if 2 args are given, tries to run with these
        if (args.length == 2) {
            System.out.println("Running on given args...");
            if (isNum(args[0])) {
                long max = Long.parseLong(args[0]);
                if (max < 0) {
                    System.out.println("\"" + args[0] + "\" is negative.");
                    endStatus = 1;
                } else {
                    if (isValidFileName(args[1])) {
                        endStatus = find(max, args[1]);
                    } else {
                        System.err.println("\"" + args[1] + "\" is not a valid path.");
                        endStatus = 1;
                    }
                }
            } else {
                System.err.println("\"" + args[0] + "\" is not a number / is too large.");
                endStatus = 1;
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            String in;

            System.out.println("Max value: ");
            in = scanner.nextLine();
            if (isNum(in)) {
                long max = Long.parseLong(in);

                if (max < 0) {
                    System.out.println("\"" + in + "\" is negative.");
                    endStatus = 1;
                } else {
                    System.out.println("File name: ");
                    in = scanner.nextLine();

                    if (isValidFileName(in)) {
                        endStatus = find(max, in);
                    } else {
                        System.err.println("\"" + in + "\" is not a valid path.");
                        endStatus = 1;
                    }
                }
            } else {
                System.err.println("\"" + in + "\" is not a number / is too large.");
                endStatus = 1;
            }

            scanner.close();
        }
        System.out.println("Program exited with status " + endStatus + ".");

        return endStatus;
    }

    public byte find(long max, String filename) {
        LinkedList<Long> list = new LinkedList<>();
        list.add((long) 2);
        list.add((long) 3);

        for (long num = 5; num < max; num += 2) {
            if (isPrime(num)) {
                list.add(num);
            }
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (long num : list) {
                pw.println(num);
            }
            pw.flush();
            pw.close();

            System.out.println("Execution complted (using 6k +- 1 test).");
            return 0;
        } catch (IOException e) {
            System.out.println("Error writing file: ");
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * Checks if {@code num} is prime.
     * 
     * @param num The number to check.
     * @return The result of the ckeck.
     */
    private static boolean isPrime(long num) {
        // if num is divisible by 2 or 3 is not prime
        if (num % 2 == 0 || num % 3 == 0) {
            return false;
        }

        // 6k +- 1 test
        for (long i = 5; Math.pow(i, 2) <= num; i += 6) {
            if (num % i == 0 || num % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Cheks id given {@code String} is a number.
     * 
     * @param str {@code String} to check.
     * @return Result of the check.
     */
    private boolean isNum(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if {@code FILE_NAME} is a valid filename
     * 
     * @param FILE_NAME Filename to check.
     * @return Result of the check.
     */
    public boolean isValidFileName(final String FILE_NAME) {
        final File file = new File(FILE_NAME);
        boolean isValid = true;
        try {
            if (file.createNewFile()) {
                file.delete();
            }
        } catch (IOException e) {
            isValid = false;
        }
        return isValid;
    }

    public static void main(String[] args) {
        System.out.println("PrimeNumbers  Copyright (C) 2022  Francesco Marras");
        System.out.println(
                "This program comes with ABSOLUTELY NO WARRANTY; for details see https://www.gnu.org/licenses/gpl-3.0.html.");
        System.out.println(
                "This is free software, and you are welcome to redistribute it under certain conditions; see https://www.gnu.org/licenses/gpl-3.0.html for details.\n");

        new PrimeNumbers().run(args);
    }
}