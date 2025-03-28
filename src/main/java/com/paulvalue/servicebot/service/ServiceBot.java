package com.paulvalue.servicebot.service;

import com.paulvalue.servicebot.model.Category;
import com.paulvalue.servicebot.model.Favor;
import com.paulvalue.servicebot.model.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ServiceBot extends TelegramLongPollingBot {
    private final CatalogService catalogService;
    private final OrderService orderService;
    private final UserStateService userStateService;

    private Long lastServiceId;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username}")
    private String botUsername;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasCallbackQuery()) {
                handleCallback(update.getCallbackQuery());
            } else if (update.hasMessage() && update.getMessage().hasText()) {
                handleMessage(update.getMessage());
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void startOrderProcess(Long chatId, Long serviceId) throws TelegramApiException {
        userStateService.setUserState(chatId, "AWAITING_PHONE");
        this.lastServiceId = serviceId;
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Введите ваш телефон:");
        execute(message);
    }

    // Реализация handleMessage
    private void handleMessage(org.telegram.telegrambots.meta.api.objects.Message message) throws TelegramApiException {
        Long chatId = message.getChatId();
        String text = message.getText();
        String userState = userStateService.getCurrentState(chatId);

        if ("AWAITING_PHONE".equals(userState)) {
            // Обработка ввода телефона
            OrderRequest order = orderService.createOrder(lastServiceId, text);
            sendMessage(chatId, "Заявка оформлена!");
            userStateService.setUserState(chatId, "DEFAULT");
        } else if (text.equals("/start")) {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId.toString());
            msg.setText("Добро пожаловать!");
            execute(msg);
        }
    }

    private void sendMessage(Long chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        execute(message);
    }

    // Генерация кнопок
    private InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    private void handleCallback(CallbackQuery callback) throws TelegramApiException {
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

    // Отправка сообщения с клавиатурой
    private void sendMessageWithKeyboard(Long chatId, String text, List<InlineKeyboardButton> buttons) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        buttons.forEach(button -> {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            keyboard.add(row);
        });

        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);

        execute(message);
    }

    private void showCategoryMenu(Long chatId, Long categoryId) throws TelegramApiException {
        List<Category> subCategories = catalogService.getSubCategories(categoryId);
        List<Favor> favors = catalogService.getServicesByCategory(categoryId);

        // Генерация инлайн-кнопок
        List<InlineKeyboardButton> buttons = new ArrayList<>();

        subCategories.forEach(cat -> buttons.add(createButton(cat.getName(), "category_" + cat.getId())));

        favors.forEach(service -> buttons.add(createButton(service.getTitle(), "service_" + service.getId())));

        sendMessageWithKeyboard(chatId, "Выберите:", buttons);
    }
}
