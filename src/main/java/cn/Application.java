package cn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * Created by Mantas Sinkevicius on 2018-01-18.
 */
public class Application {
	private static final Logger logger = LogManager.getLogger(Application.class);
	private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public static void main(String... args) throws IOException {
		PropertyManager.loadProperties();

		Runnable task = () -> {
			run();
		};
		executor.scheduleAtFixedRate(task, 0, 1, MINUTES);
	}

	private static void run() {
		List<Coin> fallenCoins = CoinmarketcapService.checkCoinPerformance();
		if (fallenCoins.isEmpty()) { // everything is good
			logger.info("To the moon!");
			return;
		}

		try {
			logger.info("Panic! Buy high, sell low! " + fallenCoins);
			MailSenderService.send(fallenCoins);
		} catch (IOException e) {
			logger.fatal(e);
		}
	}

}
