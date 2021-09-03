package avic;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class AvicSmokeTests {

    private WebDriver driver;

    @BeforeTest
    public void profileSetUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    }

    @BeforeMethod
    public void testSetUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://avic.ua/");
    }

    @Test(priority = 1)
    public void checkThatUrlContainsSearchWord() {
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("iPhone 11");
        driver.findElement(By.xpath("//button[@class='button-reset search-btn']")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        assertTrue(driver.getCurrentUrl().contains("query=iPhone"));
    }

    @Test(priority = 2)
    public void checkElementsAmountInSearchPage() {
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("iPhone 11");
        driver.findElement(By.xpath("//button[@class='button-reset search-btn']")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        List<WebElement> elementList = driver.findElements(By.xpath("//div[@class='prod-cart__descr']"));
        assertEquals(elementList.size(), 12);
    }

    @Test(priority = 3)
    public void checkThatSearchResultsContainsSearchWord() {
        driver.findElement(By.xpath("//input[@id='input_search']")).sendKeys("iPhone 12");
        driver.findElement(By.xpath("//button[@class='button-reset search-btn']")).click();
        List<WebElement> elementList = driver.findElements(By.xpath("//div[@class='prod-cart__descr']"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        for (WebElement webElement : elementList) {
            assertTrue(webElement.getText().contains("iPhone"));
        }
    }

    @Test(priority = 4)
    public void checkAddToCart() {
        driver.findElement(By.xpath("//header/div[2]/div[1]/div[2]/span[1]")).click();
        driver.findElement(By.xpath("//span[contains(text(),'Game Zone')]")).click();
        driver.findElement(By.xpath("(//a[@href='https://avic.ua/gejmerskie-kresla'][contains(.,'Геймерские кресла')])[3]")).click();
        new WebDriverWait(driver, 30).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        driver.findElement(By.xpath("//i[contains(@class,'icon icon-shopping-cart')]")).click();
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("js_cart")));
        driver.findElement(By.xpath("//a[@href='#'][contains(.,'Продолжить покупки')]")).click();
        String actualProductsCountInCart =
                driver.findElement(By.xpath("(//div[contains(@class,'count')])[2]"))
                        .getText();
        assertEquals(actualProductsCountInCart, "1");
    }


    @AfterMethod
    public void tearDown() {
        driver.close();
    }

}
