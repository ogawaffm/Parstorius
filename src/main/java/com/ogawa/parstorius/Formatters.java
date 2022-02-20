/*
 * Copyright (c) 2020-2022 Kai Bächle
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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