package cn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Mantas Sinkevicius on 2018-01-19.
 * <p>
 * Loads and holds configuration properties.
 */
public class PropertyManager {
    private static final Logger logger = LogManager.getLogger(PropertyManager.class);

    public static String API_KEY = "";
    public static String EMAIL_FROM = "";
    public static List<String> EMAILS_BCC = new ArrayList<>();

    public static BigDecimal THRESHOLD_PERCENT_1_H = BigDecimal.valueOf(-4);
    public static int THRESHOLD_COIN_AMOUNT = 15;
    public static int CHECK_COIN_AMOUNT = 20;

    public static void loadProperties() {
        String configPropertiesFile = System.getProperty("configFile");
        logger.info("Loading file " + configPropertiesFile);

        File file = new File(configPropertiesFile);
        try (InputStream input = new FileInputStream(file)) {
            if (input == null) {
                logger.fatal("File not found " + configPropertiesFile);
                System.exit(0);
            }

            Properties prop = new Properties();
            prop.load(input);

            API_KEY = prop.getProperty("send.grid.api.key");
            EMAIL_FROM = prop.getProperty("email.from");
            List<String> ccList = Arrays.asList(prop.getProperty("emails.cc").split(","));
            EMAILS_BCC.addAll(ccList);

            THRESHOLD_PERCENT_1_H = BigDecimal.valueOf(Double.valueOf(prop.getProperty("threshold.1h")));
            THRESHOLD_COIN_AMOUNT = Integer.valueOf(prop.getProperty("threshold.coin.amount"));
            CHECK_COIN_AMOUNT = Integer.valueOf(prop.getProperty("threshold.check.coin.amount"));

            String bcc = String.join(", ", ccList);
            logger.info("Properties loaded. " + String.join(", ", THRESHOLD_PERCENT_1_H.toEngineeringString(), bcc));
        } catch (IOException ex) {
            logger.fatal(ex);
            System.exit(0);
        }
    }
}
