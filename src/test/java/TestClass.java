import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TestClass {
    public String baseUrl = "https://parabank.parasoft.com/parabank/index.htm";
    String driverPath = "./drivers/geckodriver.exe";

    public WebDriver driver;

    @BeforeTest
    public void launchBrowser() {
        System.out.println("launching firefox browser");
        System.setProperty("webdriver.gecko.driver", driverPath);
        driver = new FirefoxDriver();
        driver.get(baseUrl);
    }

    @Test(priority = 1)
    public void registerSuccess() {
        driver.findElement(By.xpath("//*[@id=\"loginPanel\"]/p[2]/a")).click();
        driver.findElement(By.id("customer.firstName")).sendKeys("salam");
        driver.findElement(By.id("customer.lastName")).sendKeys("aa");
        driver.findElement(By.id("customer.address.street")).sendKeys("aa");
        driver.findElement(By.id("customer.address.city")).sendKeys("aa");
        driver.findElement(By.id("customer.address.state")).sendKeys("aa");
        driver.findElement(By.id("customer.address.zipCode")).sendKeys("aa");
        driver.findElement(By.id("customer.address.state")).sendKeys("aa");
        driver.findElement(By.id("customer.phoneNumber")).sendKeys("aa");
        driver.findElement(By.id("customer.ssn")).sendKeys("aa");
        driver.findElement(By.id("customer.username")).sendKeys("salam");
        driver.findElement(By.id("customer.password")).sendKeys("salam1");
        driver.findElement(By.id("repeatedPassword")).sendKeys("salam1");
        driver.findElement(By.xpath("//*[@id=\"customerForm\"]/table/tbody/tr[13]/td[2]/input")).click();
        String actualMessage = "";
        try {
            actualMessage = driver.findElement(By.xpath("//*[@id=\"rightPanel\"]/p")).getText();
        } catch (Exception e) {
        }
        String awaitedMessage = "Your account was created successfully. You are now logged in.";
        Assert.assertEquals(actualMessage, awaitedMessage);
    }

    @Test(priority = 2)
    public void loginFail() throws Exception {
        driver.findElement(By.name("username")).sendKeys("salam1");
        driver.findElement(By.name("password")).sendKeys("salam");
        Thread.sleep(2);
        driver.findElement(By.xpath("//*[@id=\"loginPanel\"]/form/div[3]/input")).submit();
        Thread.sleep(2);
        String msg = null;
        try {
            msg = driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[2]/h1")).getText();
        } catch (Exception e) {
        }
        String errorMsg = "Error!";
        Assert.assertEquals(msg, errorMsg);//if error message is not shown then test should be considered FAILED
    }

    @Test(priority = 3)
    public void loginValidInput() {
        driver.findElement(By.name("username")).sendKeys("salam1");
        driver.findElement(By.name("password")).sendKeys("salam1");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@id=\"loginPanel\"]/form/div[3]/input")).submit();
        String msg = null;
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        try {
            msg = driver.findElement(By.xpath("//*[@id=\"rightPanel\"]/p")).getText();
        } catch (Exception e) {
        }
        String errorMsg = "Error!";
        Assert.assertNotEquals(msg, errorMsg);//it should not return errorMsg to pass
    }

    @Test (priority = 3)
    public void opeNewAccount() throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[1]/ul/li[1]/a")).click();
        Select accounttype = new Select(driver.findElement(By.xpath("//*[@id=\"type\"]")));
        accounttype.selectByIndex(0);
        Thread.sleep(2000);
        Select amount = new Select(driver.findElement(By.xpath("//*[@id=\"fromAccountId\"]")));
        amount.selectByIndex(0);
        driver.findElement(By.xpath("//*[@id=\"rightPanel\"]/div/div/form/div/input")).click();
        Thread.sleep(2000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5));
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        WebElement message=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#rightPanel > div > div > p:nth-child(2)")));
        Assert.assertEquals(message.getText(),"Congratulations, your account is now open.");
    }

    @Test(priority = 4)
    public void transferFunds() throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("#leftPanel > ul > li:nth-child(3) > a")).click();
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofMillis(5));
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        WebElement inputField=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#amount")));
        inputField.sendKeys("100");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"fromAccountId\"]")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"toAccountId\"]")));
        Select fromAccount=new Select(driver.findElement(By.xpath("//*[@id=\"fromAccountId\"]")));
        fromAccount.selectByValue("18672");
        Select toAccount=new Select(driver.findElement(By.xpath("//*[@id=\"toAccountId\"]")));
        toAccount.selectByValue("18783");
        driver.findElement(By.cssSelector("#rightPanel > div > div > form > div:nth-child(4) > input")).click();
        WebElement msg=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#rightPanel > div > div > h1")));
        Assert.assertEquals(msg.getText(),"Transfer Complete!");
    }
    @Test(priority = 5)
    public void BillPay() throws InterruptedException {
        Thread.sleep(2000);
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofMillis(5));
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        WebElement link=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"leftPanel\"]/ul/li[4]/a")));
        link.click();
        Thread.sleep(2000);
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        WebElement payeeName=wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("payee.name")));
        payeeName.sendKeys("Leyla");
        driver.findElement(By.name("payee.address.street")).sendKeys("Baku");
        driver.findElement(By.name("payee.address.city")).sendKeys("Baku");
        driver.findElement(By.name("payee.address.state")).sendKeys("Baku");
        driver.findElement(By.name("payee.address.zipCode")).sendKeys("Baku");
        driver.findElement(By.name("payee.phoneNumber")).sendKeys("998399389");
        driver.findElement(By.name("payee.accountNumber")).sendKeys("7736372");
        driver.findElement(By.name("verifyAccount")).sendKeys("7736372");
        driver.findElement(By.name("amount")).sendKeys("7736372");
        Select fromAccout=new Select(driver.findElement(By.name("fromAccountId")));
        fromAccout.selectByIndex(0);
        driver.findElement(By.className("button")).click();
        Thread.sleep(2000);
        WebElement msg=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"rightPanel\"]/div/div[2]/h1")));
        Assert.assertEquals(msg.getText(),"Bill Payment Complete");
    }
    @Test(priority = 6)
    public void findTransactions() throws InterruptedException {
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofMillis(5));
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        WebElement link=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"leftPanel\"]/ul/li[5]/a")));
        link.click();
        Thread.sleep(2000);
        Select account = new Select(driver.findElement(By.xpath("//*[@id=\"accountId\"]")));
        account.selectByValue("14676");
        driver.findElement(By.xpath("//*[@id=\"criteria.amount\"]")).sendKeys("1000");
        driver.findElement(By.xpath("//*[@id=\"rightPanel\"]/div/div/form/div[9]/button")).click();
    }
    @AfterTest
    public void terminateBrowser() {
        driver.close();
    }

}
