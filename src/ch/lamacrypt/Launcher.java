/*
 * Copyright (c) 2016, LamaCrypt
 * All rights reserved.
 *
 * The LamaCrypt client software and its source code are available
 * under the LamaCrypt Software License:
 * https://github.com/LamaCrypt/desktop-client/blob/master/LICENSE.md
 */
package ch.lamacrypt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Starts the client with the required JVM settings
 */
public class Launcher {

    public static void main(String[] args) throws IOException, InterruptedException {
        File config = new File("desktop-client.conf");
        int scryptFactor = 21;

        if (config.exists()) {
            Scanner in = new Scanner(new FileReader(config));
            while (in.hasNext()) {
                String tmpStr = in.next();
                if (tmpStr.startsWith("scryptfactor")) {
                    scryptFactor = Integer.parseInt(tmpStr.substring(13, 15));
                }
            }
            in.close();
        } else {
            config.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(config));
            bw.write("agreedTOS=no\n");
            bw.write("scryptfactor=" + scryptFactor + "\n");
            bw.close();
        }

        String xmx = "4g";
        switch (scryptFactor) {
            case 20:
                xmx = "3g";
                break;
            case 21:
                xmx = "4g";
                break;
            case 22:
                xmx = "5g";
                break;
        }

        String cmd = "java -Xmx" + xmx + " -XX:+UseParallelGC -XX:+AggressiveHeap -jar ./desktop-client.jar";
        Runtime.getRuntime().exec(cmd).waitFor();
    }
}
