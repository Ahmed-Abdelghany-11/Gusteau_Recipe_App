package com.example.recipeapp.authentication.signUp.validation

import android.util.Patterns
import com.example.recipeapp.data.local.model.UserData
import java.util.regex.Pattern

object UserDataValidation {

    private const val namePattern = "^[A-Za-z]+$"
    private const val passwordPattern = "^(?=.*\\d)(?=.[a-z]).{8,20}$"

    private fun isValidName(name: String): Boolean {
        return name.isNotEmpty() && name.isNotBlank() && Pattern.matches(namePattern, name)
    }

    private fun isValidEmail(email: String) =
        email.isNotEmpty() && email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()


    private fun isValidPassword(pass: String) =
        pass.isNotEmpty() && pass.isNotBlank() && Pattern.matches(passwordPattern, pass)

    private fun isValidConfirmPassword(pass: String,confirmPass:String) =
        pass.isNotEmpty() && pass.isNotBlank() && confirmPass==pass



    fun validateUserData(user: UserData,confirmPass: String): MutableMap<String,String> {
        val errors = mutableMapOf<String, String>()

        if (!isValidName(user.name)) {
            errors["name"] = "Invalid name. Only letters are allowed."
        }

        if (!isValidEmail(user.email)) {
            errors["email"] = "Invalid email address."
        }

        if (!isValidPassword(user.password)) {
            errors["password"] = "Password must be between 8 and 20 characters long, and include a digit, a lowercase letter, and an uppercase letter."
        }

        if (!isValidConfirmPassword(user.password,confirmPass)) {
            errors["confirm-password"] = "Incorrect Password,Please try again"
        }

        return errors
    }



}
