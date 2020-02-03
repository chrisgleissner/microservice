package com.github.chrisgleissner.microservice.thorntail;

@RunWith(Arquillian.class)
public class JPAApplicationIT extends AbstractIntegrationTest {

    @Drone
    WebDriver browser;

    @Test
    public void testIt() {
        browser.navigate().to("http://localhost:8080/");
        assertThat(browser.getPageSource()).contains("Penny");
        assertThat(browser.getPageSource()).contains("Sheldon");
        assertThat(browser.getPageSource()).contains("Amy");
        assertThat(browser.getPageSource()).contains("Priya");
    }
}