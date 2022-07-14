package ru.arkasandr.carfinesearcher.service;import com.microsoft.playwright.BrowserContext;import com.microsoft.playwright.Page;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.stereotype.Service;import ru.arkasandr.carfinesearcher.config.BrowserInitializeConfig;import ru.arkasandr.carfinesearcher.config.GibddInitializeConfig;import ru.arkasandr.carfinesearcher.model.Answer;import ru.arkasandr.carfinesearcher.model.UserData;@Service@Slf4j@RequiredArgsConstructorpublic class GibddScrapService {    private final GibddInitializeConfig gibddInitializeConfig;    private final BrowserInitializeConfig browserInitialize;    public Answer scrap(UserData data) {        BrowserContext context = gibddInitializeConfig.getContext();        Page page = browserInitialize.page(context);        Answer result = new Answer();        try {            log.info("Start scraping process with regNumber: {} and certNumber: {} ", data.getRegNumber(), data.getCertNumber());            result = new Answer(300.5);        } catch (Exception e) {            log.info("ServiceInitialize error: " + e.getMessage());        } finally {            context.browser().close();            log.info("Browser was closed");        }        return result;    }}