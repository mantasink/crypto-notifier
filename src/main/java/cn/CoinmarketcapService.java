package cn;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mantas Sinkevicius on 2018-01-18.
 */
public class CoinmarketcapService {
	private static final Logger logger = LogManager.getLogger(CoinmarketcapService.class);
	private static final String API_URL = "https://api.coinmarketcap.com/v1/ticker/";
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

	/**
	 * @return list of coins if not enough meeting threshold, else empty list
	 */
	public static List<Coin> checkCoinPerformance() {
		List<Coin> alert1hCoins = new ArrayList<>();
		List<Coin> alert24hCoins = new ArrayList<>();
		boolean bitcoinThresholdNotMet = false;

		int i = 0;
		for (Coin coin : getCoinsFromApi()) {
			boolean threshold1hNotMet = coin.getPercentChange1h().compareTo(PropertyManager.THRESHOLD_PERCENT_1_H) < 1;
			boolean threshold24NotMet = coin.getPercentChange24h().compareTo(PropertyManager.THRESHOLD_PERCENT_24_H) < 1;

			if ("BTC".equals(coin.getSymbol()) && (threshold1hNotMet || threshold24NotMet)) {
				bitcoinThresholdNotMet = true;
			}
			if (threshold1hNotMet) {
				alert1hCoins.add(coin);
			}
			if (threshold24NotMet) {
				alert24hCoins.add(coin);
			}

			if (i++ >= PropertyManager.CHECK_COIN_AMOUNT) {
				// stop when coin limit is met
				break;
			}
		}

		// return coins if Bitcoin has fallen and THRESHOLD_COIN_AMOUNT alts of CHECK_COIN_AMOUNT has fallen too
		if (bitcoinThresholdNotMet && alert1hCoins.size() >= PropertyManager.THRESHOLD_COIN_AMOUNT) {
			return alert1hCoins;
		}
		if (bitcoinThresholdNotMet && alert24hCoins.size() >= PropertyManager.THRESHOLD_COIN_AMOUNT) {
			return alert24hCoins;
		}

		return new ArrayList<>();
	}

	private static List<Coin> getCoinsFromApi() {
		try {
			URL jsonUrl = new URL(API_URL);
			List<Coin> coins = OBJECT_MAPPER.readValue(jsonUrl, new TypeReference<List<Coin>>() {
			});
			return coins;
		} catch (IOException e) {
			logger.error(e);
		}

		return new ArrayList<>();
	}
}
