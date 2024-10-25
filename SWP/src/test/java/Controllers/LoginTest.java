/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author thaii
 */
public class LoginTest {

    static WebDriver driver;
    static WebDriverWait wait;

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "E:/University/FA24/SWT/chromedriver-win64/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, 10); // Đợi tối đa 10 giây cho đến khi element xuất hiện 
        driver.get("http://localhost:8080/HomePageController/Login");
    }

    @Test
    public void testInvalidEmail() {
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("email")).sendKeys("kienlgce@gmail");
        driver.findElement(By.id("password")).sendKeys("Lgk1412pvzz.");
        driver.findElement(By.id("terms")).click();
        WebElement checkbox = driver.findElement(By.id("terms"));
        if (!checkbox.isSelected()) {
            checkbox.click(); // Chọn checkbox nếu chưa được chọn
        }

        driver.findElement(By.name("loginBtn")).click();

        WebElement emailError = driver.findElement(By.id("emailError"));
        String actualError = emailError.getText();
        String expectedError = "Invalid email.";

        // Thêm thông báo tùy chỉnh nếu test fail
        Assert.assertEquals(actualError, expectedError);
    }

    @Test
    public void testShortPassword() {
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("email")).sendKeys("kienlgce181396@fpt.edu.vn");
        driver.findElement(By.id("password")).sendKeys("Lg1!");
        driver.findElement(By.id("terms")).click();
        WebElement checkbox = driver.findElement(By.id("terms"));
        if (!checkbox.isSelected()) {
            checkbox.click(); // Bỏ chọn nếu checkbox đã được chọn
        }
        driver.findElement(By.name("loginBtn")).click();

        WebElement passwordError = driver.findElement(By.id("passwordError"));
        Assert.assertEquals(passwordError.getText(), "Password must be 6-32 characters, at least 1 uppercase letter, 1 number and 1 special character.");
    }

    @Test
    public void testPasswordWithoutSpecialChar() {
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("email")).sendKeys("kienlgce181396@fpt.edu.vn");
        driver.findElement(By.id("password")).sendKeys("Lgk1412pvzz");
        driver.findElement(By.id("terms")).click();
        WebElement checkbox = driver.findElement(By.id("terms"));
        if (!checkbox.isSelected()) {
            checkbox.click(); // Bỏ chọn nếu checkbox đã được chọn
        }
        driver.findElement(By.name("loginBtn")).click();

        WebElement passwordError = driver.findElement(By.id("passwordError"));
        Assert.assertEquals(passwordError.getText(), "Password must be 6-32 characters, at least 1 uppercase letter, 1 number and 1 special character.");
    }

    @Test
    public void testPasswordWithoutUppercase() {
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("email")).sendKeys("kienlgce181396@fpt.edu.vn");
        driver.findElement(By.id("password")).sendKeys("lgk1412pvzz.");
        driver.findElement(By.id("terms")).click();
        WebElement checkbox = driver.findElement(By.id("terms"));
        if (!checkbox.isSelected()) {
            checkbox.click(); // Bỏ chọn nếu checkbox đã được chọn
        }
        driver.findElement(By.name("loginBtn")).click();

        WebElement passwordError = driver.findElement(By.id("passwordError"));
        Assert.assertEquals(passwordError.getText(), "Password must be 6-32 characters, at least 1 uppercase letter, 1 number and 1 special character.");
    }

    @Test
    public void testPasswordWithoutNumber() {
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("email")).sendKeys("kienlgce181396@fpt.edu.vn");
        driver.findElement(By.id("password")).sendKeys("Lgkpvzz.");
        driver.findElement(By.id("terms")).click();
        WebElement checkbox = driver.findElement(By.id("terms"));
        if (!checkbox.isSelected()) {
            checkbox.click(); // Bỏ chọn nếu checkbox đã được chọn
        };
        driver.findElement(By.name("loginBtn")).click();

        WebElement passwordError = driver.findElement(By.id("passwordError"));
        Assert.assertEquals(passwordError.getText(), "Password must be 6-32 characters, at least 1 uppercase letter, 1 number and 1 special character.");
    }

    @Test
    public void testEmptyPassword() {
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("email")).sendKeys("kienlgce181396@fpt.edu.vn");
        driver.findElement(By.id("password")).sendKeys("");
        driver.findElement(By.id("terms")).click();
        WebElement checkbox = driver.findElement(By.id("terms"));
        if (!checkbox.isSelected()) {
            checkbox.click(); // Bỏ chọn nếu checkbox đã được chọn
        }
        driver.findElement(By.name("loginBtn")).click();

        WebElement checkboxError = driver.findElement(By.id("password"));
        Assert.assertTrue(checkboxError.isDisplayed());
    }

    // Test case mới: Email null
    @Test
    public void testNullEmail() {
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("email")).sendKeys("");
        driver.findElement(By.id("password")).sendKeys("Lgk1412pvzz.");
        driver.findElement(By.id("terms")).click();
        WebElement checkbox = driver.findElement(By.id("terms"));
        if (!checkbox.isSelected()) {
            checkbox.click(); // Bỏ chọn nếu checkbox đã được chọn
        }
        driver.findElement(By.name("loginBtn")).click();

        WebElement checkboxError = driver.findElement(By.id("email"));
        Assert.assertTrue(checkboxError.isDisplayed());
    }

    // Test case mới: Email và mật khẩu đúng nhưng checkbox chưa chọn
    @Test
    public void testCorrectCredentialsButUncheckedCheckbox() {
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("email")).sendKeys("kienlgce181396@fpt.edu.vn");
        driver.findElement(By.id("password")).sendKeys("Lgk1412pvzz.");
        WebElement checkbox = driver.findElement(By.id("terms"));

        if (checkbox.isSelected()) {
            checkbox.click(); // Bỏ chọn checkbox nếu nó đã được chọn
        }

        driver.findElement(By.name("loginBtn")).click();

        WebElement checkboxError = driver.findElement(By.id("terms"));
        Assert.assertTrue(checkboxError.isDisplayed()); // Kiểm tra rằng không chọn checkbox sẽ không cho phép đăng nhập
    }

    @AfterClass
    public static void tearDown() { // Thêm static
        driver.quit(); // Đóng trình duyệt sau khi test xong
    }

}
