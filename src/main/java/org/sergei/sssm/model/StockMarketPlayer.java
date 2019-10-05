package org.sergei.sssm.model;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.10.01. initial version
 */
public enum StockMarketPlayer {
	PLAYER1("Player  1"), PLAYER2("Player  2"), PLAYER3("Player  3"), PLAYER4("Player  4"), PLAYER5("Player  5"), PLAYER6("Player  6"), PLAYER7("Player  7"), PLAYER8("Player  8"), PLAYER9(
			"Player  9"), PLAYER10("Player 10");

	private String code;

	/**
	 * Constructor initializes code attribute.
	 * 
	 * @param description
	 *            - String stock symbol code value.
	 */
	private StockMarketPlayer(final String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Method searches for a StockMarketPlayer constant by code parameter.
	 * 
	 * @param stockMarketPlayerCode
	 *            - String of stock market player code.
	 * 
	 * @return StockMarketPlayer - found constant or null.
	 */
	public static StockMarketPlayer valueOfByCode(String stockMarketPlayerCode) {
		for (StockMarketPlayer stockMarketPlayer : StockMarketPlayer.values()) {
			if (stockMarketPlayer.getCode().equals(stockMarketPlayerCode)) {
				return stockMarketPlayer;
			}
		}
		return null;
	}
}
