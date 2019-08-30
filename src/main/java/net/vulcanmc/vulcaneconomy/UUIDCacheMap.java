package net.vulcanmc.vulcaneconomy;

/*
 *   Copyright (C) 2019 GeorgH93
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This map implements a {@link ConcurrentHashMap} to store names case insensitive as key and UUIDs as String without the "-" separator.
 * It's used to cache the UUID's resolved by the {@link UUIDConverter}.
 */
class UUIDCacheMap extends ConcurrentHashMap<String, String>
{
    @Override
    public String put(@NotNull String key, @NotNull String value)
    {
        return super.put(key.toLowerCase(Locale.ROOT), value.replaceAll("-", "").toLowerCase(Locale.ROOT));
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends String> m)
    {
        for(Entry<? extends String, ? extends String> entry : m.entrySet())
        {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean contains(@NotNull Object value)
    {
        return value instanceof String && super.contains(((String) value).replaceAll("-", "").toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean containsKey(@NotNull Object key)
    {
        return key instanceof String && super.containsKey(((String) key).toLowerCase(Locale.ROOT));
    }

    @Override
    public String get(@NotNull Object key)
    {
        return key instanceof String ? super.get(((String) key).toLowerCase(Locale.ROOT)) : null;
    }

    @Override
    public String remove(@NotNull Object key)
    {
        return key instanceof String ? super.remove(((String) key).toLowerCase(Locale.ROOT)) : null;
    }
}