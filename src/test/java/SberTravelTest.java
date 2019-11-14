import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.StringEndsWith.endsWith;

public class SberTravelTest {
    static WebDriver driver;
    static String baseUrl;

    @BeforeAll
    public static void initial(){
        String brauser = "chrome";
        switch (brauser) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
                driver = new ChromeDriver();
                break;
            case "firefox":
                FirefoxOptions options = new FirefoxOptions();
                options.setBinary("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
                driver = new FirefoxDriver(options);
                break;
            }
        baseUrl = "https://www.sberbank.ru/ru/person/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }


    @ParameterizedTest
    @ValueSource(strings = {"Tsyrkunov Martin 01102000 Цыркунов Мартын Титович 01112000 4040 151617 20112018 УФМС",
                            "Kuzenkov Yevdokim 02102000 Кузинков Евдоким Андреевич 02112000 4042 251617 22112018 УФМС 2",
                            "Cherenchikova Valentina 03102000 Черенчикова Валентина Брониславовна 03112000 4043 351617 23112018 УФМС 3"})
    public void testInsurance(String date) {
        driver.get(baseUrl);
        Wait<WebDriver> wait = new WebDriverWait(driver, 5, 1000);

        driver.findElement(By.xpath("//button[@aria-label='Меню Страхование']")).click();
        driver.findElement(By.xpath("//a[text()='Страхование путешественников'] [@class='lg-menu__sub-link']")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h1[text()='Страхование путешественников']"))));

        Assert.assertThat(driver.getTitle(), endsWith("Страхование путешественников"));

        String toURL = driver.findElement(By.xpath("//a[@target='_blank']/img/parent::a")).getAttribute("href");
        driver.navigate().to(toURL);
        //driver.findElement(By.xpath("//a[@target='_blank']/img/parent::a")).click();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("(//h2[text()='Страхование путешественников'])[1]"))));

        driver.findElement(By.xpath("//div[@class='b-form-prog-box b-form-active-box']")).click();
        driver.findElement(By.xpath("//span[text()='Оформить']")).click();

        ArrayList<String> dat = new ArrayList<>(Arrays.asList(date.split(" ")));

        driver.findElement(By.name("insured0_surname")).sendKeys(dat.get(0));
        driver.findElement(By.name("insured0_name")).sendKeys(dat.get(1));
        driver.findElement(By.name("insured0_birthDate")).sendKeys(dat.get(2));
        driver.findElement(By.name("surname")).sendKeys(dat.get(3));
        driver.findElement(By.name("name")).sendKeys(dat.get(4));
        driver.findElement(By.name("middlename")).sendKeys(dat.get(5));
        driver.findElement(By.name("birthDate")).sendKeys(dat.get(6));
        driver.findElement(By.name("passport_series")).sendKeys(dat.get(7));
        driver.findElement(By.name("passport_number")).sendKeys(dat.get(8));
        driver.findElement(By.name("issueDate")).sendKeys(dat.get(9));
        driver.findElement(By.name("issuePlace")).sendKeys(dat.get(10));

        driver.findElement(By.xpath("//span[text()='Продолжить']")).click();

        Assert.assertTrue(driver.findElement(By.xpath("//div[text()='Заполнены не все обязательные поля']")).isDisplayed());

    }


    @AfterAll
    public static void tearDown() {
        driver.quit();
    }


}
