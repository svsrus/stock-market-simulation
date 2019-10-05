package org.sergei.sssm.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;

import org.sergei.sssm.utils.NumberFormatter;
import org.sergei.sssm.utils.TimeFormatter;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.10.01. initial version
 */
public class StockOrderTransaction implements Comparable<StockOrderTransaction> {
	private StockBuyOrder stockBuyOrder;
	private StockSellOrder stockSellOrder;
	private StockOrderTransactionType stockOrderTransactionType;
	private Integer exchangedStocksQuantiy;
	private BigDecimal transactionStockPrice;
	private Timestamp transactionCompletedTimestamp;
	private Long transactionCompletedNanoTime;

	/**
	 * Constructor sets class attributes with a given parameters and sets current
	 * timestamp to transaction completed timestamp attribute.
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
	 */
	public StockOrderTransaction(final StockBuyOrder stockBuyOrder, final StockSellOrder stockSellOrder, final Integer exchangedStocksQuantiy, final BigDecimal transactionStockPrice,
			final StockOrderTransactionType stockOrderTransactionType) {
		this.stockBuyOrder = stockBuyOrder;
		this.stockSellOrder = stockSellOrder;
		this.stockOrderTransactionType = stockOrderTransactionType;
		this.exchangedStocksQuantiy = exchangedStocksQuantiy;
		this.transactionStockPrice = transactionStockPrice;
		this.transactionCompletedTimestamp = new Timestamp(System.currentTimeMillis());
		this.transactionCompletedNanoTime = System.nanoTime();
	}

	/**
	 * @return the stockBuyOrder
	 */
	public StockBuyOrder getStockBuyOrder() {
		return stockBuyOrder;
	}

	/**
	 * @return the stockSellOrder
	 */
	public StockSellOrder getStockSellOrder() {
		return stockSellOrder;
	}

	/**
	 * @return the stockOrderTransactionType
	 */
	public StockOrderTransactionType getStockOrderTransactionType() {
		return stockOrderTransactionType;
	}

	/**
	 * @return the exchangedStocksQuantiy
	 */
	public Integer getExchangedStocksQuantiy() {
		return exchangedStocksQuantiy;
	}

	/**
	 * @return the transactionStockPrice
	 */
	public BigDecimal getTransactionStockPrice() {
		return transactionStockPrice;
	}

	/**
	 * @return the transactionCompletedTimestamp
	 */
	public Long getTransactionCompletedTimestamp() {
		return transactionCompletedNanoTime;
	}

	/**
	 * Method compares this object by timestamp versus a given StockOrderTransaction
	 * object's timestamp.
	 * 
	 * @param stockOrderTransaction
	 *            - StockOrderTransaction object to compare.
	 */
	@Override
	public int compareTo(final StockOrderTransaction stockOrderTransaction) {
		return Comparator.comparing(StockOrderTransaction::getTransactionCompletedTimestamp).compare(this, stockOrderTransaction);
	}

	/**
	 * Method returns this object formatted as String.
	 */
	@Override
	public String toString() {
		return new StringBuilder(this.stockOrderTransactionType.getCode()).append(" transaction \t Executed time: ").append(TimeFormatter.format(this.transactionCompletedTimestamp))
				.append("\tQuantity: ").append(this.exchangedStocksQuantiy).append("\tPrice: ").append(NumberFormatter.format(this.transactionStockPrice)).toString();
	}
}