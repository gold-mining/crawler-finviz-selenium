package com.james.crawler;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.james.modal.OverView;

public class StockLIstCrawler {

	private int startPage = 1;

	private int endPage = 200;
	
	private String output = "/Users/James/James/crawler_result/finviz/stock-list"; 

	private WebDriver driver;

	private WebDriverWait wait;
	
	private PrintWriter writer;

	public StockLIstCrawler() {
		super();
		setupSelenium();
	}

	public StockLIstCrawler(int startPage, int endPage) {
		setupSelenium();
		this.startPage = startPage;
		this.endPage = endPage;
	}

	public void getStockList() {
		startPage();
		goThroughStocks();
		clean();
	}

	private void startPage() {
		driver.get("https://finviz.com/screener.ashx?v=111&o=-marketcap&r=" + ((startPage - 1) * 20 + 1));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"screener-content\"]/table/tbody/tr[4]/td/table")));
	}

	private void goThroughStocks() {
		int currentPage = 0;

		do {
			currentPage++;
			goThroughStocksInCurrentPage(currentPage);
			goToNextPage();
		} while (currentPage != endPage);
	}

	private void goThroughStocksInCurrentPage(int page) {
		try {
			writer = new PrintWriter(output + "/result" + page + ".txt", "UTF-8");
			for (int i = 2; i <= 21; i++) {
				WebElement row = driver.findElement(By.xpath("//*[@id=\"screener-content\"]/table/tbody/tr[4]/td/table/tbody/tr[" + i + "]"));
				OverView overView = getOverView(row);	
				writer.println(overView);
				System.out.println(overView);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void goToNextPage() {
		boolean waitHuman = false;
		do {
			try {
				driver.findElement(By.xpath("//*[@id=\"screener-content\"]/table/tbody/tr[7]/td/a[last()]")).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"screener-content\"]/table/tbody/tr[4]/td/table")));
				waitHuman = false;
			} catch (Exception e) {
				// System.out.print("--------- 1. wait human ---------------");
				waitHuman = true;
			}
		} while (waitHuman);

	}

	private OverView getOverView(WebElement info) {
		OverView overView = new OverView();
		overView.Ticker = info.findElement(By.xpath("td[2]")).getText();
		overView.Company = info.findElement(By.xpath("td[3]")).getText();
		overView.Sector = info.findElement(By.xpath("td[4]")).getText();
		overView.Industry = info.findElement(By.xpath("td[5]")).getText();
		overView.Country = info.findElement(By.xpath("td[6]")).getText();
		overView.MarketCap = setMarketCap(info.findElement(By.xpath("td[7]")).getText());
		return overView;
	}
	
	private String setMarketCap(String input) {
		input  = input.trim();
		double baseNumber = Double.parseDouble(input.replace("B", "").replace("M", ""));
		double unit = input.contains("B") ? 1000000000 : 1000000;
		long marketCap = (long)(baseNumber * unit);
		return Long.toString(marketCap);
	}

	private void setupSelenium() {
		System.setProperty("webdriver.chrome.driver", "/Users/James/tools/web-driver/chromedriver");

		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 13);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.manage().window().setSize(new Dimension(1200, 3000));
	}
	
	private void clean() {
		driver.close();
	}
}
