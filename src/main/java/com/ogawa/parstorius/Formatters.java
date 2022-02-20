package com.ogawa.parstorius;

import java.util.LinkedHashMap;

public class Formatters {

    private LinkedHashMap<String, Formatter> allFormatters = null;
    private StringBuilder stringBuilder;

    Formatters() {

        init();

    }

    Formatters(Formatters formatters) {

        init();

        // put all existing formatters
        allFormatters.putAll(formatters.allFormatters);

    }

    private void init() {

        stringBuilder = new StringBuilder();
        allFormatters = new LinkedHashMap<String, Formatter>();

    }

    public Formatters put(Formatter formatter) {

        Formatters formatters = new Formatters(this);

        // put passed formatter
        formatters.allFormatters.put(formatter.getClass().getName(), formatter);

        return formatters;

    }

    public Formatter getForClass(String className) {

        if (className == null || className.isEmpty()) {

            return null;

        }

        Formatter formatter = allFormatters.get(className);

        // Not found?
        if (formatter == null) {

            try {

                Class<?> clazz = Class.forName(className);

                if (clazz.isAssignableFrom(java.lang.CharSequence.class)) {

                    formatter = allFormatters.get(java.lang.String.class.getClass().getName());

                }

/*
                if (clazz.isAssignableFrom(org.apache.velocity.runtime.directive.Define.class)) {

                    formatter = allFormatters.get(java.lang.String.class.getClass().getName());

                }
*/
            } catch (ClassNotFoundException e) {

                // keep null

            }
        }

        return formatter;

    }

    public Formatter getForObject(String className) {

return null;

    }

    @Override
    public String toString() {


        stringBuilder.setLength(0); // reset is faster than to instantiate

        stringBuilder.append("Formatters with " + allFormatters.size() + " formats\n");

        allFormatters.forEach((className,formatter) ->
                        stringBuilder.append("  ").append(formatter.toString()).append("\n")
        );

        return stringBuilder.toString();

    }
}