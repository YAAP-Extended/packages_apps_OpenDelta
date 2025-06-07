/*
 *  Copyright (C) 2025 Yet Another AOSP Extended Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package eu.chainfire.opendelta;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SourceForgeMirrors {
    private static final String TAG = "SourceForgeMirrors";
    private static final String PREF_SELECTED_MIRROR = "selected_mirror";
    private static final String PREF_AUTO_SELECT_MIRROR = "auto_select_mirror";

    public static class Mirror {
        public String name;
        public String country;
        public String code;

        public Mirror(String name, String country, String code) {
            this.name = name;
            this.country = country;
            this.code = code;
        }
    }

    private static final List<Mirror> AVAILABLE_MIRRORS = new ArrayList<Mirror>() {{
        add(new Mirror("Autoselect", "Auto", "autoselect"));
        add(new Mirror("United States", "US", "us"));
        add(new Mirror("United Kingdom", "UK", "uk"));
        add(new Mirror("Germany", "DE", "de"));
        add(new Mirror("France", "FR", "fr"));
        add(new Mirror("Netherlands", "NL", "nl"));
        add(new Mirror("Japan", "JP", "jp"));
        add(new Mirror("Australia", "AU", "au"));
        add(new Mirror("Brazil", "BR", "br"));
        add(new Mirror("Canada", "CA", "ca"));
        add(new Mirror("China", "CN", "cn"));
        add(new Mirror("India", "IN", "in"));
        add(new Mirror("Italy", "IT", "it"));
        add(new Mirror("Russia", "RU", "ru"));
        add(new Mirror("South Korea", "KR", "kr"));
        add(new Mirror("Spain", "ES", "es"));
    }};

    public static List<Mirror> getAvailableMirrors() {
        return AVAILABLE_MIRRORS;
    }

    public static String getMirrorUrl(String baseUrl, Context context) {
        if (!baseUrl.contains("sourceforge.net")) {
            return baseUrl;
        }

        String mirrorCode;
        if (isAutoSelectMirror(context)) {
            String deviceLocale = context.getResources().getConfiguration().locale.toString();
            String country = deviceLocale.split("_")[1];
            mirrorCode = findMirrorCodeByCountry(country);
        } else {
            String selectedMirror = getSelectedMirror(context);
            mirrorCode = selectedMirror != null ? selectedMirror : "autoselect";
        }

        return baseUrl + (baseUrl.contains("?") ? "&" : "?") + "use_mirror=" + mirrorCode;
    }

    private static String findMirrorCodeByCountry(String country) {
        for (Mirror mirror : AVAILABLE_MIRRORS) {
            if (mirror.country.equalsIgnoreCase(country)) {
                return mirror.code;
            }
        }
        return "autoselect";
    }

    public static void setSelectedMirror(Context context, String mirrorCode) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(PREF_SELECTED_MIRROR, mirrorCode).apply();
    }

    public static String getSelectedMirror(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREF_SELECTED_MIRROR, "autoselect");
    }

    public static void setAutoSelectMirror(Context context, boolean autoSelect) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(PREF_AUTO_SELECT_MIRROR, autoSelect).apply();
    }

    public static boolean isAutoSelectMirror(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(PREF_AUTO_SELECT_MIRROR, true);
    }
}
