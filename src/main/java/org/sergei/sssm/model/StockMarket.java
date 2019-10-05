package org.sergei.sssm.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.sergei.sssm.utils.NumberFormatter;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.10.01. initial version
 */
public class StockMarket {
	private List<AbstractStock> stocks;
	private List<StockMarketPlayer> players;
	private Map<StockSymbol, StockOrderBook> orderBooks;

	/**
	 * Constructor initializes all collections of this class.
	 */
	public StockMarket() {
		this.stocks = new ArrayList<>();
		this.players = new ArrayList<>();
		this.orderBooks = new EnumMap<>(StockSymbol.class);
	}

	/**
	 * Method adds to the registeredStockMarketCompanies list, Stock Market Company
	 * that uses Common Stocks.
	 * 
	 * @param stockSymbolCode
	 *            - String of stock symbol code.
	 * @param lastDividend
	 *            - BigDecimal of last dividend value.
	 * @param fixedDividend
	 *            - BigDecimal of fixed dividend value.
	 * @param parValue
	 *            - BigDecimal of par value.
	 * @param initialPrice
	 *            - BigDecimal of price value.
	 */
	public void registerStockMarketCompany(final String stockSymbolCode, final BigDecimal lastDividend, final BigDecimal parValue, final BigDecimal initialPrice) {
		final CommonStock commonStock = new CommonStock(stockSymbolCode, lastDividend, parValue, initialPrice);
		this.stocks.add(commonStock);
		initializeStockOrderBook(commonStock);
	}

	/**
	 * Method adds to the registeredStockMarketCompanies list, Stock Market Company
	 * that uses Preferred Stocks.
	 * 
	 * @param stockSymbolCode
	 *            - String of stock symbol code.
	 * @param lastDividend
	 *            - BigDecimal of last dividend value.
	 * @param fixedDividend
	 *            - BigDecimal of fixed dividend value.
	 * @param parValue
	 *            - BigDecimal of par value.
	 * @param initialPrice
	 *            - BigDecimal of price value.
	 */
	public void registerStockMarketCompany(final String stockSymbolCode, final BigDecimal lastDividend, final BigDecimal fixedDividend, final BigDecimal parValue, final BigDecimal initialPrice) {
		final PreferredStock preferredStock = new PreferredStock(stockSymbolCode, lastDividend, fixedDividend, parValue, initialPrice);
		this.stocks.add(preferredStock);
		initializeStockOrderBook(preferredStock);
	}

	/**
	 * Method initializes stock order book for a given stock market company.
	 * 
	 * @param commonStock
	 *            - AbstractStock.
	 */
	private void initializeStockOrderBook(final AbstractStock abstractStock) {
		this.orderBooks.put(abstractStock.getStockSymbol(), new StockOrderBook());
	}

	/**
	 * Method adds to the registeredStockMarketPlayers StockMarketPlayer.
	 * 
	 * @param stockMarketPlayerCode
	 *            - String of Stock Market Player code.
	 */
	public void registerStockMarketPlayer(String stockMarketPlayerCode) {
		this.players.add(StockMarketPlayer.valueOfByCode(stockMarketPlayerCode));
	}

	/**
	 * Method puts a buy order of a given stock symbol to the stockOrderBooks map.
	 * 
	 * @param stock
	 *            - AbstractStock of a stock buy order.
	 * @param quantity
	 *            - Integer of a stock buy order.
	 * @param offeredPrice
	 *            - BigDecimal offered price value of a stock buy order.
	 * @param stockMarketPlayer
	 *            - StockMarketPlayer who is issuing stock buy order.
	 * 
	 * @return executedStockOrderTransactions - Set<StockOrderTransaction>
	 *         containing a set of an executed trade transactions.
	 */
	public Set<StockOrderTransaction> putBuyOrder(final AbstractStock stock, final Integer quantity, final BigDecimal offeredPrice, final StockMarketPlayer stockMarketPlayer) {
		final StockOrderBook stockOrderBook = this.orderBooks.get(stock.getStockSymbol());
		return stockOrderBook.addBuyStockOrder(stock, quantity, offeredPrice, stockMarketPlayer);
	}

	/**
	 * Method puts a sell order of a given stock symbol to the stockOrderBooks map.
	 * 
	 * @param stockMarketCompany
	 *            - StockMarketCompany of a stock sell order.
	 * @param stock
	 *            - AbstractStock of a stock sell order.
	 * @param quantity
	 *            - Integer of a stock sell order.
	 * @param offeredPrice
	 *            - BigDecimal offered price value of a stock sell order.
	 * @param stockMarketPlayer
	 *            - StockMarketPlayer who is issuing stock sell order.
	 * 
	 * @return executedStockOrderTransactions - Set<StockOrderTransaction>
	 *         containing a set of an executed trade transactions.
	 */
	public Set<StockOrderTransaction> putSellOrder(final AbstractStock stock, final Integer quantity, final BigDecimal offeredPrice, final StockMarketPlayer stockMarketPlayer) {
		final StockOrderBook stockOrderBook = this.orderBooks.get(stock.getStockSymbol());
		return stockOrderBook.addSellStockOrder(stock, quantity, offeredPrice, stockMarketPlayer);
	}

	/**
	 * Method calculates All Share Index using formula: power of 1/N of product of
	 * all Volume Weighted Stock Prices that had trading activity (value different
	 * of zero).
	 * 
	 * @return BigDecimal of All Share Index.
	 */
	public BigDecimal calculateAllShareIndex() {
		BigDecimal volumeWeightedStockPricesProduct = BigDecimal.ONE;
		int volumeWeightedStocksPricesCount = 0;

		for (Entry<StockSymbol, StockOrderBook> stockOrderBookEntry : this.orderBooks.entrySet()) {
			final StockOrderBook stockOrderBook = stockOrderBookEntry.getValue();
			final BigDecimal volumeWeightedStockPrice = stockOrderBook.calculateVolumeWeightedStockPrice();

			if (BigDecimal.ZERO.compareTo(volumeWeightedStockPrice) != 0) {
				volumeWeightedStockPricesProduct = volumeWeightedStockPricesProduct.multiply(volumeWeightedStockPrice);
				volumeWeightedStocksPricesCount++;
			}
		}

		final BigDecimal power = BigDecimal.ONE.divide(BigDecimal.valueOf(volumeWeightedStocksPricesCount), NumberFormatter.SCALE_4_DECIMAL_DIGITS, RoundingMode.HALF_EVEN);

		return BigDecimal.valueOf(Math.pow(volumeWeightedStockPricesProduct.doubleValue(), power.doubleValue())).setScale(NumberFormatter.SCALE_4_DECIMAL_DIGITS, RoundingMode.HALF_EVEN);
	}

	/**
	 * @return the stocks
	 */
	public List<AbstractStock> getStocks() {
		return stocks;
	}

	/**
	 * @return the players
	 */
	public List<StockMarketPlayer> getPlayers() {
		return players;
	}

	/**
	 * @return the orderBooks
	 */
	public Map<StockSymbol, StockOrderBook> getOrderBooks() {
		return orderBooks;
	}
}