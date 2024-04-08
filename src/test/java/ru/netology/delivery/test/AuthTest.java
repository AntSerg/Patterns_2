package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.delivery.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.delivery.data.DataGenerator.Registration.getUser;
import static ru.netology.delivery.data.DataGenerator.getRandomLogin;
import static ru.netology.delivery.data.DataGenerator.getRandomPass;

public class AuthTest {
    @BeforeEach
    public void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Success login and active registered user")
    public void successLogIfRegisteredActiveUser () {
        var regUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(regUser.getLogin());
        $("[data-test-id='password'] input").setValue(regUser.getPassword());
        $("button.button").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Negative login and active unregistered user")
    public void negativeLogIfUnRegisteredActiveUser () {
        var unRegUser = getUser("active");
        $("[data-test-id='login'] input").setValue(unRegUser.getLogin());
        $("[data-test-id='password'] input").setValue(unRegUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Positive login and blocked unregistered user")
    public void positiveLogIfRegisteredBlockedUser () {
        var regUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(regUser.getLogin());
        $("[data-test-id='password'] input").setValue(regUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Negative login, positive password and active registered user")
    public void negativeLoginPositivePasswordRegisteredActiveUser () {
        var regUser = getRegisteredUser("active");
        var wrongLog = getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLog);
        $("[data-test-id='password'] input").setValue(regUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Positive login, negative password and active registered user")
    public void positiveLoginNegativePasswordRegisteredActiveUser () {
        var regUser = getRegisteredUser("active");
        var wrongPass = getRandomPass();
        $("[data-test-id='login'] input").setValue(regUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPass);
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

}
