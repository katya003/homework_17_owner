package helpers.extensions;


import api.AccountApi;
import data.AuthData;
import models.LoginResponseModel;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;

public class LoginExtension implements BeforeEachCallback {
    public static LoginResponseModel cookies;


    @Override
    public void beforeEach(ExtensionContext context) {

        cookies = AccountApi.getAuthorizationCookie();

        step("Добавить cookie (в ответе) из браузера", () -> {
            open("/favicon.ico");
            getWebDriver().manage().addCookie(new Cookie("userID", cookies.getUserId()));
            getWebDriver().manage().addCookie(new Cookie("expires", cookies.getExpires()));
            getWebDriver().manage().addCookie(new Cookie("token", cookies.getToken()));
        });

        step("Проверить успешный вход в учетную запись", () -> {
                    open("/profile");
                    $("#userName-value").shouldBe(visible);
                    $("#userName-value").shouldHave(text(AuthData.USER_NAME));
                }
        );
    }
}
