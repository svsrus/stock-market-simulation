package org.sergei.sssm.model;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.10.01. initial version
 */
public class StockBuyOrder extends AbstractStockOrder implements Comparable<StockBuyOrder> {

	/**
	 * Constructor calls superclass constructor.
	 */
	public StockBuyOrder() {
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
	 *            - StockMarketPlayer who is issuing a stock order.
	 */
	public StockBuyOrder(final AbstractStock stock, final Integer quantity, final BigDecimal offeredPrice, final StockMarketPlayer stockMarketPlayer) {
		super(stock, quantity, offeredPrice, stockMarketPlayer);
	}

	/**
	 * Method compares this object's offeredPrice and time stamp attribute to the
	 * price parameters object's attribute in ascending order and time stamp in
	 * descending order. Example of ordering:
	 * 
	 * Buy order: Offered Price: 10.0 Time stamp: 2017-10-03 19:20:27 <br>
	 * Buy order: Offered Price: 9.80 Time stamp: 2017-10-03 19:20:28 <br>
	 * Buy order: Offered Price: 9.80 Time stamp: 2017-10-03 19:20:31 <br>
	 * Buy order: Offered Price: 9.50 Time stamp: 2017-10-03 19:20:29 <br>
	 * Buy order: Offered Price: 9.00 Time stamp: 2017-10-03 19:20:30 <br>
	 * 
	 * @param stockBuyOrder
	 *            - StockBuyOrder to compare with this object.
	 * 
	 * @return int - This object is less then a given object if offeredPrice and
	 *         time stamp is lower then of a given object's attributes.<br>
	 *         This object is equals to a given object if offeredPrice and time
	 *         stamp are equals of a given object's attributes.<br>
	 *         This object is greater then a given object if offeredPrice and time
	 *         stamp is greater then of a given object's attributes.<br>
	 */
	@Override
	public int compareTo(final StockBuyOrder stockBuyOrder) {
		return Comparator.comparing(StockBuyOrder::getOfferedPrice).thenComparing(StockBuyOrder::getTimestamp, Comparator.reverseOrder()).compare(stockBuyOrder, this);
	}
}