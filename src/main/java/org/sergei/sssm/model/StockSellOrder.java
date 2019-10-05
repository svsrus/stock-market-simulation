package org.sergei.sssm.model;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.10.01. initial version
 */
public class StockSellOrder extends AbstractStockOrder implements Comparable<StockSellOrder> {

	/**
	 * Constructor calls superclass constructor.
	 */
	public StockSellOrder() {
		super();
	}

	/**
	 * Constructor calls for super constructor.
	 * 
	 * @param stock
	 *            - AbstractStock of order.
	 * @param quantity
	 *            - Integer stock order quantity.
	 * @param offeredPrice
	 *            - BigDecimal stock order offered price.
	 * @param stockMarketPlayer
	 *            - StockMarketPlayer who is issuing stock sell order.
	 */
	public StockSellOrder(final AbstractStock stock, final Integer quantity, final BigDecimal offeredPrice, final StockMarketPlayer stockMarketPlayer) {
		super(stock, quantity, offeredPrice, stockMarketPlayer);
	}

	/**
	 * Method compares this object's offeredPrice and time stamp attribute to the
	 * given parameters object's attributes in ascending order. Example of ordering:
	 * 
	 * Sell order: Offered Price: 9.00 Time stamp: 2017-10-03 19:20:33 <br>
	 * Sell order: Offered Price: 9.50 Time stamp: 2017-10-03 19:20:32 <br>
	 * Sell order: Offered Price: 9.50 Time stamp: 2017-10-03 19:20:34 <br>
	 * Sell order: Offered Price: 9.50 Time stamp: 2017-10-03 19:20:36 <br>
	 * Sell order: Offered Price: 9.90 Time stamp: 2017-10-03 19:20:35 <br>
	 * Sell order: Offered Price: 10.0 Time stamp: 2017-10-03 19:20:31 <br>
	 * 
	 * @param stockSellOrder
	 *            - StockSellOrder to compare with this object.
	 * 
	 * @return int - This object is less then a given object if offeredPrice and
	 *         time stamp is lower then of a given object's attributes.<br>
	 *         This object is equals to a given object if offeredPrice and time
	 *         stamp are equals of a given object's attributes.<br>
	 *         This object is greater then a given object if offeredPrice and time
	 *         stamp is greater then of a given object's attributes.<br>
	 */
	@Override
	public int compareTo(final StockSellOrder stockSellOrder) {
		return Comparator.comparing(StockSellOrder::getOfferedPrice).thenComparing(StockSellOrder::getTimestamp).compare(this, stockSellOrder);
	}
}