package com.james.crawler;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.james.modal.BasicStatistic;
import com.james.modal.Insider;
import com.james.modal.News;
import com.james.modal.RatingOuter;

public class StockDetailCrawler {

	private String ticker = "";
	
	private String output = "/Users/James/James/crawler_result/finviz/stock-detail";

	private WebDriver driver;

	private WebDriverWait wait;

	private PrintWriter writer;

	public StockDetailCrawler() {
		super();
		setupSelenium();
	}

	public StockDetailCrawler(String ticker, String date) {
		try {
			setupSelenium();
			this.ticker = ticker;
			this.writer = new PrintWriter(output + "/" + date + "/" + this.ticker + ".txt", "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getDetailInfo() {
		startPage();
		BasicStatistic basicStatistic = getBasicStatistic();
		List<RatingOuter> ratingOuters = getRatingOuter();
		List<News> news = getNews();
		List<Insider> insiders = getInsider();
		outpuResult(basicStatistic, ratingOuters, news, insiders);
		clean();
	}

	private void startPage() {
		driver.get("https://finviz.com/quote.ashx?t=" + this.ticker + "&ty=c&p=d&b=1");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("chart0")));
	}

	private BasicStatistic getBasicStatistic() {
		BasicStatistic basicStatistic = new BasicStatistic();
		WebElement basicStatisticSection = driver.findElement(By.xpath("/html/body/table[3]/tbody/tr[1]/td/table/tbody/tr[7]/td/table"));

		basicStatistic.Index = basicStatisticSection.findElement(By.xpath("tbody/tr[1]/td[2]")).getText();
		basicStatistic.MarketCap = basicStatisticSection.findElement(By.xpath("tbody/tr[2]/td[2]")).getText();
		basicStatistic.Income = basicStatisticSection.findElement(By.xpath("tbody/tr[3]/td[2]")).getText();
		basicStatistic.Sales = basicStatisticSection.findElement(By.xpath("tbody/tr[4]/td[2]")).getText();
		basicStatistic.Book_sh = basicStatisticSection.findElement(By.xpath("tbody/tr[5]/td[2]")).getText();
		basicStatistic.Cash_sh = basicStatisticSection.findElement(By.xpath("tbody/tr[6]/td[2]")).getText();
		basicStatistic.Dividend = basicStatisticSection.findElement(By.xpath("tbody/tr[7]/td[2]")).getText();
		basicStatistic.Dividend_percentage = basicStatisticSection.findElement(By.xpath("tbody/tr[8]/td[2]")).getText();
		basicStatistic.Employees = basicStatisticSection.findElement(By.xpath("tbody/tr[9]/td[2]")).getText();
		basicStatistic.Optionable = basicStatisticSection.findElement(By.xpath("tbody/tr[10]/td[2]")).getText();
		basicStatistic.Shortable = basicStatisticSection.findElement(By.xpath("tbody/tr[11]/td[2]")).getText();
		basicStatistic.Recom = basicStatisticSection.findElement(By.xpath("tbody/tr[12]/td[2]")).getText();

		basicStatistic.P_E = basicStatisticSection.findElement(By.xpath("tbody/tr[1]/td[4]")).getText();
		basicStatistic.Forward_P_E = basicStatisticSection.findElement(By.xpath("tbody/tr[2]/td[4]")).getText();
		basicStatistic.PEG = basicStatisticSection.findElement(By.xpath("tbody/tr[3]/td[4]")).getText();
		basicStatistic.P_S = basicStatisticSection.findElement(By.xpath("tbody/tr[4]/td[4]")).getText();
		basicStatistic.P_B = basicStatisticSection.findElement(By.xpath("tbody/tr[5]/td[4]")).getText();
		basicStatistic.P_C = basicStatisticSection.findElement(By.xpath("tbody/tr[6]/td[4]")).getText();
		basicStatistic.P_FCF = basicStatisticSection.findElement(By.xpath("tbody/tr[7]/td[4]")).getText();
		basicStatistic.Quick_Ratio = basicStatisticSection.findElement(By.xpath("tbody/tr[8]/td[4]")).getText();
		basicStatistic.Current_Ratio = basicStatisticSection.findElement(By.xpath("tbody/tr[9]/td[4]")).getText();
		basicStatistic.Debt_Eq = basicStatisticSection.findElement(By.xpath("tbody/tr[10]/td[4]")).getText();
		basicStatistic.LT_Debt_Eq = basicStatisticSection.findElement(By.xpath("tbody/tr[11]/td[4]")).getText();
		basicStatistic.SMA20 = basicStatisticSection.findElement(By.xpath("tbody/tr[12]/td[4]")).getText();

		basicStatistic.EPS_ttm = basicStatisticSection.findElement(By.xpath("tbody/tr[1]/td[6]")).getText();
		basicStatistic.EPS_next_Y_num = basicStatisticSection.findElement(By.xpath("tbody/tr[2]/td[6]")).getText();
		basicStatistic.EPS_next_Q = basicStatisticSection.findElement(By.xpath("tbody/tr[3]/td[6]")).getText();
		basicStatistic.EPS_this_Y = basicStatisticSection.findElement(By.xpath("tbody/tr[4]/td[6]")).getText();
		basicStatistic.EPS_next_Y_percentage = basicStatisticSection.findElement(By.xpath("tbody/tr[5]/td[6]")).getText();
		basicStatistic.EPS_next_5Y = basicStatisticSection.findElement(By.xpath("tbody/tr[6]/td[6]")).getText();
		basicStatistic.EPS_past_5Y = basicStatisticSection.findElement(By.xpath("tbody/tr[7]/td[6]")).getText();
		basicStatistic.Sales_past_5Y = basicStatisticSection.findElement(By.xpath("tbody/tr[8]/td[6]")).getText();
		basicStatistic.Sales_Q_Q = basicStatisticSection.findElement(By.xpath("tbody/tr[9]/td[6]")).getText();
		basicStatistic.EPS_Q_Q = basicStatisticSection.findElement(By.xpath("tbody/tr[10]/td[6]")).getText();
		basicStatistic.Earnings = basicStatisticSection.findElement(By.xpath("tbody/tr[11]/td[6]")).getText();
		basicStatistic.SMA50 = basicStatisticSection.findElement(By.xpath("tbody/tr[12]/td[6]")).getText();

		basicStatistic.Insider_Own = basicStatisticSection.findElement(By.xpath("tbody/tr[1]/td[8]")).getText();
		basicStatistic.Insider_Trans = basicStatisticSection.findElement(By.xpath("tbody/tr[2]/td[8]")).getText();
		basicStatistic.Inst_Own = basicStatisticSection.findElement(By.xpath("tbody/tr[3]/td[8]")).getText();
		basicStatistic.Inst_Trans = basicStatisticSection.findElement(By.xpath("tbody/tr[4]/td[8]")).getText();
		basicStatistic.ROA = basicStatisticSection.findElement(By.xpath("tbody/tr[5]/td[8]")).getText();
		basicStatistic.ROE = basicStatisticSection.findElement(By.xpath("tbody/tr[6]/td[8]")).getText();
		basicStatistic.ROI = basicStatisticSection.findElement(By.xpath("tbody/tr[7]/td[8]")).getText();
		basicStatistic.Gross_Margin = basicStatisticSection.findElement(By.xpath("tbody/tr[8]/td[8]")).getText();
		basicStatistic.Oper_Margin = basicStatisticSection.findElement(By.xpath("tbody/tr[9]/td[8]")).getText();
		basicStatistic.Profit_Margin = basicStatisticSection.findElement(By.xpath("tbody/tr[10]/td[8]")).getText();
		basicStatistic.Payout = basicStatisticSection.findElement(By.xpath("tbody/tr[11]/td[8]")).getText();
		basicStatistic.SMA200 = basicStatisticSection.findElement(By.xpath("tbody/tr[12]/td[8]")).getText();

		basicStatistic.Shs_Outstand = basicStatisticSection.findElement(By.xpath("tbody/tr[1]/td[10]")).getText();
		basicStatistic.Shs_Float = basicStatisticSection.findElement(By.xpath("tbody/tr[2]/td[10]")).getText();
		basicStatistic.Short_Float = basicStatisticSection.findElement(By.xpath("tbody/tr[3]/td[10]")).getText();
		basicStatistic.Short_Ratio = basicStatisticSection.findElement(By.xpath("tbody/tr[4]/td[10]")).getText();
		basicStatistic.Target_Price = basicStatisticSection.findElement(By.xpath("tbody/tr[5]/td[10]")).getText();
		basicStatistic.W52_Range = basicStatisticSection.findElement(By.xpath("tbody/tr[6]/td[10]")).getText();
		basicStatistic.W52_High = basicStatisticSection.findElement(By.xpath("tbody/tr[7]/td[10]")).getText();
		basicStatistic.W52_Low = basicStatisticSection.findElement(By.xpath("tbody/tr[8]/td[10]")).getText();
		basicStatistic.RSI = basicStatisticSection.findElement(By.xpath("tbody/tr[9]/td[10]")).getText();
		basicStatistic.Rel_Volume = basicStatisticSection.findElement(By.xpath("tbody/tr[10]/td[10]")).getText();
		basicStatistic.Avg_Volume = basicStatisticSection.findElement(By.xpath("tbody/tr[11]/td[10]")).getText();
		basicStatistic.Volume = basicStatisticSection.findElement(By.xpath("tbody/tr[12]/td[10]")).getText();

		basicStatistic.Perf_Week = basicStatisticSection.findElement(By.xpath("tbody/tr[1]/td[12]")).getText();
		basicStatistic.Perf_Month = basicStatisticSection.findElement(By.xpath("tbody/tr[2]/td[12]")).getText();
		basicStatistic.Perf_Quarter = basicStatisticSection.findElement(By.xpath("tbody/tr[3]/td[12]")).getText();
		basicStatistic.Perf_Half_Y = basicStatisticSection.findElement(By.xpath("tbody/tr[4]/td[12]")).getText();
		basicStatistic.Perf_Year = basicStatisticSection.findElement(By.xpath("tbody/tr[5]/td[12]")).getText();
		basicStatistic.Perf_YTD = basicStatisticSection.findElement(By.xpath("tbody/tr[6]/td[12]")).getText();
		basicStatistic.Beta = basicStatisticSection.findElement(By.xpath("tbody/tr[7]/td[12]")).getText();
		basicStatistic.ATR = basicStatisticSection.findElement(By.xpath("tbody/tr[8]/td[12]")).getText();
		basicStatistic.Volatility = basicStatisticSection.findElement(By.xpath("tbody/tr[9]/td[12]")).getText();
		basicStatistic.Prev_Close = basicStatisticSection.findElement(By.xpath("tbody/tr[10]/td[12]")).getText();
		basicStatistic.Price = basicStatisticSection.findElement(By.xpath("tbody/tr[11]/td[12]")).getText();
		basicStatistic.Change = basicStatisticSection.findElement(By.xpath("tbody/tr[12]/td[12]")).getText();

		return basicStatistic;
	}

	private List<RatingOuter> getRatingOuter() {
		try {
			List<RatingOuter> ratingOuters = new ArrayList<RatingOuter>();
			WebElement element = driver.findElement(By.xpath("/html/body/table[3]/tbody/tr[2]/td/table/tbody/tr[4]/td/table"));
			List<WebElement> allRatings = element.findElements(By.xpath("tbody/tr"));

			for (int i = 0; i < allRatings.size(); i++) {
				RatingOuter ratingOuter = new RatingOuter();
				ratingOuter.date = allRatings.get(i).findElement(By.xpath("td/table/tbody/tr/td[1]")).getText();
				ratingOuter.rating = allRatings.get(i).findElement(By.xpath("td/table/tbody/tr/td[2]")).getText();
				ratingOuter.institution = allRatings.get(i).findElement(By.xpath("td/table/tbody/tr/td[3]")).getText();
				ratingOuter.action = allRatings.get(i).findElement(By.xpath("td/table/tbody/tr/td[4]")).getText();
				ratingOuter.targetPrice = allRatings.get(i).findElement(By.xpath("td/table/tbody/tr/td[5]")).getText();
				ratingOuters.add(ratingOuter);
			}

			return ratingOuters;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private List<News> getNews() {
		try {
			List<News> newses = new ArrayList<News>();
			List<WebElement> allNews = driver.findElements(By.xpath("//*[@id=\"news-table\"]/tbody/tr"));
			String preDate = null;

			for (int i = 0; i < allNews.size(); i++) {
				News news = new News();

				String dateTime = allNews.get(i).findElement(By.xpath("td[1]")).getText().trim();
				String date = (dateTime.split(" ").length > 1) ? dateTime.split(" ")[0] : preDate;
				String time = (dateTime.split(" ").length > 1) ? dateTime.split(" ")[1] : dateTime.split(" ")[0];
				preDate = date;

				news.date = date;
				news.time = time;
				news.content = allNews.get(i).findElement(By.xpath("td[2]")).getText();
				newses.add(news);
			}
			return newses;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<Insider> getInsider() {
		try {
			List<Insider> insiders = new ArrayList<Insider>();
			List<WebElement> allInsiders = driver.findElements(By.xpath("/html/body/table[3]/tbody/tr[2]/td/table/tbody/tr[10]/td/table/tbody/tr"));

			for (int i = 0; i < allInsiders.size(); i++) {
				Insider insider = new Insider();

				insider.name = allInsiders.get(i).findElement(By.xpath("td[1]")).getText();
				insider.relationShip = allInsiders.get(i).findElement(By.xpath("td[2]")).getText();
				insider.date = allInsiders.get(i).findElement(By.xpath("td[3]")).getText();
				insider.transaction = allInsiders.get(i).findElement(By.xpath("td[4]")).getText();
				insider.cost = allInsiders.get(i).findElement(By.xpath("td[5]")).getText();
				insider.shares = allInsiders.get(i).findElement(By.xpath("td[6]")).getText();
				insider.value = allInsiders.get(i).findElement(By.xpath("td[7]")).getText();
				insider.sharesTotal = allInsiders.get(i).findElement(By.xpath("td[8]")).getText();
				insider.total = allInsiders.get(i).findElement(By.xpath("td[9]")).getText();

				insiders.add(insider);
			}

			return insiders;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void outpuResult(BasicStatistic basicStatistic, List<RatingOuter> ratingOuters, List<News> news, List<Insider> insiders) {
		ratingOuters = (ratingOuters == null) ? new ArrayList<RatingOuter>() : ratingOuters;
		news = (news == null) ? new ArrayList<News>() : news;
		insiders = (insiders == null) ? new ArrayList<Insider>() : insiders;
		
		outpuResultToConsole(basicStatistic, ratingOuters, news, insiders);
		outpuResultToFiles(basicStatistic, ratingOuters, news, insiders);
	}
	
	private void outpuResultToFiles(BasicStatistic basicStatistic, List<RatingOuter> ratingOuters, List<News> news, List<Insider> insiders) {
		writer.println(this.ticker);
		writer.println("\t basic statistic");
		writer.println("\t\t" + BasicStatistic.markers());
		writer.println("\t\t" + basicStatistic);
		
		writer.println("\t rating");
		for(RatingOuter ratingOuter: ratingOuters) writer.println("\t\t" + ratingOuter);
		
		writer.println("\t news");
		for(News singleNews: news) writer.println("\t\t" + singleNews);
		
		writer.println("\t inside trading");
		for(Insider insider: insiders) writer.println("\t\t" + insider);
	}
	
	private void outpuResultToConsole(BasicStatistic basicStatistic, List<RatingOuter> ratingOuters, List<News> news, List<Insider> insiders) {
		System.out.println(this.ticker);
		System.out.println("\t basic statistic");
		System.out.println("\t\t" + BasicStatistic.markers());
		System.out.println("\t\t" + basicStatistic);
		
		System.out.println("\t rating");
		for(RatingOuter ratingOuter: ratingOuters) System.out.println("\t\t" + ratingOuter);
		
		System.out.println("\t news");
		for(News singleNews: news) System.out.println("\t\t" + singleNews);
		
		System.out.println("\t inside trading");
		for(Insider insider: insiders) System.out.println("\t\t" + insider);
		
		System.out.println();
		System.out.println();
	}

	private void setupSelenium() {
		System.setProperty("webdriver.chrome.driver", "/Users/James/tools/web-driver/chromedriver");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 13);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.manage().window().setSize(new Dimension(1200, 3000));
	}

	private void clean() {
		this.driver.close();
		this.driver.quit();
		this.writer.close();
	}
}
