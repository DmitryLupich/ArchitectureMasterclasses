//
//  LoginPresenter.swift
//  iOS_MVP
//
//  Created by Petro Korienev on 5/14/20.
//  Copyright © 2020 Petro Korienev. All rights reserved.
//

import Foundation

class LoginPresenter {
    typealias Props = LoginViewController.Props
    weak var controller: LoginViewController?
    
    private var login: ValidatableField<LoginValidator.Error> = .defaultValue
    private var loginValidator = LoginValidator()
    private var password: ValidatableField<PasswordValidator.Error> = .defaultValue
    private var passwordValidator = PasswordValidator()
    
    init(controller: LoginViewController) {
        self.controller = controller
        present()
    }
    
    func present() {
        controller?.render(props: makeProps())
    }
    
    private func makeProps() -> Props {
        return Props(
            login: makeLoginField(),
            password: makePasswordField(),
            loginEnabled: loginValidator.validate(string: login.text) == nil && passwordValidator.validate(string: password.text) == nil,
            loginAction: weakify(self, LoginPresenter.loginAction)
        )
    }
    
    private func makeLoginField() -> ValidatableField<LoginValidator.Error> {
        return .init(error: login.error,
                     text: login.text,
                     startEditing: weakify(self, LoginPresenter.loginStartEditing),
                     changedEditing: weakify(self, LoginPresenter.loginEditingChanged),
                     endEditing: weakify(self, LoginPresenter.loginEndEditing)
                )
    }
    
    private func loginStartEditing(_ text: String) {
        login.error = nil
        present()
    }
    
    private func loginEditingChanged(_ text: String) {
        login.text = text
        present()
    }
    
    private func loginEndEditing(_ text: String) {
        login.error = loginValidator.validate(string: text);
        present()
    }
    
    private func makePasswordField() -> ValidatableField<PasswordValidator.Error> {
        return .init(error: password.error,
                     text: password.text,
                     startEditing: weakify(self, LoginPresenter.passwordStartEditing),
                     changedEditing: weakify(self, LoginPresenter.passwordEditingChanged),
                     endEditing: weakify(self, LoginPresenter.passwordEndEditing)
                )
    }
    
    private func passwordStartEditing(_ text: String) {
        password.error = nil
        present()
    }
    
    private func passwordEditingChanged(_ text: String) {
        password.text = text
        present()
    }
    
    private func passwordEndEditing(_ text: String) {
        password.error = passwordValidator.validate(string: text);
        present()
    }
            
    private func loginAction() {
        // Call login method
        print("LoginPresenter now will call login with:\n\(login.text)\n\(password.text)")
    }
}
