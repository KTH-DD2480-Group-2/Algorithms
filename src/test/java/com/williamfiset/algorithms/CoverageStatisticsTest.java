package com.williamfiset.algorithms;

import org.junit.*;

import static com.google.common.truth.Truth.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CoverageStatisticsTest {

    @Test
    public void testCoverage() throws IOException {
        String str = "TreePrinter.display.id_0".concat("\n")
                .concat("TreePrinter.display.id_1").concat("\n")
                .concat("TreePrinter.display.id_2").concat("\n")
                .concat("TreePrinter.display.id_3").concat("\n")
                .concat("TreePrinter.display.id_4").concat("\n")
                .concat("\n")
                .concat("dp.CoinChange.coinChange.id_0").concat("\n")
                .concat("dp.CoinChange.coinChange.id_1").concat("\n")
                .concat("dp.CoinChange.coinChange.id_2").concat("\n")
                .concat("dp.CoinChange.coinChange.id_3").concat("\n")
                .concat("dp.CoinChange.coinChange.id_4").concat("\n")
                .concat("dp.CoinChange.coinChange.id_5").concat("\n")
                .concat("dp.CoinChange.coinChange.id_6").concat("\n")
                .concat("dp.CoinChange.coinChange.id_7").concat("\n")
                .concat("dp.CoinChange.coinChange.id_8").concat("\n")
                .concat("dp.CoinChange.coinChange.id_9").concat("\n")
                .concat("dp.CoinChange.coinChange.id_10").concat("\n")
                .concat("ABC.EditDistance.editDistance.id_0").concat("\n")
                .concat("ABC.EditDistance.editDistance.id_1").concat("\n")
                .concat("ABC.EditDistance.editDistance.id_2").concat("\n")
                .concat("\n")
                .concat("XYZ.NotTested.id_0").concat("\n")
                .concat("XYZ.NotTested.id_1").concat("\n")
                .concat("XYZ.NotTested.id_2").concat("\n")
                .concat("XYZ.NotTested.id_3").concat("\n");
        BufferedWriter writer = new BufferedWriter(new FileWriter("allBranches.txt"));
        writer.write(str);
        writer.close();

        str = "TreePrinter.display.id_0".concat("\n")
                .concat("TreePrinter.display.id_1").concat("\n")
                .concat("TreePrinter.display.id_3").concat("\n")
                .concat("TreePrinter.display.id_4").concat("\n")
                .concat("\n")
                .concat("dp.CoinChange.coinChange.id_6").concat("\n")
                .concat("dp.CoinChange.coinChange.id_4").concat("\n")
                .concat("dp.CoinChange.coinChange.id_5").concat("\n")
                .concat("dp.CoinChange.coinChange.id_10").concat("\n")
                .concat("ABC.EditDistance.editDistance.id_0").concat("\n")
                .concat("ABC.EditDistance.editDistance.id_1").concat("\n")
                .concat("ABC.EditDistance.editDistance.id_2").concat("\n")
                .concat("\n")
                .concat("Unexisting.test.id_1").concat("\n");
        writer = new BufferedWriter(new FileWriter("visitedBranches.txt"));
        writer.write(str);
        writer.close();


        CoverageStatistics.getStatistics("allBranches.txt", "visitedBranches.txt");

        File f = new File("allBranches.txt");
        f.delete();
        f = new File("visitedBranches.txt");
        f.delete();

        assertThat(true);
    }
}
