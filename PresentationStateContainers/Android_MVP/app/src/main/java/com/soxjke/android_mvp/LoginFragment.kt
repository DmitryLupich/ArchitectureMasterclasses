package com.soxjke.android_mvp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    data class Props(
        val login: ValidatableField<LoginValidator.Error>,
        val password: ValidatableField<PasswordValidator.Error>,
        val loginEnabled: Boolean,
        val loginAction: () -> Unit
    )
    private var props = Props(
        ValidatableField<LoginValidator.Error>(),
        ValidatableField<PasswordValidator.Error>(),
        false,
        {}
    )

    fun render(props: Props) {
        this.props = props
        if (isResumed) { render() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as? MainActivity)?.onFragmentAttach(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onStart() {
        super.onStart()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        render()
    }

    private fun setupObservers() {
        login_field.setOnFocusChangeListener { _, hasFocus ->
            when (hasFocus) {
                true -> this.props.login.startEditing?.invoke(this.login_field.text.toString())
                false -> this.props.login.endEditing?.invoke(this.login_field.text.toString())
            }
        }
        password_field.setOnFocusChangeListener { _, hasFocus ->
            when (hasFocus) {
                true -> this.props.password.startEditing?.invoke(this.password_field.text.toString())
                false -> this.props.password.endEditing?.invoke(this.password_field.text.toString())
            }
        }
        login_field.afterTextChanged { this.props.login.changedEditing?.invoke(it) }
        password_field.afterTextChanged { this.props.password.changedEditing?.invoke(it) }
    }

    private fun render() {
        renderLoginField()
        renderPasswordField()
        renderLoginButton()
    }

    private fun renderLoginField() {
        if (!login_field.hasFocus()) { login_field.appSetText(props.login.text) }
        renderLoginErrorIfAny()
    }

    private fun renderLoginErrorIfAny() {
        when (props.login.error) {
            null -> login_field_layout.isErrorEnabled = false
            LoginValidator.Error.LoginTooShort -> login_field_layout.error = getString(R.string.login_too_short_error)
            LoginValidator.Error.LoginTooLong -> login_field_layout.error = getString(R.string.login_too_long_error)
            LoginValidator.Error.LoginNonAscii -> login_field_layout.error = getString(R.string.login_non_ascii_error)
        }
    }

    private fun renderPasswordField() {
        if (!password_field.hasFocus()) { password_field.appSetText(props.password.text) }
        renderPasswordErrorIfAny()
    }

    private fun renderPasswordErrorIfAny() {
        when (props.password.error) {
            null -> password_field_layout.isErrorEnabled = false
            PasswordValidator.Error.PasswordTooShort -> password_field_layout.error = getString(R.string.password_too_short_error)
            PasswordValidator.Error.PasswordShouldContainSpecialCharactes -> password_field_layout.error = getString(R.string.password_should_contain_special_characters_error)
        }
    }

    private fun renderLoginButton() {
        login_button.isEnabled = props.loginEnabled
        login_button.setOnClickListener { this.props.loginAction() }
    }
}
