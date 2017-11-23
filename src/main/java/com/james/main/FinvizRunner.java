package com.james.main;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.james.crawler.StockDetailCrawler;
import com.james.crawler.StockLIstCrawler;
import com.james.database.StockList;

public class FinvizRunner {
	public static void main(String[] args) {
		Date date1 = new Date();

		FinvizRunner runner = new FinvizRunner();
		// runner.getAllStockDetail();
		runner.getAllStockDetailMultiThread(5);
		// runner.getStockList();

		Date date2 = new Date();
		System.out.println(date2.getTime() - date1.getTime());
	}

	public void getStockList() {
		StockLIstCrawler crawler = new StockLIstCrawler(1, 115);
		crawler.getStockList();
	}

	public void getAllStockDetail() {
		String[] stockList = { "INTC", "NVS", "T", "PFE", "PTR", "TSM", "TM", "ORCL", "UNH", "CHL" };
		for (String stock : stockList) {
			StockDetailCrawler crawler = new StockDetailCrawler(stock, "2017-11-18");
			crawler.getDetailInfo();
		}
	}

	public void getAllStockDetailMultiThread(int threadNumber) {

		try {
			Queue<String> queue = StockList.getStockListQueus();

			ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

			while (!queue.isEmpty()) {
				executor.execute(new Runnable() {
					String stock;

					public void run() {
						StockDetailCrawler crawler = new StockDetailCrawler(stock, "2017-11-18");
						crawler.getDetailInfo();
					}

					private Runnable init(String stock) {
						this.stock = stock;
						return this;
					}
				}.init(queue.poll()));
			}

			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
