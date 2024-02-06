package testcase.web;

import base.BaseTest;
import com.google.common.base.Verify;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.BadgeRequestPage;

import javax.swing.*;
import java.net.MalformedURLException;
import java.time.Duration;

public class TestBadgeRequest extends BaseTest {
    String pageUrl;
    BadgeRequestPage badgeRequestPage;

    @BeforeClass
    public void classSetup() throws MalformedURLException {
        pageUrl = getPageUrl("home") + "app/badge-request";
        driver.get(pageUrl);
        badgeRequestPage = new BadgeRequestPage(driver);
    }

    @Test(priority = 1)
    public void TC_VerifyScrollableList() {
        SAssert = new SoftAssert();
        try {
            //STEP:1 - Verify presence of scrollable badge labels
            SAssert.assertFalse(badgeRequestPage.getScrollableBadges().isEmpty());

            //STEP:2 - Verify scrollable badge label text
            badgeRequestPage.getScrollableBadges().forEach(badge -> SAssert.assertNotNull(badge.getText()));

            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 2)
    public void TC_VerifyGetBadgeProcess() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String expectedBadgePageUrl = pageUrl.concat("/selection?");
        try {
            badgeRequestPage.getGetBadgeButton().click();
            wait.until(ExpectedConditions.urlContains("selection"));
            String currentUrl = driver.getCurrentUrl();
            Assert.assertEquals(expectedBadgePageUrl, currentUrl);
        } catch (Exception error) {
            failedTest(error);
        }
    }
}
