/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.beanutils2.converters;

/**
 * {@link org.apache.commons.beanutils2.Converter} implementation that handles conversion
 * to and from <b>java.lang.Character</b> objects.
 * <p>
 * Can be configured to either return a <i>default value</i> or throw a
 * {@code ConversionException} if a conversion error occurs.
 *
 * <p>
 *     This converter may also accept hexadecimal {@link String}s if obtaining
 *     a character from it's numeric value is desired.
 *
 *     Intended for cases where there are concerns regarding the environment,
 *     such as system/file encodings between clients, applications, and servers.
 * </p>
 *
 * @since 1.3
 */
public final class CharacterConverter extends AbstractConverter {

    /** Determines if an input is a hexadecimal {@link String}. */
    private static final String HEX_PREFIX = "0x";

    /**
     * Constructs a <b>java.lang.Character</b> <i>Converter</i> that throws
     * a {@code ConversionException} if an error occurs.
     */
    public CharacterConverter() {
    }

    /**
     * Constructs a <b>java.lang.Character</b> <i>Converter</i> that returns
     * a default value if an error occurs.
     *
     * @param defaultValue The default value to be returned
     * if the value to be converted is missing or an error
     * occurs converting the value.
     */
    public CharacterConverter(final Object defaultValue) {
        super(defaultValue);
    }

    /**
     * Gets the default type this {@code Converter} handles.
     *
     * @return The default type this {@code Converter} handles.
     * @since 1.8.0
     */
    @Override
    protected Class<?> getDefaultType() {
        return Character.class;
    }

    /**
     * <p>Converts a java.lang.Class or object into a String.</p>
     *
     * @param value The input value to be converted
     * @return the converted String value.
     * @since 1.8.0
     */
    @Override
    protected String convertToString(final Object value) {
        final String strValue = value.toString();
        return strValue.isEmpty() ? "" : strValue.substring(0, 1);
    }

    /**
     * <p>Converts the input object into a java.lang.Character.</p>
     *
     * @param <T> Target type of the conversion.
     * @param type Data type to which this value should be converted.
     * @param value The input value to be converted.
     * @return The converted value.
     * @throws Exception if conversion cannot be performed successfully
     * @since 1.8.0
     */
    @Override
    protected <T> T convertToType(final Class<T> type, final Object value) throws Exception {
        if (Character.class.equals(type) || Character.TYPE.equals(type)) {
            final String stringValue = toString(value);

            if (stringValue.isEmpty()) {
                throw new IllegalArgumentException("Value can't be empty.");
            }

            if (stringValue.length() > 2 && stringValue.substring(0, 2).equalsIgnoreCase(HEX_PREFIX)) {
                final String substring = stringValue.substring(HEX_PREFIX.length());
                final int hex = Integer.parseInt(substring, 16);
                return type.cast((char) hex);
            }

            return type.cast(stringValue.charAt(0));
        }

        throw conversionException(type, value);
    }

}
