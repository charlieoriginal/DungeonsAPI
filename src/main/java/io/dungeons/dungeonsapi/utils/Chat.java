package io.dungeons.dungeonsapi.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat {
    private static Pattern hex = Pattern.compile("#[0-9a-fA-F]{6}");
    private static Pattern fix2 = Pattern.compile("\\{#[0-9a-fA-F]{6}\\}");
    private static Pattern fix3 = Pattern.compile("\\&x[\\&0-9a-fA-F]{12}");
    private static Pattern gradient1 = Pattern.compile("<#[0-9a-fA-F]{6}>[^<]*</#[0-9a-fA-F]{6}>");
    private static Pattern gradient2 = Pattern.compile("\\{#[0-9a-fA-F]{6}>\\}[^\\{]*\\{#[0-9a-fA-F]{6}<\\}");

    private static String toChatColor(String hexCode) {
        StringBuilder magic = new StringBuilder("§x");
        char[] var3 = hexCode.substring(1).toCharArray();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; var5++) {
            char c = var3[var5];
            magic.append('§').append(c);
        }

        return magic.toString();
    }

    private static String toHexString(int red, int green, int blue) {
        String s = Integer.toHexString((red << 16) + (green << 8) + blue);
        while (s.length() < 6) s = "0" + s;
        return s;
    }

    public static String toChatColorString(String textInput) {
        String text = applyFormats(textInput);
        Matcher m = hex.matcher(text);
        while (m.find()) {
            String hexcode = m.group();
            text = text.replace(hexcode, toChatColor(hexcode));
        }
        return text;
    }

    public static BaseComponent[] getComponent(String textInput) {
        return TextComponent.fromLegacyText(toChatColorString(textInput));
    }

    private static String applyFormats(String textInput) {
        String text = textInput;
        text = fixFormat1(text);
        text = fixFormat2(text);
        text = fixFormat3(text);
        text = setGradient1(text);
        text = setGradient2(text);
        return text;
    }

    private static String fixFormat1(String textInput) {
        return textInput.replace("&#", "#");
    }

    private static String fixFormat2(String textInput) {
        String text = textInput;
        Matcher matcher = fix2.matcher(text);
        while (matcher.find()) {
            String hexCode = matcher.group();
            String fixed = hexCode.substring(2, 8);
            text = text.replace(hexCode, "#" + fixed);
        }
        return text;
    }

    private static String fixFormat3(String textInput) {
        String text = textInput;
        text = text.replace('\u00a7', '&');
        Matcher m = fix3.matcher(text);
        while (m.find()) {
            String hexcode = m.group();
            String fixed = new StringBuilder().append(hexcode.toCharArray()[3]).append(hexcode.toCharArray()[5]).append(hexcode.toCharArray()[7]).append(hexcode.toCharArray()[9]).append(hexcode.toCharArray()[11]).append(hexcode.toCharArray()[13]).toString();
            text = text.replace(hexcode, "#" + fixed);
        }
        return text;
    }

    private static String setGradient1(String input) {
        String text = input;
        Matcher m = gradient1.matcher(text);
        while (m.find()) {
            String format = m.group();
            TextColor start = new TextColor(Integer.parseInt(format.substring(2, 8), 16));
            String message = format.substring(9, format.length() - 10);
            TextColor end = new TextColor(Integer.parseInt(format.substring(format.length()-7, format.length()-1), 16));
            String applied = asGradient(start, message, end);
            text = text.replace(format, applied);
        }
        return text;
    }

    private static String setGradient2(String input) {
        String text = input;
        Matcher m = gradient2.matcher(text);
        while (m.find()) {
            String format = m.group();
            TextColor start = new TextColor(Integer.parseInt(format.substring(2, 8), 16));
            String message = format.substring(10, format.length() - 10);
            TextColor end = new TextColor(Integer.parseInt(format.substring(format.length()-8, format.length()-2), 16));
            String applied = asGradient(start, message, end);
            text = text.replace(format, applied);
        }
        return text;
    }

    private static String asGradient(TextColor start, String text, TextColor end) {
        StringBuilder sb = new StringBuilder();
        int length = text.length();
        for (int i = 0; i < length; i++) {
            float ratio = (float) i / (float) length;
            int red = (int) (start.r * ratio + end.r * (1 - ratio));
            int green = (int) (start.g * ratio + end.g * (1 - ratio));
            int blue = (int) (start.b * ratio + end.b * (1 - ratio));

            sb.append("#" + toHexString(red, green, blue) + text.toCharArray()[i]);
        }
        return sb.toString();
    }

    static class TextColor {
        public int r;
        public int g;
        public int b;
        public TextColor(int hexColor) {
            this.r = (hexColor >> 16) & 0xFF;
            this.g = (hexColor >> 8) & 0xFF;
            this.b = hexColor & 0xFF;
        }
    }
}


