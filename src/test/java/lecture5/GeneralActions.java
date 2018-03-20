package lecture5;


import lecture5.model.ProductData;
import lecture5.utils.Properties;
import lecture5.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.Random;

import java.util.List;

import static lecture5.utils.DataConverter.parsePriceValue;
import static lecture5.utils.DataConverter.parseStockValue;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;

    private By submitNewsletterButton = By.name("submitNewsletter");
    private By mobileUserInfo =  By.id("_mobile_user_info");
    private By desktopUserInfo = By.id("_desktop_user_info");

    private By allProductsLink = By.xpath("//div//a[@class='all-product-link pull-xs-left pull-md-right h4']");
    private By thumbnailContainer = By.xpath("//div//h1[@class='h3 product-title']/a");

    private String currentProductURL;
    private By openedProductURL = By.xpath("//div//ol/li/a");
    private String currentProductName;
    private By openedProductName = By.xpath("//div/h1");
    private String currentProductPrice;
    private By openedProductPrice = By.xpath("//div[@class='current-price']/span");
    private By descriptionLink = By.xpath("//div//a[@href='#product-details']");
    private int currentProductInStock;
    private By openedProductInStock = By.xpath("//div[@class='product-quantities']/span");

    private By addToCartButton = By.xpath("//button[@class='btn btn-primary add-to-cart']");
    private By checkoutButton = By.xpath("//div//a[@class='btn btn-primary']");
    private By productLineGrid = By.xpath("//div[@class='product-line-grid']");
    private By productName = By.xpath("//div[@class='product-line-info']/a");
    private By productQty = By.xpath("//input[@class='js-cart-line-product-quantity form-control']");
    private By productPrice = By.xpath("//div/span[@class='product-price']");

    private By orderButton = By.xpath("//div/a[@class='btn btn-primary']");
    private By continueButton = By.xpath("//button[@name='continue']");
    private By inputFirstName = By.xpath("//input[@name='firstname']");
    private By inputLastName = By.xpath("//input[@name='lastname']");
    private By inputEmail = By.xpath("//input[@name='email']");

    private By inputAddress1 = By.xpath("//input[@name='address1']");
    private By inputPostCode = By.xpath("//input[@name='postcode']");
    private By inputCity = By.xpath("//input[@name='city']");
    private By confirmAddressButton = By.xpath("//button[@name='confirm-addresses']");

    private By confirmDeliveryOption = By.xpath("//button[@name='confirmDeliveryOption']");
    private By checkPayment = By.xpath("//input[@id='payment-option-2']");
    private By checkCondition = By.xpath("//input[@id='conditions_to_approve[terms-and-conditions]']");
    private By finalOrderButton = By.xpath("//button[@class='btn btn-primary center-block']");

    private By saveButton = By.xpath("//button[@class='btn btn-primary form-control-submit pull-xs-right']");
    private By confirmMessage = By.xpath("//div/h3[@class='h1 card-title']");
    private By orderLineRow = By.xpath("//div[@class='order-line row']");
    private By orderProductDetails = By.xpath("//div[@class='col-sm-4 col-xs-9 details']");
    private By orderProductPrice = By.xpath("//div[@class='col-xs-5 text-sm-right text-xs-left']");



    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }


    /** Check Site Version */
    public void checkSiteVersion(Boolean isMobileTest) {
        CustomReporter.logAction("Check site version.");
        driver.get(Properties.getBaseUrl());
        waitForContentLoad();
        if (isMobileTest)
            Assert.assertTrue(driver.findElement(mobileUserInfo).isDisplayed(),"Not mobile site version is used.");
        else
            Assert.assertTrue(driver.findElement(desktopUserInfo).isDisplayed(),"Not desktop site version is used." );
    }

    /** Open random product */
    public void openRandomProduct() {
        CustomReporter.logAction("Open random product");
        // TODO implement logic to open random product before purchase
        driver.get(Properties.getBaseUrl());
        waitForContentLoad();
        driver.findElement(allProductsLink).click();

        waitForContentLoad();
        List<WebElement> containers = driver.findElements(thumbnailContainer);
        Random random = new Random();
        containers.get(random.nextInt(containers.size())).click();
    }

    /*** Extracts product information from opened product details page.
     *
     * @return
     */
    public ProductData getOpenedProductInfo() {
        CustomReporter.logAction("Get information about currently opened product");
        // TODO extract data from opened page
        waitForContentLoad();
        List<WebElement> links = driver.findElements(openedProductURL);
        currentProductURL = links.get(links.size()-1).getAttribute("href"); // get last URL
        currentProductName = driver.findElement(openedProductName).getText();
        currentProductPrice = driver.findElement(openedProductPrice).getText();
        driver.findElement(descriptionLink).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(openedProductInStock));
        currentProductInStock = parseStockValue(driver.findElement(openedProductInStock).getText());

        float price = parsePriceValue(currentProductPrice);
        return new ProductData(currentProductName, currentProductInStock, price);
    }

    /*** Extracts product information from opened product details page.
     *
     * @return
     */
    public void addProductToCard() {
        CustomReporter.logAction("Add product to Cart and validate product information in the Cart");
        driver.findElement(addToCartButton).click();
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
        driver.findElement(checkoutButton).click();

        List<WebElement> itemsCount = driver.findElements(productLineGrid);
        Assert.assertEquals(itemsCount.size(), 1, "There is more than one product in the card.");

        Assert.assertEquals(driver.findElement(productName).getText().toUpperCase(), currentProductName, "Product name is not equals.");

        Assert.assertEquals(driver.findElement(productPrice).getText(), currentProductPrice, "Product price is not equals");
    }

    // add product to Cart and validate product information in the Cart
    public void orderCreation(String firstname, String lastname, String email, String address1, String postcode, String city) {
        CustomReporter.logAction("Proceed to order creation, fill required information");
        driver.findElement(orderButton).click();

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(continueButton)));
        driver.findElement(inputFirstName).sendKeys(firstname);
        driver.findElement(inputLastName).sendKeys(lastname);
        driver.findElement(inputEmail).sendKeys(email);
        driver.findElement(continueButton).click();

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(confirmAddressButton)));
        driver.findElement(inputAddress1).sendKeys(address1);
        driver.findElement(inputPostCode).sendKeys(postcode);
        driver.findElement(inputCity).sendKeys(city);
        driver.findElement(confirmAddressButton).click();

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(confirmDeliveryOption)));
        driver.findElement(confirmDeliveryOption).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(finalOrderButton));
        driver.findElement(checkPayment).click();
        driver.findElement(checkCondition).click();
        driver.findElement(finalOrderButton).click();
    }

    // place new order and validate order summary
    public void validateOrderSummary(ProductData product) {
        CustomReporter.logAction("Place new order and validate order summary.");
        wait.until(ExpectedConditions.presenceOfElementLocated(saveButton));

        Boolean confirmMessageIsShown;
        if (driver.findElement(confirmMessage).getText().contains("ВАШ ЗАКАЗ ПОДТВЕРЖДЁН")) confirmMessageIsShown = true;
            else if (driver.findElement(confirmMessage).getText().contains("ВАШЕ ЗАМОВЛЕННЯ ПІДТВЕРДЖЕНО")) confirmMessageIsShown = true;
                else confirmMessageIsShown = false;
        Assert.assertTrue(confirmMessageIsShown, "Confirm messages was not shown.");

        List<WebElement> rows = driver.findElements(orderLineRow);
        Assert.assertEquals(rows.size(), 1 , "There are more than one product in the order.");

        String productDetails = driver.findElement(orderProductDetails).getText();
        String orderProductName;
        if (productDetails.contains("Size"))
            orderProductName = productDetails.substring(0,productDetails.indexOf("Size")-3);
        else                    // test products doesn't content properties "Size" and "Color"
            orderProductName = productDetails;
        Assert.assertEquals(orderProductName.toUpperCase(), product.getName(), "Product names are not equals.");

        Float productPrice = parsePriceValue(driver.findElement(orderProductPrice).getText());
        Assert.assertEquals(productPrice, product.getPrice(), "Product prices are not equals.");
    }

    // check updated In Stock value
    public void chkUpdInStockValue(ProductData product) {
        CustomReporter.logAction("Check updated In Stock value.");
        driver.get(currentProductURL);
        waitForContentLoad();

        driver.findElement(descriptionLink).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(openedProductInStock));

        currentProductInStock = parseStockValue(driver.findElement(openedProductInStock).getText());
        Assert.assertEquals(currentProductInStock, product.getQty()-1, "Product in stock quantities was not changed.");
    }

    /**
     * Waits until page loader disappears from the page
     */
    public void waitForContentLoad() {
        // TODO implement generic method to wait until page content is loaded
        wait.until(ExpectedConditions.presenceOfElementLocated(submitNewsletterButton));
    }

}