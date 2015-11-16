/*
 * Copyright 2015 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.linecorp.armeria.common.util;

import io.netty.buffer.ByteBuf;

/**
 * Formats a duration and a buffer size into a {@link String} or a {@link StringBuilder}.
 */
public final class UnitFormatter {

    /**
     * Appends the number of readable bytes in the specified {@link ByteBuf} to the specified
     * {@link StringBuilder}.
     */
    public static void appendSize(StringBuilder buf, ByteBuf content) {
        if (content != null) {
            final int size = content.readableBytes();
            if (size >= 104857600) { // >= 100 MiB
                buf.append(size / 1048576).append("MiB");
            } else if (size >= 102400) { // >= 100 KiB
                buf.append(size / 1024).append("KiB");
            } else {
                buf.append(size).append('B');
            }
        } else {
            buf.append("null");
        }
    }

    /**
     * Appends the duration between the specified {@code startTimeNanos} and {@code endTimeNanos} to the
     * specified {@link StringBuilder}.
     */
    public static void appendElapsed(StringBuilder buf, long startTimeNanos, long endTimeNanos) {
        final long elapsedNanos = endTimeNanos - startTimeNanos;
        if (elapsedNanos >= 100000000000L) { // >= 100 s
            buf.append(elapsedNanos / 1000000000L).append('s');
        } else if (elapsedNanos >= 100000000L) { // >= 100 ms
            buf.append(elapsedNanos / 1000000L).append("ms");
        } else if (elapsedNanos >= 100000L) { // >= 100 us
            buf.append(elapsedNanos / 1000L).append("\u00B5s");
        } else {
            buf.append(elapsedNanos).append("ns");
        }
    }

    /**
     * A shortcut method that calls {@link #appendElapsed(StringBuilder, long, long)} and
     * {@link #appendSize(StringBuilder, ByteBuf)}, concatenated by {@code ", "}.
     */
    public static void appendElapsedAndSize(StringBuilder buf, long startTimeNanos, long endTimeNanos,
                                            ByteBuf content) {
        appendElapsed(buf, startTimeNanos, endTimeNanos);
        buf.append(", ");
        appendSize(buf, content);
    }

    /**
     * Creates a new {@link StringBuilder} whose content is the duration between the specified
     * {@code startTimeNanos} and {@code endTimeNanos}.
     */
    public static StringBuilder elapsed(long startTimeNanos, long endTimeNanos) {
        final StringBuilder buf = new StringBuilder(16);
        appendElapsed(buf, startTimeNanos, endTimeNanos);
        return buf;
    }

    /**
     * Creates a new {@link StringBuilder} whose content is the number of readable bytes in the specified
     * {@code ByteBuf}.
     */
    public static StringBuilder size(ByteBuf content) {
        final StringBuilder buf = new StringBuilder(16);
        appendSize(buf, content);
        return buf;
    }

    /**
     * Similar to {@link #appendElapsedAndSize(StringBuilder, long, long, ByteBuf)} except that this method
     * creates a new {@link StringBuilder}.
     */
    public static StringBuilder elapsedAndSize(long startTimeNanos, long endTimeNanos, ByteBuf content) {
        final StringBuilder buf = new StringBuilder(16);
        appendElapsedAndSize(buf, startTimeNanos, endTimeNanos, content);
        return buf;
    }

    private UnitFormatter() {}
}
