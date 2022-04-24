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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PrimeNumbersTest {
    @Test
    void ok() {
        PrimeNumbers classUnderTest = new PrimeNumbers();
        String[] args = { "1000", "primes.txt" };

        assertEquals(0, classUnderTest.run(args));
    }

    @Test
    void tooLarge() {
        PrimeNumbers classUnderTest = new PrimeNumbers();
        String[] args = { "100000000000000000000000000000000000000000000000000000000000", "primes.txt" };

        assertEquals(1, classUnderTest.run(args));
    }

    @Test
    void invalidFilepath() {
        PrimeNumbers classUnderTest = new PrimeNumbers();
        String[] args = { "1000", "primes.?" };

        assertEquals(1, classUnderTest.run(args));
    }
}
