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
    /**
     * Test Scenario: Validate successful user registration
     * Steps:
     * 1. Navigate to the registration page
     * 2. Fill in the registration form with valid user information
     * 3. Submit the form
     * Expected Outcome:
     * - A success message should be displayed indicating that the user account was created successfully.
     */
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

    /**
     * Test Scenario: Validate unsuccessful user login with incorrect credentials
     * Steps:
     * 1. Navigate to the login page
     * 2. Enter incorrect username and password
     * 3. Submit the login form
     * Expected Outcome:
     * - An error message should be displayed indicating login failure.
     */
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

    /**
     * Test Scenario: Validate successful user login with valid credentials
     * Steps:
     * 1. Navigate to the login page
     * 2. Enter correct username and password
     * 3. Submit the login form
     * Expected Outcome:
     * - A success message should be displayed indicating successful login.
     */
    @Test(priority = 3)
    public void loginSuccess() {
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
    /**
     * Test Scenario: Validate successful opening of a new account
     * Steps:
     * 1. Navigate to the new account page
     * 2. Select account type and amount
     * 3. Submit the new account form
     * Expected Outcome:
     * - A success message should be displayed indicating the account is open.
     */
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
    /**
     * Test Scenario: Validate successful fund transfer between accounts
     * Steps:
     * 1. Navigate to the fund transfer page
     * 2. Enter transfer details and amount
     * 3. Submit the transfer form
     * Expected Outcome:
     * - A success message should be displayed indicating the completion of the fund transfer.
     */
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
    /**
     * Test Scenario: Validate successful bill payment
     * Steps:
     * 1. Navigate to the bill payment page
     * 2. Enter payee details, account information, and amount
     * 3. Submit the bill payment form
     * Expected Outcome:
     * - A success message should be displayed indicating the completion of the bill payment.
     */
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
    /**
     * Test Scenario: Validate accurate retrieval of transactions
     * Steps:
     * 1. Navigate to the transaction search page
     * 2. Enter search criteria and initiate the search
     * Expected Outcome:
     * - The system should display transactions matching the specified criteria.
     */
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
