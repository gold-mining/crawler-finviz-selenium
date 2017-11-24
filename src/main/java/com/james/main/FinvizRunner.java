package com.james.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.james.crawler.StockDetailCrawler;
import com.james.crawler.StockLIstCrawler;

public class FinvizRunner {
	public static void main(String[] args) {
		Date date1 = new Date();

		Map<String, String> input = new HashMap<String, String>();
		for (int i = 0; i < args.length; i++) {
			String[] parts = args[i].trim().split("=");
			input.put(parts[0], parts[1]);
			System.out.println(parts[0] + ": " + parts[1]);
		}

		FinvizRunner runner = new FinvizRunner();

		switch (input.get("mode")) {
			case "list-stock": runner.getStockList(input.get("start"), input.get("stop"), input.get("output")); break;
			case "all-stock-detail": runner.getAllStockDetail(input.get("stock-list"), input.get("date"), input.get("output")); break;
			case "all-stock-detail-multithread": runner.getAllStockDetailMultiThread(input.get("thread"), input.get("stock-list"), input.get("date"), input.get("output")); break;
			default: break;
		}

		Date date2 = new Date();
		System.out.println(date2.getTime() - date1.getTime());
	}

	public void getStockList(String start, String stop, String output) {
		StockLIstCrawler crawler = new StockLIstCrawler(Integer.parseInt(start), Integer.parseInt(stop), output);
		crawler.getStockList();
	}

	public void getAllStockDetail(String stockList, String date, String output) {
		Queue<String> queue = getStockListFromFile(stockList);
		while (!queue.isEmpty()) {
			StockDetailCrawler crawler = new StockDetailCrawler();
			if(crawler.initCrawler(queue.poll(), "2017-11-18", output)) crawler.getDetailInfo();
		}
	}

	public void getAllStockDetailMultiThread(String threadNumber, String stockList, String date, String output) {
		try {
			Queue<String> queue = getStockListFromFile(stockList);

			ExecutorService executor = Executors.newFixedThreadPool(Integer.parseInt(threadNumber));

			while (!queue.isEmpty()) {
				executor.execute(new Runnable() {
					String stock;
					String folder;
					String output;
					public void run() {
						StockDetailCrawler crawler = new StockDetailCrawler();
						if(crawler.initCrawler(stock, folder, output)) crawler.getDetailInfo();
					}

					private Runnable init(String stock, String folder, String output) {
						this.stock = stock;
						this.folder = folder;
						this.output = output;
						return this;
					}
				}.init(queue.poll(), date, output));
			}

			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Queue<String> getStockListFromFile(String stockList) {
		Queue<String> stockQueue = new LinkedList<String>();

		try {
			File file = new File(stockList);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stockQueue.add(line.split("\t")[0]);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stockQueue;
	}
}
