package org.sergei.sssm.thread;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.sergei.sssm.model.AbstractStock;
import org.sergei.sssm.model.StockBuyOrder;
import org.sergei.sssm.model.StockMarket;
import org.sergei.sssm.model.StockMarketPlayer;
import org.sergei.sssm.model.StockOrderTransaction;
import org.sergei.sssm.utils.NumberFormatter;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.10.01. initial version
 */
public class Player implements Runnable {
	/**
	 * Trading playing time limit is set to 5 minutes.
	 */
	private static final int PLAYING_TIME_LIMIT = 5 * 60 * 1000;
	/**
	 * Buy stock price max percentage, used to set maximum and minimum buy stock
	 * price.
	 */
	private static final BigDecimal BUY_STOCK_PRICE_MAX_PERCENTAGE = new BigDecimal("0.1");
	/**
	 * Sell stock price multiplier, used to set maximim and minimum sell stock
	 * price.
	 */
	private static final BigDecimal SELL_STOCK_PRICE_MULTIPLIER = new BigDecimal("2");

	private StockMarketPlayer stockMarketPlayer;
	private StockMarket stockMarket;
	private CountDownLatch countDownLatch;
	private Random random;

	/**
	 * Constructor initializes classes attributes.
	 * 
	 * @param stockMarketPlayer
	 *            - StockMarketPlayer.
	 * @param stockMarket
	 *            - StockMarket.
	 * @param countDownLatch
	 *            - CountDownLatch.
	 */
	public Player(final StockMarketPlayer stockMarketPlayer, final StockMarket stockMarket, final CountDownLatch countDownLatch) {
		this.stockMarketPlayer = stockMarketPlayer;
		this.stockMarket = stockMarket;
		this.countDownLatch = countDownLatch;
		this.random = new Random();
	}

	/**
	 * Method waits for synchronized threads start and then runs trade operations.
	 */
	@Override
	public void run() {
		waitForSynchronizedStart();
		trade();
	}

	/**
	 * Method calls countDownLatch count down method to synchronize player's start.
	 */
	private void waitForSynchronizedStart() {
		countDownLatch.countDown();
	}

	/**
	 * Method runs trade operations for a given time, each operation uses random
	 * stock, quantity, buy price, sell price, operation buy/sell. After each
	 * successful placed order, which found its counterpart match, prints a result
	 * of the buy/sell operations.
	 * 
	 * If a quantity of an placed order is superior of a counterpart order, many
	 * transactions can be executed to completely fulfill a placed order.
	 */
	private void trade() {
		final long startTime = System.currentTimeMillis();

		while (System.currentTimeMillis() - startTime <= PLAYING_TIME_LIMIT) {
			final AbstractStock randomStock = getRandomStock();
			final Integer randomQuantity = getRandomQuantity();
			final BigDecimal randomBuyPrice = getRandomBuyPrice(randomStock);
			final BigDecimal randomSellPrice = getRandomSellPrice(randomStock);
			final Boolean buyRandomOperation = isBuyRandomOperation();
			Set<StockOrderTransaction> executedTransactions = null;

			synchronized (this.stockMarket) {
				if (buyRandomOperation) {
					executedTransactions = this.stockMarket.putBuyOrder(randomStock, randomQuantity, randomBuyPrice, stockMarketPlayer);
				} else {
					executedTransactions = this.stockMarket.putSellOrder(randomStock, randomQuantity, randomSellPrice, stockMarketPlayer);
				}
			}

			if (executedTransactions != null) {
				for (StockOrderTransaction stockOrderTransaction : executedTransactions) {
					// For simplicity of simulation, no logging libraries are used
					System.out.println(String.format("%1$s\t%2$s", randomStock.getStockSymbol().getCode(), stockOrderTransaction));
				}
			}
		}
	}

	/**
	 * Method returns random stock.
	 * 
	 * @return AbstractStock - random stock.
	 */
	private AbstractStock getRandomStock() {
		final List<AbstractStock> stocks = this.stockMarket.getStocks();
		final int randomStockIndex = random.nextInt(stocks.size());
		return stocks.get(randomStockIndex);
	}

	/**
	 * Method returns random buy/sell operation.
	 * 
	 * @return Boolean true if buy operation is generated.
	 */
	private Boolean isBuyRandomOperation() {
		return random.nextBoolean();
	}

	/**
	 * Method generates random quantity, if a zero quantity is returned, the
	 * operation is repeated for 10 times to generate non zero value.
	 * 
	 * In a real world, this issue should be validated at the input time.
	 * 
	 * @return
	 */
	private Integer getRandomQuantity() {
		int randomQuantity = 0;
		for (int i = 0; i < 10 && randomQuantity == 0; i++) {
			randomQuantity = random.nextInt(StockBuyOrder.MAX_QUANTITY_LIMIT);
		}
		return randomQuantity;
	}

	/**
	 * Method generates maximum and minimum price trend to be used in random price
	 * generation method.
	 * 
	 * @param stock
	 *            - AbstractStock used to get stock current price.
	 * 
	 * @return BigDecimal - random generated buy price which is in max/min trend.
	 */
	private BigDecimal getRandomBuyPrice(final AbstractStock stock) {
		final BigDecimal minPrice = stock.getPrice().multiply(BUY_STOCK_PRICE_MAX_PERCENTAGE);
		final BigDecimal maxPrice = stock.getPrice().add(minPrice);
		return getRandomPrice(minPrice, maxPrice);
	}

	/**
	 * Method generates maximum and minimum price trend to be used in random price
	 * generation method.
	 * 
	 * @param stock
	 *            - AbstractStock used to get stock current price.
	 * 
	 * @return BigDecimal - random generated sell price which is in max/min trend.
	 */
	private BigDecimal getRandomSellPrice(AbstractStock stock) {
		final BigDecimal minPrice = stock.getPrice().subtract(stock.getPrice().multiply(BUY_STOCK_PRICE_MAX_PERCENTAGE));
		final BigDecimal maxPrice = stock.getPrice().multiply(SELL_STOCK_PRICE_MULTIPLIER);
		return getRandomPrice(minPrice, maxPrice);
	}

	/**
	 * Method generates random price inside of maximum and minimum price trend, if a
	 * zero price is returned, the operation is repeated for 10 times to generate
	 * non zero value.
	 * 
	 * In a real world, this issue should be validated at the input time.
	 * 
	 * @param minPrice
	 *            - BigDecimal of minimum price to be generated.
	 * @param maxPrice
	 *            - BigDecimal of maximum price to be generated.
	 * 
	 * @return BigDecimal - random generated buy price which is in max/min trend.
	 */
	private BigDecimal getRandomPrice(final BigDecimal minPrice, final BigDecimal maxPrice) {
		BigDecimal price = BigDecimal.ZERO;
		for (int i = 0; i < 10 && price.doubleValue() == 0; i++) {
			price = maxPrice.subtract(minPrice).multiply(BigDecimal.valueOf(random.nextDouble())).add(minPrice);
		}
		return price.setScale(NumberFormatter.SCALE_4_DECIMAL_DIGITS, RoundingMode.HALF_EVEN);
	}
}