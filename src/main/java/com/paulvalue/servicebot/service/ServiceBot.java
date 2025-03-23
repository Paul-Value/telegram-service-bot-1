package com.paulvalue.servicebot.service;

import com.paulvalue.servicebot.model.Category;
import com.paulvalue.servicebot.model.Favor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.core.io.support.SpringFactoriesLoader.FailureHandler.handleMessage;

@Component
@RequiredArgsConstructor
public class ServiceBot extends TelegramLongPollingBot {
    private final CatalogService catalogService;
    private final OrderService orderService;
    private final UserStateService userStateService;

    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return "ServiceCatalogBot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
    }

    private void handleCallback(CallbackQuery callback) {
        String data = callback.getData();
        Long chatId = callback.getMessage().getChatId();

        if (data.startsWith("category_")) {
            Long categoryId = Long.parseLong(data.split("_")[1]);
            showCategoryMenu(chatId, categoryId);
        } else if (data.startsWith("service_")) {
            Long serviceId = Long.parseLong(data.split("_")[1]);
            startOrderProcess(chatId, serviceId);
        }
    }

    private void showCategoryMenu(Long chatId, Long categoryId) {
        List<Category> subCategories = catalogService.getSubCategories(categoryId);
        List<Favor> favors = catalogService.getServicesByCategory(categoryId);

        // Генерация инлайн-кнопок
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        subCategories.forEach(cat ->
                buttons.add(createButton(cat.getName(), "category_" + cat.getId())));

        favors.forEach(service ->
                buttons.add(createButton(service.getTitle(), "service_" + service.getId())));

        sendMessageWithKeyboard(chatId, "Выберите:", buttons);
    }
}
