package com.walterlauk.utils

import java.util.regex.Pattern

class ValidationUtils {
    companion object {
        fun checkEmail(email: String?): Boolean {
            val regex =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(email)
            return !matcher.matches()
        }

        fun checkEquals(stringOne: String?, stringTwo: String?): Boolean {
            //return !StringUtils.equals(stringOne, stringTwo)
            return !stringOne.equals(stringTwo)
        }
    }
}