import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
public class AmazonAppTest {
    WebDriver driver;
    WebDriverWait wait;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "chrome_path");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.amazon.com");
    }

    @After
    public void tearDown() {
        driver.quit();
    }
    @Test
    public void testLoginWithValidCredentials() {
        driver.findElement(By.id("nav-link-accountList")).click();
        driver.findElement(By.id("ap_email")).sendKeys("give Email_ID");
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("ap_password")).sendKeys("give password");
        driver.findElement(By.id("signInSubmit")).click();
        WebElement accountName = driver.findElement(By.id("nav-link-accountList-nav-line-1"));
        assertTrue(accountName.isDisplayed());
    }


    @Test
    public void testSearchFunctionality() {
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("laptop");
        driver.findElement(By.id("nav-search-submit-button")).click();
        String title = driver.getTitle();
        assertTrue(title.contains("laptop"));
    }


    @Test
    public void testAddToCart() {
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("laptop");
        driver.findElement(By.id("nav-search-submit-button")).click();
        driver.findElements(By.cssSelector(".s-title")).get(0).click();
        driver.findElement(By.id("add-to-cart-button")).click();
        WebElement confirmationMessage = driver.findElement(By.cssSelector("#huc-v2-order-row-confirm-text"));
        assertTrue(confirmationMessage.isDisplayed());
    }


    @Test
    public void testRemoveFromCart() {
        driver.get("https://www.amazon.com/gp/cart/view.html");
        WebElement deleteButton = driver.findElement(By.cssSelector(".sc-action-delete .a-declarative"));
        if (deleteButton.isDisplayed()) {
            deleteButton.click();
            WebElement emptyCartMessage = driver.findElement(By.cssSelector(".sc-your-amazon-cart-is-empty"));
            assertTrue(emptyCartMessage.isDisplayed());
        }
    }


    @Test
    public void testPaymentWithInvalidCreditCard() {

        driver.get("https://www.amazon.com/gp/cart/view.html");
        driver.findElement(By.name("proceedToRetailCheckout")).click();
        driver.findElement(By.id("addCreditCardNumber")).sendKeys("0000 0000 0000 0000");
        driver.findElement(By.id("addCreditCardExpirationYear")).sendKeys("2025");
        driver.findElement(By.id("addCreditCardSecurityCode")).sendKeys("0000");
        driver.findElement(By.cssSelector("input[type='submit']")).click();
        WebElement errorMessage = driver.findElement(By.className("a-alert-heading"));
        assertTrue(errorMessage.isDisplayed());
    }
    @Test
    public void testProductQuantityUpdate() {
        WebElement quantityInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='quantity']")));
        quantityInput.clear();
        int newQuantity = 3;
        quantityInput.sendKeys(String.valueOf(newQuantity));
        quantityInput.submit();
        wait.until(ExpectedConditions.textToBePresentInElementValue(quantityInput, String.valueOf(newQuantity)));
        String updatedQuantity = quantityInput.getAttribute("value");
        Assert.assertEquals(Integer.parseInt(updatedQuantity), newQuantity, "Quantity update verification failed!");
    }


    @Test
    public void testAddAndRemoveFromWishlist() {
        WebElement addToWishlistButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'add-to-wishlist')]")));
        addToWishlistButton.click();
        WebElement wishlistConfirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='wishlist-confirmation']")));
        Assert.assertTrue(wishlistConfirmation.isDisplayed(), "The item was not successfully added to the wishlist.");
        driver.get("https://example.com/wishlist");
        WebElement wishlistItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='wishlist-item']")));
        Assert.assertTrue(wishlistItem.isDisplayed(), "The item is not present in the wishlist.");
        WebElement removeFromWishlistButton = wishlistItem.findElement(By.xpath(".//button[contains(@class, 'remove-from-wishlist')]"));
        removeFromWishlistButton.click();
        wait.until(ExpectedConditions.invisibilityOf(wishlistItem));
        boolean isItemPresent = driver.findElements(By.xpath("//div[@class='wishlist-item']")).size() > 0;
        Assert.assertFalse(isItemPresent, "The item was not removed from the wishlist.");
    }

    @Test
    public void testSearchWithNoResults() {
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("search")));
        String noResultsSearchTerm = "qwertyuiop";
        searchInput.sendKeys(noResultsSearchTerm);
        searchInput.submit();
        WebElement noResultsMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='no-results-message']")));
        Assert.assertTrue(noResultsMessage.isDisplayed(), "No results message is not displayed.");
        String expectedMessage = "No results found for \"" + noResultsSearchTerm + "\"";
        String actualMessage = noResultsMessage.getText();
        Assert.assertEquals(actualMessage, expectedMessage, "No results message text does not match expected.");
    }
    @Test
    public void testAddAddress() {
        WebElement addNewAddressButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Add New Address']")));
        addNewAddressButton.click();
        WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
        WebElement address = driver.findElement(By.name("address"));
        WebElement city = driver.findElement(By.name("city"));
        WebElement postalCode = driver.findElement(By.name("postalCode"));
        WebElement phoneNumber = driver.findElement(By.name("phoneNumber"));

        name.sendKeys("name");
        address.sendKeys("address");
        city.sendKeys("Sample City");
        postalCode.sendKeys("pincode");
        phoneNumber.sendKeys("555-555-5555");
        WebElement saveButton = driver.findElement(By.xpath("//button[text()='Save']"));
        saveButton.click();
        WebElement addedAddress = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), '123 Main St')]")));
        Assert.assertTrue(addedAddress.isDisplayed(), "Address was not added successfully.");
    }

    @Test
    public void testEditAddress() {
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'edit-address')]")));
        editButton.click();
        WebElement address = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("address")));
        address.clear();
        address.sendKeys("456 Updated St");
        WebElement saveButton = driver.findElement(By.xpath("//button[text()='Save']"));
        saveButton.click();
        WebElement updatedAddress = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), '456 Updated St')]")));
        Assert.assertTrue(updatedAddress.isDisplayed(), "Address was not updated successfully.");
    }

    @Test
    public void testDeleteAddress() {
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'delete-address')]")));
        deleteButton.click();
        WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Confirm']")));
        confirmDeleteButton.click();
        boolean isAddressPresent = driver.findElements(By.xpath("//div[contains(text(), 'address')]")).size() > 0;
        Assert.assertFalse(isAddressPresent, "Address was not deleted successfully.");
    }

    @Test
    public void testLogout() {
        WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='account']")));
        accountButton.click();
        WebElement logoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Logout']")));
        logoutButton.click();
        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));
        Assert.assertTrue(loginButton.isDisplayed(), "Logout was not successful - login button not displayed.");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"), "User is not redirected to the login page.");
    }
    @Test
    public void testForgotPassword() {
        WebElement forgotPasswordLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Forgot Password?")));
        forgotPasswordLink.click();
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        String userEmail = "give mail-id";
        emailInput.sendKeys(userEmail);
        WebElement resetButton = driver.findElement(By.xpath("//button[text()='Send Reset Link']"));
        resetButton.click();
        WebElement confirmationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmationMessage")));
        String expectedMessage = "A password reset link has been sent to " + userEmail;
        String actualMessage = confirmationMessage.getText();
        Assert.assertEquals(actualMessage, expectedMessage, "Password reset confirmation message does not match.");
    }
}
