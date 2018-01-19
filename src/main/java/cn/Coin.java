package cn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by Mantas Sinkevicius on 2018-01-18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coin {

	private String name;
	private String symbol;
	private Integer rank;

	@JsonProperty("price_usd")
	private BigDecimal priceUsd;
	@JsonProperty("percent_change_1h")
	private BigDecimal percentChange1h;
	@JsonProperty("percent_change_24h")
	private BigDecimal percentChange24h;
	@JsonProperty("percent_change_7d")
	private BigDecimal percentChange7d;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public BigDecimal getPriceUsd() {
		return priceUsd;
	}

	public void setPriceUsd(BigDecimal priceUsd) {
		this.priceUsd = priceUsd;
	}

	public BigDecimal getPercentChange1h() {
		return percentChange1h;
	}

	public void setPercentChange1h(BigDecimal percentChange1h) {
		this.percentChange1h = percentChange1h;
	}

	public BigDecimal getPercentChange24h() {
		return percentChange24h;
	}

	public void setPercentChange24h(BigDecimal percentChange24h) {
		this.percentChange24h = percentChange24h;
	}

	public BigDecimal getPercentChange7d() {
		return percentChange7d;
	}

	public void setPercentChange7d(BigDecimal percentChange7d) {
		this.percentChange7d = percentChange7d;
	}

	@Override
	public String toString() {
		return symbol + " (" + priceUsd + "$), 1h " + percentChange1h + "%, 24h " + percentChange24h + "%";
	}
}
