package ph.easyaf.batchobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BatchObject {

    // REGEX = "(?>-(\\w{1,})) (?>\"([\\w\\-:. ]{1,})\")"
    // CHECK FOR DOUBLE
    // "(\\d{1,}\\.\\d{1,})|(\"\\d{1,}\\.\\d{1,}\")"
    private static final String REGEX_NAME = "\\w{1,}",
            REGEX_INT = "\\d{1,}",
            REGEX_DOUBLE = "\\d{1,}\\.\\d{1,}",
            REGEX_STRING = "\"[\\w\\-:. ]{1,}\"",
            REGEX_BOOLEAN = "\"(false|true)\"",
            REGEX_STRING_GET = "\"([\\w\\-:. ]{1,})\"",
            REGEX = "(?>-(\\w{1,})) (?>((\\d{1,}\\.\\d{1,})|(\\d{1,})|(\"([\\w\\-:. ]){1,}\")))";
    private HashMap<String, Object> values = new HashMap<>();

    public BatchObject() {}

    public BatchObject(String batch) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(batch);

        while (matcher.find()) {
            if (Objects.requireNonNull(matcher.group(2)).matches(REGEX_INT)) {
                values.put(matcher.group(1),
                        Integer.parseInt(Objects.requireNonNull(matcher.group(2))));
            } else if (Objects.requireNonNull(matcher.group(2)).matches(REGEX_DOUBLE)) {
                values.put(matcher.group(1),
                        Double.parseDouble(Objects.requireNonNull(matcher.group(2))));
            } else if (Objects.requireNonNull(matcher.group(2)).matches(REGEX_STRING)) {
                if (Objects.requireNonNull(matcher.group(2)).matches(REGEX_BOOLEAN)) {
                    values.put(matcher.group(1),
                            Boolean.parseBoolean(Objects.requireNonNull(matcher.group(2))));
                } else {
                    Matcher m = Pattern.compile(REGEX_STRING_GET).matcher(
                            Objects.requireNonNull(matcher.group(2)));
                    if (m.find()) values.put(matcher.group(1), m.group(1));
                }
            }
            //values.put(matcher.group(1), matcher.group(2));
        }
    }

    public boolean has(String name) {
        return values.containsKey(name);
    }

    public boolean getBoolean(String name) throws BatchObjectException {
        if (values.containsKey(name)) {
            if (values.get(name) instanceof Boolean) return (boolean)values.get(name);
            else throw new BatchObjectException("Value not instance of boolean");
        } else throw new BatchObjectException("No value for " + name);
    }

    public int getInt(String name) throws BatchObjectException {
        if (values.containsKey(name)) {
            if (values.get(name) instanceof Integer) return (int)values.get(name);
            else throw new BatchObjectException("Value not instance of int");
        } else throw new BatchObjectException("No value for " + name);
    }

    public double getDouble(String name) throws BatchObjectException {
        if (values.containsKey(name)) {
            if (values.get(name) instanceof Double) return (double)values.get(name);
            else {
                try {
                    return Double.parseDouble(values.get(name) + "");
                } catch (NumberFormatException e) {
                    throw new BatchObjectException("Value not instance of double");
                }
            }
        } else throw new BatchObjectException("No value for " + name);
    }

    public String getString(String name) throws BatchObjectException {
        if (values.containsKey(name)) {
            return values.get(name) + "";
        } else throw new BatchObjectException("No value for " + name);
    }

    public void put(String name, boolean value) throws BatchObjectException {
        if (name.matches(REGEX_NAME)) values.put(name, value);
        else throw new BatchObjectException("Name must only composed of alphabet and numbers");
    }

    public void put(String name, int value) throws BatchObjectException {
        if (name.matches(REGEX_NAME)) values.put(name, value);
        else throw new BatchObjectException("Name must only composed of alphabet and numbers");
    }

    public void put(String name, double value) throws BatchObjectException {
        if (name.matches(REGEX_NAME)) values.put(name, value);
        else throw new BatchObjectException("Name must only composed of alphabet and numbers");
    }

    public void put(String name, String value) throws BatchObjectException {
        if (name.matches(REGEX_NAME)) values.put(name, value);
        else throw new BatchObjectException("Name must only composed of alphabet and numbers");
    }

    public String toString() {
        String text = "";
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Integer) text += "-" + key + " " + value + " ";
            else if (value instanceof Double) {
                String doubleText = String.format("%.2f", value);
                text += "-" + key + " " + doubleText + " ";
            }
            else if (value instanceof Boolean) text += "-" + key + " \"" + value + "\" ";
            else text += "-" + key + " \"" + value + "\" ";
            //text += "-" + entry.getKey() + " \"" + entry.getValue() + "\" ";
        }
        return text;
    }
}
