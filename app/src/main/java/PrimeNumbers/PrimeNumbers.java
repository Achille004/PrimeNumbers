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

package PrimeNumbers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Francesco Marras
 */
public class PrimeNumbers {
    private static final int THREADS = 16;
    private List<Long> primeList;

    public PrimeNumbers() {
        primeList = new LinkedList<>();
    }

    public byte run(String... args) {
        String maxString, fileNameString;
        // if 2 args are given, tries to run with these
        if (args.length == 2) {
            System.out.println("Running on given args...");
            maxString = args[0];
            fileNameString = args[1];
        } else {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Max value: ");
            maxString = scanner.nextLine();

            System.out.println("File name: ");
            fileNameString = scanner.nextLine();

            scanner.close();
        }

        byte endStatus;
        if (isNum(maxString)) {
            long max = Long.parseLong(maxString);

            if (max < 0) {
                System.out.println("\"" + maxString + "\" is negative.");
                endStatus = 1;
            } else {
                if (isValidFileName(fileNameString)) {
                    find(max, THREADS);
                    endStatus = save(fileNameString);
                } else {
                    System.err.println("\"" + fileNameString + "\" is not a valid path.");
                    endStatus = 1;
                }
            }
        } else {
            System.err.println("\"" + maxString + "\" is not a number / is too large.");
            endStatus = 1;
        }
        System.out.println("Program exited with status " + endStatus + ".");

        return endStatus;
    }

    public void find(long max, int numThreads) {
        primeList.add(2L);
        primeList.add(3L);
    
        List<Thread> threads = new ArrayList<>(numThreads);
    
        long step = (max - 5) / numThreads;
        long start = 5;
    
        for (int i = 0; i < numThreads; i++) {
            long end = start + step;
            if(end % 2 == 0) {
                end--;
            }

            final long threadStart = start;
            final long threadEnd = (i == numThreads - 1) ? max : end;

            Thread thread = new Thread(() -> {
                for (long num = threadStart; num <= threadEnd; num += 2) {

                    if (isPrime(num)) {
                        synchronized (primeList) {
                            primeList.add(num);
                        }
                    }
                }
            });
    
            threads.add(thread);
            thread.start();
    
            start = end + 2;
        }
    
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    

    private byte save(String filename) {
        Long[] primeArray = primeList.toArray(new Long[primeList.size()]);
        Arrays.sort(primeArray);

        System.out.println("Prova: " + primeArray.length);

        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (long num : primeArray) {
                pw.println(num);
            }
            pw.flush();
            pw.close();

            System.out.println("Execution completed (using 6k +- 1 test).");
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
        if (num % 3 == 0) {
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