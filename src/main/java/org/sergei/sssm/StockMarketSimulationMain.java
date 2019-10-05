package org.sergei.sssm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.sergei.sssm.model.AbstractStock;
import org.sergei.sssm.model.StockMarket;
import org.sergei.sssm.model.StockMarketPlayer;
import org.sergei.sssm.model.StockOrderBook;
import org.sergei.sssm.model.StockSymbol;
import org.sergei.sssm.thread.Player;
import org.sergei.sssm.utils.NumberFormatter;
import org.sergei.sssm.utils.TimeFormatter;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.09.30. initial version
 */
public class StockMarketSimulationMain {
	/**
	 * Constant indicates maximum price limit for initial price value of the stocks.
	 */
	private static final int MAX_INITIAL_PRICE_LIMIT = 100;

	/**
	 * Main model class, for simplicity of this example, it is a part of the main
	 * class.
	 */
	private StockMarket stockMarket;

	/**
	 * Default constructor initializes stockMarket attribute.
	 */
	public StockMarketSimulationMain() {
		this.stockMarket = new StockMarket();
	}

	/**
	 * Main method initializes class attributes and init's the stock market exchange
	 * process.
	 * 
	 * @param args
	 *            - String[] of arguments: is not used.
	 */
	public static void main(final String[] args) {
		final StockMarketSimulationMain superSimpleStockMarketMain = new StockMarketSimulationMain();

		superSimpleStockMarketMain.initializeStocks();
		superSimpleStockMarketMain.initializeStockMarketPlayers();
		superSimpleStockMarketMain.printDividendYieldAndPERatio();
		superSimpleStockMarketMain.trade();
		superSimpleStockMarketMain.printVolumeWeightedStockPrice();
		superSimpleStockMarketMain.printAllShareIndex();
	}

	/**
	 * Method initializes class attributes that holds main model object, for
	 * simplicity no Service/DAO Pattern is implemented in this example.
	 * 
	 * <b> The values of Last Dividend, Par Value are divided by 100 because in a
	 * requirement commented: "All number values in pennies".
	 * 
	 * Fixed Dividend is divided by 100 because it is a percentage value. <b>
	 */
	private void initializeStocks() {
		this.stockMarket.registerStockMarketCompany("TEA", new BigDecimal("0.00"), new BigDecimal("1.00"), getRandomInitialPrice());
		this.stockMarket.registerStockMarketCompany("POP", new BigDecimal("0.08"), new BigDecimal("1.00"), getRandomInitialPrice());
		this.stockMarket.registerStockMarketCompany("ALE", new BigDecimal("0.23"), new BigDecimal("0.60"), getRandomInitialPrice());
		this.stockMarket.registerStockMarketCompany("GIN", new BigDecimal("0.08"), new BigDecimal("0.02"), new BigDecimal("1.00"), getRandomInitialPrice());
		this.stockMarket.registerStockMarketCompany("JOE", new BigDecimal("0.13"), new BigDecimal("2.50"), getRandomInitialPrice());
	}

	/**
	 * Method generates random initial price but using MAX_INITIAL_PRICE_LIMIT
	 * constant.
	 * 
	 * @return BigDecimal of a initial price.
	 */
	private BigDecimal getRandomInitialPrice() {
		final BigDecimal randomStockPrice = BigDecimal.valueOf(new Random().nextInt(MAX_INITIAL_PRICE_LIMIT) + new Random().nextDouble());
		return randomStockPrice.setScale(NumberFormatter.SCALE_4_DECIMAL_DIGITS, RoundingMode.HALF_EVEN);
	}

	/**
	 * Method calls for each StockMarketCompany to calculate Dividend Yield, P/E
	 * Ratio and prints the whole company information and calculated Dividend Yield,
	 * P/E Ratio values.
	 */
	private void printDividendYieldAndPERatio() {
		for (AbstractStock abstractStock : this.stockMarket.getStocks()) {
			System.out.print(abstractStock.toString());
			System.out.print(String.format("\t%1$s %2$s", "Dividend Yield:", abstractStock.calculateDividendYieldFormatted()));
			System.out.println(String.format("\t%1$s %2$s", "P/E Ratio:", abstractStock.calculatePERatioFormatted()));
		}
	}

	/**
	 * Method initializes Stock Market Player codes that will be used as name of the
	 * Buying/Selling Player Threads, this simulates real world's trader
	 * registration operation.
	 */
	private void initializeStockMarketPlayers() {
		this.stockMarket.registerStockMarketPlayer(StockMarketPlayer.PLAYER1.getCode());
		this.stockMarket.registerStockMarketPlayer(StockMarketPlayer.PLAYER2.getCode());
		this.stockMarket.registerStockMarketPlayer(StockMarketPlayer.PLAYER3.getCode());
		this.stockMarket.registerStockMarketPlayer(StockMarketPlayer.PLAYER4.getCode());
		this.stockMarket.registerStockMarketPlayer(StockMarketPlayer.PLAYER5.getCode());
		this.stockMarket.registerStockMarketPlayer(StockMarketPlayer.PLAYER6.getCode());
		this.stockMarket.registerStockMarketPlayer(StockMarketPlayer.PLAYER7.getCode());
		this.stockMarket.registerStockMarketPlayer(StockMarketPlayer.PLAYER8.getCode());
		this.stockMarket.registerStockMarketPlayer(StockMarketPlayer.PLAYER9.getCode());
		this.stockMarket.registerStockMarketPlayer(StockMarketPlayer.PLAYER10.getCode());
	}

	/**
	 * Simulation of a trade, all threads are launched in synchronized way, using
	 * countDownLatch, and then all threads are joined this main thread, to complete
	 * calculations over the traded transactions.
	 */
	private void trade() {
		final List<StockMarketPlayer> registeredStockMarketPlayers = this.stockMarket.getPlayers();
		final CountDownLatch countDownLatch = new CountDownLatch(registeredStockMarketPlayers.size());
		final List<Thread> players = new ArrayList<>();

		for (StockMarketPlayer stockMarketPlayer : registeredStockMarketPlayers) {
			final Thread player = new Thread(new Player(stockMarketPlayer, this.stockMarket, countDownLatch), stockMarketPlayer.getCode());
			players.add(player);
			player.start();
		}

		startStockTrading(countDownLatch);
		joinPlayerThreads(players);
	}

	/**
	 * Method synchronizes and starts the threads execution.
	 * 
	 * Also method prints start of the execution time.
	 * 
	 * @param countDownLatch
	 *            - CountDownLatch.
	 */
	private void startStockTrading(final CountDownLatch countDownLatch) {
		try {
			countDownLatch.await();
			System.out.println(String.format("Start trading time: %s", TimeFormatter.format(System.currentTimeMillis())));
		} catch (InterruptedException e) {
			System.out.println("Error in synchronous thread starting.");
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Method joins all players threads to the main thread, to complete afterwards
	 * the calculations over the trade transactions.
	 * 
	 * Also method prints stop of the execution time.
	 * 
	 * @param players
	 *            - List<Thread> of trading threads.
	 */
	private void joinPlayerThreads(final List<Thread> players) {
		for (Thread thread : players) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				System.out.println("Error in synchronous thread starting.");
				Thread.currentThread().interrupt();
			}
		}
		System.out.println(String.format("End trading time: %s", TimeFormatter.format(System.currentTimeMillis())));
	}

	/**
	 * Method calculates and prints per each Stock a Volume Weighted Stock Price.
	 */
	private void printVolumeWeightedStockPrice() {
		final Map<StockSymbol, StockOrderBook> stockOrderBooks = this.stockMarket.getOrderBooks();
		for (Entry<StockSymbol, StockOrderBook> stockSymbolEntry : stockOrderBooks.entrySet()) {
			final StockSymbol stockSymbol = stockSymbolEntry.getKey();
			final StockOrderBook stockOrderBook = stockOrderBooks.get(stockSymbol);
			System.out.println(String.format("%1$s \tVolume Weighted Stock Price: %2$s", stockSymbol.getCode(), NumberFormatter.format(stockOrderBook.calculateVolumeWeightedStockPrice())));
		}
	}

	/**
	 * Method calculates and prints All Share Index of all stocks and transactions
	 * that were executed in trading operation.
	 */
	private void printAllShareIndex() {
		System.out.println(String.format("GBCE All Share Index: %s", NumberFormatter.format(this.stockMarket.calculateAllShareIndex())));
	}
}