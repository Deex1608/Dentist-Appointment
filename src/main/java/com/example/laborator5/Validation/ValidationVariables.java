package com.example.laborator5.Validation;

import com.example.laborator5.Exceptions.IllegalVariableType;
import com.example.laborator5.UI.CONSTANTS;

public class ValidationVariables {
    public static boolean hasNumbers(String verificationName){
        for(char character : verificationName.toCharArray()){
            if(Character.isDigit(character)){
                return true;
            }
        }
        return false;
    }

    public static boolean IsNotAGoodId(Integer Id) {
        return Id == null || Id < CONSTANTS.WE_CHECK_WITH_A_ZERO;
    }

    public static boolean hasSpecialCharacters(String verificationName){
        for(char character : verificationName.toCharArray()){
            if(!Character.isLetter(character) && !Character.isDigit(character) && !Character.isWhitespace(character)){
                return true;
            }
        }
        return false;
    }

    public static boolean hasLetters(String verificationName){
        for(char character : verificationName.toCharArray()){
            if(Character.isLetter(character)){
                return true;
            }
        }
        return false;
    }

    public static boolean isAPhoneNumber(String verificationName){
        int plusAppearance = 0, plusPositionAppearance = 0, stringIndex = 0;
        for(char character : verificationName.toCharArray()){
            if(!Character.isDigit(character) && !Character.isWhitespace(character) && !(character == '+')){
                return false;
            }
            if (character == '+') {
                plusAppearance++;
                plusPositionAppearance =  stringIndex;
            }
            stringIndex++;
        }

        if (plusPositionAppearance > CONSTANTS.NECESSARY_POSITION_FOR_A_PLUS_IN_A_PHONE_NUMBER || plusAppearance > CONSTANTS.MAXIMAL_PLUS_POSITION_APPEARANCE){
            return false;
        }
        return true;
    }

    public static boolean isAnEmail(String verificationName){
        int appearance_around = 0, positionAppearance = 0, stringIndex = 0;
        for(char character : verificationName.toCharArray()){
            if(!Character.isLetter(character) && !Character.isDigit(character) && character != '@' && character != '_' && character != '.') {
                return false;
            }
            if (character == '@') {
                appearance_around++;
                positionAppearance = stringIndex;
            }
            stringIndex++;
        }
        if (appearance_around !=  CONSTANTS.WE_CHECK_WITH_A_ONE ||  positionAppearance ==  CONSTANTS.WE_CHECK_WITH_A_ZERO){
            return false;
        }
        return true;
    }

    public static boolean isADate(String date) {
        if (date.length() != CONSTANTS.WE_CHECK_IF_IS_TEN) {
            throw new IllegalArgumentException("The date must have type dd/mm/yyyy! ");
        }
        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6, 10));

        if (month > CONSTANTS.DECEMBER || month < CONSTANTS.JANUARY) {
            throw new IllegalArgumentException("The month must be between " + CONSTANTS.FIRST_MONTH_OF_A_YEAR + " and " + CONSTANTS.LAST_MONTH_OF_A_YEAR);
        }

        if (year < CONSTANTS.FIRST_YEAR_FOR_APPOINTMENT){
            throw new IllegalArgumentException("The Appointment year must be after " + CONSTANTS.FIRST_YEAR_FOR_APPOINTMENT);
        }
        if (month == CONSTANTS.FEBRUARY){
            if (year % CONSTANTS.BISECT_YEAR == CONSTANTS.WE_CHECK_WITH_A_ZERO && day > CONSTANTS.LAST_DAY_OF_BISECT_FEBRUARY){
                throw new IllegalArgumentException("February must be between " + CONSTANTS.FIRST_DAY_OF_A_MONTH + " and " + CONSTANTS.LAST_DAY_OF_BISECT_FEBRUARY + " days");
            } else if (day > CONSTANTS.LAST_DAY_OF_NORMAL_FEBRUARY){
                throw new IllegalArgumentException("Normal february must be between " + CONSTANTS.FIRST_DAY_OF_A_MONTH + " and " + CONSTANTS.LAST_DAY_OF_NORMAL_FEBRUARY + " days");
            }
        } else if (month == CONSTANTS.APRIL || month == CONSTANTS.JUNE || month == CONSTANTS.SEPTEMBER || month == CONSTANTS.NOVEMBER) {
            if (day > CONSTANTS.LAST_DAY_OF_30_DAYS_MONTH)
                throw new IllegalArgumentException("April, June, September and November must be between " + CONSTANTS.FIRST_DAY_OF_A_MONTH + " and " + CONSTANTS.LAST_DAY_OF_30_DAYS_MONTH + " days");
        } else {
            if (day > CONSTANTS.LAST_DAY_OF_31_DAYS_MONTH || day < CONSTANTS.FIRST_DAY_OF_A_MONTH) {
                throw new IllegalArgumentException("The day must be between " + CONSTANTS.FIRST_DAY_OF_A_MONTH + " and " + CONSTANTS.LAST_DAY_OF_31_DAYS_MONTH + " days");
            }
        }

        return date.matches("\\d{2}/\\d{2}/\\d{4}");
    }

    public static boolean isATime(String time) {
        if  (time.length() != CONSTANTS.WE_CHECK_IF_IS_FIVE) {
            throw new IllegalArgumentException("Invalid Time! This should be of type hh:mm!");
        }

        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3, 5));

        if(hour < CONSTANTS.FIRST_HOUR || hour > CONSTANTS.LAST_HOUR){
            throw new IllegalArgumentException("Invalid Time! The hour should be between " + CONSTANTS.FIRST_HOUR + " and " + CONSTANTS.LAST_HOUR + " hours");
        }

        if (minute < CONSTANTS.FIRST_MINUTE || minute > CONSTANTS.LAST_MINUTE) {
            throw new IllegalArgumentException("Invalid Time! The minutes should be between " + CONSTANTS.FIRST_MINUTE + " and " + CONSTANTS.LAST_MINUTE + " minutes");
        }

        return time.matches("\\d{2}:\\d{2}");
    }

}
