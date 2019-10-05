package org.sergei.sssm.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.sergei.sssm.utils.NumberFormatter;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.10.02. initial version
 */
public class StockOrderBook {
	/**
	 * Placed buy stock orders set.
	 */
	private Set<StockBuyOrder> buyStockOrders;
	/**
	 * Placed sell stock orders set.
	 */
	private Set<StockSellOrder> sellStockOrders;
	/**
	 * Executed trade buy+sell stock orders transaction.
	 */
	private Set<StockOrderTransaction> stockOrderTransactions;
	/**
	 * Total sum of price multiplied by quantity of executed exchange transactions.
	 */
	private BigDecimal totalPriceQuantitySum;
	/**
	 * Total sum of stocks quantity of executed exchange transactions.
	 */
	private BigInteger totalQuantity;

	/**
	 * Constructor initializes class attributes using TreeSet class, this class was
	 * used because it fits better and has better performance on current
	 * solution.<br>
	 * 
	 * TreeSet has a log(n) time complexity guarantee for add()/remove()/contains()
	 * methods.<br>
	 * Sorting an ArrayList takes n*log(n) operations, but add()/get() takes only 1
	 * operation.
	 */
	public StockOrderBook() {
		this.buyStockOrders = new TreeSet<>();
		this.sellStockOrders = new TreeSet<>();
		this.stockOrderTransactions = new TreeSet<>();
		this.totalPriceQuantitySum = BigDecimal.ZERO;
		this.totalQuantity = BigInteger.ZERO;
	}

	/**
	 * Method adds buy stock order, if a buy order's price is equal to or higher
	 * than the lowest priced sell order currently available, a trade occurs and
	 * trade transaction is added to the transactions set.
	 * 
	 * @param stock
	 *            - AbstractStock of the stock to buy.
	 * @param quantity
	 *            - Integer of quantity of the stocks to buy.
	 * @param offeredPrice
	 *            - BigDecimal of offered price of the stock to buy.
	 * @param stockMarketPlayer
	 *            - StockMarketPlayer who is issuing stock buy order.
	 * 
	 * @return executedStockOrderTransactions - Set<StockOrderTransaction>
	 *         containing a set of an executed trade transactions.
	 */
	public Set<StockOrderTransaction> addBuyStockOrder(final AbstractStock stock, final Integer quantity, final BigDecimal offeredPrice, final StockMarketPlayer stockMarketPlayer) {
		final Set<StockOrderTransaction> executedStockOrderTransactions = new TreeSet<>();
		final StockBuyOrder stockBuyOrder = new StockBuyOrder(stock, quantity, offeredPrice, stockMarketPlayer);
		this.buyStockOrders.add(stockBuyOrder);

		for (final Iterator<StockSellOrder> iterator = this.sellStockOrders.iterator(); iterator.hasNext() && !stockBuyOrder.isMatched();) {
			final StockSellOrder stockSellOrder = iterator.next();

			if (!stockSellOrder.isMatched() && stockBuyOrder.getOfferedPrice().compareTo(stockSellOrder.getOfferedPrice()) >= 0) {

				final Integer exchangedStocksQuantiy = executeTradeTransaction(stock, stockBuyOrder, stockSellOrder);

				executedStockOrderTransactions.add(addExecutedStockOrderTransaction(stockBuyOrder, stockSellOrder, exchangedStocksQuantiy, stock.getPrice(), StockOrderTransactionType.BUY));
			}
		}

		return executedStockOrderTransactions;
	}

	/**
	 * Method adds a sell stock order to the set, if a sell order's price is equal
	 * to or lower than the highest priced buy order currently available, a trade
	 * occurs and trade transaction is added to the transactions set.
	 * 
	 * @param stock
	 *            - AbstractStock of the stock to sell.
	 * @param quantity
	 *            - Integer of quantity of the stocks to sell.
	 * @param offeredPrice
	 *            - BigDecimal of offered price of the stock to sell.
	 * @param stockMarketPlayer
	 *            - StockMarketPlayer who is issuing stock sell order.
	 * 
	 * @return executedStockOrderTransactions - Set<StockOrderTransaction>
	 *         containing a set of an executed trade transactions.
	 */
	public Set<StockOrderTransaction> addSellStockOrder(final AbstractStock stock, final Integer quantity, final BigDecimal offeredPrice, final StockMarketPlayer stockMarketPlayer) {
		final Set<StockOrderTransaction> executedStockOrderTransactions = new TreeSet<>();
		final StockSellOrder stockSellOrder = new StockSellOrder(stock, quantity, offeredPrice, stockMarketPlayer);
		this.sellStockOrders.add(stockSellOrder);

		for (final Iterator<StockBuyOrder> iterator = this.buyStockOrders.iterator(); iterator.hasNext() && !stockSellOrder.isMatched();) {
			final StockBuyOrder stockBuyOrder = iterator.next();

			if (!stockBuyOrder.isMatched() && stockSellOrder.getOfferedPrice().compareTo(stockBuyOrder.getOfferedPrice()) <= 0) {

				final Integer exchangedStocksQuantiy = executeTradeTransaction(stock, stockSellOrder, stockBuyOrder);

				executedStockOrderTransactions.add(addExecutedStockOrderTransaction(stockBuyOrder, stockSellOrder, exchangedStocksQuantiy, stock.getPrice(), StockOrderTransactionType.SELL));
			}
		}

		return executedStockOrderTransactions;
	}

	/**
	 * Method checks quantity of an incoming order and placed order and completes a
	 * trade transaction by calling corresponding method, returning value of
	 * executed stock trade quantity.
	 * 
	 * <b>The stock price is set with offered price value of incoming stock
	 * order.</b>
	 * 
	 * @param stock
	 *            - AbstractStock trading stock.
	 * @param incomingStockOrder
	 *            - AbstractStockOrder incoming new stock order which is placed to
	 *            the order book.
	 * @param existingStockOrder
	 *            - AbstractStockOrder existing stock order in the stock order book.
	 * 
	 * @return executedStockTradeQuantity - Integer value of executed stock trade
	 *         quantity.
	 */
	private Integer executeTradeTransaction(final AbstractStock stock, final AbstractStockOrder incomingStockOrder, final AbstractStockOrder existingStockOrder) {
		final int buyStockOrderQuantityCompareTo = incomingStockOrder.getQuantity().compareTo(existingStockOrder.getQuantity());
		int executedStockTradeQuantity = 0;

		if (buyStockOrderQuantityCompareTo > 0) {
			executedStockTradeQuantity = executeUncompletedStockOrderCompletedStockOrder(incomingStockOrder, existingStockOrder);
		} else if (buyStockOrderQuantityCompareTo < 0) {
			executedStockTradeQuantity = executeUncompletedStockOrderCompletedStockOrder(existingStockOrder, incomingStockOrder);
		} else {
			executedStockTradeQuantity = executeCompletedEqualsBuySellOrder(incomingStockOrder, existingStockOrder);
		}

		stock.setPrice(incomingStockOrder.getOfferedPrice());

		return executedStockTradeQuantity;
	}

	/**
	 * Method executes uncompleted buy stock order transaction due to buy stock
	 * order asked for more stock quantity, and complete sell stock order. Setting
	 * the remained quantity of stocks to buy/sell. Returning value of executed
	 * stock trade quantity.
	 * 
	 * @param uncompletedStockOrder
	 *            - StockBuyOrder.
	 * @param completedStockOrder
	 *            - StockSellOrder.
	 * 
	 * @return executedStockTradeQuantity - Integer value of executed stock trade
	 *         quantity.
	 */
	private Integer executeUncompletedStockOrderCompletedStockOrder(final AbstractStockOrder uncompletedStockOrder, final AbstractStockOrder completedStockOrder) {
		final Integer remainingBuyQuantity = uncompletedStockOrder.getQuantity() - completedStockOrder.getQuantity();
		uncompletedStockOrder.setQuantity(remainingBuyQuantity);
		completedStockOrder.setMatched(Boolean.TRUE);
		return completedStockOrder.getQuantity();
	}

	/**
	 * Method executes completed buy+sell stock orders of equal quantity orders.
	 * Returning value of executed stock trade quantity.
	 * 
	 * @param stockOrderOne
	 *            - AbstractStockOrder.
	 * @param stockOrderTwo
	 *            - AbstractStockOrder.
	 * 
	 * @return executedStockTradeQuantity - Integer value of executed stock trade
	 *         quantity.
	 */
	private Integer executeCompletedEqualsBuySellOrder(final AbstractStockOrder stockOrderOne, final AbstractStockOrder stockOrderTwo) {
		stockOrderOne.setMatched(Boolean.TRUE);
		stockOrderTwo.setMatched(Boolean.TRUE);
		return stockOrderOne.getQuantity();
	}

	/**
	 * Method calculates Volume Weighted Stock Price using accumulated Price
	 * multiplied Quantity and divided by total Quantity <br>
	 * (Price[i] * Quantity[i] / Total Quantity),<br>
	 * of all transactions made in this Stock Order Book.
	 * 
	 * @return BigDecimal of Volume Weighted Stock Price of executed buy/sell trade
	 *         transactions.
	 */
	public BigDecimal calculateVolumeWeightedStockPrice() {
		return this.totalPriceQuantitySum.divide(new BigDecimal(this.totalQuantity.intValue()), NumberFormatter.SCALE_4_DECIMAL_DIGITS, RoundingMode.HALF_EVEN);
	}

	/**
	 * Method adds the two matched buy+sell stock orders to an executed stock order
	 * transactions set.
	 * 
	 * @param stockBuyOrder
	 *            - StockBuyOrder of an executed stock buy order.
	 * @param stockSellOrder
	 *            - StockBuyOrder of an executed stock sell order.
	 * @param exchangedStocksQuantiy
	 *            - Integer of traded/executed stocks quantity.
	 * @param transactionStockPrice
	 *            - BigDecimal of transaction stocks price.
	 * @param stockOrderTransactionType
	 *            - StockOrderTransactionType of an executed stock operation.
	 * 
	 * @return stockOrderTransaction - StockOrderTransaction of an executed trade
	 *         operation.
	 */
	private StockOrderTransaction addExecutedStockOrderTransaction(final StockBuyOrder stockBuyOrder, final StockSellOrder stockSellOrder, final Integer exchangedStocksQuantiy,
			final BigDecimal transactionStockPrice, final StockOrderTransactionType stockOrderTransactionType) {
		final StockOrderTransaction stockOrderTransaction = new StockOrderTransaction(stockBuyOrder, stockSellOrder, exchangedStocksQuantiy, transactionStockPrice, stockOrderTransactionType);
		this.stockOrderTransactions.add(stockOrderTransaction);
		this.totalPriceQuantitySum = this.totalPriceQuantitySum.add(transactionStockPrice.multiply(new BigDecimal(exchangedStocksQuantiy.intValue())));
		this.totalQuantity = this.totalQuantity.add(BigInteger.valueOf(exchangedStocksQuantiy.intValue()));
		return stockOrderTransaction;
	}

	/**
	 * @return the buyStockOrders
	 */
	public Set<StockBuyOrder> getBuyStockOrders() {
		return buyStockOrders;
	}

	/**
	 * @return the sellStockOrders
	 */
	public Set<StockSellOrder> getSellStockOrders() {
		return sellStockOrders;
	}

	/**
	 * @return the stockOrderTransactions
	 */
	public Set<StockOrderTransaction> getStockOrderTransactions() {
		return stockOrderTransactions;
	}
}