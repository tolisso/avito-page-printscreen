import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.interactions.Actions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Main {

    private static String inputUrl;
    private static String outputFileName = "page.png";
    private static String outputExtension = "png";

    private static final int height = 3500;
    private static final int width = 1200;

    private static final String bigNumberPrefix = "<div class=\"item-phone-big-number";
    private static final String exitFromBigNumberXpath = "//*[@class=\"b-popup item-popup\"]/span";

    public static void main(String[] args) throws IOException, InterruptedException {

        if (args.length == 0) {
            System.err.println("Wrong number of arguments");
            return;
        }
        switch (args.length) {
            case 3:
                outputExtension = args[2];
            case 2:
                outputFileName = args[1];
            case 1:
                inputUrl = args[0];
        }

        var headlessOptions = new ChromeOptions();
        headlessOptions.addArguments("--headless");
        headlessOptions.addArguments("window-size=" + width + "," + height);
//        headlessOptions.setExperimentalOption("mobileEmulation", "")

	    ChromeDriver driver = new ChromeDriver(headlessOptions);
        Actions action = new Actions(driver);
	    try {
            driver.get(inputUrl);

            var phoneElements = driver.findElementsByXPath("//*[contains(text(), \'Показать телефон\')]");
            WebElement button = null;

            for (int i = 0; i < phoneElements.size(); i++) {
                if (phoneElements.get(i).isDisplayed()) {
                    button = phoneElements.get(i);
                    break;
                }
            }

            Random random = new Random();
            TimeUnit.MILLISECONDS.sleep(1000 + random.nextInt() % 500);

            TimeUnit.MILLISECONDS.sleep(random.nextInt() % 2000 + 8000);
            action.moveToElement(button).perform();
            TimeUnit.MILLISECONDS.sleep(1000 + random.nextInt() % 500);
            button.click();

            do {
                TimeUnit.MILLISECONDS.sleep(2000 + random.nextInt() % 500);
            } while (getUrlsOfNumbersImages(driver.getPageSource(), bigNumberPrefix).isEmpty());

            WebElement exit;
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(2000 + random.nextInt() % 500);
                    exit = driver.findElementByXPath(exitFromBigNumberXpath);
                    break;
                } catch (NoSuchElementException exc) {

                }
            }

            exit.click();
            TimeUnit.MILLISECONDS.sleep(3000 + random.nextInt() % 500);

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage bufferedImage = ImageIO.read(screenshot);
            File outputfile = new File(outputFileName);
            ImageIO.write(bufferedImage, outputExtension, outputfile);
        } finally {
	        driver.close();
        }
    }

    private static List<String> getUrlsOfNumbersImages(String source, String imgClass) {
        ArrayList<String> ans = new ArrayList<>();
        outer : for (int i = 0; i < source.length() - imgClass.length(); i++) {
            for (int j = 0; j < imgClass.length(); j++) {
                if (source.charAt(i + j) != imgClass.charAt(j)) {
                    continue outer;
                }
            }
            int start = i + imgClass.length();
            final String urlPref = "src=\"";
            while (!checkSubstr(source, urlPref, start)) {
                start++;
            }
            start += urlPref.length();
            int end = start;
            while (source.charAt(end) != '\"') {
                end++;
            }
            ans.add(source.substring(start, end));
        }
        return ans;
    }

    private static boolean checkSubstr(String source, String substr, int from) {
        for (int i = 0; i < substr.length(); i++) {
            if (source.charAt(i + from) != substr.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}


