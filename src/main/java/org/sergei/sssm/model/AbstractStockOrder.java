package org.sergei.sssm.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.10.03. initial version
 */
public abstract class AbstractStockOrder {
	public static final int MAX_QUANTITY_LIMIT = 1000;
	private AbstractStock stock;
	private Integer quantity;
	private BigDecimal offeredPrice;
	private Timestamp timestamp;
	private StockMarketPlayer stockMarketPlayer;
	private boolean matched;

	/**
	 * Constructor sets current time stamp to timestamp attribute.
	 */
	public AbstractStockOrder() {
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * Constructor initializes classes attributes.
	 * 
	 * @param stock
	 *            - AbstractStock of order.
	 * @param quantity
	 *            - Integer stock order quantity.
	 * @param offeredPrice
	 *            - BigDecimal stock order offered price.
	 * @param stockMarketPlayer
	 *            - StockMarketPlayer who is issuing stock order.
	 */
	public AbstractStockOrder(AbstractStock stock, Integer quantity, BigDecimal offeredPrice, StockMarketPlayer stockMarketPlayer) {
		this();
		this.stock = stock;
		this.quantity = quantity;
		this.offeredPrice = offeredPrice;
		this.stockMarketPlayer = stockMarketPlayer;
	}

	/**
	 * @return the stock
	 */
	public AbstractStock getStock() {
		return stock;
	}

	/**
	 * @param stock
	 *            the stock to set
	 */
	public void setStock(AbstractStock stock) {
		this.stock = stock;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the offeredPrice
	 */
	public BigDecimal getOfferedPrice() {
		return offeredPrice;
	}

	/**
	 * @param offeredPrice
	 *            the offeredPrice to set
	 */
	public void setOfferedPrice(BigDecimal offeredPrice) {
		this.offeredPrice = offeredPrice;
	}

	/**
	 * @return the timestamp
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the stockMarketPlayer
	 */
	public StockMarketPlayer getStockMarketPlayer() {
		return stockMarketPlayer;
	}

	/**
	 * @param stockMarketPlayer
	 *            the stockMarketPlayer to set
	 */
	public void setStockMarketPlayer(StockMarketPlayer stockMarketPlayer) {
		this.stockMarketPlayer = stockMarketPlayer;
	}

	/**
	 * @return the matched
	 */
	public boolean isMatched() {
		return matched;
	}

	/**
	 * @param matched
	 *            the matched to set
	 */
	public void setMatched(boolean matched) {
		this.matched = matched;
	}

	/**
	 * Method represents this object as string value.
	 */
	@Override
	public String toString() {
		return new StringBuilder("Stock Price: ").append(this.stock.getPrice().toString()).append("\tOrder quantity: ").append(this.quantity).append("\tOffered Price: ").append(this.offeredPrice)
				.append("\tTimestamp: ").append(this.timestamp).toString();
	}
}